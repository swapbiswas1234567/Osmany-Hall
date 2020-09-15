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
public class StdInfoView extends javax.swing.JFrame {

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
    public StdInfoView() {
        initComponents();
        initialize();
    }
    
    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
        setCombo();
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
    
    public void setCombo(){
        deptCombo.setSelectedIndex(0);
        hallCombo.setSelectedIndex(0);
    }

    public void Tabledecoration() {
        infoTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        infoTable.getTableHeader().setOpaque(false);
        infoTable.getTableHeader().setBackground(new Color(32, 136, 203));
        infoTable.getTableHeader().setForeground(new Color(255, 255, 255));
        infoTable.setRowHeight(25);
        infoTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

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
    
    
    public void showInfo(){
        String id = findHallId();
        String dept = (String) deptCombo.getSelectedItem();
        String hall = (String) hallCombo.getSelectedItem();
        
        String query = "";
        
        
        
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
        jLabel1.setText("Students Information View");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel2.setText("Hall Id / Roll");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel3.setText("Dept. ");

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel4.setText("Hall Wise");

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N

        deptCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        deptCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "CE", "CSE", "EECE", "ME", "AE", "ARCHI", "NAME", "IPE", "BME", "PME", "EWCE", " " }));

        hallCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        hallCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Osmany Hall", "Ext-A", "Ext-B", "Ext-C", "Ext-E" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(113, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hallCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hallCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        infoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hall Id", "Name", "Roll", "Father's Name", "Mother's Name", "Dept", "Entry Date", "Contact No", "Email", "Blood Group", "Date Of Birth", "Present Address", "Permanent Address", "Room No", "Total Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(infoTable);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(StdInfoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdInfoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdInfoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdInfoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdInfoView().setVisible(true);
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
