/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms.hallAuthoDashboardInternalJframe;

import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import omms.BillPermission;
import omms.MessBillView;

/**
 *
 * @author Asus
 */
public class HallAuthoBillDash extends javax.swing.JInternalFrame {

    /**
     * Creates new form HallAuthoBillDash
     */
    public HallAuthoBillDash() {
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
        viewMonBillBtn = new javax.swing.JButton();
        payViewBtn = new javax.swing.JButton();
        billGenPerBtn = new javax.swing.JButton();
        billDateRanBtn = new javax.swing.JButton();
        viewDaiBillBtn = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        viewMonBillBtn.setBackground(new java.awt.Color(255, 255, 255));
        viewMonBillBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        viewMonBillBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        viewMonBillBtn.setText("  Student's Monthly Bill ");
        viewMonBillBtn.setBorder(null);
        viewMonBillBtn.setFocusPainted(false);
        viewMonBillBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        viewMonBillBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMonBillBtnActionPerformed(evt);
            }
        });

        payViewBtn.setBackground(new java.awt.Color(255, 255, 255));
        payViewBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        payViewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/file_preview_64px.png"))); // NOI18N
        payViewBtn.setText("  Payment View                  ");
        payViewBtn.setBorder(null);
        payViewBtn.setFocusPainted(false);
        payViewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payViewBtnActionPerformed(evt);
            }
        });

        billGenPerBtn.setBackground(new java.awt.Color(255, 255, 255));
        billGenPerBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        billGenPerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/receipt_64px.png"))); // NOI18N
        billGenPerBtn.setText("Bill Generate Permission");
        billGenPerBtn.setBorder(null);
        billGenPerBtn.setFocusPainted(false);
        billGenPerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billGenPerBtnActionPerformed(evt);
            }
        });

        billDateRanBtn.setBackground(new java.awt.Color(255, 255, 255));
        billDateRanBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        billDateRanBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/pay_date_64px.png"))); // NOI18N
        billDateRanBtn.setText("  Bill Date Range Set");
        billDateRanBtn.setBorder(null);
        billDateRanBtn.setFocusPainted(false);
        billDateRanBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        billDateRanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billDateRanBtnActionPerformed(evt);
            }
        });

        viewDaiBillBtn.setBackground(new java.awt.Color(255, 255, 255));
        viewDaiBillBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        viewDaiBillBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        viewDaiBillBtn.setText("  Student's Daily Bill ");
        viewDaiBillBtn.setBorder(null);
        viewDaiBillBtn.setFocusPainted(false);
        viewDaiBillBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        viewDaiBillBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDaiBillBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(billGenPerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(viewMonBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(billDateRanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewDaiBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(payViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(billGenPerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewMonBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(billDateRanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewDaiBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addComponent(payViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewMonBillBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMonBillBtnActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_viewMonBillBtnActionPerformed

    private void payViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payViewBtnActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_payViewBtnActionPerformed

    private void billGenPerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billGenPerBtnActionPerformed
        // TODO add your handling code here:
        BillPermission st = new BillPermission();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_billGenPerBtnActionPerformed

    private void billDateRanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billDateRanBtnActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_billDateRanBtnActionPerformed

    private void viewDaiBillBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDaiBillBtnActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_viewDaiBillBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton billDateRanBtn;
    private javax.swing.JButton billGenPerBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton payViewBtn;
    private javax.swing.JButton viewDaiBillBtn;
    private javax.swing.JButton viewMonBillBtn;
    // End of variables declaration//GEN-END:variables
}
