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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class GroupStdAssign extends javax.swing.JFrame {
    
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel alltablemodel;
    TableModel singletablemodel;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag,flag1;
    
    /**
     * Creates new form StdnGrpAssign
     */
    public GroupStdAssign() {
        this.flag = 0;
        flag1=0;
        initComponents();
        allabledecoration();
        singletabledecoration();
        inittialization();
        flag=1;
        
        Date date= null;
        String state = "";
        date = datechooser.getDate();
        state = statecombo.getSelectedItem().toString();
        if(date != null){
            setgrp(date,state);
        }
        allidtxt.requestFocus();
        closeBtn();
    }
    
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    NewDashboard das = new NewDashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
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
        datechooser.setDate(todaysdate);  // setting both datechooser todays date
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) datechooser.getDateEditor();
        dtedit.setEditable(false);
        
        
        dec = new DecimalFormat("#0.00");
        alltablemodel =  alltable.getModel();
        singletablemodel =  singletable.getModel();
        
        
    }
    
    
    
    public void allabledecoration(){
        alltable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        alltable.getTableHeader().setOpaque(false);
        alltable.getTableHeader().setBackground(new Color(32,136,203));
        alltable.getTableHeader().setForeground(new Color(255,255,255));
        alltable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        //offtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        alltable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        alltable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        alltable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        alltable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        alltable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
    }
    
    
    public void singletabledecoration(){
        singletable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        singletable.getTableHeader().setOpaque(false);
        singletable.getTableHeader().setBackground(new Color(32,136,203));
        singletable.getTableHeader().setForeground(new Color(255,255,255));
        singletable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        singletable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        singletable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        singletable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        singletable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        singletable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
    }
    
    
    public String numtogrp(int dateserial,String state,int serial){
        String name="";
        //System.out.println(dateserial+" "+state+" "+serial);
        
        try{
            psmt = conn.prepareStatement("select name from grp where date=? and state= ? and serial=?");
            psmt.setInt(1, dateserial);
            psmt.setString(2, state.toLowerCase());
            psmt.setInt(3, serial);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString(1));
                name = rs.getString(1);
                //System.out.println(name);
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "failed to convert grp number to date", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return name;
    }
    
    
    public void updatesinglevalue(int selectedrow, Date date, int grpserial, String state){
        boolean check=false;
        int dateserial=0, hallid=0;
        String strhallid="",grpname="", updategrpname="";
        
        updategrpname = updategrpcombo.getSelectedItem().toString();
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in change all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //System.out.println(check+" "+dateserial+" "+grpserial);
        
            
            try{
                strhallid = singletablemodel.getValueAt(selectedrow, 1).toString();
                hallid = Integer.parseInt(strhallid);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "hall id parse error","parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        //System.out.println(grpserial+" "+dateserial+" "+hallid+" "+state);   
        if(state.equals("Breakfast")){
            //System.out.println(grpserial+" "+dateserial+" "+hallid);
            try{
            psmt = conn.prepareStatement("update mealsheet SET bfgrp=? WHERE date= ? and hallid=? ");
            psmt.setInt(1, grpserial);
            psmt.setInt(2, dateserial);
            psmt.setInt(3, hallid);
            psmt.execute();
            psmt.close();

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data updating errpr"
                        + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        else if(state.equals("Lunch")){
            //System.out.println(grpserial+" "+dateserial+" "+hallid);
            try{
            psmt = conn.prepareStatement("update mealsheet SET lunchgrp=? WHERE date= ? and hallid=? ");
            psmt.setInt(1, grpserial);
            psmt.setInt(2, dateserial);
            psmt.setInt(3, hallid);
            psmt.execute();
            psmt.close();

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data updating errpr"
                        + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        else if(state.equals("Dinner")){
            //System.out.println(grpserial+" "+dateserial+" "+hallid);
            try{
            psmt = conn.prepareStatement("update mealsheet SET dinnergrp=? WHERE date= ? and hallid=? ");
            psmt.setInt(1, grpserial);
            psmt.setInt(2, dateserial);
            psmt.setInt(3, hallid);
            psmt.execute();
            psmt.close();

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data updating errpr"
                        + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
            
            
            
        tablemodel = (DefaultTableModel) alltable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }

        tablemodel = (DefaultTableModel) singletable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        grpserial =-2;
        grpname = grpcombo.getSelectedItem().toString();
        grpserial = grpcombo.getSelectedIndex();
        //System.out.println(date+" "+state+" "+grpname+" "+grpserial);
        setsingletable(date,state,grpname,grpserial+1);
        setalltable(date, state, grpname, grpserial+1);
        
        //System.out.println("called "+hallid+" "+updategrpname);
        if(updategrpname.equals(grpname)){
            System.out.println("called "+hallid);
            searchsingletable(hallid);
        }
        else if( !updategrpname.equals(grpname)){
            System.out.println("called alltable "+hallid);
            searchalltable(hallid, false);
        }
        
    }
    
    
    public void setalltable(Date date, String state, String grpname, int grpserial){
        int dateserial=0, serial=1;
        String sql="",name="";
        tablemodel = (DefaultTableModel) alltable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(state.equals("Breakfast")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.bfgrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.bfgrp != ? and mealsheet.bfgrp != 0";
        }
        else if(state.equals("Lunch")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.lunchgrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.lunchgrp != ? and mealsheet.lunchgrp != 0";
        }
        else if(state.equals("Dinner")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.dinnergrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.dinnergrp != ? and mealsheet.dinnergrp != 0";
        }
        
        //System.out.println(grpserial+" "+state);
        try{
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, dateserial);
            psmt.setInt(2, grpserial);
            rs = psmt.executeQuery();
            //System.out.println("called");
            while(rs.next()){
                //System.out.print(rs.getInt(1));
                Object o [] = {false,serial,rs.getInt(1),rs.getString(2), rs.getString(3),rs.getInt(4)};
                tablemodel.addRow(o);
                serial++;
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to set all group table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        serial=0;
        //System.out.println("called"+alltable.getRowCount());
        for(int i=0; i<alltablemodel.getRowCount(); i++){
            serial = Integer.parseInt(alltablemodel.getValueAt(i, 5).toString());
            name = numtogrp(dateserial,state,serial);
            //System.out.println(serial);
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            alltablemodel.setValueAt(name, i, 5);
        }
        
        
        
    }
    
    public void setsingletable(Date date, String state, String grpname, int grpserial){
        int dateserial=0, serial=1;
        String sql="",name="";
        tablemodel = (DefaultTableModel) singletable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.println(state);
        if(state.equals("Breakfast")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.bfgrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.bfgrp=? and mealsheet.breakfast!=0";
        }
        else if(state.equals("Lunch")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.lunchgrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.lunchgrp=? and mealsheet.breakfast!=0";
        }
        else if(state.equals("Dinner")){
            sql="select stuinfo.hallid, stuinfo.name, stuinfo.roomno, mealsheet.dinnergrp from stuinfo JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date = ? and mealsheet.dinnergrp=? and mealsheet.breakfast!=0";
        }
        
        //System.out.println(sql+" "+grpserial);
        try{
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, dateserial);
            psmt.setInt(2, grpserial);
            rs = psmt.executeQuery();
            //System.out.println("called");
            while(rs.next()){
                //System.out.print(rs.getInt(1));
                Object o [] = {serial,rs.getInt(1),rs.getString(2), rs.getString(3),grpname};
                tablemodel.addRow(o);
                serial++;
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to set sigle group table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    
    
    
    
    public void setgrp(Date date, String state){
        int dateserial=0;
        String grpname="";
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.println(dateserial);
        grpcombo.removeAllItems();
        updategrpcombo.removeAllItems();
        try{
            psmt = conn.prepareStatement("select name from grp where date=? and state= ? order by serial");
            psmt.setInt(1, dateserial);
            psmt.setString(2, state.toLowerCase());
            rs = psmt.executeQuery();
            while(rs.next()){
                grpname = rs.getString(1);
                grpname = grpname.substring(0, 1).toUpperCase() + grpname.substring(1);
                grpcombo.addItem(grpname);
                updategrpcombo.addItem(grpname);
                //System.out.print("called"+" "+grpname);
                //System.out.println();
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count total bf lunch dinner", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.println("called setgrp\n");
        
    }
    
    
    public int searchhallid(String hallid){
        int id=0;
        try{
            psmt = conn.prepareStatement("select hallid from stuinfo where hallid=? or roll=?");
            psmt.setString(1, hallid);
            psmt.setString(2, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.print(rs.getInt(1));
                id = rs.getInt(1);
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count total bf lunch dinner", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        return id;
    }
    
    public void searchalltable(int hallid, boolean check){
        int id=0, flag=0,grpserial=-1, totalrow=-1;
        String state="";
        Date date=null;
        
        totalrow = alltablemodel.getRowCount();
        for(int i=0; i<totalrow; i++){
            id = Integer.parseInt(alltablemodel.getValueAt(i, 2).toString());
            if( id == hallid){
                alltable.requestFocus();
                alltable.changeSelection(i,0,false, false);
                flag=1;
                if(check){
                    date = datechooser.getDate();
                    grpserial = grpcombo.getSelectedIndex();
                    state = statecombo.getSelectedItem().toString();
                    alltablemodel.setValueAt(true, i, 0);
                    changealltable(i,date,grpserial+1,state);
                }
                break;
            }
        }
        if(flag == 0){
            JOptionPane.showMessageDialog(null, "Id "+hallid+" does "
                    + "not found in All table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    public void searchsingletable(int hallid){
        int id=0, flag=0,grpserial=-1, totalrow=-1;
        String state="";
        Date date=null;
        
        totalrow = singletablemodel.getRowCount();
        for(int i=0; i<totalrow; i++){
            id = Integer.parseInt(singletablemodel.getValueAt(i, 1).toString());
            if( id == hallid){
                singletable.requestFocus();
                singletable.changeSelection(i,0,false, false);
                flag=1;
                break;
            }
        }
        if(flag == 0){
            JOptionPane.showMessageDialog(null, "Id "+hallid+" does "
                    + "not found in Selected Item table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    
    public void changealltable(int selectedrow, Date date, int grpserial, String state){
        boolean check=false;
        int dateserial=0, hallid=0;
        String strhallid="",grpname="";
        
        try{
            check = Boolean.parseBoolean(alltablemodel.getValueAt(selectedrow,0).toString());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Boolean parsing error","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in change all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //System.out.println(check+" "+dateserial+" "+grpserial);
        if(check){
            
            try{
                strhallid = alltablemodel.getValueAt(selectedrow, 2).toString();
                hallid = Integer.parseInt(strhallid);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "hall id parse error","parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(state.equals("Breakfast")){
                //System.out.println(grpserial+" "+dateserial+" "+hallid);
                try{
                psmt = conn.prepareStatement("update mealsheet SET bfgrp=? WHERE date= ? and hallid=? ");
                psmt.setInt(1, grpserial);
                psmt.setInt(2, dateserial);
                psmt.setInt(3, hallid);
                psmt.execute();
                psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(state.equals("Lunch")){
                //System.out.println(grpserial+" "+dateserial+" "+hallid);
                try{
                psmt = conn.prepareStatement("update mealsheet SET lunchgrp=? WHERE date= ? and hallid=? ");
                psmt.setInt(1, grpserial);
                psmt.setInt(2, dateserial);
                psmt.setInt(3, hallid);
                psmt.execute();
                psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            else if(state.equals("Dinner")){
                //System.out.println(grpserial+" "+dateserial+" "+hallid);
                try{
                psmt = conn.prepareStatement("update mealsheet SET dinnergrp=? WHERE date= ? and hallid=? ");
                psmt.setInt(1, grpserial);
                psmt.setInt(2, dateserial);
                psmt.setInt(3, hallid);
                psmt.execute();
                psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            
            tablemodel = (DefaultTableModel) alltable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            tablemodel = (DefaultTableModel) singletable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            grpname = grpcombo.getSelectedItem().toString();
            ///System.out.println(date+" "+state+" "+grpname+" "+grpserial);
            setsingletable(date,state,grpname,grpserial);
            setalltable(date, state, grpname, grpserial);
            searchsingletable(hallid);
        }
        
    }
    
    public void setupdategrp(int selectedrow){
        Object grpname="";
        grpname = singletablemodel.getValueAt(selectedrow, 4);
        //System.out.println(selectedrow);
        updategrpcombo.setSelectedItem(grpname);
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
        datechooser = new com.toedter.calendar.JDateChooser();
        checkbx = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        singleidtxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        statecombo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        grpcombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        allidtxt = new javax.swing.JTextField();
        allsearchbtn = new javax.swing.JButton();
        singlesearchbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        singletable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        alltable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        updategrpcombo = new javax.swing.JComboBox<>();
        updatebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/clients.png"))); // NOI18N
        jLabel1.setText("STUDENT GROUPING");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/calendar.png"))); // NOI18N
        jLabel2.setText("Date");

        datechooser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        datechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                datechooserPropertyChange(evt);
            }
        });

        checkbx.setBackground(new java.awt.Color(208, 227, 229));
        checkbx.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        checkbx.setText("Auto");
        checkbx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkbxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel3.setText("Hall Id");

        singleidtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        singleidtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleidtxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel4.setText("State");

        statecombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        statecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        statecombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statecomboActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/add (1).png"))); // NOI18N
        jLabel5.setText("Group");

        grpcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        grpcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grpcomboActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel6.setText("Hall Id");

        allidtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        allidtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allidtxtActionPerformed(evt);
            }
        });

        allsearchbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        allsearchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        allsearchbtn.setText("Search");
        allsearchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allsearchbtnActionPerformed(evt);
            }
        });

        singlesearchbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        singlesearchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        singlesearchbtn.setText("Search");
        singlesearchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singlesearchbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(datechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(singleidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(93, 93, 93)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(grpcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 369, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(singlesearchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(checkbx)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(allidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(allsearchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(statecombo)
                            .addComponent(grpcombo)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(allsearchbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(allidtxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(singleidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(singlesearchbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkbx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        singletable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Hall Id", "Name", "Room", "Group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        singletable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        singletable.setRowHeight(25);
        singletable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        singletable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        singletable.setShowVerticalLines(false);
        singletable.getTableHeader().setReorderingAllowed(false);
        singletable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                singletableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(singletable);
        if (singletable.getColumnModel().getColumnCount() > 0) {
            singletable.getColumnModel().getColumn(2).setMinWidth(150);
            singletable.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        alltable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Sl", "Hall Idl", "Name", "Room", "Group"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        alltable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        alltable.setRowHeight(25);
        alltable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        alltable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        alltable.setShowVerticalLines(false);
        alltable.getTableHeader().setReorderingAllowed(false);
        alltable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                alltableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(alltable);
        if (alltable.getColumnModel().getColumnCount() > 0) {
            alltable.getColumnModel().getColumn(3).setMinWidth(150);
            alltable.getColumnModel().getColumn(3).setMaxWidth(200);
        }

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/add (1).png"))); // NOI18N
        jLabel7.setText("Group");

        updategrpcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update meal.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updategrpcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(updategrpcombo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void datechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_datechooserPropertyChange
        // TODO add your handling code here:
        Date date= null;
        String state = "",grpname="";
        int grpserial=-1;
        
        date = datechooser.getDate();
        state = statecombo.getSelectedItem().toString();
        
        if(date != null && flag ==1){
            flag1=0;
            setgrp(date,state);
            
            tablemodel = (DefaultTableModel) singletable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            tablemodel = (DefaultTableModel) alltable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            if(grpcombo.getItemCount() <= 0){
                //System.out.println("called");
//                JOptionPane.showMessageDialog(null, "No group exist on "+
//                        formatter1.format(date), "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            flag1=1;
            
            grpname = grpcombo.getSelectedItem().toString();
            grpserial = grpcombo.getSelectedIndex();
            setalltable(date,state,grpname,grpserial+1);
            
           
            setsingletable(date,state,grpname,grpserial+1);
            singleidtxt.requestFocus();
        }
    }//GEN-LAST:event_datechooserPropertyChange

    private void statecomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statecomboActionPerformed
        // TODO add your handling code here:
        Date date= null;
        String state = "",grpname="";
        int grpserial=-1;
        
        date = datechooser.getDate();
        state = statecombo.getSelectedItem().toString();
        if(date != null && flag ==1){
            flag1=0;
            setgrp(date,state);
            
            tablemodel = (DefaultTableModel) alltable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            
            tablemodel = (DefaultTableModel) singletable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            if(grpcombo.getItemCount() <= 0){
                //System.out.println("called");
                return;
            }
            flag1=1;
        
            grpname = grpcombo.getSelectedItem().toString();
            grpserial = grpcombo.getSelectedIndex();
            setalltable(date,state,grpname,grpserial+1);
            
            
            
            setsingletable(date,state,grpname,grpserial+1);
            singleidtxt.requestFocus();
        }
    }//GEN-LAST:event_statecomboActionPerformed

    private void grpcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grpcomboActionPerformed
        // TODO add your handling code here:
        Date date= null;
        String state = "",grpname="";
        
        
        int grpserial=-1;
        
        date = datechooser.getDate();
        state = statecombo.getSelectedItem().toString();
        //System.out.println(date+" "+flag+" "+flag1);
        if(date != null && flag ==1 && flag1 == 1){
            //System.out.println("called grp combo");
            tablemodel = (DefaultTableModel) alltable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            grpname = grpcombo.getSelectedItem().toString();
            grpserial = grpcombo.getSelectedIndex();
            setalltable(date,state,grpname,grpserial+1);
            
            tablemodel = (DefaultTableModel) singletable.getModel();
            if(tablemodel.getColumnCount() > 0){
                tablemodel.setRowCount(0);
            }
            
            setsingletable(date,state,grpname,grpserial+1);
            allidtxt.requestFocus();
        }
    }//GEN-LAST:event_grpcomboActionPerformed

    private void alltableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_alltableMouseClicked
        // TODO add your handling code here:
         //changealltable
         singletable.clearSelection();
         
         Date date=null;
        int grpserial =0, selectedrow =-1;
        String state="";
        date = datechooser.getDate();
        selectedrow = alltable.getSelectedRow();
        grpserial = grpcombo.getSelectedIndex();
        state = statecombo.getSelectedItem().toString();
        
        if( date!= null && selectedrow >=0){
            changealltable(selectedrow,date, grpserial+1,state);
        }
        else{
            JOptionPane.showMessageDialog(null, "No date is selected","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_alltableMouseClicked

    private void allsearchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allsearchbtnActionPerformed
        // TODO add your handling code here:
        String strid="";
        int id=0;
        boolean check=false;
        
        strid = allidtxt.getText().trim();
        if(strid != null){
            id = searchhallid(strid);
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter a Valid Id","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(id != 0){
            check = checkbx.isSelected();
            searchalltable(id,check);
        }
        else{
            JOptionPane.showMessageDialog(null, "Id does not exist in All table","Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_allsearchbtnActionPerformed

    private void singletableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_singletableMouseClicked
        // TODO add your handling code here:
        
        int selectedrow =-1;
        selectedrow = singletable.getSelectedRow();
        if(selectedrow >= 0){
            alltable.clearSelection();
            setupdategrp(selectedrow);
        }
        
    }//GEN-LAST:event_singletableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow=-1, grpserial=-1;
        Date date=null;
        String state="";
        
        date = datechooser.getDate();
        selectedrow = singletable.getSelectedRow();
        grpserial = updategrpcombo.getSelectedIndex();
        state = statecombo.getSelectedItem().toString();
        if(selectedrow >= 0){
            updatesinglevalue(selectedrow,date,grpserial+1,state);
            singleidtxt.requestFocus();
        }
        else{
            JOptionPane.showMessageDialog(null, "No row is selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updatebtnActionPerformed

    private void singlesearchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singlesearchbtnActionPerformed
        // TODO add your handling code here:
        //searchsingletable
        String strid="";
        int id=0;
        boolean check=false;
        
        strid = singleidtxt.getText().trim();
        if(strid != null){
            id = searchhallid(strid);
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter a Valid Id","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(id != 0){
            searchsingletable(id);
        }
        else{
            JOptionPane.showMessageDialog(null, "Id does not exist in Single table","Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_singlesearchbtnActionPerformed

    private void checkbxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbxActionPerformed
        // TODO add your handling code here:
        allidtxt.requestFocus();
    }//GEN-LAST:event_checkbxActionPerformed

    private void allidtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allidtxtActionPerformed
        // TODO add your handling code here:
        allsearchbtn.doClick();
    }//GEN-LAST:event_allidtxtActionPerformed

    private void singleidtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleidtxtActionPerformed
        // TODO add your handling code here:
        singlesearchbtn.doClick();
    }//GEN-LAST:event_singleidtxtActionPerformed

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
            java.util.logging.Logger.getLogger(GroupStdAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GroupStdAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GroupStdAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GroupStdAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GroupStdAssign().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField allidtxt;
    private javax.swing.JButton allsearchbtn;
    private javax.swing.JTable alltable;
    private javax.swing.JCheckBox checkbx;
    private com.toedter.calendar.JDateChooser datechooser;
    private javax.swing.JComboBox<String> grpcombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField singleidtxt;
    private javax.swing.JButton singlesearchbtn;
    private javax.swing.JTable singletable;
    private javax.swing.JComboBox<String> statecombo;
    private javax.swing.JButton updatebtn;
    private javax.swing.JComboBox<String> updategrpcombo;
    // End of variables declaration//GEN-END:variables
}
