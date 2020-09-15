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
public class NonStoredItemUpdate extends javax.swing.JFrame {
    
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
     * Creates new form NonStoredItemUpdate
     */
    public NonStoredItemUpdate() {
        initComponents();
        Tabledecoration();
        inittialization();
        getAllstoreditem();
        flag=1;
        
        Date date= fromdatechooser.getDate();
        String name = itemcombobox.getSelectedItem().toString().toLowerCase();
        setupdatetable(date,name);
        closeBtn();
    }
    
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    
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
                    conn.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    
    public void deleteselectedrow(){
       int dateserial =0;
       int totalrow = updatetable.getRowCount();
       String strdate="", name="",memo="",state="",check="";
       Double amount=0.00,price=0.00;
       boolean select=false;
       Date date =null;
       
       
       for(int i=0; i<totalrow ;i++){
           
           
           select = Boolean.valueOf(model.getValueAt(i,0).toString());
          
           if( select){
               
                strdate = model.getValueAt(i, 1).toString();
                name = model.getValueAt(i, 2).toString().toLowerCase();
                amount= Double.parseDouble(model.getValueAt(i, 3).toString());
                price = Double.parseDouble(model.getValueAt(i, 4).toString());
                memo = model.getValueAt(i, 5).toString();
                state = model.getValueAt(i, 6).toString().toLowerCase();
                
                try{
                date =formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
                }
                catch(NumberFormatException | ParseException e){
                    JOptionPane.showMessageDialog(null, "Date parsing "
                            + "failed in delete row", "Data parse error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
               
                try{
                    psmt = conn.prepareStatement("insert into nonstoreditemlog (serial,name,amount,price, memono,state,user) values (?,?,?,?,?,?,?)");
                    psmt.setInt(1, dateserial);
                    psmt.setString(2, name);
                    psmt.setDouble(3, amount);
                    psmt.setDouble(4, price);
                    psmt.setString(5, memo);
                    psmt.setString(6, state);
                    psmt.setString(7, UserLog.name);
                    psmt.execute();
                    psmt.close();
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to insert log"
                            , "Data insert error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                System.out.println(dateserial+" "+name+" "+amount+" "+price+" "+memo+" "+state);
                try{
                    psmt = conn.prepareStatement("delete from nonstoreditem where serial = ? and name = ? and state = ?");
                    psmt.setInt(1, dateserial);
                    psmt.setString(2, name);
                    psmt.setString(3, state);
                    psmt.execute();
                    psmt.close();
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to delete value"
                            , "Data delete error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
               
               
           }
           //System.out.print(select);
       }
       
       date = fromdatechooser.getDate();
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        //setupdatetable();
        if( date != null && flag ==1){
            name = itemcombobox.getSelectedItem().toString().toLowerCase();
            setupdatetable(date,name);
        }
       
       
       
    }
    
    
    
    
    //get name of all item from database 
    public void getAllstoreditem(){
       
        try{
            psmt = conn.prepareStatement("select name from nonstoreditemlist");
            rs = psmt.executeQuery();
            while(rs.next()){
                String outname = rs.getString(1).substring(0, 1).toUpperCase() + rs.getString(1).substring(1);
                itemcombobox.addItem(outname);  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
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
    
    
     public void setupdatetable(Date from, String item){
        //System.out.println(from+" "+ " "+item);
        int serial =0;
        String strdate = "";
        Date date=null;
        tablemodel = (DefaultTableModel) updatetable.getModel();
        
        
        try{
            serial = Integer.parseInt(formatter.format(from));
           
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setupdatetable","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        
   
        
        try{
            psmt = conn.prepareStatement("select * from nonstoreditem where name =? and serial = ?");
            psmt.setString(1, item);
            psmt.setInt(2, serial);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getInt(1));
                try{
                date = formatter.parse(rs.getString(1));
                strdate = formatter1.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
            
                //System.out.println(strdate);
                String outname = item.substring(0, 1).toUpperCase() + item.substring(1);
                String outstate = rs.getString(6).substring(0, 1).toUpperCase() + rs.getString(6).substring(1);
                Object o [] = {false,strdate,outname,rs.getDouble(3),rs.getDouble(4),rs.getString(5),outstate};
                tablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }

        
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
        //updatetable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
       
        
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
        jLabel3 = new javax.swing.JLabel();
        itemcombobox = new javax.swing.JComboBox<>();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        updatetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/nonstoredupdate.png"))); // NOI18N
        jLabel1.setText("NON STORED ITEM UPDATE");

        jLabel2.setFont(new java.awt.Font("Bodoni MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("Date ");

        fromdatechooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdatechooserPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/list.png"))); // NOI18N
        jLabel3.setText("Item ");

        itemcombobox.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        itemcombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemcomboboxActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash.png"))); // NOI18N
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
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatechooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(itemcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)))
                .addGap(34, 34, 34))
        );

        updatetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Date", "Name", "Amount", "Price", "Memo No", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        updatetable.setRowHeight(25);
        updatetable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        updatetable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        updatetable.setShowVerticalLines(false);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            name = itemcombobox.getSelectedItem().toString().toLowerCase();
            setupdatetable(date,name);
        }
        
    }//GEN-LAST:event_itemcomboboxActionPerformed

    private void fromdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdatechooserPropertyChange
        // TODO add your handling code here:
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
            name = itemcombobox.getSelectedItem().toString().toLowerCase();
            setupdatetable(date,name);
        }
        
    }//GEN-LAST:event_fromdatechooserPropertyChange

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        deleteselectedrow();
        
        
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
            java.util.logging.Logger.getLogger(NonStoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NonStoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NonStoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NonStoredItemUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NonStoredItemUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private com.toedter.calendar.JDateChooser fromdatechooser;
    private javax.swing.JComboBox<String> itemcombobox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable updatetable;
    // End of variables declaration//GEN-END:variables
}
