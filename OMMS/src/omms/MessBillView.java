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
public class MessBillView extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    int flag = 0;

    /**
     * Creates new form MessBillView
     */
    public MessBillView() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers();
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
    }

    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    NewDashboard das = new NewDashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
    }

    public void setDateChoosers() {
        Date date = new Date();
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        monthCombo.setSelectedIndex(Integer.parseInt(month.format(date)) - 1);
        yearTxt.setText(year.format(date));
    }

    public void Tabledecoration() {
        showBillTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        showBillTable.getTableHeader().setOpaque(false);
        showBillTable.getTableHeader().setBackground(new Color(32, 136, 203));
        showBillTable.getTableHeader().setForeground(new Color(255, 255, 255));
        showBillTable.setRowHeight(25);
        showBillTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        showBillTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(11).setCellRenderer(centerRender);
    }

    public void showBill() {
        int month = monthCombo.getSelectedIndex() + 1;
        int year;
        if (!yearTxt.getText().equals("")) {
            year = Integer.parseInt(yearTxt.getText());
            if (year < 0) {
                JOptionPane.showMessageDialog(null, "Year Can't be Negetive", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Year field is empty", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int serial = 0;

        tablemodel = (DefaultTableModel) showBillTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, bill, others, fine, waive, due FROM stuinfo JOIN billhistory USING(hallid) WHERE month = ? AND year = ? ORDER BY hallid");
            ps.setInt(1, month);
            ps.setInt(2, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                int bill = rs.getInt(5);
                int others = rs.getInt(6);
                int fine = rs.getInt(7);
                int waive = rs.getInt(8);
                int due = rs.getInt(9);
                String str = "";
                serial++;
                int total = bill + others + fine - waive + due;
                str = Integer.toString(total);
                if (total < 0) {
                    total *= -1;
                    str = Integer.toString(total);
                    str += "(A)";
                }
                if (due < 0) {
                    due *= -1;
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, 0, due, str};
                    tablemodel.addRow(obj);
                } else {
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, due, 0, str};
                    tablemodel.addRow(obj);
                }

            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error From stuinfo and billhistory", "Database ERROR!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public void highlightStd(String id) {
        tablemodel = (DefaultTableModel) showBillTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            for (int j = 1; j <=3; j+=2) {//For each column
                for (int i = 0; i < tablemodel.getRowCount(); i++) {//For each row in that column
                    if (tablemodel.getValueAt(i, j).toString().equals(id)) {//Search the model
                        showBillTable.requestFocus();
                        showBillTable.changeSelection(i,j,false, false);
                        break;
                    }
                }//For loop inner
            }//For loop outer
            JOptionPane.showMessageDialog(null, id + " Not Found On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No Values On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
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
        monthCombo = new javax.swing.JComboBox<>();
        yearTxt = new javax.swing.JTextField();
        idTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        showBillTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/billMess.png"))); // NOI18N
        jLabel1.setText("MESS BILL VIEW");

        monthCombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        monthCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        monthCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthComboActionPerformed(evt);
            }
        });

        yearTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        yearTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearTxtActionPerformed(evt);
            }
        });

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthMess.png"))); // NOI18N
        jLabel2.setText(" Month");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/yearMess.png"))); // NOI18N
        jLabel3.setText(" Year");

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/identification_documents_24px.png"))); // NOI18N
        jLabel4.setText(" Hall Id / Roll");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(145, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(171, 171, 171)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(183, 183, 183)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 100, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        showBillTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SN", "Hall Id", "Name", "Roll", "Room No", "Meal Charge", "Others", "Fine", "Waive", "Previous Due", "Advance", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(showBillTable);
        if (showBillTable.getColumnModel().getColumnCount() > 0) {
            showBillTable.getColumnModel().getColumn(0).setMinWidth(45);
            showBillTable.getColumnModel().getColumn(0).setMaxWidth(60);
            showBillTable.getColumnModel().getColumn(1).setMinWidth(80);
            showBillTable.getColumnModel().getColumn(1).setMaxWidth(100);
            showBillTable.getColumnModel().getColumn(2).setMinWidth(225);
            showBillTable.getColumnModel().getColumn(2).setMaxWidth(500);
            showBillTable.getColumnModel().getColumn(9).setMinWidth(125);
            showBillTable.getColumnModel().getColumn(9).setMaxWidth(150);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void yearTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearTxtActionPerformed
        // TODO add your handling code here:
        showBill();
    }//GEN-LAST:event_yearTxtActionPerformed

    private void monthComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            showBill();
        }
        flag = 1;
    }//GEN-LAST:event_monthComboActionPerformed

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        if (!idTxt.getText().equals("")) {
            highlightStd(idTxt.getText().trim());
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
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MessBillView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> monthCombo;
    private javax.swing.JTable showBillTable;
    private javax.swing.JTextField yearTxt;
    // End of variables declaration//GEN-END:variables
}
