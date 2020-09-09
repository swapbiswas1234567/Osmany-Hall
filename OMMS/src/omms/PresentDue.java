/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Ajmir
 */
public class PresentDue extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    int flag=0;

    /**
     * Creates new form PresentDue
     */
    public PresentDue() {
        initComponents();
        Tabledecoration();
        initialize();
        flag=1;
        
    }
    
    
    public void initialize(){
       
        conn = Jconnection.ConnecrDb(); // set connection with database
       
        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        
        model = duetable.getModel();
        tablemodel = (DefaultTableModel) duetable.getModel();
        
        //TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tablemodel);
        //duetable.setRowSorter(sorter);
        
        int type= typecombo.getSelectedIndex();
        settable(type);
        idtxt.requestFocus();
    }
    
    
    
    public void Tabledecoration(){
        duetable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        duetable.getTableHeader().setOpaque(false);
        duetable.getTableHeader().setBackground(new Color(32,136,203));
        duetable.getTableHeader().setForeground(new Color(255,255,255));
        duetable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        duetable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
    }
    
    
    public void settable(int type){
        String sql="", strfrom="";
        int serial=1;
        Date fromdate=null;
        
        
        if(type == 0){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by totaldue ASC";
        }
        else if(type ==1){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by totaldue desc";
        }
        else if(type ==2){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by hallid";
        }
        else if(type == 3){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by roomno";
        }
        else if(type == 4){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by entrydate";
        }
        else if(type == 5){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by dept";
        }
        
        if(tablemodel.getRowCount() >0){
            tablemodel.setRowCount(0);
        }
        
        try{
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while(rs.next()){
                try{
                    fromdate = formatter1.parse(rs.getString(4));
                    strfrom = formatter.format(fromdate);
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Failed to set date", "Date set error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object o [] = {serial,rs.getInt(1),rs.getString(2),rs.getString(3),strfrom,rs.getString(5),rs.getString(6),rs.getDouble(7),rs.getString(8)};
                tablemodel.addRow(o);
                serial++;
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    public void search(String id){
        int totalrow =0, flag=0;
        totalrow = tablemodel.getRowCount();
        for(int i=0; i<totalrow;i++){
            if(model.getValueAt(i, 1).toString().equals(id) || model.getValueAt(i, 3).toString().equals(id)){
                duetable.requestFocus();
                duetable.changeSelection(i,0,false, false);
                flag=1;
                break;
            }
        }
        if( flag == 0){
            JOptionPane.showMessageDialog(null, "Id does not exist", "search", JOptionPane.WARNING_MESSAGE);
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
        typecombo = new javax.swing.JComboBox<>();
        idtxt = new javax.swing.JTextField();
        searchbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        duetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/due-date.png"))); // NOI18N
        jLabel1.setText("Total Due");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/sort.png"))); // NOI18N
        jLabel2.setText("Sort By");

        typecombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        typecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Due(asc)", "Due(desc)", "Hall id", "Room No", "Admission Date", "Dept" }));
        typecombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecomboActionPerformed(evt);
            }
        });

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(195, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typecombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchbtn)
                .addGap(0, 203, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        duetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl", "Hall Id", "Name", "Roll", "Admission", "Dept", "Room", "Due", "Mobile No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        duetable.setRowHeight(26);
        duetable.setSelectionBackground(new java.awt.Color(237, 57, 97));
        duetable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        duetable.setShowVerticalLines(false);
        duetable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(duetable);
        if (duetable.getColumnModel().getColumnCount() > 0) {
            duetable.getColumnModel().getColumn(0).setMaxWidth(35);
            duetable.getColumnModel().getColumn(2).setMinWidth(110);
            duetable.getColumnModel().getColumn(8).setMinWidth(110);
            duetable.getColumnModel().getColumn(8).setMaxWidth(170);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void typecomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typecomboActionPerformed
        // TODO add your handling code here:
        int type=-1;
        if(flag==1){
            type = typecombo.getSelectedIndex();
            
            settable(type);
        }
        idtxt.requestFocus();
    }//GEN-LAST:event_typecomboActionPerformed

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed
        // TODO add your handling code here:
        String id="";
        id= idtxt.getText().trim();
        if(!id.equals("")){
            search(id);
            idtxt.setText("");
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter an Id", "search", JOptionPane.WARNING_MESSAGE);
        }
        idtxt.requestFocus();
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
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PresentDue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable duetable;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchbtn;
    private javax.swing.JComboBox<String> typecombo;
    // End of variables declaration//GEN-END:variables
}
