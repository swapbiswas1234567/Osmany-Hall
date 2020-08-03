/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import static com.sun.xml.internal.ws.model.RuntimeModeler.capitalize;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class NonStoredItem extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;
    
    
    
    /**
     * Creates new form NonStoredItem
     */
    public NonStoredItem() {
        initComponents();
        inittialization();
        getAllstoreditem();
        Tabledecoration();
        setinsertunit();
        seteditunit();
        flag=1;
    }
    
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        insertdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        editdatechooser.setDate(todaysdate);
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) insertdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) editdatechooser.getDateEditor();
        dtedit1.setEditable(false);
        
        dec = new DecimalFormat("#0.000");
        model = nonStoretable.getModel();
        
        insertmemotxt.requestFocus();
    }
    
    public void Tabledecoration(){
        nonStoretable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        nonStoretable.getTableHeader().setOpaque(false);
        nonStoretable.getTableHeader().setBackground(new Color(32,136,203));
        nonStoretable.getTableHeader().setForeground(new Color(255,255,255));
        nonStoretable.setRowHeight(25);
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        nonStoretable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        nonStoretable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        
    }
    
    public void setinsertunit(){
        String name = insertnamecombobox.getSelectedItem().toString();
        String unit = getunit(name.toLowerCase());
        //System.out.println(unit+" "+name);
        insertunitlbl.setText(capitalize(unit));
        
    }
    
    public void seteditunit(){
        String name1 = insertnamecombobox.getSelectedItem().toString();
        String unit1 = getunit(name1.toLowerCase());
        //System.out.println(unit+" "+name);
        editunitlbl.setText(capitalize(unit1));
    }
    
    
    public void createnewitem(){
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
            //System.out.println(name.getText());
            addnewiteminlist(name.getText().toLowerCase().trim(),unit.getText().toLowerCase().trim());  // save the item name and 
            //unit in lower case
        } else {
            
        }
    }
   // adding new item in the database and also combobox 
    public void addnewiteminlist(String name, String unit){
        if(!name.equals("") && !unit.equals("")){
            String item = null;
            try{
            psmt = conn.prepareStatement("select name from nonstoreditemlist where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                item = rs.getString(1);
                //System.out.println(item);
            }
            psmt.close();
            rs.close();
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch "
                        + "data checking in addnewiteminlist", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            if(item != null){
                JOptionPane.showMessageDialog(null, "item already exists", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                try{
                psmt = conn.prepareStatement("insert into nonstoreditemlist (name,unit) values (?,?)");
                psmt.setString(1, name);
                psmt.setString(2, unit);
                psmt.execute();
                psmt.close();
                insertnamecombobox.addItem(capitalize(name));  // sending the name of item to set in the combobox
                editnamecombobox.addItem(capitalize(name));  // sending the name of item to set in the combobox
               
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "add new item in list error", "Data insertion error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        insertnamecombobox.setSelectedItem(capitalize(name));
        insertunitlbl.setText(capitalize(unit));
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter name and unit", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    //get name of all item from database 
    public void getAllstoreditem(){
       
        try{
            psmt = conn.prepareStatement("select name from nonstoreditemlist");
            rs = psmt.executeQuery();
            while(rs.next()){
                insertnamecombobox.addItem(capitalize(rs.getString(1)));  // sending the name of item to set in the combobox
                editnamecombobox.addItem(capitalize(rs.getString(1)));  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
   
    }
    
    public String getunit(String name){
        String unit = null;
        try{
            psmt = conn.prepareStatement("select unit from nonstoreditemlist where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                unit = rs.getString(1);
                //System.out.println(item);
            }
            psmt.close();
            rs.close();
            return unit;
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch "
                        + "unit in getunit", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
        return unit;
    }
    
    
    public void setnonstoredtable(String itemname, String amount, String totalprice, Date date,String status, String memono){
        //System.out.println(itemname+" "+amount+" "+totalprice+" "+date+" "+status+" "+memono);
        Double totalamount = 0.00, price = 0.00, avgprice =0.00;
        String strdate = null , intdate = null;
        int dateserial = 0;
        try{
            totalamount = Double.parseDouble(amount);
            price = Double.parseDouble(totalprice);
            strdate = formatter1.format(date);
            intdate = formatter.format(date);
            dateserial = Integer.parseInt(intdate);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "conversion error in setonstoretable", "Data"
                    + " conversion error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(intdate);
        boolean databasecheck = checkitemondatabase(itemname.toLowerCase(),dateserial,status.toLowerCase());
        boolean tablecheck = checkinjtable(itemname,strdate,status,-1);
        System.out.println(databasecheck);
        if(databasecheck){
            JOptionPane.showMessageDialog(null, "data for same item same status exist in database", "Data"
                    + " conversion error", JOptionPane.ERROR_MESSAGE);
        }
        else if(price > 0 && totalamount >0 && !databasecheck && !tablecheck){
            avgprice = price/totalamount;
            //System.out.println(totalamount+" "+price+" "+strdate);
            tablemodel = (DefaultTableModel) nonStoretable.getModel();
            Object o [] = {itemname,dec.format(totalamount),dec.format(price),dec.format(avgprice), strdate, status,memono};
            tablemodel.addRow(o);
            clearinsertfields();
            
        }
        else if(tablecheck){
            JOptionPane.showMessageDialog(null, "data for same item same status exist in Jtable", "Data"
                    + " conversion error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid amount in setonstortable", "Data"
                    + " insertion on Jtable error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean checkitemondatabase(String name, int dateserial , String state){
        
        //System.out.println(name+" "+dateserial+" "+state);
        try{
            psmt = conn.prepareStatement("select serial from nonstoreditem where name = ? and serial = ? and state = ?");
            psmt.setString(1, name);
            psmt.setInt(2, dateserial);
            psmt.setString(3, state);
            rs = psmt.executeQuery();
            while(rs.next()){
                psmt.close();
                rs.close();
                return true;
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
       return false; 
        
    }
    
    public boolean checkinjtable(String name, String date, String status, int index){
        int totalrow = -1;
        totalrow = nonStoretable.getRowCount();
        
        for( int i=0; i<totalrow; i++){
            if( model.getValueAt(i,0).toString().equals(name) && model.getValueAt(i, 4).toString().equals(date)
                    && model.getValueAt(i, 5).toString().equals(status) && i!= index){
                return true;
            }
        }
        
        return false;
    }
    
    
    public void updatefield(){
        String name = null ,amount = null, totalprice = null, strdate = null, status = null, memono = null;
        Date date = null;
        int selectedrow = nonStoretable.getSelectedRow();
        name = model.getValueAt(selectedrow, 0).toString();
        amount = model.getValueAt(selectedrow, 1).toString();
        totalprice = model.getValueAt(selectedrow, 2).toString();
        strdate = model.getValueAt(selectedrow, 4).toString();
        status = model.getValueAt(selectedrow, 5).toString();
        memono = model.getValueAt(selectedrow, 6).toString();
        //System.out.println(name+" "+amount+" "+totalprice+" "+strdate+" "+status+" "+memono);
        
        try{
            date = formatter1.parse(strdate);
        }
        catch(ParseException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "error in Updatefield", "Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(date);
        editdatechooser.setDate(date);
        editnamecombobox.setSelectedItem(name);
        editstatecombobox.setSelectedItem(status);
        editmemotxt.setText(memono);
        editamounttxt.setText(amount);
        editpricetxt.setText(totalprice);
        seteditunit();
    }
    
    
    public void seteditedlevalue(){
        String name = "", state = "", memo = "", amount = "" , price = "", strdate="";
        int selectedrow = -1;
        Double totalamount = 0.00, totalprice = 0.00, avgprice=0.00;
        Date date=null;
        name = editnamecombobox.getSelectedItem().toString();
        state = editstatecombobox.getSelectedItem().toString();
        memo = editmemotxt.getText().trim();
        amount = editamounttxt.getText().trim();
        price = editpricetxt.getText().trim();
        date = editdatechooser.getDate();
        selectedrow = nonStoretable.getSelectedRow();
        
        try{
            strdate = formatter1.format(date);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.println(selectedrow);
        boolean checkjtable = checkinjtable(name,strdate,state, selectedrow);
        //System.out.println(checkjtable);
        //System.out.println(name+" "+amount+" "+price+" "+date+" "+state+" "+memo);
        if(checkjtable){
            JOptionPane.showMessageDialog(null, "Same data already"
                    + " exist in Table","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        else if(selectedrow <0){
            JOptionPane.showMessageDialog(null, "No row is selected","Data updating error", JOptionPane.ERROR_MESSAGE);
        }
        else if( Double.parseDouble(amount) > 0 && Double.parseDouble(price) >0 && !(memo.equals("") || price.equals("")
                || amount.equals(""))){
            try{
                totalamount = Double.parseDouble(amount);
                totalprice = Double.parseDouble(price);
                avgprice = totalprice/totalamount;
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "write correct number","updating date error", JOptionPane.ERROR_MESSAGE);
            }
            
            model.setValueAt(name, selectedrow , 0);
            model.setValueAt(amount, selectedrow , 1);
            model.setValueAt(price, selectedrow , 2);
            model.setValueAt(dec.format(avgprice), selectedrow , 3);
            model.setValueAt(strdate, selectedrow , 4);
            model.setValueAt(state, selectedrow , 5);
            model.setValueAt(memo, selectedrow , 6);
        }
    }
    
    
    
    public void deleterow(int selectedrow){
        
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete"
                + " The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION){
            tablemodel = (DefaultTableModel) nonStoretable.getModel();
            tablemodel.removeRow(selectedrow);
        }
    }
    
    public void insert(){
        String name = "", state = "", memo = "###", amount = "" , price = "", strdate="";
        Double totalamount = 0.00, totalprice = 0.00;
        Date date =null;
        int totalrow =-1, dateserial = 0;
        totalrow = nonStoretable.getRowCount();
        //System.out.print(totalrow);
        
        for( int i=0; i< totalrow; i++){
            name = model.getValueAt(i, 0).toString().toLowerCase();
            amount = model.getValueAt(i, 1).toString();
            price = model.getValueAt(i, 2).toString();
            strdate = model.getValueAt(i, 4).toString();
            state = model.getValueAt(i, 5).toString().toLowerCase();
            memo = model.getValueAt(i, 6).toString();
            //System.out.print(name+" "+amount+" "+price+" "+strdate+" "+state+" "+memo);
            try{
                date = formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
                totalamount = Double.parseDouble(amount);
                totalprice = Double.parseDouble(price);
                //System.out.print(dateserial);
            }
            catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null, "date parsing error"
                        + "while inserting data","date error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            try{
                psmt = conn.prepareStatement("insert into nonstoreditem (serial,name,amount,price, memono,state) values (?,?,?,?,?,?)");
                psmt.setInt(1, dateserial);
                psmt.setString(2, name);
                psmt.setDouble(3, totalamount);
                psmt.setDouble(4, totalprice);
                psmt.setString(5, memo);
                psmt.setString(6, state);
                psmt.execute();
                psmt.close();
                
               
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "insertion error", "Data insertion error", JOptionPane.ERROR_MESSAGE);
                }
            
            
        }
        
    }
    
    
    public void clearinsertfields(){
        insertamountxt.setText("");
        insertpricetxt.setText("");
        insertmemotxt.setText("");
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
        jLabel3 = new javax.swing.JLabel();
        insertdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        insertstatecombobox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        insertnamecombobox = new javax.swing.JComboBox<>();
        createbtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        insertamountxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        insertmemotxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        insertpricetxt = new javax.swing.JTextField();
        insertbtn = new javax.swing.JButton();
        insertunitlbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        editdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        editstatecombobox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        editnamecombobox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        editamounttxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        editmemotxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        editpricetxt = new javax.swing.JTextField();
        editbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        editunitlbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nonStoretable = new javax.swing.JTable();
        saveandexit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));
        jPanel1.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/nonstored.png"))); // NOI18N
        jLabel1.setText("NON STORED ITEMS");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date ");

        insertdatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Name ");

        insertstatecombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertstatecombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("State ");

        insertnamecombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertnamecombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertnamecomboboxActionPerformed(evt);
            }
        });

        createbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        createbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/add.png"))); // NOI18N
        createbtn.setText("Create");
        createbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createbtnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Amount ");

        insertamountxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Memo No");

        insertmemotxt.setFont(new java.awt.Font("Bodoni MT", 0, 16)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Price");

        insertpricetxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        insertbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/noninsert.png"))); // NOI18N
        insertbtn.setText("INSERT");
        insertbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertbtnActionPerformed(evt);
            }
        });

        insertunitlbl.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertunitlbl.setForeground(new java.awt.Color(255, 0, 0));
        insertunitlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(createbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(insertbtn))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                    .addComponent(insertnamecombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(insertamountxt))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(insertmemotxt)
                                    .addComponent(insertstatecombobox, 0, 119, Short.MAX_VALUE)
                                    .addComponent(insertpricetxt))))
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(insertunitlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertstatecombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertnamecombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(insertmemotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertamountxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertpricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertunitlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(117, 175, 182));

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel9.setText("  EDIT");

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Date ");

        editdatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Name ");

        editstatecombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        editstatecombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));

        jLabel12.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("State ");

        editnamecombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        editnamecombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editnamecomboboxActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Amount ");

        editamounttxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel14.setText("Memo No");

        editmemotxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Price");

        editpricetxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        editbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        editbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/rubber.png"))); // NOI18N
        editbtn.setText("Edit");
        editbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editbtnActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash.png"))); // NOI18N
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        editunitlbl.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        editunitlbl.setForeground(new java.awt.Color(255, 0, 0));
        editunitlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editunitlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(editdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editnamecombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editamounttxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editstatecombobox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 113, Short.MAX_VALUE)
                    .addComponent(editbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editmemotxt)
                    .addComponent(editpricetxt))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editstatecombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editnamecombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editmemotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editamounttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editpricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editunitlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        nonStoretable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Amount", "Total Price", "Avg Price", "Date", "Status", "Memo No"
            }
        ));
        nonStoretable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        nonStoretable.setRowHeight(26);
        nonStoretable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        nonStoretable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        nonStoretable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nonStoretableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(nonStoretable);

        saveandexit.setFont(new java.awt.Font("Bell MT", 0, 25)); // NOI18N
        saveandexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        saveandexit.setText("  Save & Exit");
        saveandexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveandexitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(saveandexit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveandexit, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createbtnActionPerformed
        // TODO add your handling code here:
        
        createnewitem();
        insertmemotxt.requestFocus();
    }//GEN-LAST:event_createbtnActionPerformed

    private void insertbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbtnActionPerformed
        // TODO add your handling code here:
        String name = null, state = null, memo = null, amount = null , price = null;
        Date date=null;
        name = insertnamecombobox.getSelectedItem().toString();
        state = insertstatecombobox.getSelectedItem().toString();
        memo = insertmemotxt.getText().trim();
        amount = insertamountxt.getText().trim();
        price = insertpricetxt.getText().trim();
        date = insertdatechooser.getDate();
        
        if(memo.equals("") || amount.equals("") || price.equals("")){
            JOptionPane.showMessageDialog(null, "Fill all the field before"
                    + "insertion", "Data read error", JOptionPane.ERROR_MESSAGE);
            
        }
        else{
            setnonstoredtable(name,amount,price,date,state,memo);
        }
        
    }//GEN-LAST:event_insertbtnActionPerformed

    private void nonStoretableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nonStoretableMouseClicked
        // TODO add your handling code here:
        updatefield();
        editmemotxt.requestFocus();
    }//GEN-LAST:event_nonStoretableMouseClicked

    private void insertnamecomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertnamecomboboxActionPerformed
        // TODO add your handling code here:
        if(flag == 1){
            setinsertunit();
        }
        
    }//GEN-LAST:event_insertnamecomboboxActionPerformed

    private void editnamecomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editnamecomboboxActionPerformed
        // TODO add your handling code here:
        if( flag == 1){
            seteditunit();
        }
        
    }//GEN-LAST:event_editnamecomboboxActionPerformed

    private void editbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editbtnActionPerformed
        // TODO add your handling code here:
        seteditedlevalue();
        nonStoretable.clearSelection();
    }//GEN-LAST:event_editbtnActionPerformed

    private void saveandexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveandexitActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_saveandexitActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow = -1;
        selectedrow = nonStoretable.getSelectedRow();
        if( selectedrow > 0){
            deleterow(selectedrow);    
        }
        else{
            JOptionPane.showMessageDialog(null, "No Row is selected"
                   , "Delete Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(NonStoredItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NonStoredItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NonStoredItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NonStoredItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NonStoredItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createbtn;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField editamounttxt;
    private javax.swing.JButton editbtn;
    private com.toedter.calendar.JDateChooser editdatechooser;
    private javax.swing.JTextField editmemotxt;
    private javax.swing.JComboBox<String> editnamecombobox;
    private javax.swing.JTextField editpricetxt;
    private javax.swing.JComboBox<String> editstatecombobox;
    private javax.swing.JLabel editunitlbl;
    private javax.swing.JTextField insertamountxt;
    private javax.swing.JButton insertbtn;
    private com.toedter.calendar.JDateChooser insertdatechooser;
    private javax.swing.JTextField insertmemotxt;
    private javax.swing.JComboBox<String> insertnamecombobox;
    private javax.swing.JTextField insertpricetxt;
    private javax.swing.JComboBox<String> insertstatecombobox;
    private javax.swing.JLabel insertunitlbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable nonStoretable;
    private javax.swing.JButton saveandexit;
    // End of variables declaration//GEN-END:variables
}
