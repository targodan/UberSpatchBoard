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

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation of Marshaller marshalls lines as formatted by the mIRC
 * IRC client.
 * 
 * @author Luca Corbatto
 */
public class mIRCMarshaller extends AbstractMarshaller {
    private static final Pattern linePattern = Pattern.compile("^(?<time>\\S*)\\s(?<username>\\S+)\\s(?<content>.+)$");
    
    @Override
    public IRCMessage marshall(Object o) {
        if(!(o instanceof String)) {
            Logger.getLogger(mIRCMarshaller.class.getName()).log(Level.WARNING, "Expected String got {0}.", o.getClass().getName());
            return null;
        }
        
        String line = (String)o;
        Matcher m = mIRCMarshaller.linePattern.matcher(line);
        
        if(!m.matches()) {
            return null;
        }
        
        String timestampStr = m.group("time");
        String usernameStr = this.sanitizeUsername(m.group("username"));
        String contentStr = m.group("content");
        
        return new IRCMessage(this.parseTimestamp(timestampStr), usernameStr, "", contentStr);
    }
    
    /**
     * Parse the timestamp from string.
     * 
     * Since the time format of mIRC seems to be customizable and the timestamp
     * isn't particularly important we'll just return null for now.
     * 
     * @param timestamp The timestamp to be parsed.
     * @return null
     */
    private LocalDateTime parseTimestamp(String timestamp) {
        return null;
    }
}
