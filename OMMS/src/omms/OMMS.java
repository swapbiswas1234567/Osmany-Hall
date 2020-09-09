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
        //StoreInSum st = new StoreInSum();
        //StoreInForm st =new StoreInForm();
        //StoreInHistory st =new StoreInHistory();
         //StoredItemOutUpdate st= new StoredItemOutUpdate();
        StoreOutHistory st= new StoreOutHistory();
         st.setVisible(true);
    }

}
