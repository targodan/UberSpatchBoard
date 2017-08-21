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

import de.targodan.usb.data.CaseManager;
import de.targodan.usb.io.DataConsumer;
import de.targodan.usb.io.DataSource;
import de.targodan.usb.io.DefaultHandler;
import de.targodan.usb.io.DefaultParser;
import de.targodan.usb.io.Handler;
import de.targodan.usb.io.HexchatMarshaller;
import de.targodan.usb.io.Parser;
import de.targodan.usb.io.SingleChannelFileDataSource;
import de.targodan.usb.ui.MainWindow;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Program {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CaseManager cm = new CaseManager();
        String appdata = System.getenv("APPDATA");
        String logfile = appdata+"\\HexChat\\logs\\FuelRats\\#fuelrats.log";
        
        Handler handler = new DefaultHandler();
        handler.registerCaseManager(cm);
        Parser parser = new DefaultParser();
        parser.registerHandler(handler);
        
        DataConsumer dc = new DataConsumer(parser);
        
        new Thread(() -> {
            dc.start();
        }).start();
        
        try {
            DataSource ds = new SingleChannelFileDataSource("#fuelrats", logfile, new HexchatMarshaller());
            dc.addDataSource(ds);
        } catch(Exception ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, "Can't open logfile.", logfile);
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, "Exception opening logfile.", ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MainWindow window = new MainWindow(cm);
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    dc.stop();
                }
            });
            window.setVisible(true);
        });
    }
}
