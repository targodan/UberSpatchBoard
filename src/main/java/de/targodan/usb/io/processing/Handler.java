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
package de.targodan.usb.io.processing;

import de.targodan.usb.data.Case;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.data.Rat;
import de.targodan.usb.data.Report;

/**
 * Implementations of Handler handle commands calls and reports calling the
 * appropriate methods on the registered CaseManager.
 * 
 * @author Luca Corbatto
 */
public interface Handler {
    /**
     * Registers a CaseManager.
     * 
     * This CaseManager will be used to handle the commands calls and reports
     * that come in.
     * 
     * @param cm The CaseManager to be registered.
     */
    void registerCaseManager(CaseManager cm);
    
    /**
     * Handles the creation of a new Case.
     * 
     * @param c The new Case to be added to the CaseManager.
     */
    void handleNewCase(Case c);
    
    /**
     * Handles a command.
     * 
     * @param cmd The command to be handled.
     */
    void handleCommand(Command cmd);
    
    /**
     * Handles a jump call.
     * 
     * @param rat The jump call to be handled.
     * @param caseIdentifier The identifier of the case the jump call is associated with.
     */
    void handleCall(Rat rat, String caseIdentifier);
    
    /**
     * Handles a report.
     * 
     * @param ratIrcName The IRC name of the rat that reported.
     * @param report The report.
     * @param caseIdentifier The identifier of the Case the report is associated with.
     */
    void handleReport(String ratIrcName, Report report, String caseIdentifier);
}
