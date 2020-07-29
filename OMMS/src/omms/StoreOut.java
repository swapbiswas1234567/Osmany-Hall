/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.Format;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class StoreOut extends javax.swing.JFrame {
    
    Connection conn = null;   //declaring connecting variable with database
    PreparedStatement psmt = null; // declaring variable to run sql query
    ResultSet rs = null; // variable to receive the data fetch from database
    DefaultTableModel tablemodel = null; // declaring JTable
    JTextFieldDateEditor datedit= null;  // variable to control jdatechooser edit option
    int selectedrow = 0;  // variable to store the selected row of table by mouse click

    /**
     * Creates new form StoreOut
     */
    public StoreOut() {
        initComponents();
        conn = Jconnection.ConnecrDb(); // set connection with database
        TableDecoration();  // calling function to decorate table
        InitDatditor();    //function for disabling date edit
        GetStoreItem();    // fetch the item name from store table
        SetUpdateStatusCombo();  // update the status combobox in beakfast lunch & dinner
        SetInsertStatusCombo(); // Insert combobox value set according to the store table data item
    }
    
    //fundtion for decorating the table 
    public void TableDecoration(){
        StoreOutTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 12));
        StoreOutTable.getTableHeader().setOpaque(false);
        StoreOutTable.getTableHeader().setBackground(new Color(32,136,203));
        StoreOutTable.getTableHeader().setForeground(new Color(255,255,255));
        StoreOutTable.setRowHeight(25);
        
    }
    
    
    // function will return the item list of store table to show in combo box
    public void GetStoreItem(){
        try{
            psmt = conn.prepareStatement("select name from item");
            rs = psmt.executeQuery();
            while( rs.next()){
               // System.out.println("called while  "+rs.getString(1));
                SetInsertItemCombo(rs.getString(1));  // calling function to slect the item of the combobox in insert portion
                SetUpdateItemCombo(rs.getString(1));  // calling function to slect the item of the combobox in update portion
            }
            psmt.close();
            rs.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error Whilte Fetching data from store table for combo box","Inserting Data Error",JOptionPane.ERROR_MESSAGE);
        }
       
    }
    
    //function for setting the item list of combobox in insert portion
    public void SetInsertItemCombo(String names){
        InsertItemCombo.addItem(names);

    }
    //function for setting the item list of combobox in update portion
    public void SetUpdateItemCombo(String names){
        UpdateItemCombo.addItem(names);
    }
    
    //function for setting the item list of combobox in update portion
    public void SetUpdateStatusCombo(){
        UpdateStatusCombo.addItem("Breakfast");
        UpdateStatusCombo.addItem("Lunch");
        UpdateStatusCombo.addItem("Dinner");
    }
    
    //function for setting the item list of combobox in update portion
    public void SetInsertStatusCombo(){
        InsertStatusCombo.addItem("Breakfast");
        InsertStatusCombo.addItem("Lunch");
        InsertStatusCombo.addItem("Dinner");
    }
    
    // function for disabling date editor
    private void InitDatditor(){
        datedit = (JTextFieldDateEditor) Insertdatechoser.getDateEditor();
        datedit.setEditable(false); // disabling date edit
        Date datchosershow =new Date();
        Insertdatechoser.setDate(datchosershow);  // set todays date to both of the datechooser by default
        UpdateDatechooser.setDate(datchosershow);
    }
    
    
    
    
    // function to insert the data in the JTable user want to out from the store 
    public void InsertTable(){   
        try{
            // storing all the input in the variables
            String item = InsertItemCombo.getSelectedItem().toString();
            String status = InsertStatusCombo.getSelectedItem().toString();
            Double remainingamount = Double.parseDouble(InsertRemainAmountText.getText().trim());
            Double amount = Double.parseDouble(InsertAmountText.getText().trim());
            Date date = Insertdatechoser.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            
            //checking conditions before inserting on the table 
            if( item != null && status != null && date != null && amount > 0 && CheckTableItem(item,status,formatter.format(date)) < 0){
                //if(amount >0 ){
                    //boolean a=CheckTableItem(item);
                    tablemodel = (DefaultTableModel) StoreOutTable.getModel();
                    Object o [] = {item, amount , remainingamount,formatter.format(date), status};
                    tablemodel.addRow(o);
                //}
                /*else if(amount<= 0){
                    JOptionPane.showMessageDialog(null,"You have inserted amount which is equal or less than zero","Inserting Data Error",JOptionPane.ERROR_MESSAGE);
                }*/
            }
            else{
                JOptionPane.showMessageDialog(null,"you are trying to insert zero amount or Null date or item exist in table","Inserting Data Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null,"you are trying to insert Null Values","Showing error while inserting data from textfield",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    // fucntion to show the selected row from jtable to update list
    public void updatefield(){
        int itemcomboindex =-1;
        int statuscomboindex =-1;
        selectedrow = StoreOutTable.getSelectedRow();
        TableModel model = StoreOutTable.getModel();
        
        String tablecomboitem = model.getValueAt(selectedrow,0).toString().trim();
        for (int i=0 ; i<UpdateItemCombo.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateItemCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (UpdateItemCombo.getItemAt(i).toString().equals(tablecomboitem)){
                itemcomboindex = i;
                break;
            }
        }
        
        String amount = model.getValueAt(selectedrow,1).toString().trim();
        String remainingamount = model.getValueAt(selectedrow,2).toString().trim();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");
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
        for (int i=0 ; i<UpdateStatusCombo.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateStatusCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (UpdateStatusCombo.getItemAt(i).toString().equals(tablecombostatus)){
                statuscomboindex = i;
                break;
            }
        }
        
        if(itemcomboindex >=0 && statuscomboindex >=0){
            UpdateRemainingtxt.setText(remainingamount);
            UpdateAmountxt.setText(amount);
            UpdateItemCombo.setSelectedIndex(itemcomboindex);
            UpdateStatusCombo.setSelectedIndex(statuscomboindex);
            UpdateDatechooser.setDate(showdate);
        }
        else{
            JOptionPane.showMessageDialog(null,"item and status combo box did not mathed with row","Data Showing error",JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(itemcomboindex);
        
    }
    
    // function of update the selected table row value
    public void updatTable(){
        try{
            selectedrow = StoreOutTable.getSelectedRow();
            String item = UpdateItemCombo.getSelectedItem().toString();
            String status = UpdateStatusCombo.getSelectedItem().toString();
            Double remainingamount = Double.parseDouble(UpdateRemainingtxt.getText().trim());
            Double amount = Double.parseDouble(UpdateAmountxt.getText().trim());
            Date date = UpdateDatechooser.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        
        
            
           // System.out.println(tableindx+" "+selectedrow);
            if( item != null && status != null && date != null && amount > 0 && selectedrow >=0 && CheckTableItem(item,status,formatter.format(date)) < 0){
                    tablemodel = (DefaultTableModel) StoreOutTable.getModel();
                    tablemodel.setValueAt(item,selectedrow, 0);
                    tablemodel.setValueAt(amount,selectedrow, 1);
                    tablemodel.setValueAt(remainingamount,selectedrow, 2);
                    tablemodel.setValueAt(formatter.format(date),selectedrow, 3);
                    tablemodel.setValueAt(status,selectedrow, 4);
                    
            }
            else{
                JOptionPane.showMessageDialog(null,"you are trying to Update zero amount or Null date or item exist or did not selected a row in table","Updating Data Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null,"you are trying to Update Null Values","Showing error while Updating data from textfield",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //function for delete a selected table row
    public void DeleteTableRow(){
        selectedrow = StoreOutTable.getSelectedRow();
        if( selectedrow >=0 ){
            int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION){
                tablemodel = (DefaultTableModel) StoreOutTable.getModel();
                tablemodel.removeRow(selectedrow);
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null,"No Row is selected","Showing error while Updating data from textfield",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //function to set the remaining amount based on date
    public void SetRemainingAmount(){
        String remaining = null;
        Date date = Insertdatechoser.getDate();
        if ( date != null ){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateserial = formatter.format(date);
            //System.out.println(dateserial);
            try{
            psmt = conn.prepareStatement("select available from storeinput where serial = ?");
            psmt.setString(1, dateserial);
            rs = psmt.executeQuery();
            if(rs.next()){
                remaining = rs.getString(1);
                //System.out.println(remaining);
                InsertRemainAmountText.setText(remaining);
            }
            else {
                psmt = conn.prepareStatement("select max(available) from storeinput where serial < ?");
                psmt.setString(1, dateserial);
                rs = psmt.executeQuery();
                remaining = rs.getString(1);
                //System.out.println("called else not zero  "+dateserial+" "+rs.getString(1));
                if(rs.next()){
                    //System.out.println("called inside"+remaining+"  "+remaining);
                    InsertRemainAmountText.setText(remaining);
                }
               
               
            }
            
            
            psmt.close();
            rs.close();
            }
            catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Error in SetRemainingItem","Fetching error from database",JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    
    
    // function to get the index number of a item based on its time it consumed or the status and input date
    public int CheckTableItem(String item, String status, String date){
        int row = StoreOutTable.getRowCount();
        TableModel model = StoreOutTable.getModel();
        int tableindx = -1;
        for(int i=0; i<row ; i++){
            //System.out.println(model.getValueAt(i,0).toString().trim());
            if (model.getValueAt(i,0).toString().trim().equals(item) && model.getValueAt(i,4).toString().trim().equals(status) && model.getValueAt(i,3).toString().trim().equals(date)){
                tableindx++;
                return tableindx;
            }
        }
        return tableindx;
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
        jLabel3 = new javax.swing.JLabel();
        InsertItemCombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        InsertRemainAmountText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        InsertAmountText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        Insertdatechoser = new com.toedter.calendar.JDateChooser();
        InsertStatusCombo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        InsertBt = new javax.swing.JButton();
        UpdateItemCombo = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        UpdateRemainingtxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        UpdateAmountxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        UpdateDatechooser = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        UpdateStatusCombo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        DeleteBtn = new javax.swing.JButton();
        UpdateBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        StoreOutTable = new javax.swing.JTable();
        StoreOutSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Store Out");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\update1.png")); // NOI18N
        jLabel1.setText("Update");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\cart.png")); // NOI18N
        jLabel2.setText("Insert");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Remaining Amount :");

        InsertItemCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InsertItemComboActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Item");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Amount :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Date:");

        Insertdatechoser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                InsertdatechoserPropertyChange(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Status :");

        InsertBt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        InsertBt.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\insertBtn.png")); // NOI18N
        InsertBt.setText("Insert");
        InsertBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InsertBtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Item:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Amount :");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Remaining Amount :");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Date:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Status :");

        DeleteBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        DeleteBtn.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\trash.png")); // NOI18N
        DeleteBtn.setText("Delete");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });

        UpdateBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        UpdateBtn.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\updateBtn.png")); // NOI18N
        UpdateBtn.setText("Update");
        UpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(UpdateItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InsertItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(InsertAmountText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                .addComponent(InsertRemainAmountText, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(UpdateAmountxt, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UpdateRemainingtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(UpdateBtn)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DeleteBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InsertBt, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(UpdateDatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                .addComponent(InsertStatusCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Insertdatechoser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(UpdateStatusCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(20, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(193, 193, 193))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(186, 186, 186))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InsertItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Insertdatechoser, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InsertRemainAmountText, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(InsertStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InsertAmountText, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InsertBt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(UpdateItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(UpdateDatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UpdateStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(UpdateRemainingtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(UpdateAmountxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(UpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        StoreOutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Amount", "Remaining Amount", "Date", "Status"
            }
        ));
        StoreOutTable.setFocusable(false);
        StoreOutTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        StoreOutTable.setRowHeight(25);
        StoreOutTable.setSelectionBackground(new java.awt.Color(232, 57, 95));
        StoreOutTable.setShowVerticalLines(false);
        StoreOutTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StoreOutTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(StoreOutTable);

        StoreOutSave.setBackground(new java.awt.Color(153, 153, 255));
        StoreOutSave.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        StoreOutSave.setForeground(new java.awt.Color(255, 255, 255));
        StoreOutSave.setIcon(new javax.swing.ImageIcon("G:\\New folder (2)\\GIT\\osmany hall\\icons\\save&exitbtn (2).png")); // NOI18N
        StoreOutSave.setText("  SAVE & EXIT");
        StoreOutSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StoreOutSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                    .addComponent(StoreOutSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(StoreOutSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    //this function will trigger if we select a date from jdatechooser
    private void InsertdatechoserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_InsertdatechoserPropertyChange
        // TODO add your handling code here:
        SetRemainingAmount();
    }//GEN-LAST:event_InsertdatechoserPropertyChange
    
    // function will trigger if item is inserted in the insert combo box
    private void InsertItemComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsertItemComboActionPerformed
        // TODO add your handling code here:
        String item = InsertItemCombo.getSelectedItem().toString();
        //System.out.print(item);
    }//GEN-LAST:event_InsertItemComboActionPerformed
    

    // function will triggered if insert button is pressed
    private void InsertBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsertBtActionPerformed
        // TODO add your handling code here:
        InsertTable();
    }//GEN-LAST:event_InsertBtActionPerformed
    
    // function will triggered if mouse is clicked on table row
    private void StoreOutTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StoreOutTableMouseClicked
        // TODO add your handling code here:
        
        updatefield();
    }//GEN-LAST:event_StoreOutTableMouseClicked
    
    //function will triggered if delete button is pressed
    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        // TODO add your handling code here:
        
        DeleteTableRow();
    }//GEN-LAST:event_DeleteBtnActionPerformed
    
    
    //function will triggered if update button is pressed
    private void UpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBtnActionPerformed
        // TODO add your handling code here:
        updatTable();
    }//GEN-LAST:event_UpdateBtnActionPerformed
    
    //function will triggered if save & exit button is pressed
    private void StoreOutSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StoreOutSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StoreOutSaveActionPerformed

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
            java.util.logging.Logger.getLogger(StoreOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOut().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JTextField InsertAmountText;
    private javax.swing.JButton InsertBt;
    private javax.swing.JComboBox<String> InsertItemCombo;
    private javax.swing.JTextField InsertRemainAmountText;
    private javax.swing.JComboBox<String> InsertStatusCombo;
    private com.toedter.calendar.JDateChooser Insertdatechoser;
    private javax.swing.JButton StoreOutSave;
    private javax.swing.JTable StoreOutTable;
    private javax.swing.JTextField UpdateAmountxt;
    private javax.swing.JButton UpdateBtn;
    private com.toedter.calendar.JDateChooser UpdateDatechooser;
    private javax.swing.JComboBox<String> UpdateItemCombo;
    private javax.swing.JTextField UpdateRemainingtxt;
    private javax.swing.JComboBox<String> UpdateStatusCombo;
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
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
