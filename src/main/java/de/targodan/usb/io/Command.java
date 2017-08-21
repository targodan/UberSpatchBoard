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
package de.targodan.usb.io;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Luca Corbatto
 */
public class Command {
    public enum Type {
        SOFT_ASSIGN, HARD_ASSIGN,
        UNASSIGN,
        TOGGLE_CODERED,
        SET_SYSTEM,
        TOGGLE_ACTIVE,
        CLOSE,
        SET_CMDR_NAME,
        GRAB,
        MARK_DELETION,
        INJECT,
        SET_IRCNICK,
        SET_PLATFORM_PC, SET_PLATFORM_PS, SET_PLATFORM_XB,
        SUBSTITUTE,
    }
    
    protected final Type type;
    protected final String[] parameters;

    public Command(Type type, String[] parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public Type getType() {
        return this.type;
    }

    public String getParameter(int i) {
        if(i >= this.parameters.length) {
            throw new IllegalArgumentException("Requested parameter "+Integer.toString(i)+" but only "+Integer.toString(this.parameters.length)+" parameters available for command type "+this.type.toString()+".");
        }
        return this.parameters[i];
    }
    
    public int getParameterCount() {
        return this.parameters.length;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Arrays.deepHashCode(this.parameters);
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
        final Command other = (Command) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Arrays.deepEquals(this.parameters, other.parameters)) {
            return false;
        }
        return true;
    }
    
    
}
