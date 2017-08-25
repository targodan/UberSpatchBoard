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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Corbatto
 */
public class Config {
    public static class DataSource {
        public static enum Type {
            HEXCHAT,
        }
        
        public Type type;
        public String path;
    }
    
    List<DataSource> dataSources;
    
    public Config() {
        this.dataSources = new ArrayList<>();
    }
    
    public static Config getDefaultConfig() {
        Config config = new Config();
        
        DataSource.Type defaultType = Config.getDefaultIRCClient();
        if(defaultType != null) {
            DataSource ds = new DataSource();
            ds.type = defaultType;
            ds.path = Config.getDefaultLogPath(ds.type);
            config.dataSources.add(ds);
        }
        
        return config;
    }
    
    private static DataSource.Type getDefaultIRCClient() {
        String path = Config.getDefaultHexchatPath();
        if(Files.exists(Paths.get(path))) {
            return DataSource.Type.HEXCHAT;
        }
        return null;
    }
    
    public static String getDefaultLogPath(DataSource.Type type) {
        switch(type) {
            case HEXCHAT:
                break;
        }
        
        throw new IllegalArgumentException("IRCClient unsupported.");
    }
    
    public static String getDefaultHexchatPath() {
        String osId = System.getProperty("os.name").toLowerCase();
        if(osId.contains("win")) {
            // Windows
            return "%APPDATA%\\hexchat\\logs\\fuelrats\\#fuelrats.log";
        } else if(osId.contains("mac")) {
            // Mac
            return "~/.config/hexchat/logs/fuelrats/#fuelrats.log";
        } else if(osId.contains("nix") || osId.contains("nux") || osId.contains("aix")) {
            // Unix
            return "~/.config/hexchat/logs/fuelrats/#fuelrats.log";
        } else if(osId.contains("sunos")) {
            // Solaris
            return "~/.config/hexchat/logs/fuelrats/#fuelrats.log";
        } else {
            // Wat?
            throw new IllegalStateException("OS not supported.");
        }
    }
}
