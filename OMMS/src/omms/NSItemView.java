
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Math.round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class NSItemView extends javax.swing.JFrame {
      
    
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
    
   

    public NSItemView() {
        initComponents();
        initialize();
        tabledecoration();
        itemcombo_set();
        dateNtableset();
        flag=1;
        initialtbl();
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
        this.setTitle("Non Stored Item View");
        
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
    
    
    ///Table decoration
    
    public void tabledecoration(){
        nsview_tbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 20));
        nsview_tbl.getTableHeader().setOpaque(false);
        nsview_tbl.getTableHeader().setBackground(new Color(32,136,203));
        nsview_tbl.getTableHeader().setForeground(new Color(255,255,255));
        nsview_tbl.setRowHeight(30);
        nsview_tbl.setFont(new Font("Segeo UI", Font.PLAIN, 18));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        nsview_tbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        nsview_tbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
       nsview_tbl.getColumnModel().getColumn(7).setCellRenderer(centerRender);
       
        
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
               nsitem_cmb.addItem(item);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    }

    /* intial data set*/
    public void initialtbl()
    {
            
        int len = -1;
        Date from=null, to =null;
        String item="";
        String stat="";
        from = frdt_ch.getDate();
         to = todt_ch.getDate(); 
 
       
        tm = (DefaultTableModel) nsview_tbl.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        if( from != null && to != null && flag==1){
            item = nsitem_cmb.getSelectedItem().toString().toLowerCase();
            stat= status_cmb.getSelectedItem().toString().toLowerCase();
            setnsItemtable(from, to, item,stat);
        }


    }
    
    ///Setting function of  for table to show non stored item
    public void setnsItemtable(Date from, Date to, String item ,String status){
        
        
        int fromserial = 0, toserial = 0;
      
        String strdate = "";
        ser=0;
        Date date=null;
        tm = (DefaultTableModel) nsview_tbl.getModel();
       
        try{
            fromserial = Integer.parseInt(formatter1.format(from));
            toserial = Integer.parseInt(formatter1.format(to));
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
   
        
        if(status.equals("all"))
        {
         
            try{
            psmt = conn.prepareStatement("select  * from nonstoreditem where name=? and serial>=? AND serial <=? order by serial,CASE  WHEN state='breakfast' THEN 1 WHEN state='lunch' THEN 2 WHEN state='dinner' THEN 3 END");
            psmt.setString(1, item);
            psmt.setInt(2, fromserial);
            psmt.setInt(3, toserial);
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
                String X= firstupperCaseMaker(item);
                String Y=firstupperCaseMaker(rs.getString(6));
                 
                
                    if(rs.getString(5).equals("###")){
                
                        ser++;
                        Object o [] = {ser,strdate,X,rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),Y,""};
                        tm.addRow(o);
                
                    }
                    else{
                           
                        ser++;
                        Object o [] = {ser,strdate,X,rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),Y,rs.getString(5)};
                        tm.addRow(o);
                    
                    }
                
                    
                
            }
            psmt.close();
            rs.close();
            
        
        }
            catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else{
        try{
            psmt = conn.prepareStatement("select  * from nonstoreditem where name=? and serial>=? AND serial <=? order by serial,CASE  WHEN state='breakfast' THEN 1 WHEN state='lunch' THEN 2 WHEN state='dinner' THEN 3 END");
            psmt.setString(1, item);
            psmt.setInt(2, fromserial);
            psmt.setInt(3, toserial);
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
                String X= firstupperCaseMaker(item);
                String Y=firstupperCaseMaker(rs.getString(6));
                 
                if(status.equals(rs.getString(6))){
                    if(rs.getString(5).equals("###")){
                
                        ser++;
                        Object o [] = {ser,strdate,X,rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),Y,""};
                        tm.addRow(o);
                
                    }
                    else{
                           
                        ser++;
                              Object o [] = {ser,strdate,X,rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),Y,rs.getString(5)};
                  tm.addRow(o);
                    
                    }
                }
                    
               
                
            }
            psmt.close();
            rs.close();
            
            
            
           
           // setlbl(item,fromavailable.toString(),prevavailable[0].toString(),Integer.toString(tablemodel.getRowCount()));
      }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
    }
    
   public void dateNtableset()
    {
        tm=(DefaultTableModel)nsview_tbl.getModel();
        tm.setRowCount(0);
        /***Date Setting**/
        Date date= new Date();
        frdt_ch.setDate(date);
        todt_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) frdt_ch.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) todt_ch.getDateEditor();
        editor.setEditable(false);
        
    }
     
    
    //get item unit
    
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
        NSitem_lbl = new javax.swing.JLabel();
        frdt_ch = new com.toedter.calendar.JDateChooser();
        todt_ch = new com.toedter.calendar.JDateChooser();
        nsitem_cmb = new javax.swing.JComboBox<>();
        status_cmb = new javax.swing.JComboBox<>();
        name_lbl = new javax.swing.JLabel();
        status_lbl = new javax.swing.JLabel();
        todt_lbl = new javax.swing.JLabel();
        from_lbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        nsview_tbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        NSitem_lbl.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        NSitem_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NSitem_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/nonstored.png"))); // NOI18N
        NSitem_lbl.setText("NON STORED ITEM VIEW");

        frdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        frdt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                frdt_chPropertyChange(evt);
            }
        });

        todt_ch.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        todt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                todt_chPropertyChange(evt);
            }
        });

        nsitem_cmb.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        nsitem_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nsitem_cmbActionPerformed(evt);
            }
        });
        nsitem_cmb.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                nsitem_cmbPropertyChange(evt);
            }
        });

        status_cmb.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        status_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner", "All" }));
        status_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_cmbActionPerformed(evt);
            }
        });

        name_lbl.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        name_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/NSitem.png"))); // NOI18N
        name_lbl.setText("NAME");

        status_lbl.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        status_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Statustime.png"))); // NOI18N
        status_lbl.setText("STATUS");

        todt_lbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        todt_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        todt_lbl.setText("TO");

        from_lbl.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        from_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        from_lbl.setText("FROM");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NSitem_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(500, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(name_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(from_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nsitem_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(todt_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(status_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(status_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(500, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NSitem_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(status_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nsitem_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(status_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(todt_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(from_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        nsview_tbl.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        nsview_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Date", "Name", "Quantity", "Price", "Avg Price", "Status", "Memo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        nsview_tbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        nsview_tbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
        nsview_tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(nsview_tbl);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void frdt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_frdt_chPropertyChange
    initialtbl();

    }//GEN-LAST:event_frdt_chPropertyChange

    private void todt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_todt_chPropertyChange
    initialtbl();
    }//GEN-LAST:event_todt_chPropertyChange

    private void nsitem_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nsitem_cmbActionPerformed
        initialtbl();
    }//GEN-LAST:event_nsitem_cmbActionPerformed

    private void status_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_cmbActionPerformed
        initialtbl();
    }//GEN-LAST:event_status_cmbActionPerformed

    private void nsitem_cmbPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_nsitem_cmbPropertyChange
       initialtbl(); 
    }//GEN-LAST:event_nsitem_cmbPropertyChange

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
            java.util.logging.Logger.getLogger(NSItemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NSItemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NSItemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NSItemView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NSItemView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel NSitem_lbl;
    private com.toedter.calendar.JDateChooser frdt_ch;
    private javax.swing.JLabel from_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel name_lbl;
    private javax.swing.JComboBox<String> nsitem_cmb;
    private javax.swing.JTable nsview_tbl;
    private javax.swing.JComboBox<String> status_cmb;
    private javax.swing.JLabel status_lbl;
    private com.toedter.calendar.JDateChooser todt_ch;
    private javax.swing.JLabel todt_lbl;
    // End of variables declaration//GEN-END:variables
}
