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

import java.util.Objects;
import java.util.Observable;

/**
 * Represents an ED System.
 * 
 * @author Luca Corbatto
 */
public class System extends Observable {
    protected final String name;
    protected boolean confirmed;

    /**
     * Constructs a System with the given name.
     * 
     * @param name The name of the system.
     */
    public System(String name) {
        this.name = name;
        this.confirmed = false;
    }

    /**
     * Returns the name of the system.
     * 
     * @return the name of the system.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if the system was confirmed.
     * 
     * "Confirmed" as in: A rat has confirmed that the client is in fact in this
     * system.
     * 
     * @return true if the system was confirmed.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets the confirmed status of this system.
     * 
     * "Confirmed" as in: A rat has confirmed that the client is in fact in this
     * system.
     * 
     * @param confirmed The confirmed status.
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + (this.confirmed ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final System other = (System) obj;
        if (this.confirmed != other.confirmed) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}
