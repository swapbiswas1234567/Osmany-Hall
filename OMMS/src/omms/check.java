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
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class check {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    
    check(){
        conn = Jconnection.ConnecrDb(); // set connection with database
    }
    
    public void print(){
        try{
            psmt = conn.prepareStatement("select *from stuinfo");
            rs = psmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(1));
                
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
}
