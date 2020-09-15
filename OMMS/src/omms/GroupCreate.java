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
import java.util.Locale;
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

/**
 *
 * @author Ajmir
 */
public class GroupCreate extends javax.swing.JFrame {
    
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    int flag=0;
    
    

    /**
     * Creates new form CreateGroup
     */
    public GroupCreate() {
        initComponents();
        Tabledecoration();
        inittialization();
        closeBtn();
        
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
    
    
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        createdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        updatedatechooser.setDate(todaysdate);
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) createdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) updatedatechooser.getDateEditor();
        dtedit1.setEditable(false);
        
        model = grptable.getModel();
        
    }
    
    
    
    
    public void Tabledecoration(){
        grptable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        grptable.getTableHeader().setOpaque(false);
        grptable.getTableHeader().setBackground(new Color(32,136,203));
        grptable.getTableHeader().setForeground(new Color(255,255,255));
        grptable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        grptable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        grptable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        grptable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
    }
    
    public int checkinjtable(String date, String name,String state, int row){
        int totalrow=-1, found=-1;
        totalrow= grptable.getRowCount();
        for(int i=0; i<totalrow; i++){
            if(model.getValueAt(i, 0).toString().equals(date) && model.getValueAt(i, 1).toString().equals(name)
                    && model.getValueAt(i, 2).toString().equals(state) && i!= row){
                found=i;
                break;  
            }
            
        }
        return found;
    }
    
    public boolean searchdatabase(int dateserial , String name, String state){
        boolean found=false;
        //System.out.println(dateserial+" "+name+" "+state);
        try{
            psmt = conn.prepareStatement("select serial FROM grp where date=? and name = ? and state = ?");
            psmt.setInt(1, dateserial);
            psmt.setString(2, name.toLowerCase());
            psmt.setString(3, state.toLowerCase(Locale.ITALY));
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.gtiout.println(rs.getInt(1));
                found = true;
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return found;
    }
    
    public void jtableinsert(Date date, String name, String state){
        int dateserial=0, jtable=-1;
        String strdate="";
        boolean database=false;
        tablemodel = (DefaultTableModel) grptable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
            strdate = formatter1.format(date);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"date convertion error","Date Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.print(dateserial+" "+state);
      
        database= searchdatabase(dateserial,name,state);
        jtable = checkinjtable(strdate,name,state,-1);
       // System.out.print(database);
        if(jtable == -1 && !database){
            Object o [] = {strdate, name,state};
            tablemodel.addRow(o);
        }
        else if(database){
            JOptionPane.showMessageDialog(null,name+" already exist in database in "+strdate,"Data insertion Error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,name+" already exist in jtable in "+strdate,"Data insertion Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void setupdatefield(int selectedrow){
        String strdate="",name="";
        Date date=null;
        Object state;
        
        strdate = model.getValueAt(selectedrow, 0).toString();
        name = model.getValueAt(selectedrow, 1).toString();
        state = model.getValueAt(selectedrow, 2);
        
        try{
            date = formatter1.parse(strdate);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"date parsing error ","Data update Error",JOptionPane.ERROR_MESSAGE);
        }
        updatedatechooser.setDate(date);
        updatenametxt.setText(name);
        updatecombo.setSelectedItem(state);
    }
    
    
    public void updatetable(int selectedrow, Date date, String name, String state){
        int dateserial=0 , jtable=-1;
        String strdate="";
        boolean found =false;
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
            strdate = formatter1.format(date);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"date convertion error","Date Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(selectedrow < 0){
            JOptionPane.showMessageDialog(null,"selectrow a row","Date Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        found = searchdatabase(dateserial,name,state);
        jtable = checkinjtable(strdate,name,state,selectedrow);
        if(!found && jtable ==-1 ){
            model.setValueAt(strdate,selectedrow , 0);
            model.setValueAt(name, selectedrow, 1);
            model.setValueAt(state, selectedrow, 2);
            grptable.clearSelection();
        }
        else if (found){
            JOptionPane.showMessageDialog(null,name+" exist in Database in "+ strdate,"Date Error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null,name+" exist in Jtable in "+ strdate,"Date Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void saveandexit(){
        int totalrow =-1, dateserial=0, serial =-1;
        totalrow = model.getRowCount();
        String strdate="", name="", state="";
        Date date=null;
        
        for(int i=0; i<totalrow; i++){
            strdate = model.getValueAt(i, 0).toString();
            name = model.getValueAt(i, 1).toString();
            state = model.getValueAt(i, 2).toString();
            
            
            try{
                date = formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
            }catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null,"date convertion error","Date Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            //System.out.println("befor "+serial);
            
            
            try{
            psmt = conn.prepareStatement("select max(serial) from grp where date = ? and state = ?");
            psmt.setInt(1, dateserial);
            psmt.setString(2, state.toLowerCase());
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.gtiout.println(rs.getInt(1));
                serial=rs.getInt(1);
                //System.out.println(serial);
            }
            psmt.close();
            rs.close();
            
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "failed to find max serial", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(serial == 0){
                serial = 1;
            }
            else{
                serial++;
            }
            
           // System.out.println("after "+serial);
            
            try{
                psmt = conn.prepareStatement("insert into grp(date, name, serial, state) VALUES( ?, ?, ?, ?)");
                psmt.setInt(1, dateserial);
                psmt.setString(2, name.toLowerCase());
                psmt.setInt(3, serial);
                psmt.setString(4, state.toLowerCase());
                psmt.execute();
                psmt.close();
            }  
            catch(SQLException e){
                JOptionPane.showMessageDialog(null, "insertion error"
                        + " in create group", "Data insertion error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(state.equals("Breakfast")){
               
                try{
                    psmt = conn.prepareStatement("update mealsheet set bfgrp=1 where date=?");
                    psmt.setInt(1, dateserial);
                    psmt.execute();
                    psmt.close();
                    //System.out.println("called");
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Updating mealsheet grp error for "+ strdate, "Data insertion error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            else if(state.equals("Lunch")){
               
                try{
                psmt = conn.prepareStatement("update mealsheet set lunchgrp=1 where date=?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Updating mealsheet grp error for "+ strdate, "Data insertion error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            else if(state.equals("Dinner")){
               
                try{
                psmt = conn.prepareStatement("update mealsheet set dinnergrp=1 where date=?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Updating mealsheet grp error for "+ strdate, "Data insertion error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
        }
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
        createdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        createnametxt = new javax.swing.JTextField();
        createbtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        createcombo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        updatedatechooser = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        updatenametxt = new javax.swing.JTextField();
        updatebtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        updatecombo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        grptable = new javax.swing.JTable();
        saveandexit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/add (1).png"))); // NOI18N
        jLabel1.setText("Create Groups");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("Date");

        createdatechooser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        createdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                createdatechooserPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/user.png"))); // NOI18N
        jLabel3.setText("Name");

        createnametxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        createnametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createnametxtActionPerformed(evt);
            }
        });

        createbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        createbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/add-button.png"))); // NOI18N
        createbtn.setText("Create");
        createbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createbtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel7.setText("State");

        createcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        createcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        createcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createcomboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(createcombo, 0, 140, Short.MAX_VALUE)
                    .addComponent(createdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(createnametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(createbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createdatechooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createnametxt))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel4.setText("Update");

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel5.setText("Date");

        updatedatechooser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        updatedatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                updatedatechooserPropertyChange(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/user.png"))); // NOI18N
        jLabel6.setText("Name");

        updatenametxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        updatenametxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatenametxtActionPerformed(evt);
            }
        });

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update meal.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel8.setText("State");

        updatecombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        updatecombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatecomboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatecombo, 0, 140, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatenametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatedatechooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatenametxt))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updatebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatecombo, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(51, 51, 51))
        );

        grptable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        grptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Name", "State"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grptable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        grptable.setRowHeight(26);
        grptable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        grptable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        grptable.setShowVerticalLines(false);
        grptable.getTableHeader().setReorderingAllowed(false);
        grptable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grptableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(grptable);

        saveandexit.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        saveandexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        saveandexit.setText("Save & Exit");
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(saveandexit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveandexit, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void createbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createbtnActionPerformed
        // TODO add your handling code here:
        Date date=null;
        String name="", state="";
        
        date = createdatechooser.getDate();
        name = createnametxt.getText().trim();
        state = createcombo.getSelectedItem().toString();
        //System.out.println(state);
        if(!name.equals("")){
            jtableinsert(date,name,state);
            createnametxt.setText("");
        }
        else{
            JOptionPane.showMessageDialog(null,"name is empty","Data insertion Error",JOptionPane.ERROR_MESSAGE);
        }
        
        //jtableinsert
    }//GEN-LAST:event_createbtnActionPerformed

    private void grptableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grptableMouseClicked
        // TODO add your handling code here:
        int selectedrow=-1;
        selectedrow = grptable.getSelectedRow();
        if(selectedrow >=0){
            setupdatefield(selectedrow);
            updatenametxt.requestFocus();
        }
        
    }//GEN-LAST:event_grptableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        String name="", state="";
        int selectedrow =-1;
        
        date = updatedatechooser.getDate();
        name = updatenametxt.getText().trim();
        state = updatecombo.getSelectedItem().toString();
        selectedrow = grptable.getSelectedRow();
        if(selectedrow >= 0){
            updatetable(selectedrow, date, name,state);
            
        }
        else{
            JOptionPane.showMessageDialog(null,"No Row is Selected","Data Update Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updatebtnActionPerformed

    private void saveandexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveandexitActionPerformed
        // TODO add your handling code here:
        //saveandexit();
        if( model.getRowCount() > 0){
            int responce = JOptionPane.showConfirmDialog(this,"Do you want to save the data ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION){
                try {
                    saveandexit();
                    
                    JFrame frame = this;
                    NewDashboard das = new NewDashboard();
                    das.setVisible(true);
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
    }//GEN-LAST:event_saveandexitActionPerformed

    private void createnametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createnametxtActionPerformed
        // TODO add your handling code here:
        createbtn.doClick();
    }//GEN-LAST:event_createnametxtActionPerformed

    private void updatenametxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatenametxtActionPerformed
        // TODO add your handling code here:
        updatebtn.doClick();
    }//GEN-LAST:event_updatenametxtActionPerformed

    private void createdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_createdatechooserPropertyChange
        // TODO add your handling code here:
        createnametxt.requestFocus();
    }//GEN-LAST:event_createdatechooserPropertyChange

    private void createcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createcomboActionPerformed
        // TODO add your handling code here:
        createnametxt.requestFocus();
    }//GEN-LAST:event_createcomboActionPerformed

    private void updatedatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_updatedatechooserPropertyChange
        // TODO add your handling code here:
        updatenametxt.requestFocus();
    }//GEN-LAST:event_updatedatechooserPropertyChange

    private void updatecomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatecomboActionPerformed
        // TODO add your handling code here:
        updatenametxt.requestFocus();
    }//GEN-LAST:event_updatecomboActionPerformed

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
            java.util.logging.Logger.getLogger(GroupCreate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GroupCreate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GroupCreate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GroupCreate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GroupCreate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createbtn;
    private javax.swing.JComboBox<String> createcombo;
    private com.toedter.calendar.JDateChooser createdatechooser;
    private javax.swing.JTextField createnametxt;
    private javax.swing.JTable grptable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveandexit;
    private javax.swing.JButton updatebtn;
    private javax.swing.JComboBox<String> updatecombo;
    private com.toedter.calendar.JDateChooser updatedatechooser;
    private javax.swing.JTextField updatenametxt;
    // End of variables declaration//GEN-END:variables
}
