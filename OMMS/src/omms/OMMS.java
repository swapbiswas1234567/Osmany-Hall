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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ajmir
 */
public class OMMS {
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    ResultSet r = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Login lg = new Login();
        lg.setVisible(true);
 
        
    }
    
    
    
    
}
