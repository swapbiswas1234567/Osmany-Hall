/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.sql.SQLException;

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
    public static void main(String[] args) throws SQLException {
        //deleteGroup st = new deleteGroup();
        //Login st = new Login();
        //Dashboard st = new Dashboard();
        //stdHallAdmission st = new stdHallAdmission();
        //stdInfoUpdate st = new 
        //NewDashboard st = new NewDashboard();
        //MessBillView st = new MessBillView();
        AccountPayment st = new AccountPayment();
        st.setVisible(true);       
    }

}
