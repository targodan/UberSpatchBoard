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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Corbatto
 */
public class ReaderDataSource implements DataSource {
    protected Marshaller marshaller;
    protected final AtomicBoolean run;
    protected final AtomicBoolean done;
    protected long readPause;
    protected String overrideChannelName;
    
    protected BufferedReader reader;
    
    public ReaderDataSource(BufferedReader reader, Marshaller marshaller) {
        this.marshaller = marshaller;
        this.run = new AtomicBoolean(true);
        this.done = new AtomicBoolean(false);
        this.readPause = 200; // milliseconds
        this.overrideChannelName = null;
        
        this.reader = reader;
        
        this.goToEndOfReader();
    }
    
    public ReaderDataSource(BufferedReader reader, Marshaller marshaller, String overrideChannelName) {
        this(reader, marshaller);
        
        this.goToEndOfReader();
    }
    
    protected final void goToEndOfReader() {
        if(this.reader == null) {
            return;
        }
        
        // Go to EOF
        String line = "";
        while(line != null) {
            try {
                line = this.reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    @Override
    public void listen(BlockingQueue<IRCMessage> output) {
        if(this.marshaller == null) {
            throw new IllegalStateException("RegisterMarshaller needs to be called before Listen.");
        }
        
        this.done.set(false);
        this.run.set(true);
        
        String line;
        while(this.run.get()) {
            try {
                Thread.sleep(this.readPause);
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            
            try {
                line = this.reader.readLine();
            } catch (IOException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
                break;
            }
            if(line == null) {
                continue;
            }
            
            IRCMessage msg;
            try {
                msg = this.marshaller.marshall(line);
            } catch(Exception ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
                continue;
            }
            if(msg == null) {
                continue;
            }
            // Fix channel name if necessary
            if(this.overrideChannelName != null && !msg.getChannel().equals(this.overrideChannelName)) {
                msg = new IRCMessage(msg.getTimestamp(), msg.getSender(), this.overrideChannelName, msg.getContent());
            }
            
            while(!output.offer(msg)) {
                while(output.remainingCapacity() == 0) {
                    try {
                        Thread.currentThread().wait(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ReaderDataSource.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        this.done.set(true);
    }

    @Override
    public void stop() {
        this.run.set(false);
        
        while(!this.done.get()) {
            try {
                this.done.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleChannelFileDataSource.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        
        try {
            this.reader.close();
        } catch (IOException ex) {
            Logger.getLogger(SingleChannelFileDataSource.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
