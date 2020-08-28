/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class ItemGrpAssign extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    int flag=0;

    /**
     * Creates new form ItemGrpAssign
     */
    public ItemGrpAssign() {
        initComponents();
        Tabledecoration();
        inittialization();
        flag=1;
        settable();
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
        
        model = grptable.getModel();
        
        String state = statecombo.getSelectedItem().toString();
        setgrp(todaysdate,state);
        
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
        grptable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        grptable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        grptable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        grptable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
    }
    
    
    
    public void changegrp(){
        int selectedrow =-1, selectedindx =-1, dateserial=0;
        String strdate = "", state="", item="";
        Date date =null;
        
        
        selectedrow = grptable.getSelectedRow();
        if(selectedrow >= 0){
            selectedindx = grpcombo.getSelectedIndex();
            strdate = model.getValueAt(selectedrow, 1).toString();
            state = model.getValueAt(selectedrow, 5).toString();
            item = model.getValueAt(selectedrow, 2).toString();
            
            try{
                date = formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "date convertion error in serachdatabase", "Date error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            switch (state) {
                case "Breakfast":
                    try{
                        psmt = conn.prepareStatement("update storeinout set bfgrp=? where serial=? and item= ?");
                        psmt.setInt(1, selectedindx);
                        psmt.setInt(2, dateserial);
                        psmt.setString(3, item);
                        psmt.execute();
                        psmt.close();
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Failed to update item grp", "Data update error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }   settable();
                    break;
                case "Lunch":
                    try{
                        psmt = conn.prepareStatement("update storeinout set lunchgrp=? where serial=? and item = ?");
                        psmt.setInt(1, selectedindx);
                        psmt.setInt(2, dateserial);
                        psmt.setString(3, item);
                        psmt.execute();
                        psmt.close();
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Failed to update item grp", "Data update error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }   settable();
                    break;
                case "Dinner":
                    try{
                        psmt = conn.prepareStatement("update storeinout set dinnergrp=? where serial=? and item =?");
                        psmt.setInt(1, selectedindx);
                        psmt.setInt(2, dateserial);
                        psmt.setString(3, item);
                        psmt.execute();
                        psmt.close();
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Failed to update item grp", "Data update error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }   settable();
                    break;
                default:
                    break;
            }
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No row is selected", "Date error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void setgrp(Date date, String state){
        int dateserial=0;
        String grpname="";
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "date convertion error in serachdatabase", "Date error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        grpcombo.removeAllItems();
        grpcombo.addItem("All");
        
        try{
            psmt = conn.prepareStatement("select name from grp where date= ? and state=?");
            psmt.setInt(1, dateserial);
            psmt.setString(2, state);
            rs = psmt.executeQuery();
            while(rs.next()){
                grpname = rs.getString(1);
                grpname = grpname.substring(0, 1).toUpperCase() + grpname.substring(1);
                grpcombo.addItem(grpname);
                //System.out.println(item+" "+bf+" "+bfgrp);
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    
    public String itemunit(String name){
        String unit="";
        
        try{
            psmt = conn.prepareStatement("select unit from storeditem where name=?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                unit = rs.getString(1);
                //System.out.println(item+" "+bf+" "+bfgrp);
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        
        return unit;
    }
    
    public String findgrp(int dateserial , int grpserial, String state){
        String grp="";
        
        try{
            psmt = conn.prepareStatement("select name from grp where date=? and serial=? and state=?");
            psmt.setInt(1, dateserial);
            psmt.setInt(2, grpserial);
            psmt.setString(3, state);
            rs = psmt.executeQuery();
            while(rs.next()){
                grp = rs.getString(1);
                //System.out.println(item+" "+bf+" "+bfgrp);
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return grp;
        
    }
    
    
    public void searchdatabase(Date date, String state){
        
        int dateserial=0, bfgrp=-1, lunchgrp=-1, dinnergrp=-1,serial=1 ,totalrow=-1 ,allgrp=-1;
        Double bf=0.0, lunch=0.0,dinner=0.0, amount=0.0;
        String strdate="", item="", unit="", grp="";
        tablemodel = (DefaultTableModel) grptable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
            strdate = formatter1.format(date);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "date convertion error in serachdatabase", "Date error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        switch (state) {
            case "Breakfast":
                //System.out.println(dateserial);
                try{
                    psmt = conn.prepareStatement("select item,bf,bfgrp from storeinout where serial= ?");
                    psmt.setInt(1, dateserial);
                    rs = psmt.executeQuery();
                    while(rs.next()){
                        
                        item= rs.getString(1);
                        bf = rs.getDouble(2);
                        bfgrp = rs.getInt(3);
                        
                        //System.out.println(item+" "+bf);
                        
                        if(bfgrp == 0 && bf != 0){
                            //System.out.println("called");
                            Object o [] = {serial,strdate,item,bf,"Stored Item", state,0};
                            tablemodel.addRow(o);
                        }
                        else if(bf != 0){
                            //grp = findgrp(dateserial,bfgrp);
                            Object o [] = {serial,strdate,item,bf,"Stored Item", state,bfgrp};
                            tablemodel.addRow(o);
                        }
                        serial++;
                        
                        //System.out.println(item+" "+bf+" "+bfgrp);
                    }
                    psmt.close();
                    rs.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    
                }   break;
            case "Lunch":
                //System.out.println(dateserial);
                try{
                    psmt = conn.prepareStatement("select item,lunch,lunchgrp from storeinout where serial= ?");
                    psmt.setInt(1, dateserial);
                    rs = psmt.executeQuery();
                    while(rs.next()){
                        //System.gtiout.println(rs.getInt(1));
                        item= rs.getString(1);
                        lunch = rs.getDouble(2);
                        lunchgrp = rs.getInt(3);
                        
                        
                        if(lunchgrp == 0 && lunch!=0){
                            //System.out.println("called");
                            Object o [] = {serial,strdate,item,lunch,"Stored Item", state,0};
                            tablemodel.addRow(o);
                        }
                        else if(lunch != 0){
                            //grp = findgrp(dateserial,lunchgrp);
                            Object o [] = {serial,strdate,item,lunch,"Stored Item", state,lunchgrp};
                            tablemodel.addRow(o);
                        }
                        serial++;
                    }
                    psmt.close();
                    rs.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }   break;
            case "Dinner":
                //System.out.println(dateserial);
                try{
                    psmt = conn.prepareStatement("select item,dinner,dinnergrp from storeinout where serial= ?");
                    psmt.setInt(1, dateserial);
                    rs = psmt.executeQuery();
                    while(rs.next()){
                        //System.gtiout.println(rs.getInt(1));
                        item= rs.getString(1);
                        dinner = rs.getDouble(2);
                        dinnergrp = rs.getInt(3);
                        
                        if(dinnergrp == 0 && dinner != 0){
                            //System.out.println("called");
                            Object o [] = {serial,strdate,item,dinner,"Stored Item", state,0};
                            tablemodel.addRow(o);
                        }
                        else if(dinner != 0){
                            //grp = findgrp(dateserial,dinnergrp);
                            Object o [] = {serial,strdate,item,dinner,"Stored Item", state,dinnergrp};
                            tablemodel.addRow(o);
                        }
                        serial++;
                        
                    }
                    psmt.close();
                    rs.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }   break;
            default:
                break;
        }
        
        totalrow = model.getRowCount();
        for(int i=0; i<totalrow; i++){
            amount = Double.parseDouble(model.getValueAt(i, 3).toString());
            allgrp = Integer.parseInt(model.getValueAt(i, 6).toString());
            //System.out.println(model.getValueAt(i, 6).toString());
            item = model.getValueAt(i, 2).toString();
            unit = itemunit(item);
            unit = Double.toString(amount) +" "+unit; // adding unit with amount
            model.setValueAt(unit, i, 3);
            if(allgrp != 0){
                grp = findgrp(dateserial,allgrp,state);
                grp = grp.substring(0, 1).toUpperCase() + grp.substring(1);
                model.setValueAt(grp, i, 6);
            }
            else if(allgrp == 0){
                model.setValueAt("All", i, 6);
            }
            
        }
        
        
    }
    
    
    public void settable(){
        
        Date date=null;
        String state="";
        date = datechooser.getDate();
        
        tablemodel = (DefaultTableModel) grptable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        if(flag ==1 && date != null){
            state = statecombo.getSelectedItem().toString();
            searchdatabase(date,state);
            setgrp(date,state);
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
        datechooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        statecombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        grpcombo = new javax.swing.JComboBox<>();
        updatebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        grptable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/compare.png"))); // NOI18N
        jLabel1.setText("ITEM GROUPING");

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

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/stats.png"))); // NOI18N
        jLabel3.setText("State");

        statecombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        statecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        statecombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statecomboActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/clients.png"))); // NOI18N
        jLabel4.setText("Groups");

        grpcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update meal.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updatebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(grpcombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(grpcombo)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statecombo)
                    .addComponent(datechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        grptable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        grptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Date", "Item", "Amount", "Type", "State", "Group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        jScrollPane1.setViewportView(grptable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_datechooserPropertyChange
        // TODO add your handling code here:
        settable();
    }//GEN-LAST:event_datechooserPropertyChange

    private void statecomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statecomboActionPerformed
        // TODO add your handling code here:
        settable();
        
        
    }//GEN-LAST:event_statecomboActionPerformed

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        changegrp();
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
            java.util.logging.Logger.getLogger(ItemGrpAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemGrpAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemGrpAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemGrpAssign.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemGrpAssign().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser datechooser;
    private javax.swing.JComboBox<String> grpcombo;
    private javax.swing.JTable grptable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> statecombo;
    private javax.swing.JButton updatebtn;
    // End of variables declaration//GEN-END:variables
}
