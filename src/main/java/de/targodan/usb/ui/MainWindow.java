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
import de.targodan.usb.data.Case;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.data.Client;
import de.targodan.usb.data.Platform;
import de.targodan.usb.data.Rat;
import de.targodan.usb.data.Report;
import de.targodan.usb.io.DataConsumer;
import java.awt.Dimension;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Luca Corbatto
 */
public class MainWindow extends javax.swing.JFrame implements Observer {
    private DataConsumer dataConsumer;
    
    public MainWindow(ConsoleWindow consoleWindow, CaseManager cm) {
        this.cm = cm;
        
        initComponents();
        
        this.consoleWindow = consoleWindow;
        
        this.runRemoveClearedCasesThread = new AtomicBoolean(true);
        this.removeClearedCasesThread = new Thread(() -> {
            while(this.runRemoveClearedCasesThread.get()) {
                try {
                    Thread t = Thread.currentThread();
                    synchronized(t) {
                        t.wait(200);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(Program.CONFIG.secondsUntilClearedCasesAreRemoved > 0) {
                    this.cm.removeClosedCasesOlderThan(LocalDateTime.now().minus((int)(Program.CONFIG.secondsUntilClearedCasesAreRemoved * 1000), ChronoUnit.MILLIS));
                }
            }
        });
        this.removeClearedCasesThread.setName("RemoveClearedCasesThread");
    }
    
    private void updateDataConsumerLabel() {
        StringBuilder shortName = new StringBuilder();
        StringBuilder longName = new StringBuilder();
        if(this.dataConsumer.isRunning()) {
            shortName.append("Reading from: ");
        } else {
            shortName.append("Connected to: ");
        }
        shortName.append(
                this.dataConsumer.getDataSources().stream()
                    .map(ds -> ds.getShortName())
                    .collect(Collectors.joining(", "))
            );
        longName.append(
                this.dataConsumer.getDataSources().stream()
                    .map(ds -> "\""+ds.getName()+"\"")
                    .collect(Collectors.joining(", "))
            );
        
        this.dataConsumerLabel.setText(shortName.toString());
        this.dataConsumerLabel.setToolTipText(longName.toString());
    }
    
    @Override
    public void update(Observable o, Object o1) {
        java.awt.EventQueue.invokeLater(() -> {
            if(o == this.dataConsumer) {
                this.updateDataConsumerLabel();
            }
        });
    }

    public void setDataConsumer(DataConsumer dataConsumer) {
        this.dataConsumer = dataConsumer;
        this.updateDataConsumerLabel();
        this.dataConsumer.addObserver(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        caseBox = new javax.swing.JPanel();
        caseWrapperPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new CaseTable(this.cm);
        statusBar = new javax.swing.JPanel();
        dataConsumerLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("USB - UberSpatchBoard");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        caseBox.setBorder(javax.swing.BorderFactory.createTitledBorder("Cases"));

        caseWrapperPanel.setLayout(new java.awt.BorderLayout());

        jTable1.setRowSelectionAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        caseWrapperPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout caseBoxLayout = new javax.swing.GroupLayout(caseBox);
        caseBox.setLayout(caseBoxLayout);
        caseBoxLayout.setHorizontalGroup(
            caseBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseWrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
        );
        caseBoxLayout.setVerticalGroup(
            caseBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseWrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
        );

        dataConsumerLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        statusBar.add(dataConsumerLabel);

        jMenu1.setText("File");

        jMenuItem8.setText("Settings");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSettingsClicked(evt);
            }
        });
        jMenu1.add(jMenuItem8);
        jMenu1.add(jSeparator2);

        jMenuItem1.setText("Close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCloseMenuClicked(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Test");
        jMenu2.setToolTipText("");

        jMenuItem4.setText("Show Console");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onShowConsoleClicked(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem2.setText("Add test Case");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddTestCaseClicked(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Open injection Window");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOpenInjectionWindowClicked(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem5.setText("Report a Bug");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onReportABugClicked(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setText("Contribute");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onContributeClicked(evt);
            }
        });
        jMenu3.add(jMenuItem6);
        jMenu3.add(jSeparator1);

        jMenuItem7.setText("About");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAboutClicked(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(caseBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(caseBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onCloseMenuClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCloseMenuClicked
        this.dispose();
    }//GEN-LAST:event_onCloseMenuClicked

    private void onAddTestCaseClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddTestCaseClicked
        Case testCase = new Case(this.cm.getOpenCases().size()+1, new Client("Kies", "Kies", Platform.PC, "de"), new de.targodan.usb.data.System("Cubeo"), false, LocalDateTime.now());
        Rat rat = new Rat("testRat");
        rat.setJumps(5);
        rat.setAssigned(true);
        rat.insertReport(new Report(Report.Type.SYS, true));
        testCase.assignRat(rat);
        this.cm.addCase(testCase);
    }//GEN-LAST:event_onAddTestCaseClicked

    private void onOpenInjectionWindowClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOpenInjectionWindowClicked
        MessageInjectionWindow window = new MessageInjectionWindow(this);
        window.setVisible(true);
        Program.dataConsumer.addDataSource(window);
    }//GEN-LAST:event_onOpenInjectionWindowClicked

    private void onShowConsoleClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onShowConsoleClicked
        this.consoleWindow.setVisible(true);
    }//GEN-LAST:event_onShowConsoleClicked

    private void onReportABugClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onReportABugClicked
        try {
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/targodan/UberSpatchBoard#Report-a-Bug"));
        } catch (Exception ex) { /* Seriously Java, bugger off with your checked exceptions! */ }
    }//GEN-LAST:event_onReportABugClicked

    private void onContributeClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onContributeClicked
        try {
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/targodan/UberSpatchBoard#Contribute"));
        } catch (Exception ex) { /* Seriously Java, bugger off with your checked exceptions! */ }
    }//GEN-LAST:event_onContributeClicked

    private void onAboutClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAboutClicked
        java.awt.EventQueue.invokeLater(() -> {
            AboutWindow w = new AboutWindow();
            w.setSize(new Dimension(400, 300));
            w.setVisible(true);
        });
    }//GEN-LAST:event_onAboutClicked

    private void onSettingsClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSettingsClicked
        java.awt.EventQueue.invokeLater(() -> {
            new SettingsWindow(this).setVisible(true);
        });
    }//GEN-LAST:event_onSettingsClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.removeClearedCasesThread.start();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.runRemoveClearedCasesThread.set(false);
        try {
            this.removeClearedCasesThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private CaseManager cm;
    private ConsoleWindow consoleWindow;
    private final Thread removeClearedCasesThread;
    private AtomicBoolean runRemoveClearedCasesThread;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel caseBox;
    private javax.swing.JPanel caseWrapperPanel;
    private javax.swing.JLabel dataConsumerLabel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel statusBar;
    // End of variables declaration//GEN-END:variables
}
