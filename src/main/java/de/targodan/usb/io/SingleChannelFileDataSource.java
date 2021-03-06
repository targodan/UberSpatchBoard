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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * The SingleChannelFileDataSource is a ReaderDataSource that creates a reader
 * from a file.
 *
 * @author Luca Corbatto
 */
public class SingleChannelFileDataSource extends ReaderDataSource {
    private final File file;
    
    public SingleChannelFileDataSource(String channelName, String fileName, Charset charset, Marshaller marshaller) throws FileNotFoundException {
        super(marshaller, channelName);
        
        this.file = new File(fileName);
        if(!this.file.canRead()) {
            throw new IllegalArgumentException("File \""+fileName+"\" is not readable.");
        }
        this.reader = new BufferedReader(
                new IRCFormatFilteringReader(
                        new InputStreamReader(
                                new FileInputStream(this.file), charset)));
        
        this.goToEndOfReader();
    }
    
    
    @Override
    public String getName() {
        return "file://"+this.file.getPath();
    }

    @Override
    public String getShortName() {
        return "file://..."+File.separator+this.file.getName();
    }
}
