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
package de.targodan.usb.ui;

import de.targodan.usb.Program;
import de.targodan.usb.io.DataSource;
import de.targodan.usb.io.IRCFormatFilteringReader;
import de.targodan.usb.io.processing.IRCMessage;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Corbatto
 */
public class MessageInjectionWindow extends javax.swing.JDialog implements DataSource {
    private BlockingQueue<IRCMessage> output;

    /**
     * Creates new form MessageInjectionWindow
     * 
     * @param owner The owner of this JDialog.
     */
    public MessageInjectionWindow(Window owner) {
        super(owner);
        initComponents();
        
        Program.dataConsumer.addDataSource(this);
        
        this.message.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    MessageInjectionWindow.this.onSendButtonClicked(null);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        ircNickname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        channel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        message = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("USB - Inject IRC Messages");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        getContentPane().setLayout(layout);

        jLabel1.setText("IRC Nickname");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jLabel1, gridBagConstraints);

        ircNickname.setMinimumSize(new java.awt.Dimension(100, 20));
        ircNickname.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(ircNickname, gridBagConstraints);

        jLabel2.setText("Channel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jLabel2, gridBagConstraints);

        channel.setMinimumSize(new java.awt.Dimension(100, 20));
        channel.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(channel, gridBagConstraints);

        jLabel3.setText("Message");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        getContentPane().add(jLabel3, gridBagConstraints);

        message.setMinimumSize(new java.awt.Dimension(100, 20));
        message.setPreferredSize(new java.awt.Dimension(400, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(message, gridBagConstraints);

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSendButtonClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        getContentPane().add(jButton1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendComplete() {
        java.awt.EventQueue.invokeLater(() -> {
            this.message.setText("");
        });
    }
    
    private void onSendButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSendButtonClicked
        if(this.output == null) {
            return;
        }
        
        StringBuilder msgContent = new StringBuilder();
        char[] buf = new char[128];
        int numRead;
        try(BufferedReader reader = new BufferedReader(new IRCFormatFilteringReader(new StringReader(this.message.getText())))) {
            while(true) {
                numRead = reader.read(buf);
                if(numRead > 0) {
                    msgContent.append(buf, 0, numRead);
                } else{
                    break;
                }
            }
        } catch(IOException ex) {}
        
        IRCMessage msg = new IRCMessage(LocalDateTime.now(), this.ircNickname.getText(), this.channel.getText(), msgContent.toString());
        
        java.awt.EventQueue.invokeLater(() -> {
            try {
                while(!this.output.offer(msg, 100, TimeUnit.MILLISECONDS)) {}
                this.sendComplete();
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageInjectionWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }//GEN-LAST:event_onSendButtonClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Program.dataConsumer.removeDataSource(this);
    }//GEN-LAST:event_formWindowClosing

    @Override
    public void listen(BlockingQueue<IRCMessage> output) {
        this.output = output;
    }

    @Override
    public void stop() {
        this.output = null;
    }

    @Override
    public String getShortName() {
        return "injecter window";
    }

    @Override
    public String getName() {
        return "injecter window";
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField channel;
    private javax.swing.JTextField ircNickname;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField message;
    // End of variables declaration//GEN-END:variables
}
