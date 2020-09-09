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
    public static final String DEST = "G:\\New folder (2)\\pdfsplit_test.pdf";
    public static void main(String[] args) throws DocumentException, IOException {
        //StoreInSum st = new StoreInSum();
        //st.setVisible(true);
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Test().gen(DEST);
    }

}
