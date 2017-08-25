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
import de.targodan.usb.data.Client;
import de.targodan.usb.data.Platform;
import de.targodan.usb.data.Rat;
import de.targodan.usb.data.Report;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 *
 * @author Luca Corbatto
 */
public class DefaultParserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock
    Handler mockHandler;
    
    @Captor
    ArgumentCaptor<Rat> ratCaptor;
    
    public DefaultParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseAndHandleRatsignal method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandleRatsignal() {
        System.out.println("parseAndHandleRatsignal");
        
        {
            LocalDateTime openTime = LocalDateTime.now();
            IRCMessage message = new IRCMessage(openTime, "MechaSqueak[BOT]", "#fuelrats", "RATSIGNAL - CMDR Filip - System: ScoutCZ (not in EDDB) - Platform: PC - O2: OK - Language: Czech (cs) (Case #2)");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleRatsignal(message);
            assertEquals(expResult, result);

            Case expCase = new Case(2, new Client("Filip", "Filip", Platform.PC, "cs"), new de.targodan.usb.data.System("ScoutCZ"), false, openTime);
            verify(mockHandler).handleNewCase(expCase);
        }
        {
            LocalDateTime openTime = LocalDateTime.now();
            IRCMessage message = new IRCMessage(openTime, "MechaSqueak[BOT]", "#fuelrats", "just some text");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = false;
            boolean result = instance.parseAndHandleRatsignal(message);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of parseAndHandleCommand method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandleCommand() {
        System.out.println("parseAndHandleCommand");
        
        
//            Command cmd = new Command(Command.Type.SOFT_ASSIGN, new String[] {});
//            verify(mockHandler).handleCommand(cmd);
        
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "go 2 Kies");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.SOFT_ASSIGN, new String[] {"2", "Kies"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "go 2 Tom Kies");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.SOFT_ASSIGN, new String[] {"2", "Tom", "Kies"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "!go 2 Kies");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.HARD_ASSIGN, new String[] {"2", "Kies"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "!go 2 Tom Kies");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.HARD_ASSIGN, new String[] {"2", "Tom", "Kies"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "!inject 2 this is some text");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.INJECT, new String[] {"2", "this is some text"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "!cmdr 2 Cpt. Obvious");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCommand(message);
            assertThat(result, equalTo(expResult));
            
            Command cmd = new Command(Command.Type.SET_CMDR_NAME, new String[] {"2", "Cpt. Obvious"});
            verify(mockHandler).handleCommand(cmd);
        }
        reset(mockHandler);
    }

    /**
     * Test of parseAndHandleCall method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandleCall() {
        System.out.println("parseAndHandleCall");
        
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq("2"));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j c2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq("2"));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j clientName");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq("clientName"));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq(""));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j +scooping #3");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq("3"));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "5j+scooping #3");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleCall(message);
            assertThat(result, equalTo(expResult));
            
            Rat rat = new Rat("Kies");
            rat.setJumps(5);
            verify(mockHandler).handleCall(ratCaptor.capture(), Mockito.eq("3"));
            assertThat(ratCaptor.getValue(), equalTo(rat));
            assertThat(ratCaptor.getValue().getJumps(), equalTo(rat.getJumps()));
        }
        reset(mockHandler);
    }

    /**
     * Test of parseAndHandleReport method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandleReport() {
        System.out.println("parseAndHandleReport");
        
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "fr+ #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            Report report = new Report(Report.Type.FR, true);
            verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "fr- client");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            Report report = new Report(Report.Type.FR, false);
            verify(mockHandler).handleReport("Kies", report, "client");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "bc+ #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            Report report = new Report(Report.Type.BC, true);
            verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "wb- #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            Report report = new Report(Report.Type.BC, false);
            verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "sys+ #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            Report report = new Report(Report.Type.SYS, true);
            verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "sys+ fr- wr+ #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            InOrder order = inOrder(mockHandler, mockHandler, mockHandler);
            Report report = new Report(Report.Type.SYS, true);
            order.verify(mockHandler).handleReport("Kies", report, "2");
            report = new Report(Report.Type.FR, false);
            order.verify(mockHandler).handleReport("Kies", report, "2");
            report = new Report(Report.Type.WR, true);
            order.verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "fr+ wr- #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            InOrder order = inOrder(mockHandler, mockHandler, mockHandler);
            Report report = new Report(Report.Type.FR, true);
            order.verify(mockHandler).handleReport("Kies", report, "2");
            report = new Report(Report.Type.WR, false);
            order.verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "fr+, wr- #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = true;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
            
            InOrder order = inOrder(mockHandler, mockHandler, mockHandler);
            Report report = new Report(Report.Type.FR, true);
            order.verify(mockHandler).handleReport("Kies", report, "2");
            report = new Report(Report.Type.WR, false);
            order.verify(mockHandler).handleReport("Kies", report, "2");
        }
        reset(mockHandler);
        {
            IRCMessage message = new IRCMessage(LocalDateTime.now(), "Kies", "#fuelrats", "just some text #2");
            DefaultParser instance = new DefaultParser();
            instance.registerHandler(mockHandler);
            boolean expResult = false;
            boolean result = instance.parseAndHandleReport(message);
            assertThat(result, equalTo(expResult));
        }
        reset(mockHandler);
    }
}
