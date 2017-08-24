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
package de.targodan.usb;

import org.hamcrest.BaseMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 *
 * @author Luca Corbatto
 */
public class VersionTest {
    
    public VersionTest() {
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
     * Test of parse method, of class Version.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        
        {
            String versionString = "v0.1";
            Version expResult = new Version(0, 1);
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
        {
            String versionString = "4.1";
            Version expResult = new Version(4, 1);
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
        {
            String versionString = "v4.05.0";
            Version expResult = new Version(4, 5, 0);
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
        {
            String versionString = "v1.0-beta";
            Version expResult = new Version(1, 0, "beta");
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
        {
            String versionString = "11.3-rc1";
            Version expResult = new Version(11, 3, "rc1");
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
        {
            String versionString = "4.0.2-alpha";
            Version expResult = new Version(4, 0, 2, "alpha");
            Version result = Version.parse(versionString);
            assertThat(result, equalTo(expResult));
        }
    }

    /**
     * Test of toString method, of class Version.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        
        {
            Version instance = new Version(1, 0);
            String expResult = "v1.0";
            String result = instance.toString();
            assertThat(result, equalTo(expResult));
        }
        {
            Version instance = new Version(1, 4);
            String expResult = "v1.4";
            String result = instance.toString();
            assertThat(result, equalTo(expResult));
        }
        {
            Version instance = new Version(1, 0, "alpha");
            String expResult = "v1.0-alpha";
            String result = instance.toString();
            assertThat(result, equalTo(expResult));
        }
        {
            Version instance = new Version(4, 2, 9);
            String expResult = "v4.2.9";
            String result = instance.toString();
            assertThat(result, equalTo(expResult));
        }
        {
            Version instance = new Version(4, 2, 9, "rc1");
            String expResult = "v4.2.9-rc1";
            String result = instance.toString();
            assertThat(result, equalTo(expResult));
        }
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        
        {
            Version instance = new Version(1, 1);
            Version other = new Version(2, 0);
            int result = instance.compareTo(other);
            assertTrue(result < 0);
        }
    }

    /**
     * Test of compare method, of class Version.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        
        {
            Version v1 = new Version(1, 1);
            Version v2 = new Version(2, 0);
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
        {
            Version v1 = new Version(2, 0);
            Version v2 = new Version(2, 1);
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
        {
            Version v1 = new Version(2, 0, 1);
            Version v2 = new Version(2, 0, 1);
            int result = Version.compare(v1, v2);
            assertTrue(result == 0);
        }
        {
            Version v1 = new Version(2, 0, 1);
            Version v2 = new Version(2, 0, 2);
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
        {
            Version v1 = new Version(2, 0, 1, "z");
            Version v2 = new Version(2, 0, 2, "a");
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
        {
            Version v1 = new Version(2, 0, 1, "a");
            Version v2 = new Version(2, 0, 1, "z");
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
        {
            Version v1 = new Version(2, 0, 1, "alpha1");
            Version v2 = new Version(2, 0, 1, "alpha2");
            int result = Version.compare(v1, v2);
            assertTrue(result < 0);
        }
    }

    /**
     * Test of hashCode method, of class Version.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        
        {
            Version instance = new Version(1, 0);
            int expResult = new Version(1, 0).hashCode();
            int result = instance.hashCode();
            assertEquals(expResult, result);
        }
        {
            Version instance = new Version(1, 0, "a");
            int expResult = new Version(1, 0).hashCode();
            int result = instance.hashCode();
            assertNotEquals(expResult, result);
        }
    }

    /**
     * Test of equals method, of class Version.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        {
            Object obj = new Version(1, 1);
            Version instance = new Version(1, 1);
            boolean expResult = true;
            boolean result = instance.equals(obj);
            assertEquals(expResult, result);
        }
    }
}
