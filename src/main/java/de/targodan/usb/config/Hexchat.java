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

import de.targodan.usb.io.HexchatMarshaller;
import de.targodan.usb.io.Marshaller;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Hexchat represents the HexChat IRC client.
 * 
 * @author Luca Corbatto
 */
public class Hexchat implements IRCClient {
    private String getBasePath() {
        String path;
        switch(OperatingSystem.getCurrent()) {
            case WINDOWS:
                path = "%APPDATA%\\hexchat";
                break;
                
            case MAC:
                // TODO: Check if it's the same on mac
            case UNIX:
            case SOLARIS:
                path = "~/.config/hexchat";
                break;
                
            default:
                throw new UnsupportedOperationException("OS is not supported.");
        }
        return PathSanitizer.sanitize(path);
    }
    
    @Override
    public boolean isInstalled() {
        File f = new File(this.getBasePath());
        String path = f.getAbsolutePath();
        return f.exists();
    }
    
    @Override
    public String getFuelratsLogfilePath() {
        Pattern fuelratsFolderPattern = Pattern.compile(".*fuelrats.*", Pattern.CASE_INSENSITIVE);
        Pattern fuelratsFilePattern = Pattern.compile(".*fuelrats\\.log", Pattern.CASE_INSENSITIVE);
        
        String basePath = this.getBasePath();
        
        Path logsPath = Paths.get(basePath, "logs");
        File logsFolder = logsPath.toFile();
        
        File fuelratsFolder = Arrays.asList(logsFolder.listFiles()).stream()
                .filter(f -> f.isDirectory())
                .filter(dir -> fuelratsFolderPattern.matcher(dir.getName()).matches())
                .findFirst().orElse(null);
        
        if(fuelratsFolder == null) {
            return null;
        }
        
        File fuelratsFile = Arrays.asList(fuelratsFolder.listFiles()).stream()
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
        return new HexchatMarshaller();
    }

    @Override
    public String getName() {
        return "hexchat";
    }
}
