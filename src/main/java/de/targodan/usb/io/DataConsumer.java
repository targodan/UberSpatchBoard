/*
 * The MIT License
 *
 * Copyright 2017 Luca Corbatto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.targodan.usb.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * DataConsumer consumes, parses and handles messages from arbitrarily many
 * DataSources.
 * 
 * Each DataSource will be started in its own thread.
 * 
 * @author Luca Corbatto
 */
public class DataConsumer extends Observable {
    private final BlockingQueue<IRCMessage> queue;
    private final Parser parser;
    private final AtomicBoolean run;
    private final AtomicBoolean done;
    private final List<DataSource> dataSources;
    private final List<Thread> threads;
    
    /**
     * Constructs a DataConsumer with a Parser.
     * 
     * @param parser The parser to be used for parsing and handling of messages.
     */
    public DataConsumer(Parser parser) {
        this.queue = new ArrayBlockingQueue<>(8);
        this.parser = parser;
        this.run = new AtomicBoolean(false);
        this.done = new AtomicBoolean(false);
        this.dataSources = new CopyOnWriteArrayList<>();
        this.threads = new ArrayList<>();
    }
    
    /**
     * Creates and starts a new thread that will listen on the DataSource.
     * 
     * @param ds The DataSource that will be listened to in the created thread.
     */
    private void createAndStartThread(DataSource ds) {
        Thread t = new Thread(() -> {
            ds.listen(this.queue);
        });
        t.setName("DataSourceThread_"+ds.getShortName());
        this.threads.add(t);
        t.start();
    }
    
    /**
     * Adds a DataSource.
     * 
     * If the DataConsumer was started already a thread will be created and
     * started listening on the given DataSource.
     * 
     * @see DataConsumer#createAndStartThread(de.targodan.usb.io.DataSource) 
     * 
     * @param ds The DataSource to be added.
     */
    public void addDataSource(DataSource ds) {
        this.dataSources.add(ds);
        if(this.run.get() && !this.done.get()) {
            this.createAndStartThread(ds);
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Removes the given DataSource from the consumer stopping any started
     * threads.
     * 
     * @param ds The DataSource to be removed.
     */
    public void removeDataSource(DataSource ds) {
        int indexOfDS = IntStream.range(0, this.dataSources.size())
                .filter(i -> this.dataSources.get(i) == ds)
                .findFirst().orElse(-1);
        if(indexOfDS == -1) {
            return;
        }
        
        this.dataSources.remove(indexOfDS);
        
        ds.stop();
        try {
            this.threads.get(indexOfDS).join();
        } catch (InterruptedException ex) {
            Logger.getLogger(DataConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.threads.remove(indexOfDS);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns an unmodifiable list of the contained DataSources.
     * 
     * @return an unmodifiable list of the contained DataSources.
     */
    public List<DataSource> getDataSources() {
        return Collections.unmodifiableList(this.dataSources);
    }
    
    /**
     * Returns ture if the DataConsumer is still running.
     * 
     * @return ture if the DataConsumer is still running.
     */
    public boolean isRunning() {
        return this.run.get();
    }
    
    /**
     * Starts the DataConsumer, starting any attached DataSources in a thread each.
     * 
     * This will block until you call stop().
     */
    public void start() {
        this.done.set(false);
        this.run.set(true);
        
        this.dataSources.forEach(ds -> {
            this.createAndStartThread(ds);
        });
        
        this.setChanged();
        this.notifyObservers();
        
        IRCMessage msg;
        while(this.run.get()) {
            try {
                msg = this.queue.poll(100, TimeUnit.MILLISECONDS);
                if(msg == null) {
                    continue;
                }
                
                ParseResult result = this.parser.parseAndHandle(msg);
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.INFO, "Parsed message.", result);
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.FINE, "Parsed message.", msg);
            } catch(Exception ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        this.done.set(true);
    }
    
    /**
     * Stops the DataConsumer and all attached DataSources.
     */
    public void stop() {
        this.dataSources.forEach(ds -> {
            ds.stop();
        });
        
        this.threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DataConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.run.set(false);
        
        while(!this.done.get()) {
            try {
                Thread t = Thread.currentThread();
                synchronized(t) {
                    t.wait(50);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        
        this.setChanged();
        this.notifyObservers();
    }
}
