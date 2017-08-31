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

import de.targodan.usb.data.Case;
import de.targodan.usb.data.CaseManager;
import de.targodan.usb.data.Client;
import de.targodan.usb.data.Platform;
import de.targodan.usb.data.Rat;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.stream.Stream;
import javafx.util.Pair;
import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Luca Corbatto
 */
public class CaseTable extends JTable {
    private static class Model implements TableModel, Observer {
        private static String[] COLUMNS = new String[] {
            "Case", "CMDR Name", "Lang", "Plat", "System", "Rats", "Notes"
        };
        private static Class<?>[] COLUMN_CLASSES = new Class<?>[] {
            Integer.class, Client.class, Client.class, Client.class, de.targodan.usb.data.System.class, Set.class, List.class
        };
        private CaseManager cm;
        private Set<TableModelListener> listeners;
        
        public Model(CaseManager cm) {
            this.cm = cm;
            this.cm.addObserver(this);
            this.listeners = new HashSet<>();
        }
        
        @Override
        public int getRowCount() {
            return this.cm.getOpenCases().size() + this.cm.getClosedCases().size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COLUMNS[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return COLUMN_CLASSES[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 6;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return new Pair<>(this.getCase(rowIndex).getNumber(), this.getCase(rowIndex));
                    
                case 1:
                case 2:
                case 3:
                    return this.getCase(rowIndex).getClient();
                    
                case 4:
                    return this.getCase(rowIndex).getSystem();
                    
                case 5:
                    return this.getCase(rowIndex).getRats();
                    
                case 6:
                    return this.getCase(rowIndex).getNotes();
            }
            throw new IllegalArgumentException("Requested column "+columnIndex+" but only 7 columns supported.");
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(columnIndex == 6) {
                String val = aValue.toString();
                this.getCase(rowIndex).setNotes(val.split("\n"));
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            this.listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            this.listeners.remove(l);
        }
        
        private void notifyListeners() {
            this.listeners.forEach(l -> {
                l.tableChanged(new TableModelEvent(this));
            });
        }
        
        private Case getCase(int rowIndex) {
            if(rowIndex < this.cm.getClosedCases().size()) {
                return this.cm.getClosedCases().get(rowIndex);
            }
            return this.cm.getOpenCases().get(rowIndex - this.cm.getClosedCases().size());
        }

        @Override
        public void update(Observable o, Object arg) {
            this.notifyListeners();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static class CaseNumberRenderer implements TableCellRenderer {
        private static final Color CR_BACKGROUND_COLOR = Color.RED;
        private static final Color CR_FOREGROUND_COLOR = Color.WHITE;
        private static final Color CLOSED_BACKGROUND_COLOR = Color.GREEN;
        private static final Color CLOSED_FOREGROUND_COLOR = Color.BLACK;
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Pair<Integer, Case> pair = (Pair<Integer, Case>)value;
            TextPanel panel = new TextPanel("#"+pair.getKey().toString());
            if(pair.getValue().isClosed()) {
                panel.setBackground(CLOSED_BACKGROUND_COLOR);
                panel.getLabel().setForeground(CLOSED_FOREGROUND_COLOR);
                panel.getLabel().setFont(new Font(panel.getLabel().getFont().getFamily(), Font.PLAIN, panel.getLabel().getFont().getSize()));
            } else if(pair.getValue().isCodeRed()) {
                panel.setBackground(CR_BACKGROUND_COLOR);
                panel.getLabel().setForeground(CR_FOREGROUND_COLOR);
                panel.getLabel().setFont(new Font(panel.getLabel().getFont().getFamily(), Font.BOLD, panel.getLabel().getFont().getSize()));
            } else if(!pair.getValue().isActive()) {
                panel.setText("("+panel.getText()+")");
            }
            return panel;
        }
    }
    private static class ClientRenderer implements TableCellRenderer {
        protected String platformToString(Platform platform) {
            switch(platform) {
                case PC:
                    return "PC";

                case PS4:
                    return "PS4";

                case XBOX:
                    return "XBox";
            }
            throw new IllegalArgumentException("Platform \""+platform.toString()+"\" is not supported.");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Client client = (Client)value;
            switch(column) {
                case 1:
                    return new CopyableTextPanel(client.getCMDRName());
                    
                case 2:
                    return new TextPanel(client.getLanguage().toUpperCase());
                    
                case 3:
                    return new TextPanel(this.platformToString(client.getPlatform()));
            }
            throw new IllegalArgumentException("Requested rendering for column "+column+" on ClientRenderer but only columns 1, 2 and 3 are suported.");
        }
    }
    private static class SystemRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            de.targodan.usb.data.System system = (de.targodan.usb.data.System)value;
            return new CopyableTextPanel(system.getName());
        }
    }
    
    @SuppressWarnings("unchecked")
    private static class RatsRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Set<Rat> rats = (Set<Rat>)value;
            JPanel panel = new JPanel(new GridLayout(rats.size(), 1));
            rats.stream()
                    .sorted((r1, r2) -> r1.getCMDRName().compareTo(r2.getCMDRName()))
                    .forEach(rat -> {
                        panel.add(new RatView(rat));
                    });
            
            int height = Stream.of(panel.getComponents())
                    .mapToInt(c -> c.getPreferredSize().height)
                    .sum();
            
            if(height > 0) {
                table.setRowHeight(row, height);
            }
            
            return panel;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static class NotesRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            List<String> notes = (List<String>)value;
            return new MultiTextPanel(String.join("\n", notes));
        }
    }
    
    private static class NotesEditor extends AbstractCellEditor implements TableCellEditor {
        private Map<Point, Component> cells;
        private MultiTextPanel panel;
        
        public NotesEditor(Map<Point, Component> cells) {
            this.cells = cells;
        }
        
        @Override
        public Object getCellEditorValue() {
            return this.panel.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if(column == 6) {
                this.panel = (MultiTextPanel)this.cells.get(new Point(row, column));
                return this.panel;
            }
            return null;
        }
    }
    
    private class MouseHandler extends MouseAdapter {
        private void relayEvent(MouseEvent e) {
            int row = CaseTable.this.rowAtPoint(e.getPoint());
            int column = CaseTable.this.columnAtPoint(e.getPoint());
            Component c = CaseTable.this.cells.get(new Point(row, column));
            if(c == null) {
                return;
            }

            Rectangle pos = CaseTable.this.getCellRect(row, column, true);

            e.translatePoint(-(int)pos.getX(), -(int)pos.getY());
            c.dispatchEvent(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            this.relayEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.relayEvent(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.relayEvent(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            this.relayEvent(e);
        }
    }
    
    public CaseTable(CaseManager cm) {
        super(new Model(cm));
        
        this.cells = new HashMap<>();
        
        this.setDefaultRenderer(Integer.class, new CaseNumberRenderer());
        this.setDefaultRenderer(Client.class, new ClientRenderer());
        this.setDefaultRenderer(de.targodan.usb.data.System.class, new SystemRenderer());
        this.setDefaultRenderer(Set.class, new RatsRenderer());
        this.setDefaultRenderer(List.class, new NotesRenderer());
        
        this.setDefaultEditor(List.class, new NotesEditor(this.cells));
        
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseHandler());
        
        // Case number
        this.getColumnModel().getColumn(0).setMaxWidth(45);
        // Language
        this.getColumnModel().getColumn(2).setMaxWidth(45);
        // Platform
        this.getColumnModel().getColumn(3).setMaxWidth(45);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        
        this.cells.put(new Point(row, column), c);
        
        return c;
    }
    
    private Map<Point, Component> cells;
}