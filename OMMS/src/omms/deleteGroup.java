/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Asus
 */
public class deleteGroup extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    Date curDate = new Date();

    /**
     * Creates new form deleteGroup
     */
    public deleteGroup() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers(); // setting todays date to the date chooser
        closeBtn();
        Tabledecoration();
        model = deleteGroupTable.getModel();
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) dateChoser.getDateEditor();
        dtedit.setEditable(false);
    }

    /*
        Seeting the cross button action to Dashboard
     */
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    Dashboard das = new Dashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /*
        Setting the date choosers to todays current date
     */
    public void setDateChoosers() {
        Date todaysDate = new Date();
        dateChoser.setDate(todaysDate);
    }

    public void Tabledecoration() {
        deleteGroupTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        deleteGroupTable.getTableHeader().setOpaque(false);
        deleteGroupTable.getTableHeader().setBackground(new Color(32, 136, 203));
        deleteGroupTable.getTableHeader().setForeground(new Color(255, 255, 255));
        deleteGroupTable.setRowHeight(25);
        deleteGroupTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        deleteGroupTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        deleteGroupTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        deleteGroupTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        deleteGroupTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);

    }

    public String grpIdToGrpName(int id, int date, String state) {
        try {
            PreparedStatement p = conn.prepareStatement("SELECT name FROM grp WHERE serial = ? AND state = ? AND date = ?");
            p.setInt(1, id);
            p.setString(2, state);
            p.setInt(3, date);
            ResultSet r = p.executeQuery();
            String name = r.getString(1);

            p.close();
            r.close();
            return name;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to convert group name from Id", "Database Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    public void showItemList(int row) {
        Date date = dateChoser.getDate();
        String state = stateComboBox.getSelectedItem().toString().toLowerCase();
        int dateInt = Integer.parseInt(formatDate.format(date));
        String showMsg = "Stored Items \n";
        int flag = 0;

        String str = "bfgrp";
        if (state.equals("lunch")) {
            str = "lunchgrp";
        } else if (state.equals("dinner")) {
            str = "dinnergrp";
        }

        try {
            ps = conn.prepareStatement("SELECT item, bf, unit FROM storeinout sio JOIN storeditem si ON sio.item = si.name WHERE sio.serial = ? AND " + str + " = ?");
            ps.setInt(1, dateInt);
            ps.setInt(2, row);
            rs = ps.executeQuery();
            while (rs.next()) {
                showMsg = showMsg.concat(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3) + "\n");
                flag++;
            }
            if (flag == 0) {
                showMsg = showMsg.concat("No Items\n");
            }
            flag = 0;
            //JOptionPane.showMessageDialog(null, showMsg, "Item Details", JOptionPane.INFORMATION_MESSAGE);
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch Stored Items data from Database to ShowItem", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        showMsg = showMsg.concat("\nNon Stored Items\n");
        try {
            ps = conn.prepareStatement("SELECT name, amount, unit FROM nonstoreditem nsi JOIN nonstoreditemlist nsil USING(name) WHERE nsi.serial = ? AND grp = ? AND state = ?");
            ps.setInt(1, dateInt);
            ps.setInt(2, row);
            ps.setString(3, state);
            rs = ps.executeQuery();
            while (rs.next()) {
                showMsg = showMsg.concat(rs.getString(1) + " - " + rs.getString(2) + " " + rs.getString(3) + "\n");
                flag++;
            }

            if (flag == 0) {
                showMsg = showMsg.concat("No Items\n");
            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch Nonsoted Items data from Database to ShowItem", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, showMsg, "Item Details", JOptionPane.INFORMATION_MESSAGE);

    }

    public void setDeleteTable() {
        Date date = dateChoser.getDate();
        String state = stateComboBox.getSelectedItem().toString().toLowerCase();
        int dateInt = Integer.parseInt(formatDate.format(date));

        ArrayList<Integer> grpIds = new ArrayList<>();
        int serial = 0;

        tablemodel = (DefaultTableModel) deleteGroupTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            ps = conn.prepareStatement("SELECT serial FROM grp WHERE date = ? AND state = ?");
            ps.setInt(1, dateInt);
            ps.setString(2, state);
            rs = ps.executeQuery();
            while (rs.next()) {
                grpIds.add(rs.getInt(1));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data from grp table of Database  to setTable", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String str = "bfgrp";
        if (state.equals("lunch")) {
            str = "lunchgrp";
        } else if (state.equals("dinner")) {
            str = "dinnergrp";
        }
        for (int i = 0; i < grpIds.size(); i++) {
            int items = 0;
            int mealCount = 0;
            try {
                ps = conn.prepareStatement("SELECT COUNT(item) FROM storeinout WHERE serial = ? AND " + str + " = ?");
                ps.setInt(1, dateInt);
                ps.setInt(2, grpIds.get(i));
                rs = ps.executeQuery();
                while (rs.next()) {
                    items += rs.getInt(1);
                }
                ps.close();
                rs.close();

                PreparedStatement psNonStored = conn.prepareStatement("SELECT COUNT(name) FROM nonstoreditem WHERE serial = ? AND state = ? AND grp = ?");
                psNonStored.setInt(1, dateInt);
                psNonStored.setString(2, state);
                psNonStored.setInt(3, grpIds.get(i));
                ResultSet rsNonStored = psNonStored.executeQuery();
                while (rsNonStored.next()) {
                    items += rsNonStored.getInt(1);
                }
                psNonStored.close();
                rsNonStored.close();

                PreparedStatement psMealCount = conn.prepareStatement("SELECT COUNT(hallid) FROM mealsheet WHERE date = ? AND " + str + " = ?");
                psMealCount.setInt(1, dateInt);
                psMealCount.setInt(2, grpIds.get(i));
                ResultSet rsMealCount = psMealCount.executeQuery();
                while (rsMealCount.next()) {
                    mealCount = rsMealCount.getInt(1);
                }
                psMealCount.close();
                rsMealCount.close();
                serial++;
                Object o[] = {serial, grpIdToGrpName(i + 1, dateInt, state), items, mealCount};
                tablemodel.addRow(o);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to fetch Item Counting for setTable from Database", "Database ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void deleteGroups() {
        Date date = dateChoser.getDate();
        String state = stateComboBox.getSelectedItem().toString().toLowerCase();
        int dateInt = Integer.parseInt(formatDate.format(date));
        try {
            ps = conn.prepareStatement("DELETE FROM grp WHERE date = ? AND state = ? ");
            ps.setInt(1, dateInt);
            ps.setString(2, state);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to delete data from grp table of Database", "Database Deletion Error", JOptionPane.ERROR_MESSAGE);
        }

        String str = "bfgrp";
        if (state.equals("lunch")) {
            str = "lunchgrp";
        } else if (state.equals("dinner")) {
            str = "dinnergrp";
        }

        try {
            ps = conn.prepareStatement("UPDATE storeinout SET " + str + " = 0 WHERE serial = ? AND " + str + " > 0");
            ps.setInt(1, dateInt);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to update data from StoreInOut table of Database", "Database Deletion Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            ps = conn.prepareStatement("UPDATE nonstoreditem SET grp = 0 WHERE serial = ? AND state = ?");
            ps.setInt(1, dateInt);
            ps.setString(2, state);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to update Nonsoted Items data from Database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ps = conn.prepareStatement("UPDATE mealsheet SET " + str + " = 0 WHERE date = ?");
            ps.setInt(1, dateInt);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to update MealSheet data from Database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tempFoodViewJpanel = new javax.swing.JLabel();
        dateChoser = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        totalBillJlbl = new javax.swing.JLabel();
        stateComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        deleteBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        deleteGroupTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        tempFoodViewJpanel.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        tempFoodViewJpanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tempFoodViewJpanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/x-button (1).png"))); // NOI18N
        tempFoodViewJpanel.setText("DELETE GROUP");

        dateChoser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        dateChoser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateChoserPropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("Date");

        totalBillJlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalBillJlbl.setForeground(new java.awt.Color(255, 51, 0));
        totalBillJlbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        stateComboBox.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        stateComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        stateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stateComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel3.setText("State");

        deleteBtn.setBackground(new java.awt.Color(255, 102, 102));
        deleteBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_.png"))); // NOI18N
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        deleteGroupTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        deleteGroupTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Group", "Item Count", "Meal Count"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        deleteGroupTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        deleteGroupTable.setRowHeight(26);
        deleteGroupTable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        deleteGroupTable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        deleteGroupTable.setShowVerticalLines(false);
        deleteGroupTable.getTableHeader().setReorderingAllowed(false);
        deleteGroupTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteGroupTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(deleteGroupTable);
        if (deleteGroupTable.getColumnModel().getColumnCount() > 0) {
            deleteGroupTable.getColumnModel().getColumn(0).setMinWidth(150);
            deleteGroupTable.getColumnModel().getColumn(0).setMaxWidth(500);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateChoser, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(deleteBtn)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tempFoodViewJpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalBillJlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(tempFoodViewJpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(dateChoser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)))
                .addGap(15, 15, 15)
                .addComponent(totalBillJlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateChoserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateChoserPropertyChange
        if (dateChoser.getDate() != null) {
            curDate = dateChoser.getDate();
            setDeleteTable();
        }
    }//GEN-LAST:event_dateChoserPropertyChange

    private void stateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stateComboBoxActionPerformed
        // TODO add your handling code here:
        setDeleteTable();
    }//GEN-LAST:event_stateComboBoxActionPerformed

    private void deleteGroupTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteGroupTableMouseClicked
        // TODO add your handling code here:
        int i = -1;
        i = deleteGroupTable.getSelectedRow();
        if (i >= 0) {
            showItemList(i + 1);
        }
    }//GEN-LAST:event_deleteGroupTableMouseClicked

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        if (model.getRowCount() > 0) {
            int responce = JOptionPane.showConfirmDialog(this, "Do you want to delete these data ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (responce == JOptionPane.YES_OPTION) {
                try {
                    deleteGroups();
                    setDeleteTable();
                } catch (Exception ex) {
                    Logger.getLogger(StoreOutItem.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "No item is available on the table", "Table item not found", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(deleteGroup.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(deleteGroup.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(deleteGroup.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(deleteGroup.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new deleteGroup().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateChoser;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTable deleteGroupTable;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> stateComboBox;
    private javax.swing.JLabel tempFoodViewJpanel;
    private javax.swing.JLabel totalBillJlbl;
    // End of variables declaration//GEN-END:variables
}
