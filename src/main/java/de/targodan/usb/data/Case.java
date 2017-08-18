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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author corbatto
 */
public class Case {
    private boolean active;
    private final int number;
    private Client client;
    private System system;
    private final List<Rat> calls;
    private final Set<Rat> rats;
    private final List<String> notes;
    private Rat firstLimpet;
    private boolean caseRed;
    private final LocalDateTime openTime;
    private LocalDateTime closeTime;
    
    private CaseManager attachedManager;

    public Case(int number, Client client, System system, boolean caseRed) {
        this.number = number;
        this.client = client;
        this.system = system;
        this.caseRed = caseRed;
        
        this.active = true;
        this.calls = new ArrayList<>();
        this.rats = new HashSet<>();
        this.notes = new ArrayList<>();
        this.firstLimpet = null;
        this.openTime = LocalDateTime.now();
        this.closeTime = null;
        
        this.attachedManager = null;
    }
    
    public void assignRat(Rat rat) {
        if(this.rats.size() >= 3 && !this.rats.contains(rat)) {
            throw new IllegalStateException("There have already been 3 rats assigned to this case. Unassign first!");
        }
        this.rats.add(rat);
    }
    
    public void unassignRat(Rat rat) {
        this.rats.remove(rat);
    }

    public boolean isActive() {
        return this.active;
    }

    public int getNumber() {
        return this.number;
    }

    public Client getClient() {
        return this.client;
    }

    public System getSystem() {
        return this.system;
    }

    public Set<Rat> getRats() {
        return Collections.unmodifiableSet(this.rats);
    }

    public List<String> getNotes() {
        return Collections.unmodifiableList(this.notes);
    }
    
    public void addNote(String note) {
        this.notes.add(note);
    }

    public Rat getFirstLimpet() {
        return this.firstLimpet;
    }

    public boolean isCaseRed() {
        return this.caseRed;
    }

    public LocalDateTime getOpenTime() {
        return this.openTime;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public void setFirstLimpet(Rat firstLimpet) {
        this.firstLimpet = firstLimpet;
    }

    public void setCaseRed(boolean caseRed) {
        this.caseRed = caseRed;
    }

    public LocalDateTime getCloseTime() {
        return this.closeTime;
    }
    
    public void close() {
        this.closeTime = LocalDateTime.now();
        if(this.attachedManager != null) {
            this.attachedManager.notifyCaseClosed(this);
        }
    }
    
    public void attachManager(CaseManager manager) {
        this.attachedManager = manager;
    }
    
    public void addCall(Rat rat, int j) {
        rat.setJumps(j);
        this.calls.add(rat);
    }
    
    public List<Rat> getCalls() {
        return Collections.unmodifiableList(this.calls);
    }
    
    public Rat lookupAssociatedRat(String ircName) {
                // Try and find rat among assigned rats.
        return this.rats.stream()
                .filter(rat -> rat.getIrcName().equals(ircName))
                .findFirst().orElse(
                    // No assigned rat found => search in calls.
                    this.calls.stream()
                        .filter(rat -> rat.getIrcName().equals(ircName))
                        .findFirst().orElse(null)
                );
    }
}
