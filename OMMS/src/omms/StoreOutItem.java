/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class StoreOutItem extends javax.swing.JFrame {
    
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    ArrayList<StoredItem>storeinfo[] = null;    
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    /**
     * Creates new form StoreOutItem
     */
    public StoreOutItem() {
        initComponents();
        Tabledecoration();
        initialize();
        getAllstoreditem();
        setIteminlist();
    }
    
    
    public void initialize(){
        int combolen = -1;
        conn = Jconnection.ConnecrDb(); // set connection with database
       
        combolen = itemComboboxlen();
        storeinfo = new ArrayList[3];
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");
        
        Date todaysdate =new Date();
        insertdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        updatedatechooser.setDate(todaysdate);
        
        model = Storeouttable.getModel();
    }
    
    
    
    public void setIteminlist(){
        
        
        int combolen=-1;
        String comboItemname = null;
        
        int dateserial = 0;
        String itemname = null;
        Double inamount = -1.0;
        Double price = -1.0;
        String memono = null;
        Double bf = -1.0;
        Double lunch = -1.0;
        Double dinner = -1.0;
        Double available = 0.00;
        
        
        try{
            combolen = itemComboboxlen();
            for(int i=0; i<combolen; i++){
                comboItemname = comboIndextoitem(i);
                psmt = conn.prepareStatement("select * from storeinout where item = ?");
                psmt.setString(1, comboItemname);
                rs = psmt.executeQuery();
                storeinfo[i] = new ArrayList<StoredItem>();
                while(rs.next()){
                    dateserial = rs.getInt(1);
                    itemname = rs.getString(2);
                    inamount = rs.getDouble(3);
                    price = rs.getDouble(4);
                    bf = rs.getDouble(6);
                    lunch = rs.getDouble(7);
                    dinner = rs.getDouble(8);
                    
                    //available = available + inamount -(bf+lunch+dinner);
                    //System.out.println(dateserial+" "+ itemname+" "+ inamount+" "+ price+" "+ bf+" "+ lunch+" "+ dinner);
                    st = new StoredItem(dateserial,itemname,inamount,price,bf,lunch,dinner,0);
                    storeinfo[i].add(st);
                    
                }
            }
            // System.out.println(storeinfo[0].size());
            //printlist();
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //get name of all item from database 
    public void getAllstoreditem(){
        try{
            psmt = conn.prepareStatement("select name from item");
            rs = psmt.executeQuery();
            while(rs.next()){
                setInsertitemcombobox(rs.getString(1));  // sending the name of item to set in the combobox
                setUpdateitemcombobox(rs.getString(1));  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void setOuttable(String name, String status,String amount,String remaining, Date date){
        try{
            String getdate = null;
            int dateserial=-1;
            Double available = 0.00;
            Double nextavailable = 0.00;
            try{
                getdate = formatter.format(date);
                
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null,"Date Convertion Error","Date Error",JOptionPane.ERROR_MESSAGE);
            }
            
            dateserial = Integer.parseInt(formatter1.format(date));
            available = countAvailableitem(dateserial,name);
            nextavailable = nextavailableitem(dateserial, name, available);
            System.out.println(available +"  "+nextavailable);
            if( name != null && status != null && getdate != null && Double.parseDouble(amount) > 0 
                    && CheckTableItem(name,status,getdate) < 0 && available >=0 && nextavailable >= 0){
  
                tablemodel = (DefaultTableModel) Storeouttable.getModel();
                Object o [] = {name, amount , remaining,getdate, status};
                tablemodel.addRow(o);
                clearTextfieldafterinsert(); // after each insertion it will clear text field 
                
                
                status = status.toLowerCase();
                
                insertablevaluetolist(dateserial, name,Double.parseDouble(amount),status);  // insert the value in jtable
                sortList(comboItemtoindex(name));       //sort the array list after each insertion
                //printlist(comboItemtoindex(name));
                                            
            }
            else{
                JOptionPane.showMessageDialog(null,"you are trying to insert zero amount or amount more than remaining amount"
                        + " or Null date or item exist in table","Inserting Data Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Data reading for Jtable error","Data Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    public void insertablevaluetolist(int serial,String name, Double amount, String status){
        int index=-1;
        index =comboItemtoindex(name);
        switch (status) {
            case "breakfast":
                st = new StoredItem(serial,name,0.00,0.00,amount,0.00,0.00,1);
                storeinfo[index].add(st);
                break;
            case "lunch":
                st = new StoredItem(serial,name,0.00,0.00,0.00,amount,0.00,1);
                storeinfo[index].add(st);
                break;
            case "dinner":
                st = new StoredItem(serial,name,0.00,0.00,0.00,0.00,amount,1);
                storeinfo[index].add(st);
                break;
            default:
                break;
        }
    }
    
    
    
    // function to get the index number of a item based on its time it consumed or the status and input date
    public int CheckTableItem(String item, String status, String date){
        int row = Storeouttable.getRowCount();
        int tableindx = -1;
        //System.out.println(item+" "+status+" "+date);
        for(int i=0; i<row ; i++){
            if (model.getValueAt(i,0).toString().trim().equals(item) && model.getValueAt(i,4).toString().trim().equals(status) && model.getValueAt(i,3).toString().trim().equals(date)){
                tableindx++;
                return tableindx;
            }
        }
        return tableindx;
    }
    
    // after each insertion in arraylist it will sort it by date order 
    public void sortList(int index){
        Collections.sort(storeinfo[index], new Comparator<StoredItem>()
        {
            @Override
            public int compare(StoredItem s1, StoredItem s2){
                return Integer.valueOf(s1.dateserial).compareTo(s2.dateserial);
            }
        });
    }
    
    
    public void updatefield(){
        int itemcomboindex =-1;
        int statuscomboindex =-1;
        int selectedrow= -1;
        selectedrow = Storeouttable.getSelectedRow();
        
        
        String tablecomboitem = model.getValueAt(selectedrow,0).toString().trim();
        for (int i=0 ; i<updateCombobox.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateItemCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (updateCombobox.getItemAt(i).toString().equals(tablecomboitem)){
                itemcomboindex = i;
                break;
            }
        }
        
        String amount = model.getValueAt(selectedrow,1).toString().trim();
        String remainingamount = model.getValueAt(selectedrow,2).toString().trim();
        String TableDate = model.getValueAt(selectedrow,3).toString().trim();
        Date showdate=null;
        try{
            showdate = new SimpleDateFormat("dd-MM-yyyy").parse(TableDate);
            //System.out.println(showdate);
        }
        catch(ParseException e){
            Logger.getLogger(StoreOut.class.getName()).log(Level.SEVERE, null, e);
        }
        
        String tablecombostatus = model.getValueAt(selectedrow,4).toString().trim();
        for (int i=0 ; i<updatestatuscombo.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateStatusCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (updatestatuscombo.getItemAt(i).toString().equals(tablecombostatus)){
                statuscomboindex = i;
                break;
            }
        }
        
        if(itemcomboindex >=0 && statuscomboindex >=0){
            updateavailable.setText(remainingamount);
            updateamounttxt.setText(amount);
            updateCombobox.setSelectedIndex(itemcomboindex);
            updatestatuscombo.setSelectedIndex(statuscomboindex);
            updatedatechooser.setDate(showdate);
        }
        else{
            JOptionPane.showMessageDialog(null,"item and status combo box did not mathed with row","Data Showing error",JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(itemcomboindex);
        
    }
    
    
    
    
    public String comboIndextoitem(int index){
        return insertCombobox.getItemAt(index);
    }
    
    
    // get the index of a item based on name
    public int comboItemtoindex(String item){
        for(int i=0; i<itemComboboxlen() ; i++){
            if(insertCombobox.getItemAt(i).equals(item)){
                return i;
            }
        }
        return -1;
    }
    
    
     //function for setting the item list of combobox in insert portion
    public void setInsertitemcombobox(String names){
        insertCombobox.addItem(names);
        
    }
    //function for setting the item list of combobox in update portion
    public void setUpdateitemcombobox(String names){
        updateCombobox.addItem(names);
    }
    
    public int itemComboboxlen(){
        return insertCombobox.getItemCount();
    }
    
    
    public void clearTextfieldafterinsert(){
        insertamounttxt.setText("");
        updateamounttxt.setText("");
    }
    
    /*initially no combo item is selected thats why serching item using combo box value causes errro
     thats why before searching using combo box it checks whether a item is selected in the 
    combobox */
    public boolean ComboSelectedItem(){
        int comboindex =-1;
        comboindex = insertCombobox.getSelectedIndex();
        return comboindex >=0;
    }
    
    
    public void printlist(int index){
        System.out.println();
        for (StoredItem item : storeinfo[index]) {
             System.out.print(item.flag + " ");
        }
    }
    
    // count the available amount of a item from arraylist 
    public Double countAvailableitem(int serial, String item){
        int index = comboItemtoindex(item);
        Double available= 0.00;
        for (StoredItem item1 : storeinfo[index]) {
            if(item1.dateserial <= serial){
                available = available + item1.inamount - (item1.bf + item1.lunch+item1.dinner);
            }
        }
        
        return available;
    }
    
    public Double nextavailableitem(int serial, String item , Double prevavailable){
        int index = comboItemtoindex(item);
        for( int i= storeinfo[index].size()-1 ; i>= 0 ; i--){
            if(storeinfo[index].get(i).dateserial > serial){
                prevavailable = prevavailable + storeinfo[index].get(i).inamount - 
                        (storeinfo[index].get(i).bf+storeinfo[index].get(i).lunch+storeinfo[index].get(i).dinner);
                
            }  
        }
        System.out.println(prevavailable);
        return prevavailable;
    }
    
    
    public void setInsertunit(int index){
        String name = comboIndextoitem(index);
        try{
            psmt = conn.prepareStatement("select unit from item where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                unitLbl.setText(rs.getString(1));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //number of item present in the item table 
    public int itemnumber(){
        try{
            psmt = conn.prepareStatement("select count(name) from item");
            rs = psmt.executeQuery();
            while(rs.next()){
                return rs.getInt(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch number of"
                    + "item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }
    
    
    
    public void Tabledecoration(){
        Storeouttable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 12));
        Storeouttable.getTableHeader().setOpaque(false);
        Storeouttable.getTableHeader().setBackground(new Color(32,136,203));
        Storeouttable.getTableHeader().setForeground(new Color(255,255,255));
        Storeouttable.setRowHeight(25);
        
        //InsertRemainAmountText.setEditable(false);
        //UpdateRemainingtxt.setEditable(false);
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
        Insertlabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        insertdatechooser = new com.toedter.calendar.JDateChooser();
        insertCombobox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        insertamounttxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        insertavailabletxt = new javax.swing.JTextField();
        insertbtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        unitLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        updatedatechooser = new com.toedter.calendar.JDateChooser();
        updateCombobox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        updateamounttxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        updatestatuscombo = new javax.swing.JComboBox<>();
        updateavailable = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        updatebtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Storeouttable = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        Insertlabel.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        Insertlabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Insertlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/cart.png"))); // NOI18N
        Insertlabel.setText("  STORE OUT");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Date ");

        insertdatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                insertdatechooserPropertyChange(evt);
            }
        });

        insertCombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertComboboxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Item");

        insertamounttxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Status");

        jComboBox2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "breakfast", "lunch", "dinner" }));

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Available");

        insertavailabletxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        insertbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Insertbtn (2).png"))); // NOI18N
        insertbtn.setText("Insert");
        insertbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertbtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Amount");

        unitLbl.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        unitLbl.setText("Unit");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(insertdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(insertamounttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(insertavailabletxt, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unitLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Insertlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertCombobox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Insertlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertamounttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertavailabletxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(unitLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel7.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        jLabel7.setText("UPDATE");

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Date");

        updatedatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        updateCombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Item ");

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Amount");

        updateamounttxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Status");

        updatestatuscombo.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatestatuscombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "breakfast", "lunch", "dinner" }));

        updateavailable.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Available");

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/updateBtn.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash.png"))); // NOI18N
        jButton3.setText("Delete");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setText("Unit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateamounttxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateavailable)
                    .addComponent(updatebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 75, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(updateCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updatestatuscombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updatestatuscombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(updateamounttxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateavailable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );

        Storeouttable.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        Storeouttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Amount", "Remaining Amount", "Date", "Status"
            }
        ));
        Storeouttable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        Storeouttable.setRowHeight(25);
        Storeouttable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        Storeouttable.setShowVerticalLines(false);
        Storeouttable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StoreouttableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Storeouttable);

        jButton4.setBackground(new java.awt.Color(204, 204, 255));
        jButton4.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        jButton4.setText("Save & Exit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_insertdatechooserPropertyChange
        // TODO add your handling code here:
        Date date = insertdatechooser.getDate();
        int dateserial =-1;
        String itemname = null;
        Double available = 0.00;
        if ( date != null && ComboSelectedItem()){
            dateserial = Integer.parseInt(formatter1.format(date));
            itemname = insertCombobox.getSelectedItem().toString();
            available = countAvailableitem(dateserial,itemname);
            insertavailabletxt.setText(available.toString());
            
            int comboindex =-1;
            comboindex = insertCombobox.getSelectedIndex();
            setInsertunit(comboindex);
        }
        
        
    }//GEN-LAST:event_insertdatechooserPropertyChange
    
    private void insertComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertComboboxActionPerformed
        // TODO add your handling code here:
        
     
    }//GEN-LAST:event_insertComboboxActionPerformed

    private void insertbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbtnActionPerformed
        // TODO add your handling code here:
        String name = insertCombobox.getSelectedItem().toString();
        String status = jComboBox2.getSelectedItem().toString();
        String amount = insertamounttxt.getText().trim();
        String remaining = insertavailabletxt.getText().trim();
        Date date = insertdatechooser.getDate();
        
        setOuttable(name,status,amount,remaining,date);
        Date date1 = insertdatechooser.getDate();
        int dateserial =-1;
        String itemname = null;
        Double available = 0.00;
        if ( date1 != null && ComboSelectedItem()){
            dateserial = Integer.parseInt(formatter1.format(date1));
            itemname = insertCombobox.getSelectedItem().toString();
            available = countAvailableitem(dateserial,itemname);
            insertavailabletxt.setText(available.toString());
        }
        
        
    }//GEN-LAST:event_insertbtnActionPerformed

    private void StoreouttableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StoreouttableMouseClicked
        // TODO add your handling code here:
        updatefield();
    }//GEN-LAST:event_StoreouttableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        String name = updateCombobox.getSelectedItem().toString();
        String status = updatestatuscombo.getSelectedItem().toString();
        String amount = updateamounttxt.getText().trim();
        String remaining = updateavailable.getText().trim();
        Date date = updatedatechooser.getDate();
        
        setOuttable(name,status,amount,remaining,date);
       
    }//GEN-LAST:event_updatebtnActionPerformed

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
            java.util.logging.Logger.getLogger(StoreOutItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOutItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOutItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOutItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOutItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Insertlabel;
    private javax.swing.JTable Storeouttable;
    private javax.swing.JComboBox<String> insertCombobox;
    private javax.swing.JTextField insertamounttxt;
    private javax.swing.JTextField insertavailabletxt;
    private javax.swing.JButton insertbtn;
    private com.toedter.calendar.JDateChooser insertdatechooser;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel unitLbl;
    private javax.swing.JComboBox<String> updateCombobox;
    private javax.swing.JTextField updateamounttxt;
    private javax.swing.JTextField updateavailable;
    private javax.swing.JButton updatebtn;
    private com.toedter.calendar.JDateChooser updatedatechooser;
    private javax.swing.JComboBox<String> updatestatuscombo;
    // End of variables declaration//GEN-END:variables
}
