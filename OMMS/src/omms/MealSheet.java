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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class MealSheet extends javax.swing.JFrame {
    
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter3;
    TableModel onmodel;
    TableModel offmodel;
    DefaultTableModel ontablemodel = null;
    DefaultTableModel offtablemodel = null;
    DecimalFormat dec;
    DecimalFormat dec2;
    int flag=0;
    
    JProgressBar jProgressBar;
    JFrame frame;
    JOptionPane pane;
    JDialog dialog;
    

    /**
     * Creates new form MealSheet
     */
    public MealSheet() {
        initComponents();
        Ontabledecoration();
        Offtabledecoration();
        inittialization();
        closeBtn();
        
    }
    
    /*
        Seeting the cross button action to Dashboard
     */
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
        sheetdate.setDate(todaysdate);  // setting both datechooser todays date
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) sheetdate.getDateEditor();
        dtedit.setEditable(false);
        
        
        dec = new DecimalFormat("#0.00");
        onmodel = ontable.getModel();
        offmodel = offtable.getModel();
        
        onprogress.setStringPainted(true);
        onprogress.setVisible(true);
        onprogress.setValue(0);
        onprogress.update(onprogress.getGraphics());
        
        onhallidtxt.requestFocus();
        
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
    
    
    public void clearfields(){
        hallidlbl.setText("");
        namelbl.setText("");
        bftxt.setText("");
        lunchtxt.setText("");
        dinnertxt.setText("");
    }
    
    public void countmealon(int dateserial){
        
        try{
            psmt = conn.prepareStatement("select sum(mealsheet.breakfast), sum(mealsheet.lunch), sum(mealsheet.dinner) from stuinfo INNER JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date= ?");
            psmt.setInt(1, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.print(rs.getInt(1));
                totalbftxt.setText(Integer.toString(rs.getInt(1)));
                totallunchtxt.setText(Integer.toString(rs.getInt(2)));
                totaldinnertxt.setText(Integer.toString(rs.getInt(3)));
                
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count total bf lunch dinner", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void counttotalmeal(int dateserial){
        
        try{
            psmt = conn.prepareStatement("select count(mealsheet.hallid) from stuinfo INNER JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date= ?");
            psmt.setInt(1, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.print(rs.getInt(1));
                totalmeallbl.setText(Integer.toString(rs.getInt(1)));
                
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count total meal", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    
    public void deleteunnecessarydate(){
        
        try{
                psmt = conn.prepareStatement("DELETE from mealsheet where breakfast=0 and lunch =0 and dinner =0;");
                psmt.execute();
                psmt.close();
                //System.out.println("called");   
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while deleting "
                        + "unnecessary data", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
    }
    
    public int[] search1stgrp(int dateserial){
        //int i=0;
        int []grp = new int[3];
        grp[0] =0;
        grp[1]=0;
        grp[2]=0;
        
        try{
                psmt = conn.prepareStatement("select min(serial),state FROM grp where date =? GROUP by state");
                psmt.setInt(1, dateserial);
                rs = psmt.executeQuery();
                while(rs.next()){
                    //System.out.print(rs.getInt(1));
                    if(rs.getString(2).equals("breakfast")){
                        grp[0] = rs.getInt(1);
                    }
                    else if(rs.getString(2).equals("lunch")){
                        grp[1] = rs.getInt(1);
                    }
                    else if(rs.getString(2).equals("dinner")){
                        grp[2] = rs.getInt(1);
                    }
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while deleting "
                        + "unnecessary data", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                
            }
        //System.out.println(grp[2]);
        return grp;
    }
    
    
    public void onall(Date date){
        int totalrow =-1, dateserial=0, hallid=0, bf=1, lunch=1,dinner=1, grpserial=0;
        int []grp = new int[3];
        totalrow = offmodel.getRowCount();
        
         try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        grp = search1stgrp(dateserial);
        //System.out.println(grpserial);
        
        int interval = totalrow/100;
        int count =1;
        
        for(int i=0; i<totalrow; i++){
            //System.out.println(i%interval);
            hallid = Integer.parseInt(offmodel.getValueAt(i, 1).toString());
            if( i%interval == 0){
               onprogress.setValue(count);
               onprogress.update(onprogress.getGraphics());
               count++;
            }
            
            try{
                psmt = conn.prepareStatement("insert into mealsheet (hallid, date, breakfast,lunch,dinner,bfgrp,lunchgrp,dinnergrp) values(?,?,?,?,?,?,?,?)");
                psmt.setInt(1, hallid);
                psmt.setInt(2, dateserial);
                psmt.setInt(3, bf);
                psmt.setInt(4, lunch);
                psmt.setInt(5, dinner);
                psmt.setInt(6, grp[0]);
                psmt.setInt(7, grp[1]);
                psmt.setInt(8, grp[2]);
                
                psmt.execute();
                psmt.close();
                
            }  
            catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        }
        
        
    }
    
    public void offall(Date date){
        
        int dateserial =0;
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
                psmt = conn.prepareStatement("delete from mealsheet where date = ?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                //System.out.println("called");   
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while"
                        + " turning all breakfast off", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        countmealon(dateserial);
        counttotalmeal(dateserial);
        
    }
    
    
    public void offallbreakfast(Date date){
        int dateserial =0;
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
                psmt = conn.prepareStatement("update mealsheet set breakfast=0 where date = ?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                //System.out.println("called");   
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while"
                        + " turning all breakfast off", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        countmealon(dateserial);
        counttotalmeal(dateserial);
        
        
    }
    
    
    public void offalllunch(Date date){
        int dateserial =0;
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
                psmt = conn.prepareStatement("update mealsheet set lunch=0 where date = ?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                //System.out.println("called");   
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while"
                        + " turning all breakfast off", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        countmealon(dateserial);
        counttotalmeal(dateserial);
        
    }
    
    
    
    public void offallldinner(Date date){
        int dateserial =0;
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in on all","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
                psmt = conn.prepareStatement("update mealsheet set dinner=0 where date = ?");
                psmt.setInt(1, dateserial);
                psmt.execute();
                psmt.close();
                //System.out.println("called");   
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Error while"
                        + " turning all breakfast off", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        countmealon(dateserial);
        counttotalmeal(dateserial);
        
    }
    
    
    
    public void Ontabledecoration(){
        ontable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        ontable.getTableHeader().setOpaque(false);
        ontable.getTableHeader().setBackground(new Color(32,136,203));
        ontable.getTableHeader().setForeground(new Color(255,255,255));
        ontable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        //ontable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        ontable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        
        
    }
    
    
    
    
    public void Offtabledecoration(){
        offtable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 15));
        offtable.getTableHeader().setOpaque(false);
        offtable.getTableHeader().setBackground(new Color(32,136,203));
        offtable.getTableHeader().setForeground(new Color(255,255,255));
        offtable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        //offtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        offtable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        offtable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        offtable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        
        
    }
    
  
    
    
   
    
    
    
    public void setofftable(Date date){
        int dateserial=0, totalrow=-1;
        offtablemodel = (DefaultTableModel) offtable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in set on table","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try{
            
            psmt = conn.prepareStatement("select hallid,roomno, name from stuinfo where hallid not in (select stuinfo.hallid from stuinfo inner join mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date= ?) and entrydate <= ?");
            //System.out.println(dateserial);
            psmt.setInt(1, dateserial);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            //System.out.println("called");
            while(rs.next()){
                //System.out.println(rs.getInt(1));

                Object o [] = {false,rs.getInt(1),rs.getInt(2), rs.getString(3)};
                offtablemodel.addRow(o);
                 
            }
           
            psmt.close();
            rs.close();
            
            totalrow = offtable.getRowCount();
            offlbl.setText(Integer.toString(totalrow));
            
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                        + "set off table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
        
        //totalrow = offmodel.getRowCount();
        //offlbl.setText(Integer.toUnsignedString(totalrow));
        
        
        
    }
    
    
    
    
    
    public void setontable(Date date){
        int dateserial=0;
        ontablemodel = (DefaultTableModel) ontable.getModel();
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in set on table","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try{
            //System.out.println(dateserial);
            psmt = conn.prepareStatement("select stuinfo.hallid, stuinfo.roomno , stuinfo.name,"
                    + " mealsheet.breakfast, mealsheet.lunch, mealsheet.dinner from "
                    + "stuinfo INNER JOIN mealsheet on stuinfo.hallid = mealsheet.hallid and mealsheet.date= ?");
            
            psmt.setInt(1, dateserial);
            rs = psmt.executeQuery();
            
            while(rs.next()){
                Object o [] = {false,rs.getInt(1), rs.getString(2), rs.getString(3),rs.getInt(4),
                    rs.getInt(5),rs.getString(6)};
                ontablemodel.addRow(o);
            }
            psmt.close();
            rs.close();
            
           
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                        + "set on table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                
            }
        
            countmealon(dateserial);  // each time after setting the meal on table it will count the total meal on 
            counttotalmeal(dateserial);
    }
    
    public void setupdate(int selectedrow, Date date){
        String bf="", lunch="", dinner="";
        int dateserial =0, hallid=0, inbf=0,indinner=0, inlunch=0;
        int []grp = new int[3];
        
        bf= bftxt.getText().trim();
        lunch= lunchtxt.getText().trim();
        dinner= dinnertxt.getText().trim();
        hallid = Integer.parseInt(hallidlbl.getText());
        //System.out.println(selectedrow);
        
        try{
            dateserial = Integer.parseInt(formatter.format(date));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "in set on table","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        try{
            inbf = Integer.parseInt(bf);
            inlunch = Integer.parseInt(lunch);
            indinner = Integer.parseInt(dinner);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Bf lunch dinner format erro","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        if( inbf >= 0 && inlunch >=0  && indinner >= 0){
            //System.out.println(bf+" "+lunch+" "+dinner);
//            onmodel.setValueAt(bf, selectedrow, 4);
//            onmodel.setValueAt(lunch, selectedrow, 5);
//            onmodel.setValueAt(dinner, selectedrow, 6);
            
            grp = search1stgrp(dateserial);
            
            try{
                psmt = conn.prepareStatement("update mealsheet set breakfast=?,lunch=?, dinner=?,bfgrp=?,lunchgrp=?,dinnergrp=? where hallid=? and date = ?");
                psmt.setInt(1, Integer.parseInt(bf));
                psmt.setInt(2, Integer.parseInt(lunch));
                psmt.setInt(3, Integer.parseInt(dinner));
                psmt.setInt(4, grp[0]);
                psmt.setInt(5, grp[1]);
                psmt.setInt(6, grp[2]);
                psmt.setInt(7, hallid);
                psmt.setInt(8, dateserial);
                psmt.execute();
                psmt.close();
                    
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data updating errpr"
                        + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            
            deleteunnecessarydate(); 
            clearbothtable();
            setofftable(date);
            setontable(date);
            selectoffupdate(hallidlbl.getText());
            selectonupdate(hallidlbl.getText());
            onhallidtxt.setText("");
            onhallidtxt.requestFocus();
            //clearfields();
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid input enter value than -1 ", "Data update error", JOptionPane.ERROR_MESSAGE);
        }
        updategrp();
 
    }
    
    
    
    public void setiedntity(int hallid){
        
        try{
            psmt = conn.prepareStatement("select hallid,name from stuinfo where hallid = ?");
            psmt.setInt(1, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.out.print(rs.getString(1));
                hallidlbl.setText(rs.getString(1));
                namelbl.setText(rs.getString(2));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void searchontable(String hallid, boolean check, int dateserial){
        int totalrow=-1, id=-1;
        totalrow = onmodel.getRowCount();
        int flag=-1;
        //System.out.print(check);
        
        for(int i=0; i<totalrow; i++){
            if(onmodel.getValueAt(i, 1).toString().equals(hallid)){
                //ontable.setRowSelectionInterval(i, i);
                ontable.requestFocus();
                ontable.changeSelection(i,0,false, false);
                if(check){
                    ontable.setValueAt(true, i, 0);
                    updateontable(i,dateserial);
                    onhallidtxt.setText("");
                    flag=0;
                    break;
                }
                flag=0;
                updatefield(i);
                id = Integer.parseInt(hallid);
                setiedntity(id);
                break;
            }
        }
        if(flag ==-1){
            JOptionPane.showMessageDialog(null, "Hall id "+hallid+" does "
                    + "not found in this table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
    public void searchofftable(String hallid, boolean check, int dateserial){
        int totalrow=-1 , id=-1;
        totalrow = offmodel.getRowCount();
        int flag=-1;
        for(int i=0; i<totalrow; i++){
            if(offmodel.getValueAt(i, 1).toString().equals(hallid)){
                offtable.requestFocus();
                offtable.changeSelection(i,0,false, false);
                if(check){
                    offtable.setValueAt(true, i, 0);
                    //System.out.print(onmodel.getValueAt(i, 0));
                    updateofftable(i,dateserial);
                    offhallidtxt.setText("");
                }
                flag=0;
                break;
            }
        }
        if(flag ==-1){
            JOptionPane.showMessageDialog(null, "Hall id "+hallid+" does "
                    + "not found in this table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void selectonupdate(String hallid){
        int totalrow=-1;
        totalrow = onmodel.getRowCount();
        for(int i=0; i<totalrow; i++){
            if(onmodel.getValueAt(i, 1).toString().equals(hallid)){
                //System.out.println("called");
                ontable.requestFocus();
                ontable.changeSelection(i,0,false, false);
                updatefield(i);
                setiedntity(Integer.parseInt(hallid));
                break;
            }
        }
    }
    
    
    public void selectoffupdate(String hallid){
        int totalrow=-1;
        totalrow = offmodel.getRowCount();
        for(int i=0; i<totalrow; i++){
            if(offmodel.getValueAt(i, 1).toString().equals(hallid)){
                offtable.requestFocus();
                offtable.changeSelection(i,0,false, false);
                break;
            }
        }
    }
    
    
    public void updatefield(int selectedrow){
        String bf="", lunch="", dinner="";
        bf= onmodel.getValueAt(selectedrow, 4).toString();
        lunch = onmodel.getValueAt(selectedrow, 5).toString();
        dinner= onmodel.getValueAt(selectedrow, 6).toString();
        
        bftxt.setText(bf);
        lunchtxt.setText(lunch);
        dinnertxt.setText(dinner);
        
    }
    
    
    public void updateontable(int selectedrow, int dateserial){
        int hallid= 0;
        boolean check=false;
        Date date = null;
        
        hallid = Integer.parseInt(onmodel.getValueAt(selectedrow,1).toString());
        
        try{
            check = Boolean.parseBoolean(onmodel.getValueAt(selectedrow,0).toString());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Boolean parsing error","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try{
            date= formatter.parse(Integer.toString(dateserial));
        }
        catch(ParseException e){
            JOptionPane.showMessageDialog(null, "Date convertion error in update ontable","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.print(check);
        if(check){
            try{
                psmt = conn.prepareStatement("DELETE FROM mealsheet WHERE hallid=? and date = ?");
                psmt.setInt(1, hallid);
                psmt.setInt(2, dateserial);
                psmt.execute();
                psmt.close();
                    
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data updating errpr"
                        + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            }
            clearfields();
            clearbothtable();
            setofftable(date);
            setontable(date);
            selectoffupdate(Integer.toString(hallid));
        }
        else if(!check){
            updatefield(selectedrow);
            setiedntity(hallid);
            bftxt.requestFocus();
        }
    }
    
    
    
    public void updateofftable(int selectedrow, int dateserial){
        int hallid= 0, bf=1, lunch=1, dinner=1;
        int []grp= new int[3];
        boolean check=false;
        Date date = null;
        
        hallid = Integer.parseInt(offmodel.getValueAt(selectedrow,1).toString());
        //System.out.print(selectedrow+" "+dateserial);
        try{
            check = Boolean.parseBoolean(offmodel.getValueAt(selectedrow,0).toString());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Boolean parsing error","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try{
            date= formatter.parse(Integer.toString(dateserial));
        }
        catch(ParseException e){
            JOptionPane.showMessageDialog(null, "Date convertion error in update offtable","Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        grp = search1stgrp(dateserial);
        
        if(check){
            try{
                psmt = conn.prepareStatement("insert into mealsheet (hallid, date, breakfast,lunch,dinner,bfgrp,lunchgrp,dinnergrp) values(?,?,?,?,?,?,?,?)");
                psmt.setInt(1, hallid);
                psmt.setInt(2, dateserial);
                psmt.setInt(3, bf);
                psmt.setInt(4, lunch);
                psmt.setInt(5, dinner);
                psmt.setInt(6, grp[0]);
                psmt.setInt(7, grp[1]);
                psmt.setInt(8, grp[2]);
                
                psmt.execute();
                psmt.close();
                
            }  
            catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage(), "Data insertion error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            clearbothtable();
            setofftable(date);
            setontable(date);
            selectonupdate(Integer.toString(hallid));
        }
    }
    
    public void updategrp(){
        try{
                psmt = conn.prepareStatement("update mealsheet SET bfgrp=0 WHERE breakfast=0");
                psmt.execute();
                psmt.close();
                    
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data updating errpr"
                    + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        try{
                psmt = conn.prepareStatement("update mealsheet SET lunchgrp=0 WHERE lunch=0");
                psmt.execute();
                psmt.close();
                    
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data updating errpr"
                    + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        try{
                psmt = conn.prepareStatement("update mealsheet SET dinnergrp=0 WHERE dinner=0");
                psmt.execute();
                psmt.close();
                    
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data updating errpr"
                    + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    
    
    public void clearbothtable(){
        
        ontablemodel = (DefaultTableModel) ontable.getModel();
        if(ontablemodel.getColumnCount() > 0){
            ontablemodel.setRowCount(0);
        }
        
        offtablemodel = (DefaultTableModel) offtable.getModel();
        if(offtablemodel.getColumnCount() > 0){
            offtablemodel.setRowCount(0);
           
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
        jLabel2 = new javax.swing.JLabel();
        sheetdate = new com.toedter.calendar.JDateChooser();
        onhallidtxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ontablesearchbtn = new javax.swing.JButton();
        offallbtn = new javax.swing.JButton();
        onallbtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        offhallidtxt = new javax.swing.JTextField();
        offtablesearchbtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        totalmeallbl = new javax.swing.JLabel();
        onprogress = new javax.swing.JProgressBar();
        onchk = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        offlbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ontable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        offtable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        bftxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lunchtxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        dinnertxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totalbftxt = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        totallunchtxt = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totaldinnertxt = new javax.swing.JLabel();
        offallbfbtn = new javax.swing.JButton();
        offalllunchbtn = new javax.swing.JButton();
        offalldinnerbtn = new javax.swing.JButton();
        updatebtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        hallidlbl = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        namelbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthly.png"))); // NOI18N
        jLabel2.setText("Date ");

        sheetdate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        sheetdate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                sheetdatePropertyChange(evt);
            }
        });

        onhallidtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        onhallidtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onhallidtxtActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel3.setText("Hall Id ");

        ontablesearchbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        ontablesearchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        ontablesearchbtn.setText("Seacrch");
        ontablesearchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ontablesearchbtnActionPerformed(evt);
            }
        });

        offallbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        offallbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/wrong.png"))); // NOI18N
        offallbtn.setText("Off All");
        offallbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offallbtnActionPerformed(evt);
            }
        });

        onallbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        onallbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/correct.png"))); // NOI18N
        onallbtn.setText("On All");
        onallbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onallbtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/hallid.png"))); // NOI18N
        jLabel4.setText("Hall Id ");

        offhallidtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        offhallidtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offhallidtxtActionPerformed(evt);
            }
        });

        offtablesearchbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        offtablesearchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        offtablesearchbtn.setText("Search");
        offtablesearchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offtablesearchbtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 28)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/mealsheet.png"))); // NOI18N
        jLabel5.setText("Daily Meal Sheet");

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setText("Total On : ");

        totalmeallbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalmeallbl.setForeground(new java.awt.Color(255, 51, 0));
        totalmeallbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalmeallbl.setText("0");

        onchk.setBackground(new java.awt.Color(208, 227, 229));
        onchk.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        onchk.setText("Auto");

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Total Off :");

        offlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        offlbl.setForeground(new java.awt.Color(255, 51, 0));
        offlbl.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(onchk, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(onhallidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sheetdate, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ontablesearchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 100, Short.MAX_VALUE)
                        .addComponent(offallbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(onallbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalmeallbl, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(offhallidtxt, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(offlbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(offtablesearchbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addComponent(onprogress, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(sheetdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalmeallbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(onprogress, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(onchk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(offtablesearchbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(onhallidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ontablesearchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offallbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(onallbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offhallidtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ontable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ontable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Hall Id", "Room", "Name", "Bf", "Lunch", "Dinner"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ontable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        ontable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        ontable.setShowVerticalLines(false);
        ontable.getTableHeader().setReorderingAllowed(false);
        ontable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ontableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ontable);
        if (ontable.getColumnModel().getColumnCount() > 0) {
            ontable.getColumnModel().getColumn(0).setMaxWidth(60);
            ontable.getColumnModel().getColumn(1).setMaxWidth(150);
            ontable.getColumnModel().getColumn(2).setMaxWidth(70);
            ontable.getColumnModel().getColumn(4).setMaxWidth(60);
            ontable.getColumnModel().getColumn(5).setMaxWidth(60);
            ontable.getColumnModel().getColumn(6).setMaxWidth(60);
        }

        offtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Hall ID", "Room", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        offtable.setRowHeight(26);
        offtable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        offtable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        offtable.setShowVerticalLines(false);
        offtable.getTableHeader().setReorderingAllowed(false);
        offtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                offtableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(offtable);
        if (offtable.getColumnModel().getColumnCount() > 0) {
            offtable.getColumnModel().getColumn(0).setMaxWidth(60);
            offtable.getColumnModel().getColumn(1).setMaxWidth(100);
            offtable.getColumnModel().getColumn(2).setMaxWidth(100);
        }

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel12.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Breakfast ");

        bftxt.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        bftxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bftxtActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Lunch ");

        lunchtxt.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lunchtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lunchtxtActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Dinner ");

        dinnertxt.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        dinnertxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dinnertxtActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 51, 0));
        jLabel7.setText("Total Breakfast");

        totalbftxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalbftxt.setForeground(new java.awt.Color(255, 51, 0));
        totalbftxt.setText("0");

        jLabel16.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 51, 0));
        jLabel16.setText("Total Lunch");

        totallunchtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totallunchtxt.setForeground(new java.awt.Color(255, 51, 0));
        totallunchtxt.setText("0");

        jLabel18.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 51, 0));
        jLabel18.setText("Total Dinner");

        totaldinnertxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totaldinnertxt.setForeground(new java.awt.Color(255, 51, 0));
        totaldinnertxt.setText("0");

        offallbfbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        offallbfbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/turn-off selected.png"))); // NOI18N
        offallbfbtn.setText("Off All Breakfast");
        offallbfbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offallbfbtnActionPerformed(evt);
            }
        });

        offalllunchbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        offalllunchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/turn-off lunch.png"))); // NOI18N
        offalllunchbtn.setText("Off All Lunch");
        offalllunchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offalllunchbtnActionPerformed(evt);
            }
        });

        offalldinnerbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        offalldinnerbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/dinneroff.png"))); // NOI18N
        offalldinnerbtn.setText("Off All Dinner");
        offalldinnerbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offalldinnerbtnActionPerformed(evt);
            }
        });

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update meal.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Hall ID ");

        hallidlbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Name  ");

        namelbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(199, 199, 199)
                        .addComponent(offallbfbtn)
                        .addGap(18, 18, 18)
                        .addComponent(offalllunchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(offalldinnerbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(305, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(dinnertxt, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lunchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(bftxt, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(hallidlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totallunchtxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totaldinnertxt, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                            .addComponent(totalbftxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(87, 87, 87))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalbftxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totallunchtxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bftxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hallidlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lunchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dinnertxt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totaldinnertxt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(offallbfbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offalllunchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offalldinnerbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sheetdatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sheetdatePropertyChange
        // TODO add your handling code here:
        Date date=null;
        date = sheetdate.getDate();
        
        clearbothtable();
        

        if(date != null ){
            setontable(date);
            setofftable(date);
        }
    }//GEN-LAST:event_sheetdatePropertyChange

    private void onallbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onallbtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        date = sheetdate.getDate();
        if(date != null){
            onall(date);
            
            clearbothtable();
            
            
            setofftable(date);
            setontable(date);
        }
        else{
            JOptionPane.showMessageDialog(null, "No date is selected","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
        }
      
    }//GEN-LAST:event_onallbtnActionPerformed

    private void ontableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ontableMouseClicked
        // TODO add your handling code here:
        //System.out.println("called");
        offtable.clearSelection();
        
        Date date=null;
        int dateserial =0, selectedrow =-1;
        date = sheetdate.getDate();
        selectedrow = ontable.getSelectedRow();
        
        if( date!= null && selectedrow >=0){
            
            try{
            dateserial = Integer.parseInt(formatter.format(date));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Date Parse "
                        + "in set on table","Date parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            updateontable(selectedrow,dateserial);
            
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No date is selected","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ontableMouseClicked

    private void offtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_offtableMouseClicked
        // TODO add your handling code here:
        ontable.clearSelection();
        
        Date date=null;
        int dateserial =0, selectedrow =-1;
        date = sheetdate.getDate();
        selectedrow = offtable.getSelectedRow();
        
        if( date!= null && selectedrow >=0){
            
            try{
            dateserial = Integer.parseInt(formatter.format(date));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Date Parse "
                        + "in set off table","Date parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            updateofftable(selectedrow,dateserial);
            
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No date is selected","Meal sheet on error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_offtableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow =-1;
        Date date=null;
        date = sheetdate.getDate();
        selectedrow = ontable.getSelectedRow();
        //System.out.print(selectedrow+" "+date);
        if(selectedrow >= 0){
            setupdate(selectedrow, date);
        }
        else{
            JOptionPane.showMessageDialog(null, "No row is selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updatebtnActionPerformed

    private void offallbfbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offallbfbtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        date = sheetdate.getDate();
        if(date != null){
            offallbreakfast(date);
            deleteunnecessarydate();
            updategrp();
            clearbothtable();
            setofftable(date);
            setontable(date);
        }
        else{
            JOptionPane.showMessageDialog(null, "No Date selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_offallbfbtnActionPerformed

    private void offalllunchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offalllunchbtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        date = sheetdate.getDate();
        if(date != null){
            offalllunch(date);
            deleteunnecessarydate();
            updategrp();
            clearbothtable();
            setofftable(date);
            setontable(date);
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No Date selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_offalllunchbtnActionPerformed

    private void offalldinnerbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offalldinnerbtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        date = sheetdate.getDate();
        if(date != null){
            offallldinner(date);
            deleteunnecessarydate();
            updategrp();
            clearbothtable();
            setofftable(date);
            setontable(date);
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No Date selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_offalldinnerbtnActionPerformed

    private void ontablesearchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ontablesearchbtnActionPerformed
        // TODO add your handling code here:
        String hallid ="";
        Date date=null;
        int id=0, dateserial =0;
        boolean check=false;
        hallid = onhallidtxt.getText().trim();
        date = sheetdate.getDate();
        check = onchk.isSelected();
        //System.out.println(check);
        
        try{
            id = Integer.parseInt(hallid);
            dateserial = Integer.parseInt(formatter.format(date));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter valid hall id","hallid parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        if(id >0){
            searchontable(hallid,check,dateserial);
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter valid hall id","hallid parsing error", JOptionPane.ERROR_MESSAGE);
        }
        
        if(check){
            onhallidtxt.requestFocus();
        }
        else{
            bftxt.requestFocus();
        }
    }//GEN-LAST:event_ontablesearchbtnActionPerformed

    private void offtablesearchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offtablesearchbtnActionPerformed
        // TODO add your handling code here:
        
        Date date=null;
        String hallid ="";
        boolean check=false;
        int id=0, dateserial =0;
        hallid = offhallidtxt.getText().trim();
        date = sheetdate.getDate();
        check = onchk.isSelected();
        
        
        try{
            id = Integer.parseInt(hallid);
            dateserial = Integer.parseInt(formatter.format(date));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter valid hall id","hallid parsing error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        if(id >0){
            searchofftable(hallid,check,dateserial);
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter valid hall id","hallid parsing error", JOptionPane.ERROR_MESSAGE);
        }
        offhallidtxt.requestFocus();
        
    }//GEN-LAST:event_offtablesearchbtnActionPerformed

    private void onhallidtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onhallidtxtActionPerformed
        // TODO add your handling code here:
        ontablesearchbtn.doClick();
        //offtable.clearSelection();
    }//GEN-LAST:event_onhallidtxtActionPerformed

    private void offhallidtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offhallidtxtActionPerformed
        // TODO add your handling code here:
        offtablesearchbtn.doClick();
        //ontable.clearSelection();
    }//GEN-LAST:event_offhallidtxtActionPerformed

    private void offallbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offallbtnActionPerformed
        // TODO add your handling code here:
        Date date =null;
        date = sheetdate.getDate();
        if(date != null){
            offall(date);
            clearfields();
            deleteunnecessarydate();
            clearbothtable();
            setofftable(date);
            setontable(date);
            
        }
        else{
            JOptionPane.showMessageDialog(null, "No Date selected","Update error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_offallbtnActionPerformed

    private void lunchtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lunchtxtActionPerformed
        // TODO add your handling code here:
        dinnertxt.requestFocus();
    }//GEN-LAST:event_lunchtxtActionPerformed

    private void dinnertxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dinnertxtActionPerformed
        // TODO add your handling code here:
        updatebtn.doClick();
    }//GEN-LAST:event_dinnertxtActionPerformed

    private void bftxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bftxtActionPerformed
        // TODO add your handling code here:
        lunchtxt.requestFocus();
    }//GEN-LAST:event_bftxtActionPerformed

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
            java.util.logging.Logger.getLogger(MealSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MealSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MealSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MealSheet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MealSheet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bftxt;
    private javax.swing.JTextField dinnertxt;
    private javax.swing.JLabel hallidlbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lunchtxt;
    private javax.swing.JLabel namelbl;
    private javax.swing.JButton offallbfbtn;
    private javax.swing.JButton offallbtn;
    private javax.swing.JButton offalldinnerbtn;
    private javax.swing.JButton offalllunchbtn;
    private javax.swing.JTextField offhallidtxt;
    private javax.swing.JLabel offlbl;
    private javax.swing.JTable offtable;
    private javax.swing.JButton offtablesearchbtn;
    private javax.swing.JButton onallbtn;
    private javax.swing.JCheckBox onchk;
    private javax.swing.JTextField onhallidtxt;
    private javax.swing.JProgressBar onprogress;
    private javax.swing.JTable ontable;
    private javax.swing.JButton ontablesearchbtn;
    private com.toedter.calendar.JDateChooser sheetdate;
    private javax.swing.JLabel totalbftxt;
    private javax.swing.JLabel totaldinnertxt;
    private javax.swing.JLabel totallunchtxt;
    private javax.swing.JLabel totalmeallbl;
    private javax.swing.JButton updatebtn;
    // End of variables declaration//GEN-END:variables
}
