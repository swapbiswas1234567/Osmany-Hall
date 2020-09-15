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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class TmpFoodUpdate extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter2;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;
    

    /**
     * Creates new form TmpFoodUpdate
     */
    public TmpFoodUpdate() {
        initComponents();
        Tabledecoration();
        inittialization();
        closeBtn();
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
    
    
    
    public void deleteselectedrow(){
       int dateserial =0, hallid=0;
       int totalrow = updatetable.getRowCount();
       String strhallid="", strdate="", remarks="";
       Double bill=0.00;
       boolean select=false;
       Date date =null;
       UserLog.name="account";
       
       //System.out.println(remarks);
       for(int i=0; i<totalrow ;i++){
           
           
           select = Boolean.valueOf(model.getValueAt(i,0).toString());
           //System.out.println(select+" "+i);
           //System.out.println(select);
           if( select){
               
                strhallid = model.getValueAt(i, 1).toString();
                strdate = model.getValueAt(i, 5).toString();
                bill= Double.parseDouble(model.getValueAt(i, 6).toString());
                remarks= model.getValueAt(i, 7).toString();
                
                try{
                date =formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
                hallid = Integer.parseInt(strhallid);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Date parsing "
                            + "failed in delete row", "Data parse error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                //System.out.println(dateserial+" "+name+" "+amount+" "+price+" "+memo+" "+state);
               
                try{
                    psmt = conn.prepareStatement("insert into tempfoodlog (hallid,serial,bill,user,remarks) values (?,?,?,?,?)");
                    psmt.setInt(1, hallid);
                    psmt.setInt(2, dateserial);
                    psmt.setDouble(3, bill);
                    psmt.setString(4, UserLog.name);
                    psmt.setString(5, remarks);
                    psmt.execute();
                    psmt.close();
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to insert log"
                            , "Data insert error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try{
                    psmt = conn.prepareStatement("delete from tempfood where dateserial = ? and hallid = ? ");
                    psmt.setInt(1, dateserial);
                    psmt.setInt(2, hallid);
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
       commonfortable();
       
       
    }
    
    
    
    
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String stryear = Integer.toString(year);
        yeartxt.setText(stryear);
        
        int month = Calendar.getInstance().get(Calendar.MONTH);
        monthcombo.setSelectedIndex(month);
        
        dec = new DecimalFormat("#0.000");
        model = updatetable.getModel();
        
        hallidtxt.requestFocus();
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
        
        updatetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
    }
    
    
    
    public void updatetablemonth(String serial){
        
        Date date=null;
        String strdate="";
        
        serial = serial+"%";
        
        try{
            psmt = conn.prepareStatement("SELECT stuinfo.hallid, stuinfo.roll, "
                    + "stuinfo.name, stuinfo.roomno, tempfood.dateserial, tempfood.bill, tempfood.remarks FROM stuinfo INNER JOIN tempfood "
                    + "ON tempfood.hallid=stuinfo.hallid where tempfood.dateserial like ?");
            
            psmt.setString(1, serial);
            rs = psmt.executeQuery();
            //System.out.println(serial);
            while(rs.next()){
                //System.out.println(rs.getString(1));
                try{
                    date = formatter.parse(rs.getString(5));
                    strdate = formatter1.format(date);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
                //System.out.println(strdate);
                Object o [] = {false,rs.getInt(1), rs.getInt(2), rs.getString(3),rs.getInt(4),
                    strdate,rs.getDouble(6),rs.getString(7)};
                tablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                        + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
        
        
        
    }
    
    
     public void updatetablemonthid(String serial,int hallid){
        
        Date date=null;
        String strdate="";
        
        serial = serial+"%";
        
        try{
            psmt = conn.prepareStatement("SELECT stuinfo.hallid, stuinfo.roll, "
                    + "stuinfo.name, stuinfo.roomno, tempfood.dateserial, tempfood.bill, tempfood.remarks FROM stuinfo INNER JOIN tempfood "
                    + "ON tempfood.hallid=stuinfo.hallid where tempfood.dateserial like ? and tempfood.hallid= ?");
            
            psmt.setString(1, serial);
            psmt.setInt(2, hallid);
            rs = psmt.executeQuery();
            //System.out.println(serial);
            while(rs.next()){
                //System.out.println(rs.getString(1));
                try{
                date = formatter.parse(rs.getString(5));
                strdate = formatter1.format(date);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
                
                Object o [] = {false,rs.getInt(1), rs.getInt(2), rs.getString(3),rs.getInt(4),
                    strdate,rs.getDouble(6),rs.getString(7)};
                tablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                        + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
        
  
    }
     
     
    
    public void commonfortable(){
        String month="", stryear="", strhallid ="";
        int serial=0, hallid=0;
        int selectedindx = monthcombo.getSelectedIndex();
        month= "0" + Integer.toString(selectedindx+1);
        stryear = yeartxt.getText().trim();
        strhallid = hallidtxt.getText().trim();
        
        stryear= stryear+month;
        try{
            serial = Integer.parseInt(stryear);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "year format error","Date insert error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        
        if( serial > 200000 && !stryear.equals("") && strhallid.equals("")){
            //System.out.println(stryear);
            updatetablemonth(stryear);
            
        }
        else if( serial > 200000 && !stryear.equals("") && !strhallid.equals("")){
            //System.out.println(serial);
            
            try{
            hallid = Integer.parseInt(strhallid);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "hallid format error","Date insert error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            updatetablemonthid(stryear, hallid);
        }
        else{
            JOptionPane.showMessageDialog(null, "invalid year","Date insert error", JOptionPane.ERROR_MESSAGE);
        }
        
        //System.out.println("called");
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
        hallidtxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        monthcombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        yeartxt = new javax.swing.JTextField();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        updatetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/tempfoodupdate.png"))); // NOI18N
        jLabel1.setText("TEMP FOOD UPDATE");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel2.setText("Hall Id ");

        hallidtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        hallidtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hallidtxtActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthly.png"))); // NOI18N
        jLabel3.setText("Month");

        monthcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        monthcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        monthcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthcomboActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/year.png"))); // NOI18N
        jLabel4.setText("Year");

        yeartxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        yeartxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yeartxtActionPerformed(evt);
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
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hallidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yeartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(hallidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(monthcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yeartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        updatetable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        updatetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Hall Id", "Roll", "Name", "Room No", "Date", "Bill", "Remarks"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        updatetable.setRowHeight(26);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void monthcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthcomboActionPerformed
        // TODO add your handling code here:
        commonfortable();
    }//GEN-LAST:event_monthcomboActionPerformed

    private void hallidtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hallidtxtActionPerformed
        // TODO add your handling code here:
        commonfortable();
    }//GEN-LAST:event_hallidtxtActionPerformed

    private void yeartxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yeartxtActionPerformed
        // TODO add your handling code here:
        commonfortable();
    }//GEN-LAST:event_yeartxtActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete"
                + " The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION){
                deleteselectedrow();   
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
            java.util.logging.Logger.getLogger(TmpFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TmpFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TmpFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TmpFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TmpFoodUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField hallidtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> monthcombo;
    private javax.swing.JTable updatetable;
    private javax.swing.JTextField yeartxt;
    // End of variables declaration//GEN-END:variables
}
