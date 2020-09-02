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
              previousavgprice(20200727,key);  // calculate the previous avg price
          }
          
         //System.out.println(" "+previous.get("Rice").avgprice+" "+previous.get("Rice").prevavailable);
//          calculatestoreddailybill(fromdate,todate);  //calculate stored bill
//          calculatenonstoreddailybill(fromdate,todate); //add nonstored bill
//          for(int i=0; i<billMap.get(20200813).item.size(); i++){
//              System.out.println(billMap.get(20200813).item.get(i).name+" "+billMap.get(20200813).item.get(i).avgprice+" "+
//                      billMap.get(20200813).item.get(i).bfamount+" "+billMap.get(20200813).item.get(i).dinneramount);
//          }
          //System.out.println(billMap.get(todate).bf);
//          calculateavg(fromdate,todate); // divide total bill by total meal on
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
    
    
    public void storeditemdailycost(int fromdate,int todate){
        
    }
    
    
    
    public void previousavgprice(int dateserial, String item){
        Double available= 0.00, prevavailable=0.00 , totalprice=0.00, totalamount=0.00, avg =0.00;
        
        
        
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
