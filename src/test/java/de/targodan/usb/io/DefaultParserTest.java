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
import de.targodan.usb.data.Client;
import de.targodan.usb.data.Platform;
import de.targodan.usb.data.Rat;
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
     * Test of parseAndHandle method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandle() {
        System.out.println("parseAndHandle");
        IRCMessage message = null;
        DefaultParser instance = new DefaultParser();
        ParseResult expResult = null;
        ParseResult result = instance.parseAndHandle(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        IRCMessage message = null;
        DefaultParser instance = new DefaultParser();
        boolean expResult = false;
        boolean result = instance.parseAndHandleCommand(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
    }

    /**
     * Test of parseAndHandleReport method, of class DefaultParser.
     */
    @Test
    public void testParseAndHandleReport() {
        System.out.println("parseAndHandleReport");
        IRCMessage message = null;
        DefaultParser instance = new DefaultParser();
        boolean expResult = false;
        boolean result = instance.parseAndHandleReport(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
