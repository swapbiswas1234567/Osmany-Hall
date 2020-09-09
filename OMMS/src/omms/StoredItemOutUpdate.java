
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

public class StoredItemOutUpdate extends javax.swing.JFrame {

    
    
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
    int flag;
    PreparedStatement psmt1 = null;
    ResultSet rs1 = null;
    int rser =0;
    int supid=0;
 

    
    public StoredItemOutUpdate() {
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
        selectedRow = -1;
        this.setTitle("Stored Item Output Update");
        
        
    }
    
    
    
    
    ///Table decoration
    
    public void tabledecoration(){
        store_tbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 22));
        store_tbl.getTableHeader().setOpaque(false);
        store_tbl.getTableHeader().setBackground(new Color(32,136,203));
        store_tbl.getTableHeader().setForeground(new Color(255,255,255));
        store_tbl.setRowHeight(30);
        store_tbl.setFont(new Font("Segeo UI", Font.PLAIN, 22));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        store_tbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        
        
    }
    
    
    
    public void dateNtableset()
    {
        tm=(DefaultTableModel)store_tbl.getModel();
        tm.setRowCount(0);
        
       
        
        /***Date Setting**/
        Date date= new Date();
        date_ch.setDate(date);
        
        JTextFieldDateEditor editor = (JTextFieldDateEditor) date_ch.getDateEditor();
        editor.setEditable(false);
        
    }
   
    
    
    
    //Combo Name of item setting
    public void itemcombo_set()
    {
        try{
           psmt=conn.prepareStatement("select name from storeditem");
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

    
    
    /* intial data set*/
    public void initialtbl()
    {
            
        int len = -1;
        Date date=null;
        String item="";
        String stat="";
        date = date_ch.getDate();
   
        tm = (DefaultTableModel) store_tbl.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        if( date != null && flag==1){
            item = firstupperCaseMaker(Item_cmb.getSelectedItem().toString().toLowerCase());
            stat= firstupperCaseMaker(status_cmb.getSelectedItem().toString().toLowerCase());
           
            setSItemtable(date, item);
        }
        
    }

    
    
     
    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
   
    
     ///Setting function of  for table to show  stored item
    public void setSItemtable(Date from,  String item ){
        
        
        int dateserial = 0, toserial = 0;
        
        String strdate = "";
        rser=0;
        Date date=null;
        tm = (DefaultTableModel) store_tbl.getModel();
        
        System.out.println(item);
        System.out.println(from);
        
        
        
        try{
            dateserial = Integer.parseInt(formatter1.format(from));
          
        System.out.println(dateserial);
            
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
   
        try{
            
           
        if(status_cmb.getSelectedItem().toString().equals("Breakfast")){
            
        
            psmt = conn.prepareStatement("select  *from storeinout where item =? and serial = ?" );
            psmt.setString(1, item);
            psmt.setInt(2, dateserial);
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
                
              if(rs.getDouble(6)!=0 )  
              { 
                  String X= firstupperCaseMaker(item.toLowerCase());
             
                 rser++;
                 
     
                Object o [] = {strdate,item,rs.getDouble(6),"Breakfast"};
                tm.addRow(o);
                
                
              }
              else
              {
                  return;
              }
            }
            psmt.close();
            rs.close();
            
        } 
        
        else if(status_cmb.getSelectedItem().toString().equals("Lunch")){
            
        
            psmt = conn.prepareStatement("select  *from storeinout where item =? and serial = ?" );
            psmt.setString(1, item);
            psmt.setInt(2, dateserial);
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
               if(rs.getDouble(7)!=0  )  
              {
                String X= firstupperCaseMaker(item.toLowerCase());
                
                 rser++;
                 
                
                Object o [] = {strdate,item,rs.getDouble(7),"Lunch"};
                tm.addRow(o);
                
               
              }
               else{
                   return;
               }
            }
            psmt.close();
            rs.close();
            
        } 
        
        
        else if(status_cmb.getSelectedItem().toString().equals("Dinner")){
            
        
            psmt = conn.prepareStatement("select  *from storeinout where item =? and serial = ?" );
            psmt.setString(1, item);
            psmt.setInt(2, dateserial);
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
                
                if(rs.getDouble(8)!= 0 )  
              {
                String X= firstupperCaseMaker(item.toLowerCase());
                
                 rser++;
                 
                
                Object o [] = {strdate,item,rs.getDouble(8),"Dinner"};
                tm.addRow(o);
                
             
              }
                else{
                    return;
                }
            }
            psmt.close();
            rs.close();
            
        } 
        
        
        
           // setlbl(item,fromavailable.toString(),prevavailable[0].toString(),Integer.toString(tablemodel.getRowCount()));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
   
    
    //deletion/update query
    
      public void updatedatabase(Date date, String item, String status){
         
         int serial =0;
         int delserial=0;
         int deldate=0;
         Date dt=new Date();
         String delname="", memo="";
         String stats="";
         
         Double inamount=0.00,price=0.00,bf=0.00,lunch=0.00,dinner=0.00;
        supid= getid();
        supid++;
        int sta= statsid(status); 
        
         try{
            serial = Integer.parseInt(formatter1.format(date));
            deldate= Integer.parseInt(formatter1.format(dt));
         }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Format is inavlid ","Date Parsing Error", JOptionPane.ERROR_MESSAGE);
        }
         
         
         try{
            psmt = conn.prepareStatement("select serial,item,inamount,price,memono,bf,lunch,dinner from storeinout where item =? and serial = ? ");
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
                    

                    psmt = conn.prepareStatement(" insert into storeinoutlog (id,serial,item,inamount,price,memono,bf,lunch,dinner,user,status,deletedate) values(?,?,?,?,?,?,?,?,?,?,?,?) ");
                   
                    psmt.setInt(1, supid);
                    psmt.setInt(2, delserial);
                    psmt.setString(3, delname);
                    psmt.setDouble(4, inamount);
                    psmt.setDouble(5, price);
                    psmt.setString(6, memo);
                    psmt.setDouble(7, bf);
                    psmt.setDouble(8, lunch);
                    psmt.setDouble(9, dinner);
                    psmt.setString(10, UserLog.name);
                    psmt.setInt(11, sta);
                    psmt.setInt(12, deldate);
                    System.out.println("called"+delserial+" "+delname+" "+inamount + ""+price+" "+memo+" "+bf+" "+lunch+" "+dinner);
                   
                    psmt.execute();
                    psmt.close();

                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Failed to insert data  into log"
                               , "Data Insertion Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
            }
         
            psmt.close();
            rs.close();
            
           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "log table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
         
         
         if(status.equals("Breakfast"))
         {
                 
         try{
            psmt = conn.prepareStatement("select * from storeinout where item =? and serial = ? ");
            psmt.setString(1, item);
            psmt.setInt(2, serial);
            rs = psmt.executeQuery();
            while(rs.next()){
                delserial = rs.getInt(1);
                delname = rs.getString(2);
                inamount = rs.getDouble(3);
                price= rs.getDouble(4);
                memo = rs.getString(5);
                bf= 0.00;
                lunch= rs.getDouble(7);
                dinner = rs.getDouble(8);
              
                if(inamount==0 && lunch==0 && dinner==0 && bf ==0)
                {
                    try{
                    psmt = conn.prepareStatement("delete from storeinout where  serial = ? and item = ?");
                    psmt.setInt(1, serial);
                    psmt.setString(2, item);
                    psmt.execute();
                    psmt.close();  
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                    
                }
                else
                {
                    try{
            psmt = conn.prepareStatement("update storeinout set bf=0 where serial =? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                }
                
                
            }     
         psmt.close();
         rs.close();
         }
         
         catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "log table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
         
         }
         else if (status.equals("Lunch"))
                 {
                      try{
              psmt = conn.prepareStatement("select * from storeinout where item =? and serial = ? ");
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
                lunch= 0.0;
                dinner = rs.getDouble(8);
              
                if(inamount==0 && lunch==0 && dinner==0 && bf ==0)
                {
                    try{
                    psmt = conn.prepareStatement("delete from storeinout where  serial = ? and item = ?");
                    psmt.setInt(1, serial);
                    psmt.setString(2, item);
                    psmt.execute();
                    psmt.close();  
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                    
                }
               
                
                else
                {
                    try{
            psmt = conn.prepareStatement("update storeinout set lunch=0 where serial =? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                }
                
                
            }     
         psmt.close();
         rs.close();
         }
         
         catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "log table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
 
                 }
         else if (status.equals("Dinner"))
                 {
                      try{
            psmt = conn.prepareStatement("select * from storeinout where item =? and serial = ? ");
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
                dinner = 0.0;
              
                if(inamount==0 && lunch==0 && dinner==0 && bf ==0)
                {
                    try{
                    psmt = conn.prepareStatement("delete from storeinout where  serial = ? and item = ?");
                    psmt.setInt(1, serial);
                    psmt.setString(2, item);
                    psmt.execute();
                    psmt.close();  
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                    
                }
                
                
                else
                {
                    try{
            psmt = conn.prepareStatement("update storeinout set dinner=0 where serial =? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, item);
            psmt.execute();
            psmt.close();  
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update greater"
                    + " data for storeupdate", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
         
                }
                
                
            }     
         psmt.close();
         rs.close();
         }
         
         catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "log table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
                 }
 
     }
    
   
       
    //get Tble log_id
      
      public int getid()
      {
          int id=0;
          try{
              psmt= conn.prepareStatement("select id from storeinoutlog");
              rs=psmt.executeQuery();
              
              while(rs.next())
              {
                  id=rs.getInt(1);
                     
              }
              
              psmt.close();
              rs.close();
          return id;
          }
          catch(Exception e){
              JOptionPane.showMessageDialog(null, "Id didnot generate","ID Error",JOptionPane.ERROR_MESSAGE);
          }
          
          return id;
      }
        
      
      
      public int statsid(String status)
      {
          if(status.equals("Breakfast"))
              return 1;
          else if(status.equals("Lunch"))
              return 2;
          else if(status.equals("Dinner"))
              return 3;
          
          
          return 0;
       }
      //Delete action
        public void deleteTableRow(){
          selectedRow = store_tbl.getSelectedRow();
 
            if(selectedRow == -1){
            JOptionPane.showMessageDialog(null, "No row selected!", "Alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        tm.removeRow(selectedRow);
        selectedRow = -1;
        Item_cmb.setSelectedIndex(0);
        status_cmb.setSelectedIndex(0);
        store_tbl.getSelectionModel().clearSelection();
    }
  
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        store_tbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Item_cmb = new javax.swing.JComboBox<>();
        date_ch = new com.toedter.calendar.JDateChooser();
        delete_btn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        status_cmb = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        store_tbl.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        store_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Item", "Quantity", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        store_tbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        store_tbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
        store_tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(store_tbl);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel5.setText("STORED ITEM OUT UPDATE");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Kitchen.png"))); // NOI18N
        jLabel1.setText("ITEM");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel2.setText("DATE");

        Item_cmb.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        Item_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Item_cmbActionPerformed(evt);
            }
        });
        Item_cmb.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Item_cmbPropertyChange(evt);
            }
        });

        date_ch.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        date_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                date_chPropertyChange(evt);
            }
        });

        delete_btn.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        delete_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/delete.png"))); // NOI18N
        delete_btn.setText("DELETE");
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Statustime.png"))); // NOI18N
        jLabel3.setText("STATUS");

        status_cmb.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        status_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        status_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_cmbActionPerformed(evt);
            }
        });
        status_cmb.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                status_cmbPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(delete_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(status_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(date_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(status_cmb, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addComponent(delete_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        Date date = null;
        String name="";
        String stats="";
        UserLog.name="Manager";
        
        date = date_ch.getDate();
        name = Item_cmb.getSelectedItem().toString();
        stats=status_cmb.getSelectedItem().toString();
        
        int response = JOptionPane.showConfirmDialog(this,"Do You Want"
                + " To Delete as "+UserLog.name+"?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION){
           updatedatabase(date,name,stats);
           deleteTableRow();
            
        }
        
        
        
    }//GEN-LAST:event_delete_btnActionPerformed

    private void Item_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Item_cmbActionPerformed
    initialtbl();
 
    }//GEN-LAST:event_Item_cmbActionPerformed

    private void date_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_date_chPropertyChange
         initialtbl();

    }//GEN-LAST:event_date_chPropertyChange

    private void status_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_cmbActionPerformed
            initialtbl();

    }//GEN-LAST:event_status_cmbActionPerformed

    private void status_cmbPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_status_cmbPropertyChange
            initialtbl();

    }//GEN-LAST:event_status_cmbPropertyChange

    private void Item_cmbPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_Item_cmbPropertyChange
                          initialtbl();


    }//GEN-LAST:event_Item_cmbPropertyChange

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
            java.util.logging.Logger.getLogger(StoredItemOutUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoredItemOutUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoredItemOutUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoredItemOutUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoredItemOutUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Item_cmb;
    private com.toedter.calendar.JDateChooser date_ch;
    private javax.swing.JButton delete_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> status_cmb;
    private javax.swing.JTable store_tbl;
    // End of variables declaration//GEN-END:variables
}
