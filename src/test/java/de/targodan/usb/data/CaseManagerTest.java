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
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Luca Corbatto
 */
public class CaseManagerTest {
    private Observer observer;
    
    public CaseManagerTest() {
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
    
    private CaseManager createWithObserver() {
        CaseManager instance = new CaseManager();
        this.observer = mock(Observer.class);
        instance.addObserver(this.observer);
        return instance;
    }

    /**
     * Test of getCase method, of class CaseManager.
     */
    @Test
    public void testGetCase() {
        java.lang.System.out.println("getCase");
        
        {
            CaseManager instance = this.createWithObserver();
            Case expResult = null;
            instance.cases.put(2, expResult);
            Case result = instance.getCase(2);
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
        {
            CaseManager instance = this.createWithObserver();
            Case expResult = new Case(2, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
            instance.cases.put(2, expResult);
            Case result = instance.getCase(2);
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
        {
            CaseManager instance = this.createWithObserver();
            Case expResult = new Case(2, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
            instance.cases.put(4, new Case(4, new Client("123",  "321", Platform.PC, "fr"), new System("test2"), true));
            instance.cases.put(2, expResult);
            Case result = instance.getCase(2);
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
        {
            CaseManager instance = this.createWithObserver();
            Case result = instance.getCase(0);
            Case expResult = null;
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
        {
            CaseManager instance = this.createWithObserver();
            instance.cases.put(2, null);
            instance.getCase(0);
            verify(this.observer, never()).update(any(), any());
        }
    }

    /**
     * Test of getOpenCases method, of class CaseManager.
     */
    @Test
    public void testGetOpenCases() {
        java.lang.System.out.println("getOpenCases");
        
        {
            Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true, LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 0));
            Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true, LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 1));
            Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true, LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 2));
            CaseManager instance = this.createWithObserver();
            instance.cases.put(c1.getNumber(), c1);
            instance.cases.put(c2.getNumber(), c2);
            instance.cases.put(c3.getNumber(), c3);
            List<Case> expResult = new ArrayList<>();
            expResult.add(c1);
            expResult.add(c2);
            expResult.add(c3);
            List<Case> result = instance.getOpenCases();
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
    }

    /**
     * Test of getClosedCases method, of class CaseManager.
     */
    @Test
    public void testGetClosedCases() {
        java.lang.System.out.println("getClosedCases");
        
        {
            Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
            c1.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 0));
            Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
            c2.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 1));
            Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
            c3.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 2));
            CaseManager instance = this.createWithObserver();
            instance.closedCases.add(c3);
            instance.closedCases.add(c1);
            instance.closedCases.add(c2);
            List<Case> expResult = new ArrayList<>();
            expResult.add(c1);
            expResult.add(c2);
            expResult.add(c3);
            List<Case> result = instance.getClosedCases();
            assertThat(result, equalTo(expResult));
            verify(this.observer, never()).update(any(), any());
        }
    }

    /**
     * Test of addCase method, of class CaseManager.
     */
    @Test
    public void testAddCase() {
        java.lang.System.out.println("addCase");
        
        {
            Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
            Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
            Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
            CaseManager instance = this.createWithObserver();
            instance.addCase(c1);
            instance.addCase(c2);
            instance.addCase(c3);
            Map<Integer, Case> expResult = new HashMap<>();
            expResult.put(c1.getNumber(), c1);
            expResult.put(c2.getNumber(), c2);
            expResult.put(c3.getNumber(), c3);
            assertThat(instance.cases, equalTo(expResult));
            verify(this.observer, times(3)).update(instance, null);
        }
    }

    /**
     * Test of notifyCaseClosed method, of class CaseManager.
     */
    @Test
    public void testNotifyCaseClosed() {
        java.lang.System.out.println("notifyCaseClosed");
        
        {
            Case c = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
            CaseManager instance = this.createWithObserver();
            instance.notifyCaseClosed(c);
            verify(this.observer).update(instance, null);
        }
        {
            Case c = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
            CaseManager instance = this.createWithObserver();
            instance.addCase(c);
            reset(this.observer);
            instance.notifyCaseClosed(c);
            Set<Case> result = instance.closedCases;
            Set<Case> expectedClosedCases = new HashSet<>();
            expectedClosedCases.add(c);
            assertThat(instance.cases.size(), equalTo(0));
            assertThat(result, equalTo(expectedClosedCases));
            verify(this.observer).update(instance, null);
        }
    }

    /**
     * Test of removeClosedCasesOlderThan method, of class CaseManager.
     */
    @Test
    public void testRemoveClosedCasesOlderThan() {
        java.lang.System.out.println("removeClosedCasesOlderThan");
        
        {
            Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
            c1.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 0));
            Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
            c2.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 1));
            Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
            c3.close(LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 2));
            CaseManager instance = this.createWithObserver();
            instance.closedCases.add(c3);
            instance.closedCases.add(c1);
            instance.closedCases.add(c2);
            LocalDateTime closeTime = LocalDateTime.of(2017, Month.SEPTEMBER, 1, 12, 1);
            instance.removeClosedCasesOlderThan(closeTime);
            Set<Case> expResult = new HashSet<>();
            expResult.add(c2);
            expResult.add(c3);
            Set<Case> result = instance.closedCases;
            assertThat(result, equalTo(expResult));
            verify(this.observer).update(instance, null);
        }
    }

    /**
     * Test of lookupCaseOfClient method, of class CaseManager.
     */
    @Test
    public void testLookupCaseOfClient() {
        java.lang.System.out.println("lookupCaseOfClient");
        
        Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
        Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
        Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
        CaseManager instance = this.createWithObserver();
        instance.addCase(c1);
        instance.addCase(c2);
        instance.addCase(c3);
        reset(this.observer);
        
        {
            String clientName = "123";
            Case expResult = c1;
            Case result = instance.lookupCaseOfClient(clientName);
            assertThat(result, equalTo(expResult));
        }
        {
            String clientName = "asdf";
            Case expResult = c2;
            Case result = instance.lookupCaseOfClient(clientName);
            assertThat(result, equalTo(expResult));
        }
        {
            String clientName = "fdsa";
            Case expResult = c2;
            Case result = instance.lookupCaseOfClient(clientName);
            assertThat(result, equalTo(expResult));
        }
        {
            String clientName = "as";
            Case expResult = null;
            Case result = instance.lookupCaseOfClient(clientName);
            assertThat(result, equalTo(expResult));
        }
        {
            String clientName = "test";
            Case expResult = null;
            Case result = instance.lookupCaseOfClient(clientName);
            assertThat(result, equalTo(expResult));
        }
        verifyNoMoreInteractions(this.observer);
    }

    /**
     * Test of lookupCaseWithRat method, of class CaseManager.
     */
    @Test
    public void testLookupCaseWithRat() {
        java.lang.System.out.println("lookupCaseWithRat");
        
        Case c1 = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
        c1.addCall(new Rat("testRat1"));
        c1.addCall(new Rat("testRat2"));
        Case c2 = new Case(1, new Client("asdf",  "fdsa", Platform.PC, "de"), new System("test"), true);
        c2.assignRat(new Rat("testRat3"));
        Case c3 = new Case(5, new Client("22",  "33", Platform.XBOX, "gb"), new System("test3"), true);
        c3.addCall(new Rat("testRat4"));
        c3.assignRat(new Rat("testRat5"));
        CaseManager instance = this.createWithObserver();
        instance.addCase(c1);
        instance.addCase(c2);
        instance.addCase(c3);
        reset(this.observer);
        
        {
            Rat rat = new Rat("testRat1");
            Case expResult = c1;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        {
            Rat rat = new Rat("testRat2");
            Case expResult = c1;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        {
            Rat rat = new Rat("testRat3");
            Case expResult = c2;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        {
            Rat rat = new Rat("testRat4");
            Case expResult = c3;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        {
            Rat rat = new Rat("testRat5");
            Case expResult = c3;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        {
            Rat rat = new Rat("notarat");
            Case expResult = null;
            Case result = instance.lookupCaseWithRat(rat);
            assertThat(result, equalTo(expResult));
        }
        verifyNoMoreInteractions(this.observer);
    }

    /**
     * Test of update method, of class CaseManager.
     */
    @Test
    public void testUpdate() {
        java.lang.System.out.println("update");
        
        {
            Observable o = null;
            Object arg = null;
            CaseManager instance = this.createWithObserver();
            instance.update(o, arg);
            verify(this.observer, never()).update(any(), any());
        }
        {
            Case obs = new Case(4, new Client("123",  "321", Platform.PC, "ru"), new System("test2"), true);
            Object arg = "testArg";
            CaseManager instance = this.createWithObserver();
            instance.cases.put(obs.getNumber(), obs);
            instance.update(obs, arg);
            verify(this.observer).update(instance, obs);
        }
    }
}
