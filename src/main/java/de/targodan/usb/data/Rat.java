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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author corbatto
 */
public class Rat extends User {
    private static Pattern platformPattern;
    
    protected int jumps;
    protected boolean assigned;
    
    protected final Set<Report> reports;
    
    public Rat(String ircName) {
        this(ircName, null, Rat.guessPlatform(ircName));
    }
    
    public Rat(String ircName, String cmdrName) {
        this(ircName, cmdrName, Rat.guessPlatform(ircName));
    }
    
    public Rat(String ircName, String cmdrName, Platform platform) {
        super(ircName, cmdrName, platform);
        
        this.jumps = 0;
        this.reports = new HashSet<>();
        this.assigned = false;
    }
    
    private static Platform guessPlatform(String ircName) {
        if(Rat.platformPattern == null) {
            Rat.platformPattern = Pattern.compile(".*\\[.*(?<platform>(pc|ps|xb))", Pattern.CASE_INSENSITIVE);
        }
        Matcher m = Rat.platformPattern.matcher(ircName);
        if(!m.matches()) {
            return null;
        }
        switch(m.group("platform").toLowerCase()) {
            case "pc":
                return Platform.PC;
                
            case "ps":
                return Platform.PS4;
                
            case "xb":
                return Platform.XBOX;
        }
        
        return null;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public Collection<Report> getReports() {
        return Collections.unmodifiableCollection(this.reports);
    }
    
    public void insertReport(Report report) {
        if(this.reports.contains(report)) {
            this.reports.remove(report);
        }
        this.reports.add(report);
        
        this.setChanged();
        this.notifyObservers();
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
        
        this.setChanged();
        this.notifyObservers();
    }
}
