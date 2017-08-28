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
 * PathSanitizer is a helper to clean up paths and resolve environment variables
 * that may contained in a path.
 * 
 * @author Luca Corbatto
 */
public class PathSanitizer {
    private static Map<String, String> environment = System.getenv();
    
    /**
     * OverrideEnvironment overrides the default environment.
     * 
     * This is only used for tests.
     * 
     * @param environment 
     */
    protected static void overrideEnvironment(Map<String, String> environment) {
        PathSanitizer.environment = environment;
    }
    
    /**
     * Sanitize cleans up a given path, replacing any environment variables
     * contained in the native formatting.
     * 
     * @param path
     * @return The cleaned up path.
     */
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
    
    /**
     * SanitizeReplaceString escapes the given text to be interpreted
     * literally by a regex.
     * 
     * @param text
     * @return 
     */
    protected static String sanitizeReplaceString(String text) {
        return text.replace("\\", "\\\\")
                .replace("$", "\\$");
    }
    
    /**
     * SanitizeWindows sanitizes the given path replacing windows environment
     * variables like "%APPDATA%".
     * 
     * @param path
     * @return 
     */
    protected static String sanitizeWindows(String path) {
        Matcher m = Pattern.compile("%(?<var>[a-zA-Z0-9_]+)%").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(sanitizeReplaceString(environment.get(m.group("var"))));
        }
        return path;
    }
    
    /**
     * SanitizeWindows sanitizes the given path replacing linux environment
     * variables like "$HOME" or "~".
     * 
     * @param path
     * @return 
     */
    protected static String sanitizeUnix(String path) {
        path = path.replaceFirst("^~", System.getProperty("user.home"));
        Matcher m = Pattern.compile("\\$(?<var>[a-zA-Z0-9_]+)").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(sanitizeReplaceString(environment.get(m.group("var"))));
        }
        m = Pattern.compile("\\$\\{(?<var>[a-zA-Z0-9_]+)\\}").matcher(path);
        while(m.find()) {
            path = m.replaceFirst(sanitizeReplaceString(environment.get(m.group("var"))));
        }
        return path;
    }
}
