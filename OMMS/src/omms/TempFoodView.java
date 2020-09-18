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
import java.beans.PropertyChangeEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
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
public class TempFoodView extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    public TempFoodView() {
        initComponents();
        Tabledecoration();
        initialize();
        tempFoodViewTable.getColumnModel().getColumn(7).setCellRenderer(new WordWrapCellRenderer());
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers(); // setting todays date to the date chooser
        //setComboBoxMonthYear(); // setting the default month and Year as cuurent 
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
        //setTempFoodViewTable();
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) fromDateChooser.getDateEditor();
        dtedit.setEditable(false);
        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) toDateChooser.getDateEditor();
        dtedit.setEditable(false);
        
        
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

    /*
        Seeting the cross button action to Dashboard
     */
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

    /*
        Setting the date choosers to todays current date
     */
    public void setDateChoosers() {
        Date todaysDate = new Date();
        fromDateChooser.setDate(todaysDate);
        toDateChooser.setDate(todaysDate);
    }

    public void updateByFromToDateTFVTable(Date from, Date to) {

        //System.out.println("Date "+from);
        Date date = null;
        String strFromDate = "";
        String strToDate = "";
        String strDate = "";
        int intToDate = 0;
        int intFromDate = 0;
        int intDate = 0;
        int serialNo = 0;
        double totalBill = 0;

        tablemodel = (DefaultTableModel) tempFoodViewTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            strFromDate = formatDate.format(from);
            //System.out.println("Date"+strFromDate);
            intFromDate = Integer.parseInt(strFromDate);
            strToDate = formatDate.format(to);
            //System.out.println("Date"+strFromDate);
            intToDate = Integer.parseInt(strToDate);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Date format in update table", "Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ps = conn.prepareStatement("SELECT name, roll, hallid, roomno, dateserial, bill,remarks  from tempfood JOIN stuinfo USING (hallid) ORDER BY dateserial, hallid");
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
                    try {
                        totalBill += rs.getDouble(6);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Cant Get Bill from Temp Food Table", "Data parsing error", JOptionPane.ERROR_MESSAGE);
                    }
                    serialNo++;
                    Object o[] = {serialNo, rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), strDate, rs.getDouble(6),rs.getString(7)};
                    tablemodel.addRow(o);
                }
            }
            totalBillJlbl.setText("Total Bill : " + totalBill + "Tk");
            ps.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateByFromToDateIdTFVTable(Date from, Date to, String id) {

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

        tablemodel = (DefaultTableModel) tempFoodViewTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

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
                ps = conn.prepareStatement("SELECT name, roll, hallid, roomno, dateserial, bill  from tempfood JOIN stuinfo USING (hallid) WHERE hallid = ? ORDER BY dateserial, hallid");
                ps.setInt(1, Id);
            } else if (id.length() == 9) {
                ps = conn.prepareStatement("SELECT name, roll, hallid, roomno, dateserial, bill  from tempfood JOIN stuinfo USING (hallid) WHERE roll = ? ORDER BY dateserial, hallid");
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
                    try {
                        totalBill += rs.getDouble(6);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Cant Get Bill from Temp Food Table", "Data parsing error", JOptionPane.ERROR_MESSAGE);
                    }
                    //System.out.println("Entered Idf to");
                    serialNo++;
                    Object o[] = {serialNo, rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), strDate, rs.getDouble(6)};
                    tablemodel.addRow(o);
                }

            }
            totalBillJlbl.setText("Total Bill : " + totalBill + "Tk");

            ps.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Tabledecoration() {
        tempFoodViewTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        tempFoodViewTable.getTableHeader().setOpaque(false);
        tempFoodViewTable.getTableHeader().setBackground(new Color(32, 136, 203));
        tempFoodViewTable.getTableHeader().setForeground(new Color(255, 255, 255));
        tempFoodViewTable.setRowHeight(25);
        tempFoodViewTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tempFoodViewTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        tempFoodViewTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);

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
        idTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tempFoodViewTable = new javax.swing.JTable();
        totalBillJlbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        tempFoodViewJpanel.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        tempFoodViewJpanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tempFoodViewJpanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/tempfoodupdate.png"))); // NOI18N
        tempFoodViewJpanel.setText("Temporary Food View");

        idTxt.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel1.setText("ID");

        fromDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromDateChooserPropertyChange(evt);
            }
        });

        toDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        toDateChooser.setPreferredSize(new java.awt.Dimension(150, 22));
        toDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                toDateChooserPropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("From");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel3.setText("To");

        tempFoodViewTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Name", "Roll", "Hall Id", "Room No", "Date", "Tk", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tempFoodViewTable.setSelectionBackground(new java.awt.Color(237, 57, 97));
        tempFoodViewTable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        tempFoodViewTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tempFoodViewTable);
        if (tempFoodViewTable.getColumnModel().getColumnCount() > 0) {
            tempFoodViewTable.getColumnModel().getColumn(0).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            tempFoodViewTable.getColumnModel().getColumn(1).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            tempFoodViewTable.getColumnModel().getColumn(2).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(2).setPreferredWidth(30);
            tempFoodViewTable.getColumnModel().getColumn(3).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(3).setPreferredWidth(15);
            tempFoodViewTable.getColumnModel().getColumn(4).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(4).setPreferredWidth(15);
            tempFoodViewTable.getColumnModel().getColumn(5).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(5).setPreferredWidth(30);
            tempFoodViewTable.getColumnModel().getColumn(6).setResizable(false);
            tempFoodViewTable.getColumnModel().getColumn(6).setPreferredWidth(20);
        }

        totalBillJlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalBillJlbl.setForeground(new java.awt.Color(255, 51, 0));
        totalBillJlbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tempFoodViewJpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(totalBillJlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(tempFoodViewJpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(toDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fromDateChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalBillJlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
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
        
        tablemodel = (DefaultTableModel) tempFoodViewTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            id = idTxt.getText().trim();
            int err = Integer.parseInt(id);
            // System.out.println(" To " + formatDate.format(toDate) + " Todays " + formatDate.format(new Date()));
            if (!id.equals("")) {
                //System.out.println("Search by id and from date in idTxt");
                updateByFromToDateIdTFVTable(fromDate, toDate, id);
            } else {
                //System.out.println("Search by to and from date in idTxt");
                updateByFromToDateTFVTable(fromDate, toDate);
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
        tablemodel = (DefaultTableModel) tempFoodViewTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        if (fromDate != null && toDate != null) {
            if (Integer.parseInt(formatDate.format(fromDate)) <= Integer.parseInt(formatDate.format(toDate))) {
                if (id.equals("")) {
                    System.out.println("Search By From N To Date in from Date");
                    updateByFromToDateTFVTable(fromDate, toDate);
                } else {
                    System.out.println("Search By From & To Date & Id in from Date");
                    updateByFromToDateIdTFVTable(fromDate, toDate, id);
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

        tablemodel = (DefaultTableModel) tempFoodViewTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        //System.out.println("Print Id " + id + " From " + fromDate + " To " + toDate);
        if (fromDate != null && toDate != null) {
            if (Integer.parseInt(formatDate.format(fromDate)) <= Integer.parseInt(formatDate.format(toDate))) {
                if (id.equals("")) {
                    System.out.println("Search By From N To Date in To Date");
                    updateByFromToDateTFVTable(fromDate, toDate);
                } else {
                    System.out.println("Search By From & To Date & Id in To Date");
                    updateByFromToDateIdTFVTable(fromDate, toDate, id);
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
            java.util.logging.Logger.getLogger(TempFoodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TempFoodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TempFoodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TempFoodView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TempFoodView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel tempFoodViewJpanel;
    private javax.swing.JTable tempFoodViewTable;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JLabel totalBillJlbl;
    // End of variables declaration//GEN-END:variables
}
