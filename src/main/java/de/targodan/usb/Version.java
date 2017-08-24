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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author corbatto
 */
public class Version {
    public static final Pattern VERSION_STRING_PATTERN = Pattern.compile("^v?(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<bugfix>\\d+))?(?:-(?<addition>\\w+))?$");
    
    private int major;
    private int minor;
    private int bugfix;
    private String addition;
    
    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
        this.bugfix = 0;
        this.addition = null;
    }
    
    public Version(int major, int minor, String addition) {
        this(major, minor);
        this.addition = addition;
    }
    
    public Version(int major, int minor, int bugfix) {
        this(major, minor);
        this.bugfix = bugfix;
    }
    
    public Version(int major, int minor, int bugfix, String addition) {
        this(major, minor, bugfix);
        this.addition = addition;
    }
    
    public static Version parse(String versionString) {
        Version version = new Version(0, 0);
        
        Matcher m = Version.VERSION_STRING_PATTERN.matcher(versionString);
        if(!m.matches()) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        
        String majorString = m.group("major");
        String minorString = m.group("minor");
        String bugfixString = m.group("bugfix");
        String additionString = m.group("addition");
        
        if(majorString == null || majorString.equals("")) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        try {
            version.major = Integer.parseInt(majorString);
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Not a valid version string!", ex);
        }
        
        if(minorString == null || minorString.equals("")) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        try {
            version.minor = Integer.parseInt(minorString);
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Not a valid version string!", ex);
        }
        
        version.bugfix = 0;
        if(bugfixString != null && !bugfixString.equals("")) {
            try {
                version.bugfix = Integer.parseInt(bugfixString);
            } catch(NumberFormatException ex) {}
        }
        
        version.addition = null;
        if(additionString != null && !additionString.equals("")) {
            version.addition = additionString;
        }
        
        return version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("v");
        sb.append(this.major);
        sb.append(".");
        sb.append(this.minor);
        if(this.bugfix > 0) {
            sb.append(".");
            sb.append(this.bugfix);
        }
        if(this.addition != null) {
            sb.append("-");
            sb.append(this.addition);
        }
        
        return sb.toString();
    }
    
    public int compareTo(Version other) {
        return Version.compare(this, other);
    }
    
    public static int compare(Version v1, Version v2) {
        if(v1.major != v2.major) {
            return v1.major - v2.major;
        }
        if(v1.minor != v2.minor) {
            return v1.minor - v2.minor;
        }
        if(v1.bugfix != v2.bugfix) {
            return v1.bugfix - v2.bugfix;
        }
        if(v1.addition == null && v2.addition != null) {
            return 1;
        }
        if(v1.addition != null && v2.addition == null) {
            return -1;
        }
        if(v1.addition == null && v2.addition == null) {
            return 0;
        }
        return v1.addition.compareTo(v2.addition);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.major;
        hash = 97 * hash + this.minor;
        hash = 97 * hash + this.bugfix;
        hash = 97 * hash + Objects.hashCode(this.addition);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        if (this.major != other.major) {
            return false;
        }
        if (this.minor != other.minor) {
            return false;
        }
        if (this.bugfix != other.bugfix) {
            return false;
        }
        if (!Objects.equals(this.addition, other.addition)) {
            return false;
        }
        return true;
    }
}
