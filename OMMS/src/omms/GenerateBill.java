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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class GenerateBill extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;

    /**
     * Creates new form GenerateBill
     */
    public GenerateBill() {
        initComponents();
        billtabledecoration();
        inittialization();
        flag=1;
    }
    
    
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        
        dec = new DecimalFormat("#0");
        model = billtbl.getModel();
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yeartxt.setText(Integer.toString(year));
        
        int month = Calendar.getInstance().get(Calendar.MONTH);
        //System.out.println(month+" "+year);
        monthcombo.setSelectedIndex(month);
        setgeneratedate();
        
        savebtnvisibility(month+1, year);
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
    
    
    public void savebtnvisibility(int month,int year){
        int permission=-1, generated=-1;
        try{
            //.out.println(month+" called "+year);
            psmt = conn.prepareStatement("select permission,generated from billpermission where month=? and year=?");
            psmt.setInt(1, month);
            psmt.setInt(2, year);
            rs = psmt.executeQuery();
            
            while(rs.next()){
                permission=rs.getInt(1);
                generated = rs.getInt(2);
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to check permission", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.println(permission+" "+generated+" "+month+" "+year);
        if(permission ==1 && generated == 0){
            savebtn.setVisible(true);
        }
        else{
            savebtn.setVisible(false);
        }
    }
    
    public void setgeneratedate(){
        String strfrom="", strto="", stryear="",strmonth="";
        int month=0, fromserial=0, toserial=0;
        Date fromdate=null, todate=null;
        
        try{
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select fromdate,todate from mealrange where sl =1");
            rs = psmt.executeQuery();
            
            while(rs.next()){
                strfrom= rs.getString(1);
                strto = rs.getString(2);
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetchname of stored item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        stryear = yeartxt.getText();
        //strmonth = Integer.toString(monthcombo.getSelectedIndex());
        month = monthcombo.getSelectedIndex();
        if(month <=9){
            strmonth = Integer.toString(month);
            strmonth = "0"+strmonth;
        }
        else{
            strmonth = Integer.toString(month);
        }
        strfrom=stryear+strmonth+strfrom;
        //System.out.println("from date "+ strfrom);
        month=month+1;
        if(month <=9){
            strmonth = Integer.toString(month);
            strmonth = "0"+strmonth;
        }
        else{
            strmonth = Integer.toString(month);
        }
        strto=stryear+strmonth+strto;
        //System.out.println("to date "+ strto);
        
        try{
            fromdate = formatter.parse(strfrom);
            todate = formatter.parse(strto);
            strfrom = formatter1.format(fromdate);
            strto =formatter1.format(todate);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Failed to set date", "Date set error", JOptionPane.ERROR_MESSAGE);
            return;
        }

          fromdatelbl.setText(strfrom);
          todatelbl.setText(strto);
    }
    
    
    
    
    public void billtabledecoration(){
        billtbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        billtbl.getTableHeader().setOpaque(false);
        billtbl.getTableHeader().setBackground(new Color(32,136,203));
        billtbl.getTableHeader().setForeground(new Color(255,255,255));
        billtbl.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        billtbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(10).setCellRenderer(centerRender);
        billtbl.getColumnModel().getColumn(11).setCellRenderer(centerRender);
        
    }
    
    
    public void calculatebill(int fromdate, int todate){
        SingleStdntBill st= new SingleStdntBill();
        DailyAvgBill db= new DailyAvgBill();
        Map<Integer, BillAmount> billmap = new HashMap<>();
        tablemodel = (DefaultTableModel) billtbl.getModel();
        
        Double bill=0.00, tmpbill=0.0, prevbill=0.0,totalbill=0.0;
        int hallid=0,serial=1;
        String roll="",name="",room="",strtotal="";
        
        billmap = db.setbill(fromdate, todate);
        
//        //System.out.println(bill);
        try{
            psmt = conn.prepareStatement("select hallid,roll,name,roomno,totaldue from stuinfo ORDER by hallid");
            rs = psmt.executeQuery();
            while(rs.next()){
                hallid = rs.getInt(1);
                roll = rs.getString(2);
                name = rs.getString(3);
                room = rs.getString(4);
                prevbill = rs.getDouble(5);
                
                bill =st.monthlybill(billmap, fromdate, todate, hallid);
                tmpbill = st.monthlytmpfoodbill(fromdate, todate,hallid);
                bill = bill+tmpbill;
                bill = Math.ceil(bill);
                //prevbill = st.previousbill(hallid);
                
                if(prevbill > 0){
                    
                    totalbill = bill+prevbill;
                    Object o []={serial,hallid,roll,name, room,dec.format(bill),
                    0,0,0,prevbill,0,dec.format(totalbill)};
                    tablemodel.addRow(o);
                }
                else if(prevbill < 0){
                    prevbill =prevbill* -1;
                    totalbill = bill-prevbill;
                    if(totalbill <0){
                        totalbill*=-1;
                        strtotal = dec.format(totalbill)+"(Extra)";
                        Object o []={serial,hallid,roll,name, room, dec.format(bill),
                        0,0,0,0.0,prevbill,strtotal};
                        tablemodel.addRow(o);
                    }
                    else{
                        Object o []={serial,hallid,roll,name, room, dec.format(bill),
                        0,0,0,0.0,prevbill,dec.format(totalbill)};
                        tablemodel.addRow(o);
                    }
                }
                else{
                    Object o []={serial,hallid,roll,name, room, dec.format(bill),
                    0,0,0,0.0,0.0,dec.format(bill)};
                    tablemodel.addRow(o);
                }
                serial++;
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "bill calcualtion for each student failed", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void calculatetotal(){
        Double totalmessbill=0.00, totalothers=0.00, totalfine=0.00, totalwaive=0.00, grandtotal=0.00;
        int totalrow =-1; 
        totalrow = billtbl.getRowCount();
        
        for(int i=0; i<totalrow; i++){
            totalmessbill += Double.parseDouble(model.getValueAt(i, 5).toString());
            totalothers += Double.parseDouble(model.getValueAt(i, 6).toString());
            totalfine += Double.parseDouble(model.getValueAt(i, 7).toString());
            totalwaive += Double.parseDouble(model.getValueAt(i, 8).toString());
        }
        grandtotal = totalmessbill+totalothers+totalfine-totalwaive;
        
        messbilllbl.setText(dec.format(totalmessbill));
        othersbilltxt.setText(dec.format(totalothers));
        finelbl.setText(dec.format(totalfine));
        waivelbl.setText(dec.format(totalwaive));
        grandtotallbl.setText(dec.format(grandtotal));
    }
    
    public void clearfield(){
        namelbl.setText("");
        roomlbl.setText("");
        billbl.setText("");
        otherstxt.setText("");
        finetxt.setText("");
        waivetxt.setText("");
        duelbl.setText("");
        advancelbl.setText("");
        totallbl.setText("");
        idtxt.setText("");
        billtbl.clearSelection();
    }
    
    public boolean searchtable(String id){
        int totalrow=-1;
        
        totalrow = billtbl.getRowCount();
        for(int i=0; i<totalrow; i++){
            if(model.getValueAt(i, 1).toString().equals(id)){
                billtbl.requestFocus();
                billtbl.changeSelection(i,0,false, false);
                setvalue();
                return true;
            }
            else if(model.getValueAt(i, 2).toString().equals(id)){
                billtbl.requestFocus();
                billtbl.changeSelection(i,0,false, false);
                setvalue();
                return true;
            }
        }
        return false;
    }
    
    public void setupdatevalue(){
        //System.out.println("called");
        String strothers="", strfine="", strwaive="",strtotal="";
        Double others=0.0, fine=0.0 , waive=0.0, total=0.0, meal=0.0, previous=0.0, advance=0.0;
        int selectedrow=-1;
        
        strothers = otherstxt.getText().trim();
        strfine = finetxt.getText().trim();
        strwaive = waivetxt.getText().trim();
        try{
            others = Double.parseDouble(strothers);
            fine = Double.parseDouble(strfine);
            waive = Double.parseDouble(strwaive);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "value format error", "Update error", JOptionPane.ERROR_MESSAGE);
            otherstxt.requestFocus();
            return;
        }
        
        selectedrow = billtbl.getSelectedRow();
        
        if(selectedrow >= 0 && others >=0  && fine >= 0 && waive >=0){
            model.setValueAt(strothers, selectedrow, 6);
            model.setValueAt(strfine, selectedrow, 7);
            model.setValueAt(strwaive, selectedrow, 8);
            
            meal = Double.parseDouble(model.getValueAt(selectedrow, 5).toString());
            previous = Double.parseDouble(model.getValueAt(selectedrow, 9).toString());
            advance = Double.parseDouble(model.getValueAt(selectedrow, 10).toString());
            
            total = meal+others+fine-waive+previous-advance;
            if(total < 0){
                total *=-1;
                strtotal = dec.format(total)+"(Extra)";
                model.setValueAt(strtotal, selectedrow, 11);
            }
            else{
                model.setValueAt(dec.format(total), selectedrow, 11);
            }
            calculatetotal();
            billtbl.clearSelection();
            clearfield();
            idtxt.requestFocus();
        }
        else if(selectedrow <0){
            JOptionPane.showMessageDialog(null, "No row is selected", "Update error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter valid amount", "Update error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    public void setallothers(){
        int totalrow=-1, selectedrow=-1;
        String strothers="", strfine="", strwaive="", strtotal="";
        Double others=0.0, fine=0.0, waive=0.0, meal=0.0, previous=0.0, advance=0.0,total=0.0;
        
        strothers = otherstxt.getText().trim();
        try{
            others = Double.parseDouble(strothers);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "value format error", "Update error", JOptionPane.ERROR_MESSAGE);
            otherstxt.requestFocus();
            return;
        }
        if(others < 0){
            JOptionPane.showMessageDialog(null, "Enter amount greater than zero", "Update error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        totalrow = billtbl.getRowCount();
        for(int i=0; i<totalrow; i++){
            billtbl.setValueAt(others, i, 6);
            meal = Double.parseDouble(model.getValueAt(i, 5).toString());
            fine = Double.parseDouble(model.getValueAt(i, 7).toString());
            waive = Double.parseDouble(model.getValueAt(i, 8).toString());
            previous = Double.parseDouble(model.getValueAt(i, 9).toString());
            advance = Double.parseDouble(model.getValueAt(i, 10).toString());
            total = meal+others+fine-waive+previous-advance;
            if(total < 0){
                total *=-1;
                strtotal = dec.format(total)+"(Extra)";
                billtbl.setValueAt(strtotal, i, 11);
            }
            else{
                billtbl.setValueAt(dec.format(total), i, 11);
            }
        }
        
        selectedrow = billtbl.getSelectedRow();
        //System.out.println(selectedrow);
        if(selectedrow >=0){
            strfine = finetxt.getText().trim();
            strwaive = waivetxt.getText().trim();
            
            
            try{
                fine = Double.parseDouble(strfine);
                waive = Double.parseDouble(strwaive);
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "value format error", "Update error", JOptionPane.ERROR_MESSAGE);
                otherstxt.requestFocus();
                return;
            }
            
            if(fine <0 || waive <0){
                JOptionPane.showMessageDialog(null, "Enter amount greater than zero", "Update error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            model.setValueAt(strfine, selectedrow, 7);
            model.setValueAt(strwaive, selectedrow, 8);
            meal = Double.parseDouble(model.getValueAt(selectedrow, 5).toString());
            previous = Double.parseDouble(model.getValueAt(selectedrow, 9).toString());
            advance = Double.parseDouble(model.getValueAt(selectedrow, 10).toString());
            
            total = meal+others+fine-waive+previous-advance;
            if(total < 0){
                total *=-1;
                strtotal = dec.format(total)+"(Extra)";
                model.setValueAt(strtotal, selectedrow, 11);
            }
            else{
                model.setValueAt(dec.format(total), selectedrow, 11);
            }
            
        }
        calculatetotal();
        billtbl.clearSelection();
        clearfield();
        idtxt.requestFocus();
    }
    
    public void setvalue(){
        
        int selectedrow=-1;
        String name="",previous="", room="", advance="", others="",fine="",waive="", bill="", total="";
        
        selectedrow = billtbl.getSelectedRow();
        if(selectedrow >=0){
            name = model.getValueAt(selectedrow, 3).toString();
            room = model.getValueAt(selectedrow, 4).toString();
            bill = model.getValueAt(selectedrow, 5).toString();
            others = model.getValueAt(selectedrow, 6).toString();
            fine = model.getValueAt(selectedrow, 7).toString();
            waive = model.getValueAt(selectedrow, 8).toString();
            previous = model.getValueAt(selectedrow, 9).toString();
            advance = model.getValueAt(selectedrow, 10).toString();
            total = model.getValueAt(selectedrow, 11).toString();
            
            namelbl.setText(name);
            roomlbl.setText(room);
            billbl.setText(bill);
            otherstxt.setText(others);
            finetxt.setText(fine);
            waivetxt.setText(waive);
            duelbl.setText(previous);
            advancelbl.setText(advance);
            totallbl.setText(total);
        }
        else{
            JOptionPane.showMessageDialog(null, "No Row is selected", "Update error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void savedatabase(int totalrow, int year, int month){
        Double bill=0.0, others=0.0, fine=0.0, waive=0.0,advance=0.0, due=0.0, totaldue=0.0;
        int hallid=0;
        
        
        try{
            psmt = conn.prepareStatement("update billpermission set generated=? where month=? and year=?");
            psmt.setInt(1, 1);
            psmt.setInt(2, month);
            psmt.setInt(3, year);
            psmt.execute();
            psmt.close();

        }  
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error in permission table", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        
        for(int i=0; i<totalrow; i++){
            hallid =Integer.parseInt(model.getValueAt(i, 1).toString());
            bill = Double.parseDouble(model.getValueAt(i, 5).toString());
            others = Double.parseDouble(model.getValueAt(i, 6).toString());
            fine = Double.parseDouble(model.getValueAt(i, 7).toString());
            waive = Double.parseDouble(model.getValueAt(i, 8).toString());
            advance = Double.parseDouble(model.getValueAt(i, 10).toString());
            due = Double.parseDouble(model.getValueAt(i, 9).toString());
            
            if(advance > 0){
                totaldue = -1*advance;
            }
            else if(due > 0){
                totaldue=due;
            }
            else{
                totaldue=0.0;
            }
            
            
            
            
            
            try{
                    psmt = conn.prepareStatement("insert into billhistory (hallid, year, month,bill,others,fine,waive,due) VALUES(?,?,?,?,?,?,?,?)");
                    psmt.setInt(1, hallid);
                    psmt.setInt(2, year);
                    psmt.setInt(3, month);
                    psmt.setDouble(4, bill);
                    psmt.setDouble(5, others);
                    psmt.setDouble(6, fine);
                    psmt.setDouble(7, waive);
                    psmt.setDouble(8, totaldue);

                    psmt.execute();
                    psmt.close();
                
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            totaldue = bill+others+fine-waive+due-advance;
            
            try{
                psmt = conn.prepareStatement("update stuinfo SET totaldue=? where hallid=? ");
                psmt.setDouble(1, totaldue);
                psmt.setInt(2, hallid);
                psmt.execute();
                psmt.close();
                
            }  
            catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error in save and exit", JOptionPane.ERROR_MESSAGE);
                return;
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
        jLabel3 = new javax.swing.JLabel();
        generatebtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        monthcombo = new javax.swing.JComboBox<>();
        yeartxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        namelbl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        billbl = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        duelbl = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        roomlbl = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        advancelbl = new javax.swing.JLabel();
        otherscheck = new javax.swing.JCheckBox();
        otherstxt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        finetxt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        totallbl = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        messbilllbl = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        othersbilltxt = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        finelbl = new javax.swing.JLabel();
        updatebtn = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        waivelbl = new javax.swing.JLabel();
        datelbl = new javax.swing.JLabel();
        idtxt = new javax.swing.JTextField();
        savebtn = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        searchbtn = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        waivetxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        grandtotallbl = new javax.swing.JLabel();
        fromdatelbl = new javax.swing.JLabel();
        todatelbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billtbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/point-of-sale.png"))); // NOI18N
        jLabel1.setText("Generate Bill");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/start.png"))); // NOI18N
        jLabel2.setText("From Date");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/end.png"))); // NOI18N
        jLabel3.setText("To Date");

        generatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        generatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/pos-terminal (1).png"))); // NOI18N
        generatebtn.setText("Generate");
        generatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatebtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthly.png"))); // NOI18N
        jLabel4.setText("Month");

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/year.png"))); // NOI18N
        jLabel5.setText("Year ");

        monthcombo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        monthcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July ", "August", "September", "October", "November", "December" }));
        monthcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthcomboActionPerformed(evt);
            }
        });

        yeartxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        yeartxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yeartxtActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Name");

        namelbl.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Bill");

        billbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Previous Due");

        duelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Room ");

        roomlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        roomlbl.setText("0");

        jLabel14.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Advance ");

        advancelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        advancelbl.setText("0");

        otherscheck.setBackground(new java.awt.Color(208, 227, 229));
        otherscheck.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        otherscheck.setText("Others");

        otherstxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        otherstxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherstxtActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Fine");

        finetxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        finetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finetxtActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Total ");

        totallbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totallbl.setText("0");

        jLabel19.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 51, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Total Mess Bill");

        messbilllbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        messbilllbl.setForeground(new java.awt.Color(255, 51, 0));
        messbilllbl.setText("0");

        jLabel21.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 51, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Total Others Bill");

        othersbilltxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        othersbilltxt.setForeground(new java.awt.Color(255, 51, 0));
        othersbilltxt.setText("0");

        jLabel23.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 51, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Total Fine");

        finelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        finelbl.setForeground(new java.awt.Color(255, 51, 0));
        finelbl.setText("0");

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update meal.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 51, 0));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Total Waive");

        waivelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        waivelbl.setForeground(new java.awt.Color(255, 51, 0));
        waivelbl.setText("0");

        datelbl.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        datelbl.setForeground(new java.awt.Color(255, 51, 0));
        datelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datelbl.setText("February,2020");

        idtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtxtActionPerformed(evt);
            }
        });

        savebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        savebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/uploading.png"))); // NOI18N
        savebtn.setText("Save & Exit");
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel28.setText("Roll/ Hall Id");

        searchbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        searchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        searchbtn.setText("Search");
        searchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbtnActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Waive");

        waivetxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        waivetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                waivetxtActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Grand Total");

        grandtotallbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        grandtotallbl.setForeground(new java.awt.Color(255, 51, 0));
        grandtotallbl.setText("0");

        fromdatelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fromdatelbl.setForeground(new java.awt.Color(255, 51, 0));

        todatelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        todatelbl.setForeground(new java.awt.Color(255, 51, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(todatelbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fromdatelbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yeartxt, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(monthcombo, javax.swing.GroupLayout.Alignment.LEADING, 0, 150, Short.MAX_VALUE)
                        .addComponent(datelbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(namelbl, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addComponent(billbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(duelbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(roomlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(otherscheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updatebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(advancelbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(otherstxt)
                    .addComponent(finetxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totallbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(waivetxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(waivelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messbilllbl, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(othersbilltxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(finelbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(savebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grandtotallbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(185, 185, 185))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(datelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(messbilllbl, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(monthcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(advancelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(yeartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(billbl, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(otherstxt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(otherscheck, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(othersbilltxt, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(finelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(duelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(finetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fromdatelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(waivelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(roomlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(todatelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(waivetxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(idtxt, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                                    .addComponent(totallbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(grandtotallbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(savebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        billtbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        billtbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Hall ID", "Roll", "Name", "Room", "Meal Charge", "Others", "Fine", "Waive", "Previous Due", "Advance", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billtbl.setRowHeight(26);
        billtbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        billtbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
        billtbl.setShowVerticalLines(false);
        billtbl.getTableHeader().setReorderingAllowed(false);
        billtbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billtblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(billtbl);
        if (billtbl.getColumnModel().getColumnCount() > 0) {
            billtbl.getColumnModel().getColumn(0).setMaxWidth(50);
            billtbl.getColumnModel().getColumn(3).setMinWidth(200);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void generatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatebtnActionPerformed
        // TODO add your handling code here:
        Date from=null, to=null;
        String strfrom="", strto="";
        int fromserial =0, toserial=0;
        strfrom = fromdatelbl.getText();
        strto = todatelbl.getText();
        
        try {
            from = formatter1.parse(strfrom);
            to =  formatter1.parse(strto);
        } catch (ParseException ex) {
            Logger.getLogger(GenerateBill.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        
        
        try{
            
            fromserial = Integer.parseInt(formatter.format(from));
            toserial = Integer.parseInt(formatter.format(to));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Failed to convert date", "Data convertion error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tablemodel = (DefaultTableModel) billtbl.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        //System.out.println(fromserial+" "+toserial);
        if(fromserial <= toserial){
            calculatebill(fromserial,toserial);
            calculatetotal();
            idtxt.requestFocus();
        }
        else{
            JOptionPane.showMessageDialog(null, "From date must be smaller than to date", "Data convertion error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_generatebtnActionPerformed

    private void monthcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthcomboActionPerformed
        // TODO add your handling code here:
        
        String name="",year="";
        int month=0, yearval=0;
        
        name= monthcombo.getSelectedItem().toString();
        year = yeartxt.getText();
        //System.out.println(name);
        name=name+", "+year;
        datelbl.setText(name);
        setgeneratedate();
        
        try {
            yearval = Integer.parseInt(year);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Year format error", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(flag ==1 && yearval >0){
            month = monthcombo.getSelectedIndex()+1;
            savebtnvisibility(month, yearval);
        }
        else if(flag==1){
            JOptionPane.showMessageDialog(null, "Invalid year", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_monthcomboActionPerformed

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed
        // TODO add your handling code here:
        String id="";
        boolean found=false;
        id = idtxt.getText().trim();
        found = searchtable(id);
        
        if(!found){
            JOptionPane.showMessageDialog(null,id+ " does not exist", "Search Error", JOptionPane.ERROR_MESSAGE);
            clearfield();
            idtxt.requestFocus();
            return;
        }
        otherstxt.requestFocus();
    }//GEN-LAST:event_searchbtnActionPerformed

    private void billtblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billtblMouseClicked
        // TODO add your handling code here:
        setvalue();
        otherstxt.requestFocus();
    }//GEN-LAST:event_billtblMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        boolean check=false;
        check = otherscheck.isSelected();
        if(!check){
            setupdatevalue();
        }
        else{
            setallothers();
        }
        
    }//GEN-LAST:event_updatebtnActionPerformed

    private void otherstxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherstxtActionPerformed
        // TODO add your handling code here:
        finetxt.requestFocus();
    }//GEN-LAST:event_otherstxtActionPerformed

    private void finetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finetxtActionPerformed
        // TODO add your handling code here:
        waivetxt.requestFocus();
    }//GEN-LAST:event_finetxtActionPerformed

    private void waivetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_waivetxtActionPerformed
        // TODO add your handling code here:
        updatebtn.doClick();
    }//GEN-LAST:event_waivetxtActionPerformed

    private void idtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtxtActionPerformed
        // TODO add your handling code here:
        searchbtn.doClick();
    }//GEN-LAST:event_idtxtActionPerformed

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        // TODO add your handling code here:
        String stryear="";
        int totalrow=-1,year=0,month=0;
        totalrow = billtbl.getRowCount();
        stryear = yeartxt.getText().trim();
        month= monthcombo.getSelectedIndex()+1;
        try{
            year = Integer.parseInt(stryear);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"year format error", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(year >0){
            System.out.println(month);
            savedatabase(totalrow,year,month);
            savebtnvisibility(month, year);
        }
        else{
            JOptionPane.showMessageDialog(null,"year not valid", "Search Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_savebtnActionPerformed

    private void yeartxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yeartxtActionPerformed
        // TODO add your handling code here:
        String name="",year="",stryear="";
        int month=0,yearval=0;
        
        name= monthcombo.getSelectedItem().toString();
        year = yeartxt.getText();
        //System.out.println(name);
        name=name+", "+year;
        datelbl.setText(name);
        
        
        stryear = yeartxt.getText().trim();
        month= monthcombo.getSelectedIndex()+1;
        
        try{
            yearval = Integer.parseInt(stryear);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"year format error", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(yearval >0){
            //System.out.println(month);
            setgeneratedate();
            savebtnvisibility(month, yearval);
        }
        else{
            JOptionPane.showMessageDialog(null,"year not valid", "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_yeartxtActionPerformed

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
            java.util.logging.Logger.getLogger(GenerateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GenerateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GenerateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GenerateBill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advancelbl;
    private javax.swing.JLabel billbl;
    private javax.swing.JTable billtbl;
    private javax.swing.JLabel datelbl;
    private javax.swing.JLabel duelbl;
    private javax.swing.JLabel finelbl;
    private javax.swing.JTextField finetxt;
    private javax.swing.JLabel fromdatelbl;
    private javax.swing.JButton generatebtn;
    private javax.swing.JLabel grandtotallbl;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel messbilllbl;
    private javax.swing.JComboBox<String> monthcombo;
    private javax.swing.JLabel namelbl;
    private javax.swing.JLabel othersbilltxt;
    private javax.swing.JCheckBox otherscheck;
    private javax.swing.JTextField otherstxt;
    private javax.swing.JLabel roomlbl;
    private javax.swing.JButton savebtn;
    private javax.swing.JButton searchbtn;
    private javax.swing.JLabel todatelbl;
    private javax.swing.JLabel totallbl;
    private javax.swing.JButton updatebtn;
    private javax.swing.JLabel waivelbl;
    private javax.swing.JTextField waivetxt;
    private javax.swing.JTextField yeartxt;
    // End of variables declaration//GEN-END:variables
}
