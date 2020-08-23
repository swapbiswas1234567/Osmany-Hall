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
public class stdIndBillStat extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    /**
     * Creates new form stdIndBillStat
     */
    public stdIndBillStat() {
        initComponents();
        Tabledecoration();
        initialize();

    }

    public void Tabledecoration() {
        mealSheetJtable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        mealSheetJtable.getTableHeader().setOpaque(false);
        mealSheetJtable.getTableHeader().setBackground(new Color(32, 136, 203));
        mealSheetJtable.getTableHeader().setForeground(new Color(255, 255, 255));
        mealSheetJtable.setRowHeight(25);
        mealSheetJtable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        mealSheetJtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        mealSheetJtable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        mealSheetJtable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        mealSheetJtable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        mealSheetJtable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        mealSheetJtable.getColumnModel().getColumn(5).setCellRenderer(centerRender);

    }

    public void initialize() {
        conn = Jconnection.ConnecrDb();
        setDateChooser();
        closeBtn();
        idTxt.requestFocus();
    }

    public void setDateChooser() {
        Date todaysDate = new Date();
        fromDateChooser.setDate(todaysDate);
        toDateChooser.setDate(todaysDate);
    }

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

    public void updateByFromToDateIdStdMealTable(Date from, Date to, String id) {

        //System.out.println("Date "+from);
        Date date = null;
        String strFromDate = "";
        String strToDate = "";
        String strDate = "";
        int intToDate = 0;
        int intFromDate = 0;
        int intDate = 0;
        int serialNo = 0;
        int Id = 0;
        double totalBill = 0;
        double total = 0;

        tablemodel = (DefaultTableModel) mealSheetJtable.getModel();

        try {
            strFromDate = formatDate.format(from);
            //System.out.println("Date"+strFromDate);
            intFromDate = Integer.parseInt(strFromDate);
            strToDate = formatDate.format(to);
            //System.out.println("Date"+strFromDate);
            intToDate = Integer.parseInt(strToDate);
            Id = Integer.parseInt(id);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Date format in update table", "Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (id.length() == 4) {
                ps = conn.prepareStatement("SELECT name, roll, hallid, roomno, date, breakfast, lunch, dinner FROM stuinfo JOIN mealsheet USING (hallid) WHERE hallid = ? ORDER BY date");
                ps.setInt(1, Id);
            } else if (id.length() == 9) {
                ps = conn.prepareStatement("SELECT name, roll, hallid, roomno, date, breakfast, lunch, dinner FROM stuinfo JOIN mealsheet USING (hallid) WHERE roll = ? ORDER BY date");
                ps.setString(1, id);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    date = formatDate.parse(rs.getString(5));
                    strDate = tableDateFormatter.format(date);
                    intDate = Integer.parseInt(formatDate.format(date));
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Date parse in set Temp food table", "Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (intDate >= intFromDate && intDate <= intToDate) {
                    //System.out.println("Entered Idf to");
                    serialNo++;
                    total = rs.getInt(6) + rs.getInt(7) + rs.getInt(8);
                    totalBill += total;
                    Object o[] = {serialNo, strDate, rs.getInt(6), rs.getInt(7), rs.getInt(8), total};
                    tablemodel.addRow(o);
                }
                if (serialNo == 0) {
                    nameJlbl.setText("Name:              " + rs.getString(1));
                    stdIdJlbl.setText("Student Id:      " + rs.getString(2));
                    hallIdJlbl.setText("Hall Id:            " + String.valueOf(rs.getInt(3)));
                    roomNoJlbl.setText("Room No:        " + rs.getString(4));
                }

            }
            totalBillJlbl.setText("Total Bill :       " + totalBill + "Tk");

            ps.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
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
        idTxt = new javax.swing.JTextField();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        nameJlbl = new javax.swing.JLabel();
        stdIdJlbl = new javax.swing.JLabel();
        hallIdJlbl = new javax.swing.JLabel();
        roomNoJlbl = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        totalBillJlbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mealSheetJtable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 204, 204));

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel1.setText("ID");

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        fromDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromDateChooserPropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText(" From");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel3.setText(" To");

        toDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        toDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toDateChooserPropertyChange(evt);
            }
        });

        nameJlbl.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        nameJlbl.setText("Name:");

        stdIdJlbl.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        stdIdJlbl.setText("Student Id: ");

        hallIdJlbl.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        hallIdJlbl.setText("Hall Id: ");

        roomNoJlbl.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        roomNoJlbl.setText("Room No: ");

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel10.setText(" Student's Daily Meal Sheet");

        totalBillJlbl.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        totalBillJlbl.setText("Total Bill: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(stdIdJlbl, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                        .addComponent(hallIdJlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(roomNoJlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(totalBillJlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(nameJlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(280, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameJlbl)
                    .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(stdIdJlbl)
                        .addGap(10, 10, 10)
                        .addComponent(hallIdJlbl)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(roomNoJlbl)
                        .addGap(17, 17, 17)
                        .addComponent(totalBillJlbl))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28))
        );

        mealSheetJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serail", "Date", "Breakfast", "Launch", "Dinner", "Total"
            }
        ));
        jScrollPane1.setViewportView(mealSheetJtable);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        Date fromDate = null;
        Date toDate = null;
        //Date todaysDate = new Date();
        String id = "";

        fromDate = fromDateChooser.getDate();
        toDate = toDateChooser.getDate();

        String todaysDate = formatDate.format(new Date()).trim();
        String temp = formatDate.format(fromDate).trim();
        tablemodel = (DefaultTableModel) mealSheetJtable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            id = idTxt.getText().trim();
            int err = Integer.parseInt(id);
            // System.out.println(" To " + formatDate.format(toDate) + " Todays " + formatDate.format(new Date()));
            if (!id.equals("") && id.length() == 4 || id.length() == 9) {
                //System.out.println("Search by id and from date in idTxt");
                updateByFromToDateIdStdMealTable(fromDate, toDate, id);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Id inserted, Enter a valid Roll no or Hall Id", "Wrong Input", JOptionPane.ERROR_MESSAGE);
            }
            //System.out.println(strFromDate);
            idTxt.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Id inserted, Enter a valid Roll no or Hall Id", "Wrong Input", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_idTxtActionPerformed

    private void fromDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromDateChooserPropertyChange
        // TODO add your handling code here:
        Date fromDate = null;
        Date toDate = null;
        String id = "";

        fromDate = fromDateChooser.getDate();
        toDate = toDateChooser.getDate();
        id = idTxt.getText().trim();

        //System.out.println("Print Id " + id + " From " + fromDate + " To " + toDate);
        tablemodel = (DefaultTableModel) mealSheetJtable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        if (fromDate != null && toDate != null) {
            if (Integer.parseInt(formatDate.format(fromDate)) <= Integer.parseInt(formatDate.format(toDate))) {
                if (!id.equals("")) {
                    System.out.println("Search By From & To Date & Id in from Date");
                    updateByFromToDateIdStdMealTable(fromDate, toDate, id);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invail From Date Chosen", "Wrong Date Inserted", JOptionPane.ERROR_MESSAGE);
                //setTempFoodViewTable();
            }
        }

        //System.out.println(strFromDate);
        idTxt.requestFocus();
    }//GEN-LAST:event_fromDateChooserPropertyChange

    private void toDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_toDateChooserPropertyChange
        // TODO add your handling code here:
        Date fromDate = null;
        Date toDate = null;
        String id = "";

        fromDate = fromDateChooser.getDate();
        toDate = toDateChooser.getDate();
        id = idTxt.getText();

        tablemodel = (DefaultTableModel) mealSheetJtable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        //System.out.println("Print Id " + id + " From " + fromDate + " To " + toDate);
        if (fromDate != null && toDate != null) {
            if (Integer.parseInt(formatDate.format(fromDate)) <= Integer.parseInt(formatDate.format(toDate))) {
                if (!id.equals("")) {
                    System.out.println("Search By From & To Date & Id in To Date");
                    updateByFromToDateIdStdMealTable(fromDate, toDate, id);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invail To Date Chosen", "Wrong Date Inserted", JOptionPane.ERROR_MESSAGE);
                //setTempFoodViewTable();
            }
        }
        //System.out.println(strFromDate);
        idTxt.requestFocus();
    }//GEN-LAST:event_toDateChooserPropertyChange

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
            java.util.logging.Logger.getLogger(stdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stdIndBillStat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JLabel hallIdJlbl;
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable mealSheetJtable;
    private javax.swing.JLabel nameJlbl;
    private javax.swing.JLabel roomNoJlbl;
    private javax.swing.JLabel stdIdJlbl;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JLabel totalBillJlbl;
    // End of variables declaration//GEN-END:variables
}
