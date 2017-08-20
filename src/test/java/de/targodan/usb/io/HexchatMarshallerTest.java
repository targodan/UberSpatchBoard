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

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Luca Corbatto
 */
public class HexchatMarshallerTest {
    
    public HexchatMarshallerTest() {
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
     * Test of parseMonth method, of class HexchatMarshaller.
     */
    @Test
    public void testParseMonth() {
        System.out.println("parseMonth");
        
        {
            String month = "Jan";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.JANUARY;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Feb";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.FEBRUARY;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Mar";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.MARCH;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Apr";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.APRIL;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "May";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.MAY;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Jun";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.JUNE;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Jul";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.JULY;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Aug";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.AUGUST;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Sep";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.SEPTEMBER;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Oct";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.OCTOBER;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Nov";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.NOVEMBER;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
        
        {
            String month = "Dec";
            HexchatMarshaller instance = new HexchatMarshaller();
            Month expResult = Month.DECEMBER;
            Month result = instance.parseMonth(month);
            assertEquals(expResult, result);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseMonthThrows() {
        String month = "Invalid";
        HexchatMarshaller instance = new HexchatMarshaller();
        instance.parseMonth(month);
    }

    /**
     * Test of parseDateTime method, of class HexchatMarshaller.
     */
    @Test
    public void testParseDateTime() {
        System.out.println("parseDateTime");
        String dateTime = "Sep 08 23:42:02";
        HexchatMarshaller instance = new HexchatMarshaller();
        LocalDateTime expResult = LocalDateTime.of(LocalDateTime.now().getYear(), Month.SEPTEMBER, 8, 23, 42, 02);
        LocalDateTime result = instance.parseDateTime(dateTime);
        assertThat(result, equalTo(expResult));
    }

    /**
     * Test of sanitizeUsername method, of class HexchatMarshaller.
     */
    @Test
    public void testSanitizeUsername() {
        System.out.println("sanitizeUsername");
        
        {
            String name = ".:Kies:.";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
        {
            String name = ".:Kies[PC]:.";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies[PC]";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
        {
            String name = "<Kies[PC]>";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies[PC]";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
        {
            String name = "<+Kies[PC]>";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies[PC]";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
        {
            String name = "<@Kies[PC]>";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies[PC]";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
        {
            String name = "<%Kies[PC]>";
            HexchatMarshaller instance = new HexchatMarshaller();
            String expResult = "Kies[PC]";
            String result = instance.sanitizeUsername(name);
            assertThat(result, equalTo(expResult));
        }
    }

    /**
     * Test of marshall method, of class HexchatMarshaller.
     */
    @Test
    public void testMarshall() {
        System.out.println("marshall");
        
        {
            String line = "Aug 17 18:22:18 Kies	no";
            HexchatMarshaller instance = new HexchatMarshaller();
            IRCMessage expResult = new IRCMessage(LocalDateTime.of(LocalDateTime.now().getYear(), Month.AUGUST, 17, 18, 22, 18), "Kies", "#fuelrats", "no");
            IRCMessage result = instance.marshall(line);
            assertThat(result, equalTo(expResult));
        }
        
        {
            String line = "Aug 17 18:25:31 @Kies	heyo: please confirm you do not see an oxygen depletion timer in the middle right of your screen";
            HexchatMarshaller instance = new HexchatMarshaller();
            IRCMessage expResult = new IRCMessage(LocalDateTime.of(LocalDateTime.now().getYear(), Month.AUGUST, 17, 18, 25, 31), "Kies", "#fuelrats", "heyo: please confirm you do not see an oxygen depletion timer in the middle right of your screen");
            IRCMessage result = instance.marshall(line);
            assertThat(result, equalTo(expResult));
        }
        
        {
            String line = "Aug 17 18:25:08 *	Kies[PS4] has quit (Read error)";
            HexchatMarshaller instance = new HexchatMarshaller();
            IRCMessage expResult = new IRCMessage(LocalDateTime.of(LocalDateTime.now().getYear(), Month.AUGUST, 17, 18, 25, 8), "*", "#fuelrats", "Kies[PS4] has quit (Read error)");
            IRCMessage result = instance.marshall(line);
            assertThat(result, equalTo(expResult));
        }
    }
    
}
