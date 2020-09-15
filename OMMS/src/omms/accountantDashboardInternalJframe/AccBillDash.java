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
        viewBill = new javax.swing.JButton();

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

        viewBill.setBackground(new java.awt.Color(255, 255, 255));
        viewBill.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        viewBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        viewBill.setText("  Student's Monthly Bill ");
        viewBill.setBorder(null);
        viewBill.setFocusPainted(false);
        viewBill.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        viewBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(257, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(viewBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(genBillBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addGap(227, 227, 227))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(genBillBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(viewBill, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(163, Short.MAX_VALUE))
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

    private void viewBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBillActionPerformed
        // TODO add your handling code here:
        MessBillView st = new MessBillView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_viewBillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton genBillBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton viewBill;
    // End of variables declaration//GEN-END:variables
}
