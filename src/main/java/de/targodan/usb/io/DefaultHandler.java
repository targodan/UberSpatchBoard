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
package de.targodan.usb.io;

import de.targodan.usb.data.Case;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.data.Platform;
import de.targodan.usb.data.Rat;
import de.targodan.usb.data.Report;
import de.targodan.usb.data.System;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DefaultHandler represents the default implementation of the Handler
 * interface.
 *
 * @author Luca Corbatto
 */
public class DefaultHandler implements Handler {
    protected Case latestCase;
    protected Map<Platform, Case> latestCases;
    protected CaseManager cm;
    
    /**
     * Constructs a DefaultHandler.
     */
    public DefaultHandler() {
        this.latestCases = new HashMap<>();
    }

    @Override
    public void registerCaseManager(CaseManager cm) {
        this.cm = cm;
    }

    @Override
    public void handleNewCase(Case c) {
        if(this.cm == null) {
            throw new IllegalStateException("Call RegisterCaseManager before calling any of the handle* functions.");
        }
        
        this.cm.addCase(c);
        
        this.latestCase = c;
        this.latestCases.put(c.getClient().getPlatform(), c);
    }

    @Override
    public void handleCommand(Command cmd) {
        if(this.cm == null) {
            throw new IllegalStateException("Call RegisterCaseManager before calling any of the handle* functions.");
        }
        
        switch(cmd.type) {
            case CLOSE:
                this.handleCommandClose(cmd);
                break;
                
            case HARD_ASSIGN:
                this.handleCommandHardAssign(cmd);
                break;
                
            case GRAB:
                this.handleCommandGrab(cmd);
                break;
                
            case INJECT:
                this.handleCommandInject(cmd);
                break;
                
            case MARK_DELETION:
                this.handleCommandMarkDeletion(cmd);
                break;
                
            case SET_CMDR_NAME:
                this.handleCommandSetCMDRName(cmd);
                break;
                
            case SET_IRCNICK:
                this.handleCommandIRCNick(cmd);
                break;
                
            case SET_PLATFORM_PC:
                this.handleCommandSetPlatformPC(cmd);
                break;
                
            case SET_PLATFORM_PS:
                this.handleCommandSetPlatformPS4(cmd);
                break;
                
            case SET_PLATFORM_XB:
                this.handleCommandSetPlatformXBox(cmd);
                break;
                
            case SET_SYSTEM:
                this.handleCommandSetSystem(cmd);
                break;
                
            case SOFT_ASSIGN:
                this.handleCommandSoftAssign(cmd);
                break;
                
            case SUBSTITUTE:
                this.handleCommandSubstitute(cmd);
                break;
                
            case TOGGLE_ACTIVE:
                this.handleCommandToggleActive(cmd);
                break;
                
            case TOGGLE_CODERED:
                this.handleCommandToggleCodered(cmd);
                break;
                
            case UNASSIGN:
                this.handleCommandUnassign(cmd);
                break;
        }
    }

    @Override
    public void handleCall(Rat rat, String caseIdentifier) {
        if(this.cm == null) {
            throw new IllegalStateException("Call RegisterCaseManager before calling any of the handle* functions.");
        }
        
        Case c = this.lookupCase(caseIdentifier, rat);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Recieved call but couldn't find related case.", rat);
            return;
        }
        c.addCall(rat);
    }

    @Override
    public void handleReport(String ratIrcName, Report report, String caseIdentifier) {
        if(this.cm == null) {
            throw new IllegalStateException("Call RegisterCaseManager before calling any of the handle* functions.");
        }
        
        Case c = this.lookupCase(caseIdentifier, new Rat(ratIrcName));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Recieved report but couldn't find related case.");
            return;
        }
        Rat rat = c.lookupAssociatedRat(ratIrcName);
        if(rat == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Recieved report but couldn't find associated rat.");
            return;
        }
        rat.insertReport(report);
    }
    
    /**
     * Returns the determined Case.
     * 
     * First this method will try to find a case by the caseIdentifier. The
     * caseIdentifier can be either the case number or the name of the client.
     * If no case can be found by caseIdentifier it will try to find an open
     * case with the given rat already assigned. If that does not work either
     * the case will be guessed the latest added case of the same platform as
     * the rat. If all fails null will be returned.
     * 
     * @param caseIdentifier The case identifier to search for, can be null.
     * @param rat The rat to search for, can be null.
     * @return 
     */
    protected Case lookupCase(String caseIdentifier, Rat rat) {
        if(caseIdentifier != null) {
            try {
                int caseID = Integer.valueOf(caseIdentifier.replace("#", ""));
                return this.cm.getCase(caseID);
            } catch(Exception ex) {}

            // Was no numeral, so we'll assume it's the clients name.
            Case c = this.cm.lookupCaseOfClient(caseIdentifier);
            if(c != null) {
                return c;
            }
        }
        
        if(rat != null) {
            // Couldn't find the client name, try to find the rat.
            Case c = this.cm.lookupCaseWithRat(rat);
            if(c != null) {
                return c;
            }
            
            // Couldn't find the rat, try to guess the case via platform
            if(rat.getPlatform() != null) {
                Case latestPlatformCase = this.latestCases.get(rat.getPlatform());
                if(latestPlatformCase != null && latestPlatformCase.isActive()) {
                    return latestPlatformCase;
                }
            }
        }
        
        // Rat platform unknown or no latest case for that platform, just use the latest case...
        if(this.latestCase != null && this.latestCase.isActive()) {
            return this.latestCase;
        }
        
        return null;
    }
    
    /**
     * Handles the !close command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandClose(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        if(cmd.getParameterCount() >= 2) {
            String ratName = cmd.getParameter(1);
            Rat rat = c.lookupAssociatedRat(ratName);
            if(rat == null) {
                rat = new Rat(ratName);
            }
            c.setFirstLimpet(rat);
        }
        c.close();
    }
    
    /**
     * Handles the !go command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandHardAssign(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        for(int i = 1; i < cmd.getParameterCount(); ++i) {
            String ratName = cmd.getParameter(i);
            Rat rat = c.lookupAssociatedRat(ratName);
            if(rat == null) {
                rat = new Rat(ratName);
            }
            rat.setAssigned(true);
            c.assignRat(rat);
        }
    }
    
    /**
     * Handles the !grab command.
     * 
     * This is not really supported yet, so it will just write a warning to the log and return.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandGrab(Command cmd) {
        Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Grab not yet supported.", cmd);
    }
    
    /**
     * Handles the !inject command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandInject(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.addNote(cmd.getParameter(1));
    }
    
    /**
     * Handles the !md command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandMarkDeletion(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.close();
    }
    
    /**
     * Handles the !cmdr command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSetCMDRName(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setCMDRName(cmd.getParameter(1));
    }
    
    /**
     * Handles the !ircnick command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandIRCNick(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setIRCName(cmd.getParameter(1));
    }
    
    /**
     * Handles the !pc command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSetPlatformPC(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.PC);
    }
    
    /**
     * Handles the !ps4 command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSetPlatformPS4(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.PS4);
    }
    
    /**
     * Handles the !xb command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSetPlatformXBox(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.XBOX);
    }
    
    /**
     * Handles the !sys command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSetSystem(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setSystem(new System(cmd.getParameter(1)));
    }
    
    /**
     * Handles soft assigning rats by using "go # rat1 rat2 ..." (same as !go
     * command without the '!'.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSoftAssign(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        for(int i = 1; i < cmd.getParameterCount(); ++i) {
            String ratName = cmd.getParameter(i);
            Rat rat = c.lookupAssociatedRat(ratName);
            if(rat == null) {
                rat = new Rat(ratName);
            }
            c.assignRat(rat);
        }
    }
    
    /**
     * Handles the !sub command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandSubstitute(Command cmd) {
        Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Substitute not yet properly supported. Redirecting to inject.", cmd);
        this.handleCommandInject(new Command(Command.Type.INJECT, new String[]{cmd.getParameter(0), cmd.getParameter(2)}));
    }
    
    /**
     * Handles the !active command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandToggleActive(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setActive(!c.isActive());
    }
    
    /**
     * Handles the !cr command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandToggleCodered(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setCodeRed(!c.isCodeRed());
    }
    
    /**
     * Handles the !unassign command.
     * 
     * @param cmd The command to be handled.
     */
    protected void handleCommandUnassign(Command cmd) {
        Case c = this.lookupCase(cmd.getParameter(0), null);
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        Rat rat = c.lookupAssociatedRat(cmd.getParameter(1));
        c.unassignRat(rat);
    }
}
