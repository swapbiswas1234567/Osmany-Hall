/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;
import com.itextpdf.text.DocumentException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ajmir
 */
public class OMMS {
    //Connection con=null; 
    //PreparedStatement ps =null;
    //ResultSet rs = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //NonStoredItem st = new NonStoredItem();
        //NonStoredItemUpdate st = new NonStoredItemUpdate();
        //TempFoodUpdate st= new TempFoodUpdate();
        //StoredItemUpdate st= new StoredItemUpdate();
        //Dashboard st = new Dashboard();
        
//        st.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println("WindowClosingDemo.windowClosing");
//                System.exit(0);
//            }
//        });
        
        Dashboard st= new Dashboard();
        st.setVisible(true);
        
    }
    
    
    
}
