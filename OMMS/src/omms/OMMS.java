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
    public static void main(String[] args) {
        Login lg = new Login();
        //StdIndBillStat lg = new StdIndBillStat();
        //StdInfoViewPrev lg = new StdInfoViewPrev();
        //StdHallAdmission lg = new StdHallAdmission();
        //StdInfoViewCur lg = new StdInfoViewCur();
        //MessBillView lg = new MessBillView();
        //StdInfoDelete lg = new StdInfoDelete();
        //StdInfoUpdate lg = new StdInfoUpdate();
        //AccountPaymentDelete lg = new AccountPaymentDelete();
        lg.setVisible(true);
    }
}
