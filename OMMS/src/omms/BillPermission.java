/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class BillPermission extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag = 0;

    /**
     * Creates new form BillPermission
     */
    public BillPermission() {
        initComponents();
        conn = Jconnection.ConnecrDb(); // set connection with database
        initialize();
    }

    public void initialize() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yeartxt.setText(Integer.toString(year));

        int month = Calendar.getInstance().get(Calendar.MONTH);
        //System.out.println(month+" "+year);
        monthcombo.setSelectedIndex(month);
        closeBtn();
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

    public int[] checkprevious(int year, int month) {
        int[] permission = new int[2];
        permission[0] = -1;
        permission[1] = -1;
        try {
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select permission,generated from billpermission where year=? and month=?");
            psmt.setInt(1, year);
            psmt.setInt(2, month);
            rs = psmt.executeQuery();

            while (rs.next()) {
                permission[0] = rs.getInt(1);
                permission[1] = rs.getInt(2);
            }

            psmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to check previous permissions", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return permission;
    }

    public String checkpending() {
        String time = "";

        try {
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select month,year from billpermission where generated=0");
            rs = psmt.executeQuery();

            while (rs.next()) {
                time = rs.getString(1) + ", " + rs.getString(2);
            }

            psmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to check pending", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return time;

    }

    public boolean getlastmonth(int setmonth, int setyear) {
        int year = 0, month = 0;

        try {
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select year,max(month) from billpermission ");
            rs = psmt.executeQuery();

            while (rs.next()) {
                year = rs.getInt(1);
                month = rs.getInt(2);
            }

            psmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to get last billgenerated month", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //System.out.println(setmonth+" "+month);

        if (setyear == year && setmonth - 1 == month) {
            return true;
        } else if (setyear == year && setmonth - 1 > month) {

            int responce = JOptionPane.showConfirmDialog(this, "No bill has generated in " + monthcombo.getItemAt(setmonth - 2) + "\n do you want to skip " + monthcombo.getItemAt(setmonth - 2)
                    + " and give permission for " + monthcombo.getItemAt(setmonth - 1), "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            return JOptionPane.YES_OPTION == responce;
        } else {
            return true;
        }

    }

    public void givepermission(int year, int month) {
        try {
            psmt = conn.prepareStatement("insert into billpermission(year,month,permission,generated) values(?,?,1,0)");
            psmt.setInt(1, year);
            psmt.setInt(2, month);
            psmt.execute();
            psmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void updatepermission(int permission, int year, int month) {
        try {
            psmt = conn.prepareStatement("update billpermission set permission=? where month=? and year=?");
            psmt.setInt(1, permission);
            psmt.setInt(2, month);
            psmt.setInt(3, year);
            psmt.execute();
            psmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void deletepermission(int year, int month) {
        try {
            psmt = conn.prepareStatement("delete from billpermission where month=? and year=?");
            psmt.setInt(1, month);
            psmt.setInt(2, year);
            psmt.execute();
            psmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Data delete error", JOptionPane.ERROR_MESSAGE);

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
        monthcombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        yeartxt = new javax.swing.JTextField();
        pemissionbtn = new javax.swing.JButton();
        revokebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/unlock.png"))); // NOI18N
        jLabel1.setText("Bill Generation Permission");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthly.png"))); // NOI18N
        jLabel2.setText("Month");

        monthcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        monthcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/year.png"))); // NOI18N
        jLabel3.setText("Year");

        yeartxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        pemissionbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        pemissionbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/permission.png"))); // NOI18N
        pemissionbtn.setText("Permit");
        pemissionbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pemissionbtnActionPerformed(evt);
            }
        });

        revokebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        revokebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/wrong.png"))); // NOI18N
        revokebtn.setText("Revoke");
        revokebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revokebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(monthcombo, 0, 140, Short.MAX_VALUE)
                    .addComponent(yeartxt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(137, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pemissionbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(revokebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pemissionbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(revokebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pemissionbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pemissionbtnActionPerformed
        // TODO add your handling code here:
        String stryear = "", strmonth = "", pending = "";
        int month = 0, year = 0;
        int[] permission = new int[2];
        permission[0] = -1;
        permission[1] = -1;
        boolean val = false;

        stryear = yeartxt.getText().trim();
        month = monthcombo.getSelectedIndex() + 1;
        strmonth = monthcombo.getSelectedItem().toString();
        try {
            year = Integer.parseInt(stryear);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "year Format error", "Update error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (year > 0) {
            permission = checkprevious(year, month);
            if (permission[0] == 1 && permission[1] == 1) {
                JOptionPane.showMessageDialog(null, "Already Permission has giver for " + strmonth + ", " + year + " & bill"
                        + " has been generated", "Update error", JOptionPane.ERROR_MESSAGE);
            } else if (permission[0] == 1 && permission[1] == 0) {
                JOptionPane.showMessageDialog(null, "Already Permission has giver for " + strmonth + ", " + year + "\n but bill"
                        + " has not generated", "Update error", JOptionPane.ERROR_MESSAGE);
            } else if (permission[0] == -1 && permission[1] == -1) {
                //System.out.println("called");
                int responce = JOptionPane.showConfirmDialog(this, "Do you want to permit for bill generation of " + strmonth + ", " + stryear, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (responce == JOptionPane.YES_OPTION) {

                    pending = checkpending();
                    if (pending.equals("")) {
                        val = getlastmonth(month, year);
                        if (val) {
                            givepermission(year, month);
                        }

                    } else if (!pending.equals("")) {
                        JOptionPane.showMessageDialog(null, "Already Permission has giver for " + pending + "\n but bill"
                                + " has not generated", "Update error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "Year is not valid ", "Update error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_pemissionbtnActionPerformed

    private void revokebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revokebtnActionPerformed
        // TODO add your handling code here:

        String stryear = "", strmonth = "";
        int month = 0, year = 0;
        int[] permission = new int[2];
        permission[0] = -1;
        permission[1] = -1;

        stryear = yeartxt.getText().trim();
        month = monthcombo.getSelectedIndex() + 1;
        strmonth = monthcombo.getSelectedItem().toString();
        try {
            year = Integer.parseInt(stryear);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "year Format error", "Update error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (year > 0) {
            permission = checkprevious(year, month);
            //System.out.println(permission[0]+" "+permission[1]);
            if (permission[0] == 1 && permission[1] == 1) {
                JOptionPane.showMessageDialog(null, "Already Permission has giver for " + strmonth + ", " + year + " & bill"
                        + " has been generated", "Update error", JOptionPane.ERROR_MESSAGE);
            } else if (permission[0] == -1 && permission[1] == -1) {
                JOptionPane.showMessageDialog(null, "No permission has giver on " + strmonth + ", " + year, "Update error", JOptionPane.ERROR_MESSAGE);
            } else if (permission[0] == 1 && permission[1] == 0) {
                //System.out.println("called");
                int responce = JOptionPane.showConfirmDialog(this, "Do you want to Revoke bill generation permission of " + month + ", " + stryear, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (responce == JOptionPane.YES_OPTION) {
                    deletepermission(year, month);
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "Year is not valid ", "Update error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_revokebtnActionPerformed

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
            java.util.logging.Logger.getLogger(BillPermission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillPermission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillPermission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillPermission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BillPermission().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> monthcombo;
    private javax.swing.JButton pemissionbtn;
    private javax.swing.JButton revokebtn;
    private javax.swing.JTextField yeartxt;
    // End of variables declaration//GEN-END:variables
}
