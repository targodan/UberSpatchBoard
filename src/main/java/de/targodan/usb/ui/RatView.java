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

import de.targodan.usb.data.Rat;
import de.targodan.usb.data.Report;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Luca Corbatto
 */
public class RatView extends javax.swing.JPanel implements Observer {

    /**
     * Creates new form RatView
     */
    public RatView() {
        initComponents();
    }
    
    public RatView(Rat rat) {
        this();
        
        this.rat = rat;
        this.updateRatView();
        this.rat.addObserver(this);
    }

    private String getRatName() {
        String name = this.rat.getCMDRName();
        if(name.length() == 0) {
            name = this.rat.getIRCName();
        }
        return name;
    }
    
    private void updateRatView() {
        if(this.rat == null) {
            throw new IllegalStateException("Can't updateRatView without a rat.");
        }
        
        this.ratName.setText(this.getRatName());
        this.jumps.setText(Integer.toString(this.rat.getJumps())+"j");
        this.updateReports();
        
        this.revalidate();
        this.repaint();
    }
    
    private void updateReports() {
        this.reportsPanel.removeAll();
        
        this.rat.getReports().stream()
                .sorted((r1, r2) -> this.reportOrder(r1.getType()) - this.reportOrder(r2.getType()))
                .forEach(report -> {
                    JLabel label = new JLabel();
                    JPanel panel = new JPanel();

                    String text = this.reportTypeToString(report.getType());
                    if(report.isPlus()) {
                        text += "+";
                        panel.setBackground(RatView.PLUS_BACKGROUND_COLOR);
                        label.setForeground(RatView.PLUS_FOREGROUND_COLOR);
                    } else {
                        text += "-";
                        panel.setBackground(RatView.MINUS_BACKGROUND_COLOR);
                        label.setForeground(RatView.MINUS_FOREGROUND_COLOR);
                        label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, label.getFont().getSize()));
                    }
                    label.setText(text);
                    panel.add(label);
                    panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 25));

                    this.reportsPanel.add(panel);
                });
    }
    
    private String reportTypeToString(Report.Type type) {
        switch(type) {
            case SYS:
                return "sys";
                
            case BC:
                return "bc";
                
            case COMMS:
                return "comms";
                
            case FR:
                return "fr";
                
            case INST:
                return "inst";
                
            case PARTY:
                return "party";
                
            case WR:
                return "wr";
        }
        
        throw new IllegalArgumentException("Unknown report type \""+type.toString()+"\".");
    }
    
    private int reportOrder(Report.Type type) {
        switch(type) {
            case SYS:
                return 0;
  
            case FR:
                return 1;
                
            case PARTY:
                return 2;
            
            case COMMS:
                return 3;
                
            case WR:
                return 4;
                
            case BC:
                return 5;
                
            case INST:
                return 6;
        }
        
        throw new IllegalArgumentException("Unknown report type \""+type.toString()+"\".");
    }
    
    @Override
    public void update(Observable o, Object arg) {
        java.awt.EventQueue.invokeLater(() -> {
            this.updateRatView();
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

        ratNamePanel = new javax.swing.JPanel();
        ratName = new javax.swing.JLabel();
        jumpsPanel = new javax.swing.JPanel();
        jumps = new javax.swing.JLabel();
        reportsPanel = new javax.swing.JPanel();

        ratNamePanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));

        ratName.setText("ratName");

        javax.swing.GroupLayout ratNamePanelLayout = new javax.swing.GroupLayout(ratNamePanel);
        ratNamePanel.setLayout(ratNamePanelLayout);
        ratNamePanelLayout.setHorizontalGroup(
            ratNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ratNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ratName)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        ratNamePanelLayout.setVerticalGroup(
            ratNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ratNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ratName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jumpsPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)));

        jumps.setText("12j");

        javax.swing.GroupLayout jumpsPanelLayout = new javax.swing.GroupLayout(jumpsPanel);
        jumpsPanel.setLayout(jumpsPanelLayout);
        jumpsPanelLayout.setHorizontalGroup(
            jumpsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jumpsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jumps)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jumpsPanelLayout.setVerticalGroup(
            jumpsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jumpsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jumps)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reportsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ratNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jumpsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ratNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jumpsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(reportsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private Rat rat;
    
    private static final Color MINUS_BACKGROUND_COLOR = Color.RED;
    private static final Color MINUS_FOREGROUND_COLOR = Color.WHITE;
    private static final Color PLUS_BACKGROUND_COLOR = Color.GREEN;
    private static final Color PLUS_FOREGROUND_COLOR = Color.BLACK;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jumps;
    private javax.swing.JPanel jumpsPanel;
    private javax.swing.JLabel ratName;
    private javax.swing.JPanel ratNamePanel;
    private javax.swing.JPanel reportsPanel;
    // End of variables declaration//GEN-END:variables
}
