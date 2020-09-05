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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class DailyAvgBill {
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DecimalFormat dec;
    
    
    Map<Integer, BillAmount> billMap;
    Map<String, PreviousValue> previous;
    Map<Integer, Double> tmpfood;
    
    DailyAvgBill(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        billMap = new HashMap<>();
        previous = new HashMap<>();
        tmpfood = new HashMap<>();
        dec = new DecimalFormat("#0.000");
    }
    
    public Map<Integer, BillAmount> setbill(int fromdate, int todate){
          allstoreditem(fromdate,todate);
          
          for(Map.Entry mapelement : previous.entrySet()){
              String key = (String) mapelement.getKey();
              //System.out.println(key+" "+previous.get(key).amount);
              previousavgprice(20200720,key);  // calculate the previous avg price
          }
          
         //System.out.println(" "+previous.get("Rice").avgprice+" "+previous.get("Rice").prevavailable);
          storeditemdailycost(20200720,20200828);  //calculate stored bill
//          System.out.println(billMap.get(20200807).bfbill+" lunch "+
//                  billMap.get(20200807).lunchbill.get(0)
//                  +" dinner "+billMap.get(20200807).dinnerbill.get(0));
          nonstoreddailycost(20200720,20200828); //add nonstored bill
//          for(int i=0; i<billMap.get(20200807).item.size(); i++){
//              System.out.println(billMap.get(20200807).item.get(i).name+" "+billMap.get(20200807).item.get(i).avgprice+" "+
//                      billMap.get(20200807).item.get(i).bfamount+" "+billMap.get(20200807).item.get(i).dinneramount);
//          }
        System.out.println(billMap.get(20200820).bfbill+" "+billMap.get(20200807).lunchbill);
        perheadmeal(20200720,20200828); // divide total bill by total meal on
        System.out.println(billMap.get(20200820).bfbill+" "+billMap.get(20200807).lunchbill);
          return billMap;
          
    }
    

    
    public void allstoreditem(int fromdate, int todate){
        try{
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select DISTINCT item from storeinout where serial BETWEEN ? and ?");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            while(rs.next()){
                previous.put(rs.getString(1), new PreviousValue(0.0,0.0));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetchname of stored item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void nonstoreddailycost(int fromdate, int todate){
        Double amount=0.0, price=0.0;
        String name="", state="";
        int date=0,grp=0,len=-1;
       // System.out.println(fromdate+" "+todate);
        try{
            psmt = conn.prepareStatement("select serial, amount, price, state,name,grp from nonstoreditem where serial >= ? and serial <= ? order by serial,name");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            while(rs.next()){
                date = rs.getInt(1);
                amount = rs.getDouble(2);
                price = rs.getDouble(3);
                state = rs.getString(4);
                name = rs.getString(5);
                grp = rs.getInt(6);
                
                if(!billMap.containsKey(date)){
                    billMap.put(date, new BillAmount());
                }
                
                if (state.equals("breakfast")){
                    if(!billMap.get(date).bfbill.isEmpty() ){
                        price = billMap.get(date).bfbill.get(grp) + price;
                        billMap.get(date).bfbill.set(grp, price);
                        //billMap.get(date).item.add(name)
                        len =billMap.get(date).item.size()-1;
                        if( billMap.get(date).item.get(len).equals(name)){
                            billMap.get(date).item.get(len).bfamount = amount;
                            billMap.get(date).item.get(len).bfgrp = grp;
                            billMap.get(date).item.get(len).avgprice += price;
                        }
                        else{
                            billMap.get(date).item.add(new DailyItem(name,amount,0.0,0.0,price,grp,0,0));
                        }
                    }
                    else{
                        //System.out.println(billMap.get(date).bfbill+" "+grp+" "+date+" cal "+price);
                        billMap.get(date).bfbill.add(price);
                        billMap.get(date).item.add(new DailyItem(name,amount,0.0,0.0,price,grp,0,0));
                        //System.out.println(billMap.get(date).bfbill+" "+date);
                    }
                }
                else if (state.equals("lunch")){
                    
                    if(!billMap.get(date).lunchbill.isEmpty()){
                        //System.out.println(billMap.get(date).lunchbill.size()+" "+grp+" "+date);
                        price = billMap.get(date).lunchbill.get(grp) + price;
                        billMap.get(date).lunchbill.set(grp, price);
                        len =billMap.get(date).item.size()-1;
                        //System.out.println(len);
                        if( billMap.get(date).item.get(len).equals(name)){
                            billMap.get(date).item.get(len).lunchamount = amount;
                            billMap.get(date).item.get(len).lunchgrp = grp;
                            billMap.get(date).item.get(len).avgprice += price;
                        }
                        else{
                            billMap.get(date).item.add(new DailyItem(name,0.0,amount,0.0,price,0,grp,0));
                        }
                    }
                    else{
                        billMap.get(date).lunchbill.add(price);
                        billMap.get(date).item.add(new DailyItem(name,0.0,amount,0.0,price,0,grp,0));
                    }
                }
                else if (state.equals("dinner")){
                    if( !billMap.get(date).dinnerbill.isEmpty()){
                        price = billMap.get(date).dinnerbill.get(grp) + price;
                        billMap.get(date).dinnerbill.add(price);
                        len =billMap.get(date).item.size()-1;
                        //System.out.println(len);
                        if( billMap.get(date).item.get(len).equals(name)){
                            billMap.get(date).item.get(len).dinneramount = amount;
                            billMap.get(date).item.get(len).dinnergrp = grp;
                            billMap.get(date).item.get(len).avgprice += price;
                        }
                        else{
                            billMap.get(date).item.add(new DailyItem(name,0.0,0.0,amount,price,0,0,grp));
                        }
                        
                    }
                    else{
                        billMap.get(date).dinnerbill.add(price);
                        billMap.get(date).item.add(new DailyItem(name,0.0,0.0,amount,price,0,0,grp));
                    }
                }
                //System.out.println(date);
            }
            psmt.close();
            rs.close();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Failed to calculate nonstored cost", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
    
    public void storeditemdailycost(int fromdate,int todate){
        Double prevavailable=0.00, avgprice=0.00,totalamount=0.0, totalprice=0.0, bfbill=0.00, lunchbill=0.00, dinnerbill=0.00;
        String name="", state="";
        int date=0, bfgrp=0,lunchgrp=0, dinnergrp=0;
        
        
        try{
            psmt = conn.prepareStatement("select date,state,serial from grp where date >= ? and date <= ? ORDER by date,state, serial");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            
            while(rs.next()){
                //billMap.get(rs.getInt(1)).
                date = rs.getInt(1);
                //System.out.println(date);
                //System.out.println(billMap.get(date).bfbill);
                if( !billMap.containsKey(date)){
                    billMap.put(date, new BillAmount());
                    billMap.get(date).bfbill.add(0.0);
                    billMap.get(date).lunchbill.add(0.0);
                    billMap.get(date).dinnerbill.add(0.0);
                }
                
                state = rs.getString(2);
                if(state.equals("breakfast")){
                   
                    billMap.get(date).bfbill.add(0.0);
                }
                else if(state.equals("lunch")){
                    
                    billMap.get(date).lunchbill.add(0.0);
                }
                else if(state.equals("dinner")){
                    billMap.get(date).dinnerbill.add(0.0);
                }
            }
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to inset date in arraylist", "Data fetch error", JOptionPane.ERROR_MESSAGE);
           
        }
        //System.out.println(billMap.get(20200820).dinnerbill);
        
        
        
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price,item,serial,bfgrp,lunchgrp,dinnergrp from storeinout where serial >= ? and serial<= ? order by serial");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    name = rs.getString(6);
                    prevavailable = previous.get(name).prevavailable;
                    avgprice = previous.get(name).avgprice;
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "name not found in hashmap", "stored daily calculation", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                bfgrp = rs.getInt(8);
                lunchgrp = rs.getInt(9);
                dinnergrp = rs.getInt(10);
                
                totalprice =(prevavailable*avgprice)+rs.getDouble(5);
                totalamount = prevavailable + rs.getDouble(1);
                avgprice = totalprice/totalamount;
                //System.out.println(avgprice+" "+totalprice+" "+totalamount);
                previous.get(name).prevavailable = totalamount-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                previous.get(name).avgprice = avgprice;
                
                bfbill = rs.getDouble(2)*avgprice;
                lunchbill = rs.getDouble(3)*avgprice;
                dinnerbill = rs.getDouble(4) * avgprice;
                //System.out.println(bfbill+" "+lunchbill+" "+dinnerbill);
                date = rs.getInt(7);
                if(billMap.containsKey(date)){
                    bfbill = billMap.get(date).bfbill.get(bfgrp)+bfbill;
                    billMap.get(date).bfbill.set(bfgrp, bfbill);
                }
                else{
                   billMap.put(date, new BillAmount());
                   billMap.get(date).bfbill.add(bfbill);
                   billMap.get(date).lunchbill.add(lunchbill);
                   billMap.get(date).dinnerbill.add(dinnerbill);
                   
                }
                billMap.get(date).item.add(new DailyItem(name,rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),avgprice,bfgrp,lunchgrp,dinnergrp));
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate dailybill", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void perheadmeal(int fromdate, int todate){
        String strdate="";
        int grp=-1, date=0,meal=0;
        Double price=0.0;
        
        try{
            psmt = conn.prepareStatement("select date,sum(breakfast),bfgrp from mealsheet where date >= ? and date <= ? GROUP by date,bfgrp order by date asc, bfgrp DESC");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    grp = rs.getInt(3);
                    date = rs.getInt(1);
                    meal = rs.getInt(2);
                    //System.out.println(rs.getInt(1)+" "+grp+" "+meal+" "+billMap.get(date).bfbill);
                    if( grp == 0 && meal != 0){
                        price = billMap.get(date).bfbill.get(grp)/meal;
                        billMap.get(date).bfbill.set(grp, price);
                    }
                    else if(meal !=0){
                        price = billMap.get(date).bfbill.get(grp) + billMap.get(date).bfbill.get(0);
                        //System.out.println(price);
                        price= price/meal;
                        billMap.get(date).bfbill.set(grp, price);
                    }
                            
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null,rs.getInt(1)+" has no store out", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate bf perhead meal", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        try{
            psmt = conn.prepareStatement("select date,sum(lunch),lunchgrp from mealsheet where date >= ? and date <= ? GROUP by date,lunchgrp order by date asc, lunchgrp DESC");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    grp = rs.getInt(3);
                    date = rs.getInt(1);
                    meal = rs.getInt(2);
                    //System.out.println(rs.getInt(1)+" "+grp+" "+meal+" "+billMap.get(date).lunchbill);
                    if( grp == 0 && meal != 0){
                        price = billMap.get(date).lunchbill.get(grp)/meal;
                        billMap.get(date).lunchbill.set(grp, price);
                    }
                    else if(meal !=0){
                        price = billMap.get(date).lunchbill.get(grp) + billMap.get(date).lunchbill.get(0);
                        //System.out.println(price+" "+price/meal);
                        price= price/meal;
                        billMap.get(date).lunchbill.set(grp, price);
                        System.out.println(billMap.get(date).lunchbill);
                    }
                            
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null,rs.getInt(1)+" has no store out", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate lunch per head meal", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        try{
            psmt = conn.prepareStatement("select date,sum(dinner),dinnergrp from mealsheet where date >= ? and date <= ? GROUP by date,dinnergrp order by date asc, dinnergrp DESC");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    grp = rs.getInt(3);
                    date = rs.getInt(1);
                    meal = rs.getInt(2);
                    //System.out.println(rs.getInt(1)+" "+grp+" "+meal+" "+billMap.get(date).dinnerbill);
                    if( grp == 0 && meal != 0){
                        price = billMap.get(date).dinnerbill.get(grp)/meal;
                        billMap.get(date).dinnerbill.set(grp, price);
                    }
                    else if(meal !=0){
                        price = billMap.get(date).dinnerbill.get(grp) + billMap.get(date).dinnerbill.get(0);
                        //System.out.println(price);
                        price= price/meal;
                        billMap.get(date).dinnerbill.set(grp, price);
                    }
                            
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null,rs.getInt(1)+" has no store out", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate lunch per head meal", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    }
    
    
    
    public void previousavgprice(int dateserial, String item){
        Double prevavailable=0.00 , totalprice=0.00, totalamount=0.00, avg =0.00;
        
        
        
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price,serial from storeinout where item =? and serial < ? order by serial");
            psmt.setString(1, item);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            
            while(rs.next()){
                
                //available =available+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                //System.out.println(prevavailable*avg+" int amount "+rs.getDouble(5));
                totalprice = (prevavailable*avg)+(rs.getDouble(5));
                totalamount = prevavailable+rs.getDouble(1);
                avg = totalprice/totalamount;
               //System.out.println(totalprice+" "+totalamount);
                prevavailable =prevavailable+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
            }
            //System.out.println(totalamount +" "+totalprice);
            previous.get(item).prevavailable = prevavailable;
            previous.get(item).avgprice = avg;
            
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to count previous "
                    + "available", "Data fetch error", JOptionPane.ERROR_MESSAGE);
           
        }
    }
    
    
  
    
    
    
    
    
    public Map<Integer, Double> tmpfoodbill(int fromdate, int todate, int hallid){
        
        try{
            psmt = conn.prepareStatement("select bill,dateserial from tempfood where dateserial BETWEEN ? and ? and hallid=?");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            psmt.setInt(3, hallid);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
               tmpfood.put(rs.getInt(2), rs.getDouble(1));
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate tmpfood price", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        return tmpfood;
    }
    
    
}
