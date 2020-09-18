/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
public class StdInfoViewPrev extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    /**
     * Creates new form StdInfoView
     */
    public StdInfoViewPrev() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        //closeBtn();
        setCombo();
        setTitle("Current Students Info");

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

    }

    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    conn.close();
                    if (UserLog.name.equals("accountant")) {
                        DashboardAccountant das = new DashboardAccountant();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("provost")) {
                        DashboardHallAutho das = new DashboardHallAutho();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("mess")) {
                        DashboardMess das = new DashboardMess();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("captain")) {
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

    public void setCombo() {
        try {
            ps = conn.prepareStatement("SELECT dept from dept");
            rs = ps.executeQuery();
            while (rs.next()) {
                String dept = rs.getString(1);
                if (!dept.equals("")) {
                    deptCombo.addItem(dept);
                }
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
        }

        try {
            ps = conn.prepareStatement("SELECT DISTINCT hall from stuinfo WHERE hall IS NOT NULL");
            rs = ps.executeQuery();
            while (rs.next()) {
                String hall = rs.getString(1);
                if (!hall.equals("")) {
                    hallCombo.addItem(hall);
                }
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
        }

        deptCombo.setSelectedIndex(0);
        hallCombo.setSelectedIndex(0);
    }

    public void Tabledecoration() {
        infoTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 18));
        infoTable.getTableHeader().setOpaque(false);
        infoTable.getTableHeader().setBackground(new Color(32, 136, 203));
        infoTable.getTableHeader().setForeground(new Color(255, 255, 255));
        infoTable.setRowHeight(25);
        infoTable.setFont(new Font("Segeo UI", Font.PLAIN, 17));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        infoTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(11).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(12).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(13).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(14).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(15).setCellRenderer(centerRender);
        infoTable.getColumnModel().getColumn(16).setCellRenderer(centerRender);
    }

    public String findHallId() {
        String id = idTxt.getText();
        if (!id.equals("")) {
            try {
                ps = conn.prepareStatement("SELECT hallid FROM previousstudents WHERE hallid = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
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
                ps = conn.prepareStatement("SELECT hallid FROM previousstudents WHERE roll = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
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

    public void highlightStd(String id) {
        tablemodel = (DefaultTableModel) infoTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            int chk = 0;
            for (int i = 0; i < tablemodel.getRowCount(); i++) {//For each row in that column
                if (tablemodel.getValueAt(i, 1).toString().equals(id)) {//Search the model
                    infoTable.requestFocus();
                    infoTable.changeSelection(i, 1, false, false);
                    chk++;
                    break;
                }
            }//For loop inner
            if (chk == 0) {
                JOptionPane.showMessageDialog(null, id + " Not Found On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Values On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showInfo() {;
        String dept = (String) deptCombo.getSelectedItem();
        String hall = (String) hallCombo.getSelectedItem();

        String query = "";

        if (!dept.equals("All")) {
            query += " WHERE dept LIKE '" + dept + "%'";
            if (!hall.equals("All")) {
                switch (hall) {
                    case "Osmany Hall":
                        query += " AND roomno > 0 AND roomno < 999";
                        break;
                    default:
                        query += " AND roomno LIKE '" + hall.charAt(4) + "%'";
                        break;
                }
            }
        } else if (!hall.equals("All")) {
            idTxt.setText("");
            switch (hall) {
                case "Osmany Hall":
                    query += " WHERE roomno > 0 AND roomno < 999";
                    break;
                default:
                    query += " WHERE roomno LIKE '" + hall.charAt(4) + "%'";
                    break;
            }
            if (!dept.equals("All")) {
                query += " AND dept LIKE '" + dept + "%'";
            }
        }

        tablemodel = (DefaultTableModel) infoTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            ps = conn.prepareStatement("SELECT * FROM previousstudents" + query);
            rs = ps.executeQuery();
            int serial = 0;
            while (rs.next()) {
                serial++;
                String endate = "";
                String dob = "";
                try {
                    Date date = formatDate.parse(rs.getString(8));
                    endate = tableDateFormatter.format(date);
                    formatDate.parse(rs.getString(14));
                    dob = tableDateFormatter.format(date);
                } catch (ParseException ex) {
                    Logger.getLogger(StdInfoViewCur.class.getName()).log(Level.SEVERE, null, ex);
                }
                Object[] obj = {serial, rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),
                    rs.getString(5), rs.getString(6), endate, rs.getString(26), rs.getString(9),
                    rs.getString(10), rs.getString(11), dob, rs.getString(15),
                    rs.getString(16), rs.getString(18), rs.getDouble(19)};
                tablemodel.addRow(obj);
            }

            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        idTxt = new javax.swing.JTextField();
        deptCombo = new javax.swing.JComboBox<>();
        hallCombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/info_64px.png"))); // NOI18N
        jLabel1.setText("Previous Students Information View");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/identification_documents_24px.png"))); // NOI18N
        jLabel2.setText("Hall Id / Roll");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/user_group_24px.png"))); // NOI18N
        jLabel3.setText("Dept. ");

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/building_30px.png"))); // NOI18N
        jLabel4.setText("Hall Wise");

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        deptCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        deptCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        deptCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptComboActionPerformed(evt);
            }
        });

        hallCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        hallCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        hallCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hallComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1545, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hallCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hallCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        infoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL", "Hall Id", "Name", "Roll", "Father", "Mother", "Dept", "Entry Date", "Out Date", "Contact", "Email", "Blood Group", "Birthdate", "Pres. Add.", "Perm. Add.", "Room", "Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        infoTable.setSelectionBackground(new java.awt.Color(255, 51, 51));
        infoTable.setSelectionForeground(new java.awt.Color(255, 255, 252));
        jScrollPane1.setViewportView(infoTable);
        if (infoTable.getColumnModel().getColumnCount() > 0) {
            infoTable.getColumnModel().getColumn(0).setMinWidth(35);
            infoTable.getColumnModel().getColumn(0).setMaxWidth(60);
            infoTable.getColumnModel().getColumn(1).setMinWidth(80);
            infoTable.getColumnModel().getColumn(1).setMaxWidth(100);
            infoTable.getColumnModel().getColumn(2).setMinWidth(250);
            infoTable.getColumnModel().getColumn(2).setMaxWidth(350);
            infoTable.getColumnModel().getColumn(15).setMinWidth(80);
            infoTable.getColumnModel().getColumn(15).setMaxWidth(100);
            infoTable.getColumnModel().getColumn(16).setMinWidth(100);
            infoTable.getColumnModel().getColumn(16).setMaxWidth(250);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        String id = findHallId();
        if (!id.equals("")) {
            highlightStd(id);
        }
    }//GEN-LAST:event_idTxtActionPerformed

    private void deptComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptComboActionPerformed
        // TODO add your handling code here:
        idTxt.setText("");
        showInfo();
    }//GEN-LAST:event_deptComboActionPerformed

    private void hallComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hallComboActionPerformed
        // TODO add your handling code here:
        idTxt.setText("");
        showInfo();
    }//GEN-LAST:event_hallComboActionPerformed

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
            java.util.logging.Logger.getLogger(StdInfoViewPrev.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdInfoViewPrev.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdInfoViewPrev.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdInfoViewPrev.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdInfoViewPrev().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> deptCombo;
    private javax.swing.JComboBox<String> hallCombo;
    private javax.swing.JTextField idTxt;
    private javax.swing.JTable infoTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
