package omms;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author User
 */
public class StoreOutSum extends javax.swing.JFrame {
    ///Variable declaration
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter2;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int selectedRow;
    DecimalFormat dec2;
   
    int flag;
    PreparedStatement psmt1 = null;
    ResultSet rs1 = null;
    int ser=0;
    
    
    public StoreOutSum() {
        initComponents();
        initialize();
        tabledecoration();
        dateNtableset();
        itemcombo_set();
        flag=1;
        initialtbl();
    }

            /**Initializing Variable Function **/
    public void initialize()
    {
        conn= Jconnection.ConnecrDb();
        psmt=null;
        rs=null;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  
        formatter2 = new SimpleDateFormat("MMM dd,yyyy");
        dec2 = new DecimalFormat("#0.00");
        
        selectedRow = -1;
        this.setTitle("Non Stored Item View");
        closeBtn();
        
         try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    conn.close();
                    if(UserLog.name.equals("accountant")){
                        DashboardAccountant das = new DashboardAccountant();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("provost")){
                        DashboardHallAutho das = new DashboardHallAutho();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("mess")){
                        DashboardMess das = new DashboardMess();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                    else if(UserLog.name.equals("captain")){
                        DashboardMessCap das = new DashboardMessCap();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    
    ///Table decoration
    
    public void tabledecoration(){
        store_tbl.getTableHeader().setFont(new java.awt.Font("Segeo UI", java.awt.Font.BOLD, 20));
        store_tbl.getTableHeader().setOpaque(false);
        store_tbl.getTableHeader().setBackground(new Color(32,136,203));
        store_tbl.getTableHeader().setForeground(new Color(255,255,255));
        store_tbl.setRowHeight(30);
        store_tbl.setFont(new java.awt.Font("Segeo UI", java.awt.Font.PLAIN, 18));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        
        store_tbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        
        
    }

   public void dateNtableset()
    {
        tm=(DefaultTableModel)store_tbl.getModel();
        tm.setRowCount(0);
        /***Date Setting**/
        Date date= new Date();
        fromdt_ch.setDate(date);
        todt_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) fromdt_ch.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) todt_ch.getDateEditor();
        editor.setEditable(false);
        
    }
     
    
    
    //Combo Name of item setting
    public void itemcombo_set()
    {
        try{
           psmt=conn.prepareStatement("select name from storeditem");
           rs=psmt.executeQuery();
           
           while(rs.next())
           {
               String item = firstupperCaseMaker(rs.getString(1).toLowerCase());
               Item_cmb.addItem(item);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    }
   
    
       
    public Double[] getpreviousavailable(int dateserial, String itemname){
        Double []available = new Double[2];
        Double total=0.0, amount=0.0;
        //System.out.println(dateserial);
        available[0]= 0.00;
        available[1] = 0.00;
        
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price from storeinout where item =? and serial < ? order by serial");
            psmt.setString(1, itemname);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                total = (available[0]*available[1])+rs.getDouble(5);
                amount = available[0]+rs.getDouble(1);
                available[1] = total/amount;
                available[0] =available[0]+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
            }
            //System.out.println(available[1]);
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + " previous avaialable", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        
        
        
        return available;
    }
    
    public Map<String, PreviousValue> allstoreditem(int fromdate){
        Map<String, PreviousValue> previous;
        previous = new HashMap<>();
        try{
            //System.out.println(fromdate+" called "+todate);
            psmt = conn.prepareStatement("select DISTINCT name from storeditem");
            rs = psmt.executeQuery();
            while(rs.next()){
                previous.put(rs.getString(1), new PreviousValue(0.0,0.0));
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetchname of stored item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        return previous;
    }
    
    
    public Map<String, PreviousValue> allgetpreviousavailable(int dateserial, Map<String, PreviousValue> previous){
        Double total=0.0, amount=0.0;
        
        
        try{
            psmt = conn.prepareStatement("select item,inamount,bf,lunch,dinner,price from storeinout where serial < ? order by serial,item");
            psmt.setInt(1, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                total = (previous.get(rs.getString(1)).avgprice*previous.get(rs.getString(1)).prevavailable)+rs.getDouble(6);
                amount =previous.get(rs.getString(1)).prevavailable +rs.getDouble(2);
                previous.get(rs.getString(1)).avgprice = total/amount;
                previous.get(rs.getString(1)).prevavailable =previous.get(rs.getString(1)).prevavailable+ rs.getDouble(2)-(rs.getDouble(3)+rs.getDouble(4)+rs.getDouble(5));
            }
            //System.out.println(available[1]);
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "all previous avaialable", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        
        return previous;
    }
    

    
     ///Setting function of  for table to show non stored item
    public void setItemtable(Date from, Date to, String item ){
        
        Map<String, PreviousValue> previous;
        int fromserial = 0, toserial = 0;
        Double total=0.0, amount=0.0,bf=0.0,lunch=0.0,dinner=0.0;
        Double []available = new Double[2];
        available[0]= 0.00;
        available[1] = 0.00;
      
        String strdate = "",name="";
        ser=0;
        Date date=null;
        tm = (DefaultTableModel) store_tbl.getModel();
       
        try{
            fromserial = Integer.parseInt(formatter1.format(from));
            toserial = Integer.parseInt(formatter1.format(to));
            
            if(fromserial>toserial)
            {
                JOptionPane.showConfirmDialog(null, "Invalid Date","Date Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
   
        try{
            if(item.equals("All")){
                previous = allstoreditem(fromserial);
                previous = allgetpreviousavailable(fromserial, previous);
                psmt = conn.prepareStatement("select  serial,item,inamount,price,memono,bf,lunch,dinner from storeinout where serial>=? and serial<=? order by serial,item ;  ");
                psmt.setInt(1, fromserial);
                psmt.setInt(2, toserial);
                rs = psmt.executeQuery();
                
                while(rs.next()){
                    
                    try{
                        date = formatter1.parse(rs.getString(1));
                        strdate = formatter2.format(date);
                    }
                    catch(ParseException e){
                        JOptionPane.showMessageDialog(null, "Date Parse "
                                + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                    }
                    String X="";
                    name = rs.getString(2);
                    X=getUnit(name);
                    bf= rs.getDouble(6);
                    lunch = rs.getDouble(7);
                    dinner = rs.getDouble(8);
                    //System.out.println(""+rs.getString(2)+" "+previous.get(rs.getString(2)).avgprice);
                    total = (previous.get(name).avgprice*previous.get(name).prevavailable)+rs.getDouble(4);
                    amount =previous.get(name).prevavailable +rs.getDouble(3);
                    previous.get(name).avgprice = total/amount;
                    previous.get(name).prevavailable =previous.get(name).prevavailable+ rs.getDouble(3)-(bf+lunch+dinner);

                    if(rs.getString(5).equals("###") ){
                        //System.out.println(""+previous.get(name).avgprice);
                        ser++;
                        Object o[] = {ser,strdate,name,dec2.format(bf),dec2.format(lunch),dec2.format(dinner),dec2.format(previous.get(name).avgprice),""};
                        //Object o [] = {ser,strdate,rs.getString(2),dec2.format(rs.getDouble(6))+" "+X,rs.getDouble(8)+" "+X,dec2.format(previous.get(rs.getString(2)).avgprice),""};
                        tm.addRow(o);
                
                    }
                    else {
                           
                        ser++;
                        Object o[] = {ser,strdate,name,dec2.format(bf),dec2.format(lunch),dec2.format(dinner),dec2.format(previous.get(name).avgprice),rs.getString(5)};
                        tm.addRow(o);
                    
                    }  
                
                }
            }
            else
            {  
                available = getpreviousavailable(fromserial, item);
                psmt = conn.prepareStatement("select  serial,item,inamount,price,memono,bf,lunch,dinner from storeinout where  serial>=? and serial<=? and item=? order by serial,item ;  ");
                psmt.setInt(1, fromserial);
                psmt.setInt(2, toserial);
                psmt.setString(3, item);
                rs = psmt.executeQuery();
                while(rs.next()){
                
                try{
                    date = formatter1.parse(rs.getString(1));
                    strdate = formatter2.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
                    String X="";
                    X=getUnit(rs.getString(2));
                    //System.err.println(""+rs.getDouble(4)+" "+rs.getDouble(3));
                    total = (available[0]*available[1])+rs.getDouble(4);
                    amount = available[0]+rs.getDouble(3);
                    available[1] = total/amount;
                    available[0] =available[0]+ rs.getDouble(3)-(rs.getDouble(6)+rs.getDouble(7)+rs.getDouble(8));
                    
                    if(rs.getString(5).equals("###") ){
                
                        ser++;
                        Object o [] = {ser,strdate,rs.getString(2),dec2.format(rs.getDouble(6)),dec2.format(rs.getDouble(7)),dec2.format(rs.getDouble(8)),dec2.format(available[1]),""};
                        tm.addRow(o);
                    }
                    else {
                           
                        ser++;
                        Object o [] = {ser,strdate,rs.getString(2),dec2.format(rs.getDouble(6)),dec2.format(rs.getDouble(7)),dec2.format(rs.getDouble(8)),dec2.format(available[1]),rs.getString(5)};
                        tm.addRow(o);
                    
                    }
                
                    
                
                }
                
            }
           
            psmt.close();
            rs.close();
            
        
        }
           
        
       catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
    
   
   
    //generate all
    
//    public void generateAll()
//    {
//        String strdate = "";
//        ser=0;
//        Date date=null;
//        tm = (DefaultTableModel) store_tbl.getModel();
//        
//        
//        
//    }
//    
    
    
    
     /* intial data set*/
    public void initialtbl()
    {
            
        
        Date from=null, to =null;
        String item="";
        String stat="";
        from = fromdt_ch.getDate();
         to = todt_ch.getDate(); 
 
       
        tm = (DefaultTableModel) store_tbl.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        if( from != null && to != null && flag==1){
            item = firstupperCaseMaker(Item_cmb.getSelectedItem().toString().toLowerCase());
    
            setItemtable(from, to, item);
        }

    
    }
    
//Generate pdf function
    public void genpdf()
    {
        String path="";
        JFileChooser j=new JFileChooser();
        //j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int x=j.showSaveDialog(this);
        
        if(x==JFileChooser.APPROVE_OPTION)
        {
            path=j.getSelectedFile().getPath();
        }
        
        Document doc=new Document ();
        
        try{
            
         
            PdfWriter.getInstance(doc,new FileOutputStream(path+".pdf"));
             
            doc.open();
            
            Image image1 = Image.getInstance("..\\\\MIST_Logo.png");
            image1.setAlignment(Element.ALIGN_CENTER);
            image1.scaleAbsolute(100, 70);
            //Add to document
            doc.add(image1);
            
            Paragraph osmany=new Paragraph("OSMANY HALL",FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, com.itextpdf.text.Font.NORMAL));
            osmany.setAlignment(Element.ALIGN_CENTER);
            doc.add(osmany);
            
            Paragraph p=new Paragraph("STORE OUT REPORT\n",FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, com.itextpdf.text.Font.NORMAL));     
             
            p.setAlignment(Element.ALIGN_CENTER);
             
            doc.add(p);
            Date ddt=fromdt_ch.getDate();
            Date dt=todt_ch.getDate();
            
            
            Paragraph q=new Paragraph("From :"+formatter2.format(ddt) ,FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));           
            q.setAlignment(Element.ALIGN_CENTER);   
            doc.add(q);
            
            Paragraph q1=new Paragraph("To :"+formatter2.format(dt),FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));           
            q1.setAlignment(Element.ALIGN_CENTER);   
            doc.add(q1);
            
            Paragraph total=new Paragraph("Total Price:",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            
            Paragraph newline=new Paragraph("\n");
            doc.add(newline);
             
            String[] header = new String[] { "Serial", "Date", "Name",
            "Breakfast", "Lunch","Dinner","Avg Price","Memo" };
            
            PdfPTable table = new PdfPTable(header.length);
            table.setHeaderRows(1);
            table.setWidths(new int[] { 2,3, 3, 4, 3, 2,3,2 });
            table.setWidthPercentage(98);
            table.setSpacingBefore(15);
            table.setSplitLate(false);
            for (String columnHeader : header) {
                PdfPCell headerCell = new PdfPCell();
                headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD)));
                headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            } 
             
             
             for(int i=0; i<store_tbl.getRowCount(); i++){
                 
                 String ser= store_tbl.getValueAt(i, 0).toString();
                 String Date= store_tbl.getValueAt(i, 1).toString();
                 String Item= store_tbl.getValueAt(i, 2).toString();
                 String bf= store_tbl.getValueAt(i,3).toString();
                 String lunch= store_tbl.getValueAt(i,4).toString();
                 String dinner= store_tbl.getValueAt(i,5).toString();
                 String avg= store_tbl.getValueAt(i,6).toString();
                 String mem= store_tbl.getValueAt(i,7).toString();
                 String[] content = new String[] { ser, Date,
                Item, bf, lunch, dinner,avg,mem };
                
                for (String text : content) {
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL)));
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setPadding(5);
                    table.addCell(cell);
                }

             }
             doc.add(table);
            doc.add(new Phrase("\n\n\n"));
            
            LineSeparator separator = new LineSeparator();
            separator.setPercentage(24);
            separator.setAlignment(Element.ALIGN_RIGHT);
            separator.setLineColor(BaseColor.BLACK);
            Chunk linebreak = new Chunk(separator);
            doc.add(linebreak);
            
            Paragraph name1 = new Paragraph("Asst/Associate Hall Provost   ",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));
            name1.setAlignment(Element.ALIGN_RIGHT);
            doc.add(name1);
            
            doc.add(new Phrase("\n\n\n"));
            
            LineSeparator separator1 = new LineSeparator();
            separator1.setPercentage(24);
            separator1.setAlignment(Element.ALIGN_RIGHT);
            separator1.setLineColor(BaseColor.BLACK);
            Chunk linebreak1 = new Chunk(separator1);
            doc.add(linebreak1);
            
            Paragraph name2 = new Paragraph("Hall Provost                 ",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));
            name2.setAlignment(Element.ALIGN_RIGHT);
            doc.add(name2);
             
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Data not inserted","File Error", JOptionPane.ERROR_MESSAGE);
        }
        
        doc.close();
    }
        //get item unit
   
    
    
    //Generate Unit
    public String getUnit(String Item)
    {
       String X="";
        PreparedStatement ps=null;
        ResultSet rs=null;
       try{
        ps=conn.prepareStatement("select unit from storeditem where name=?");
        ps.setString(1, Item);
        rs=ps.executeQuery();
        X=rs.getString(1);
        ps.close();
        rs.close();
        return X;
       }
       catch(Exception e)
       {
           JOptionPane.showMessageDialog(null,"Unit Fetch Error","Unit Error",JOptionPane.ERROR_MESSAGE);
       }
        
        return "";
     
    }
    
    
    //getTotalPrice 
    public double totalprice()
    {
     int row=store_tbl.getRowCount();
     double sum=0.0,z; 
     for(int i=0;i<row;i++)
      {
          z=Double.parseDouble(store_tbl.getValueAt(i, 4).toString()) ;
          sum=sum+z; 
      }
     
     return sum;
    }
   
    
     //getTotalPrice 
    public String totalquantity()
    {
     int row=store_tbl.getRowCount();
     double sum=0.0,z;
     String strquan="",part1="", part2="";
     String Xw="";
     for(int i=0;i<row;i++)
      {
          strquan = store_tbl.getValueAt(i, 3).toString();
          String[] parts = strquan.split(" ");
          part1 = parts[0];
          part2 = parts[1];
          
          //System.out.println(""+part1+" \n"+part2);
          try{
          z=Double.parseDouble(part1) ;
          sum=sum+z; 
          }
          catch(Exception e)
          {
              JOptionPane.showMessageDialog(null,"Quantity Parsing Error","Quantity Error", JOptionPane.ERROR_MESSAGE);
          }
          }
       Xw=dec2.format(sum);
       Xw=Xw+" "+part2;
     return Xw;
    }
    
    
    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
    
 

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Item_cmb = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        fromdt_ch = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        todt_ch = new com.toedter.calendar.JDateChooser();
        pdf_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        store_tbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/summary.png"))); // NOI18N
        jLabel1.setText("STORE OUT SUMMARY");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/list.png"))); // NOI18N
        jLabel2.setText("ITEM :");

        Item_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Item_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL" }));
        Item_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Item_cmbActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel3.setText("FROM:");

        fromdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fromdt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdt_chPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel4.setText("TO :");

        todt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        todt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                todt_chPropertyChange(evt);
            }
        });

        pdf_btn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pdf_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/power (1).png"))); // NOI18N
        pdf_btn.setText("Generate");
        pdf_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdf_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(pdf_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdf_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 144, Short.MAX_VALUE))
        );

        store_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial", "Date", "Item", "Breakfast", "Lunch", "Dinner", "Average Price", "Memo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        store_tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(store_tbl);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Item_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Item_cmbActionPerformed
        initialtbl();
    }//GEN-LAST:event_Item_cmbActionPerformed

    private void fromdt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdt_chPropertyChange
        initialtbl();
    }//GEN-LAST:event_fromdt_chPropertyChange

    private void todt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_todt_chPropertyChange
        initialtbl();
    }//GEN-LAST:event_todt_chPropertyChange

    private void pdf_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdf_btnActionPerformed
        genpdf();
    }//GEN-LAST:event_pdf_btnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOutSum().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Item_cmb;
    private com.toedter.calendar.JDateChooser fromdt_ch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pdf_btn;
    private javax.swing.JTable store_tbl;
    private com.toedter.calendar.JDateChooser todt_ch;
    // End of variables declaration//GEN-END:variables
}
