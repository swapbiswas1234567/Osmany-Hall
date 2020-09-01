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

    public void setDeleteTable() {
        Date date = dateChoser.getDate();
        String state = stateComboBox.getSelectedItem().toString().toLowerCase();
        int dateInt = Integer.parseInt(formatDate.format(date));

        //System.out.println("In the Set table");
        //System.out.println("Date " + dateInt + " State " + state);
        //String[] grpNames;
        ArrayList<String> grpNames = new ArrayList<String>();
        int noOfGrps = 0;
        HashMap<String, DelGrpItem> itemSearch = new HashMap<String, DelGrpItem>();

        int serial = 0;

        tablemodel = (DefaultTableModel) deleteGroupTable.getModel();
        if (tablemodel.getColumnCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            //Extracting Group Names
//            ps = conn.prepareStatement("SELECT COUNT(name) FROM grp WHERE date = ? AND state = ?");
//            ps.setInt(1, dateInt);
//            ps.setString(2, state);
//            rs = ps.executeQuery();
//            int groupCount = 0;
//            groupCount = rs.getInt(1);
//            if (groupCount != 0) {
//                grpNames = new String[groupCount];
//                ps.close();
//                rs.close();
//            } else {
//                ps.close();
//                rs.close();
//                return;
//            }

            ps = conn.prepareStatement("SELECT name FROM grp WHERE date = ? AND state = ?");
            ps.setInt(1, dateInt);
            ps.setString(2, state);
            rs = ps.executeQuery();
            //System.out.println("Date " + dateInt + " State " + state);
            while (rs.next()) {
                //String st = ;
                grpNames.add(rs.getString(1));
                //System.out.println("Items \n" + grpNames[noOfGrps] + "No of items: " + noOfGrps);
                noOfGrps++;
            }
            ps.close();
            rs.close();
            //System.out.println(grpNames + " " + grpNames.size());
            if (noOfGrps == 0) {
                return;
            }

            itemSearch.put(grpNames.get(0), new DelGrpItem(grpNames.get(0), state, dateInt));
//            //Extracting Non Stored Item Names
//            ps = conn.prepareStatement("SELECT COUNT(nonstoreditem.name) FROM grp JOIN nonstoreditem ON grp.date = nonstoreditem.serial WHERE grp.date = ? AND grp.state = ? AND grp.name = ?");
//            ps.setInt(1, dateInt);
//            ps.setString(2, state);
//            ps.setString(3, grpNames[0]);
//            rs = ps.executeQuery();
//            groupCount = 0;
//            groupCount = rs.getInt(1);
//            System.out.println("Non Stored " + groupCount);
//            if (groupCount != 0) {
//                nonStoredItems = new String[groupCount];
//                ps.close();
//                rs.close();
//            } else {
//                ps.close();
//                rs.close();
//                return;
//            }
//
//            ps = conn.prepareStatement("SELECT nonstoreditem.name FROM grp JOIN nonstoreditem ON grp.date = nonstoreditem.serial WHERE grp.date = ? AND grp.state = ? AND grp.name = ?");
//            ps.setInt(1, dateInt);
//            ps.setString(2, state);
//            ps.setString(3, grpNames[0]);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                nonStoredItems[noOfNonStroredItems] = rs.getString(1);
//                System.out.println("Items \n" + nonStoredItems[noOfNonStroredItems] + " No of items: " + noOfNonStroredItems);
//                noOfNonStroredItems++;
//            }
//            ps.close();
//            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data from Database", "Database Error", JOptionPane.ERROR_MESSAGE);
            //return;
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

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel3.setText("State");

        deleteBtn.setBackground(new java.awt.Color(255, 102, 102));
        deleteBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete_.png"))); // NOI18N
        deleteBtn.setText("Delete");

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
