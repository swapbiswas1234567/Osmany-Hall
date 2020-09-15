/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms.accountantDashboardInternalJframe;

import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import omms.GenerateBill;
import omms.MessBillView;
import omms.PresentDue;
import omms.StdIndBillStat;

/**
 *
 * @author Asus
 */
public class AccBillDash extends javax.swing.JInternalFrame {

    /**
     * Creates new form BillViewDash
     */
    public AccBillDash() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        BasicInternalFrameUI bi = (BasicInternalFrameUI)this.getUI();
        bi.setNorthPane(null);
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
        genBillBtn = new javax.swing.JButton();
        viewMonBill = new javax.swing.JButton();
        presDueBill = new javax.swing.JButton();
        viewDailyBill = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        genBillBtn.setBackground(new java.awt.Color(255, 255, 255));
        genBillBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        genBillBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/receipt_64px.png"))); // NOI18N
        genBillBtn.setText(" Generate & View Bill     ");
        genBillBtn.setBorder(null);
        genBillBtn.setFocusPainted(false);
        genBillBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genBillBtnActionPerformed(evt);
            }
        });

        viewMonBill.setBackground(new java.awt.Color(255, 255, 255));
        viewMonBill.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        viewMonBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        viewMonBill.setText("  Student's Monthly Bill ");
        viewMonBill.setBorder(null);
        viewMonBill.setFocusPainted(false);
        viewMonBill.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        viewMonBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMonBillActionPerformed(evt);
            }
        });

        presDueBill.setBackground(new java.awt.Color(255, 255, 255));
        presDueBill.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        presDueBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/invoice.png"))); // NOI18N
        presDueBill.setText("  Student's Current Due");
        presDueBill.setBorder(null);
        presDueBill.setFocusPainted(false);
        presDueBill.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        presDueBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                presDueBillActionPerformed(evt);
            }
        });

        viewDailyBill.setBackground(new java.awt.Color(255, 255, 255));
        viewDailyBill.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        viewDailyBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/bill_acc.png"))); // NOI18N
        viewDailyBill.setText("  Student's Daily Bill ");
        viewDailyBill.setBorder(null);
        viewDailyBill.setFocusPainted(false);
        viewDailyBill.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        viewDailyBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDailyBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(253, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(presDueBill, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewMonBill, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewDailyBill, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(231, 231, 231))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(genBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(viewMonBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(viewDailyBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(presDueBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void genBillBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genBillBtnActionPerformed
        // TODO add your handling code here:
        GenerateBill st = new GenerateBill();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_genBillBtnActionPerformed

    private void viewMonBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMonBillActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_viewMonBillActionPerformed

    private void presDueBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_presDueBillActionPerformed
        // TODO add your handling code here:
        PresentDue st = new PresentDue();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_presDueBillActionPerformed

    private void viewDailyBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDailyBillActionPerformed
        // TODO add your handling code here:
        StdIndBillStat st = new StdIndBillStat();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_viewDailyBillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton genBillBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton presDueBill;
    private javax.swing.JButton viewDailyBill;
    private javax.swing.JButton viewMonBill;
    // End of variables declaration//GEN-END:variables
}
