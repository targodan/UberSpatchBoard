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
package de.targodan.usb.data;

/**
 * Client is a User with a language.
 * 
 * @author Luca Corbatto
 */
public class Client extends User {
    protected String language;
    
    /**
     * Construct a Client with the given names, platform and language.
     * 
     * @param ircName
     * @param cmdrName
     * @param platform
     * @param language Two character language code.
     */
    public Client(String ircName, String cmdrName, Platform platform, String language) {
        super(ircName, cmdrName, platform);
        
        this.language = language.toUpperCase();
    }

    /**
     * Returns the language of the client.
     * 
     * @return Two character upper case language code.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language of the client.
     * 
     * @param language Two character language code.
     */
    public void setLanguage(String language) {
        this.language = language.toUpperCase();
        
        this.setChanged();
        this.notifyObservers();
    }
}
