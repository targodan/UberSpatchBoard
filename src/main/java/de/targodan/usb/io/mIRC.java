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

import de.targodan.usb.io.processing.Marshaller;
import de.targodan.usb.io.processing.mIRCMarshaller;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * mIRC represents the mIRC IRC client.
 *
 * @author Luca Corbatto
 */
public class mIRC implements IRCClient {
    public static final Set<OperatingSystem> SUPPORTED_OPERATING_SYSTEMS
            = Collections.unmodifiableSet(
                EnumSet.of(OperatingSystem.WINDOWS)
            );
    
    private static final Pattern fuelratsFilePattern = Pattern.compile(".*fuelrats.*fuelrats.*\\.log", Pattern.CASE_INSENSITIVE);
    
    /**
     * Returns the base mIRC path that contains the log files and configs.
     * 
     * @return the base mIRC path that contains the log files and configs.
     */
    private String getBasePath() {
        return PathSanitizer.sanitize("%APPDATA%\\mIRC");
    }

    @Override
    public boolean isInstalled() {
        File f = new File(this.getBasePath());
        return f.exists();
    }

    @Override
    public String getFuelratsLogfilePath() {
        String basePath = this.getBasePath();
        
        Path logsPath = Paths.get(basePath, "logs");
        File logsFolder = logsPath.toFile();
        
        File fuelratsFile = Arrays.asList(logsFolder.listFiles()).stream()
                .filter(f -> f.isFile())
                .filter(f -> fuelratsFilePattern.matcher(f.getName()).matches())
                .findFirst().orElse(null);
        if(fuelratsFile == null) {
            return null;
        }
                
        return fuelratsFile.getAbsolutePath();
    }

    @Override
    public Marshaller getMarshaller() {
        return new mIRCMarshaller();
    }

    @Override
    public String getName() {
        return "mIRC";
    }

    @Override
    public Set<OperatingSystem> getSupportedOperatingSystems() {
        return mIRC.SUPPORTED_OPERATING_SYSTEMS;
    }

    @Override
    public Charset getDefaultLogFileEncoding() {
        return Charset.forName("UTF-8");
    }
}
