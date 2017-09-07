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
package de.targodan.usb.io.processing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Luca Corbatto
 */
public abstract class AbstractMarshaller implements Marshaller {
    protected final Pattern sanitizationPattern = Pattern.compile("\\.?:?\\<?[+\\-%@~]?(?<clean>.*?)\\>?:?\\.?");
    
    /**
     * Sanitizes the given username.
     * 
     * This strips any pre- and postfixes added by HexChat, like "&lt;NAME&gt;"
     * or ".:+NAME:." and so on.
     * 
     * @param name The name to be sanitized.
     * @return the sanitized username.
     */
    protected String sanitizeUsername(String name) {
        Matcher m = this.sanitizationPattern.matcher(name);
        if(!m.matches()) {
            throw new UnknownError("This should never happen.");
        }
        return m.group("clean");
    }
}
