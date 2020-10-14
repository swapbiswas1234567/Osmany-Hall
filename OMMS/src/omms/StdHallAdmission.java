/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Image;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Asus
 */
public class StdHallAdmission extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    String filename = "";
    byte[] person_image = null;

    /**
     * Creates new form stdHallAdmission
     */
    public StdHallAdmission() {
        initComponents();
        initialize();
        setTitle("Hall Admission");
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers(); // setting todays date to the date chooser
        stdNameTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
        JTextFieldDateEditor dtedit;
        dtedit = (JTextFieldDateEditor) entryDateChooser.getDateEditor();
        dtedit.setEditable(false);
        setCombo();
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
        entryDateChooser.setDate(todaysDate);
    }

    public void setCombo() {
        try {
            ps = conn.prepareStatement("SELECT dept from dept");
            rs = ps.executeQuery();
            while (rs.next()) {
                String dept = rs.getString(1);
                if (!dept.equals("")) {
                    deptComboBox.addItem(dept);
                }
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
        }
        deptComboBox.setSelectedIndex(0);
        stdNameTxt.requestFocusInWindow();
    }

    public void clearAll() {
        stdNameTxt.setText("");
        stdRollTxt.setText("");
        batchTxt.setText("");
        roomNoTxt.setText("");
        contactNoTxt.setText("");
        setDateChoosers();
        deptComboBox.setSelectedIndex(0);
        emailTxt.setText("");
        showImageLbl.setText("Add Image");
        showImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/landscape.png")));

    }

    public boolean rollValidity(String stdRoll) {
        try {
            ps = conn.prepareStatement("SELECT * FROM stuinfo WHERE roll = ?");
            ps.setString(1, stdRoll);
            rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                rs.close();
                JOptionPane.showMessageDialog(null, "This roll is used before", "Invalid Roll Inserted", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                ps.close();
                rs.close();
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error", "Database Problem", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public int getHallid(String roll) {
        int maxHallId = 0;
        try {
            ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE roll = ?");
            ps.setString(1, roll);
            rs = ps.executeQuery();
            rs.next();
            maxHallId = rs.getInt(1);
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data fgetching failed", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return maxHallId;
    }

    public void admitStd() {
        String stdName = stdNameTxt.getText().toString().trim();
        String stdRoll = stdRollTxt.getText().toString().trim();
        String stdDept = deptComboBox.getSelectedItem().toString().trim();
        String stdBatch = batchTxt.getText().toString().trim();
        String stdRoomNo = roomNoTxt.getText().toString().trim();
        int entryDate = Integer.parseInt(formatDate.format(entryDateChooser.getDate()));
        String stdContactNo = contactNoTxt.getText().toString().trim();
        String email = emailTxt.getText().toString().trim();

        if (stdName.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Student's Name", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else if (stdRoll.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Student's Roll", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else if (stdBatch.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter Student's Batch", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
        } else {
            if (rollValidity(stdRoll)) {
                try {
                    ps = conn.prepareStatement("INSERT INTO stuinfo (roll, name, dept, batch, entrydate, contno, roomno, image, email, fname, mname, bgrp, sex, rel, dob, peradd, presentadd, hall, totaldue, securitymoney, messad, idcard, depositdate, withdrawdate, moneystatus) VALUES (?,?,?,?,?,?,?,?,?, '', '', '', '', '', 0, '', '', '', 0, 0, 0, 0, 0, 0, 0 )");
                    ps.setString(1, stdRoll);
                    ps.setString(2, stdName);
                    ps.setString(3, stdDept);
                    ps.setInt(4, Integer.parseInt(stdBatch));
                    ps.setInt(5, entryDate);
                    ps.setString(6, stdContactNo);
                    ps.setString(7, stdRoomNo);
                    ps.setBytes(8, person_image);
                    ps.setString(9, email);
                    ps.execute();

                    ps.close();
                    clearAll();

                    int hallId = getHallid(stdRoll);

                    String msg = "Hall Id of " + stdName + " is " + hallId;

                    JOptionPane.showMessageDialog(null, msg);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Data insertion failed in Stuinfo", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        stdNameTxt = new javax.swing.JTextField();
        stdRollTxt = new javax.swing.JTextField();
        roomNoTxt = new javax.swing.JTextField();
        contactNoTxt = new javax.swing.JTextField();
        entryDateChooser = new com.toedter.calendar.JDateChooser();
        clearBtn = new javax.swing.JButton();
        submitBtn = new javax.swing.JButton();
        deptComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        batchTxt = new javax.swing.JTextField();
        showImageLbl = new javax.swing.JLabel();
        attachFileBtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        emailTxt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel2.setFont(new java.awt.Font("Bell MT", 1, 34)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/admission.png"))); // NOI18N
        jLabel2.setText("STUDENT HALL ADMISSION");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(373, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(372, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel1.setText("Student Roll");

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel3.setText("Department");

        jLabel4.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel4.setText("Student Name ");

        jLabel5.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel5.setText("Entry Date");

        jLabel6.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel6.setText("Room No");

        jLabel7.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel7.setText("Contact No");

        stdNameTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stdNameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stdNameTxtActionPerformed(evt);
            }
        });

        stdRollTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        stdRollTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stdRollTxtActionPerformed(evt);
            }
        });

        roomNoTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        roomNoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomNoTxtActionPerformed(evt);
            }
        });

        contactNoTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        contactNoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactNoTxtActionPerformed(evt);
            }
        });

        entryDateChooser.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N

        clearBtn.setBackground(new java.awt.Color(0, 153, 153));
        clearBtn.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        clearBtn.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/reset (1).png"))); // NOI18N
        clearBtn.setText("Clear");
        clearBtn.setFocusPainted(false);
        clearBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        submitBtn.setBackground(new java.awt.Color(0, 153, 153));
        submitBtn.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        submitBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/enter (2).png"))); // NOI18N
        submitBtn.setText("Submit");
        submitBtn.setFocusPainted(false);
        submitBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        submitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitBtnActionPerformed(evt);
            }
        });

        deptComboBox.setBackground(new java.awt.Color(204, 204, 204));
        deptComboBox.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        deptComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptComboBoxActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel8.setText("Batch");

        batchTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        batchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchTxtActionPerformed(evt);
            }
        });

        showImageLbl.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        showImageLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        showImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/landscape.png"))); // NOI18N
        showImageLbl.setText("Add Image");

        attachFileBtn.setBackground(new java.awt.Color(0, 153, 153));
        attachFileBtn.setFont(new java.awt.Font("Bell MT", 1, 24)); // NOI18N
        attachFileBtn.setForeground(new java.awt.Color(255, 255, 255));
        attachFileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/paper-clip (1).png"))); // NOI18N
        attachFileBtn.setText("Attach File");
        attachFileBtn.setFocusPainted(false);
        attachFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachFileBtnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 22)); // NOI18N
        jLabel9.setText("Email");

        emailTxt.setFont(new java.awt.Font("Bell MT", 0, 22)); // NOI18N
        emailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(176, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(submitBtn))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailTxt)
                            .addComponent(entryDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stdRollTxt)
                            .addComponent(roomNoTxt)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(deptComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(25, 25, 25)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(batchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(contactNoTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(stdNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showImageLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(attachFileBtn)
                        .addGap(35, 35, 35)))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stdNameTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(stdRollTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(deptComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(batchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(roomNoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(entryDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contactNoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(showImageLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(attachFileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
        clearAll();
        stdNameTxt.requestFocus();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void submitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitBtnActionPerformed
        // TODO add your handling code here:
        int responce = JOptionPane.showConfirmDialog(this, "Do you want to admit this student?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (responce == JOptionPane.YES_OPTION) {
            admitStd();
        }

    }//GEN-LAST:event_submitBtnActionPerformed

    private void attachFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachFileBtnActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_attachFileBtnActionPerformed

    private void stdNameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stdNameTxtActionPerformed
        // TODO add your handling code here:
        stdRollTxt.requestFocusInWindow();
    }//GEN-LAST:event_stdNameTxtActionPerformed

    private void stdRollTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stdRollTxtActionPerformed
        // TODO add your handling code here:
        deptComboBox.requestFocusInWindow();
    }//GEN-LAST:event_stdRollTxtActionPerformed

    private void deptComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptComboBoxActionPerformed
        // TODO add your handling code here:
        batchTxt.requestFocusInWindow();
    }//GEN-LAST:event_deptComboBoxActionPerformed

    private void batchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batchTxtActionPerformed
        // TODO add your handling code here:
        roomNoTxt.requestFocusInWindow();
    }//GEN-LAST:event_batchTxtActionPerformed

    private void roomNoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomNoTxtActionPerformed
        // TODO add your handling code here:
        contactNoTxt.requestFocusInWindow();
    }//GEN-LAST:event_roomNoTxtActionPerformed

    private void contactNoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactNoTxtActionPerformed
        // TODO add your handling code here:
        emailTxt.requestFocusInWindow();
    }//GEN-LAST:event_contactNoTxtActionPerformed

    private void emailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTxtActionPerformed
        // TODO add your handling code here:
        submitBtn.doClick();
    }//GEN-LAST:event_emailTxtActionPerformed

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
            java.util.logging.Logger.getLogger(StdHallAdmission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StdHallAdmission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StdHallAdmission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StdHallAdmission.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StdHallAdmission().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attachFileBtn;
    private javax.swing.JTextField batchTxt;
    private javax.swing.JButton clearBtn;
    private javax.swing.JTextField contactNoTxt;
    private javax.swing.JComboBox<String> deptComboBox;
    private javax.swing.JTextField emailTxt;
    private com.toedter.calendar.JDateChooser entryDateChooser;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField roomNoTxt;
    private javax.swing.JLabel showImageLbl;
    private javax.swing.JTextField stdNameTxt;
    private javax.swing.JTextField stdRollTxt;
    private javax.swing.JButton submitBtn;
    // End of variables declaration//GEN-END:variables

}
