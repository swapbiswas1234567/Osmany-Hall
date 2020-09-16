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
public class TempFoodUpdate extends javax.swing.JFrame {
    
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
     * Creates new form TempFoodUpdate
     */
    public TempFoodUpdate() {
        initComponents();
        Tabledecoration();
        initialization();
        flag=1;
        
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
    
    
    
    public void deleteselectedrow(){
       int dateserial =0, hallid=0;
       int totalrow = updatetable.getRowCount();
       String strhallid="", strdate="";
       Double bill=0.00;
       boolean select=false;
       Date date =null;
       UserLog.name="account";
       
        //System.out.println(totalrow);
       
       for(int i=0; i<totalrow ;i++){
           
           
           select = Boolean.valueOf(model.getValueAt(i,3).toString());
           //.println(select+" "+i);
           
           if( select){
               
                strhallid = model.getValueAt(i, 0).toString();
                strdate = model.getValueAt(i, 1).toString();
                bill= Double.parseDouble(model.getValueAt(i, 2).toString());
               
                
                try{
                date =formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
                hallid = Integer.parseInt(strhallid);
                }
                catch(NumberFormatException | ParseException e){
                    JOptionPane.showMessageDialog(null, "Date parsing "
                            + "failed in delete row", "Data parse error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                //System.out.println(dateserial+" "+name+" "+amount+" "+price+" "+memo+" "+state);
               
                try{
                    psmt = conn.prepareStatement("insert into tempfoodlog (hallid,serial,bill,user) values (?,?,?,?)");
                    psmt.setInt(1, hallid);
                    psmt.setInt(2, dateserial);
                    psmt.setDouble(3, bill);
                    psmt.setString(4, UserLog.name);
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
                
                idtxt.postActionEvent();
               
           }
           
           
           //System.out.print(select);
       }
       
       
       
    }
    
    
    
    
    
    
    public void initialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate = new Date();
        fromdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) fromdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        
        dec = new DecimalFormat("#0.000");
        model = updatetable.getModel();
        
        idtxt.requestFocus();
    }
    
    
    
    
    
    public void setupdatetable(Date from, String id){
        //System.out.println(from+" "+ " "+item);
        int serial =0, hallid=0;
        String strdate = "",search="";
        Date date = null;
        tablemodel = (DefaultTableModel) updatetable.getModel();
        formatter2 = new SimpleDateFormat("yyyyMM");
        
        try{
            search = formatter2.format(from);
            hallid = Integer.parseInt(id);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date format in setupdatetable","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        search=search+"%";
        System.out.println(hallid);
        try{
            psmt = conn.prepareStatement("SELECT * from tempfood where dateserial like ? and hallid = ? ");
            psmt.setString(1, search);
            psmt.setInt(2, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString(1));
                try{
                date = formatter.parse(rs.getString(2));
                strdate = formatter1.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
                //System.out.println(strdate);
                Object o [] = {rs.getInt(1),strdate,rs.getDouble(3),false};
                tablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                        + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }

        
    }
    
    
    
    
    
    public void updatetabledate(Date from){
        //System.out.println(from+" ");
        int serial =0;
        String strdate = "",search="";
        Date date=null;
        tablemodel = (DefaultTableModel) updatetable.getModel();
        formatter2 = new SimpleDateFormat("yyyyMM");
        
        try{
            search = formatter2.format(from);
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date format in setupdatetable","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        search=search+"%";
        //System.out.println(search);
        try{
            psmt = conn.prepareStatement("SELECT * from tempfood where dateserial like ? ");
            psmt.setString(1, search);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString(1));
                try{
                date = formatter.parse(rs.getString(2));
                strdate = formatter1.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
                //.println(strdate);
                Object o [] = {rs.getInt(1),strdate,rs.getDouble(3),false};
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
        updatetable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        updatetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
      
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
        idtxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fromdatechooser = new com.toedter.calendar.JDateChooser();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        updatetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/tempfoodupdate.png"))); // NOI18N
        jLabel1.setText("TEMP FOOD UPDATE");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel2.setText("Hall Id");

        idtxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtxtActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel3.setText("Date ");

        fromdatechooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdatechooserPropertyChange(evt);
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

        updatetable.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hall Id", "Date", "Bill", "Select"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        updatetable.setAutoscrolls(false);
        updatetable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        updatetable.setRowHeight(26);
        updatetable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        updatetable.setShowHorizontalLines(false);
        updatetable.setShowVerticalLines(false);
        jScrollPane1.setViewportView(updatetable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fromdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fromdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdatechooserPropertyChange
        // TODO add your handling code here:
        
        Date date=null;
        String id="";
        date = fromdatechooser.getDate();
        id = idtxt.getText();
        System.out.println(date);
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        if( date != null && !id.equals("")){
            setupdatetable(date,id);
        }
        else if( date != null && id.equals("")){
            updatetabledate(date);
        }
        
        idtxt.requestFocus();
    }//GEN-LAST:event_fromdatechooserPropertyChange

    private void idtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtxtActionPerformed
        // TODO add your handling code here:
        Date date=null;
        String id="";
        date = fromdatechooser.getDate();
        id = idtxt.getText();
        
        tablemodel = (DefaultTableModel) updatetable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        if( date != null && !id.equals("")){
            setupdatetable(date,id);
        }
        else if( date != null && id.equals("")){
            updatetabledate(date);
        }
        
    }//GEN-LAST:event_idtxtActionPerformed

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
            java.util.logging.Logger.getLogger(TempFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TempFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TempFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TempFoodUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TempFoodUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private com.toedter.calendar.JDateChooser fromdatechooser;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable updatetable;
    // End of variables declaration//GEN-END:variables
}
