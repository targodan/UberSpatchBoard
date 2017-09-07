/*
 * The MIT License
 *
 * Copyright 2017 Luca Corbatto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights7
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
import java.io.Reader;

/**
 * Represents a filter for IRC formatting special characters.
 * 
 * @author Luca Corbatto
 */
public class IRCFormatFilteringReader extends Reader {
    private char[] buffer;
    private boolean isColorDefinition;
    private boolean isHexColor;
    private boolean wasCommaReadInColor;
    private int numColorDigitsRead;
    private final Reader reader;

    /**
     * Constructs an IRCFormatFilteringReader reading from the reader.
     * 
     * @param reader The reader to be read from.
     */
    public IRCFormatFilteringReader(Reader reader) {
        super(reader);
        this.reader = reader;
        this.buffer = new char[512];
        this.isColorDefinition = false;
        this.wasCommaReadInColor = false;
        this.numColorDigitsRead = 0;
    }
    
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if(this.buffer.length < len) {
            this.buffer = new char[len];
        }
        int size = this.reader.read(this.buffer, 0, len);
        if(size < 0) {
            return size;
        }
        
        int newSize = 0;
        for(int i = 0; i < size; ++i) {
            if(!this.isPrintable(this.buffer[i])) {
                continue;
            }
            
            cbuf[off++] = this.buffer[i];
            ++newSize;
        }
        
        return newSize;
    }
    
    /**
     * Checks if the given character should be read or not.
     * 
     * @param c The character to be checked.
     * @return true if the character should be read.
     */
    protected boolean isPrintable(char c) {
        if(this.isColorDefinition) {
            if(this.isHexColor) {
                if(this.handleHexColor(c)) {
                    return false;
                }
            } else {
                if(this.handleIndexColor(c)) {
                    return false;
                }
            }
        }
        
        if(c == (char)0x03) {
            this.isColorDefinition = true;
            this.isHexColor = false;
            this.wasCommaReadInColor = false;
            this.numColorDigitsRead = 0;
        } else if(c == (char)0x04) {
            this.isColorDefinition = true;
            this.isHexColor = true;
            this.wasCommaReadInColor = false;
            this.numColorDigitsRead = 0;
        }
        
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (Character.isWhitespace(c) || !Character.isISOControl(c)) &&
                block != null &&
                !block.equals(Character.UnicodeBlock.SPECIALS);
    }
    
    /**
     * Handles a hex color definition.
     * 
     * @param c The current character.
     * @return true if the color the current character has to be ignored.
     */
    private boolean handleHexColor(char c) {
        if(this.numColorDigitsRead > 6) {
            this.isColorDefinition = false;
            return false;
        }
        
        if(('0' <= c && c <= '9')
                || ('a' <= Character.toLowerCase(c) && Character.toLowerCase(c) <= 'f')) {
            ++this.numColorDigitsRead;
            return true;
        }
        
        this.isColorDefinition = false;
        return false;
    }
    
    /**
     * Handles an index color definition.
     * 
     * @param c The current character.
     * @return true if the color the current character has to be ignored.
     */
    private boolean handleIndexColor(char c) {
        if(this.wasCommaReadInColor) {
            if(this.numColorDigitsRead == 4) {
                this.isColorDefinition = false;
                return false;
            }
        } else {
            if(this.numColorDigitsRead == 2 && c != ',') {
                this.isColorDefinition = false;
                return false;
            }
        }
        
        if('0' <= c && c <= '9') {
            ++this.numColorDigitsRead;
            return true;
        }
        
        if(!this.wasCommaReadInColor && c == ',') {
            // This is technically not correct as a comma without digits after 
            // it should be displayed. However in the context of this software
            // a missing comma shouldn't be a problem.
            // https://modern.ircdocs.horse/formatting.html#forms-of-color-codes
            this.wasCommaReadInColor = true;
            return true;
        }
        
        this.isColorDefinition = false;
        return false;
    }

    @Override
    public void close() throws IOException {
        this.buffer = null;
        this.reader.close();
    }
}
