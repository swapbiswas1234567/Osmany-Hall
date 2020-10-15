/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Asus
 */
public class StdInfoUpdate extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    SimpleDateFormat formatDate = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate1 = new SimpleDateFormat("yyyyMMdd");

    int hallId = 0;
    String rollNo = "";
    String filename = "";
    byte[] person_image = null;

    /**
     * Creates new form stdInfoUpdate
     */
    public StdInfoUpdate() {
        initComponents();
        initialize();
        //closeBtn();
        setTitle("Student's Info Update");
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); 
        nameTxt.requestFocus();
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) dobDateChooser.getDateEditor();
        dtedit.setEditable(false);

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

    /*
        Seeting the cross button action to Dashboard
     */
    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    conn.close();
                    if (UserLog.name.equals("accountant")) {
                        DashboardAccountant das = new DashboardAccountant();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("provost")) {
                        DashboardHallAutho das = new DashboardHallAutho();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("mess")) {
                        DashboardMess das = new DashboardMess();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("captain")) {
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

    /*
        Setting the date choosers to todays current date
     */
    public void setDateChoosers() {
        Date todaysDate = new Date();
        dobDateChooser.setDate(todaysDate);
    }

    public void showImage(byte[] imagedata) {
        ImageIcon imgic = new ImageIcon(imagedata);
        Image scaleImage = imgic.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT);
        imgic = new ImageIcon(scaleImage);
        showImageLbl.setIcon(imgic);
    }

    public void clearAll() {
        nameTxt.setText("");
        rollTxt.setText("");
        deptComboBox.setSelectedIndex(0);
        batchTxt.setText("");
        contactNoTxt.setText("");
        emailTxt.setText("");
        roomNoTxt.setText("");
        bloodComboBox.setSelectedIndex(0);
        religionComboBox.setSelectedIndex(0);

        fNameTxt.setText("");
        mNameTxt.setText("");
        permAddTxt.setText("");
        presAddTxt.setText("");
        dobDateChooser.setDate(null);
        //setDateChoosers();
        showImageLbl.setText("Add Image");
        showImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/landscape.png")));
        hallId = 0;
        rollNo = "";
        filename = "";
        person_image = null;
    }

    public void selectImageFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            filename = f.getAbsolutePath();
            try {
                File image = new File(filename);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }

                person_image = bos.toByteArray();
                ImageIcon imgic = new ImageIcon(person_image);
                Image scaleImg = imgic.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT);
                imgic = new ImageIcon(scaleImg);
                showImageLbl.setIcon(imgic);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error Fetching Image", "Image", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    public String findHallId() {
        String id = searchTxt.getText();
        if (!id.equals("")) {

            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE hallid = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE roll = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            JOptionPane.showMessageDialog(null, "Inserted Hall Id/Roll can't be found in Current Student's Databases", "Data No Found", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return "";
    }

    public void setStuInfo(int hallId) {
        try {
            ps = conn.prepareStatement("SELECT roll, name, fname, mname, dept, batch, contno, email, bgrp, rel, dob, roomno, peradd, presentadd, image FROM stuinfo WHERE hallid = ?");
            ps.setInt(1, hallId);
            rs = ps.executeQuery();
            if (rs.next()) {
                rollNo = rs.getString(1);
                rollTxt.setText(rollNo);
                nameTxt.setText(rs.getString(2));
                fNameTxt.setText(rs.getString(3));
                mNameTxt.setText(rs.getString(4));
                deptComboBox.setSelectedItem(rs.getString(5));
                batchTxt.setText(String.valueOf(rs.getInt(6)));
                contactNoTxt.setText(rs.getString(7));
                emailTxt.setText(rs.getString(8));
                bloodComboBox.setSelectedItem(rs.getString(9));
                religionComboBox.setSelectedItem(rs.getString(10));
                int dobstr = rs.getInt(11);
                if (dobstr != 0) {
                    Date dob = formatDate1.parse(Integer.toString(dobstr));
                    dobDateChooser.setDate(dob);
                }
                permAddTxt.setText(rs.getString(14));
                presAddTxt.setText(rs.getString(13));
                roomNoTxt.setText(rs.getString(12));
                System.out.println("Image");
                person_image = rs.getBytes("image");
                if (person_image != null) {
                    showImage(person_image);
                }
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
        String blood = null;
        String religion = null;
        if (bloodComboBox.getSelectedItem() != null) {
            blood = bloodComboBox.getSelectedItem().toString().trim();
        }
        if (religionComboBox.getSelectedItem() != null) {
            religion = religionComboBox.getSelectedItem().toString().trim();
        }
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

        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Student's Name", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else if (roll.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Student's Roll", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else if (batch == 0) {
            JOptionPane.showMessageDialog(null, "Enter Student's Batch", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else {
            if (rollValidity(roll)) {
                try {
                    ps = conn.prepareStatement("UPDATE stuinfo SET roll = ?, name = ?, fname = ?, mname = ?, dept = ?, batch = ?, contno = ?, bgrp = ?, rel = ?, dob = ?, peradd = ?, presentadd = ?, roomno = ?, image = ? WHERE hallid = ?");
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
                    ps.setBytes(14, person_image);
                    ps.setInt(15, hallId);

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
        showImageLbl = new javax.swing.JLabel();
        attachFileBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        removeImgBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        emailTxt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/storeupdate.png"))); // NOI18N
        jLabel1.setText("STUDENT INFO UPDATE ");

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
        nameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTxtActionPerformed(evt);
            }
        });

        rollTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        rollTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rollTxtActionPerformed(evt);
            }
        });

        fNameTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        fNameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fNameTxtActionPerformed(evt);
            }
        });

        mNameTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        mNameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mNameTxtActionPerformed(evt);
            }
        });

        deptComboBox.setBackground(new java.awt.Color(204, 204, 204));
        deptComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        deptComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CE", "CSE", "EECE", "ME", "AE", "NAME ", "EWCE", "BME", "ARCHI", "IPE", "PME" }));
        deptComboBox.setBorder(null);
        deptComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptComboBoxActionPerformed(evt);
            }
        });

        batchTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        batchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchTxtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel8.setText("Contact No");

        contactNoTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        contactNoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNoTxtActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel9.setText("Blood Group");

        bloodComboBox.setBackground(new java.awt.Color(204, 204, 204));
        bloodComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        bloodComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A+", "A-", "AB+", "B+", "B-", "AB-", "O+", "O-" }));
        bloodComboBox.setBorder(null);
        bloodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bloodComboBoxActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel10.setText("Religion");

        religionComboBox.setBackground(new java.awt.Color(204, 204, 204));
        religionComboBox.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        religionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Islam", "Hindu", "Chirstian", "Buddhist", "Jews", "Others" }));
        religionComboBox.setBorder(null);
        religionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                religionComboBoxActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel11.setText("Present Address ");

        jLabel12.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel12.setText("Permanent Address ");

        permAddTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        permAddTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                permAddTxtActionPerformed(evt);
            }
        });

        presAddTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        presAddTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                presAddTxtActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel13.setText("Date of Birth");

        dobDateChooser.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel14.setText("Room No");

        roomNoTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        roomNoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNoTxtActionPerformed(evt);
            }
        });

        searchTxt.setBackground(new java.awt.Color(0, 204, 204));
        searchTxt.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        searchTxt.setForeground(new java.awt.Color(153, 153, 153));
        searchTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchTxt.setText("Enter Hall Id");
        searchTxt.setBorder(null);
        searchTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchTxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchTxtFocusLost(evt);
            }
        });
        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTxtKeyPressed(evt);
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

        showImageLbl.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        showImageLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/landscape.png"))); // NOI18N
        showImageLbl.setText("Add Image");
        showImageLbl.setAutoscrolls(true);

        attachFileBtn.setBackground(new java.awt.Color(0, 153, 153));
        attachFileBtn.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        attachFileBtn.setForeground(new java.awt.Color(255, 255, 255));
        attachFileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/paper-clip (1).png"))); // NOI18N
        attachFileBtn.setText("Attach File");
        attachFileBtn.setAutoscrolls(true);
        attachFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachFileBtnActionPerformed(evt);
            }
        });

        updateBtn.setBackground(new java.awt.Color(0, 153, 153));
        updateBtn.setFont(new java.awt.Font("Bell MT", 1, 20)); // NOI18N
        updateBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update1.png"))); // NOI18N
        updateBtn.setText(" UPDATE");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        removeImgBtn.setBackground(new java.awt.Color(255, 51, 51));
        removeImgBtn.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        removeImgBtn.setForeground(new java.awt.Color(255, 255, 255));
        removeImgBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/trash_01.png"))); // NOI18N
        removeImgBtn.setText("Remove Image");
        removeImgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeImgBtnActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel15.setText("Email");

        emailTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        emailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dobDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(fNameTxt)
                                .addComponent(mNameTxt)
                                .addComponent(permAddTxt)
                                .addComponent(presAddTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(bloodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(religionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(roomNoTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(updateBtn, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(deptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(batchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(nameTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rollTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(contactNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 100, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(showImageLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(attachFileBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(removeImgBtn))
                            .addGap(21, 21, 21)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(searchBtn)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addComponent(showImageLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(attachFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(removeImgBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rollTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(deptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(batchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bloodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(religionComboBox))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roomNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(permAddTxt)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(presAddTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dobDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1070, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
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
        if (!searchTxt.getText().equals("Enter Hall Id") && !searchTxt.getText().equals("")) {
            clearAll();
            hallId = Integer.parseInt(findHallId());
            setStuInfo(hallId);
        } else {
            clearAll();
            JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchBtnActionPerformed

    private void attachFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachFileBtnActionPerformed
        // TODO add your handling code here:
        if (!searchTxt.getText().equals("Enter Hall Id") || hallId != 0) {
            selectImageFile();
        } else {
            JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_attachFileBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (!searchTxt.getText().equals("Enter Hall Id") || hallId != 0) {
            int responce = JOptionPane.showConfirmDialog(this, "Do you want to save the current infomations of this student?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (responce == JOptionPane.YES_OPTION) {
                updateStdInfo();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void removeImgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeImgBtnActionPerformed
        // TODO add your handling code here:
        if (!searchTxt.getText().equals("Enter Hall Id") && !searchTxt.getText().equals("") || hallId != 0) {
            if (person_image != null) {
                person_image = null;
                try {
                    showImageLbl.setText("Add Image");
                    showImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/landscape.png")));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Can't remove the image!!!", "Database Upadate Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No image selected!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removeImgBtnActionPerformed

    private void searchTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTxtKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!searchTxt.getText().equals("Enter Hall Id") && !searchTxt.getText().equals("")) {
                clearAll();
                hallId = Integer.parseInt(searchTxt.getText().toString().trim());
                setStuInfo(hallId);
            } else {
                clearAll();
                JOptionPane.showMessageDialog(null, "Enter a hall id!!!", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_searchTxtKeyPressed

    private void nameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTxtActionPerformed
        // TODO add your handling code here:
        rollTxt.requestFocusInWindow();
    }//GEN-LAST:event_nameTxtActionPerformed

    private void rollTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollTxtActionPerformed
        // TODO add your handling code here:
        deptComboBox.requestFocusInWindow();
    }//GEN-LAST:event_rollTxtActionPerformed

    private void batchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batchTxtActionPerformed
        // TODO add your handling code here:
        contactNoTxt.requestFocusInWindow();
    }//GEN-LAST:event_batchTxtActionPerformed

    private void deptComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptComboBoxActionPerformed
        // TODO add your handling code here:
        batchTxt.requestFocusInWindow();
    }//GEN-LAST:event_deptComboBoxActionPerformed

    private void contactNoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNoTxtActionPerformed
        // TODO add your handling code here:
        emailTxt.requestFocusInWindow();
    }//GEN-LAST:event_contactNoTxtActionPerformed

    private void emailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTxtActionPerformed
        // TODO add your handling code here:
        bloodComboBox.requestFocusInWindow();
    }//GEN-LAST:event_emailTxtActionPerformed

    private void bloodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bloodComboBoxActionPerformed
        // TODO add your handling code here:
        religionComboBox.requestFocusInWindow();
    }//GEN-LAST:event_bloodComboBoxActionPerformed

    private void religionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_religionComboBoxActionPerformed
        // TODO add your handling code here:
        roomNoTxt.requestFocusInWindow();
    }//GEN-LAST:event_religionComboBoxActionPerformed

    private void roomNoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomNoTxtActionPerformed
        // TODO add your handling code here:
        fNameTxt.requestFocusInWindow();
    }//GEN-LAST:event_roomNoTxtActionPerformed

    private void fNameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fNameTxtActionPerformed
        // TODO add your handling code here:
        mNameTxt.requestFocusInWindow();
    }//GEN-LAST:event_fNameTxtActionPerformed

    private void mNameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mNameTxtActionPerformed
        // TODO add your handling code here:
        permAddTxt.requestFocusInWindow();
    }//GEN-LAST:event_mNameTxtActionPerformed

    private void permAddTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_permAddTxtActionPerformed
        // TODO add your handling code here:
        presAddTxt.requestFocusInWindow();
    }//GEN-LAST:event_permAddTxtActionPerformed

    private void presAddTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_presAddTxtActionPerformed
        // TODO add your handling code here:
        dobDateChooser.requestFocusInWindow();
    }//GEN-LAST:event_presAddTxtActionPerformed

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
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdInfoUpdate.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdInfoUpdate().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attachFileBtn;
    private javax.swing.JTextField batchTxt;
    private javax.swing.JComboBox<String> bloodComboBox;
    private javax.swing.JTextField contactNoTxt;
    private javax.swing.JComboBox<String> deptComboBox;
    private com.toedter.calendar.JDateChooser dobDateChooser;
    private javax.swing.JTextField emailTxt;
    private javax.swing.JTextField fNameTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JTextField mNameTxt;
    private javax.swing.JTextField nameTxt;
    private javax.swing.JTextField permAddTxt;
    private javax.swing.JTextField presAddTxt;
    private javax.swing.JComboBox<String> religionComboBox;
    private javax.swing.JButton removeImgBtn;
    private javax.swing.JTextField rollTxt;
    private javax.swing.JTextField roomNoTxt;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JLabel showImageLbl;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
