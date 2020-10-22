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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajmir
 */
public class OMMS {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Login lg = new Login();
        //SecDepUpd lg=new SecDepUpd();
        StdInfoUpdate lg = new StdInfoUpdate();
        lg.setVisible(true);
        
    }
    
    
    
}
