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

import de.targodan.usb.io.processing.Marshaller;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * IRCClient represents a supported IRC client.
 * 
 * The implementations of this interface should be able to detect whether the
 * client is installed and ideally where the fuelrats log file is located.
 * 
 * @author corbatto
 */
public interface IRCClient {
    /**
     * IsInstalled should check whether or not the client is installed.
     * 
     * @return true if the IRC client is installed.
     */
    boolean isInstalled();
    
    /**
     * GetFuelratsLogfilePath should return the path for the fuelrats log file.
     * 
     * @return the filepath of the default logfile of the #fuelrats channel.
     */
    String getFuelratsLogfilePath();
    
    /**
     * GetMarshaller should return a Marshaller that is able to marshal the log
     * files of this client.
     * 
     * @return the marshaller to be used for marshalling.
     */
    Marshaller getMarshaller();
    
    /**
     * GetName returns the name of the client.
     * 
     * @return the name of the client.
     */
    String getName();
    
    /**
     * GetSupportedOperatingSystems returns a set of operating systems that are
     * supported by the IRC client.
     * 
     * @return a set of operating systems that are supported by the IRC client.
     */
    Set<OperatingSystem> getSupportedOperatingSystems();
    
    /**
     * GetDefaultLogFileEncoding returns the default charset for log files.
     * @return the default charset for log files.
     */
    Charset getDefaultLogFileEncoding();
}
