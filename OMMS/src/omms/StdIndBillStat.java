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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author Asus
 */
public class StdIndBillStat extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;

    /**
     * Creates new form stdIndBillStat
     */
    public StdIndBillStat() {
        initComponents();
        billtabledecoration();
        initialize();
        idtxt.requestFocus();
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
    
    
    
    
    public void billtabledecoration(){
        billtable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        billtable.getTableHeader().setOpaque(false);
        billtable.getTableHeader().setBackground(new Color(32,136,203));
        billtable.getTableHeader().setForeground(new Color(255,255,255));
        billtable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        billtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        billtable.getColumnModel().getColumn(8).setCellRenderer(centerRender);    
    }
    
    
    public void initialize(){
       
        conn = Jconnection.ConnecrDb(); // set connection with database
       
        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        
        Date todaysdate =new Date();
        fromdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        todatechooser.setDate(todaysdate);
        
        
        dec = new DecimalFormat("#0.00");
        
        model = billtable.getModel();
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) fromdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        dtedit = (JTextFieldDateEditor) todatechooser.getDateEditor();
        dtedit.setEditable(false);
        
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
    
    public boolean checkhallid(String hallid){
        boolean exist=false;
        try{
            psmt = conn.prepareStatement("select roll,name,dept,hallid from stuinfo where hallid= ?");
            psmt.setString(1, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                exist= true;
                namelbl.setText(rs.getString(2));
                rolllbl.setText(rs.getString(1));
                deptlbl.setText(rs.getString(3));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return exist;
    }

    
    public int checkroll(String roll){
        int hallid=0;
        try{
            psmt = conn.prepareStatement("select hallid,roll,name,dept from stuinfo where roll= ?");
            psmt.setString(1, roll);
            rs = psmt.executeQuery();
            while(rs.next()){
                hallid = rs.getInt(1);
                namelbl.setText(rs.getString(3));
                rolllbl.setText(rs.getString(2));
                deptlbl.setText(rs.getString(4));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return hallid;
    }

    
    public void mealstatus(int fromdate, int todate, int hallid){
        Map<Integer, BillAmount> billmap = new HashMap<>();
        Map<Integer, DailyMealState> mealsheet = new HashMap<>();
        Map<Integer, Double> tmpbill = new HashMap<>();
        tablemodel = (DefaultTableModel) billtable.getModel();
        
        Date date=null;
        String strdate="";
        int bf=0, lunch=0, dinner=0, key=0, bfgrp=0, lunchgrp=0, dinnergrp=0;
        Double bfbill=0.0, lunchbill=0.0, dinnerbill=0.0, temporary=0.00, total=0.0;
        
        DailyAvgBill db= new DailyAvgBill();
        SingleStdntBill st= new SingleStdntBill();

        billmap=db.setbill(fromdate, todate);
        tmpbill = db.tmpfoodbill(fromdate, todate, hallid);
        mealsheet = st.meal(fromdate, todate, hallid);
        
        int reminder=0;
        List keys = new ArrayList(billmap.keySet());
        Collections.sort(keys);
        
        
        for(int i=0 ;i<keys.size(); i++){
            //System.out.println(keys.get(i));
            key = Integer.parseInt(keys.get(i).toString());
            try{
                date =formatter1.parse(Integer.toString(key));
                strdate = formatter.format(date);
            }catch(ParseException e){
                JOptionPane.showMessageDialog(null, "Date convertion error", "convertion error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //System.out.println(mealsheet.containsKey(i)+" "+keys);
            
            if(mealsheet.containsKey(key) && !tmpbill.containsKey(key)){
//                //System.out.println(key);
                bf=mealsheet.get(key).bf;
                lunch = mealsheet.get(key).lunch;
                dinner = mealsheet.get(key).dinner;
                
                bfgrp = mealsheet.get(key).bfgrp;
                lunchgrp = mealsheet.get(key).lunchgrp;
                dinnergrp = mealsheet.get(key).dinnergrp;
                
                bfbill = billmap.get(key).bfbill.get(bfgrp)*bf;
                lunchbill = billmap.get(key).lunchbill.get(lunchgrp)*lunch;
                dinnerbill = billmap.get(key).dinnerbill.get(dinnergrp) * dinner;
                
                total = bfbill + lunchbill + dinnerbill;
                
                Object o []={strdate,bf,lunch,dinner, dec.format(bfbill), dec.format(lunchbill),
                    dec.format(dinnerbill),0.00,dec.format(total)};
                tablemodel.addRow(o);
            }
            else if(mealsheet.containsKey(key) && tmpbill.containsKey(key)){
                //System.out.println(key);
                bf=mealsheet.get(key).bf;
                lunch = mealsheet.get(key).lunch;
                dinner = mealsheet.get(key).dinner;
                
                bfgrp = mealsheet.get(key).bfgrp;
                lunchgrp = mealsheet.get(key).lunchgrp;
                dinnergrp = mealsheet.get(key).dinnergrp;
                
                bfbill = billmap.get(key).bfbill.get(bfgrp)*bf;
                lunchbill = billmap.get(key).lunchbill.get(lunchgrp)*lunch;
                dinnerbill = billmap.get(key).dinnerbill.get(dinnergrp) * dinner;
                
                temporary = tmpbill.get(key);
                
                total = bfbill + lunchbill + dinnerbill+temporary;
                
                Object o []={strdate,bf,lunch,dinner, dec.format(bfbill), dec.format(lunchbill),
                    dec.format(dinnerbill),dec.format(temporary),dec.format(total)};
                tablemodel.addRow(o);
            }
            else if( !mealsheet.containsKey(key) && tmpbill.containsKey(key)){
                //System.out.println(key);
                bfbill =00.0;
                lunchbill =00.0;
                dinnerbill = 00.0;
                temporary = tmpbill.get(key);
                
                total = temporary;
                
                Object o []={strdate, 0, 0, 0, dec.format(bfbill), dec.format(lunchbill),
                    dec.format(dinnerbill),dec.format(temporary),dec.format(total)};
                tablemodel.addRow(o);
            }
        }
//        //st.generatebill(billmap, mealsheet);
//        
        total=0.00;
        for(int i=0; i<model.getRowCount(); i++){
            total += Double.parseDouble(model.getValueAt(i, 8).toString());
            
        }
        grandtotaltxt.setText(dec.format(total));
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
        fromdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        todatechooser = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        idtxt = new javax.swing.JTextField();
        shwobtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        grandtotaltxt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        namelbl = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rolllbl = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        deptlbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billtable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/bill.png"))); // NOI18N
        jLabel1.setText("Student Bill");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/start.png"))); // NOI18N
        jLabel2.setText("From Date");

        fromdatechooser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/end.png"))); // NOI18N
        jLabel3.setText("To Date");

        todatechooser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel4.setText("Roll /Hall Id");

        idtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtxtActionPerformed(evt);
            }
        });

        shwobtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        shwobtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/billshow.png"))); // NOI18N
        shwobtn.setText("Show Bill");
        shwobtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shwobtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Grand Total");

        grandtotaltxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        grandtotaltxt.setForeground(new java.awt.Color(255, 51, 0));
        grandtotaltxt.setText("0");

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Name");

        namelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Roll ");

        rolllbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Dept ");

        deptlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(grandtotaltxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rolllbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(todatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shwobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(51, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(deptlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fromdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(todatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(grandtotaltxt, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(shwobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rolllbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deptlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );

        billtable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        billtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Breakfast", "Lunch", "Dinner", "Bf Bill", "Lunch Bill", "Dinner Bill", "Tmp Bill", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billtable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        billtable.setRowHeight(26);
        billtable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        billtable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        billtable.setShowVerticalLines(false);
        billtable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(billtable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 969, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void shwobtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shwobtnActionPerformed
        // TODO add your handling code here:
        String strid="";
        int id=0, hallid=0;
        strid = idtxt.getText().trim();
        Date from=null, to=null;
        int fromdate=0, todate=0;
        
        
        from = fromdatechooser.getDate();
        to = todatechooser.getDate();
        
        //System.out.println(strid);
        try{
            fromdate = Integer.parseInt(formatter1.format(from));
            todate = Integer.parseInt(formatter1.format(to));
        }
        catch(Exception e ){
            JOptionPane.showMessageDialog(null, "date conversion error", "Error Occured!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tablemodel = (DefaultTableModel) billtable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        if (fromdate > todate){
            JOptionPane.showMessageDialog(null, "From date must be smaller than Todate", "Date error", JOptionPane.ERROR_MESSAGE);
            return;
        }
//       
//        
//        
        if(checkhallid(strid)){
            //System.out.println("called");
            try{
            id= Integer.parseInt(strid);
            }
            catch(Exception e ){
                JOptionPane.showMessageDialog(null, "Enter a valid hallid", "Error Occured!", JOptionPane.ERROR_MESSAGE);
                namelbl.setText("");
                rolllbl.setText("");
                deptlbl.setText("");
                return;
            }
            mealstatus(fromdate,todate, id);
        }
        else{
            hallid = checkroll(strid);
            //System.out.println(hallid);
            if(hallid != 0){
                mealstatus(fromdate,todate, hallid);
            }
            else{
                JOptionPane.showMessageDialog(null, "id does not exist", "Error Occured!", JOptionPane.ERROR_MESSAGE);
                namelbl.setText("");
                rolllbl.setText("");
                deptlbl.setText("");
                return;
            }
        }
        
//        System.out.println(tablemodel.getRowCount());
        if(tablemodel.getRowCount() <= 0){
            JOptionPane.showMessageDialog(null, "No data exist for the given date", "Error Occured!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_shwobtnActionPerformed

    private void idtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtxtActionPerformed
        // TODO add your handling code here:
        shwobtn.doClick();
    }//GEN-LAST:event_idtxtActionPerformed

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
            java.util.logging.Logger.getLogger(StdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdIndBillStat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdIndBillStat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable billtable;
    private javax.swing.JLabel deptlbl;
    private com.toedter.calendar.JDateChooser fromdatechooser;
    private javax.swing.JLabel grandtotaltxt;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel namelbl;
    private javax.swing.JLabel rolllbl;
    private javax.swing.JButton shwobtn;
    private com.toedter.calendar.JDateChooser todatechooser;
    // End of variables declaration//GEN-END:variables
}
