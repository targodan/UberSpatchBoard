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
import java.util.Observer;
import java.util.Set;

/**
 * The Case class represents a fuelrats case.
 * 
 * @author Luca Corbatto
 */
public class Case extends Observable implements Observer {
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

    /**
     * Constructs a Case with number, client, system and codeRed status.
     * 
     * The opening time will be set to the current time.
     * 
     * @param number Number of the case as given by MechaSqueak.
     * @param client The client information.
     * @param system The system as reported by the client or rats.
     * @param codeRed Whether or not the case is a code red.
     */
    public Case(int number, Client client, System system, boolean codeRed) {
        this(number, client, system, codeRed, LocalDateTime.now());
    }

    /**
     * Constructs a Case with number, client, system and codeRed status.
     * 
     * @param number Number of the case as given by MechaSqueak.
     * @param client The client information.
     * @param system The system as reported by the client or rats.
     * @param codeRed Whether or not the case is a code red.
     * @param openTime The time at which the case was opened.
     */
    public Case(int number, Client client, System system, boolean codeRed, LocalDateTime openTime) {
        this.number = number;
        this.client = client;
        this.client.addObserver(this);
        this.system = system;
        this.system.addObserver(this);
        this.codeRed = codeRed;
        
        this.active = true;
        this.calls = new ArrayList<>();
        this.rats = new HashSet<>();
        this.notes = new ArrayList<>();
        this.firstLimpet = null;
        this.openTime = openTime;
        this.closeTime = null;
        
        this.attachedManager = null;
    }
    
    /**
     * Assigns a rat to a case.
     * 
     * @param rat The rat to be assigned.
     * @throws IllegalStateException If there are already 3 rats assigned.
     */
    public void assignRat(Rat rat) {
        if(this.rats.size() >= 3 && !this.rats.contains(rat)) {
            throw new IllegalStateException("There have already been 3 rats assigned to this case. Unassign first!");
        }
        this.rats.add(rat);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Unassigns a rat.
     * 
     * @param rat The rat to be unassigned.
     */
    public void unassignRat(Rat rat) {
        this.rats.remove(rat);
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Returns true if the case is active.
     * 
     * @return true iff. the case is active.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns the case number.
     * 
     * @return the case number.
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Returns the client.
     * 
     * @return the client.
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * Returns the system.
     * 
     * @return the system.
     */
    public System getSystem() {
        return this.system;
    }

    /**
     * Returns the assigned rats.
     * 
     * @return the assigned rats.
     */
    public Set<Rat> getRats() {
        return Collections.unmodifiableSet(this.rats);
    }

    /**
     * Returns the notes.
     * 
     * @return the notes.
     */
    public List<String> getNotes() {
        return Collections.unmodifiableList(this.notes);
    }
    
    /**
     * Adds a note.
     * 
     * @param note The note to be added.
     */
    public void addNote(String note) {
        this.notes.add(note);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Overwrites all notes with the given ones.
     * 
     * @param notes The notes to overwrite the current ones.
     */
    public void setNotes(String[] notes) {
        this.notes.clear();
        this.notes.addAll(Arrays.asList(notes));
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Returns the rat who got first limpet.
     * 
     * @return the rat who got first limpet.
     */
    public Rat getFirstLimpet() {
        return this.firstLimpet;
    }

    /**
     * Returns true if the case is a code red.
     * @return true if the case is a code red.
     */
    public boolean isCodeRed() {
        return this.codeRed;
    }

    /**
     * Returns the opening time.
     * 
     * @return the opening time.
     */
    public LocalDateTime getOpenTime() {
        return this.openTime;
    }

    /**
     * Sets the cases active state.
     * 
     * @param active True if the case should be set to active.
     */
    public void setActive(boolean active) {
        this.active = active;
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Sets the client.
     * 
     * @param client The client to be set.
     */
    public void setClient(Client client) {
        if(this.client != null) {
            this.client.deleteObserver(this);
        }
        this.client = client;
        this.client.addObserver(this);
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Sets the system.
     * 
     * @param system The system to be set.
     */
    public void setSystem(System system) {
        if(this.system != null) {
            this.system.deleteObserver(this);
        }
        this.system = system;
        this.system.addObserver(this);
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Sets the rat who first limpeted.
     * 
     * @param firstLimpet The rat to be set as first limpet.
     */
    public void setFirstLimpet(Rat firstLimpet) {
        if(this.firstLimpet != null) {
            this.firstLimpet.deleteObserver(this);
        }
        this.firstLimpet = firstLimpet;
        this.firstLimpet.addObserver(this);
        
        this.notifyObservers();
    }

    /**
     * Sets the cases code red state.
     * 
     * @param caseRed True if the case is supposed to be a code red.
     */
    public void setCodeRed(boolean caseRed) {
        this.codeRed = caseRed;
        
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Returns the time the case was closed or null if the case is still open.
     * 
     * @return the time the case was closed or null if the case is still open.
     */
    public LocalDateTime getCloseTime() {
        return this.closeTime;
    }
    
    /**
     * Closes the case with now as the closing time.
     */
    public void close() {
        this.closeTime = LocalDateTime.now();
        if(this.attachedManager != null) {
            this.attachedManager.notifyCaseClosed(this);
        }
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns true if the case is closed.
     * 
     * @return the time the case was closed or null if the case is still open.
     */
    public boolean isClosed() {
        return this.closeTime != null;
    }
    
    public void attachManager(CaseManager manager) {
        this.attachedManager = manager;
    }
    
    /**
     * Adds a jump call of a rat.
     * 
     * Rats that call won't be automatically assigned. However if the rat was
     * already assigned they won't be added again but instead their jumps will
     * be updated.
     * 
     * @param rat The call to be added.
     */
    public void addCall(Rat rat) {
        this.calls.add(rat);
        rat.addObserver(this);
        
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
    
    /**
     * Returns all unassigned rats that have called for this case.
     * 
     * @return all unassigned rats that have called for this case.
     */
    public List<Rat> getCalls() {
        return Collections.unmodifiableList(this.calls);
    }
    
    /**
     * Searches for a rat with the given IRC name.
     * 
     * Preferably an assigned rat is returned. If no assigned rat with this name
     * can be found an unassigned rat that has called jumps will be returned. If
     * no such rat can be found either null is returned.
     * 
     * @param ircName The irc name to be set.
     * @return A rat with the given name or null if no such rat can be found.
     */
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

    @Override
    public void update(Observable o, Object arg) {
        this.setChanged();
        this.notifyObservers(o);
    }
}
