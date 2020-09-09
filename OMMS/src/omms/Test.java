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
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class Test {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void test() throws SQLException {
        conn = Jconnection.ConnecrDb();
        try {

            ps = conn.prepareStatement("Select * From stuinfo");
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(" " + rs.getString(2) + " " + rs.getString(3) + " \n" );
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
        }
    }

}
