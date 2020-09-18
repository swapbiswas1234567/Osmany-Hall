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
import static omms.SendMail.flag;

/**
 *
 * @author Asus
 */
public class AccountPayment extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    int serial = 0;
    int row = -1;

    public class Info {

        int hallid;
        String name;
        String roll;
        String room;
        double due;

        public Info() {
            hallid = 0;
            name = "";
            roll = "";
            room = "";
            due = 0;
        }

        public Info(int hi, String nm, String rl, String rm, double d) {
            hallid = hi;
            name = nm;
            roll = rl;
            room = rm;
            due = d;
        }
    }

    /**
     * Creates new form AccountPayment
     */
    public AccountPayment() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers();
        Tabledecoration();
        stIdTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
        JTextFieldDateEditor jt = new JTextFieldDateEditor();
        jt = (JTextFieldDateEditor) stPayDate.getDateEditor();
        jt.setEditable(false);

        jt = (JTextFieldDateEditor) upPayDate.getDateEditor();
        jt.setEditable(false);
        tablemodel = (DefaultTableModel) stdPayTable.getModel();

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
                    switch (UserLog.name) {
                        case "accountant": {
                            DashboardAccountant das = new DashboardAccountant();
                            das.setVisible(true);
                            frame.setVisible(false);
                            break;
                        }
                        case "provost": {
                            DashboardHallAutho das = new DashboardHallAutho();
                            das.setVisible(true);
                            frame.setVisible(false);
                            break;
                        }
                        case "mess": {
                            DashboardMess das = new DashboardMess();
                            das.setVisible(true);
                            frame.setVisible(false);
                            break;
                        }
                        case "captain": {
                            DashboardMessCap das = new DashboardMessCap();
                            das.setVisible(true);
                            frame.setVisible(false);
                            break;
                        }
                        default:
                            break;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void closeWindow() {
        try {
            conn.close();
            switch (UserLog.name) {
                case "accountant": {
                    DashboardAccountant das = new DashboardAccountant();
                    das.setVisible(true);
                    this.setVisible(false);
                    break;
                }
                case "provost": {
                    DashboardHallAutho das = new DashboardHallAutho();
                    das.setVisible(true);
                    this.setVisible(false);
                    break;
                }
                case "mess": {
                    DashboardMess das = new DashboardMess();
                    das.setVisible(true);
                    this.setVisible(false);
                    break;
                }
                case "captain": {
                    DashboardMessCap das = new DashboardMessCap();
                    das.setVisible(true);
                    this.setVisible(false);
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDateChoosers() {
        Date date = new Date();
        stPayDate.setDate(date);
        upPayDate.setDate(date);
    }

    public void Tabledecoration() {
        stdPayTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        stdPayTable.getTableHeader().setOpaque(false);
        stdPayTable.getTableHeader().setBackground(new Color(32, 136, 203));
        stdPayTable.getTableHeader().setForeground(new Color(255, 255, 255));
        stdPayTable.setRowHeight(25);
        stdPayTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        stdPayTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        stdPayTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
    }

    public void setPayTable(Info inf, String pd, Double pa, String md, String rf) {
        String payDate = pd;
        int id = inf.hallid;
        String name = inf.name;
        String roll = inf.roll;
        String room = inf.room;
        double due = inf.due;
        Double paidAmnt = pa;
        String media = md;
        String ref = rf;

        int dup = 0;
        String msg = "Already Payment Inserted for " + inf.name + "(" + inf.roll + ")\n";

        for (int i = 0; i < tablemodel.getRowCount(); i++) {
            int dt1 = 0;
            try {
                dt1 = Integer.parseInt(formatDate.format(tableDateFormatter.parse(pd)));
            } catch (ParseException ex) {
                Logger.getLogger(AccountPayment.class.getName()).log(Level.SEVERE, null, ex);
            }
            int dt2 = 0;
            try {
                dt2 = Integer.parseInt(formatDate.format(tableDateFormatter.parse(tablemodel.getValueAt(i, 1).toString())));
            } catch (ParseException ex) {
                Logger.getLogger(AccountPayment.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(dt1 + "  " + dt2);
            if (inf.hallid == Integer.parseInt(tablemodel.getValueAt(i, 2).toString()) && dt1 == dt2) {
                msg += tablemodel.getValueAt(i, 0).toString() + ". " + tablemodel.getValueAt(i, 6).toString() + " Tk. \n";
                dup++;
            }
        }
        if (dup > 0) {
            int responce = JOptionPane.showConfirmDialog(this, msg, "Duplicate Insertion On " + payDate + ". Confirm!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.NO_OPTION) {
                return;
            }
        }

        serial++;
        Object obj[] = {serial, payDate, id, name, roll, room, paidAmnt, media, ref, due};
        tablemodel.addRow(obj);
        clearAllStdPay();
    }

    public void setUpdatedPayTable(Info inf, String pd, Double pa, String md, String rf) {
        tablemodel.setValueAt(inf.hallid, row, 2);
        tablemodel.setValueAt(pd, row, 1);
        tablemodel.setValueAt(inf.name, row, 3);
        tablemodel.setValueAt(inf.roll, row, 4);
        tablemodel.setValueAt(inf.room, row, 5);
        tablemodel.setValueAt(pa, row, 6);
        tablemodel.setValueAt(md, row, 7);
        tablemodel.setValueAt(rf, row, 8);
        clearAllUpd();
    }

    public void callSetTableForStdPay(String id) {
        Info inf = findInfos(id);
        if (inf == null) {
            return;
        }
        String payDate = tableDateFormatter.format(stPayDate.getDate());
        Double paidAmnt;
        try {
            paidAmnt = Double.parseDouble(stPaidAmntTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Inserted Paid Amount is Worng", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String media = stMediaCombo.getSelectedItem().toString();
        String ref = stRefTxt.getText();
        setPayTable(inf, payDate, paidAmnt, media, ref);
    }

    public void callSetTableForUpd(String id) {
        Info inf = findInfos(id);
        if (inf == null) {
            return;
        }
        String payDate = tableDateFormatter.format(upPayDate.getDate());
        Double paidAmnt;
        try {
            paidAmnt = Double.parseDouble(upPaidAmntTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Inserted Paid Amount is Worng in Set", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String media = upMediaCombo.getSelectedItem().toString();
        String ref = upRefTxt.getText();
        setUpdatedPayTable(inf, payDate, paidAmnt, media, ref);
    }

    public Info findInfos(String strId) {
        Info inf = new Info();
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Inserted Hall Id/Roll is Worng", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, totaldue FROM stuinfo WHERE hallid = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                inf = new Info(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
                ps.close();
                rs.close();
                return inf;
            }

            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, totaldue FROM stuinfo WHERE roll = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                inf = new Info(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
                ps.close();
                rs.close();
                return inf;
            }

            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        JOptionPane.showMessageDialog(null, "Inserted Hall Id/Roll can't be found in Current Student's Database", "Data No Found", JOptionPane.ERROR_MESSAGE);
        return null;

    }

    public void setUpdate(int row) {
        upIdTxt.setText(tablemodel.getValueAt(row, 2).toString());
        try {
            Date date = tableDateFormatter.parse(tablemodel.getValueAt(row, 1).toString());
        } catch (ParseException ex) {
            Logger.getLogger(AccountPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        upPaidAmntTxt.setText(tablemodel.getValueAt(row, 6).toString());
        upMediaCombo.setSelectedItem(tablemodel.getValueAt(row, 7).toString());
        upRefTxt.setText(tablemodel.getValueAt(row, 8).toString());
    }

    public void clearAllStdPay() {
        stIdTxt.setText("");
        stPaidAmntTxt.setText("");
        stRefTxt.setText("");
        stMediaCombo.setSelectedIndex(0);
        setDateChoosers();
    }

    public void clearAllUpd() {
        upIdTxt.setText("");
        upPaidAmntTxt.setText("");
        upRefTxt.setText("");
        upMediaCombo.setSelectedIndex(0);
        setDateChoosers();
    }

    public void sendmail(int hallid, String paymentdate, Double previousdue, Double paidamount) {
        String body = "", greetings = "", tail = "", msg = "", strhallid = "", strcurrentdue = "";
        boolean isval = false;
        Double currentdue = 0.0;
        String subject = "Confirmation of Mess Bill Payment(" + paymentdate + ")";

        currentdue = previousdue - paidamount;
        if (currentdue < 0) {
            strcurrentdue = Double.toString(currentdue) + "(Advanced)";
        } else if (currentdue > 0) {
            strcurrentdue = Double.toString(currentdue) + " (Due)";
        } else {
            strcurrentdue = Double.toString(currentdue);
        }

        try {

            ps = conn.prepareStatement("select st.name,st.email,from stuinfo st where st.hallid =? ");
            ps.setInt(1, hallid);
            rs = ps.executeQuery();

            greetings = "Assalamualaikum ";
            body = "You Mess bill paid on " + paymentdate + " has inserted on the database successfully. "
                    + "Your last payment and total due details are given below \n";
            tail = "Best regards,\nOsmany Hall Authority\nMIST, Mirpur Cantonment";
            while (rs.next()) {
                greetings = greetings + " " + rs.getString(1) + ",\n";
                body = body + "\n Previous Due: " + previousdue + " \n Paid Amount : " + paidamount + "\n Current Bill : " + strcurrentdue;
                msg = greetings + body + tail;
                Email.send("mist.osmanyhall@gmail.com", "osm@nycse17", rs.getString(2), subject, msg, strhallid);
                isval = true;
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetch for Mail Send Failed", "Sending Mail error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void savePayData() {
        if (tablemodel.getRowCount() > 0) {
            for (int i = 0; i < tablemodel.getRowCount(); i++) {
                int id = (int) tablemodel.getValueAt(i, 2);
                Double paidAmnt = Double.parseDouble(tablemodel.getValueAt(i, 6).toString());
                Date date;
                int payDate;
                int insertDate;
                try {
                    date = tableDateFormatter.parse(tablemodel.getValueAt(i, 1).toString());
                    payDate = Integer.parseInt(formatDate.format(date));
                    date = new Date();
                    insertDate = Integer.parseInt(formatDate.format(date));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Date parsing error", "Parsing Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String media = tablemodel.getValueAt(i, 7).toString();
                String ref = tablemodel.getValueAt(i, 8).toString();

                try {
                    //INSERT INTO paymenthistory VALUES (1315, 1000, 20200908, 20200909, "Bank", "")
                    ps = conn.prepareStatement("INSERT INTO paymenthistory (hallid, paidamount, paymentdate, insertdate, media, reference) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setDouble(2, paidAmnt);
                    ps.setInt(3, payDate);
                    ps.setInt(4, insertDate);
                    ps.setString(5, media);
                    ps.setString(6, ref);
                    ps.execute();

                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AccountPayment.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }

                try {
                    ps = conn.prepareStatement("UPDATE stuinfo SET totaldue = totaldue - ? WHERE hallid = ?");
                    ps.setDouble(1, paidAmnt);
                    ps.setInt(2, id);
                    ps.execute();

                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AccountPayment.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }

            }
        } else {
            JOptionPane.showMessageDialog(null, "No data found on table", "Data Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        upIdTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        upPayDate = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        upPaidAmntTxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        upMediaCombo = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        upRefTxt = new javax.swing.JTextField();
        upUpdatebtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        stPayDate = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        stIdTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        stPaidAmntTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        stRefTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        stMediaCombo = new javax.swing.JComboBox<>();
        stInsertBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stdPayTable = new javax.swing.JTable();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 209, 160));

        jLabel2.setFont(new java.awt.Font("Bell MT", 1, 30)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/nonstoredupdate.png"))); // NOI18N
        jLabel2.setText("Update");
        jLabel2.setToolTipText("");

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel9.setText("Hall Id / Roll");

        upIdTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        upIdTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upIdTxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel4.setText("Payment Date");

        upPayDate.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        upPayDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                upPayDatePropertyChange(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel10.setText("Paid Amount");

        upPaidAmntTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        upPaidAmntTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upPaidAmntTxtActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel11.setText("Media");

        upMediaCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        upMediaCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bank", "bKash", "Card", "Others" }));
        upMediaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upMediaComboActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel12.setText("Referrence");

        upRefTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        upRefTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upRefTxtActionPerformed(evt);
            }
        });

        upUpdatebtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        upUpdatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/pay_update_32px.png"))); // NOI18N
        upUpdatebtn.setText(" Update");
        upUpdatebtn.setBorder(null);
        upUpdatebtn.setFocusPainted(false);
        upUpdatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upUpdatebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(upRefTxt))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(upPayDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(upIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(upPaidAmntTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(upMediaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(upUpdatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upPaidAmntTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(upMediaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(upRefTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(upUpdatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 213, 219));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 30)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/card_payment_32px.png"))); // NOI18N
        jLabel1.setText("Student Payment");
        jLabel1.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel3.setText("Payment Date");

        stPayDate.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stPayDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                stPayDatePropertyChange(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel5.setText("Hall Id / Roll");

        stIdTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stIdTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stIdTxtActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel6.setText("Paid Amount");

        stPaidAmntTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stPaidAmntTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stPaidAmntTxtActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel7.setText("Referrence");

        stRefTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stRefTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stRefTxtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel8.setText("Media");

        stMediaCombo.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stMediaCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bank", "bKash", "Card", "Others" }));
        stMediaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stMediaComboActionPerformed(evt);
            }
        });

        stInsertBtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        stInsertBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/Enter.png"))); // NOI18N
        stInsertBtn.setText(" Insert");
        stInsertBtn.setBorder(null);
        stInsertBtn.setFocusPainted(false);
        stInsertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stInsertBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(112, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(stRefTxt))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stPayDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stPaidAmntTxt)
                            .addComponent(stMediaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(stInsertBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)))
                .addGap(0, 72, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stPaidAmntTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(stMediaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stRefTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(stInsertBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        stdPayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SN", "Payment Date", "Hall Id", "Name", "Roll", "Room No", "Paid Amount", "Media", "Referrence", "Previous Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stdPayTable.setSelectionBackground(new java.awt.Color(204, 0, 0));
        stdPayTable.setSelectionForeground(new java.awt.Color(204, 204, 204));
        stdPayTable.getTableHeader().setReorderingAllowed(false);
        stdPayTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                stdPayTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(stdPayTable);
        if (stdPayTable.getColumnModel().getColumnCount() > 0) {
            stdPayTable.getColumnModel().getColumn(0).setMinWidth(65);
            stdPayTable.getColumnModel().getColumn(0).setMaxWidth(85);
        }

        saveBtn.setBackground(new java.awt.Color(204, 204, 204));
        saveBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/save_as_42px.png"))); // NOI18N
        saveBtn.setText("Save & Exit");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(saveBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void stIdTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stIdTxtActionPerformed
        // TODO add your handling code here:
        stPayDate.requestFocusInWindow();
    }//GEN-LAST:event_stIdTxtActionPerformed

    private void stPaidAmntTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stPaidAmntTxtActionPerformed
        // TODO add your handling code here:
        stMediaCombo.requestFocusInWindow();
    }//GEN-LAST:event_stPaidAmntTxtActionPerformed

    private void stMediaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stMediaComboActionPerformed
        // TODO add your handling code here:
        stRefTxt.requestFocusInWindow();
    }//GEN-LAST:event_stMediaComboActionPerformed

    private void stInsertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stInsertBtnActionPerformed
        // TODO add your handling code here:
        callSetTableForStdPay(stIdTxt.getText().trim());
        stIdTxt.requestFocusInWindow();
    }//GEN-LAST:event_stInsertBtnActionPerformed

    private void stRefTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stRefTxtActionPerformed
        // TODO add your handling code here:
        stInsertBtn.doClick();
    }//GEN-LAST:event_stRefTxtActionPerformed

    private void stPayDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_stPayDatePropertyChange
        // TODO add your handling code here:
        if (stPayDate.getDate() != null) {
            stPaidAmntTxt.requestFocusInWindow();
        }
    }//GEN-LAST:event_stPayDatePropertyChange

    private void stdPayTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stdPayTableMousePressed
        // TODO add your handling code here:
        row = -1;
        row = stdPayTable.getSelectedRow();
        if (row >= 0) {
            setUpdate(row);
            upIdTxt.requestFocusInWindow();
        }
    }//GEN-LAST:event_stdPayTableMousePressed

    private void upUpdatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upUpdatebtnActionPerformed
        // TODO add your handling code here:
        callSetTableForUpd(upIdTxt.getText().trim());
        stIdTxt.requestFocusInWindow();
    }//GEN-LAST:event_upUpdatebtnActionPerformed

    private void upIdTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upIdTxtActionPerformed
        // TODO add your handling code here:
        upPayDate.requestFocusInWindow();
    }//GEN-LAST:event_upIdTxtActionPerformed

    private void upPayDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_upPayDatePropertyChange
        // TODO add your handling code here:
        upPaidAmntTxt.requestFocusInWindow();
    }//GEN-LAST:event_upPayDatePropertyChange

    private void upPaidAmntTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upPaidAmntTxtActionPerformed
        // TODO add your handling code here:
        upMediaCombo.requestFocusInWindow();
    }//GEN-LAST:event_upPaidAmntTxtActionPerformed

    private void upMediaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upMediaComboActionPerformed
        // TODO add your handling code here:
        upRefTxt.requestFocusInWindow();
    }//GEN-LAST:event_upMediaComboActionPerformed

    private void upRefTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upRefTxtActionPerformed
        // TODO add your handling code here:
        upUpdatebtn.doClick();
    }//GEN-LAST:event_upRefTxtActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
        if (tablemodel.getRowCount() > 0) {
            int responce = JOptionPane.showConfirmDialog(this, "Do you want to save the data ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION) {
                savePayData();
                closeWindow();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No item is inserted on the table", "Table item not found", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AccountPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountPayment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField stIdTxt;
    private javax.swing.JButton stInsertBtn;
    private javax.swing.JComboBox<String> stMediaCombo;
    private javax.swing.JTextField stPaidAmntTxt;
    private com.toedter.calendar.JDateChooser stPayDate;
    private javax.swing.JTextField stRefTxt;
    private javax.swing.JTable stdPayTable;
    private javax.swing.JTextField upIdTxt;
    private javax.swing.JComboBox<String> upMediaCombo;
    private javax.swing.JTextField upPaidAmntTxt;
    private com.toedter.calendar.JDateChooser upPayDate;
    private javax.swing.JTextField upRefTxt;
    private javax.swing.JButton upUpdatebtn;
    // End of variables declaration//GEN-END:variables
}
