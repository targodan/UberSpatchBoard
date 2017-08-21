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
package de.targodan.usb.ui;

import de.targodan.usb.data.Case;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.data.Client;
import de.targodan.usb.data.Platform;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author Luca Corbatto
 */
public class MainWindow extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        
        this.casePanel.setLayout(new VerticalLayout());
    }
    
    public MainWindow(CaseManager cm) {
        this();
        
        this.cm = cm;
        this.updateCases();
        this.cm.addObserver(this);
    }
    
    private void updateCases() {
        if(this.cm == null) {
            throw new IllegalStateException("Can't updateCases without a CaseManager.");
        }
        
        this.casePanel.removeAll();
        this.cm.getCases().forEach(c -> {
            this.casePanel.add(new CaseView(c));
        });
        this.revalidate();
        this.repaint();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        java.awt.EventQueue.invokeLater(() -> {
            this.updateCases();
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

        jToolBar1 = new javax.swing.JToolBar();
        statusBar = new javax.swing.JPanel();
        caseBox = new javax.swing.JPanel();
        caseWrapperPanel = new javax.swing.JPanel();
        caseView1 = new de.targodan.usb.ui.CaseView();
        caseScrollPane = new javax.swing.JScrollPane();
        casePanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USB - UberSpatchBoard");

        javax.swing.GroupLayout statusBarLayout = new javax.swing.GroupLayout(statusBar);
        statusBar.setLayout(statusBarLayout);
        statusBarLayout.setHorizontalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        statusBarLayout.setVerticalGroup(
            statusBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        caseBox.setBorder(javax.swing.BorderFactory.createTitledBorder("Cases"));

        javax.swing.GroupLayout casePanelLayout = new javax.swing.GroupLayout(casePanel);
        casePanel.setLayout(casePanelLayout);
        casePanelLayout.setHorizontalGroup(
            casePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1701, Short.MAX_VALUE)
        );
        casePanelLayout.setVerticalGroup(
            casePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 535, Short.MAX_VALUE)
        );

        caseScrollPane.setViewportView(casePanel);

        javax.swing.GroupLayout caseWrapperPanelLayout = new javax.swing.GroupLayout(caseWrapperPanel);
        caseWrapperPanel.setLayout(caseWrapperPanelLayout);
        caseWrapperPanelLayout.setHorizontalGroup(
            caseWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseView1, javax.swing.GroupLayout.DEFAULT_SIZE, 1134, Short.MAX_VALUE)
            .addGroup(caseWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(caseScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1134, Short.MAX_VALUE))
        );
        caseWrapperPanelLayout.setVerticalGroup(
            caseWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseWrapperPanelLayout.createSequentialGroup()
                .addComponent(caseView1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 535, Short.MAX_VALUE))
            .addGroup(caseWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseWrapperPanelLayout.createSequentialGroup()
                    .addGap(0, 37, Short.MAX_VALUE)
                    .addComponent(caseScrollPane)))
        );

        javax.swing.GroupLayout caseBoxLayout = new javax.swing.GroupLayout(caseBox);
        caseBox.setLayout(caseBoxLayout);
        caseBoxLayout.setHorizontalGroup(
            caseBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseWrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        caseBoxLayout.setVerticalGroup(
            caseBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseWrapperPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setLabel("Close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCloseMenuClicked(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Test");
        jMenu2.setToolTipText("");

        jMenuItem2.setText("Add test case");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddTestCaseClicked(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

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
        Case testCase = new Case(this.cm.getCases().size()+1, new Client("Kies", "Kies", Platform.PC, "de"), new de.targodan.usb.data.System("Cubeo"), false, LocalDateTime.now());
        this.cm.addCase(testCase);
    }//GEN-LAST:event_onAddTestCaseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
    
    private CaseManager cm;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel caseBox;
    private javax.swing.JPanel casePanel;
    private javax.swing.JScrollPane caseScrollPane;
    private de.targodan.usb.ui.CaseView caseView1;
    private javax.swing.JPanel caseWrapperPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel statusBar;
    // End of variables declaration//GEN-END:variables
}
