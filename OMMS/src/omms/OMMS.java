package omms;




import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
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
        //StoreInHistory st = new StoreInHistory();
        //PaymentHistory st= new PaymentHistory();
        //NSItemView st= new NSItemView();
        //GenerateBill st= new GenerateBill();
        //stdIndBillStat st= new stdIndBillStat();
        //TmpFoodUpdate st= new TmpFoodUpdate();
        //TemporaryFoodIn st= new TemporaryFoodIn();
        AboutUs st= new AboutUs();
        st.setVisible(true);
        
    }

}
