/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ajmir
 */
public class SendMail extends javax.swing.JFrame {

    /**
     * Creates new form SendMail
     */
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int combo = 0;
    public static int flag;
    public static ArrayList<String> hallid;

    public SendMail() {
        initComponents();
        initialize();
        flag = 0;
        combo = 1;

    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yeartxt.setText(Integer.toString(year));

        int month = Calendar.getInstance().get(Calendar.MONTH);
        //System.out.println(month+" "+year);
        monthcombo.setSelectedIndex(month);
        hallid = new ArrayList<>();

        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        
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

    
    public void sendmail(boolean single, int hallid, int month, String monthname, int year) {
        String body = "", greetings = "", billinfo = "", tail = "", msg = "", strhallid = "", strprevd="";
        boolean isval = false;
        Double bill = 0.00, others = 0.00, fine = 0.00, waive = 0.00, due = 0.00, total = 0.0;
        String subject = "Mess Bill of OSMANY HALL(" + monthname + ", " + year + ")";

        try {
            if (single && hallid > 0) {
                psmt = conn.prepareStatement("select st.name,st.email,bh.bill, bh.others, bh.fine, bh.waive, bh.due,st.hallid from stuinfo st join billhistory bh on st.hallid = bh.hallid and st.hallid =? and bh.month=? and bh.year=?");
                psmt.setInt(1, hallid);
                psmt.setInt(2, month);
                psmt.setInt(3, year);
                rs = psmt.executeQuery();
            } else if (!single) {
                psmt = conn.prepareStatement("select st.name,st.email,bh.bill, bh.others, bh.fine, bh.waive, bh.due,st.hallid from stuinfo st join billhistory bh on st.hallid = bh.hallid and bh.month=? and bh.year=?");
                psmt.setInt(1, month);
                psmt.setInt(2, year);
                rs = psmt.executeQuery();
            }
            greetings = "Assalamualaikum";
            body = "Your mess bill of " + monthname + "," + Integer.toString(year) + " has been published. You are requested to pay the mess bill in due time. Total bill description is given below\n";
            tail = "Best regards,\nOsmany Hall Authority\nMIST, Mirpur Cantonment";
            while (rs.next()) {
                greetings = greetings + " " + rs.getString(1) + ",\n";
                bill = rs.getDouble(3);
                others = rs.getDouble(4);
                fine = rs.getDouble(5);
                waive = rs.getDouble(6);
                due = rs.getDouble(7);
                strhallid = rs.getString(8);
                total = bill + others + fine - waive + due;
                if(due < 0){
                    strprevd = Double.toString(due*-1) +" (Advance)";
                }else{
                    strprevd = Double.toString(due);
                }
                
                body = body + "\n Mess Bill: " + Double.toString(bill) + " \n Others: " + Double.toString(others) + "\n Fine: " + Double.toString(fine) + "\n Waive: "
                        + Double.toString(waive) + "\n Previous Due: " + strprevd + "\n Total: " + total + "\n\n For further details contact with the hall office\n\n";
                msg = greetings + body + tail;
                Email.send("mist.osmanyhall@gmail.com", "osm@nycse17", rs.getString(2), subject, msg, strhallid);
                isval = true;
            }
            psmt.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetch for Mail Send Failed", "Sending Mail error", JOptionPane.ERROR_MESSAGE);
        }
        //System.out.println("called "+flag);
        if (flag == 1 && !single) {
            genpdf();
            JOptionPane.showMessageDialog(null, "Failed to send mail check the pdf", "Sending Mail error", JOptionPane.ERROR_MESSAGE);
        } else if (single && flag == 1) {
            JOptionPane.showMessageDialog(null, "Failed to send mail to hallid: " + strhallid, "Sending Mail error", JOptionPane.ERROR_MESSAGE);
        }
        if (!isval) {
            JOptionPane.showMessageDialog(null, "No Bill has generated in " + monthname + "," + year, "Mail send failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void genpdf() {
        String date = null;
        Document doc = new Document();
        try {

            PdfWriter.getInstance(doc, new FileOutputStream("C:\\Users\\Ajmir\\Desktop\\Mailerror.pdf"));

            doc.open();

            Image image1 = Image.getInstance(getClass().getResource("/imagepackage/MIST_Logo.png"));
            image1.setAlignment(Element.ALIGN_CENTER);
            image1.scaleAbsolute(100, 70);
            //Add to document
            doc.add(image1);

            Paragraph osmany = new Paragraph("OSMANY HALL", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, com.itextpdf.text.Font.NORMAL));
            osmany.setAlignment(Element.ALIGN_CENTER);
            doc.add(osmany);

            Paragraph p = new Paragraph("List of Students Did not Received Mail\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, com.itextpdf.text.Font.NORMAL));

            p.setAlignment(Element.ALIGN_CENTER);

            doc.add(p);

            Date todaysdate = new Date();
            date = formatter.format(todaysdate);

            Paragraph q = new Paragraph("Date: " + date + "\n\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));
            q.setAlignment(Element.ALIGN_CENTER);
            doc.add(q);

            Paragraph name = new Paragraph("Serial     Hallid");
            name.setAlignment(Element.ALIGN_LEFT);
            doc.add(name);

            int serial = 1;
            for (String phone1 : hallid) {
                Paragraph numbers = new Paragraph(serial + ".           " + phone1);
                numbers.setAlignment(Element.ALIGN_LEFT);
                doc.add(numbers);
                serial++;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Pdf generation error", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        doc.close();
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
        idtxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        monthcombo = new javax.swing.JComboBox<>();
        allcheck = new javax.swing.JCheckBox();
        sendbtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        yeartxt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(117, 175, 180));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/email.png"))); // NOI18N
        jLabel1.setText("SEND MAIL");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/contact.png"))); // NOI18N
        jLabel2.setText("Id");

        idtxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/month.png"))); // NOI18N
        jLabel3.setText("Month");

        monthcombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        monthcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        allcheck.setBackground(new java.awt.Color(117, 175, 180));
        allcheck.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        allcheck.setText("All");

        sendbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        sendbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/send-message.png"))); // NOI18N
        sendbtn.setText("Send");
        sendbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendbtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/year.png"))); // NOI18N
        jLabel4.setText("Year");

        yeartxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(262, 262, 262)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(300, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idtxt)
                    .addComponent(monthcombo, 0, 148, Short.MAX_VALUE)
                    .addComponent(yeartxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(allcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(monthcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(sendbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sendbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendbtnActionPerformed
        // TODO add your handling code here:
        StdIndBillStat sb = new StdIndBillStat();
        String strid = "", monthname = "", stryear = "";
        int id = 0, hallid = 0, month = -1, year = 0;
        boolean check = false;

        strid = idtxt.getText().trim();
        stryear = yeartxt.getText().trim();

        try {
            id = Integer.parseInt(strid);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Id Format Error", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            year = Integer.parseInt(stryear);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Year Format Error", "Data error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (id > 0 && combo == 1 && year > 0) {
            monthname = monthcombo.getSelectedItem().toString();
            month = monthcombo.getSelectedIndex();
            check = allcheck.isSelected();

            if (sb.checkhallid(strid)) {
                if (check) {
                    sendmail(false, id, month + 1, monthname, year);
                } else {
                    sendmail(true, id, month + 1, monthname, year);
                }

            } else {
                hallid = sb.checkroll(strid);
                if (hallid > 0) {
                    if (check) {
                        sendmail(false, id, month + 1, monthname, year);
                    } else {
                        sendmail(true, id, month + 1, monthname, year);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Id does not exist", "Data error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Id is not valid", "Data error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_sendbtnActionPerformed

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
            java.util.logging.Logger.getLogger(SendMail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendMail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendMail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendMail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SendMail().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox allcheck;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> monthcombo;
    private javax.swing.JButton sendbtn;
    private javax.swing.JTextField yeartxt;
    // End of variables declaration//GEN-END:variables
}
