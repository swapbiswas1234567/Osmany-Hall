/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import java.awt.Component;
import javafx.scene.control.Tab;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {

    WordWrapCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        if (table.getRowHeight(row) != getPreferredSize().height) {
            table.setRowHeight(row, getPreferredSize().height);
            
        }
        
        
        if (table.isCellSelected(row, column)) {
            setForeground(new java.awt.Color(240, 240, 240));
            setBackground(new java.awt.Color(232, 57, 97));
        }
        else{
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
        }
        

        return this;
    }

   
}
