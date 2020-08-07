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
import java.text.DecimalFormat;
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
 * @author Ajmir
 */
public class StoredItemUpdate extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;
    
    
    /**
     * Creates new form StoredItemUpdate
     */
    public StoredItemUpdate() {
        initComponents();
        Tabledecoration();
        inittialization();
        getAllstoreditem();
        flag =1;
        String name="";
        Date date = fromdatechooser.getDate();
        name = itemcombobox.getSelectedItem().toString();
        setupdatetable(date,name);
        
        
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try{
                    Dashboard das = new Dashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
    }
    
    
    
    //get name of all item from database 
    public void getAllstoreditem(){
       
        try{
            psmt = conn.prepareStatement("select name from storeditem");
            rs = psmt.executeQuery();
            while(rs.next()){
                itemcombobox.addItem(rs.getString(1));  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
   
    }
    
    
    public void setupdatetable(Date from, String item){
        //System.out.println(from+" "+to+" "+item);
        int serial =0;
        String strdate = "", checkdate="";
        Date date=null;
        tablemodel = (DefaultTableModel) updatetable.getModel();
        
        
        try{
            serial = Integer.parseInt(formatter.format(from));
            //checkdate= formatter1.format(from);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setupdatetable","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        
   
        
        try{
            psmt = conn.prepareStatement("select * from storeinout where item =? and serial >= ? order by serial");
            psmt.setString(1, item);
            psmt.setInt(2, serial);
            rs = psmt.executeQuery();
            while(rs.next()){
                
                try{
                date = formatter.parse(rs.getString(1));
                strdate = formatter1.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
            
                //System.out.println(strdate);
                Object o [] = {strdate,item,rs.getDouble(3),rs.getDouble(4),rs.getString(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8)};
                tablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }

        
    }
    
    
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        fromdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) fromdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        
        dec = new DecimalFormat("#0.000");
        model = updatetable.getModel();
        
    }
    
    
    
     public void Tabledecoration(){
        updatetable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        updatetable.getTableHeader().setOpaque(false);
        updatetable.getTableHeader().setBackground(new Color(32,136,203));
        updatetable.getTableHeader().setForeground(new Color(255,255,255));
        updatetable.setRowHeight(25);
        updatetable.setFont(new Font("Segeo UI", Font.PLAIN, 14));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        updatetable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        
        
    }

    
     
     public void updatedatabase(Date date, String item){
         
         int serial =0;
         int delserial=0;
         String delname="", memo="";
         Double inamount=0.00,price=0.00,bf=0.00,lunch=0.00,dinner=0.00;
         try{
            serial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
         
         
         try{
            psmt = conn.prepareStatement("select * from storeinout where item =? and serial >= ? order by serial");
            psmt.setString(1, item);
            psmt.setInt(2, serial);
            rs = psmt.executeQuery();
            while(rs.next()){
                
                delserial = rs.getInt(1);
                delname = rs.getString(2);
                inamount = rs.getDouble(3);
                price= rs.getDouble(4);
                memo = rs.getString(5);
                bf= rs.getDouble(6);
                lunch= rs.getDouble(7);
                dinner = rs.getDouble(8);
                try{
                    //System.out.println("called"+delserial+" "+delname+" "+price+" "+memo+" "+bf+" "+lunch+" "+dinner);
                    psmt = conn.prepareStatement("insert into storeinoutlog (serial,item,inamount,price,memono,bf,lunch,dinner,user) values(?,?,?,?,?,?,?,?,?)");

                    psmt.setInt(1, delserial);
                    psmt.setString(2, delname);
                    psmt.setDouble(3, inamount);
                    psmt.setDouble(4, price);
                    psmt.setString(5, memo);
                    psmt.setDouble(6, bf);
                    psmt.setDouble(7, lunch);
                    psmt.setDouble(8, dinner);
                    psmt.setString(9, UserLog.name);
                    psmt.execute();
                   
                    psmt.close();

                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Failed to insert data log"
                               , "Data insertion error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
            }
            psmt.close();
            rs.close();
            
           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "log table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
         
         
         
         
         
         try{
            psmt = conn.prepareStatement("update storeinout set inamount=0,price = 0 , bf=0,lunch=0,dinner=0 where serial = ? and item=?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
         
         
         try{
            psmt = conn.prepareStatement("update storeinout set bf=0,lunch=0,dinner=0 where serial > ? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
         try{
            psmt = conn.prepareStatement("delete from storeinout where inamount =0 and price=0 and bf=0 and lunch=0 and dinner=0 and serial >= ? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
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
        fromdatechooser = new com.toedter.calendar.JDateChooser();
        itemcombobox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        updatetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/storeupdate.png"))); // NOI18N
        jLabel1.setText("STORED ITEM UPDATE");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("Date ");

        fromdatechooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdatechooserPropertyChange(evt);
            }
        });

        itemcombobox.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        itemcombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemcomboboxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/list.png"))); // NOI18N
        jLabel4.setText("Item ");

        deletebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash1.png"))); // NOI18N
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fromdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        updatetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Item", "In Quantity", "Price", "Memo No", "Breakfast", "Lunch", "Dinner"
            }
        ));
        jScrollPane1.setViewportView(updatetable);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fromdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdatechooserPropertyChange
        // TODO add your handling code here:
        Date date =null;
        String name="";
        date = fromdatechooser.getDate();
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        //setupdatetable();
        if( date != null && flag ==1){
            name = itemcombobox.getSelectedItem().toString();
            setupdatetable(date,name);
        }
    }//GEN-LAST:event_fromdatechooserPropertyChange

    private void itemcomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemcomboboxActionPerformed
        // TODO add your handling code here:
        
        Date date =null;
        String name="";
        date = fromdatechooser.getDate();
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        //setupdatetable();
        if( date != null && flag ==1){
            name = itemcombobox.getSelectedItem().toString();
            setupdatetable(date,name);
        }
        
        
    }//GEN-LAST:event_itemcomboboxActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        Date date = null;
        String name="";
        UserLog.name="accountant";
        
        date = fromdatechooser.getDate();
        name = itemcombobox.getSelectedItem().toString();
        
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want"
                + " To Delete as "+UserLog.name+"?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION){
            updatedatabase(date,name);
            tablemodel = (DefaultTableModel) updatetable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            
        }
        
    }//GEN-LAST:event_deletebtnActionPerformed

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
            java.util.logging.Logger.getLogger(StoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoredItemUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private com.toedter.calendar.JDateChooser fromdatechooser;
    private javax.swing.JComboBox<String> itemcombobox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable updatetable;
    // End of variables declaration//GEN-END:variables
}
