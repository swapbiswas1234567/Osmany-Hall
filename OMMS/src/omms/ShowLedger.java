/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class ShowLedger extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag=0;
    
    
    /**
     * Creates new form ShowLedger
     */
    public ShowLedger() {
        initComponents();
        Tabledecoration();
        inittialization();
        getAllstoreditem();
        flag=1;
        
        
        
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try{
                    Dashboard das = new Dashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
    }
    
    
        //get name of all item from database 
    public void getAllstoreditem(){
       
        try{
            psmt = conn.prepareStatement("select name from storeditem");
            rs = psmt.executeQuery();
            while(rs.next()){
                itemcombobox.addItem(rs.getString(1));  // sending the name of item to set in the combobox
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for combobox", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
   
    }
    
    public Double[] getpreviousavailable(int dateserial, String itemname){
        Double []available = new Double[3];
        //System.out.println(dateserial);
        available[0]= 0.00;
        available[1] = 0.00;
        available[2] = 0.00;
        
        try{
            psmt = conn.prepareStatement("select inamount,bf,lunch,dinner,price from storeinout where item =? and serial < ?");
            psmt.setString(1, itemname);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                available[0] =available[0]+ rs.getDouble(1)-(rs.getDouble(2)+rs.getDouble(3)+rs.getDouble(4));
                available[1] = available[1]+rs.getDouble(1);
                available[2]= available[2]+rs.getDouble(5);
                if(available[0] == 0){
                    available[1] = 0.00;
                    available[2]= 0.00;
                }
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
    
    
    public String getunit(String name){
        
        String unit="";
        try{
            psmt = conn.prepareStatement("select unit from storeditem where name = ?");
            psmt.setString(1, name);
            rs = psmt.executeQuery();
            while(rs.next()){
                unit = rs.getString(1);
            }
            psmt.close();
            rs.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, ""
                    + "failed to set the unit of for item", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
        //System.out.println("called"+unit);
        return unit;
        
    }
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        fromdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        todatechooser.setDate(todaysdate);
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) fromdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) todatechooser.getDateEditor();
        dtedit1.setEditable(false);
        
        dec = new DecimalFormat("#0.000");
        model = ledgertable.getModel();
        
    }
    
    
    public void setledgertable(Date from, Date to, String item){
        //System.out.println(from+" "+to+" "+item);
        int fromserial = 0, toserial = 0, count=2;
        Double lastavg=0.00, lastavl=0.00 , fromavailable=0.00;
        Double prevavailable[] =new Double[3];
        prevavailable[0] = 0.00;
        prevavailable[1] =0.00;
        prevavailable[2] =0.00;
        String strdate = "";
        Date date=null;
        tablemodel = (DefaultTableModel) ledgertable.getModel();
        boolean flag=false;
        
        try{
            fromserial = Integer.parseInt(formatter.format(from));
            toserial = Integer.parseInt(formatter.format(to));
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        prevavailable = getpreviousavailable(fromserial, item);
        //System.out.println(prevavailable[0]+" "+prevavailable[1]+" "+prevavailable[2]);
        
        fromavailable = prevavailable[0];
        try{
            psmt = conn.prepareStatement("select serial,inamount,bf,lunch,dinner,price,memono from storeinout where item =? and serial >= ? and serial <=? order by serial");
            psmt.setString(1, item);
            psmt.setInt(2, fromserial);
            psmt.setInt(3, toserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                
                try{
                date = formatter.parse(rs.getString(1));
                strdate = formatter1.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
                //System.out.println(prevavailable[0]);
                lastavl = prevavailable[0] +rs.getDouble(2)-(rs.getDouble(3)+rs.getDouble(4)+rs.getDouble(5));
                prevavailable[1] =  prevavailable[1] + rs.getDouble(2);
                prevavailable[2] =  prevavailable[2] + rs.getDouble(6);
                //System.out.println("serial "+rs.getInt(1)+"prev available: "+prevavailable[0]+"last available: "+lastavl+"avg price: "+prevavailable[1]);
                lastavg = prevavailable[2]/prevavailable[1];
                
                Object o [] = {strdate,item,dec.format(prevavailable[0]),rs.getDouble(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),lastavl,dec.format(lastavg),rs.getString(7)};
                tablemodel.addRow(o);
                prevavailable[0] = lastavl;
                
                if(lastavl == 0){
                    prevavailable[1] =0.00;
                    prevavailable[2] = 0.00;
                }
                flag=true;
            }
            psmt.close();
            rs.close();
            
           //System.out.println("called "+item);
            String unit = getunit(item);
            if(flag){
                namelbl.setText(item);
                previousavllbl.setText(fromavailable.toString());
                currevtavllbl.setText(prevavailable[0].toString());
                dayslbl.setText(Integer.toString(tablemodel.getRowCount()));
                unitlbl.setText(unit);
            }
            else{
                namelbl.setText("");
                previousavllbl.setText("");
                currevtavllbl.setText("");
               dayslbl.setText("");
               unitlbl.setText("");
            }
           // setlbl(item,fromavailable.toString(),prevavailable[0].toString(),Integer.toString(tablemodel.getRowCount()));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    
//    public void setlbl(String name, String prev, String current, String days){
//        String unit = getunit(name);
//        namelbl.setText(name);
//        previousavllbl.setText(current);
//        currevtavllbl.setText(days);
//        dayslbl.setText(Integer.toString(tablemodel.getRowCount()));
//        unitlbl.setText(unit);
//    }
    
    
    public void Tabledecoration(){
        ledgertable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        ledgertable.getTableHeader().setOpaque(false);
        ledgertable.getTableHeader().setBackground(new Color(32,136,203));
        ledgertable.getTableHeader().setForeground(new Color(255,255,255));
        ledgertable.setRowHeight(25);
        ledgertable.setFont(new Font("Segeo UI", Font.PLAIN, 14));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        ledgertable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        ledgertable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fromdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        todatechooser = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        itemcombobox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        ledgertable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        namelbl = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        previousavllbl = new javax.swing.JLabel();
        unitlbl = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        currevtavllbl = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dayslbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/ledger.png"))); // NOI18N
        jLabel1.setText("  LEDGER");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/start.png"))); // NOI18N
        jLabel2.setText("From");

        fromdatechooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fromdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdatechooserPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/end.png"))); // NOI18N
        jLabel3.setText("To ");

        todatechooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        todatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                todatechooserPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/survey.png"))); // NOI18N
        jLabel4.setText("Item ");

        itemcombobox.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        itemcombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemcomboboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemcombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(todatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
        );

        ledgertable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Item", "Previous Quantity", "In Quantity", "Breakfast", "Lunch", "Dinner", "Last Quantity", "Avg Price", "Memo No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ledgertable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        ledgertable.setRowHeight(26);
        ledgertable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        ledgertable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        jScrollPane1.setViewportView(ledgertable);
        if (ledgertable.getColumnModel().getColumnCount() > 0) {
            ledgertable.getColumnModel().getColumn(2).setMinWidth(110);
        }

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        jLabel5.setText("Item Name: ");

        namelbl.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel9.setText("Previous Available :");

        previousavllbl.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N

        unitlbl.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        unitlbl.setForeground(new java.awt.Color(255, 0, 1));

        jLabel12.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Current Available :");

        currevtavllbl.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Unit : ");

        jLabel15.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel15.setText("No Of Row :");

        dayslbl.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dayslbl, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(86, 86, 86))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(previousavllbl, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currevtavllbl, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitlbl, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(namelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(previousavllbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currevtavllbl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dayslbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fromdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdatechooserPropertyChange
        // TODO add your handling code here:
        int len = -1;
        Date from=null, to =null;
        String item="";
        from = fromdatechooser.getDate();
        to = todatechooser.getDate(); 
 
        
        tablemodel = (DefaultTableModel) ledgertable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        if( from != null && to != null && flag==1){
            item = itemcombobox.getSelectedItem().toString();
            setledgertable(from, to, item);
        }
    }//GEN-LAST:event_fromdatechooserPropertyChange

    private void todatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_todatechooserPropertyChange
        // TODO add your handling code here:
        
        int len = -1;
        Date from=null, to =null;
        String item="";
        from = fromdatechooser.getDate();
        to = todatechooser.getDate(); 
        //len = itemcombobox.getItemCount();
        
        tablemodel = (DefaultTableModel) ledgertable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        if( from != null && to != null && flag ==1 ){
            item = itemcombobox.getSelectedItem().toString();
            setledgertable(from, to, item);
        }
        
        
        
    }//GEN-LAST:event_todatechooserPropertyChange

    private void itemcomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemcomboboxActionPerformed
        // TODO add your handling code here:
        
                int len = -1;
        Date from=null, to =null;
        String item="";
        from = fromdatechooser.getDate();
        to = todatechooser.getDate(); 
        //len = itemcombobox.getItemCount();
        
        tablemodel = (DefaultTableModel) ledgertable.getModel();
        if(tablemodel.getColumnCount() > 0){
            tablemodel.setRowCount(0);
        }
        
        
        if( from != null && to != null && flag ==1 ){
            item = itemcombobox.getSelectedItem().toString();
            setledgertable(from, to, item);
        }
        
    }//GEN-LAST:event_itemcomboboxActionPerformed

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
            java.util.logging.Logger.getLogger(ShowLedger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowLedger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowLedger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowLedger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShowLedger().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currevtavllbl;
    private javax.swing.JLabel dayslbl;
    private com.toedter.calendar.JDateChooser fromdatechooser;
    private javax.swing.JComboBox<String> itemcombobox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable ledgertable;
    private javax.swing.JLabel namelbl;
    private javax.swing.JLabel previousavllbl;
    private com.toedter.calendar.JDateChooser todatechooser;
    private javax.swing.JLabel unitlbl;
    // End of variables declaration//GEN-END:variables
}
