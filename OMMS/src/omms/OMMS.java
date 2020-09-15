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
           Login lg = new Login();
           lg.setVisible(true);
    }

}
