/*
this class is created to established to connect with database 
 */
package omms;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajmir
 */
public class Jconnection {
    
    // function for esatblish connection between sqlite and java 
    public static Connection ConnecrDb(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.sqlite.JDBC"); // name of the database we are connecting with
            
            Connection conn = DriverManager.getConnection("jdbc:sqlite:..\\osmanyhall.db"); // location of the database 
            //System.out.println("Connected");
            return conn;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Eror while connecting with database","Dtabase Connection Error",JOptionPane.ERROR_MESSAGE);
            return null; // if fails to connect with database then it will generate a error message and return null or stop the programme
        }
    }
}
