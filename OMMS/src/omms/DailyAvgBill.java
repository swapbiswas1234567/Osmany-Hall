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
              calculateprevious(fromdate,key);  // calculate the previous avg price
          }
          
         // System.out.println(previous.get("Rice").amount);
          calculatestoreddailybill(fromdate,todate);  //calculate stored bill
          calculatenonstoreddailybill(fromdate,todate); //add nonstored bill
//          for(int i=0; i<billMap.get(20200813).item.size(); i++){
//              System.out.println(billMap.get(20200813).item.get(i).name+" "+billMap.get(20200813).item.get(i).avgprice+" "+
//                      billMap.get(20200813).item.get(i).bfamount+" "+billMap.get(20200813).item.get(i).dinneramount);
//          }
          //System.out.println(billMap.get(todate).bf);
          calculateavg(fromdate,todate); // divide total bill by total meal on
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
                previous.put(rs.getString(1), new PreviousValue(0.0,0.0,0.0));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetchname of stored item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void calculateavg(int fromserial, int toserial){
        Double avg=0.00,  bf=0.00,lunch=0.0,dinner=0.0;
        String name="";
        int date=0;
        
        try{
            psmt = conn.prepareStatement("select sum(breakfast),sum(lunch),sum(dinner),date from mealsheet where date BETWEEN ? and ? GROUP by date");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    date=rs.getInt(4);
                    //System.out.println(date+" "+billMap.get(date).bf);
                    bf=billMap.get(date).bf/rs.getInt(1);
                    //System.out.println(date+" "+billMap.get(date).bf+" "+rs.getInt(1));
                    lunch=billMap.get(date).lunch/rs.getInt(2);
                    dinner=billMap.get(date).dinner/rs.getInt(3);
                    billMap.put(date, new BillAmount(bf,lunch,dinner));
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null,date+" has no store out", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate dailybill", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    
     public void calculatenonstoreddailybill(int fromserial, int toserial){
        Double price=0.0, amount=0.0;
        String state="",name="";
        int date=0;
        
        try{
            psmt = conn.prepareStatement("select serial, amount, price, state,name from nonstoreditem where serial BETWEEN ? and ? order by serial, name");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    int len=-1;
                    date=rs.getInt(1);
                    state = rs.getString(4);
                    name= rs.getString(5);
                    price=rs.getDouble(3);
                    amount = rs.getDouble(2);
                    //System.out.println(name+" "+amount+" "+state);
                    switch (state) {
                        case "breakfast":
                            if(billMap.containsKey(date)){
                                //System.out.println(billMap.get(date).bf+" "+price);
                                billMap.get(date).bf += price;
                                //System.out.println(billMap.get(date).bf);
                                len=billMap.get(date).item.size()-1;
                                //System.out.println(billMap.get(date).item.get(len).name);
                                if(billMap.get(date).item.get(len).name.equals(name)){
                                    billMap.get(date).item.get(len).bfamount=amount;
                                    billMap.get(date).item.get(len).avgprice += price;
                                    //System.out.println("Called");
                                }
                                else if( !billMap.get(date).item.get(len).name.equals(name) ){
                                    billMap.get(date).item.add(new DailyItem(name,amount,0.0,0.0,price));
                                    //System.out.println("Called");
                                }
                            }
                            else{
                                
                                billMap.put(date, new BillAmount(price,0.00,0.00));
                                billMap.get(date).item.add(new DailyItem(name,amount,0.0,0.0,price));
                            }
                            //billMap.get(date).item.add(new DailyItem())
                            break;
                        case "lunch":
                            if(billMap.containsKey(date)){
                                billMap.get(date).lunch += price;
                                len=billMap.get(date).item.size()-1;
                                if(billMap.get(date).item.get(len).name.equals(name)){
                                    billMap.get(date).item.get(len).lunchamount =amount;
                                    billMap.get(date).item.get(len).avgprice += price;
                                }
                                else if( !billMap.get(date).item.get(len).name.equals(name) ){
                                    billMap.get(date).item.add(new DailyItem(name,0.00,amount,0.0,price));
                                    //System.out.println("Called");
                                }
                            }
                            else{
                                billMap.put(date, new BillAmount(price,0.00,0.00));
                                billMap.get(date).item.add(new DailyItem(name,0.00,amount,0.0,price));
                            }
                            break;
                        case "dinner":
                            if(billMap.containsKey(date)){
                                billMap.get(date).dinner += price;
                                len=billMap.get(date).item.size()-1;
                                if(billMap.get(date).item.get(len).name.equals(name)){
                                    billMap.get(date).item.get(len).dinneramount =amount;
                                    billMap.get(date).item.get(len).avgprice += price;
                                }
                                else if( !billMap.get(date).item.get(len).name.equals(name) ){
                                    billMap.get(date).item.add(new DailyItem(name,0.00,0.0,amount,price));
                                    //System.out.println("Called");
                                }
                            }
                            else{
                                billMap.put(date, new BillAmount(price,0.00,0.00));
                                billMap.get(date).item.add(new DailyItem(name,0.00,0.00,amount,price));
                            }
                            break;
                        default:
                            break;
                    }
                    
                    
                }
                catch(Exception e){
                    //JOptionPane.showMessageDialog(null,date+" has no nonstore out", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    //return;
                }
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate dailybill", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    
    
    
    
    
    
    public void calculatestoreddailybill(int fromserial, int toserial){
        Double avg=0.00, amount=0.0, price=0.0, bf=0.00,lunch=0.0,dinner=0.0,available=0.0,prevbf=0.0,prevlunch=0.0,prevdinner=0.0;
        String name="";
        int serial=0;
        
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price,item,serial from storeinout where serial between ? and ? order by serial");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            rs = psmt.executeQuery();
            //System.out.println(name+" "+dateserial);
            while(rs.next()){
                try{
                    name = rs.getString(6);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "name not found in hashmap", "stored daily calculation", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //System.out.println(name);
                previous.get(name).available += rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                amount = previous.get(name).amount;
                amount = amount+rs.getDouble(1);
                price = previous.get(name).price;
                price = price + rs.getDouble(5);
                //System.out.println(previous.get(name).amount+" "+amount+" "+price+" "+name+" "+price/amount);
                avg=price/amount;
                bf=rs.getDouble(2)*avg;
                lunch = rs.getDouble(3)*avg;
                dinner = rs.getDouble(4)*avg;
                //System.out.println(amount+" "+price+" "+name+" "+avg+" "+bf);
                
                serial = rs.getInt(7);
                if(billMap.containsKey(serial)){
                    billMap.get(serial).bf += bf;
                    billMap.get(serial).lunch += lunch;
                    billMap.get(serial).dinner += dinner;
                    
                }
                else{
                    billMap.put(serial, new BillAmount(bf,lunch,dinner));
                }
                if(previous.get(name).available == 0){
                    amount=0.0;
                    price=0.0;
                }
                previous.get(name).amount = amount;
                previous.get(name).price = price;
                billMap.get(serial).item.add(new DailyItem(name,rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),avg));
                
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate dailybill", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
  
    
    
    public void calculateprevious(int dateserial, String item){
        Double available=0.0, totalamount =0.0, totalprice=0.0;
        String name="";
        //System.out.println(name+" "+dateserial);
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price,item,serial from storeinout where serial < ? and item =? order by serial");
            psmt.setInt(1, dateserial);
            psmt.setString(2, item);
            rs = psmt.executeQuery();
            //System.out.println(rs.getDouble(1)+" "+rs.getDouble(2));
            while(rs.next()){
                name = rs.getString(6);
                totalamount =rs.getDouble(1);
                //System.out.println(rs.getInt(7)+" "+totalamount+" "+rs.getDouble(2)+" "+rs.getDouble(3)+" "+rs.getDouble(4)+" "+rs.getDouble(5));
                try{
                   available = previous.get(name).available;
                   previous.get(name).available = available+totalamount-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                   previous.get(name).amount += totalamount;
                   previous.get(name).price += rs.getDouble(5);
                   //System.out.println( previous.get(name).amount +" "+previous.get(name).available +" "+name);
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Hashmap failed to store previous available", "Data fetch error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if(previous.get(name).available == 0){
                    previous.get(name).amount =0.0;
                    previous.get(name).amount =0.0;
                }
                //previous.put(name, new PreviousValue(totalamount,totalprice,available));
            }
            //previous.put(name, new PreviousValue(totalamount,totalprice));
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to calculate previous available", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println( previous.get(name).amount +" "+previous.get(name).available +" "+name);
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
