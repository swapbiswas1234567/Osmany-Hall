/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Asus
 */
public class AccountPaymentDelete extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    PreparedStatement psd = null;
    ResultSet rsd = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    int check = 0;
    int tc = 0;

    /**
     * Creates new form AccountPaymentDelete
     */
    public AccountPaymentDelete() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers();
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        //closeBtn();

        JTextFieldDateEditor jt = new JTextFieldDateEditor();
        jt = (JTextFieldDateEditor) fromDate.getDateEditor();
        jt.setEditable(false);

        jt = (JTextFieldDateEditor) toDate.getDateEditor();
        jt.setEditable(false);
        tablemodel = (DefaultTableModel) payHistTable.getModel();
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

    public void setDateChoosers() {
        Date date = new Date();
        fromDate.setDate(date);
        toDate.setDate(date);
        callShowTableFunction();
    }

    public void callShowTableFunction() {
        Date from = fromDate.getDate();
        Date to = toDate.getDate();
        String id = findHallId();
        showPayFromToDate(from, to, id);
    }

    public void Tabledecoration() {
        payHistTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        payHistTable.getTableHeader().setOpaque(false);
        payHistTable.getTableHeader().setBackground(new Color(32, 136, 203));
        payHistTable.getTableHeader().setForeground(new Color(255, 255, 255));
        payHistTable.setRowHeight(25);
        payHistTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        //payHistTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        payHistTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);
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

    public void showPayFromToDate(Date from, Date to, String id) {
        int serial = 0;
        String query = "";
        Date date;
        if (!id.equals("")) {
            query = "AND hallid = " + id;
        }

        tablemodel = (DefaultTableModel) payHistTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }
        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, payid, paidamount, paymentdate, insertdate, contno FROM stuinfo JOIN paymenthistory USING(hallid) WHERE paymentdate >= ? AND paymentdate <= ? " + query + " ORDER BY paymentdate");
            ps.setInt(1, Integer.parseInt(formatDate.format(from)));
            ps.setInt(2, Integer.parseInt(formatDate.format(to)));
            rs = ps.executeQuery();
            while (rs.next()) {
                serial++;
                String pd;
                String ind;
                try {
                    date = formatDate.parse(String.valueOf(rs.getInt(7)));
                    pd = tableDateFormatter.format(date);
                    date = formatDate.parse(String.valueOf(rs.getInt(8)));
                    ind = tableDateFormatter.format(date);
                } catch (SQLException | ParseException e) {
                    JOptionPane.showMessageDialog(null, "Date parse in set Temp food table", "Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object obj[] = {false, serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDouble(6), pd, ind, rs.getString(9)};
                tablemodel.addRow(obj);
            }
            ps.close();
            rs.close();
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Payments Cannot Be Inserted On Table", "Table Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void paymentDelete(int row) {
        int hallid = (int) tablemodel.getValueAt(row, 2);
        Double paidAmnt = (Double) tablemodel.getValueAt(row, 7);
        Date date;
        String pd = "";

        try {
            date = tableDateFormatter.parse(tablemodel.getValueAt(row, 8).toString());
            pd = formatDate.format(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Date cannot be parsed from table", "Parsing Error", JOptionPane.ERROR_MESSAGE);
        }

        int pid = (int) tablemodel.getValueAt(row, 6);

        try {
            ps = conn.prepareStatement("DELETE FROM paymenthistory WHERE hallid = ? AND paidamount = ?  AND paymentdate = ? AND payid = ?");
            ps.setInt(1, hallid);
            ps.setDouble(2, paidAmnt);
            ps.setString(3, pd);
            ps.setInt(4, pid);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Payments cannot be deleted from table", "Deletion Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            ps = conn.prepareStatement("UPDATE stuinfo SET totaldue = totaldue + ? WHERE hallid = ?");
            ps.setDouble(1, paidAmnt);
            ps.setInt(2, hallid);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Due cannot be updated from table", "Deletion Error", JOptionPane.ERROR_MESSAGE);
        }
        tableRedeco(row);
    }

    public void tableRedeco(int row) {
        tablemodel.removeRow(row);
        for (int i = row; i < tablemodel.getRowCount(); i++) {
            tablemodel.setValueAt(i + 1, i, 1);
        }
    }

    public String generateString(int row) {
        String str = "Do you want to delete payment of " + tablemodel.getValueAt(row, 3) + "\nHallid: " + tablemodel.getValueAt(row, 2) + "\nPayment Id: " + tablemodel.getValueAt(row, 6) + "\nAmount: " + tablemodel.getValueAt(row, 7) + "\nDate: " + tablemodel.getValueAt(row, 8) + " ?";
        return str;
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
        jLabel3 = new javax.swing.JLabel();
        fromDate = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        toDate = new com.toedter.calendar.JDateChooser();
        delPayBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        payHistTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Delete Payment");

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_pay_64px.png"))); // NOI18N
        jLabel1.setText("Delete Payment");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/identification_documents_24px.png"))); // NOI18N
        jLabel2.setText("Hall Id / Roll");

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/start.png"))); // NOI18N
        jLabel3.setText("From");

        fromDate.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromDatePropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/end.png"))); // NOI18N
        jLabel4.setText("To");

        toDate.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        toDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toDatePropertyChange(evt);
            }
        });

        delPayBtn.setBackground(new java.awt.Color(255, 102, 102));
        delPayBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        delPayBtn.setForeground(new java.awt.Color(255, 255, 255));
        delPayBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_1.png"))); // NOI18N
        delPayBtn.setText("Delete");
        delPayBtn.setBorder(null);
        delPayBtn.setFocusPainted(false);
        delPayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delPayBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toDate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(delPayBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fromDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(toDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(delPayBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        payHistTable.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        payHistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "SL", "Hall Id", "Name", "Roll", "Room No", "Payment Id", "Paid Amount", "Payment Date", "Inserted Date", "Mobile"
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
        jScrollPane1.setViewportView(payHistTable);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        if (!idTxt.getText().equals("")) {
            callShowTableFunction();
        } else {
            JOptionPane.showMessageDialog(null, "Enter A Hall Id / Roll", "Empty Id Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }//GEN-LAST:event_idTxtActionPerformed

    private void fromDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromDatePropertyChange
        // TODO add your handling code here:
        Date from = fromDate.getDate();
        Date to = toDate.getDate();
        String id = findHallId();

        if (from != null && to != null) {
            if (Integer.parseInt(formatDate.format(from)) <= Integer.parseInt(formatDate.format(to))) {
                if (check == 0) {
                    showPayFromToDate(from, to, id);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invail From Date Chosen", "Wrong Date Inserted", JOptionPane.ERROR_MESSAGE);
            }
        }
        idTxt.requestFocus();
    }//GEN-LAST:event_fromDatePropertyChange

    private void toDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_toDatePropertyChange
        // TODO add your handling code here:
        Date from = fromDate.getDate();
        Date to = toDate.getDate();
        String id = findHallId();

        if (from != null && to != null) {
            if (Integer.parseInt(formatDate.format(from)) <= Integer.parseInt(formatDate.format(to))) {
                if (check == 0 && tc >= 1) {
                    showPayFromToDate(from, to, id);
                }
                tc++;
            } else {
                JOptionPane.showMessageDialog(null, "Invail To Date Chosen", "Wrong Date Inserted", JOptionPane.ERROR_MESSAGE);
            }
        }
        idTxt.requestFocus();
    }//GEN-LAST:event_toDatePropertyChange

    private void delPayBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delPayBtnActionPerformed
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
                    paymentDelete(i);
                }
            }
        }

    }//GEN-LAST:event_delPayBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AccountPaymentDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountPaymentDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountPaymentDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountPaymentDelete.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountPaymentDelete().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton delPayBtn;
    private com.toedter.calendar.JDateChooser fromDate;
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable payHistTable;
    private com.toedter.calendar.JDateChooser toDate;
    // End of variables declaration//GEN-END:variables

}
