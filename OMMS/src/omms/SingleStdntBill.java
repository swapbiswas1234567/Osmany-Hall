/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajmir
 */
public class SingleStdntBill {
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DecimalFormat dec;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    
    Map<Integer, DailyMealState> mealsheet;
    Map<Integer, Double> bill;
    
    SingleStdntBill(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        
        mealsheet = new HashMap<>();
        bill = new HashMap<>();
        
        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
    }
    
    public Map<Integer, DailyMealState> meal(int fromdate, int todate, int hallid){
        //System.out.println(fromdate+" "+todate+hallid);
        try{
            psmt = conn.prepareStatement("select date,breakfast,lunch,dinner,bfgrp,lunchgrp,dinnergrp from mealsheet where date BETWEEN ? and ? and hallid=? ORDER by date");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            psmt.setInt(3, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                mealsheet.put(rs.getInt(1), new DailyMealState(rs.getInt(2), rs.getInt(3), rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7)));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        return mealsheet; 
    }
    
    
    public Double monthlybill(Map<Integer, BillAmount> billmap, int fromdate, int todate,int hallid){
        Double bill=0.0 , bfbill=0.0, lunchbill=0.0, dinnerbill=0.0;
        int key=0, bf=0,lunch=0,dinner=0,dateserial=0, bfgrp=0, lunchgrp=0, dinnergrp=0;
        Date date=null;
        String strdate= "";
        
        try{
            psmt = conn.prepareStatement("SELECT *from mealsheet where date between ? and ? and hallid=? order by date");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            psmt.setInt(3, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                //mealsheet.put(rs.getInt(1), new DailyMealState(rs.getInt(2), rs.getInt(3), rs.getInt(4)));
                bf =rs.getInt(3);
                lunch = rs.getInt(4);
                dinner = rs.getInt(5);
                dateserial= rs.getInt(2);
                bfgrp= rs.getInt(6);
                lunchgrp = rs.getInt(7);
                dinnergrp = rs.getInt(8);
                
                try{
                    date = formatter1.parse(Integer.toString(dateserial));
                    strdate = formatter.format(date);
                }catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Failed to convert date monthly bill calculation", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return 0.00;
                }
                
                try{
                    bfbill = billmap.get(dateserial).bfbill.get(bfgrp);
                    lunchbill = billmap.get(dateserial).lunchbill.get(lunchgrp);
                    dinnerbill = billmap.get(dateserial).dinnerbill.get(dinnergrp);
                    
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "No store out exist for "+strdate+" but "
                            + "mealsheet exist for hallid"+hallid, "bill calculation error", JOptionPane.ERROR_MESSAGE);
                    return 0.00;
                }
                //System.out.println(dateserial+" "+bfbill+" "+lunchbill+" "+dinnerbill);
                bill =bill+ (bf*bfbill)+(lunch*lunchbill)+(dinner*dinnerbill);
                //System.out.println(bill);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        return bill;
    }
    
    
    public Double monthlytmpfoodbill(int fromdate, int todate, int hallid){
        Double tmpbill=0.0;
        try{
            psmt = conn.prepareStatement("select sum(bill) from tempfood where dateserial BETWEEN ? and ? and hallid=?");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            psmt.setInt(3, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                tmpbill = rs.getDouble(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return tmpbill;
    }
    
    
    public Double previousbill(int hallid){
        Double prevbill=0.0;
        try{
            psmt = conn.prepareStatement("select totaldue from totalbill where hallid=?");
            psmt.setInt(1, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                prevbill = rs.getDouble(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        return prevbill;
    }
    
}
