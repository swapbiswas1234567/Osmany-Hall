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

/**
 *
 * @author Ajmir
 */
public class TemporaryFoodIn extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int flag = 0;

    /**
     * Creates new form TemporaryFoodIn
     */
    public TemporaryFoodIn() {
        initComponents();
        
        
        inittialization();
        Tabledecoration();
        closeBtn();
        tempfoodtable.getColumnModel().getColumn(6).setCellRenderer(new WordWrapCellRenderer());
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
    
    
    public boolean checkitemondatabase(int dateserial, int hallid) {

        //System.out.println(" "+dateserial+" "+hallid);
        try {
            psmt = conn.prepareStatement("select hallid from tempfood where hallid = ? and dateserial=?");
            psmt.setInt(1, hallid);
            psmt.setInt(2, dateserial);
            rs = psmt.executeQuery();
            while (rs.next()) {
                //System.gtiout.println(rs.getInt(1));
                psmt.close();
                rs.close();
                return true;
            }

        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        }

        return false;

    }

    public boolean checkinjtable(String hallid, String date, int index) {
        int totalrow = -1;
        totalrow = tempfoodtable.getRowCount();
        //System.out.println(hallid+" "+date);
        for (int i = 0; i < totalrow; i++) {
            if (model.getValueAt(i, 0).toString().equals(hallid) && model.getValueAt(i, 4).toString().equals(date)
                    && i != index) {
                return true;
            }
        }

        return false;
    }

    public void clearinsertfields() {
        inserthallid.setText("");
        insertbill.setText("");
        insertremarks.setText("");
    }

    public void clearupdate() {
        updatehallid.setText("");
        updatebill.setText("");
        updateremarks.setText("");
    }

    public void deleterow(int selectedrow) {
        int responce = JOptionPane.showConfirmDialog(this, "Do You Want To Delete"
                + " The Selected Row ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION) {
            tablemodel = (DefaultTableModel) tempfoodtable.getModel();
            tablemodel.removeRow(selectedrow);
        }

    }

    public void inittialization() {
        conn = Jconnection.ConnecrDb(); // set connection with database
        formatter = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        formatter1 = new SimpleDateFormat("MMM dd,yyyy");
        Date todaysdate = new Date();
        insertdatechooser.setDate(todaysdate);  // setting both datechooser todays date
        updatedatechooser.setDate(todaysdate);

        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) insertdatechooser.getDateEditor();
        dtedit.setEditable(false);

        JTextFieldDateEditor dtedit1;
        dtedit1 = (JTextFieldDateEditor) updatedatechooser.getDateEditor();
        dtedit1.setEditable(false);

        dec = new DecimalFormat("#0.00");
        model = tempfoodtable.getModel();

        inserthallid.requestFocus();
        
        
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

    public void insertdatabase() {
        String hallid = "", bill = "", strdate = "", remarks = "";
        int dateserial = 0, id = 0;
        Double totalbill = 0.00;
        Date date = null;
        int totalrow = tempfoodtable.getRowCount();
        for (int i = 0; i < totalrow; i++) {
            hallid = model.getValueAt(i, 0).toString();
            bill = model.getValueAt(i, 5).toString();
            strdate = model.getValueAt(i, 4).toString();
            remarks = model.getValueAt(i, 6).toString();
            //System.out.println(hallid+" "+bill+" "+strdate);

            try {
                date = formatter1.parse(strdate);
                dateserial = Integer.parseInt(formatter.format(date));

            } catch (NumberFormatException | ParseException e) {
                JOptionPane.showMessageDialog(null, "date parsing error"
                        + "while inserting data", "date error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {

                totalbill = Double.parseDouble(bill);
                id = Integer.parseInt(hallid);
                //System.out.print(dateserial);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "bill/ hall id convertion error", "date error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //System.out.println(dateserial+" "+id+" "+totalbill);
            try {
                psmt = conn.prepareStatement("insert into tempfood (hallid, dateserial, bill,remarks) values(?, ? ,?,?)");
                psmt.setInt(1, id);
                psmt.setInt(2, dateserial);
                psmt.setDouble(3, totalbill);
                psmt.setString(4, remarks);
                psmt.execute();
                psmt.close();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "insertion error"
                        + " in temp food", "Data insertion error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    public void settable(Date date, String hallid, String bill, String remarks) {
        String name = "", strdate = "";
        int room = 0, roll = 0, id = 0, dateserial = 0;
        Double totalbill = 0.00;

        try {
            totalbill = Double.parseDouble(bill);
            id = Integer.parseInt(hallid);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bill/ Hall Id type error", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            strdate = formatter1.format(date);
            dateserial = Integer.parseInt(formatter.format(date));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "date formate error ", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            psmt = conn.prepareStatement("select roll, name, roomno from stuinfo where hallid = ? ");
            psmt.setInt(1, id);
            rs = psmt.executeQuery();
            while (rs.next()) {
                //System.gtiout.println(rs.getInt(1));
                roll = rs.getInt(1);
                name = rs.getString(2);
                room = rs.getInt(3);
            }
            psmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch"
                    + " hall id from database ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean checkdatabase = checkitemondatabase(dateserial, id);
        boolean tablecheck = checkinjtable(hallid, strdate, -1);
        //System.out.println(tablecheck);

        if (roll != 0 && !name.equals("") && room != 0 && !checkdatabase && !tablecheck && totalbill > 0) {
            //System.out.println(roll+" "+name);
            tablemodel = (DefaultTableModel) tempfoodtable.getModel();
            Object o[] = {hallid, roll, name, room, strdate, dec.format(totalbill), remarks};
            tablemodel.addRow(o);

            clearinsertfields();
            inserthallid.requestFocus();

            //updateRowHeights();
        } else if (checkdatabase) {
            JOptionPane.showMessageDialog(null, "Data already inserted "
                    + "for same date", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else if (tablecheck) {
            JOptionPane.showMessageDialog(null, "Same hall id date "
                    + "exist in JTable", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else if (totalbill <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid Bill", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Hall id doesnot exist"
                    + " enter correct hall id", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updatefield(int selectedrow) {
        String strdate = "", strhallid = "", strbill = "", remarks = "";
        Date date = null;

        strdate = model.getValueAt(selectedrow, 4).toString();
        strhallid = model.getValueAt(selectedrow, 0).toString();
        strbill = model.getValueAt(selectedrow, 5).toString();
        remarks = model.getValueAt(selectedrow, 6).toString();

        try {
            date = formatter1.parse(strdate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Date Parse "
                    + "error in Updatefield", "Date parsing error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        updatedatechooser.setDate(date);
        updatehallid.setText(strhallid);
        updatebill.setText(strbill);
        updateremarks.setText(remarks);
    }

    public void updatetable(int selectedrow) {
        String strdate = "", strbill = "", strhallid = "", remarks = "", name = "";
        Date date = null;
        int dateserial = 0, hallid = 0, roll = 0, room = 0;
        Double amount = 0.00;

        date = updatedatechooser.getDate();
        strbill = updatebill.getText().trim();
        strhallid = updatehallid.getText().trim();
        remarks = updateremarks.getText().trim();

        try {
            strdate = formatter1.format(date);
            dateserial = Integer.parseInt(formatter.format(date));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "date formate error ", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            amount = Double.parseDouble(strbill);
            hallid = Integer.parseInt(strhallid);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "hallid/amount format error", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            psmt = conn.prepareStatement("select roll, name, roomno from stuinfo where hallid = ? ");
            psmt.setInt(1, hallid);
            rs = psmt.executeQuery();
            while (rs.next()) {
                //System.gtiout.println(rs.getInt(1));
                roll = rs.getInt(1);
                name = rs.getString(2);
                room = rs.getInt(3);
            }
            psmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch"
                    + " hall id from database ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean checkdatabase = checkitemondatabase(dateserial, hallid);
        boolean tablecheck = checkinjtable(strhallid, strdate, selectedrow);

        //System.out.println(checkdatabase+" "+tablecheck);
        if (roll != 0 && !name.equals("") && room != 0 && !checkdatabase && !tablecheck && amount > 0) {
            //System.out.println(roll+" "+name);
            model.setValueAt(strhallid, selectedrow, 0);
            model.setValueAt(roll, selectedrow, 1);
            model.setValueAt(name, selectedrow, 2);
            model.setValueAt(room, selectedrow, 3);
            model.setValueAt(strdate, selectedrow, 4);
            model.setValueAt(dec.format(amount), selectedrow, 5);
            model.setValueAt(remarks, selectedrow, 6);

            inserthallid.requestFocus();
            clearupdate();
            tempfoodtable.clearSelection();
            //updateRowHeights();

        } else if (checkdatabase) {
            JOptionPane.showMessageDialog(null, "Data already inserted "
                    + "for same date", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else if (tablecheck) {
            JOptionPane.showMessageDialog(null, "Same hall id date "
                    + "exist in JTable", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid Bill", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Hall id doesnot exist"
                    + " enter correct hall id", "Data insertion error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void Tabledecoration() {
        tempfoodtable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        tempfoodtable.getTableHeader().setOpaque(false);
        tempfoodtable.getTableHeader().setBackground(new Color(32, 136, 203));
        tempfoodtable.getTableHeader().setForeground(new Color(255, 255, 255));
        tempfoodtable.setRowHeight(25);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tempfoodtable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        tempfoodtable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        //tempfoodtable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        
        
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
        jScrollPane1 = new javax.swing.JScrollPane();
        insertremarks = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        insertbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        updatedatechooser = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        updatehallid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        updatebill = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        updateremarks = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        updatebtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tempfoodtable = new javax.swing.JTable();
        saveandexitbtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/snacks.png"))); // NOI18N
        jLabel1.setText("TEMPORARY FOOD IN");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Date");

        insertdatechooser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        insertdatechooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                insertdatechooserPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Hall Id");

        inserthallid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inserthallidActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Bill ");

        insertbill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertbillActionPerformed(evt);
            }
        });

        insertremarks.setColumns(20);
        insertremarks.setLineWrap(true);
        insertremarks.setRows(5);
        jScrollPane1.setViewportView(insertremarks);

        jLabel5.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel5.setText("Remarks");

        insertbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        insertbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/noninsert.png"))); // NOI18N
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inserthallid, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insertbill, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insertdatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(29, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(insertdatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inserthallid, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(insertbill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/edit.png"))); // NOI18N
        jLabel6.setText(" UPDATE");

        jLabel7.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Date");

        updatedatechooser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Hall Id");

        jLabel9.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Bill ");

        updateremarks.setColumns(20);
        updateremarks.setLineWrap(true);
        updateremarks.setRows(5);
        jScrollPane2.setViewportView(updateremarks);

        jLabel10.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jLabel10.setText("Remarks");

        updatebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        updatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/rubber.png"))); // NOI18N
        updatebtn.setText("Update");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
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
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(updatedatechooser, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(updatehallid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(updatebill, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(updatebtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updatedatechooser, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updatehallid, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(updatebill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        tempfoodtable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tempfoodtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hall ID", "Roll", "Name", "Room No", "Date", "Bill", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tempfoodtable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tempfoodtable.setRowHeight(26);
        tempfoodtable.setSelectionBackground(new java.awt.Color(232, 57, 97));
        tempfoodtable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        tempfoodtable.setShowVerticalLines(false);
        tempfoodtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tempfoodtableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tempfoodtable);

        saveandexitbtn.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        saveandexitbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save&exitbtn (2).png"))); // NOI18N
        saveandexitbtn.setText(" Save & Exit");
        saveandexitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveandexitbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
            .addComponent(saveandexitbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveandexitbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void insertbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbtnActionPerformed
        // TODO add your handling code here:
        Date date = null;
        String hallid = "", bill = "", remarks = "";
        date = insertdatechooser.getDate();
        hallid = inserthallid.getText().trim();
        bill = insertbill.getText().trim();
        remarks = insertremarks.getText().trim();
        if (!hallid.equals("") && !bill.equals("")) {
            settable(date, hallid, bill, remarks);
        } else if (hallid.equals("")) {
            JOptionPane.showMessageDialog(null, "Hall id can't be empty", "Data insert error", JOptionPane.ERROR_MESSAGE);
        } else if (bill.equals("")) {
            JOptionPane.showMessageDialog(null, "bill can't be empty", "Data insert error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_insertbtnActionPerformed

    private void tempfoodtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tempfoodtableMouseClicked
        // TODO add your handling code here:
        int selectedrow = -1;
        //System.out.println(selectedrow+" called");
        selectedrow = tempfoodtable.getSelectedRow();
        if (selectedrow >= 0) {
            updatefield(selectedrow);
        }
//        else{
//            JOptionPane.showMessageDialog(null, "Hall id can't be empty", "Data insert error", JOptionPane.ERROR_MESSAGE);
//        }

    }//GEN-LAST:event_tempfoodtableMouseClicked

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow = -1;
        selectedrow = tempfoodtable.getSelectedRow();

        if (selectedrow >= 0) {
            updatetable(selectedrow);
        } else {
            JOptionPane.showMessageDialog(null, "No row is selected", "Data insert error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updatebtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        // TODO add your handling code here:
        int selectedrow = -1;
        selectedrow = tempfoodtable.getSelectedRow();

        if (selectedrow >= 0) {
            deleterow(selectedrow);
            clearupdate();
        } else {
            JOptionPane.showMessageDialog(null, "No row is selected", "Data delete error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void saveandexitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveandexitbtnActionPerformed
        // TODO add your handling code here:
        int responce = JOptionPane.showConfirmDialog(this, "Do you want to save"
                + " the inserted items ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (responce == JOptionPane.YES_OPTION) {
            try {
                // TODO add your handling code here:
                insertdatabase();
                JFrame frame = this;
                NewDashboard das = new NewDashboard();
                das.setVisible(true);
                frame.setVisible(false);
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(TemporaryFoodIn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_saveandexitbtnActionPerformed

    private void insertdatechooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_insertdatechooserPropertyChange
        // TODO add your handling code here:
        inserthallid.requestFocus();
    }//GEN-LAST:event_insertdatechooserPropertyChange

    private void inserthallidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inserthallidActionPerformed
        // TODO add your handling code here:

        insertbill.requestFocus();
    }//GEN-LAST:event_inserthallidActionPerformed

    private void insertbillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertbillActionPerformed
        // TODO add your handling code here:

        insertremarks.requestFocus();
    }//GEN-LAST:event_insertbillActionPerformed

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
            java.util.logging.Logger.getLogger(TemporaryFoodIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TemporaryFoodIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TemporaryFoodIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TemporaryFoodIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TemporaryFoodIn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField insertbill;
    private javax.swing.JButton insertbtn;
    private com.toedter.calendar.JDateChooser insertdatechooser;
    private javax.swing.JTextField inserthallid;
    private javax.swing.JTextArea insertremarks;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton saveandexitbtn;
    private javax.swing.JTable tempfoodtable;
    private javax.swing.JTextField updatebill;
    private javax.swing.JButton updatebtn;
    private com.toedter.calendar.JDateChooser updatedatechooser;
    private javax.swing.JTextField updatehallid;
    private javax.swing.JTextArea updateremarks;
    // End of variables declaration//GEN-END:variables
}
