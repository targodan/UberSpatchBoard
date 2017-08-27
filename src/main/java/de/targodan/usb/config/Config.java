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

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.targodan.usb.Program;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Config represents the configuration of the UberSpatchBoard.
 * 
 * @author Luca Corbatto
 */
public class Config {
    /**
     * DataSource represents the configuration of a data source.
     */
    public static class DataSource {
        public String type;
        public String path;
    }
    
    public List<DataSource> dataSources;
    
    public float secondsUntilClearedCasesAreRemoved;

    /**
     * Creates a Config instance.
     */
    public Config() {
        this.dataSources = new ArrayList<>();
        this.secondsUntilClearedCasesAreRemoved = 60;
    }
    
    /**
     * Creates a default Config trying to detect any supported
     * IRC clients installed on the local machine.
     * 
     * @return 
     */
    public static Config getDefaultConfig() {
        Config config = new Config();
        
        Config.getInstalledIRCClients().forEach((client) -> {
            DataSource ds = new DataSource();
            ds.type = client.getName();
            ds.path = client.getFuelratsLogfilePath();
            config.dataSources.add(ds);
        });
        
        return config;
    }
    
    /**
     * Returns any registered IRCClient that reports as being installed.
     * 
     * @return 
     */
    private static List<IRCClient> getInstalledIRCClients() {
        return IRCClientRegistry.getSupportedClients().stream()
                .filter(client -> client.isInstalled())
                .collect(Collectors.toList());
    }
    
    /**
     * Reads a configuration from a filename using the yaml format.
     * 
     * @param filename
     * @return 
     */
    public static Config readConfig(String filename) {
        Path path = Paths.get(filename);
        FileReader file = null;
        try {
            File f = path.toFile();
            if(f.exists()) {
                file = new FileReader(f);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
        Config config = null;
        if(file != null) {
            YamlReader reader = new YamlReader(file);
            try {
                config = reader.read(Config.class);
                reader.close();
                file.close();
            } catch (YamlException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(config == null) {
            config = Config.getDefaultConfig();
        }
        
        return config;
    }
    
    /**
     * Writes the given configuration to a file using the yaml formatting.
     * 
     * @param config
     * @param filename 
     */
    public static void writeConfig(Config config, String filename) {
        Path path = Paths.get(filename);
        FileWriter file;
        try {
            File f = path.toFile();
            f.createNewFile();
            file = new FileWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        YamlWriter writer = new YamlWriter(file);
        try {
            writer.write(config);
            writer.close();
            file.close();
        } catch (YamlException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
