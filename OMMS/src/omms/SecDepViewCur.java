/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class SecDepViewCur extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    int flag = 0;
    
    /**
     * Creates new form CurrentSecurityM
     */
    public SecDepViewCur() {
        initComponents();
        initialize();
        Tabledecoration();
        
        flag=1;
        moneytbl.getColumnModel().getColumn(8).setCellRenderer(new WordWrapCellRenderer());
        moneytbl.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
        
        moneytbl.getColumnModel().getColumn(4).setHeaderValue("<html>Admission<br>Date");
        moneytbl.getColumnModel().getColumn(9).setHeaderValue("<html>Deposit<br>Date");
        moneytbl.getColumnModel().getColumn(10).setHeaderValue("<html>Withdraw<br>Date");
        JTableHeader th = moneytbl.getTableHeader();
        th.setPreferredSize(new Dimension(37, 37));
        moneytbl.getTableHeader().repaint();
        
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
    
    public void initialize() {

        conn = Jconnection.ConnecrDb(); // set connection with database

        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial

        model = moneytbl.getModel();
        tablemodel = (DefaultTableModel) moneytbl.getModel();

        //TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tablemodel);
        //duetable.setRowSorter(sorter);
        int type = sortcombo.getSelectedIndex();
        
        idtxt.requestFocus();
    }
    
    
    public void Tabledecoration() {
        moneytbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        moneytbl.getTableHeader().setOpaque(false);
        moneytbl.getTableHeader().setBackground(new Color(32, 136, 203));
        moneytbl.getTableHeader().setForeground(new Color(255, 255, 255));
        moneytbl.setRowHeight(25);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        moneytbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        moneytbl.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        
    }
    
    
    public void settable(int type){
        String sql = "", strfrom = "", status="", strdeposit="", strwithdraw="";
        int serial = 1;

        Date fromdate = null, deposit=null, withdraw=null;

        switch (type) {
            case 0:
                sql = "select hallid,name,roll,entrydate,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from stuinfo ORDER by hallid ";
                
                break;
            case 1:
                sql = "select hallid,name,roll,entrydate,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from stuinfo where moneystatus=1";
                
                break;
            case 2:
                sql = "select hallid,name,roll,entrydate,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from stuinfo where moneystatus=0";
                
                break;
            
            default:
                break;
        }
        if (tablemodel.getRowCount() > 0) {
            tablemodel.setRowCount(0);
        }

        try {
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                try {
                    fromdate = formatter1.parse(rs.getString(4));
                    strfrom = formatter.format(fromdate);
                   
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to set date", "Date set error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (rs.getInt(10) == 0 && rs.getInt(8) == 0) {
                    status = "Not \ndeposited";

                } else if(rs.getInt(10) == 1){
                    status ="Withdrawed";
                    try {
                    
                    deposit = formatter1.parse(rs.getString(8));
                    strdeposit = formatter.format(deposit);
                    
                    withdraw = formatter1.parse(rs.getString(9));
                    strwithdraw = formatter.format(withdraw);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Failed to set wirhdraw date", "Date set error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                else if(rs.getInt(10) == 0 && rs.getInt(8) != 0){
                    status = "Not\nWithdrawed";
                    
                    try {
                    
                    deposit = formatter1.parse(rs.getString(8));
                    strdeposit = formatter.format(deposit);
                    
                   
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Failed to set not withdraw date", "Date set error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                }
                Object o[] = {serial, rs.getInt(1), rs.getString(2),
                    rs.getString(3), strfrom, rs.getDouble(5), rs.getDouble(6), rs.getDouble(7),status,strdeposit,strwithdraw};
                tablemodel.addRow(o);
                serial++;
            }
            psmt.close();
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);

        }
        
        if( tablemodel.getRowCount() <= 0){
            JOptionPane.showMessageDialog(null, "No Data exist", "table set failed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void searchtbl(String id){
        int totalrow=-1;
        boolean flag =false;
        totalrow = tablemodel.getRowCount();
        for( int i=0 ; i<totalrow; i++){
            if(moneytbl.getValueAt(i, 1).toString().equals(id) || moneytbl.getValueAt(i, 3).toString().equals(id)){
                moneytbl.requestFocus();
                moneytbl.changeSelection(i,0,false, false);
                flag = true;
                break;
            }
        }
        
        if( !flag){
            JOptionPane.showMessageDialog(null, "Id Does not found on table", "Data search error", JOptionPane.ERROR_MESSAGE);
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
        sortcombo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        idtxt = new javax.swing.JTextField();
        searchbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        moneytbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/clients.png"))); // NOI18N
        jLabel1.setText("Current Students Security Money View");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/sort.png"))); // NOI18N
        jLabel2.setText("Sort By");

        sortcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        sortcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Withdrawed", "Non-withdrawed" }));
        sortcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortcomboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel3.setText("Id");

        idtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtxtActionPerformed(evt);
            }
        });

        searchbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        searchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        searchbtn.setText("Search");
        searchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(111, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sortcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sortcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        moneytbl.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        moneytbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Hall Id", "Name", "Roll", "Admission Date", "Security", "Mess Ad", "Id Card", "Status", "deposit date", "withdraw date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        moneytbl.setRowHeight(26);
        moneytbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        moneytbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
        moneytbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(moneytbl);
        if (moneytbl.getColumnModel().getColumnCount() > 0) {
            moneytbl.getColumnModel().getColumn(0).setMaxWidth(30);
            moneytbl.getColumnModel().getColumn(2).setMinWidth(170);
            moneytbl.getColumnModel().getColumn(8).setMinWidth(50);
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
                .addComponent(jScrollPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sortcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortcomboActionPerformed
        // TODO add your handling code here:
        int index=0;
        index = sortcombo.getSelectedIndex();
        if(flag==1){
            settable(index);
        }
    }//GEN-LAST:event_sortcomboActionPerformed

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed
        // TODO add your handling code here:
        String strid="";
        strid  = idtxt.getText().trim();
        searchtbl(strid);
    }//GEN-LAST:event_searchbtnActionPerformed

    private void idtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtxtActionPerformed
        // TODO add your handling code here:
        searchbtn.doClick();
    }//GEN-LAST:event_idtxtActionPerformed

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
            java.util.logging.Logger.getLogger(SecDepViewCur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SecDepViewCur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SecDepViewCur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SecDepViewCur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SecDepViewCur().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable moneytbl;
    private javax.swing.JButton searchbtn;
    private javax.swing.JComboBox<String> sortcombo;
    // End of variables declaration//GEN-END:variables
}
