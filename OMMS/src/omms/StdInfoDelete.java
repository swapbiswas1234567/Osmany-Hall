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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Asus
 */
public class StdInfoDelete extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    int serial = 0;

    /**
     * Creates new form StdInfoDelete
     */
    public StdInfoDelete() {
        initComponents();
        initialize();
        setTitle("Delete Student's Information");
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database  
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        //closeBtn();
        tablemodel = (DefaultTableModel) stdDelTable.getModel();
        
         try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        showAll();
    }

    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    conn.close();
                    if(UserLog.name.equals("accountant")){
                        DashboardAccountant das = new DashboardAccountant();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("provost")){
                        DashboardHallAutho das = new DashboardHallAutho();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("mess")){
                        DashboardMess das = new DashboardMess();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("captain")){
                        DashboardMessCap das = new DashboardMessCap();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void Tabledecoration() {
        stdDelTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 18));
        stdDelTable.getTableHeader().setOpaque(false);
        stdDelTable.getTableHeader().setBackground(new Color(32, 136, 203));
        stdDelTable.getTableHeader().setForeground(new Color(255, 255, 255));
        stdDelTable.setRowHeight(25);
        stdDelTable.setFont(new Font("Segeo UI", Font.PLAIN, 16));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        //stdDelTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        stdDelTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);
    }

    public void showAll() {
        String hallid = findHallId();
        String query = "";
        if (!hallid.equals("")) {
            query = " WHERE hallid = " + hallid;
        }
        tablemodel = (DefaultTableModel) stdDelTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            tablemodel.setRowCount(0);
        }
        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, dept, roomno, totaldue, securitymoney, messad, idcard, contno FROM stuinfo" + query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] obj = {false, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10)};
                tablemodel.addRow(obj);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data fetching error from Student Info Table", "Data Fetching Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePrevStd(int hallid) {
        try {
            ps = conn.prepareStatement("INSERT INTO previousstudents (hallid, roll, name, fname, mname, dept, batch, entrydate, contno, email, bgrp, sex, rel, dob, peradd, presentadd, roomno, image, securitymoney, messad, idcard) SELECT hallid, roll, name, fname, mname, dept, batch, entrydate, contno, email, bgrp, sex, rel, dob, peradd, presentadd, roomno, image, securitymoney, messad, idcard FROM stuinfo WHERE hallid = ?");
            ps.setInt(1, hallid);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Insertion on Previous Students Table Can't be Done for Hallid " + hallid, "Insertion Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date date = new Date();
        int dateInt = Integer.parseInt(formatDate.format(date));

        try {
            ps = conn.prepareStatement("UPDATE previousstudents SET outdate = ? WHERE hallid = ?");
            ps.setInt(1, dateInt);
            ps.setInt(2, hallid);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Upadte Outdate on Previous Students Table Can't be Done", "Insertion Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        deleteStdInfo(hallid);
    }

    public void deleteStdInfo(int hallid) {
        try {
            ps = conn.prepareStatement("DELETE FROM stuinfo WHERE hallid = ?");
            ps.setInt(1, hallid);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Deletion on Students Info Table Can't be Done", "Deletion Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showAll();
    }

    public String findHallId() {
        String id = idTxt.getText();
        if (!id.equals("")) {

            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE hallid = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE roll = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            JOptionPane.showMessageDialog(null, "Inserted Hall Id/Roll can't be found", "Data No Found", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return "";
    }

    public String showMsg(int row) {
        String msg = "Hall Id: " + tablemodel.getValueAt(row, 1).toString()
                + "\nName:  " + tablemodel.getValueAt(row, 2).toString()
                + "\nRoll: " + tablemodel.getValueAt(row, 3).toString()
                + "\n cant be deleted because of Due: " + tablemodel.getValueAt(row, 6).toString() + " Tk";
        return msg;
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        idTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stdDelTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_file_56px.png"))); // NOI18N
        jLabel1.setText("Delete Student's Info");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/identification_documents_24px.png"))); // NOI18N
        jLabel2.setText("Hall Id / Roll");

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_.png"))); // NOI18N
        jButton1.setText("Delete");
        jButton1.setBorder(null);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1206, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        stdDelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Hall Id", "Name", "Roll", "Dept", "Room No", "Total Due", "Security Money", "Mess Advance", "Id Card", "Contact"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(stdDelTable);
        if (stdDelTable.getColumnModel().getColumnCount() > 0) {
            stdDelTable.getColumnModel().getColumn(0).setMinWidth(40);
            stdDelTable.getColumnModel().getColumn(0).setMaxWidth(80);
            stdDelTable.getColumnModel().getColumn(1).setMinWidth(100);
            stdDelTable.getColumnModel().getColumn(1).setMaxWidth(150);
            stdDelTable.getColumnModel().getColumn(2).setMinWidth(250);
            stdDelTable.getColumnModel().getColumn(2).setMaxWidth(400);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int ch = 0;
        for (int i = 0; i < tablemodel.getRowCount(); i++) {
            Boolean bool = (Boolean) tablemodel.getValueAt(i, 0);
            if (bool) {
                ch++;
            }
        }

        if (ch == 0) {
            JOptionPane.showMessageDialog(null, "No Row Is Selected To Delete", "Can't Delete", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int responce = JOptionPane.showConfirmDialog(this, "Do you want to delete all " + ch + " entries ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION) {
            for (int i = 0; i < tablemodel.getRowCount(); i++) {
                Boolean bool = (Boolean) tablemodel.getValueAt(i, 0);
                if (bool) {
                    if (((Double) tablemodel.getValueAt(i, 6)) <= 0) {
                        updatePrevStd((int) tablemodel.getValueAt(i, 1));
                    } else {
                        String msg = showMsg(i);
                        JOptionPane.showMessageDialog(null, msg, "Deletion Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        if (!idTxt.equals("")) {
            showAll();
        } else {
            JOptionPane.showMessageDialog(null, "Enter A Hall Id / Roll", "Empty Id Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_idTxtActionPerformed

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
            java.util.logging.Logger.getLogger(StdInfoDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdInfoDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdInfoDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdInfoDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdInfoDelete().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField idTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable stdDelTable;
    // End of variables declaration//GEN-END:variables
}
