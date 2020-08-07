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
public class TemporaryFood extends javax.swing.JFrame {
    
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
     * Creates new form TemporaryFood
     */
    public TemporaryFood() {
        initComponents();
        Tabledecoration();
        inittialization();
        
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
    
    
    
    
    public void Tabledecoration(){
        tempfoodtable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        tempfoodtable.getTableHeader().setOpaque(false);
        tempfoodtable.getTableHeader().setBackground(new Color(32,136,203));
        tempfoodtable.getTableHeader().setForeground(new Color(255,255,255));
        tempfoodtable.setRowHeight(25);
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tempfoodtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        
        
    }
    
    public void inittialization(){
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate =new Date();
        insertdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        editdatechooser.setDate(todaysdate);
        
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) insertdatechooser.getDateEditor();
        dtedit.setEditable(false);
        
        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) editdatechooser.getDateEditor();
        dtedit1.setEditable(false);
        
        dec = new DecimalFormat("#0.000");
        model = tempfoodtable.getModel();
        
        inserthallid.requestFocus();
    }
    
    public void inserttable(Date date, String id, String bill){
        Double totalbill = 0.00;
        int dateserial = 0, hallid =0;
        String strdate="";
        try{
            strdate = formatter1.format(date);
            dateserial = Integer.parseInt(formatter.format(date));
            hallid = Integer.parseInt(id);
            totalbill = Double.parseDouble(bill);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Failed to convert date "
                    + "inserttable", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
        boolean databasecheck = checkitemondatabase(dateserial,hallid);
        //System.out.println(databasecheck);
        boolean tablecheck = checkinjtable(id,strdate,-1);
        //System.out.println(databasecheck+" "+tablecheck);
        if(databasecheck){
            JOptionPane.showMessageDialog(null, "data for same item same exist in database", "Data"
                    + " insertion table error", JOptionPane.ERROR_MESSAGE);
        }
        else if(tablecheck){
            JOptionPane.showMessageDialog(null, "data for same item same exist in Jtable", "Data"
                    + " insertion table error", JOptionPane.ERROR_MESSAGE);
        }
        else if( totalbill > 0){
            tablemodel = (DefaultTableModel) tempfoodtable.getModel();
            Object o [] = {hallid, dec.format(totalbill),strdate};
            tablemodel.addRow(o);
            clearinsertfields();
            inserthallid.requestFocus();
        }
    }
    
    
    public boolean checkitemondatabase(int dateserial , int hallid){
        
        //System.out.println(" "+dateserial+" "+hallid);
        try{
            psmt = conn.prepareStatement("select hallid from tempfood where hallid = ? and dateserial=?");
            psmt.setInt(1, hallid);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            while(rs.next()){
                //System.gtiout.println(rs.getInt(1));
                psmt.close();
                rs.close();
                return true;
            }
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }
        
       return false; 
        
    }
    
    public boolean checkinjtable(String hallid, String date, int index){
        int totalrow = -1;
        totalrow = tempfoodtable.getRowCount();
        
        for( int i=0; i<totalrow; i++){
            if( model.getValueAt(i,0).toString().equals(hallid) && model.getValueAt(i, 2).toString().equals(date)
                   && i!= index){
                return true;
            }
        }
        
        return false;
    }
    
    
    
    public void updatefield(){
        String hallid = "" ,bill = "",strdate ="";
        Date date = null;
        int selectedrow = tempfoodtable.getSelectedRow();
        hallid = model.getValueAt(selectedrow, 0).toString();
        bill = model.getValueAt(selectedrow, 1).toString();
        strdate = model.getValueAt(selectedrow, 2).toString();
        //System.out.println(name+" "+amount+" "+totalprice+" "+strdate+" "+status+" "+memono);
        
        try{
            date = formatter1.parse(strdate);
        }
        catch(ParseException e){
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "error in Updatefield", "Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println(date+" "+hallid+" "+bill);
        
        if(selectedrow >=0){
            editdatechooser.setDate(date);
            edithallid.setText(hallid);
            editbill.setText(bill);
            
        }
        
    }
    
    
    public void seteditedlevalue(){
        String hallid = "", bill = "", strdate = "";
        int selectedrow = -1, dateserial =0, id=0;
        Double totalbill = 0.00;
        Date date=null;
       
        hallid = edithallid.getText().trim();
        date = editdatechooser.getDate();
        bill = editbill.getText();
        selectedrow = tempfoodtable.getSelectedRow();
        
        if( selectedrow >= 0){
            try{
            strdate = formatter1.format(date);
            dateserial = Integer.parseInt(formatter.format(date));
            id = Integer.parseInt(hallid);
            totalbill = Double.parseDouble(bill);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Date Parse in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
            }

            //System.out.println(bill);
            boolean databasecheck = checkitemondatabase(dateserial,id);
            //System.out.println(databasecheck);
            boolean tablecheck = checkinjtable(hallid,strdate,selectedrow);
            //System.out.println(databasecheck+" "+tablecheck);
            if(databasecheck){
                JOptionPane.showMessageDialog(null, "data for same item same exist in database", "Data"
                        + " insertion table error", JOptionPane.ERROR_MESSAGE);
            }
            else if(tablecheck){
                JOptionPane.showMessageDialog(null, "data for same item same exist in Jtable", "Data"
                        + " insertion table error", JOptionPane.ERROR_MESSAGE);
            }
            else if( totalbill > 0){
                model.setValueAt(hallid, selectedrow , 0);
                model.setValueAt(dec.format(totalbill), selectedrow , 1);
                model.setValueAt(strdate, selectedrow , 2);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "no selected row","date error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void insertdatabase(){
        String hallid="", bill="", strdate="";
        int dateserial=0, id=0;
        Double totalbill=0.00;
        Date date=null;
        int totalrow = tempfoodtable.getRowCount();
        for( int i=0; i< totalrow; i++){
            hallid = model.getValueAt(i, 0).toString();
            bill = model.getValueAt(i, 1).toString();
            strdate = model.getValueAt(i, 2).toString();
            //System.out.println(hallid+" "+bill+" "+strdate);
            
            try{
                date = formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));
                totalbill = Double.parseDouble(bill);
                id = Integer.parseInt(hallid);
                //System.out.print(dateserial);
            }
            catch(NumberFormatException | ParseException e){
                JOptionPane.showMessageDialog(null, "date parsing error"
                        + "while inserting data","date error", JOptionPane.ERROR_MESSAGE);
            }
            //System.out.println(dateserial+" "+id+" "+totalbill);
            
            try{
                psmt = conn.prepareStatement("insert into tempfood (hallid, dateserial, bill) values(?, ? ,?)");
                psmt.setInt(1, id);
                psmt.setInt(2, dateserial);
                psmt.setDouble(3, totalbill);
                psmt.execute();
                psmt.close();
                
               
                }  
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "insertion error"
                            + " in temp food", "Data insertion error", JOptionPane.ERROR_MESSAGE);
                }
            
            
            
        }
    }
    
    
    public void deleterow(int selectedrow){
        
        int responce = JOptionPane.showConfirmDialog(this,"Do You Want To Delete"
                + " The Selected Row ?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION){
            tablemodel = (DefaultTableModel) tempfoodtable.getModel();
            tablemodel.removeRow(selectedrow);
        }
    }
    
    
    
    public void clearinsertfields(){
        inserthallid.setText("");
        insertbill.setText("");
       
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
        insertdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        inserthallid = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        insertbill = new javax.swing.JTextField();
        insertbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        editdatechooser = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        edithallid = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        editbill = new javax.swing.JTextField();
        editbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tempfoodtable = new javax.swing.JTable();
        saveandexitbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));
        jPanel1.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/snacks.png"))); // NOI18N
        jLabel1.setText("  TEMPORARY FOOD");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Date ");

        insertdatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Hall id ");

        inserthallid.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        inserthallid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserthallidActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Bill ");

        insertbill.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertbill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertbillActionPerformed(evt);
            }
        });

        insertbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        insertbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/tempinsert.png"))); // NOI18N
        insertbtn.setText("Insert");
        insertbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(insertbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inserthallid)
                            .addComponent(insertbill))))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inserthallid, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertbill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));
        jPanel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel5.setText("EDIT");

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Date ");

        editdatechooser.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Hall Id ");

        edithallid.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        edithallid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edithallidActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Bill ");

        editbill.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        editbill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editbillActionPerformed(evt);
            }
        });

        editbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        editbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/rubber.png"))); // NOI18N
        editbtn.setText("Edit");
        editbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editbtnActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Bell MT", 0, 20)); // NOI18N
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash.png"))); // NOI18N
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(edithallid)
                            .addComponent(editbill))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(editbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edithallid, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editbill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tempfoodtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hall Id", "Bill", "Date"
            }
        ));
        tempfoodtable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tempfoodtable.setRowHeight(26);
        tempfoodtable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        tempfoodtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tempfoodtableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tempfoodtable);

        saveandexitbtn.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        saveandexitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        saveandexitbtn.setText("Save & Exit");
        saveandexitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveandexitbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(saveandexitbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(saveandexitbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbtnActionPerformed
        // TODO add your handling code here:
        String hallid ="", bill="";
        Date date;
        hallid = inserthallid.getText().trim();
        bill = insertbill.getText().trim();
        date = insertdatechooser.getDate();
        //System.out.println("called "+hallid);
        if(!hallid.equals("") && !bill.equals("") ){
            inserttable(date, hallid, bill);
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid amount", "Data insert error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_insertbtnActionPerformed

    private void inserthallidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserthallidActionPerformed
        // TODO add your handling code here:
        insertbill.requestFocus();
    }//GEN-LAST:event_inserthallidActionPerformed

    private void insertbillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbillActionPerformed
        // TODO add your handling code here:
        insertbtn.doClick();
    }//GEN-LAST:event_insertbillActionPerformed

    private void tempfoodtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tempfoodtableMouseClicked
        // TODO add your handling code here:
        updatefield();
        edithallid.requestFocus();
        //tempfoodtable.clearSelection();
    }//GEN-LAST:event_tempfoodtableMouseClicked

    private void editbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editbtnActionPerformed
        // TODO add your handling code here:
        seteditedlevalue();
        tempfoodtable.clearSelection();
        
        edithallid.setText("");
        editbill.setText("");
        inserthallid.requestFocus();
    }//GEN-LAST:event_editbtnActionPerformed

    private void edithallidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edithallidActionPerformed
        // TODO add your handling code here:
        editbill.requestFocus();
    }//GEN-LAST:event_edithallidActionPerformed

    private void editbillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editbillActionPerformed
        // TODO add your handling code here:
        editbtn.doClick();
    }//GEN-LAST:event_editbillActionPerformed

    private void saveandexitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveandexitbtnActionPerformed
        // TODO add your handling code here:
        insertdatabase();
    }//GEN-LAST:event_saveandexitbtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow = -1;
        selectedrow = tempfoodtable.getSelectedRow();
        
        if( selectedrow >=0){
            deleterow(selectedrow);
        }
        else{
            JOptionPane.showMessageDialog(null, "select a row", "Data delete error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deletebtnActionPerformed

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
            java.util.logging.Logger.getLogger(TemporaryFood.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TemporaryFood.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TemporaryFood.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TemporaryFood.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TemporaryFood().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField editbill;
    private javax.swing.JButton editbtn;
    private com.toedter.calendar.JDateChooser editdatechooser;
    private javax.swing.JTextField edithallid;
    private javax.swing.JTextField insertbill;
    private javax.swing.JButton insertbtn;
    private com.toedter.calendar.JDateChooser insertdatechooser;
    private javax.swing.JTextField inserthallid;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveandexitbtn;
    private javax.swing.JTable tempfoodtable;
    // End of variables declaration//GEN-END:variables
}
