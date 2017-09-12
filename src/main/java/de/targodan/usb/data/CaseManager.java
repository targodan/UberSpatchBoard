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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * The CaseManager keeps track of and manages cases.
 * 
 * @author Luca Corbatto
 */
public class CaseManager extends Observable implements Observer {
    protected final Set<Case> closedCases;
    protected final Map<Integer, Case> cases;

    /**
     * Constructs a new CaseManager.
     */
    public CaseManager() {
        this.closedCases = Collections.synchronizedSet(new HashSet<>());
        this.cases = Collections.synchronizedMap(new HashMap<>());
    }
    
    /**
     * Returns a case with the given case number.
     * 
     * @param number The number of the case as given by MechaSqueak.
     * @return The requested case or null.
     */
    public Case getCase(int number) {
        return this.cases.get(number);
    }
    
    /**
     * Returns all open cases.
     * 
     * @return all open cases.
     */
    public List<Case> getOpenCases() {
        return this.cases.values().stream()
                .sorted((i1, i2) -> i1.getOpenTime().compareTo(i2.getOpenTime()))
                .collect(Collectors.toList());
    }
    
    /**
     * Returns all closed cases.
     * 
     * @return all closed cases.
     */
    public List<Case> getClosedCases() {
        return this.closedCases.stream()
                .sorted((i1, i2) -> i1.getCloseTime().compareTo(i2.getCloseTime()))
                .collect(Collectors.toList());
    }
    
    /**
     * Adds a case.
     * 
     * @param c The case to be added.
     */
    public void addCase(Case c) {
        if(this.cases.containsKey(c.getNumber())) {
            throw new IllegalStateException("A case with the number " + Integer.toString(c.getNumber()) + " already exists!");
        }
        c.attachManager(this);
        this.cases.put(c.getNumber(), c);
        
        c.addObserver(this);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Notifies the CaseManager that the case c was closed.
     * 
     * You typically don't need to call this as Case.close() does this
     * automatically.
     * 
     * @param c The case that was just closed.
     */
    public void notifyCaseClosed(Case c) {
        Case closedCase = this.cases.remove(c.getNumber());
        if(closedCase != null) {
            this.closedCases.add(closedCase);
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Removes closed cases that have been closed before closeTime.
     * 
     * @param closeTime The timestamp of closing the case.
     */
    public void removeClosedCasesOlderThan(LocalDateTime closeTime) {
        if(this.closedCases.removeIf(item -> item.getCloseTime().isBefore(closeTime))) {
            this.setChanged();
            this.notifyObservers();
        }
    }
    
    /**
     * Returns the Case where the clients IRC or CMDR name are equal to the
     * given name or null if no such case exists.
     * 
     * @param clientName The name of the client.
     * @return the Case where the clients IRC or CMDR name are equal to the
     * given name or null if no such case exists.
     */
    public Case lookupCaseOfClient(String clientName) {
        return this.cases.values().stream()
                .filter(elem -> elem.getClient().getIRCName().equals(clientName) || elem.getClient().getCMDRName().equals(clientName))
                .findFirst().orElse(null);
    }
    
    /**
     * Returns the Case where a Rat with the same IRC name as the given rat has
     * been assigned or at least has called for the case.
     * 
     * @param rat The rat to search for.
     * @return the Case where a Rat with the same IRC name as the given rat has
     * been assigned or at least has called for the case.
     */
    public Case lookupCaseWithRat(Rat rat) {
        Case c = this.cases.values().stream()
                .filter(
                        elem -> elem.getRats().stream().anyMatch(r -> r.getIRCName().equals(rat.getIRCName()))
                )
                .findFirst().orElse(null);
        if(c == null) {
            c = this.cases.values().stream()
                .filter(
                        elem -> elem.getCalls().stream().anyMatch(r -> r.getIRCName().equals(rat.getIRCName()))
                )
                .findFirst().orElse(null);
        }
        return c;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof Case)) {
            return;
        }
        Case c = (Case)o;
        
        if(this.cases.containsValue(c) || this.closedCases.contains(c)) {
            this.setChanged();
            this.notifyObservers(c);
        } else {
            c.deleteObserver(this);
        }
    }
}
