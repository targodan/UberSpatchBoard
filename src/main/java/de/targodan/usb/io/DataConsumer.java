/*
 * The MIT License
 *
 * Copyright 2017 .
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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Corbatto
 */
public class DataConsumer {
    private final BlockingQueue<IRCMessage> queue;
    private final Parser parser;
    private final AtomicBoolean run;
    private final AtomicBoolean done;
    private final List<DataSource> dataSources;
    
    public DataConsumer(Parser parser) {
        this.queue = new ArrayBlockingQueue<>(8);
        this.parser = parser;
        this.run = new AtomicBoolean(false);
        this.done = new AtomicBoolean(false);
        this.dataSources = new CopyOnWriteArrayList<>();
    }
    
    public void addDataSource(DataSource ds) {
        this.dataSources.add(ds);
        if(this.run.get() && !this.done.get()) {
            new Thread(() -> {
                ds.listen(this.queue);
            }).start();
        }
    }
    
    public void start() {
        this.done.set(false);
        this.run.set(true);
        
        this.dataSources.forEach(ds -> {
            new Thread(() -> {
                ds.listen(this.queue);
            }).start();
        });
        
        while(this.run.get()) {
            IRCMessage msg;
            try {
                msg = this.queue.take();
            } catch(Exception ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
                continue;
            }
            if(msg == null) {
                continue;
            }
            
            ParseResult result = this.parser.parseAndHandle(msg);
            Logger.getLogger(SingleChannelFileDataSource.class.getName())
                    .log(Level.INFO, "Parsed message.", result);
            Logger.getLogger(SingleChannelFileDataSource.class.getName())
                    .log(Level.FINE, "Parsed message.", msg);
        }
        this.done.set(true);
    }
    
    public void stop() {
        this.dataSources.forEach(ds -> {
            ds.stop();
        });
        
        this.run.set(false);
        this.queue.add(null);
        
        while(!this.done.get()) {
            try {
                this.done.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }
}
