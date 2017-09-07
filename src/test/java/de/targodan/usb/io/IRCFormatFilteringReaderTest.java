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

import java.io.Reader;
import java.io.StringReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Luca Corbatto
 */
public class IRCFormatFilteringReaderTest {
    
    public IRCFormatFilteringReaderTest() {
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
     * Test of read method, of class IRCFormatFilteringReader.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        
        {
            String inputString = "";
            char[] expectedChars = "".toCharArray();
            char[] cbuf = new char[expectedChars.length];
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(new StringReader(inputString));
            int expResult = expectedChars.length;
            int result = instance.read(cbuf, 0, cbuf.length);
            
            assertThat(result, equalTo(expResult));
            assertThat(cbuf, equalTo(expectedChars));
        }
        {
            String inputString = "test 123";
            char[] expectedChars = "test 123".toCharArray();
            char[] cbuf = new char[expectedChars.length];
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(new StringReader(inputString));
            int expResult = expectedChars.length;
            int result = instance.read(cbuf, 0, cbuf.length);
            
            assertThat(result, equalTo(expResult));
            assertThat(cbuf, equalTo(expectedChars));
        }
        {
            String inputString = "test\t123";
            char[] expectedChars = "test\t123".toCharArray();
            char[] cbuf = new char[expectedChars.length];
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(new StringReader(inputString));
            int expResult = expectedChars.length;
            int result = instance.read(cbuf, 0, cbuf.length);
            
            assertThat(result, equalTo(expResult));
            assertThat(cbuf, equalTo(expectedChars));
        }
        {
            String inputString = "test\u000302\t123";
            char[] expectedChars = "test\t123".toCharArray();
            char[] cbuf = new char[inputString.length()];
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(new StringReader(inputString));
            int expResult = expectedChars.length;
            int result = instance.read(cbuf, 0, cbuf.length);
            char[] trimmedBuf = new char[result];
            System.arraycopy(cbuf, 0, trimmedBuf, 0, result);
            
            assertThat(result, equalTo(expResult));
            assertThat(trimmedBuf, equalTo(expectedChars));
        }
        {
            String inputString = "test\u000302\t123\u000302,32test";
            char[] expectedChars = "test\t123test".toCharArray();
            char[] cbuf = new char[inputString.length()];
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(new StringReader(inputString));
            int expResult = expectedChars.length;
            int result = instance.read(cbuf, 0, cbuf.length);
            char[] trimmedBuf = new char[result];
            System.arraycopy(cbuf, 0, trimmedBuf, 0, result);
            
            assertThat(result, equalTo(expResult));
            assertThat(trimmedBuf, equalTo(expectedChars));
        }
    }

    /**
     * Test of isPrintable method, of class IRCFormatFilteringReader.
     */
    @Test
    public void testIsPrintable() {
        System.out.println("isPrintable");
        
        {
            char c = ' ';
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(mock(Reader.class));
            boolean expResult = true;
            boolean result = instance.isPrintable(c);
            assertEquals(expResult, result);
        }
        {
            char c = '4';
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(mock(Reader.class));
            boolean expResult = true;
            boolean result = instance.isPrintable(c);
            assertEquals(expResult, result);
        }
        {
            char c = '\t';
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(mock(Reader.class));
            boolean expResult = true;
            boolean result = instance.isPrintable(c);
            assertEquals(expResult, result);
        }
        {
            char c = (char)0x01;
            IRCFormatFilteringReader instance = new IRCFormatFilteringReader(mock(Reader.class));
            boolean expResult = false;
            boolean result = instance.isPrintable(c);
            assertEquals(expResult, result);
        }
    }
}
