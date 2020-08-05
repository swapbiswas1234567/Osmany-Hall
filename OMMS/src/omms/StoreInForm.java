
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class StoreInForm extends javax.swing.JFrame {

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
    
    
    public StoreInForm() {
        initComponents();
        tableDecoration();
        initialize();
        dateNtableset();
        itemcombo_set();
    }
    
    
    
    public void tableDecoration()
    {
        Store_In_table.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 12));
        Store_In_table.getTableHeader().setOpaque(false);
        Store_In_table.getTableHeader().setBackground(new Color(32,136,203));
        Store_In_table.getTableHeader().setForeground(new Color(255,255,255));
        
    }
    
    
    
    //Set Date and Table
    /**Date and Table Set Function**/
    public void dateNtableset()
    {
        tm=(DefaultTableModel)Store_In_table.getModel();
        tm.setRowCount(0);
        
       
        
        /***Date Setting**/
        Date date= new Date();
        dateIn_ch.setDate(date);
        dateUp_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateIn_ch.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) dateUp_ch.getDateEditor();
        editor.setEditable(false);
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
        this.setTitle("STORE INPUT");
        
        
        
    }
   

  //Combo Input setting
    public void itemcombo_set()
    {
        try{
           psmt=conn.prepareStatement("select name from item");
           rs=psmt.executeQuery();
           
           while(rs.next())
           {
               String item = rs.getString(1);
               input_cmb.addItem(item);
               update_cmb.addItem(item);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    }
    
    
    public void inputdatacheck(String name, String Quantity ,String price,Date date  )
     {
         try{
             String getDate =null;
             int dateserial =-1;
             
             try{
                 getDate = formatter2.format(date);
                 dateserial = Integer.parseInt(formatter1.format(date));
                 System.out.println(getDate);
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, "Date error");
             }
             
              //quantity check
                 if(Quantity.equals(null) ||checkLetter(Quantity) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Quantity Input Error 1");
                        return; 
                    }
                 
                 double qty= Double.parseDouble(Quantity) ;
                    
                    if(qty ==0 )
                    {
                        JOptionPane.showMessageDialog(null, "Quanity Input Error 2");
                        return; 
                    }
                    
                    
                    

                    //Price checking
                    
                    if(price.equals(null) || checkLetter(price) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Price Input Error 1");
                        return; 
                    }
                    
                    double pr= Double.parseDouble(price) ;
                    
                    if(pr == 0)
                    {
                        JOptionPane.showMessageDialog(null, "Price Input Error 2");
                        return; 
                    }
                    
                    int z =inputcheckDatabase(name,dateserial);
                    System.out.println(z);
                   if( inputCheckTable(name,getDate,-1)<0 && inputcheckDatabase(name,dateserial)>0)
                   {
                   
                    tablemodel = (DefaultTableModel) Store_In_table.getModel();
                    Object o [] = {name,qty,pr,0,getDate};
                    tablemodel.addRow(o);
                    Store_In_table.getSelectionModel().clearSelection();
                    selectedRow =-1;
                    quantityIn_txt.setText("");
                    priceIn_txt.setText("");
                   }
                   else if(inputCheckTable(name,getDate,-1)== 1 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the table");
                   }
                   else if (inputcheckDatabase(name,dateserial)==-1 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the Database");
                   }
                   else if (inputcheckDatabase(name,dateserial)==2 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the Database for Update");
                   }
                   
                   
                    
             
             
         }
         catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, "Data is not insertable");
         }
           
     }
    
    
    // Insertion and update function
  public void insertUpdate()
  {
      
      int actvalue=0;
       int totalrow = tm.getRowCount();
        Double amount = 0.00;
        Double price=0.00;
        String itemname="";
        int serial=0;
        String tabledate = "";
        Date date;
        
        double bf=0.0;
        double lunch =0.0;
        Double dinner = 0.0;
        int databaseserial = 0;
        for( int i=0; i<totalrow; i++){
           itemname = tm.getValueAt(i, 0).toString();
           amount = Double.parseDouble(tm.getValueAt(i, 1).toString());
           price = Double.parseDouble(tm.getValueAt(i, 2).toString());
           tabledate = tm.getValueAt(i, 4).toString();
           
           databaseserial = 0;
           System.out.println("Save button:"+itemname);
           System.out.println("Save button:"+amount);
           System.out.println("Save button:"+ tabledate);
           
           
           try{
               date = formatter2.parse(tabledate);
               serial = Integer.parseInt(formatter1.format(date));
           }
           catch(NumberFormatException | ParseException e){
               JOptionPane.showMessageDialog(null,"failed to convert"
                       + "t data while inserting","Data Error",JOptionPane.ERROR_MESSAGE);
           }
           
        
      actvalue=inputcheckDatabase(itemname,serial);
      
      if( actvalue == 2)
      {
           try{
                   System.out.println("Execute update");
                    psmt = conn.prepareStatement("UPDATE storeinout SET inamount= ? , price = ?  WHERE serial = ? and item = ?");
                    psmt.setDouble(1, amount);
                    psmt.setDouble(2, price);
                    psmt.setInt(3, serial);
                    psmt.setString(4, itemname);
                    psmt.execute();
                    psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
          
      }
      else if (actvalue== 1)
      {
      try{
                System.out.println("Execute insert");
                    
               psmt = conn.prepareStatement("insert into storeinout (serial,item,inamount,price,memono,bf,lunch,dinner) values (?,?,?,?,'###',0,0,0)");
                psmt.setInt(1,serial);
                psmt.setString(2, itemname);
                psmt.setDouble(3, amount);
                psmt.setDouble(4, price);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Insert error", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }  
      }
  }
  }
 //Checking Anyother character else numbers
    public  boolean checkLetter(String S)
    {
        int stlen = S.length();
        char[] c=S.toCharArray();
        int ascii;
        int flag=0;
        
        for(int i =0 ; i<stlen; i++)
        {
            ascii= (int)c[i];
                    if(!(ascii>=48 && ascii<=57))
                       flag++;
         } 
       
        if(flag !=0)
        return false;
        else
            return true;
    }
    
// Fuction to check inserted data table
    public int inputCheckTable(String Item, String Date , int index )
    {
        int row = Store_In_table.getRowCount();
        int tbIndex = -1;
        
        
        for(int i=0; i<row ; i++)
        {
          if(tm.getValueAt(i,0).toString().trim().equals(Item) && tm.getValueAt(i,4).toString().trim().equals(Date) && i!= index ) 
        { 
             
       
            tbIndex++;
            System.out.println(tbIndex); 
            return tbIndex;
        }
        
        }
        
        
       return tbIndex; 
    }
    
    public int inputcheckDatabase(String name ,int date)
    {
        Double quantity=0.00;
        Double cost = 0.00 ;
        int date1=2022;
        System.out.println(date);
        System.out.println(name);
        int val = 0;        
        
        try{
            psmt=conn.prepareStatement("select serial ,item ,inamount from storeinout where serial = ? and  item = ? ");
            psmt.setInt(1,date);
            psmt.setString(2,name);
            rs=psmt.executeQuery();
            System.out.println("Executed");
            while(rs.next())
            {
                quantity =rs.getDouble(3);
                date1=rs.getInt(1);
                System.out.println(quantity);
                              
            }    
        
            psmt.close();
            rs.close();
        }
        
        catch(Exception e)
        {
            
        }
        System.out.println(quantity);
        System.out.println(date1);        
        
        
        if (date1 ==date)
        {
                     if(quantity >0)
                     {
                         val= -1;
                     }
                     else if(quantity ==0)
                     {
                         val= 2;
                     }
        }
        else if (date1 !=date)
            val= 1;
        
        
        System.out.println(val);        
        
            return val;
        
    }

    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
    
    
    
    public void Updateset(String name, String Quantity ,String price,Date date)
    {
               try{
             String getDate =null;
             int dateserial =-1;
             
             try{
                 getDate = formatter2.format(date);
                 dateserial = Integer.parseInt(formatter1.format(date));
                 System.out.println(getDate);
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, "Date error");
             }
             
              //quantity check
                 if(Quantity.equals(null) ||checkLetter(Quantity) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Quantity Input Error 1");
                        return; 
                    }
                 
                 double qty= Double.parseDouble(Quantity) ;
                    
                    if(qty ==0 )
                    {
                        JOptionPane.showMessageDialog(null, "Quanity Input Error 2");
                        return; 
                    }
                    
                    
                    

                    //Price checking
                    
                    if(price.equals(null) || checkLetter(price) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Price Input Error 1");
                        return; 
                    }
                    
                    double pr= Double.parseDouble(price) ;
                    
                    if(pr == 0)
                    {
                        JOptionPane.showMessageDialog(null, "Price Input Error 2");
                        return; 
                    }
                    
                    int z =inputcheckDatabase(name,dateserial);
                    System.out.println(z);
                   if( inputCheckTable(name,getDate,-1)<0 && inputcheckDatabase(name,dateserial)>0)
                   {
                   
                    tablemodel = (DefaultTableModel) Store_In_table.getModel();
                    Object o [] = {name,qty,pr,0,getDate};
                    tablemodel.addRow(o);
                    Store_In_table.getSelectionModel().clearSelection();
                    selectedRow =-1;
                    quantityIn_txt.setText("");
                    priceIn_txt.setText("");
                   }
                   else if(inputCheckTable(name,getDate,-1)== 1 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the table");
                   }
                   else if (inputcheckDatabase(name,dateserial)==-1 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the Database");
                   }
                   
                   
                   
                    
             
             
         }
         catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, "Data is not insertable");
         }
        
    }
    
   // Update Mouseclick table
    public void mouseClickSet()
    {
        selectedRow = Store_In_table.getSelectedRow();
        String itemName = tm.getValueAt(selectedRow, 0).toString().trim();
        String quantity = tm.getValueAt(selectedRow, 1).toString().trim();
        String price = tm.getValueAt(selectedRow, 2).toString().trim();
        String DATE = tm.getValueAt(selectedRow, 4).toString().trim();
        
        int len = update_cmb.getItemCount();

        for(int i=0; i<len; i++){
            if(itemName.equals(update_cmb.getItemAt(i))){
                update_cmb.setSelectedIndex(i);
                break;
            }
        }
        
        quantityUp_txt.setText(quantity);
        priceUp_txt.setText(price);
        
        SimpleDateFormat dt = new SimpleDateFormat("MMM d,yyyy"); 
        try{
            Date date = dt.parse(DATE); 
            dateUp_ch.setDate(date);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Problem!", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    //function for delete a selected table row
    public void deleteTableRow(){
        int selectedRow = Store_In_table.getSelectedRow();
        int serial = 0 ;
        String itemname;
        String stdate;
        String status;
        Date date;
        int index;
        System.out.println("before remove1");
        System.out.println(selectedRow);
        
        if(selectedRow >= 0){
            try{
            date = formatter2.parse(model.getValueAt(selectedRow,3).toString());
            serial = Integer.parseInt(formatter1.format(date));
        }
        catch(NumberFormatException | ParseException e){
            JOptionPane.showMessageDialog(null,"Date convertion failed while deleting row"
                    + "","Date Error",JOptionPane.ERROR_MESSAGE);
        }
        itemname = tm.getValueAt(selectedRow,0).toString();
        status = tm.getValueAt(selectedRow, 4).toString();
      
       
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete"
                + " The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION){
             System.out.println("before remove2");
            tablemodel = (DefaultTableModel) Store_In_table.getModel();
            tablemodel.removeRow(selectedRow);
        }
            
        
        }
        else{
            JOptionPane.showMessageDialog(null,"No Row is selected","Showing "
                    + "error while Updating data from textfield",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        StoreIn_lbl = new javax.swing.JLabel();
        dateIn_lbl = new javax.swing.JLabel();
        itemIn_lbl = new javax.swing.JLabel();
        QuantityIn_lbl = new javax.swing.JLabel();
        priceIn_lbl = new javax.swing.JLabel();
        memo_lbl = new javax.swing.JLabel();
        dateIn_ch = new com.toedter.calendar.JDateChooser();
        input_cmb = new javax.swing.JComboBox<>();
        quantityIn_txt = new javax.swing.JTextField();
        priceIn_txt = new javax.swing.JTextField();
        memo_txt = new javax.swing.JTextField();
        NewItem_btn = new javax.swing.JButton();
        enter_btn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        Update_lbl = new javax.swing.JLabel();
        dateUp_lbl = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        update_btn = new javax.swing.JButton();
        delete_btn = new javax.swing.JButton();
        dateUp_ch = new com.toedter.calendar.JDateChooser();
        update_cmb = new javax.swing.JComboBox<>();
        quantityUp_txt = new javax.swing.JTextField();
        priceUp_txt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Store_In_table = new javax.swing.JTable();
        savenexit_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(208, 227, 229));

        StoreIn_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        StoreIn_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Store_in.png"))); // NOI18N
        StoreIn_lbl.setText("STOREIN");
        StoreIn_lbl.setToolTipText("");

        dateIn_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        dateIn_lbl.setText("Date");

        itemIn_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        itemIn_lbl.setText("Item");

        QuantityIn_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        QuantityIn_lbl.setText("Quantity");

        priceIn_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        priceIn_lbl.setText("Price");

        memo_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        memo_lbl.setText("Memo");

        dateIn_ch.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        dateIn_ch.setMinimumSize(new java.awt.Dimension(30, 26));

        input_cmb.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        input_cmb.setAutoscrolls(true);
        input_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_cmbActionPerformed(evt);
            }
        });

        quantityIn_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        quantityIn_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityIn_txtActionPerformed(evt);
            }
        });

        priceIn_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        priceIn_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceIn_txtActionPerformed(evt);
            }
        });

        memo_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        NewItem_btn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        NewItem_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Insertbtn (2).png"))); // NOI18N
        NewItem_btn.setText("NEW");

        enter_btn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        enter_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Enter.png"))); // NOI18N
        enter_btn.setText("ENTER");
        enter_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enter_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(QuantityIn_lbl)
                            .addComponent(priceIn_lbl)
                            .addComponent(memo_lbl)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dateIn_lbl)
                                .addComponent(itemIn_lbl)))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(input_cmb, 0, 120, Short.MAX_VALUE)
                                    .addComponent(quantityIn_txt)
                                    .addComponent(priceIn_txt)
                                    .addComponent(memo_txt))
                                .addGap(53, 53, 53)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(enter_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(NewItem_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
                            .addComponent(dateIn_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(StoreIn_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StoreIn_lbl)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(dateIn_lbl))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(dateIn_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemIn_lbl)
                    .addComponent(NewItem_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(input_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(QuantityIn_lbl)
                    .addComponent(quantityIn_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priceIn_lbl)
                    .addComponent(priceIn_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memo_lbl)
                    .addComponent(memo_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(enter_btn)
                .addGap(21, 21, 21))
        );

        jPanel4.setBackground(new java.awt.Color(117, 175, 182));

        Update_lbl.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Update_lbl.setForeground(new java.awt.Color(255, 255, 255));
        Update_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        Update_lbl.setText("UPDATE");

        dateUp_lbl.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        dateUp_lbl.setForeground(new java.awt.Color(255, 255, 255));
        dateUp_lbl.setText("Date");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Item");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Quantity");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Price");

        update_btn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        update_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        update_btn.setText("UPDATE");
        update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_btnActionPerformed(evt);
            }
        });

        delete_btn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        delete_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash_1.png"))); // NOI18N
        delete_btn.setText("DELETE");
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });

        dateUp_ch.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        update_cmb.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        priceUp_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(delete_btn))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dateUp_lbl)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(quantityUp_txt)
                            .addComponent(dateUp_ch, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(update_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(priceUp_txt)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(Update_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Update_lbl)
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateUp_lbl)
                    .addComponent(dateUp_ch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(update_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(quantityUp_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(priceUp_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(delete_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(update_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Store_In_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item", "Quantity", "Price", "Memo", "Date"
            }
        ));
        Store_In_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Store_In_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Store_In_table);
        if (Store_In_table.getColumnModel().getColumnCount() > 0) {
            Store_In_table.getColumnModel().getColumn(0).setHeaderValue("Item");
            Store_In_table.getColumnModel().getColumn(1).setHeaderValue("Quantity");
            Store_In_table.getColumnModel().getColumn(2).setHeaderValue("Price");
            Store_In_table.getColumnModel().getColumn(3).setHeaderValue("Memo");
            Store_In_table.getColumnModel().getColumn(4).setHeaderValue("Date");
        }

        savenexit_btn.setBackground(new java.awt.Color(204, 204, 255));
        savenexit_btn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        savenexit_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        savenexit_btn.setText("SAVE & EXIT");
        savenexit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savenexit_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(savenexit_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savenexit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savenexit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savenexit_btnActionPerformed
        insertUpdate();
    }//GEN-LAST:event_savenexit_btnActionPerformed

    private void quantityIn_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityIn_txtActionPerformed
              
    }//GEN-LAST:event_quantityIn_txtActionPerformed

    private void input_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_cmbActionPerformed
        
    }//GEN-LAST:event_input_cmbActionPerformed

    private void priceIn_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceIn_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceIn_txtActionPerformed

    private void enter_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enter_btnActionPerformed
        String name =firstupperCaseMaker( input_cmb.getSelectedItem().toString());
        String quantity = quantityIn_txt.getText().trim();
        String price = priceIn_txt.getText().trim();
        Date date = dateIn_ch.getDate();
        
        inputdatacheck(name,quantity,price,date);
        
    }//GEN-LAST:event_enter_btnActionPerformed

    private void Store_In_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Store_In_tableMouseClicked
        mouseClickSet();
    }//GEN-LAST:event_Store_In_tableMouseClicked

    private void update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_btnActionPerformed
        String name =firstupperCaseMaker( update_cmb.getSelectedItem().toString());
        String quantity = quantityUp_txt.getText().trim();
        String price = priceUp_txt.getText().trim();
        Date date = dateUp_ch.getDate();
        
        Updateset(name , quantity, price,date);
        
    }//GEN-LAST:event_update_btnActionPerformed

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        deleteTableRow();
    }//GEN-LAST:event_delete_btnActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreInForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton NewItem_btn;
    private javax.swing.JLabel QuantityIn_lbl;
    private javax.swing.JLabel StoreIn_lbl;
    private javax.swing.JTable Store_In_table;
    private javax.swing.JLabel Update_lbl;
    private com.toedter.calendar.JDateChooser dateIn_ch;
    private javax.swing.JLabel dateIn_lbl;
    private com.toedter.calendar.JDateChooser dateUp_ch;
    private javax.swing.JLabel dateUp_lbl;
    private javax.swing.JButton delete_btn;
    private javax.swing.JButton enter_btn;
    private javax.swing.JComboBox<String> input_cmb;
    private javax.swing.JLabel itemIn_lbl;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel memo_lbl;
    private javax.swing.JTextField memo_txt;
    private javax.swing.JLabel priceIn_lbl;
    private javax.swing.JTextField priceIn_txt;
    private javax.swing.JTextField priceUp_txt;
    private javax.swing.JTextField quantityIn_txt;
    private javax.swing.JTextField quantityUp_txt;
    private javax.swing.JButton savenexit_btn;
    private javax.swing.JButton update_btn;
    private javax.swing.JComboBox<String> update_cmb;
    // End of variables declaration//GEN-END:variables
}




