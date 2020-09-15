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

public class StoreOutHistory extends javax.swing.JFrame {

    
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
    
   
    public StoreOutHistory() {
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
        this.setTitle("Stored Out History");
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
   
    
     ///Table decoration
    
    public void tabledecoration(){
        store_log.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 20));
        store_log.getTableHeader().setOpaque(false);
        store_log.getTableHeader().setBackground(new Color(32,136,203));
        store_log.getTableHeader().setForeground(new Color(255,255,255));
        store_log.setRowHeight(30);
        store_log.setFont(new Font("Segeo UI", Font.PLAIN, 18));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        store_log.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        store_log.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        store_log.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        store_log.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        store_log.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        store_log.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        
    }

    

     public void dateNtableset()
    {
        tm=(DefaultTableModel)store_log.getModel();
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
           psmt=conn.prepareStatement("select name from storeditem ");
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
    public void sethsItemtable(Date from, Date to, String item){
        
        
        int fromserial = 0, toserial = 0;
      
        String strdate = "";
        String strdate2 = "";
        ser=0;
        Date date=null;
        Date dt=null;
        tm = (DefaultTableModel) store_log.getModel();
       
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
            
            
            psmt = conn.prepareStatement("select  * from storeinoutlog where serial>= ? and serial <=? and item=?  order by serial ");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            psmt.setString(3, item);
            
            rs = psmt.executeQuery();
            
            while(rs.next()){
                
                try{
                date = formatter1.parse(rs.getString(2));
                strdate = formatter2.format(date);
                dt = formatter1.parse(rs.getString(12));
                strdate2 = formatter2.format(dt);
                
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
                String X= firstupperCaseMaker(item);
                
                    if(rs.getInt(11)==1 ){
                
                        ser++;
                        Object o [] = {ser,strdate,rs.getDouble(7),statsid(rs.getInt(11)),rs.getString(10),strdate2};
                        tm.addRow(o);
                
                    }
                    else if(rs.getInt(11)==2 ){
                
                        ser++;
                        Object o [] = {ser,strdate,rs.getDouble(8),statsid(rs.getInt(11)),rs.getString(10),strdate2};
                        tm.addRow(o);
                
                    }
                    
                    else if(rs.getInt(11)==3 ){
                
                        ser++;
                        Object o [] = {ser,strdate,rs.getDouble(9),statsid(rs.getInt(11)),rs.getString(10),strdate2};
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
 
       
        tm = (DefaultTableModel) store_log.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        
        
        if( from != null && to != null && flag==1){
            item = firstupperCaseMaker(Item_cmb.getSelectedItem().toString().toLowerCase());
            sethsItemtable(from, to, item);
        }
    }
    

public String statsid(int status)
      {
          if(status==1)
              return "Breakfast";
          else if(status==2)
              return "Lunch";
          else if(status==3)
              return "Dinner";
          
          
          return "";
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
        store_log = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/history.png"))); // NOI18N
        jLabel1.setText("STORE OUT HISTORY");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/NSitem.png"))); // NOI18N
        jLabel2.setText("NAME");

        Item_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Item_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Item_cmbActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel3.setText("FROM");

        fromdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fromdt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdt_chPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel4.setText("TO");

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
                .addContainerGap(71, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(140, 140, 140)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fromdt_ch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(todt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        store_log.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        store_log.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Date", "Amount", "Status", "User", "Delete Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        store_log.setSelectionBackground(new java.awt.Color(232, 57, 97));
        store_log.setSelectionForeground(new java.awt.Color(240, 240, 240));
        store_log.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(store_log);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(StoreOutHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOutHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOutHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOutHistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOutHistory().setVisible(true);
            }
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
    private javax.swing.JTable store_log;
    private com.toedter.calendar.JDateChooser todt_ch;
    // End of variables declaration//GEN-END:variables
}
