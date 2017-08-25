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
package de.targodan.usb.config;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
 * @author corbatto
 */
public class PathSanitizerTest {
    Map<String, String> env;
    
    public PathSanitizerTest() {
        env = new TreeMap<>();
        env.put("var1", "CONTENT1");
        env.put("var2", "CONTENT2");
        env.put("longer_name", "longer_content");
    }

    @Before
    public void setUp() throws Exception {
        PathSanitizer.overrideEnvironment(env);
    }

    @After
    public void tearDown() throws Exception {
        PathSanitizer.overrideEnvironment(System.getenv());
    }

    /**
     * Test of sanitizeWindows method, of class PathSanitizer.
     */
    @Test
    public void testSanitizeWindows() {
        System.out.println("sanitizeWindows");
        
        {
            String path = "%var1%/test";
            String expResult = env.get("var1")+"/test";
            String result = PathSanitizer.sanitizeWindows(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "%var2%/test";
            String expResult = env.get("var2")+"/test";
            String result = PathSanitizer.sanitizeWindows(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "%longer_name%/test";
            String expResult = env.get("longer_name")+"/test";
            String result = PathSanitizer.sanitizeWindows(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "%longer_name%withtextafter/test";
            String expResult = env.get("longer_name")+"withtextafter/test";
            String result = PathSanitizer.sanitizeWindows(path);
            assertThat(result, equalTo(expResult));
        }
    }

    /**
     * Test of sanitizeUnix method, of class PathSanitizer.
     */
    @Test
    public void testSanitizeUnix() {
        System.out.println("sanitizeUnix");
        
        {
            String path = "~/test";
            String expResult = System.getProperty("user.home")+"/test";
            String result = PathSanitizer.sanitizeUnix(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "$var1/test";
            String expResult = env.get("var1")+"/test";
            String result = PathSanitizer.sanitizeUnix(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "${var2}/test";
            String expResult = env.get("var2")+"/test";
            String result = PathSanitizer.sanitizeUnix(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "$longer_name/test";
            String expResult = env.get("longer_name")+"/test";
            String result = PathSanitizer.sanitizeUnix(path);
            assertThat(result, equalTo(expResult));
        }
        {
            String path = "${longer_name}withtextafter/test";
            String expResult = env.get("longer_name")+"withtextafter/test";
            String result = PathSanitizer.sanitizeUnix(path);
            assertThat(result, equalTo(expResult));
        }
    }
    
}
