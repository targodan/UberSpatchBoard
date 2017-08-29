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

/**
 * Represents reports by rats like "fr+" and so on.
 * 
 * Note: This class has some unexpected behaviour when it comes to the equal method.
 * @see Report#equals(java.lang.Object) 
 *
 * @author Luca Corbatto
 */
public class Report {
    /**
     * Represents the type of report.
     */
    public static enum Type {
        SYS, FR, WR, BC, COMMS, INST, PARTY
    }
    
    protected final Type type;
    protected final boolean positive;

    /**
     * Constructs a Report of the given type with the given status.
     * 
     * @param type The type of the report.
     * @param positive Whether or not the report was positive ("+").
     */
    public Report(Type type, boolean positive) {
        this.type = type;
        this.positive = positive;
    }

    /**
     * Returns the type of the report.
     * 
     * @return the type of the report.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns true if the report is positive ("+").
     * 
     * @return true if the report is positive ("+").
     */
    public boolean isPositive() {
        return positive;
    }
    
    /**
     * @see Report#isPositive()
     * 
     * @return true if the report is positive ("+").
     */
    public boolean isPlus() {
        return this.isPositive();
    }
    
    
    /**
     * Returns the inversion of Report#isPlus.
     * @see Report#isPlus()
     * 
     * @return true if the report is negative ("-").
     */
    public boolean isMinus() {
        return !this.isPositive();
    }

    /**
     * Returns the hash code of this report.
     * 
     * The hash code only contains the type of the report, not the
     * positive-state. Keep this in mind when using this class in Sets.
     * 
     * @return the hash code of this report.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.type);
        return hash;
    }

    /**
     * Returns true if the type of this report is the same as the type of the 
     * given report.
     * 
     * Equals only compares the type of the reports, not the
     * positive-state. Keep this in mind when using this class in Sets.
     * 
     * @param obj The object to compare this to.
     * @return true if the type of this report is the same as the type of the 
     * given report.
     */
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
        final Report other = (Report)obj;
        if(this.type != other.type) {
            return false;
        }
        return true;
    }
}
