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
 *
 * @author Luca Corbatto
 */
public class DefaultHandler implements Handler {
    protected Case latestCase;
    protected Map<Platform, Case> latestCases;
    protected CaseManager cm;
    
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
                
            case HARD_ASSIGN:
                this.handleCommandHardAssign(cmd);
                
            case GRAB:
                this.handleCommandGrab(cmd);
                
            case INJECT:
                this.handleCommandInject(cmd);
                
            case MARK_DELETION:
                this.handleCommandMarkDeletion(cmd);
                
            case SET_CMDR_NAME:
                this.handleCommandSetCMDRName(cmd);
                
            case SET_IRCNICK:
                this.handleCommandIRCNick(cmd);
                
            case SET_PLATFORM_PC:
                this.handleCommandSetPlatformPC(cmd);
                
            case SET_PLATFORM_PS:
                this.handleCommandSetPlatformPS4(cmd);
                
            case SET_PLATFORM_XB:
                this.handleCommandSetPlatformXBox(cmd);
                
            case SET_SYSTEM:
                this.handleCommandSetSystem(cmd);
                
            case SOFT_ASSIGN:
                this.handleCommandSoftAssign(cmd);
                
            case SUBSTITUTE:
                this.handleCommandSubstitute(cmd);
                
            case TOGGLE_ACTIVE:
                this.handleCommandToggleActive(cmd);
                
            case TOGGLE_CODERED:
                this.handleCommandToggleCodered(cmd);
                
            case UNASSIGN:
                this.handleCommandUnassign(cmd);
        }
    }

    @Override
    public void handleCall(Rat rat, String caseIdentifier) {
        if(this.cm == null) {
            throw new IllegalStateException("Call RegisterCaseManager before calling any of the handle* functions.");
        }
        
        Case c = this.lookupCase(rat, caseIdentifier);
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
        
        Case c = this.lookupCase(new Rat(ratIrcName), caseIdentifier);
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
    
    protected Case lookupCase(Rat rat, String caseIdentifier) {
        try {
            int caseID = Integer.valueOf(caseIdentifier);
            return this.cm.getCase(caseID);
        } catch(Exception ex) {}
        
        // Was no numeral, so we'll assume it's the clients name.
        Case c = this.cm.lookupCaseOfClient(caseIdentifier);
        if(c != null) {
            return c;
        }
        
        // Couldn't find the client name, try to guess the case via platform
        if(rat != null && rat.getPlatform() != null) {
            Case latestPlatformCase = this.latestCases.get(rat.getPlatform());
            if(latestPlatformCase != null && latestPlatformCase.isActive()) {
                return latestPlatformCase;
            }
        }
        
        // Rat platform unknown or no latest case for that platform, just use the latest case...
        if(this.latestCase != null && this.latestCase.isActive()) {
            return this.latestCase;
        }
        
        return null;
    }
    
    void handleCommandClose(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
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
    
    void handleCommandHardAssign(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
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
    
    void handleCommandGrab(Command cmd) {
        Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Grab not yet supported.", cmd);
    }
    
    void handleCommandInject(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.addNote(cmd.getParameter(1));
    }
    
    void handleCommandMarkDeletion(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.close();
    }
    
    void handleCommandSetCMDRName(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setCMDRName(cmd.getParameter(1));
    }
    
    void handleCommandIRCNick(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setIRCName(cmd.getParameter(1));
    }
    
    void handleCommandSetPlatformPC(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.PC);
    }
    
    void handleCommandSetPlatformPS4(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.PS4);
    }
    
    void handleCommandSetPlatformXBox(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.getClient().setPlatform(Platform.XBOX);
    }
    
    void handleCommandSetSystem(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setSystem(new System(cmd.getParameter(1)));
    }
    
    void handleCommandSoftAssign(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
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
    
    void handleCommandSubstitute(Command cmd) {
        Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Substitute not yet properly supported. Redirecting to inject.", cmd);
        this.handleCommandInject(new Command(Command.Type.INJECT, new String[]{cmd.getParameter(0), cmd.getParameter(2)}));
    }
    
    void handleCommandToggleActive(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setActive(!c.isActive());
    }
    
    void handleCommandToggleCodered(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        c.setCaseRed(!c.isCodeRed());
    }
    
    void handleCommandUnassign(Command cmd) {
        Case c = this.lookupCase(null, cmd.getParameter(0));
        if(c == null) {
            Logger.getLogger(DefaultHandler.class.getName()).log(Level.WARNING, "Could not find case for command.", cmd);
            return;
        }
        Rat rat = c.lookupAssociatedRat(cmd.getParameter(1));
        c.unassignRat(rat);
    }
}
