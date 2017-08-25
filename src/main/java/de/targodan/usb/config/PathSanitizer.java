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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author corbatto
 */
public class PathSanitizer {
    private static Map<String, String> environment = System.getenv();
    
    protected static void overrideEnvironment(Map<String, String> environment) {
        PathSanitizer.environment = environment;
    }
    
    public static String sanitize(String path) {
        switch(OperatingSystem.getCurrent()) {
            case WINDOWS:
                return sanitizeWindows(path);
                
            case UNIX:
            case MAC:
            case SOLARIS:
                return sanitizeUnix(path);
        }
        return path;
    }
    
    protected static String sanitizeWindows(String path) {
        Matcher m = Pattern.compile("%(?<var>[a-zA-Z0-9_]+)%").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(environment.get(m.group("var")));
        }
        return path;
    }
    
    protected static String sanitizeUnix(String path) {
        path = path.replaceFirst("^~", System.getProperty("user.home"));
        Matcher m = Pattern.compile("\\$(?<var>[a-zA-Z0-9_]+)").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(environment.get(m.group("var")));
        }
        m = Pattern.compile("\\$\\{(?<var>[a-zA-Z0-9_]+)\\}").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(environment.get(m.group("var")));
        }
        return path;
    }
}
