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
package de.targodan.usb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TeeOutputStream is an OutputStream which redirects everything it receives to
 * all OutputStreams given to it during construction.
 * 
 * @author Luca Corbatto
 */
public class TeeOutputStream extends OutputStream {
    
    private final List<OutputStream> outputs;
    
    /**
     * Constructs a TeeOutputStream that will redirect any data to the given
     * OutputStreams.
     * 
     * @param outputs The target OutputStreams.
     */
    public TeeOutputStream(List<OutputStream> outputs) {
        this.outputs = new ArrayList<>(outputs);
    }
    
    /**
     * Constructs a TeeOutputStream that will redirect any data to the given
     * OutputStreams.
     * 
     * @param outputs The target OutputStreams.
     */
    public TeeOutputStream(OutputStream... outputs) {
        this.outputs = new ArrayList<>(Arrays.asList(outputs));
    }

    @Override
    public void write(int i) throws IOException {
        for(OutputStream output : this.outputs) {
            output.write(i);
        }
    }
}
