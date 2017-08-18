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

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author corbatto
 */
public class CaseManager {
    protected final Set<Case> closedCases;
    protected final Map<Integer, Case> cases;

    public CaseManager() {
        this.closedCases = new HashSet<>();
        this.cases = new HashMap<>();
    }
    
    public Case getCase(int number) {
        return this.cases.get(number);
    }
    
    public List<Case> getCases() {
        return this.cases.values().stream()
                .sorted((i1, i2) -> i1.getOpenTime().compareTo(i2.getOpenTime()))
                .collect(Collectors.toList());
    }
    
    public List<Case> getClosedCases() {
        return this.closedCases.stream()
                .sorted((i1, i2) -> i1.getOpenTime().compareTo(i2.getOpenTime()))
                .collect(Collectors.toList());
    }
    
    public void addCase(Case c) {
        if(this.cases.containsKey(c.getNumber())) {
            throw new IllegalStateException("A case with the number " + Integer.toString(c.getNumber()) + " already exists!");
        }
        this.cases.put(c.getNumber(), c);
    }
    
    public void notifyCaseClosed(Case c) {
        Case closedCase = this.cases.remove(c.getNumber());
        if(closedCase != null) {
            this.closedCases.add(closedCase);
        }
    }
    
    public void removeClosedCasesOlderThan(LocalDateTime closeTime) {
        // TODO: Check if < 0 is correct
        this.closedCases.removeIf(item -> item.getCloseTime().compareTo(closeTime) < 0);
    }
    
    public Case lookupCaseOfClient(String clientName) {
        return this.cases.values().stream()
                .filter(elem -> elem.getClient().getIrcName().equals(clientName) || elem.getClient().getCMDRName().equals(clientName))
                .findFirst().orElse(null);
    }
}
