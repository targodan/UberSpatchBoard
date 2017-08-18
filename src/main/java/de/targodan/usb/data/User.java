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
package de.targodan.usb.data;

import java.util.Objects;

/**
 *
 * @author corbatto
 */
public class User {
    protected String ircName;
    protected String cmdrName;
    protected Platform platform;
    
    public User(String ircName) {
        this.ircName = ircName;
        this.cmdrName = null;
        this.platform = null;
    }
    
    public User(String ircName, String cmdrName) {
        this.ircName = ircName;
        this.cmdrName = cmdrName;
        this.platform = null;
    }

    public User(String ircName, String cmdrName, Platform platform) {
        this.ircName = ircName;
        this.cmdrName = cmdrName;
        this.platform = platform;
    }

    public String getIrcName() {
        return ircName;
    }

    public void setIrcName(String ircName) {
        this.ircName = ircName;
    }

    public String getCmdrName() {
        return cmdrName;
    }

    public void setCmdrName(String cmdrName) {
        this.cmdrName = cmdrName;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.ircName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if(!Objects.equals(this.ircName, other.ircName)) {
            return false;
        }
        return true;
    }
}
