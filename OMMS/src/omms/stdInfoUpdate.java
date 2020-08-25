/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class stdInfoUpdate extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    SimpleDateFormat formatDate = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate1 = new SimpleDateFormat("yyyyMMdd");

    int hallId = 0;
    String rollNo = "";

    /**
     * Creates new form stdInfoUpdate
     */
    public stdInfoUpdate() {
        initComponents();
        initialize();
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        //setDateChoosers(); // setting todays date to the date chooser
        nameTxt.requestFocus();
        //closeBtn();
    }

    /*
        Seeting the cross button action to Dashboard
     */
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    Dashboard das = new Dashboard();
                    das.setVisible(true);
                    frame.setVisible(false);
                    conn.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /*
        Setting the date choosers to todays current date
     */
    public void setDateChoosers() {
        Date todaysDate = new Date();
        dobDateChooser.setDate(todaysDate);
    }

    public void clearAll() {
        nameTxt.setText("");
        rollTxt.setText("");
        deptComboBox.setSelectedIndex(0);
        batchTxt.setText("");
        contactNoTxt.setText("");
        roomNoTxt.setText("");
        bloodComboBox.setSelectedIndex(0);
        religionComboBox.setSelectedIndex(0);

        fNameTxt.setText("");
        mNameTxt.setText("");
        permAddTxt.setText("");
        presAddTxt.setText("");
        dobDateChooser.setDate(null);
        //setDateChoosers();
    }

    public boolean rollValidity(String roll) {
        //System.out.println("New roll " + roll + "Old roll " + rollNo);
        try {
            if (!roll.equals(rollNo)) {
                ps = conn.prepareStatement("SELECT * FROM stuinfo WHERE roll = ?");
                ps.setString(1, roll);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ps.close();
                    rs.close();
                    JOptionPane.showMessageDialog(null, "This roll is used before", "Invalid Roll Inserted", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error", "Database Problem", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public void setStuInfo(int hallId) {
        try {
            ps = conn.prepareStatement("SELECT * FROM stuinfo WHERE hallid = ?");
            ps.setInt(1, hallId);
            rs = ps.executeQuery();
            if (rs.next()) {
                rollNo = rs.getString(2);
                rollTxt.setText(rollNo);
                nameTxt.setText(rs.getString(3));
                fNameTxt.setText(rs.getString(4));
                mNameTxt.setText(rs.getString(5));
                deptComboBox.setSelectedItem(rs.getString(6));
                batchTxt.setText(String.valueOf(rs.getInt(7)));
                contactNoTxt.setText(rs.getString(9));
                bloodComboBox.setSelectedItem(rs.getString(10));
                religionComboBox.setSelectedItem(rs.getString(12));
                Date dob = formatDate1.parse(rs.getString(13));
                if (dob != null) {
                    dobDateChooser.setDate(dob);
                }
                permAddTxt.setText(rs.getString(14));
                presAddTxt.setText(rs.getString(15));
                roomNoTxt.setText(rs.getString(16));
            } else {
                JOptionPane.showMessageDialog(null, "Hall Id can't be found", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error", "Database Connection Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void updateStdInfo() {
        String name = nameTxt.getText().trim();
        String roll = rollTxt.getText().trim();
        String dept = deptComboBox.getSelectedItem().toString().trim();
        int batch = Integer.parseInt(batchTxt.getText().trim());
        String blood = bloodComboBox.getSelectedItem().toString().trim();
        String religion = religionComboBox.getSelectedItem().toString().trim();
        String roomNo = roomNoTxt.getText().trim();

        String fname = fNameTxt.getText().trim();
        String mname = mNameTxt.getText().trim();
        String permAdd = permAddTxt.getText().trim();
        String presAdd = presAddTxt.getText().trim();
        Date dobDate = dobDateChooser.getDate();
        String dob = null;
        if (dobDate != null) {
            dob = formatDate1.format(dobDate);
        }
        String contactNo = contactNoTxt.getText().trim();

        if (rollValidity(roll)) {
            try {
                ps = conn.prepareStatement("UPDATE stuinfo SET roll = ?, name = ?, fname = ?, mname = ?, dept = ?, batch = ?, contno = ?, bgrp = ?, rel = ?, dob = ?, peradd = ?, presentadd = ?, roomno = ? WHERE hallid = ?");
                ps.setString(1, roll);
                ps.setString(2, name);
                ps.setString(3, fname);
                ps.setString(4, mname);
                ps.setString(5, dept);
                ps.setInt(6, batch);
                ps.setString(7, contactNo);
                ps.setString(8, blood);
                ps.setString(9, religion);
                ps.setString(10, dob);
                ps.setString(11, permAdd);
                ps.setString(12, presAdd);
                ps.setString(13, roomNo);
                ps.setInt(14, hallId);

                ps.execute();

                ps.close();
                clearAll();
                JOptionPane.showMessageDialog(null, "Succesfully Updated", "Alert", JOptionPane.INFORMATION_MESSAGE);
                searchTxt.setText("Enter Hall Id");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Data insertion failed", "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nameTxt = new javax.swing.JTextField();
        rollTxt = new javax.swing.JTextField();
        fNameTxt = new javax.swing.JTextField();
        mNameTxt = new javax.swing.JTextField();
        deptComboBox = new javax.swing.JComboBox<>();
        batchTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        contactNoTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        bloodComboBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        religionComboBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        permAddTxt = new javax.swing.JTextField();
        presAddTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        dobDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        roomNoTxt = new javax.swing.JTextField();
        searchTxt = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        updateBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/storeupdate.png"))); // NOI18N
        jLabel1.setText("STUDENT INFO UPDATE ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(394, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(394, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel2.setText("Name");

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel3.setText("Father's Name");

        jLabel4.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel4.setText("Roll");

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel5.setText("Mother's Name");

        jLabel6.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel6.setText("Department");

        jLabel7.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel7.setText("Batch");

        nameTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        rollTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        fNameTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        mNameTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        deptComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        deptComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CE", "CSE", "EECE", "ME", "AE", "NAME ", "EWCE", "BME", "ARCHI", "IPE", "PME" }));

        batchTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel8.setText("Contact No");

        contactNoTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel9.setText("Blood Group");

        bloodComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        bloodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A+", "A-", "AB+", "B+", "B-", "AB-", "O+", "O-" }));

        jLabel10.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel10.setText("Religion");

        religionComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        religionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Islam", "Hindu", "Chirstian", "Buddhist", "Jews", "Others" }));

        jLabel11.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel11.setText("Present Address ");

        jLabel12.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel12.setText("Permanent Address ");

        permAddTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        presAddTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel13.setText("Date of Birth");

        dobDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel14.setText("Room No");

        roomNoTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        searchTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        searchTxt.setForeground(new java.awt.Color(153, 153, 153));
        searchTxt.setText("Enter Hall Id");
        searchTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchTxtFocusLost(evt);
            }
        });

        searchBtn.setBackground(new java.awt.Color(0, 153, 153));
        searchBtn.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(bloodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(religionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(deptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(batchTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                            .addComponent(nameTxt, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rollTxt, javax.swing.GroupLayout.Alignment.LEADING))
                        .addComponent(contactNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(roomNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(searchTxt)
                        .addGap(9, 9, 9)
                        .addComponent(searchBtn))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dobDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fNameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(mNameTxt)
                            .addComponent(permAddTxt)
                            .addComponent(presAddTxt))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(permAddTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(presAddTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dobDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rollTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                .addComponent(deptComboBox)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(batchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contactNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bloodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(religionComboBox))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(roomNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72))))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        updateBtn.setBackground(new java.awt.Color(0, 153, 153));
        updateBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        updateBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        updateBtn.setText(" UPDATE");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTxtFocusGained
        // TODO add your handling code here:
        if (searchTxt.getText().equals("Enter Hall Id")) {
            searchTxt.setText("");
        }
        searchTxt.setForeground(Color.black);
    }//GEN-LAST:event_searchTxtFocusGained

    private void searchTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTxtFocusLost
        // TODO add your handling code here:
        if (searchTxt.getText().equals("")) {
            searchTxt.setText("Enter Hall Id");
        }
        searchTxt.setForeground(new Color(153, 153, 153));
    }//GEN-LAST:event_searchTxtFocusLost

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        // TODO add your handling code here:
        if (!searchTxt.getText().equals("Enter Hall Id")) {
            hallId = Integer.parseInt(searchTxt.getText().toString().trim());
            clearAll();
            setStuInfo(hallId);
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        if (!searchTxt.getText().equals("Enter Hall Id")) {
            updateStdInfo();
        } else {
            JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateBtnActionPerformed

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
            java.util.logging.Logger.getLogger(stdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stdInfoUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField batchTxt;
    private javax.swing.JComboBox<String> bloodComboBox;
    private javax.swing.JTextField contactNoTxt;
    private javax.swing.JComboBox<String> deptComboBox;
    private com.toedter.calendar.JDateChooser dobDateChooser;
    private javax.swing.JTextField fNameTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTextField mNameTxt;
    private javax.swing.JTextField nameTxt;
    private javax.swing.JTextField permAddTxt;
    private javax.swing.JTextField presAddTxt;
    private javax.swing.JComboBox<String> religionComboBox;
    private javax.swing.JTextField rollTxt;
    private javax.swing.JTextField roomNoTxt;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
