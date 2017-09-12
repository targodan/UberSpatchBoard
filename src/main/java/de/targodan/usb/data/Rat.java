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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rat is a User with additional information about the jumps called and if they
 * have been assigned to a case.
 * 
 * @author Luca Corbatto
 */
public class Rat extends User {
    private static Pattern platformPattern;
    
    protected int jumps;
    protected boolean assigned;
    
    protected final Set<Report> reports;
    
    /**
     * Construct a Rat with only an IRC name and the platform guessed based on
     * the IRC name.
     * 
     * @see Rat#guessPlatform(java.lang.String)
     * 
     * @param ircName The IRC name of the rat.
     */
    public Rat(String ircName) {
        this(ircName, null, Rat.guessPlatform(ircName));
    }
    
    /**
     * Construct a Rat with an IRC name and a commander name, the platform is
     * guessed based on the IRC name.
     * 
     * @see Rat#guessPlatform(java.lang.String)
     * 
     * @param ircName The IRC name of the rat.
     * @param cmdrName The CMDR name of the rat.
     */
    public Rat(String ircName, String cmdrName) {
        this(ircName, cmdrName, Rat.guessPlatform(ircName));
    }
    
    
    /**
     * Construct a Rat with an IRC name, a commander name and the platform.
     * 
     * @param ircName The IRC name of the rat.
     * @param cmdrName The CMDR name of the rat.
     * @param platform The platform of the rat.
     */
    public Rat(String ircName, String cmdrName, Platform platform) {
        super(ircName, cmdrName, platform);
        
        this.jumps = -1;
        this.reports = new HashSet<>();
        this.assigned = false;
    }
    
    /**
     * Guesses the platform based on the IRC name.
     * 
     * It looks for postfixes like "[PC]", "|PC" or similar. 
     * 
     * @param ircName The IRC name of the rat.
     * @return a guess on which platform that rat is.
     */
    private static Platform guessPlatform(String ircName) {
        if(Rat.platformPattern == null) {
            Rat.platformPattern = Pattern.compile(".*[\\[|].*(?<platform>(pc|ps|xb))", Pattern.CASE_INSENSITIVE);
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

    /**
     * Returns the number of jumps the Rat has left to reach the client.
     * 
     * @return the number of jumps the Rat has left to reach the client.
     */
    public int getJumps() {
        return jumps;
    }

    /**
     * Sets the number of jumps the Rat has left to reach the client.
     * 
     * @param jumps The amount of jumps to be set.
     */
    public void setJumps(int jumps) {
        this.jumps = jumps;
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns the reports this Rat has made so far.
     * 
     * @return the reports this Rat has made so far.
     */
    public Collection<Report> getReports() {
        return Collections.unmodifiableCollection(this.reports);
    }
    
    /**
     * Inserts a new Report overwriting reports of the same kind.
     * @see Report#equals(java.lang.Object)
     * 
     * @param report The report to be inserted.
     */
    public void insertReport(Report report) {
        if(this.reports.contains(report)) {
            this.reports.remove(report);
        }
        this.reports.add(report);
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Returns true if the Rat has been assigned to a case.
     * 
     * @return true if the Rat has been assigned to a case.
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * Sets the assigned status of this rat.
     * 
     * @param assigned True if the rat is supposed to be assigned.
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
        
        this.setChanged();
        this.notifyObservers();
    }
}
