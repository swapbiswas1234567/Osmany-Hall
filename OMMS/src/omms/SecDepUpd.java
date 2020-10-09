package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SecDepUpd extends javax.swing.JFrame {

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
    DecimalFormat dec2;
    int flag=0;
    int selectedRow=0;
    


    public SecDepUpd() {
        initComponents();
        tableDecoration();
        initialize();
        dateNtableset();
        stcombo_act();
       
       
        flag=1;
        
    }

     public void tableDecoration()
    {
        secDep_tbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 20));
        secDep_tbl.getTableHeader().setOpaque(false);
        secDep_tbl.getTableHeader().setBackground(new Color(32,136,203));
        secDep_tbl.getTableHeader().setForeground(new Color(255,255,255));
        secDep_tbl.setRowHeight(30);
        
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        secDep_tbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        secDep_tbl.getColumnModel().getColumn(8).setCellRenderer(centerRender);
    }
    
    
    
    //Set Date and Table
    /**Date and Table Set Function**/
    public void dateNtableset()
    {
        tm=(DefaultTableModel)secDep_tbl.getModel();
        tm.setRowCount(0);
        
       
        
        /***Date Setting**/
        Date date= new Date();
        dt_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dt_ch.getDateEditor();
        editor.setEditable(false);
        wdt_ch.setDate(date);
        
        
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
        selectedRow = -1;
        this.setTitle("SECURITY DEPOSIT UPDATE");
        dec = new DecimalFormat("#0.000");
        dec2 = new DecimalFormat("#0.00");
         name_lbl.setVisible(false);
         wdt_ch.setEnabled(false);
// closeBtn();
        
        
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
   

    //Combo Input setting
    public void stcombo_act()
    {
        int ser=0;
        try{
           if(search_cmb.getSelectedItem().toString().equals("CURRENT"))
           {
            psmt=conn.prepareStatement("select hallid,roll,name,dept,batch,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from stuinfo where moneystatus=0");
            rs=psmt.executeQuery();
            }
           else if(search_cmb.getSelectedItem().toString().equals("PREVIOUS"))
           {
            psmt=conn.prepareStatement("select hallid,roll,name,dept,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from previousstudents where moneystatus=0");
            rs=psmt.executeQuery();
            
           }
           else if(search_cmb.getSelectedItem().toString().equals("ALL"))
            {
                genAll();
            }
          
           
           String getDate="";
           String getDate2="";
           Date da=null;
           Date da1=null;
           
           while(rs.next())
           {
               ser++;
           int edate=0;
           int wdate=0;
          
               tablemodel = (DefaultTableModel) secDep_tbl.getModel();
               if(search_cmb.getSelectedItem().toString().equals("PREVIOUS")){
                 try{
                 if(rs.getInt(8)!=0)   { 
                 da = formatter1.parse(rs.getString(8));
                 getDate = formatter2.format(da);
                edate=rs.getInt(8);
                 }
                 else
                 {
                     getDate="";
                  }
                 if(rs.getInt(9)!=0)   { 
                 
                 da1 = formatter1.parse(rs.getString(9));
                 getDate2 = formatter2.format(da1); 
                 wdate=rs.getInt(9);
                 }
                else
                 {
                     getDate2="";
                 }
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, " Date error","Input Error",JOptionPane.ERROR_MESSAGE);
             }
               
               if(edate ==0 && rs.getInt(10)==0){
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getDouble(6),rs.getDouble(7),getDate ,getDate2,"Not Deposited"};
               tablemodel.addRow(o);
               }
               else if(edate !=0 && rs.getInt(10)==0){
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getDouble(6),rs.getDouble(7),getDate ,getDate2,"Deposited"};
               tablemodel.addRow(o);
               }
               }
               else
               {
           
                   
                    try{
                 if(rs.getInt(9)!=0){
                 da = formatter1.parse(rs.getString(9));
                 getDate = formatter2.format(da);
                 edate=rs.getInt(9);
                 }
                 else
                 {
                     getDate="";
                 }    
                 if( rs.getInt(10)!=0){
                 da1 = formatter1.parse(rs.getString(10));
                 getDate2 = formatter2.format(da1); 
                 wdate=rs.getInt(10);
                 }
                 else
                 {
                     getDate2="";
                 }
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, " Date error","Input Error",JOptionPane.ERROR_MESSAGE);
             }
            if(edate ==0 && rs.getInt(11)==0){
                  
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)+" "+rs.getInt(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8),getDate,getDate2,"Not Deposited"};
               tablemodel.addRow(o);
            }
            else if(edate !=0 && rs.getInt(11)==0){
                  
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)+" "+rs.getInt(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8),getDate,getDate2,"Deposited"};
               tablemodel.addRow(o);
            }
               }
               
               secDep_tbl.getSelectionModel().clearSelection();
               selectedRow =-1;
                    
           }
           
           psmt.close();
           rs.close();
          
            
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No data", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
  
    }
    
    
    
    public void genAll()
    {
        int ser=0;
        tablemodel = (DefaultTableModel) secDep_tbl.getModel();
               
        try{
              psmt=conn.prepareStatement("select hallid,roll,name,dept,batch,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from stuinfo where moneystatus=0");
              rs=psmt.executeQuery();
           String getDate="";
           String getDate2="";
           Date da=null;
           Date da1=null;
                 
              
             while(rs.next()){      
            ser++;
           int edate=0;
           int wdate=0;
          
            try{
           
           
                 if(rs.getInt(9)!=0){
                 da = formatter1.parse(rs.getString(9));
                 getDate = formatter2.format(da);
                 edate=rs.getInt(9);
                 }
                 else
                 {
                     getDate="";
                 }    
                 if( rs.getInt(10)!=0){
                 da1 = formatter1.parse(rs.getString(10));
                 getDate2 = formatter2.format(da1); 
                 wdate=rs.getInt(10);
                 }
                 else
                 {
                     getDate2="";
                 }
             }
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, " Date error","Input Error",JOptionPane.ERROR_MESSAGE);
             }
               
            if(edate ==0 && rs.getInt(11)==0){
                  
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)+" "+rs.getInt(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8),getDate,getDate2,"Not Deposited"};
               tablemodel.addRow(o);
            }
            else if(edate !=0 && rs.getInt(11)==0){
                  
               Object o [] = {ser,rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)+" "+rs.getInt(5),rs.getDouble(6),rs.getDouble(7),rs.getDouble(8),getDate,getDate2,"Deposited"};
               tablemodel.addRow(o);
            }
             }  
                
               psmt.close();
               rs.close();
        }
        
         catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No data", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
       
        PreparedStatement psmt2 = null;
        ResultSet rs2 = null;
    
        try{
             
           
           
            psmt2=conn.prepareStatement("select hallid,roll,name,dept,securitymoney,messad,idcard,depositdate,withdrawdate,moneystatus from previousstudents where moneystatus=0");
           
            rs2=psmt2.executeQuery();
           String getDate="";
           String getDate2="";
           Date da=null;
           Date da1=null;
           
           while(rs2.next())
           {
               ser++;
               int edate=0;
               int wdate=0;
           
               tablemodel = (DefaultTableModel) secDep_tbl.getModel();
                 try{
                 if(rs2.getInt(8)!=0)   { 
                 da = formatter1.parse(rs2.getString(8));
                 getDate = formatter2.format(da);
                edate=rs2.getInt(8);
                 }
                 else
                 {
                     getDate="";
                  }
                 if(rs2.getInt(9)!=0)   { 
                 
                 da1 = formatter1.parse(rs2.getString(9));
                 getDate2 = formatter2.format(da1); 
                 wdate=rs2.getInt(9);
                 }
                else
                 {
                     getDate2="";
                 }
                 }
             
             catch(Exception e){
                 JOptionPane.showMessageDialog(null, " Date error","Input Error",JOptionPane.ERROR_MESSAGE);
             }
               
              
           
               if(edate ==0 && rs2.getInt(10)==0){
               Object o [] = {ser,rs2.getInt(1),rs2.getInt(2),rs2.getString(3),rs2.getString(4),rs2.getDouble(5),rs2.getDouble(6),rs2.getDouble(7),getDate ,getDate2,"Not Deposited"};
               tablemodel.addRow(o);
               }
               else if(edate !=0 && rs2.getInt(10)==0){
               Object o [] = {ser,rs2.getInt(1),rs2.getInt(2),rs2.getString(3),rs2.getString(4),rs2.getDouble(5),rs2.getDouble(6),rs2.getDouble(7),getDate ,getDate2,"Deposited"};
               tablemodel.addRow(o);
               }
        }
               
                    
               secDep_tbl.getSelectionModel().clearSelection();
               selectedRow =-1;
       
           psmt2.close();
           rs2.close();
         
        }
          
        catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No data", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    
    }

    public void findst()
    {
       String st="";
       int id=0,flag=0;
      
       st=id_txt.getText().toString();
       try{
           id=Integer.parseInt(st);
           if(id!=0)
           {
                       tablemodel=(DefaultTableModel) secDep_tbl.getModel();
                       int row=tablemodel.getRowCount();
                       int data=0,data2=0;
                       for(int i=0;i<row;i++)
                       {
                          data= (int) tablemodel.getValueAt(i, 1);
                          data2=(int)tablemodel.getValueAt(i, 2);
                          if(id==data || id ==data2 )
                          {
                              secDep_tbl.requestFocus();
                              secDep_tbl.changeSelection(i,0,false, false);
                              flag=1;
                              focusClickSet(i);
                              break;
                          }
                       }
                       if(flag==0)
                       {
                         JOptionPane.showMessageDialog(null, "No id in Table", "ID Error", JOptionPane.ERROR_MESSAGE);
                       }
                   }
           }
         catch(Exception e)
        {
         JOptionPane.showMessageDialog(null, "No id", "ID Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
      
    public void updateset(String sedep,String messadv, String idfee,String dt,String dt1)
    {    
             String getDate ="",gd1="";
             String getDate2 ="",gd2="";
             int dateserial =0;
             int dateserial2 =0;
             System.out.println("Entrydate" +dt);
             System.out.println("WEntrydate" +dt1);
             SimpleDateFormat dat = new SimpleDateFormat("MMM d,yyyy");
          
          try{
                   if(!dt.equals(""))
                   {
                       Date d1=null;
                       d1=formatter2.parse(dt);
                       getDate=dat.format(d1).toString();
                       gd1=formatter1.format(d1).toString();
                       dateserial=Integer.parseInt(gd1);
                       System.out.println(dateserial);
                       
                       
                   
                   }
                   
                       
                       
          }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Deposit Date is not converted .", "Date Error", JOptionPane.ERROR_MESSAGE); 
        }
          
          try{
                   
              if(stats_cb.isSelected())
                   {
                       Date d1=null;
                       d1=formatter2.parse(dt1);
                       getDate2=dat.format(d1).toString();
                       gd2=formatter1.format(d1).toString();
                       dateserial2=Integer.parseInt(gd2);
                       System.out.println(dateserial2);
                       
                       
                   
                   }
                   
                   
          }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Withdraw Date is not converted .", "Date Error", JOptionPane.ERROR_MESSAGE); 
        }
               if(sedep.equals("") == true)
                    {
                        JOptionPane.showMessageDialog(null, "Security Money Field is empty .","Security Update Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                 
                 if(checkLetter(sedep) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Valid Input","Security Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                 double sedp= Double.parseDouble(sedep) ;

                    if(messadv.equals("") == true)
                    {
                        JOptionPane.showMessageDialog(null, "Mess advance Field is empty .","Mess Adv Update Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    if(checkLetter(messadv) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Valid Price","Price Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    
                    double me= Double.parseDouble(messadv) ;

                    if(idfee.equals("") == true)
                    {
                        JOptionPane.showMessageDialog(null, "Mess advance Field is empty .","Mess Adv Update Error",JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    if(checkLetter(idfee) != true)
                    {
                        JOptionPane.showMessageDialog(null, "Enter Valid Price","Price Update Error", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                    double id= Double.parseDouble(idfee) ;
             
                    int h_id= Integer.parseInt( tablemodel.getValueAt(selectedRow, 1).toString());
                       tablemodel.setValueAt(dec2.format(sedp), selectedRow,5);
                       tablemodel.setValueAt(dec2.format(me), selectedRow, 6);
                       tablemodel.setValueAt(dec2.format(id), selectedRow, 7);
                       tablemodel.setValueAt(getDate, selectedRow,8);
                       if(wdt_ch.isEnabled())
                       { 
                           tablemodel.setValueAt(getDate2, selectedRow,9);
                       }
                       else{
                        
                           tablemodel.setValueAt("", selectedRow,9);
                       }
                       
                       if(!stats_cb.isSelected())
                       {
                           tablemodel.setValueAt("Deposited", selectedRow,10);
                           updatedatabase(h_id,sedp,me,id,dateserial,dateserial2);
    
                       }
                       else if(stats_cb.isSelected())
                       {
                           tablemodel.setValueAt("Withdraw", selectedRow,10);
                           updatedatabase(h_id,sedp,me,id,dateserial,dateserial2);
                       }
    
                       name_lbl.setVisible(false);
                       secDep_txt.setText("");
                       messadv_txt.setText("");
                       idcard_txt.setText("");

    }
    
    
    public void updatedatabase(int hid,double se,double me,double idfe,int ed,int wd)
    {
        try{
            if(stats_cb.isSelected()){
              psmt=conn.prepareStatement("UPDATE previousstudents SET securitymoney=?,messad=?,idcard=?,depositdate=?,withdrawdate=?,moneystatus=1 WHERE hallid=? ");
            }
            else{
            psmt=conn.prepareStatement("UPDATE previousstudents SET securitymoney=?,messad=?,idcard=?,depositdate=?,withdrawdate=?,moneystatus=0 WHERE hallid=? ");
                
            }
              psmt.setDouble(1,se);
              psmt.setDouble(2,me);
              psmt.setDouble(3,idfe);
              psmt.setInt(4,ed);
              psmt.setInt(5,wd);
              psmt.setInt(6,hid);
              psmt.execute();
              psmt.close();
        
          }
          catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, "Data is not updated");
             return;
         }
        
        try{
              psmt=conn.prepareStatement("UPDATE stuinfo SET securitymoney=?,messad=?,idcard=?,depositdate=?,withdrawdate=0,moneystatus=0 WHERE hallid=?");
              psmt.setDouble(1,se);
              psmt.setDouble(2,me);
              psmt.setDouble(3,idfe);
              psmt.setInt(4,ed);
              psmt.setInt(5,hid);
              psmt.execute();
              psmt.close();
              JOptionPane.showMessageDialog(null, "Data is updated");
           
          }
          catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, "Data is not updated");
             return; 
         }
        
        
        
    }
    
    
     //Checking Anyother character else numbers
    public  boolean checkLetter(String S)
    {
        int stlen = S.length();
        char[] c=S.toCharArray();
        int ascii;
        int flag=0;
        
        for(int i =0 ; i<stlen; i++)
        {
            ascii= (int)c[i];
                    if(!(ascii>=48 && ascii<=57) && !(ascii == 46))
                       flag++;
         } 
       
        if(flag !=0)
        return false;
        else
            return true;
    }

    
 
    public void focusClickSet(int i)
    {
        selectedRow=i;
        String Name = tablemodel.getValueAt(selectedRow, 3).toString().trim();
        String id = tablemodel.getValueAt(selectedRow, 1).toString().trim();
        String roll = tablemodel.getValueAt(selectedRow, 2).toString().trim();
        String secM = tablemodel.getValueAt(selectedRow, 5).toString().trim();
        String messadv = tablemodel.getValueAt(selectedRow, 6).toString().trim();
        String idfee = tablemodel.getValueAt(selectedRow, 7).toString().trim();
        String EDATE = tablemodel.getValueAt(selectedRow, 8).toString().trim();
        String WDATE = tablemodel.getValueAt(selectedRow,9).toString().trim();
        
        
        int edate=0;
        int wdate=0;
        secDep_txt.setText(secM);
        messadv_txt.setText(messadv);
        idcard_txt.setText(idfee);
        name_lbl.setVisible(true);
        name_lbl.setText("Name :"+Name+"   Roll:"+roll);

        SimpleDateFormat dt = new SimpleDateFormat("MMM d,yyyy"); 
        try{
            if(!EDATE.equals("")){
            Date date = dt.parse(EDATE); 
            dt_ch.setDate(date);
            }
            else{
                Date date= new Date();
                dt_ch.setDate(date);
            }
            if(!WDATE.equals("")){
            Date date2 = dt.parse(WDATE); 
            wdt_ch.setDate(date2);
            }
            else{
                Date date= new Date();
                wdt_ch.setDate(date);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date is not converted .", "Date Error", JOptionPane.ERROR_MESSAGE); 
        }
        
        secDep_txt.requestFocus();
    }
   
     
     public void mouseClickSet()
    {
        selectedRow=secDep_tbl.getSelectedRow();;
        String Name = tablemodel.getValueAt(selectedRow, 3).toString().trim();
        String id = tablemodel.getValueAt(selectedRow, 1).toString().trim();
        String roll = tablemodel.getValueAt(selectedRow, 2).toString().trim();
        String secM = tablemodel.getValueAt(selectedRow, 5).toString().trim();
        String messadv = tablemodel.getValueAt(selectedRow, 6).toString().trim();
        String idfee = tablemodel.getValueAt(selectedRow, 7).toString().trim();
        String EDATE = tablemodel.getValueAt(selectedRow, 8).toString().trim();
        String WDATE = tablemodel.getValueAt(selectedRow,9).toString().trim();
        
        int edate=0;
        int wdate=0;
        
        secDep_txt.setText(secM);
        messadv_txt.setText(messadv);
        idcard_txt.setText(idfee);
        
         name_lbl.setVisible(true);
        name_lbl.setText("Name :"+Name+"   Roll:"+roll);
        
        SimpleDateFormat dt = new SimpleDateFormat("MMM d,yyyy"); 
        try{
            if(!EDATE.equals("")){
            Date date = dt.parse(EDATE); 
            dt_ch.setDate(date);
            }
            else{
                Date date= new Date();
                dt_ch.setDate(date);
            }
            if(!WDATE.equals("")){
            Date date2 = dt.parse(WDATE); 
            wdt_ch.setDate(date2);
            }
            else{
                Date date= new Date();
                wdt_ch.setDate(date);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Date is not converted .", "Date Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        search_cmb = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        id_txt = new javax.swing.JTextField();
        find_btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        name_lbl = new javax.swing.JLabel();
        update_btn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dt_ch = new com.toedter.calendar.JDateChooser();
        secDep_txt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        messadv_txt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        idcard_txt = new javax.swing.JTextField();
        stats_cb = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        wdt_ch = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        secDep_tbl = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        jLabel1.setText("SEARCH");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("St. Status:");

        search_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        search_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL", "CURRENT", "PREVIOUS" }));
        search_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_cmbActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/user.png"))); // NOI18N
        jLabel4.setText("ID :");

        id_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        id_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_txtActionPerformed(evt);
            }
        });

        find_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        find_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search_1.png"))); // NOI18N
        find_btn.setText("FIND");
        find_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                find_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(search_cmb, 0, 175, Short.MAX_VALUE)
                            .addComponent(id_txt))))
                .addContainerGap(200, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(find_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(76, 76, 76)
                .addComponent(find_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/storeupdate.png"))); // NOI18N
        jLabel2.setText("UPDATE");

        name_lbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        name_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name_lbl.setText("Name & ID :");

        update_btn.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        update_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        update_btn.setText("UPDATE");
        update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_btnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("S. Deposit :");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("E.DATE :");

        dt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        secDep_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        secDep_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secDep_txtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mess Adv. :");

        messadv_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        messadv_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messadv_txtActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("ID Fee:");

        idcard_txt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        idcard_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idcard_txtActionPerformed(evt);
            }
        });

        stats_cb.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        stats_cb.setText("STATUS");
        stats_cb.setOpaque(false);
        stats_cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stats_cbActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("W.DATE :");

        wdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(secDep_txt)
                            .addComponent(messadv_txt)
                            .addComponent(idcard_txt))
                        .addGap(87, 87, 87)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stats_cb))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(name_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(wdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secDep_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stats_cb, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(messadv_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idcard_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );

        secDep_tbl.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        secDep_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Hall ID", "Roll", "Name ", "Dept", "Security Money", "Mess Advance", "ID Card fee", "Deposit Date", "Withdraw Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        secDep_tbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        secDep_tbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
        secDep_tbl.getTableHeader().setReorderingAllowed(false);
        secDep_tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                secDep_tblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(secDep_tbl);
        if (secDep_tbl.getColumnModel().getColumnCount() > 0) {
            secDep_tbl.getColumnModel().getColumn(0).setMaxWidth(80);
            secDep_tbl.getColumnModel().getColumn(1).setMaxWidth(90);
            secDep_tbl.getColumnModel().getColumn(3).setMinWidth(150);
            secDep_tbl.getColumnModel().getColumn(4).setMinWidth(120);
            secDep_tbl.getColumnModel().getColumn(4).setMaxWidth(150);
            secDep_tbl.getColumnModel().getColumn(5).setMinWidth(140);
            secDep_tbl.getColumnModel().getColumn(6).setMinWidth(140);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void messadv_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messadv_txtActionPerformed
         idcard_txt.requestFocus();
    }//GEN-LAST:event_messadv_txtActionPerformed

    private void search_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_cmbActionPerformed
       tm=(DefaultTableModel)secDep_tbl.getModel();
            tm.setRowCount(0);
            int row= secDep_tbl.getRowCount();
            if(row!=0){
            for(int i=0;i<row ;i++)
            {
                tm.removeRow(i);
            }
            }
        stcombo_act();
        id_txt.requestFocus();
    }//GEN-LAST:event_search_cmbActionPerformed

    private void id_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_txtActionPerformed
        find_btn.doClick();
    }//GEN-LAST:event_id_txtActionPerformed

    private void find_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_find_btnActionPerformed
       findst();
    }//GEN-LAST:event_find_btnActionPerformed

    private void secDep_tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_secDep_tblMouseClicked
        mouseClickSet();
    }//GEN-LAST:event_secDep_tblMouseClicked

    private void update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_btnActionPerformed
       String secDp=secDep_txt.getText();
       String mess=messadv_txt.getText();
       String id=idcard_txt.getText();
       String EDATE = tablemodel.getValueAt(selectedRow, 8).toString().trim();
       String WDATE = tablemodel.getValueAt(selectedRow,9).toString().trim();
        String EDATE1 = formatter2.format(dt_ch.getDate()).toString();   
       String WDATE1 = formatter2.format(wdt_ch.getDate()).toString();
        
            updateset(secDp,mess,id,EDATE1,WDATE1);
        
        
    }//GEN-LAST:event_update_btnActionPerformed

    private void secDep_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secDep_txtActionPerformed
        messadv_txt.requestFocus();
    }//GEN-LAST:event_secDep_txtActionPerformed

    private void idcard_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idcard_txtActionPerformed
        update_btn.doClick();
    }//GEN-LAST:event_idcard_txtActionPerformed

    private void stats_cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stats_cbActionPerformed
       if(stats_cb.isSelected())
       {
             wdt_ch.setEnabled(true);
       }
       else if(!stats_cb.isSelected())
       {
             wdt_ch.setEnabled(false);
       }
    }//GEN-LAST:event_stats_cbActionPerformed

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
            java.util.logging.Logger.getLogger(SecDepUpd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SecDepUpd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SecDepUpd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SecDepUpd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SecDepUpd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dt_ch;
    private javax.swing.JButton find_btn;
    private javax.swing.JTextField id_txt;
    private javax.swing.JTextField idcard_txt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messadv_txt;
    private javax.swing.JLabel name_lbl;
    private javax.swing.JComboBox<String> search_cmb;
    private javax.swing.JTable secDep_tbl;
    private javax.swing.JTextField secDep_txt;
    private javax.swing.JCheckBox stats_cb;
    private javax.swing.JButton update_btn;
    private com.toedter.calendar.JDateChooser wdt_ch;
    // End of variables declaration//GEN-END:variables
}
