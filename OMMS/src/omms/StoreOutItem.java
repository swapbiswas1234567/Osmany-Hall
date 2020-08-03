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
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
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
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter3;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;
    Double avg=0.0;
    /**
     * Creates new form StoreOutItem
     */
    public StoreOutItem() {
        initComponents();
        Tabledecoration();
        initialize();
        getAllstoreditem();
        int combolen = itemComboboxlen();
        flag =1;  // it will allow the combobox to count the available amount 
        
        
        
        String itemname="";
        Date date = insertdatechooser.getDate();
        int dateserial =-1;
        itemname = insertCombobox.getSelectedItem().toString();  // setting the remaining amount initially
        dateserial = Integer.parseInt(formatter1.format(date));
        setAvailable(dateserial, itemname);
        
        int comboindex =-1;
        comboindex = insertCombobox.getSelectedIndex();  
        String unit = setInsertunit(comboindex);
        insertunit.setText(unit);
        
        int comboindex1 =-1;
        comboindex1 = updateCombobox.getSelectedIndex();
        String unit1 = setupdatetunit(comboindex1);
        updateunit.setText(unit1);
    }
    
    
    public void initialize(){
       
        conn = Jconnection.ConnecrDb(); // set connection with database
       
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter3 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        insertdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        updatedatechooser.setDate(todaysdate);
        
        
        dec = new DecimalFormat("#0.000");
        model = Storeouttable.getModel();
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) insertdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        dtedit = (JTextFieldDateEditor) updatedatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        insertavgprice.setEditable(false);
        updateavgprice.setEditable(false);
    }
    
    
    
    
    
    //get name of all item from database 
    public void getAllstoreditem(){
       
        try{
            psmt = conn.prepareStatement("select name from storeditem");
            rs = psmt.executeQuery();
            while(rs.next()){
                insertCombobox.addItem(rs.getString(1));  // sending the name of item to set in the combobox
                updateCombobox.addItem(rs.getString(1));  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
   
    }
    
    //adding inserted item on the jtable 
    public void setOuttable(String name, String status,String amount,String remaining, Date date){
        
        try{

            String getdate = null;
            int dateserial=-1;
            Double available = 0.00;
            Double remainingval = 0.00;
            Double tableamount =0.0;
            
            try{
                getdate = formatter3.format(date);
                dateserial = Integer.parseInt(formatter1.format(date));
                
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Date Convertion Error","Date Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            //System.out.println("called "+remaining);
           
            available = Double.parseDouble(amount);
            remainingval = Double.parseDouble(remaining);
            
            if( available <= 0 || available > remainingval){
                JOptionPane.showMessageDialog(null,"Invalid amount","Data Error",JOptionPane.ERROR_MESSAGE);
            }
            else if( !searchIndatabase(dateserial,name,status) && CheckTableItem(name,status,getdate,-1) < 0){
                //System.out.println(dec.format(available));
                tablemodel = (DefaultTableModel) Storeouttable.getModel();
                Object o [] = {name,dec.format(available),remainingval,getdate, status};
                tablemodel.addRow(o);
                clearTextfieldafterinsert(); // after each insertion it will clear text field 
                setAvailable(dateserial,name);
            }
            else if(CheckTableItem(name,status,getdate,-1) >= 0){
                JOptionPane.showMessageDialog(null,"Data already inserted in the "
                        + "table","Data Error",JOptionPane.ERROR_MESSAGE);
                return ;
            }
            else {
                JOptionPane.showMessageDialog(null,"Data exist "
                        + "in database","Data Error",JOptionPane.ERROR_MESSAGE);
                return ;
            }
            
        }
        catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Data reading Jtable error","Data Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    
    public void setUpdatevalue(){
        
        int selectedrow =-1;
        try{
            String name = updateCombobox.getSelectedItem().toString();
            String status = updatestatuscombo.getSelectedItem().toString();
            String amount = updateamounttxt.getText().trim();
            String remaining = updateavailable.getText().trim();
            String strdate=null;
            Double insertamount = 0.0;
            Double remainingamount = 0.0;
            Date date;
            date = updatedatechooser.getDate();
            //getdate = formatter.format(date);
            strdate = formatter3.format(date);
            
            selectedrow = Storeouttable.getSelectedRow();
            int serial = Integer.parseInt(formatter1.format(date));
            insertamount = Double.parseDouble(amount);
            remainingamount = Double.parseDouble(remaining);
            
            
            if( insertamount > remainingamount || remainingamount <=0 || insertamount <=0){
                JOptionPane.showMessageDialog(null,"Invalid amount","Data Error",JOptionPane.ERROR_MESSAGE);
            }
            else if(selectedrow <0 ){
                JOptionPane.showMessageDialog(null,"Please Select a Row","Data Error",JOptionPane.ERROR_MESSAGE);
            }
            else if(CheckTableItem(name,status,strdate,selectedrow) <0 && !searchIndatabase(serial,name,status)){
                model.setValueAt(name, selectedrow, 0);
                model.setValueAt(amount, selectedrow, 1);
                model.setValueAt(remaining, selectedrow, 2);
                model.setValueAt(strdate, selectedrow, 3);
                model.setValueAt(status, selectedrow, 4);
  
            }
          
            else{
                JOptionPane.showMessageDialog(null,"Value already exist","Data Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        catch(HeadlessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Update box data reading error","Data Error",JOptionPane.ERROR_MESSAGE);
        }

    }
    

    
    // function to get the index number of a item based on its time it consumed or the status and input date
    public int CheckTableItem(String item, String status, String date, int index){
        int row = Storeouttable.getRowCount();
        int tableindx = -1;
        //System.out.println(item+" "+status+" "+date);
        for(int i=0; i<row ; i++){
            if (model.getValueAt(i,0).toString().trim().equals(item) && model.getValueAt(i,4).toString().trim().equals(status)
                    && model.getValueAt(i,3).toString().trim().equals(date) && i!= index){
                tableindx++;
                return tableindx;
            }
        }
        return tableindx;
    }
    
    
    public boolean searchIndatabase(int serail, String name, String status){
        Double bf=0.00;
        Double lunch =0.00;
        Double dinner = 0.0;
        try{
            psmt = conn.prepareStatement("select bf, lunch, dinner from storeinout where serial = ? and item = ?");
            psmt.setInt(1, serail);
            psmt.setString(2, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                bf = rs.getDouble(1);
                lunch = rs.getDouble(2);
                dinner = rs.getDouble(3);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch bf,lunch,dinner "
                    + "for seach purpose in storeout table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        
        if(status.equals("Breakfast") && bf > 0.00){
            return true;
        }
        else if(status.equals("Lunch") && lunch > 0.00){
            return true;
        }
        else if(status.equals("Dinner") && dinner > 0.00){
            return true;
        }
        return false;
    }
    
 
    
    // function will show the row value in the update fields
    public void updatefield(){
        int itemcomboindex =-1;
        int statuscomboindex =-1;
        int selectedrow= -1;
        selectedrow = Storeouttable.getSelectedRow();
        int dateserial =0;
        Double actualavailable= 0.0;
        Double available = 0.0;
        Double prevavailable =0.0;
        
        String tablecomboitem = model.getValueAt(selectedrow,0).toString().trim();
        for (int i=0 ; i<updateCombobox.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateItemCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (updateCombobox.getItemAt(i).equals(tablecomboitem)){
                itemcomboindex = i;
                break;
            }
        }
        
        String amount = model.getValueAt(selectedrow,1).toString().trim();
        String remainingamount = model.getValueAt(selectedrow,2).toString().trim();
        String tabledate = model.getValueAt(selectedrow, 3).toString();
            Date date=null;
            int serial =0;
            try{
                date = formatter3.parse(tabledate);
                dateserial = Integer.parseInt(formatter1.format(date));
            }
            catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null, "String to date conversion"
                        + "error in Update field", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        String tablecombostatus = model.getValueAt(selectedrow,4).toString().trim();
        for (int i=0 ; i<updatestatuscombo.getItemCount() ; i++){
            //System.out.println(i+" "+UpdateStatusCombo.getItemAt(i).toString()+" "+tablecomboitem);
            if (updatestatuscombo.getItemAt(i).equals(tablecombostatus)){
                statuscomboindex = i;
                break;
            }
        }
        Double tableamount = Double.parseDouble(amount);
        //dateserial = Integer.parseInt(formatter1.format(showdate));
        //System.out.println(dateserial+" prev "+tablecomboitem);
        //System.out.println(prevavailable+" "+available+" "+amount);
        
        
         //System.out.println(actualavailable);
        if(itemcomboindex >=0 && statuscomboindex >=0){
            updatedatechooser.setDate(date);
            updateamounttxt.setText(amount);
            updateCombobox.setSelectedIndex(itemcomboindex);
            updatestatuscombo.setSelectedIndex(statuscomboindex);
            updateAvailable(dateserial ,tablecomboitem ,selectedrow);
            
            
        }
        else{
            JOptionPane.showMessageDialog(null,"item and status combo box did not mathed with row","Data Showing error",JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(itemcomboindex);
        
    }
    
    
    
    //pass item name based on 
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
    
    
    public void updateAvailable(int dateserial, String itemname, int selectedrow){
        Double available= 0.00;
        Double prevavailable = 0.00;
        available = databaseAvailable(dateserial,itemname);
        prevavailable = countprevAvailableitem(dateserial,itemname);
        
        int totalrow = model.getRowCount();
        Double total = 0.0;
       
        for(int i=0; i<totalrow ; i++){
            //total = total + Double.parseDouble(model.getValueAt(i,1).toString());
            String tabledate = model.getValueAt(i, 3).toString();
            Date date=null;
            int serial =0;
            try{
                date = formatter3.parse(tabledate);
                serial = Integer.parseInt(formatter1.format(date));
            }
            catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null, "String to date conversion"
                        + "error in set remaining", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Double tableamount = Double.parseDouble(model.getValueAt(i, 1).toString());
            String tableitem = model.getValueAt(i, 0).toString();
            if(serial > dateserial && tableitem.equals(itemname) && i!= selectedrow){
                available= available - tableamount;
                
            }
            else if(serial <= dateserial && tableitem.equals(itemname) && i!= selectedrow ){
                prevavailable= prevavailable - tableamount;
                available= available - tableamount;
            } 
            
        }
        
        //System.out.println(itemname+" "+available+" " +" "+prevavailable+" ");
        
        
        
        if(prevavailable < available){
            //System.out.println(prevavailable);
            //prevavailable = prevavailable - jTableavailable();
            if(prevavailable < 0){
                prevavailable =0.0;
            }
            
            //System.out.print(unit);
            updateavailable.setText(dec.format(prevavailable));
        }
        else{
            if(available < 0){
                available =0.0;
            }
            
            updateavailable.setText(dec.format(available));
        }
        
        updateavgprice.setText(dec.format(avg));
    }
    
    
    
    
    public void setAvailable(int dateserial, String itemname){
        Double available= 0.00;
        Double prevavailable = 0.00;
        available = databaseAvailable(dateserial,itemname);
        prevavailable = countprevAvailableitem(dateserial,itemname);
        
        int totalrow = model.getRowCount();
        Double total = 0.0;
       
        for(int i=0; i<totalrow ; i++){
            //total = total + Double.parseDouble(model.getValueAt(i,1).toString());
            String tabledate = model.getValueAt(i, 3).toString();
            Date date=null;
            int serial =0;
            try{
                date = formatter3.parse(tabledate);
                serial = Integer.parseInt(formatter1.format(date));
            }
            catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null, "String to date conversion"
                        + "error in set remaining", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Double tableamount = Double.parseDouble(model.getValueAt(i, 1).toString());
            String tableitem = model.getValueAt(i, 0).toString();
            if(serial > dateserial && tableitem.equals(itemname)){
                available= available - tableamount;
            }
            else if(serial <= dateserial && tableitem.equals(itemname)){
                prevavailable= prevavailable - tableamount;
                available= available - tableamount;
            }    
        }
        
        System.out.println(available+" prev"+prevavailable);
        
        if(prevavailable < available){
            if(prevavailable < 0){
                prevavailable = 0.00;
            }
            int comboindex =-1;
            comboindex = insertCombobox.getSelectedIndex();  
            String unit = setInsertunit(comboindex);
            //insertunit.setText(unit);
            
            insertavailabletxt.setText(dec.format(prevavailable));
        }
        else{
            if(available < 0){
                available = 0.00;
            }
            
            //System.out.println(dec.format(prevavailable).toString()+unit);
            insertavailabletxt.setText(dec.format(available));
        }
        insertavgprice.setText(dec.format(avg));
    }
    
    
    // count the available amount of a item from arraylist 
    public Double countprevAvailableitem(int serial, String item){
        //System.out.println(serial+" prev "+item);
        Double available= 0.00;
        avg =0.00;
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price from storeinout where item =? and serial <= ?");
            psmt.setString(1, item);
            psmt.setInt(2, serial);
            rs = psmt.executeQuery();
            int count = 0;
            while(rs.next()){
                available =available+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                if(rs.getDouble(5) != 0 && rs.getDouble(1) != 0){
                    avg=avg+(rs.getDouble(5)/rs.getDouble(1));
                    count++;
                }
            }
            avg=avg/count;
            //System.out.println(count+" "+avg);
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count previous "
                    + "available", "Data fetch error", JOptionPane.ERROR_MESSAGE);
           
        }
        
        //System.out.println(available);
        return available;
    }
    
    public Double databaseAvailable(int serial, String item){
        
        Double available= 0.00;
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner from storeinout where item =? ");
            psmt.setString(1, item);
            rs = psmt.executeQuery();
            while(rs.next()){
                available =available+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
            }
            //System.out.println(available);
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count toatl available"
                    + "", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        return available;
    }
    
    
   
    
    
    // set the unit in the amount part 
    public String setInsertunit(int index){
        String name = comboIndextoitem(index);
        try{
            psmt = conn.prepareStatement("select unit from item where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                return (rs.getString(1));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, ""
                    + "failed to set the unit of for item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        return "";
    }
    
    public String setupdatetunit(int index){
        String name = updateCombobox.getItemAt(index);
        try{
            psmt = conn.prepareStatement("select unit from item where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            //System.out.print(name+" "+index);
            while(rs.next()){
                return (rs.getString(1));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, ""
                    + "failed to set the unit of for item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        return "";
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
        Storeouttable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        Storeouttable.getTableHeader().setOpaque(false);
        Storeouttable.getTableHeader().setBackground(new Color(32,136,203));
        Storeouttable.getTableHeader().setForeground(new Color(255,255,255));
        Storeouttable.setRowHeight(25);
        
        insertavailabletxt.setEditable(false);
        updateavailable.setEditable(false);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        Storeouttable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        Storeouttable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        Storeouttable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        Storeouttable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        Storeouttable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
    }
    
    
    
    public void insert(){
        int totalrow = model.getRowCount();
        Double amount = 0.00;
        String status = "";
        String itemname="";
        int serial=0;
        String tabledate = "";
        Date date;
        double bf=0.0;
        double lunch =0.0;
        Double dinner = 0.0;
        int databaseserial = 0;
        for( int i=0; i<totalrow; i++){
           itemname = model.getValueAt(i, 0).toString();
           amount = Double.parseDouble(model.getValueAt(i, 1).toString());
           tabledate = model.getValueAt(i, 3).toString();
           status = model.getValueAt(i, 4).toString();
           databaseserial = 0;
           try{
               date = formatter3.parse(tabledate);
               serial = Integer.parseInt(formatter1.format(date));
           }
           catch(NumberFormatException | ParseException e){
               JOptionPane.showMessageDialog(null,"failed to conver"
                       + "t data while inserting","Data Error",JOptionPane.ERROR_MESSAGE);
           }
           
           try{
            psmt = conn.prepareStatement("select serial from storeinout where serial = ? and item = ?");
            psmt.setInt(1, serial);
            psmt.setString(2, itemname);
            rs = psmt.executeQuery();
            
            while(rs.next()){
               databaseserial = rs.getInt(1);
               //System.out.print(databaseserial);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch bf,lunch,dinner "
                    + "for seach purpose in storeout table", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(databaseserial);
        
        if(databaseserial != 0){
            if( status.equals("Breakfast")){
                try{
                    psmt = conn.prepareStatement("UPDATE storeinout SET bf= ? WHERE serial = ? and item = ?");
                    psmt.setDouble(1, amount);
                    psmt.setInt(2, serial);
                    psmt.setString(3, itemname);
                    psmt.execute();
                    psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if( status.equals("Lunch")){
                try{
                    psmt = conn.prepareStatement("UPDATE storeinout SET lunch= ? WHERE serial = ? and item = ? ");
                    psmt.setDouble(1, amount);
                    psmt.setInt(2, serial);
                    psmt.setString(3, itemname);
                    psmt.execute();
                    psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            else if( status.equals("Dinner")){
                try{
                    psmt = conn.prepareStatement("UPDATE storeinout SET dinner= ? WHERE serial = ? and item = ?");
                    psmt.setDouble(1, amount);
                    psmt.setInt(2, serial);
                    psmt.setString(3, itemname);
                    psmt.execute();
                    psmt.close();
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data updating errpr"
                            + "in save&exit button", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            
            
        }
        else{
            if(status.equals("Breakfast")){
                try{
                psmt = conn.prepareStatement("insert into storeinout (serial,item,inamount,price,memono,bf,lunch,dinner) values (?,?,0,0,'###',?,0,0)");
                psmt.setInt(1,serial);
                psmt.setString(2, itemname);
                psmt.setDouble(3, amount);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Insert error", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(status.equals("Lunch")){
                try{
                psmt = conn.prepareStatement("insert into storeinout (serial,item,inamount,price,memono,bf,lunch,dinner) values (?,?,0,0,'###',0,?,0)");
                psmt.setInt(1,serial);
                psmt.setString(2, itemname);
                psmt.setDouble(3, amount);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Insert error", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(status.equals("Dinner")){
                try{
                psmt = conn.prepareStatement("insert into storeinout (serial,item,inamount,price,memono,bf,lunch,dinner) values (?,?,0,0,'###',0,0,?)");
                psmt.setInt(1,serial);
                psmt.setString(2, itemname);
                psmt.setDouble(3, amount);
                psmt.execute();
                psmt.close();
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Insert error", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        
        }
        
    }
    
    
    //function for delete a selected table row
    public void DeleteTableRow(){
        int selectedrow = Storeouttable.getSelectedRow();
        int serial = 0 ;
        String itemname;
        String stdate;
        String status;
        Date date;
        int index;
        if(selectedrow >= 0){
            try{
            date = formatter3.parse(model.getValueAt(selectedrow,3).toString());
            serial = Integer.parseInt(formatter1.format(date));
        }
        catch(NumberFormatException | ParseException e){
            JOptionPane.showMessageDialog(null,"Date convertion failed while deleting row"
                    + "","Date Error",JOptionPane.ERROR_MESSAGE);
        }
        itemname = model.getValueAt(selectedrow,0).toString();
        status = model.getValueAt(selectedrow, 4).toString();
      
       
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete"
                + " The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION){
            tablemodel = (DefaultTableModel) Storeouttable.getModel();
            tablemodel.removeRow(selectedrow);
        }
            
        
        }
        else{
            JOptionPane.showMessageDialog(null,"No Row is selected","Showing "
                    + "error while Updating data from textfield",JOptionPane.ERROR_MESSAGE);
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

        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Insertlabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        insertdatechooser = new com.toedter.calendar.JDateChooser();
        insertCombobox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        insertamounttxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        insertStatusCombo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        insertavailabletxt = new javax.swing.JTextField();
        insertbtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        insertavgprice = new javax.swing.JTextField();
        insertunit = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        updateCombobox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        updateamounttxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        updatestatuscombo = new javax.swing.JComboBox<>();
        updateavailable = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        updatebtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updatedatechooser = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        updateavgprice = new javax.swing.JTextField();
        updateunit = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Storeouttable = new javax.swing.JTable();
        saveAndexitbtn = new javax.swing.JButton();

        jLabel13.setText("jLabel13");

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
        insertamounttxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertamounttxtActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Status");

        insertStatusCombo.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertStatusCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        insertStatusCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertStatusComboActionPerformed(evt);
            }
        });

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

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Avg Price");

        insertavgprice.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        insertunit.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertunit.setForeground(new java.awt.Color(255, 0, 0));
        insertunit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        insertunit.setText("Unit");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Insertlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3))
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(insertunit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(insertamounttxt)
                            .addComponent(insertavailabletxt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(insertStatusCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(insertbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addComponent(insertavgprice))
                    .addComponent(insertCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Insertlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(insertCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertamounttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(insertStatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(insertavgprice, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(insertavailabletxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(insertunit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel7.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        jLabel7.setText("UPDATE");

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Date");

        updateCombobox.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updateCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateComboboxActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Item ");

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Amount");

        updateamounttxt.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updateamounttxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateamounttxtActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Status");

        updatestatuscombo.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatestatuscombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Breakfast", "Lunch", "Dinner" }));
        updatestatuscombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatestatuscomboActionPerformed(evt);
            }
        });

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

        deleteBtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash.png"))); // NOI18N
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        updatedatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatedatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                updatedatechooserPropertyChange(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Bodoni MT", 0, 16)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Avg Price");

        updateavgprice.setFont(new java.awt.Font("Bodoni MT", 0, 16)); // NOI18N

        updateunit.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updateunit.setForeground(new java.awt.Color(255, 51, 51));
        updateunit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateunit.setText("Unit");

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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateunit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateavailable)
                    .addComponent(updateamounttxt)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updateavgprice)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(updateCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatestatuscombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(updateCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(updatestatuscombo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(updateamounttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateavailable, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateavgprice, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateunit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
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
        Storeouttable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        Storeouttable.setShowVerticalLines(false);
        Storeouttable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StoreouttableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Storeouttable);

        saveAndexitbtn.setBackground(new java.awt.Color(204, 204, 255));
        saveAndexitbtn.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        saveAndexitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        saveAndexitbtn.setText("Save & Exit");
        saveAndexitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAndexitbtnActionPerformed(evt);
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
            .addComponent(saveAndexitbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAndexitbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_insertdatechooserPropertyChange
        // TODO add your handling code here:
        Date date = insertdatechooser.getDate();
        int dateserial =-1;
        String itemname = null;
        
        if ( date != null && ComboSelectedItem()){
            dateserial = Integer.parseInt(formatter1.format(date));
            itemname = insertCombobox.getSelectedItem().toString();
            
            setAvailable(dateserial, itemname);
            
            int comboindex =-1;
            comboindex = insertCombobox.getSelectedIndex();  
            String unit = setInsertunit(comboindex);
            insertunit.setText(unit);
        }
        insertamounttxt.requestFocusInWindow();
        
    }//GEN-LAST:event_insertdatechooserPropertyChange
    
    private void insertComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertComboboxActionPerformed
        // TODO add your handling code here:

        if ( flag ==1 ){
            String itemname="";
            Date date = insertdatechooser.getDate();
            int dateserial =-1;
            itemname = insertCombobox.getSelectedItem().toString();
            dateserial = Integer.parseInt(formatter1.format(date));
            setAvailable(dateserial, itemname);
            
        int comboindex =-1;
        comboindex = insertCombobox.getSelectedIndex();  
        String unit = setInsertunit(comboindex);
        insertunit.setText(unit);
        }
        
        insertamounttxt.requestFocusInWindow();
    }//GEN-LAST:event_insertComboboxActionPerformed

    private void insertbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbtnActionPerformed
        // TODO add your handling code here:
        String name = insertCombobox.getSelectedItem().toString();
        String status = insertStatusCombo.getSelectedItem().toString();
        String amount = insertamounttxt.getText().trim();
        String remaining = insertavailabletxt.getText().trim();
        Date date = insertdatechooser.getDate();
        
        setOuttable(name,status,amount,remaining,date);
        insertamounttxt.requestFocusInWindow();
    }//GEN-LAST:event_insertbtnActionPerformed

    private void StoreouttableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StoreouttableMouseClicked
        // TODO add your handling code here:
        updatefield();
        updateamounttxt.requestFocusInWindow();
    }//GEN-LAST:event_StoreouttableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
//        String name = updateCombobox.getSelectedItem().toString();
//        String status = updatestatuscombo.getSelectedItem().toString();
//        String amount = updateamounttxt.getText().trim();
//        String remaining = updateavailable.getText().trim();
//        Date date = updatedatechooser.getDate();
       
        setUpdatevalue();
        Storeouttable.clearSelection();
    }//GEN-LAST:event_updatebtnActionPerformed

    private void updateComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateComboboxActionPerformed
        // TODO add your handling code here:
        int row =-1;
        row = Storeouttable.getSelectedRow();
        String itemname="";
        Date date = null;
        date = updatedatechooser.getDate();
        int dateserial =-1;
        itemname = updateCombobox.getSelectedItem().toString();
        dateserial = Integer.parseInt(formatter1.format(date));
        
        if ( flag ==1 ){
            updateAvailable(dateserial ,itemname ,row);
            int comboindex1 =-1;
            comboindex1 = updateCombobox.getSelectedIndex();
            String unit1 = setupdatetunit(comboindex1);
            updateunit.setText(unit1);
            
            updateamounttxt.requestFocusInWindow();
        }
        
    }//GEN-LAST:event_updateComboboxActionPerformed

    private void saveAndexitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAndexitbtnActionPerformed
        // TODO add your handling code here:
        //System.out.println(model.getRowCount());
        if( model.getRowCount() > 0){
            int responce = JOptionPane.showConfirmDialog(this,"Do you want to save the data ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION){
                insert();
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"No item is inserted on the table","Table item not found",JOptionPane.ERROR_MESSAGE);
        }
        
        
    }//GEN-LAST:event_saveAndexitbtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        DeleteTableRow();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updatedatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_updatedatechooserPropertyChange
        // TODO add your handling code here:
        
        int row =-1;
        row = Storeouttable.getSelectedRow();
        String itemname="";
        Date date = null;
        date = updatedatechooser.getDate();
        int dateserial =-1;
     
        if ( flag ==1 && date != null){
            itemname = updateCombobox.getSelectedItem().toString();
            dateserial = Integer.parseInt(formatter1.format(date));
            
            updateAvailable(dateserial ,itemname ,row);
            
            int comboindex1 =-1;
            comboindex1 = updateCombobox.getSelectedIndex();
            String unit1 = setupdatetunit(comboindex1);
            updateunit.setText(unit1);
            updateamounttxt.requestFocusInWindow();
        }
        
    }//GEN-LAST:event_updatedatechooserPropertyChange

    private void insertStatusComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertStatusComboActionPerformed
        // TODO add your handling code here:
        insertamounttxt.requestFocusInWindow();
    }//GEN-LAST:event_insertStatusComboActionPerformed

    private void updatestatuscomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatestatuscomboActionPerformed
        // TODO add your handling code here:
        updateamounttxt.requestFocusInWindow();
    }//GEN-LAST:event_updatestatuscomboActionPerformed

    private void insertamounttxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertamounttxtActionPerformed
        // TODO add your handling code here:
        insertbtn.doClick();
    }//GEN-LAST:event_insertamounttxtActionPerformed

    private void updateamounttxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateamounttxtActionPerformed
        // TODO add your handling code here:
        
        updatebtn.doClick();
    }//GEN-LAST:event_updateamounttxtActionPerformed

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
    private javax.swing.JButton deleteBtn;
    private javax.swing.JComboBox<String> insertCombobox;
    private javax.swing.JComboBox<String> insertStatusCombo;
    private javax.swing.JTextField insertamounttxt;
    private javax.swing.JTextField insertavailabletxt;
    private javax.swing.JTextField insertavgprice;
    private javax.swing.JButton insertbtn;
    private com.toedter.calendar.JDateChooser insertdatechooser;
    private javax.swing.JLabel insertunit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JButton saveAndexitbtn;
    private javax.swing.JComboBox<String> updateCombobox;
    private javax.swing.JTextField updateamounttxt;
    private javax.swing.JTextField updateavailable;
    private javax.swing.JTextField updateavgprice;
    private javax.swing.JButton updatebtn;
    private com.toedter.calendar.JDateChooser updatedatechooser;
    private javax.swing.JComboBox<String> updatestatuscombo;
    private javax.swing.JLabel updateunit;
    // End of variables declaration//GEN-END:variables
}
