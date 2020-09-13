
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
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
    DecimalFormat dec2;
    int selectedRow;
    int selectedRow2;
    int flag=0;
    
    public StoreInForm() {
        initComponents();
        tableDecoration();
        initialize();
        dateNtableset();
        itemcombo_set();
        
        
    }
    
    
    
    public void tableDecoration()
    {
        Store_In_table.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 18));
        Store_In_table.getTableHeader().setOpaque(false);
        Store_In_table.getTableHeader().setBackground(new Color(32,136,203));
        Store_In_table.getTableHeader().setForeground(new Color(255,255,255));
        Store_In_table.setRowHeight(28);
        
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        Store_In_table.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        Store_In_table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        Store_In_table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        Store_In_table.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        Store_In_table.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        
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
        quantityIn_txt.requestFocusInWindow();
        dec = new DecimalFormat("#0.000");
        dec2 = new DecimalFormat("#0.00");
        
        
        
    }
   

  //Combo Input setting
    public void itemcombo_set()
    {
        try{
           psmt=conn.prepareStatement("select name from storeditem ");
           rs=psmt.executeQuery();
           String X="";
           while(rs.next())
           {
               String item = rs.getString(1);
               X=firstupperCaseMaker(item.toLowerCase());
               input_cmb.addItem(X);
               update_cmb.addItem(X);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
        
        flag=1;
        int comboindex =-1;
        comboindex = input_cmb.getSelectedIndex();
        String unit = setInsertunit(comboindex);
        insertUnit.setText(unit);
        
            int comboindex1 =-1;
            comboindex1 = update_cmb.getSelectedIndex();
            String unit1 = setupdatetunit(comboindex1);
            updateUnit.setText(unit1);

        
    }
    
    
    public void inputdatacheck(String name, String Quantity ,String price,Date date ,String memo )
     {
         try{
             String getDate =null;
             int dateserial =-1;
             try{
                 getDate = formatter2.format(date);
                 dateserial = Integer.parseInt(formatter1.format(date));
                 
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, " Date error","Input Error",JOptionPane.ERROR_MESSAGE);
             }
             
              //quantity check
               if(checkLetter(Quantity) != true)
                 {
                        JOptionPane.showMessageDialog(null, "Enter Valid Input","Quantity Error",JOptionPane.ERROR_MESSAGE);
                        return;
                 }
               else if(Quantity.equals("")  )
                    {
                        JOptionPane.showMessageDialog(null, "Quantity Field is empty .","Quantity Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                 
                 
                 double qty= Double.parseDouble(Quantity) ;
                    
                    if(qty ==0 )
                    {
                        JOptionPane.showMessageDialog(null, " Quantity can not be 0","Quantity Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    
                    

                    //Price checking
                    if(checkLetter(price) != true)
                 {
                        JOptionPane.showMessageDialog(null, "Enter Valid Input","Price Error",JOptionPane.ERROR_MESSAGE);
                        return;
                 }
              
                    
                    if(price.equals("") )
                    {
                        JOptionPane.showMessageDialog(null, "Price Field is empty .","Price Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                                       
                    double pr= Double.parseDouble(price) ;
                    
                    if(pr == 0 )
                    {
                        JOptionPane.showMessageDialog(null, "Price can not be 0 .","Price Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    int z =inputcheckDatabase(name,dateserial);
                    
                   if( inputCheckTable(name,getDate,-1)<0 && inputcheckDatabase(name,dateserial)>0)
                   {
                   
                                   
                    tablemodel = (DefaultTableModel) Store_In_table.getModel();
                    Object o [] = {name,dec.format(qty),dec2.format(pr),memo,getDate};
                    tablemodel.addRow(o);
                    Store_In_table.getSelectionModel().clearSelection();
                    selectedRow =-1;
                    
                    quantityIn_txt.setText("");
                    priceIn_txt.setText("");
                    memo_txt.setText("");
                   }
                   else if(inputCheckTable(name,getDate,-1)>= 0 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the table","Data Duplication",JOptionPane.ERROR_MESSAGE);
                   }
                   else if (inputcheckDatabase(name,dateserial)==-1 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is already inserted .","Data Duplication",JOptionPane.ERROR_MESSAGE );
                   }
                   
                   /*else if (inputcheckDatabase(name,dateserial)==2 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the Database for Update");
                   }*/
         }
         catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, "Data is not insertable","Input Error",JOptionPane.ERROR_MESSAGE);
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
        String memo="";
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
           memo=tm.getValueAt(i, 3).toString();
           databaseserial = 0;
           
           try{
               date = formatter2.parse(tabledate);
               serial = Integer.parseInt(formatter1.format(date));
           }
           catch(NumberFormatException | ParseException e){
               JOptionPane.showMessageDialog(null,"Failed to save"
                       + "t data while inserting","Data Error",JOptionPane.ERROR_MESSAGE);
           }
           actvalue=inputcheckDatabase(itemname ,serial);
        
      if( actvalue == 2)
      {
           try{
                    psmt = conn.prepareStatement("UPDATE storeinout SET inamount = ? , price = ?, memono = ?  WHERE serial = ? and item = ? ;");
                    psmt.setDouble(1, amount);
                    psmt.setDouble(2, price);
                    psmt.setString(3, memo);
                    psmt.setInt(4, serial);
                    psmt.setString(5, itemname);
                    psmt.execute();
                    psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating error"
                            + "in save&exit button", "Data Fetch Error", JOptionPane.ERROR_MESSAGE);
                }
          
      }
      else if (actvalue== 1)
      {
      try{
                //System.out.println("Execute insert");
                    String vac="";
               psmt = conn.prepareStatement("insert into storeinout (serial,item,inamount,price,memono,bf,lunch,dinner,bfgrp,lunchgrp,dinnergrp) values (?,?,?,?,?,0,0,0,?,?,?)");
                psmt.setInt(1,serial);
                psmt.setString(2, itemname);
                psmt.setDouble(3, amount);
                psmt.setDouble(4, price);
                psmt.setString(5, memo);
                psmt.setString(6, vac);
                psmt.setString(7, vac);
                psmt.setString(8, vac);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data Insert Error", "Data Fetch Error", JOptionPane.ERROR_MESSAGE);
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
                    if(!(ascii>=48 && ascii<=57) && !(ascii == 46))
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
            //System.out.println(tbIndex); 
            return tbIndex;
        }
        
        }
        
        
       return tbIndex; 
    }
    
    public int inputcheckDatabase(String name ,int date)
    {
        Double quantity=0.00;
        Double cost = 0.00 ;
        int date1=0;
        int val = 0;        
        
        try{
            psmt=conn.prepareStatement("select serial ,item ,inamount from storeinout where serial = ? and  item = ? ");
            psmt.setInt(1,date);
            psmt.setString(2,name);
            rs=psmt.executeQuery();
            while(rs.next())
            {
                quantity =rs.getDouble(3);
                date1=rs.getInt(1);
                              
            }    
        
            psmt.close();
            rs.close();
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Data Not Found","Data Fetch Error",JOptionPane.ERROR_MESSAGE );
        }
       
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
        
            return val;
        
    }

    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
    
    
    
    public void Updateset(String name, String Quantity ,String price,Date date,String memo)
    {
               try{
             String getDate =null;
             int dateserial =-1;
             
             try{
                 getDate = formatter2.format(date);
                 dateserial = Integer.parseInt(formatter1.format(date));
                 
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, "Date Error","Date Update Error",JOptionPane.ERROR_MESSAGE);
             }
             
              //quantity check
                 if(Quantity.equals("") == true)
                    {
                        JOptionPane.showMessageDialog(null, "Quantity Field is empty .","Quantity Update Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                 
                 if(checkLetter(Quantity) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Valid Input","Quntity Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                 double qty= Double.parseDouble(Quantity) ;
                    
                    if(qty ==0 )
                    {
                        JOptionPane.showMessageDialog(null, "Quanity Cannot be 0","Quantity Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    //Price checking
                    if(price.equals("") == true)
                    {
                        JOptionPane.showMessageDialog(null, "Price Field is empty .","Price Update Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    if(checkLetter(price) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Valid Price","Price Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    double pr= Double.parseDouble(price) ;
                    
                    if(pr == 0)
                    {
                        JOptionPane.showMessageDialog(null, "Price cannot be 0","Price Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    int z =inputcheckDatabase(name,dateserial);
                   if( inputCheckTable(name,getDate,selectedRow)<0 && inputcheckDatabase(name,dateserial)>0)
                   {
                   
                       
                       tm.setValueAt(name, selectedRow, 0);
                       tm.setValueAt(dec.format(qty), selectedRow,1);
                       tm.setValueAt(dec2.format(pr), selectedRow, 2);
                       tm.setValueAt(memo, selectedRow, 3);
                       tm.setValueAt(getDate, selectedRow,4);
                       
                       Store_In_table.getSelectionModel().clearSelection();
                       selectedRow =-1;
                       quantityUp_txt.setText("");
                       priceUp_txt.setText("");
                       memoUp_txt.setText("");
                   }
                   else if(inputCheckTable(name,getDate,-1)>= 0 )
                   {
                       JOptionPane.showMessageDialog(null, "Data is in the table","Data Update Error",JOptionPane.ERROR_MESSAGE);
                   }
                   else if (inputcheckDatabase(name,dateserial)==-1 )
                   {
                       JOptionPane.showMessageDialog(null,"Data is in the Database","Data Update Error",JOptionPane.ERROR_MESSAGE);
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
        String memo = tm.getValueAt(selectedRow,3).toString().trim();
        
        int len = update_cmb.getItemCount();

        for(int i=0; i<len; i++){
            if(itemName.equals(update_cmb.getItemAt(i))){
                update_cmb.setSelectedIndex(i);
                break;
            }
        }
        
        quantityUp_txt.setText(quantity);
        priceUp_txt.setText(price);
        memoUp_txt.setText(memo);
        SimpleDateFormat dt = new SimpleDateFormat("MMM d,yyyy"); 
        try{
            Date date = dt.parse(DATE); 
            dateUp_ch.setDate(date);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date is not converted .", "Date Error", JOptionPane.ERROR_MESSAGE); 
        }
        quantityUp_txt.requestFocusInWindow();
    }
    
    //function for delete a selected table row
    public void deleteTableRow(){
        
              if(selectedRow == -1){
            JOptionPane.showMessageDialog(null, "No row selected!", "Alert", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        tm.removeRow(selectedRow);
        
        selectedRow = -1;
        
        input_cmb.setSelectedIndex(0);
        update_cmb.setSelectedIndex(0);
        quantityUp_txt.setText("");
        priceUp_txt.setText("");
	memoUp_txt.setText("");
        Store_In_table.getSelectionModel().clearSelection();
    }
    
    
    ///Create item button
    
        
    public void createnewitem(){
        String Name="";
        JTextField name = new JTextField();
        JTextField unit = new JTextField();
        name.setPreferredSize(new Dimension(150, 30));
        unit.setPreferredSize(new Dimension(150, 30));
        Object[] message = {
            "Item Name:", name,
            "Unit:", unit
        };
      
        int option = JOptionPane.showConfirmDialog(null, message, "Create Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            
            addnewiteminlist(firstupperCaseMaker(name.getText().toLowerCase().trim()),unit.getText().toLowerCase().trim());  // save the item name and 
        } 
        else {
          return;    
        }
    }
   // adding new item in the database and also combobox 
    public void addnewiteminlist(String name, String unit){
         String Name1= name;         
        if(!name.equals("") && !unit.equals("")){
            String item = null;
            try{
            psmt = conn.prepareStatement("select name from storeditem where name = ?");
            psmt.setString(1, Name1);
            rs = psmt.executeQuery();
            while(rs.next()){
                item = rs.getString(1);
                 }
            psmt.close();
            rs.close();
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch "
                        + "data checking in addnewiteminlist", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            if(item != null){
                JOptionPane.showMessageDialog(null, "Item already exists", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                psmt = conn.prepareStatement("insert into storeditem (name,unit) values (?,?)");
                psmt.setString(1, name);
                psmt.setString(2, unit);
                psmt.execute();
                psmt.close();
                String outname = name.substring(0, 1).toUpperCase() + name.substring(1);
                String outunit = unit.substring(0, 1).toUpperCase() + unit.substring(1);
                
                
                input_cmb.addItem(outname);  // sending the name of item to set in the combobox
                update_cmb.addItem(outname);  // sending the name of item to set in the combobox
                
                input_cmb.setSelectedItem(outname);
                update_cmb.setSelectedItem(outname);
                
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Add new item in list error", "Data Insertion error", JOptionPane.ERROR_MESSAGE);
                }
            }
         
        
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter name and unit", "Data Insertion error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 // set the unit in the amount part 
    public String setInsertunit(int index){
        String unit ="";
        String name = comboIndextoitem(index);
        try{
            psmt = conn.prepareStatement("select unit from storeditem where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                unit = rs.getString(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to set the unit of for Item", "Data Fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        return unit;
    }
       
    //pass item name based on 
    public String comboIndextoitem(int index){
        return input_cmb.getItemAt(index);
    }
    
    
    //pass item name based on 
    public String comboIndxtoitem(int index){
        return update_cmb.getItemAt(index);
    }
    
     public String setupdatetunit(int index){
        String unit="";
        String name = update_cmb.getItemAt(index);
    
        try{
            psmt = conn.prepareStatement("select unit from storeditem where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            
            while(rs.next()){
                unit = rs.getString(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, ""
                    + "Failed to set the update unit of for Item", "Data Fetch Error", JOptionPane.ERROR_MESSAGE);
            
        }
       
        return unit;
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
        insertUnit = new javax.swing.JLabel();
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
        memoUp_txt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        updateUnit = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Store_In_table = new javax.swing.JTable();
        savenexit_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(208, 227, 229));

        StoreIn_lbl.setFont(new java.awt.Font("Bell MT", 1, 26)); // NOI18N
        StoreIn_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/cart.png"))); // NOI18N
        StoreIn_lbl.setText("STORED IN");
        StoreIn_lbl.setToolTipText("");

        dateIn_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        dateIn_lbl.setText("Date");

        itemIn_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        itemIn_lbl.setText("Item");

        QuantityIn_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        QuantityIn_lbl.setText("Quantity");

        priceIn_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        priceIn_lbl.setText("Price");

        memo_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        memo_lbl.setText("Memo");

        dateIn_ch.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        dateIn_ch.setMinimumSize(new java.awt.Dimension(30, 26));
        dateIn_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateIn_chPropertyChange(evt);
            }
        });

        input_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        input_cmb.setAutoscrolls(true);
        input_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_cmbActionPerformed(evt);
            }
        });

        quantityIn_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        quantityIn_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityIn_txtActionPerformed(evt);
            }
        });

        priceIn_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        priceIn_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceIn_txtActionPerformed(evt);
            }
        });

        memo_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        memo_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memo_txtActionPerformed(evt);
            }
        });

        NewItem_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        NewItem_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Insertbtn (2).png"))); // NOI18N
        NewItem_btn.setText("NEW");
        NewItem_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewItem_btnActionPerformed(evt);
            }
        });

        enter_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        enter_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Enter.png"))); // NOI18N
        enter_btn.setText("ENTER");
        enter_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enter_btnActionPerformed(evt);
            }
        });

        insertUnit.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        insertUnit.setText("Unit");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(QuantityIn_lbl)
                            .addComponent(priceIn_lbl)
                            .addComponent(memo_lbl)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(dateIn_lbl)
                                .addComponent(itemIn_lbl)))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(enter_btn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateIn_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(priceIn_txt)
                                    .addComponent(quantityIn_txt, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(input_cmb, 0, 140, Short.MAX_VALUE)
                                    .addComponent(memo_txt))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(NewItem_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(insertUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(StoreIn_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(153, 153, 153)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StoreIn_lbl)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateIn_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateIn_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemIn_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(NewItem_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(input_cmb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(quantityIn_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(QuantityIn_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priceIn_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceIn_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memo_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memo_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(enter_btn)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(117, 175, 182));

        Update_lbl.setFont(new java.awt.Font("Bell MT", 1, 26)); // NOI18N
        Update_lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        Update_lbl.setText("UPDATE");

        dateUp_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        dateUp_lbl.setText("Date");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel9.setText("Item");

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Quantity");

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel11.setText("Price");

        update_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        update_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        update_btn.setText("UPDATE");
        update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_btnActionPerformed(evt);
            }
        });

        delete_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        delete_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash_1.png"))); // NOI18N
        delete_btn.setText("DELETE");
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });
        delete_btn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                delete_btnKeyPressed(evt);
            }
        });

        dateUp_ch.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        update_cmb.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        update_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_cmbActionPerformed(evt);
            }
        });

        quantityUp_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        quantityUp_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityUp_txtActionPerformed(evt);
            }
        });

        priceUp_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        priceUp_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceUp_txtActionPerformed(evt);
            }
        });

        memoUp_txt.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        memoUp_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memoUp_txtActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Memo");

        updateUnit.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        updateUnit.setText("Unit");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(delete_btn))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Update_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dateUp_lbl)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(memoUp_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(priceUp_txt)
                                    .addComponent(update_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateUp_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(quantityUp_txt))
                                .addGap(10, 10, 10)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateUnit)))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Update_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateUp_ch, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(dateUp_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(updateUnit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(quantityUp_txt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priceUp_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memoUp_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(delete_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(update_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        Store_In_table.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Store_In_table.setSelectionBackground(new java.awt.Color(232, 57, 97));
        Store_In_table.setSelectionForeground(new java.awt.Color(240, 240, 240));
        Store_In_table.getTableHeader().setReorderingAllowed(false);
        Store_In_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Store_In_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Store_In_table);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savenexit_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savenexit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savenexit_btnActionPerformed
      if( tm.getRowCount() > 0){
            int responce = JOptionPane.showConfirmDialog(this,"Do you want to save the data ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION){
                try {
                    insertUpdate();
                    
                    JFrame frame = this;
                    //Dashboard das = new Dashboard();
                    //das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(StoreOutItem.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"No item is inserted on the table","Table item not found",JOptionPane.ERROR_MESSAGE);
        }
       
        
    }//GEN-LAST:event_savenexit_btnActionPerformed

    private void quantityIn_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityIn_txtActionPerformed
             priceIn_txt.requestFocusInWindow();
    }//GEN-LAST:event_quantityIn_txtActionPerformed

    private void input_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_cmbActionPerformed
            
          if ( flag ==1 ){
            
        int comboindex =-1;
        comboindex = input_cmb.getSelectedIndex();  
        String unit = setInsertunit(comboindex);
        insertUnit.setText(unit);
        }
        quantityIn_txt.requestFocusInWindow();
         
    }//GEN-LAST:event_input_cmbActionPerformed

    private void priceIn_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceIn_txtActionPerformed
        memo_txt.requestFocusInWindow();
    }//GEN-LAST:event_priceIn_txtActionPerformed

    private void enter_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enter_btnActionPerformed
        String name = input_cmb.getSelectedItem().toString();
        String quantity = quantityIn_txt.getText().trim();
        String price = priceIn_txt.getText().trim();
        Date date = dateIn_ch.getDate();
        String memo = memo_txt.getText().trim().toString();
        
        inputdatacheck(name,quantity,price,date,memo);
        
    }//GEN-LAST:event_enter_btnActionPerformed

    private void Store_In_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Store_In_tableMouseClicked
        mouseClickSet();
    }//GEN-LAST:event_Store_In_tableMouseClicked

    private void update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_btnActionPerformed
        String name = update_cmb.getSelectedItem().toString();
        String quantity = quantityUp_txt.getText().trim();
        String price = priceUp_txt.getText().trim();
        Date date = dateUp_ch.getDate();
        String memo = memoUp_txt.getText().trim().toString();
     
        Updateset(name , quantity, price,date,memo);
        
    }//GEN-LAST:event_update_btnActionPerformed

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        deleteTableRow();
    }//GEN-LAST:event_delete_btnActionPerformed

    private void NewItem_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewItem_btnActionPerformed
        createnewitem();
    }//GEN-LAST:event_NewItem_btnActionPerformed

    private void memo_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memo_txtActionPerformed
        enter_btn.doClick();
        quantityIn_txt.requestFocusInWindow();
    }//GEN-LAST:event_memo_txtActionPerformed

    private void update_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_cmbActionPerformed
         if ( flag ==1 ){
            int comboindex1 =-1;
            comboindex1 = update_cmb.getSelectedIndex();
            String unit1 = setupdatetunit(comboindex1);
            updateUnit.setText(unit1);
            
            quantityUp_txt.requestFocusInWindow();
         }
    }//GEN-LAST:event_update_cmbActionPerformed

    private void quantityUp_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityUp_txtActionPerformed
       priceUp_txt.requestFocusInWindow();
    }//GEN-LAST:event_quantityUp_txtActionPerformed

    private void memoUp_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoUp_txtActionPerformed
        update_btn.doClick();
        quantityIn_txt.requestFocusInWindow();
    }//GEN-LAST:event_memoUp_txtActionPerformed

    private void priceUp_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceUp_txtActionPerformed
        memoUp_txt.requestFocusInWindow();
    }//GEN-LAST:event_priceUp_txtActionPerformed

    private void delete_btnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_delete_btnKeyPressed
    
    }//GEN-LAST:event_delete_btnKeyPressed

    private void dateIn_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateIn_chPropertyChange
 
    }//GEN-LAST:event_dateIn_chPropertyChange

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
    private javax.swing.JLabel insertUnit;
    private javax.swing.JLabel itemIn_lbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField memoUp_txt;
    private javax.swing.JLabel memo_lbl;
    private javax.swing.JTextField memo_txt;
    private javax.swing.JLabel priceIn_lbl;
    private javax.swing.JTextField priceIn_txt;
    private javax.swing.JTextField priceUp_txt;
    private javax.swing.JTextField quantityIn_txt;
    private javax.swing.JTextField quantityUp_txt;
    private javax.swing.JButton savenexit_btn;
    private javax.swing.JLabel updateUnit;
    private javax.swing.JButton update_btn;
    private javax.swing.JComboBox<String> update_cmb;
    // End of variables declaration//GEN-END:variables
}




