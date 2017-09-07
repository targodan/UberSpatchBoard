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

import de.targodan.usb.io.processing.IRCMessage;
import java.util.concurrent.BlockingQueue;

/**
 * Implementations of DataSource represent a data source for IRC messages.
 * 
 * @author Luca Corbatto
 */
public interface DataSource {
    /**
     * Listen should listen for data sending all IRCMessages to the given queue.
     * 
     * This method will block until stop is called.
     * 
     * @see DataSource#stop() 
     * 
     * @param output The BlockingQueue that will be receiving the IRC messages.
     */
    void listen(BlockingQueue<IRCMessage> output);
    
    /**
     * Stop should stop the listening.
     */
    void stop();
    
    /**
     * Returns the long name of the DataSource.
     * 
     * @return the long name of the DataSource.
     */
    String getName();
    
    /**
     * Returns the short name of the DataSource.
     * 
     * @return the short name of the DataSource.
     */
    String getShortName();
}
