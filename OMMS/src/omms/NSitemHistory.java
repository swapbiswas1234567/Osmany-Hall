
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class NSitemHistory extends javax.swing.JFrame {
   
     ///Variable declaration
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter2;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int selectedRow;
    DecimalFormat dec2;
   
    int flag;
    PreparedStatement psmt1 = null;
    ResultSet rs1 = null;
    int ser=0;
  


    public NSitemHistory() {
        initComponents();
        initialize();
        tabledecoration();
        dateNtableset();
        itemcombo_set();
        flag=1; 
        initialtbl();
       
    }

    
    
      /**Initializing Variable Function **/
    public void initialize()
    {
        conn= Jconnection.ConnecrDb();
        psmt=null;
        rs=null;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  
        formatter2 = new SimpleDateFormat("MMM dd,yyyy");
        dec2 = new DecimalFormat("#0.00");
        
        selectedRow = -1;
        this.setTitle("Non Stored ItemHistory");
        
    }
    
     ///Table decoration
    
    public void tabledecoration(){
        nsStore_log.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 20));
        nsStore_log.getTableHeader().setOpaque(false);
        nsStore_log.getTableHeader().setBackground(new Color(32,136,203));
        nsStore_log.getTableHeader().setForeground(new Color(255,255,255));
        nsStore_log.setRowHeight(30);
        nsStore_log.setFont(new Font("Segeo UI", Font.PLAIN, 18));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        nsStore_log.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        nsStore_log.getColumnModel().getColumn(7).setCellRenderer(centerRender);
               
        
    }

       public void dateNtableset()
    {
        tm=(DefaultTableModel)nsStore_log.getModel();
        tm.setRowCount(0);
        /***Date Setting**/
        Date date= new Date();
        fromdt_ch.setDate(date);
        todt_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) fromdt_ch.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) todt_ch.getDateEditor();
        editor.setEditable(false);
        
    }
   

  
    //Combo Name of item setting
    public void itemcombo_set()  
    {
        try{
           psmt=conn.prepareStatement("select name from nonstoreditemlist ");
           rs=psmt.executeQuery();
           
           while(rs.next())
           {
               String item = firstupperCaseMaker(rs.getString(1).toLowerCase());
               Item_cmb.addItem(item);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    }
    
    
    
    
    ///Setting function of  for table to show  stored item log
    public void setnsItemtable(Date from, Date to, String item){
        
        
        int fromserial = 0, toserial = 0;
      
        String strdate = "";
        ser=0;
        Date date=null;
        tm = (DefaultTableModel) nsStore_log.getModel();
       
        try{
            fromserial = Integer.parseInt(formatter1.format(from));
            toserial = Integer.parseInt(formatter1.format(to));
            
            if(fromserial >toserial)
            {
                JOptionPane.showMessageDialog(null, "From date Invalid","Date Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
   
        
        try{
            
            
            psmt = conn.prepareStatement("select  * from nonstoreditemlog where serial>= ? and serial <=? and name=?   order by serial ");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            psmt.setString(3, item.toLowerCase());
            rs = psmt.executeQuery();
            
            while(rs.next()){
                
                try{
                date = formatter1.parse(rs.getString(1));
                strdate = formatter2.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
                    if(rs.getString(5).equals("###") ){
                
                        ser++;
                        Object o [] = {ser,strdate,item,rs.getDouble(3),rs.getDouble(4),"",firstupperCaseMaker(rs.getString(6)),firstupperCaseMaker(rs.getString(7))};
                        tm.addRow(o);
                
                    }
                    else{
                           
                        ser++;
                        Object o [] = {ser,strdate,item,rs.getDouble(3),rs.getDouble(4),rs.getString(5),firstupperCaseMaker(rs.getString(6)),firstupperCaseMaker(rs.getString(7))};
                        
                  tm.addRow(o);
                    
                    }
                }

            
            psmt.close();
            rs.close();
    }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
    
    
    
    
    
    /* intial data set*/
    public void initialtbl()
    {
            
        Date from=null, to =null;
        String item="";
        String stat="";
        from = fromdt_ch.getDate();
         to = todt_ch.getDate(); 
 
       
        tm = (DefaultTableModel) nsStore_log.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        
        
        if( from != null && to != null && flag==1){
            item = firstupperCaseMaker(Item_cmb.getSelectedItem().toString().toLowerCase());
            setnsItemtable(from, to, item);
        }
    }
    
    
    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Item_cmb = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        fromdt_ch = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        todt_ch = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        nsStore_log = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/history.png"))); // NOI18N
        jLabel1.setText(" NON STORE ITEM HISTORY");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/NSitem.png"))); // NOI18N
        jLabel2.setText("NAME:");

        Item_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Item_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Item_cmbActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel3.setText("FROM :");

        fromdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fromdt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdt_chPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel4.setText("TO :");

        todt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        todt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                todt_chPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(144, 144, 144)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(todt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fromdt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        nsStore_log.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        nsStore_log.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Date", "Item", "Quantity", "Price", "Memo", "State", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        nsStore_log.setSelectionBackground(new java.awt.Color(232, 57, 97));
        nsStore_log.setSelectionForeground(new java.awt.Color(240, 240, 240));
        nsStore_log.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(nsStore_log);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Item_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Item_cmbActionPerformed
        initialtbl();
    }//GEN-LAST:event_Item_cmbActionPerformed

    private void fromdt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdt_chPropertyChange
       initialtbl();
    }//GEN-LAST:event_fromdt_chPropertyChange

    private void todt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_todt_chPropertyChange
        initialtbl();
    }//GEN-LAST:event_todt_chPropertyChange

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
            java.util.logging.Logger.getLogger(NSitemHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NSitemHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NSitemHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NSitemHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NSitemHistory().setVisible(true);  }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Item_cmb;
    private com.toedter.calendar.JDateChooser fromdt_ch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable nsStore_log;
    private com.toedter.calendar.JDateChooser todt_ch;
    // End of variables declaration//GEN-END:variables
}
