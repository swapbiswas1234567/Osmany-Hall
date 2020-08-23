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
import java.util.HashMap;
import java.util.Map;
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
<<<<<<< HEAD

        //Dashboard st=new Dashboard();
        stdIndBillStat st= new stdIndBillStat();
        //GenerateBill st = new GenerateBill();
        st.setVisible(true);
        //Map<Integer, BillAmount> billmap = new HashMap<>();
         //DailyAvgBill st= new DailyAvgBill();
         //st.setbill(0, 0);
        
       
=======
        // NSItemView st=new NSItemView();
        //StoreInForm st = new StoreInForm();
        //Dashboard st = new Dashboard();
        //TempFoodView st = new TempFoodView();
        //TempFoodUpdate st = new TempFoodUpdate();
        //NSItemView st=new NSItemView();
        //StoreInForm st = new StoreInForm();
        //StoreOutItem st =new StoreOutItem();
        //Dashboard st= new Dashboard();
        //stdIndBillStat st = new stdIndBillStat();
        stdHallAdmission st = new stdHallAdmission();
        st.setVisible(true);

>>>>>>> 3951fcbe8d233c2ea07ae56c44975801fedfcb75
    }

}
