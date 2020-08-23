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
    
    Map<Integer, DailyMealState> mealsheet;
    Map<Integer, Double> bill;
    
    SingleStdntBill(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        
        mealsheet = new HashMap<>();
        bill = new HashMap<>();
    }
    
    public Map<Integer, DailyMealState> meal(int fromdate, int todate, int hallid){
        //System.out.println(fromdate+" "+todate+hallid);
        try{
            psmt = conn.prepareStatement("select date,breakfast,lunch,dinner from mealsheet where date BETWEEN ? and ? and hallid=? ORDER by date");
            psmt.setInt(1, fromdate);
            psmt.setInt(2, todate);
            psmt.setInt(3, hallid);
            rs = psmt.executeQuery();
            while(rs.next()){
                mealsheet.put(rs.getInt(1), new DailyMealState(rs.getInt(2), rs.getInt(3), rs.getInt(4)));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        return mealsheet; 
    }
    
    public Map<Integer, Double> generatebill(Map<Integer, BillAmount> billmap, Map<Integer, DailyMealState> mealsheet){
        int key=0;
        for(Map.Entry mapelement : mealsheet.entrySet()){
            key = (Integer) mapelement.getKey();
            System.out.println(key);
        }
        
        return bill;
    }
    
}
