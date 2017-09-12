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
package de.targodan.usb;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.IOUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Version represents a version as defined in semantic versioning
 * <a href="http://semver.org/">SemVer</a> providing methods to parse and compare versions.
 * 
 * @author Luca Corbatto
 */
public class Version {
    public static final Pattern VERSION_STRING_PATTERN = Pattern.compile("^v?(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<bugfix>\\d+))?(?:-(?<suffix>[a-zA-Z.0-9]+))?$");
    
    private int major;
    private int minor;
    private int patch;
    private String suffix;
    private int suffixVersion;
    
    /**
     * Constructs a version with just major and minor version.
     * 
     * @param major Major version component.
     * @param minor Minor version component.
     */
    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
        this.patch = 0;
        this.suffix = null;
        this.suffixVersion = 0;
    }
    
    /**
     * Constructs a version with just major and minor version and an additional
     * suffix.
     * 
     * @param major Major version component.
     * @param minor Minor version component.
     * @param suffix Version suffix.
     */
    public Version(int major, int minor, String suffix) {
        this(major, minor);
        this.splitAndSetSuffix(suffix);
    }
    
    /**
     * Constructs a version with just major, minor and patch version.
     * 
     * @param major Major version component.
     * @param minor Minor version component.
     * @param patch Patch version component.
     */
    public Version(int major, int minor, int patch) {
        this(major, minor);
        this.patch = patch;
    }
    
    /**
     * Constructs a version with just major, minor and patch version and a suffix.
     * 
     * @param major Major version component.
     * @param minor Minor version component.
     * @param patch Patch version component.
     * @param suffix Version suffix.
     */
    public Version(int major, int minor, int patch, String suffix) {
        this(major, minor, patch);
        this.splitAndSetSuffix(suffix);
    }
    
    /**
     * SplitAndSetSuffix splits the suffix on a '.' trying to interpret the second
     * part as a number.
     * 
     * @param suffix The suffix to be analysed and set.
     */
    private void splitAndSetSuffix(String suffix) {
        String[] parts = suffix.split("\\.");
        if(parts.length > 2) {
            throw new IllegalArgumentException("The version suffix must not contain more than one '.'!");
        }
        this.suffix = parts[0];
        if(parts.length == 2) {
            try {
                this.suffixVersion = Integer.parseUnsignedInt(parts[1]);
            } catch(NumberFormatException ex) {
                this.suffixVersion = 0;
                this.suffix = suffix;
            }
        }
    }
    
    /**
     * Parse parses the given version string to create a Version instance.
     * 
     * The format is "[v]MAJOR.MINOR[.PATCH][-SUFFIX]" with the elements
     * in brackets being optional.
     * 
     * @param versionString The string representation of the version.
     * @return The parsed Version
     * @throws IllegalArgumentException If the string could not be parsed.
     */
    public static Version parse(String versionString) {
        Version version = new Version(0, 0);
        
        Matcher m = Version.VERSION_STRING_PATTERN.matcher(versionString);
        if(!m.matches()) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        
        String majorString = m.group("major");
        String minorString = m.group("minor");
        String bugfixString = m.group("bugfix");
        String suffixString = m.group("suffix");
        
        if(majorString == null || majorString.equals("")) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        try {
            version.major = Integer.parseUnsignedInt(majorString);
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Not a valid version string!", ex);
        }
        
        if(minorString == null || minorString.equals("")) {
            throw new IllegalArgumentException("Not a valid version string!");
        }
        try {
            version.minor = Integer.parseUnsignedInt(minorString);
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Not a valid version string!", ex);
        }
        
        version.patch = 0;
        if(bugfixString != null && !bugfixString.equals("")) {
            try {
                version.patch = Integer.parseUnsignedInt(bugfixString);
            } catch(NumberFormatException ex) {}
        }
        
        version.suffix = null;
        if(suffixString != null && !suffixString.equals("")) {
            version.splitAndSetSuffix(suffixString);
        }
        
        return version;
    }
    
    /**
     * This method will look up the latest released version on GitHub.
     * 
     * It will block until the result is in. NOTE: pre-releases will be ignored.
     * 
     * @return The latest version released on GitHub.
     */
    public static Version getLatestVersionFromGithub() {
        String url = "https://api.github.com/repos/targodan/UberSpatchBoard/releases/latest";
        HttpRequestFactory fac = new NetHttpTransport().createRequestFactory();
        try {
            HttpRequest request = fac.buildGetRequest(new GenericUrl(url));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType("application/json");
            headers.setAcceptEncoding("application/vnd.github.v3+json");
            request.setHeaders(headers);
            
            HttpResponse response = request.execute();
            
            ByteArrayOutputStream jsonStream = new ByteArrayOutputStream();
            IOUtils.copy(response.getContent(), jsonStream);
            
            String jsonContent;
            if(response.getContentEncoding() == null) {
                jsonContent = jsonStream.toString("utf-8");
            } else {
                jsonContent = jsonStream.toString(response.getContentEncoding());
            }
            
            JSONObject jsonObj = new JSONObject(jsonContent);
            String version = jsonObj.getString("tag_name");
            
            return Version.parse(version);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Version.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * ToString returns a string representation of the Version.
     * 
     * The format is "vMAJOR.MINOR[.PATCH][-SUFFIX]" with the elements
     * in brackets being omitted if empty.
     * 
     * @return The string representation of this version.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("v");
        sb.append(this.major);
        sb.append(".");
        sb.append(this.minor);
        if(this.patch > 0) {
            sb.append(".");
            sb.append(this.patch);
        }
        if(this.suffix != null) {
            sb.append("-");
            sb.append(this.suffix);
            if(this.suffixVersion > 0) {
                sb.append(".");
                sb.append(this.suffixVersion);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Compares the other version to this one.
     * 
     * @param other The version to compare this one to.
     * @return &lt; 0 iff. this &lt; other
     */
    public int compareTo(Version other) {
        return Version.compare(this, other);
    }
    
    /**
     * Compares the other version to this one.
     * 
     * @param v1 One of the versions to be compared.
     * @param v2 The other of the versions to be compared.
     * @return &lt; 0 iff. v1 &lt; v2
     */
    public static int compare(Version v1, Version v2) {
        if(v1.major != v2.major) {
            return v1.major - v2.major;
        }
        if(v1.minor != v2.minor) {
            return v1.minor - v2.minor;
        }
        if(v1.patch != v2.patch) {
            return v1.patch - v2.patch;
        }
        if(v1.suffix == null && v2.suffix != null) {
            return 1;
        }
        if(v1.suffix != null && v2.suffix == null) {
            return -1;
        }
        if(v1.suffix == null && v2.suffix == null) {
            return 0;
        }
        int suffixComp = v1.suffix.compareTo(v2.suffix);
        if(suffixComp != 0) {
            return suffixComp;
        }
        return v1.suffixVersion - v2.suffixVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.major;
        hash = 97 * hash + this.minor;
        hash = 97 * hash + this.patch;
        hash = 97 * hash + Objects.hashCode(this.suffix);
        hash = 97 * hash + this.suffixVersion;
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
        if (this.patch != other.patch) {
            return false;
        }
        if (!Objects.equals(this.suffix, other.suffix)) {
            return false;
        }
        if (this.suffixVersion != other.suffixVersion) {
            return false;
        }
        return true;
    }
}
