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

import de.targodan.usb.config.CaseManagerFactory;
import de.targodan.usb.config.Config;
import de.targodan.usb.config.PathSanitizer;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.io.DataConsumer;
import de.targodan.usb.ui.ConsoleWindow;
import de.targodan.usb.ui.MainWindow;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Program {
    public static DataConsumer dataConsumer;
    public static final Version VERSION = Version.parse("v1.0-alpha.3");
    public static String CONFIG_FILE = "usb.yml";
    public static final String[] CONTRIBUTORS = new String[] {
        "Your name could be here",
    };
    public static Config CONFIG;

    /**
     * The main function will start up the UberSpatchBoard.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        ConsoleWindow consoleWindow = new ConsoleWindow();

        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.INFO);
        for(java.util.logging.Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.INFO);
        }
        
        if(args.length >= 2 && args[0].equals("--config")) {
            CONFIG_FILE = PathSanitizer.sanitize(args[1]);
        }

        CONFIG = Config.readConfig(Program.CONFIG_FILE);

        CaseManagerFactory factory = CaseManagerFactory.getDefaultFactory(CONFIG);

        CaseManager cm = factory.createCaseManager();
        Program.dataConsumer = factory.createDataConsumer();

        Thread dataConsumerThread = new Thread(() -> {
            Program.dataConsumer.start();
        });
        dataConsumerThread.setName("DataConsumerThread");
        dataConsumerThread.start();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MainWindow window = new MainWindow(consoleWindow, cm);
            window.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    consoleWindow.dispose();

                    /*
                    Open a new Thread because you can't wait in the EventQueue Thread.
                    The application will stay open as long as there are extra Threads open,
                    so this should work as intended.
                    */
                    Thread cleanUpThread = new Thread(() -> {
                        Program.dataConsumer.stop();
                        try {
                            dataConsumerThread.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    cleanUpThread.setName("cleanUpThread");
                    cleanUpThread.start();
                }
            });
            window.setVisible(true);
        });
    }
}
