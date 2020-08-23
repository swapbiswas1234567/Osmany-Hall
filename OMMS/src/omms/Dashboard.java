/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Ajmir
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        
        String image = "dashboard.jpg";
        imageSet(imagelbl, image);
    }
    
    
    void imageSet(JLabel x, String imgname){
	ImageIcon myimage =  new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagepackage/"+imgname)));
	Image img1 = myimage.getImage();
	Image img2 = img1.getScaledInstance(x.getWidth(), x.getHeight(), Image.SCALE_SMOOTH);
	ImageIcon i = new ImageIcon(img2);
	x.setIcon(i);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagelbl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        storeinbtn = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        storeoutbtn = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        storeupdatebtn = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        ledgerbtn = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        nonstorebtn = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        nonstoreupdatebtn = new javax.swing.JMenuItem();
        tmpfoodbtn = new javax.swing.JMenu();
        tempfoodinbtn = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        tmpfoodupdatebtn = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        tempfoodview = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        createmealsheet = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenuBar1.setBackground(new java.awt.Color(204, 204, 255));
        jMenuBar1.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        jMenuBar1.setPreferredSize(new java.awt.Dimension(56, 40));

        jMenu1.setText("Stored Item");
        jMenu1.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        storeinbtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        storeinbtn.setText("Store In Item");
        storeinbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeinbtnActionPerformed(evt);
            }
        });
        jMenu1.add(storeinbtn);
        jMenu1.add(jSeparator1);

        storeoutbtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        storeoutbtn.setText("Store Out Item");
        storeoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeoutbtnActionPerformed(evt);
            }
        });
        jMenu1.add(storeoutbtn);
        jMenu1.add(jSeparator2);

        storeupdatebtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        storeupdatebtn.setText("Stored Item View & Update");
        storeupdatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeupdatebtnActionPerformed(evt);
            }
        });
        jMenu1.add(storeupdatebtn);
        jMenu1.add(jSeparator4);

        ledgerbtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        ledgerbtn.setText("Show Ledger");
        ledgerbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledgerbtnActionPerformed(evt);
            }
        });
        jMenu1.add(ledgerbtn);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Non Stored Item");
        jMenu2.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        nonstorebtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        nonstorebtn.setText("Non Stored Item In");
        nonstorebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonstorebtnActionPerformed(evt);
            }
        });
        jMenu2.add(nonstorebtn);
        jMenu2.add(jSeparator3);

        nonstoreupdatebtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        nonstoreupdatebtn.setText("Non Stored Item View & Update");
        nonstoreupdatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonstoreupdatebtnActionPerformed(evt);
            }
        });
        jMenu2.add(nonstoreupdatebtn);

        jMenuBar1.add(jMenu2);

        tmpfoodbtn.setText("Temporary Food");
        tmpfoodbtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        tempfoodinbtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        tempfoodinbtn.setText("Temporary Food In");
        tempfoodinbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempfoodinbtnActionPerformed(evt);
            }
        });
        tmpfoodbtn.add(tempfoodinbtn);
        tmpfoodbtn.add(jSeparator5);

        tmpfoodupdatebtn.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        tmpfoodupdatebtn.setText("Temporary Food View & Update");
        tmpfoodupdatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmpfoodupdatebtnActionPerformed(evt);
            }
        });
        tmpfoodbtn.add(tmpfoodupdatebtn);
        tmpfoodbtn.add(jSeparator6);

        tempfoodview.setText("Temporary Food View");
        tempfoodview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempfoodviewActionPerformed(evt);
            }
        });
        tmpfoodbtn.add(tempfoodview);

        jMenuBar1.add(tmpfoodbtn);

        jMenu3.setText("Daily Meal Sheet");
        jMenu3.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N

        createmealsheet.setFont(new java.awt.Font("Bell MT", 0, 14)); // NOI18N
        createmealsheet.setText("Create or Update Meal Sheet");
        createmealsheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createmealsheetActionPerformed(evt);
            }
        });
        jMenu3.add(createmealsheet);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagelbl, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagelbl, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void storeinbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeinbtnActionPerformed
        // TODO add your handling code here:
        StoreInForm st= new StoreInForm();
        st.setVisible(true);
        dispose();
    }//GEN-LAST:event_storeinbtnActionPerformed

    private void storeoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeoutbtnActionPerformed
        // TODO add your handling code here:
        
        StoreOutItem st= new StoreOutItem();
        st.setVisible(true);
        dispose();
    }//GEN-LAST:event_storeoutbtnActionPerformed

    private void storeupdatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeupdatebtnActionPerformed
        // TODO add your handling code here:
        
        StoredItemUpdate st= new StoredItemUpdate();
        st.setVisible(true);
        dispose();
    }//GEN-LAST:event_storeupdatebtnActionPerformed

    private void ledgerbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledgerbtnActionPerformed
        // TODO add your handling code here:
        
        ShowLedger sl= new ShowLedger();
        sl.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_ledgerbtnActionPerformed

    private void nonstorebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonstorebtnActionPerformed
        // TODO add your handling code here:
        NonStoredItem ns= new NonStoredItem();
        ns.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_nonstorebtnActionPerformed

    private void nonstoreupdatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonstoreupdatebtnActionPerformed
        // TODO add your handling code here:
        
        NonStoredItemUpdate nsu= new NonStoredItemUpdate();
        nsu.setVisible(true);
        dispose();
    }//GEN-LAST:event_nonstoreupdatebtnActionPerformed

    private void tempfoodinbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempfoodinbtnActionPerformed
        // TODO add your handling code here:
        
        TemporaryFoodIn tf = new TemporaryFoodIn();
        tf.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_tempfoodinbtnActionPerformed

    private void tmpfoodupdatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmpfoodupdatebtnActionPerformed
        // TODO add your handling code here:
        
        TmpFoodUpdate tfu = new TmpFoodUpdate();
        tfu.setVisible(true);
        dispose();
        
    }//GEN-LAST:event_tmpfoodupdatebtnActionPerformed

    private void tempfoodviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempfoodviewActionPerformed
        // TODO add your handling code here:
        TempFoodView tfu = new TempFoodView();
        tfu.setVisible(true);
        dispose();
    }//GEN-LAST:event_tempfoodviewActionPerformed

    private void createmealsheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createmealsheetActionPerformed
        // TODO add your handling code here:
        MealSheet ms= new MealSheet();
        ms.setVisible(true);
        dispose();
    }//GEN-LAST:event_createmealsheetActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem createmealsheet;
    private javax.swing.JLabel imagelbl;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JMenuItem ledgerbtn;
    private javax.swing.JMenuItem nonstorebtn;
    private javax.swing.JMenuItem nonstoreupdatebtn;
    private javax.swing.JMenuItem storeinbtn;
    private javax.swing.JMenuItem storeoutbtn;
    private javax.swing.JMenuItem storeupdatebtn;
    private javax.swing.JMenuItem tempfoodinbtn;
    private javax.swing.JMenuItem tempfoodview;
    private javax.swing.JMenu tmpfoodbtn;
    private javax.swing.JMenuItem tmpfoodupdatebtn;
    // End of variables declaration//GEN-END:variables
}
