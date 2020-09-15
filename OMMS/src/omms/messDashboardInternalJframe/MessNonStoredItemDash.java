/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms.messDashboardInternalJframe;

import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import omms.NSItemView;
import omms.NonStoredItem;
import omms.NonStoredItemUpdate;

/**
 *
 * @author Asus
 */
public class MessNonStoredItemDash extends javax.swing.JInternalFrame {

    /**
     * Creates new form NonStoredItemDash
     */
    public MessNonStoredItemDash() {
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
        nonStrInBtn = new javax.swing.JButton();
        nonStrUpdtBtn = new javax.swing.JButton();
        nonStrViewBtn = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        nonStrInBtn.setBackground(new java.awt.Color(255, 255, 255));
        nonStrInBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        nonStrInBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/put-in-cart.png"))); // NOI18N
        nonStrInBtn.setText("  Non Store Item In");
        nonStrInBtn.setBorder(null);
        nonStrInBtn.setFocusPainted(false);
        nonStrInBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        nonStrInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonStrInBtnActionPerformed(evt);
            }
        });

        nonStrUpdtBtn.setBackground(new java.awt.Color(255, 255, 255));
        nonStrUpdtBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        nonStrUpdtBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update_64px.png"))); // NOI18N
        nonStrUpdtBtn.setText("  Non Store Item Update");
        nonStrUpdtBtn.setBorder(null);
        nonStrUpdtBtn.setFocusPainted(false);
        nonStrUpdtBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        nonStrUpdtBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonStrUpdtBtnActionPerformed(evt);
            }
        });

        nonStrViewBtn.setBackground(new java.awt.Color(255, 255, 255));
        nonStrViewBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        nonStrViewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        nonStrViewBtn.setText("  Non Store Item View");
        nonStrViewBtn.setBorder(null);
        nonStrViewBtn.setFocusPainted(false);
        nonStrViewBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        nonStrViewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nonStrViewBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nonStrViewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nonStrUpdtBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nonStrInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(nonStrInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(nonStrUpdtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(nonStrViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nonStrInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonStrInBtnActionPerformed
        // TODO add your handling code here:
        NonStoredItem st = new NonStoredItem();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_nonStrInBtnActionPerformed

    private void nonStrUpdtBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonStrUpdtBtnActionPerformed
        // TODO add your handling code here:
        NonStoredItemUpdate st = new NonStoredItemUpdate();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_nonStrUpdtBtnActionPerformed

    private void nonStrViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonStrViewBtnActionPerformed
        // TODO add your handling code here:
        NSItemView st = new NSItemView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_nonStrViewBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nonStrInBtn;
    private javax.swing.JButton nonStrUpdtBtn;
    private javax.swing.JButton nonStrViewBtn;
    // End of variables declaration//GEN-END:variables
}
