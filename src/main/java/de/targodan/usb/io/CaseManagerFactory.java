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

import de.targodan.usb.io.processing.DefaultHandler;
import de.targodan.usb.io.processing.Handler;
import de.targodan.usb.io.processing.DefaultParser;
import de.targodan.usb.io.processing.Parser;
import de.targodan.usb.data.CaseManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CaseManagerFactory creates a CaseManager with DataSources as defined in the
 * given Config.
 * 
 * @author Luca Corbatto
 */
public abstract class CaseManagerFactory {
    /**
     * Creates a CaseManager.
     * 
     * @return a CaseManager.
     */
    public abstract CaseManager createCaseManager();
    
    /**
     * Creates a DataConsumer with DataSources as defined in the Config.
     * 
     * @return The DataConsumer to be used for reading data.
     */
    public abstract DataConsumer createDataConsumer();
    
    /**
     * Creates a default factory.
     * 
     * @param config The configuration for the factory.
     * @return a CaseManagerFactory.
     */
    public static CaseManagerFactory getDefaultFactory(Config config) {
        return new DefaultCaseManagerFactory(config);
    }
    
    /**
     * The default implementation of the CaseManagerFactory.
     */
    private static class DefaultCaseManagerFactory extends CaseManagerFactory {
        private Config config = null;
        private CaseManager cm = null;
        private DataConsumer dc = null;
        
        /**
         * Constructs a new CaseManagerFactory that will create a CaseManager
         * with DataSources as defined by the given config.
         * 
         * @param config The configuration to be used for creating the DataSources.
         */
        public DefaultCaseManagerFactory(Config config) {
            this.config = config;
        }
        
        @Override
        public CaseManager createCaseManager() {
            if(this.cm == null) {
                this.cm = new CaseManager();

                Handler handler = new DefaultHandler();
                handler.registerCaseManager(cm);
                Parser parser = new DefaultParser();
                parser.registerHandler(handler);

                this.dc = new DataConsumer(parser);
            }
           
            return this.cm;
        }

        @Override
        public DataConsumer createDataConsumer() {
            if(this.dc == null) {
                this.createCaseManager();
            }
            
            this.config.dataSources.stream()
            .forEach((dsConfig) -> {
                IRCClient ircClient = IRCClientRegistry.getIRCClientByName(dsConfig.type);
                if(ircClient == null) {
                    throw new IllegalArgumentException("IRCClient type \""+dsConfig.type+"\" is unknown.");
                }
                
                String filePath = dsConfig.path;
                if(filePath == null) {
                    filePath = ircClient.getFuelratsLogfilePath();
                }
                filePath = PathSanitizer.sanitize(filePath);
                
                try {
                    Logger.getLogger(CaseManagerFactory.class.getName()).log(Level.INFO, "Requesting DataSource {0}:\"{1}\"", new Object[]{ircClient.getName(), filePath});
                    DataSource ds = new SingleChannelFileDataSource("#fuelrats", filePath, ircClient.getDefaultLogFileEncoding(), ircClient.getMarshaller());
                    this.dc.addDataSource(ds);
                    Logger.getLogger(CaseManagerFactory.class.getName()).log(Level.INFO, "DataSource successful.");
                } catch (Exception ex) {
                    Logger.getLogger(CaseManagerFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            return this.dc;
        }
    }
}