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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Set;

/**
 *
 * @author corbatto
 */
public class Case extends Observable {
    private boolean active;
    private final int number;
    private Client client;
    private System system;
    private final List<Rat> calls;
    private final Set<Rat> rats;
    private final List<String> notes;
    private Rat firstLimpet;
    private boolean codeRed;
    private final LocalDateTime openTime;
    private LocalDateTime closeTime;
    
    private CaseManager attachedManager;

    public Case(int number, Client client, System system, boolean caseRed) {
        this(number, client, system, caseRed, LocalDateTime.now());
    }

    public Case(int number, Client client, System system, boolean caseRed, LocalDateTime openTime) {
        this.number = number;
        this.client = client;
        this.system = system;
        this.codeRed = caseRed;
        
        this.active = true;
        this.calls = new ArrayList<>();
        this.rats = new HashSet<>();
        this.notes = new ArrayList<>();
        this.firstLimpet = null;
        this.openTime = openTime;
        this.closeTime = null;
        
        this.attachedManager = null;
    }
    
    public void assignRat(Rat rat) {
        if(this.rats.size() >= 3 && !this.rats.contains(rat)) {
            throw new IllegalStateException("There have already been 3 rats assigned to this case. Unassign first!");
        }
        this.rats.add(rat);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public void unassignRat(Rat rat) {
        this.rats.remove(rat);
        
        this.setChanged();
        this.notifyObservers();
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
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public void setNotes(String[] note) {
        this.notes.clear();
        this.notes.addAll(Arrays.asList(note));
        
        this.setChanged();
        this.notifyObservers();
    }

    public Rat getFirstLimpet() {
        return this.firstLimpet;
    }

    public boolean isCodeRed() {
        return this.codeRed;
    }

    public LocalDateTime getOpenTime() {
        return this.openTime;
    }

    public void setActive(boolean active) {
        this.active = active;
        
        this.setChanged();
        this.notifyObservers();
    }

    public void setClient(Client client) {
        this.client = client;
        
        this.setChanged();
        this.notifyObservers();
    }

    public void setSystem(System system) {
        this.system = system;
        
        this.setChanged();
        this.notifyObservers();
    }

    public void setFirstLimpet(Rat firstLimpet) {
        this.firstLimpet = firstLimpet;
        
        this.notifyObservers();
    }

    public void setCaseRed(boolean caseRed) {
        this.codeRed = caseRed;
        
        this.setChanged();
        this.notifyObservers();
    }

    public LocalDateTime getCloseTime() {
        return this.closeTime;
    }
    
    public void close() {
        this.closeTime = LocalDateTime.now();
        if(this.attachedManager != null) {
            this.attachedManager.notifyCaseClosed(this);
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public boolean isClosed() {
        return this.closeTime != null;
    }
    
    public void attachManager(CaseManager manager) {
        this.attachedManager = manager;
    }
    
    public void addCall(Rat rat) {
        this.calls.add(rat);
        
        try {
            // If that rat was already assigned update its jumps.
            this.rats.stream()
                    .filter(elem -> elem.equals(rat))
                    .findFirst().get()
                    .setJumps(rat.getJumps());
        } catch(Exception ex) {}
        
        this.setChanged();
        this.notifyObservers();
    }
    
    public List<Rat> getCalls() {
        return Collections.unmodifiableList(this.calls);
    }
    
    public Rat lookupAssociatedRat(String ircName) {
                // Try and find rat among assigned rats.
        return this.rats.stream()
                .filter(rat -> rat.getIRCName().equals(ircName))
                .findFirst().orElse(
                    // No assigned rat found => search in calls.
                    this.calls.stream()
                        .filter(rat -> rat.getIRCName().equals(ircName))
                        .findFirst().orElse(null)
                );
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (this.active ? 1 : 0);
        hash = 43 * hash + this.number;
        hash = 43 * hash + Objects.hashCode(this.client);
        hash = 43 * hash + Objects.hashCode(this.system);
        hash = 43 * hash + Objects.hashCode(this.calls);
        hash = 43 * hash + Objects.hashCode(this.rats);
        hash = 43 * hash + Objects.hashCode(this.notes);
        hash = 43 * hash + Objects.hashCode(this.firstLimpet);
        hash = 43 * hash + (this.codeRed ? 1 : 0);
        hash = 43 * hash + Objects.hashCode(this.openTime);
        hash = 43 * hash + Objects.hashCode(this.closeTime);
        hash = 43 * hash + Objects.hashCode(this.attachedManager);
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
        final Case other = (Case) obj;
        if (this.active != other.active) {
            return false;
        }
        if (this.number != other.number) {
            return false;
        }
        if (this.codeRed != other.codeRed) {
            return false;
        }
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.system, other.system)) {
            return false;
        }
        if (!Objects.equals(this.calls, other.calls)) {
            return false;
        }
        if (!Objects.equals(this.rats, other.rats)) {
            return false;
        }
        if (!Objects.equals(this.notes, other.notes)) {
            return false;
        }
        if (!Objects.equals(this.firstLimpet, other.firstLimpet)) {
            return false;
        }
        if (!Objects.equals(this.openTime, other.openTime)) {
            return false;
        }
        if (!Objects.equals(this.closeTime, other.closeTime)) {
            return false;
        }
        if (!Objects.equals(this.attachedManager, other.attachedManager)) {
            return false;
        }
        return true;
    }
    
    
}
