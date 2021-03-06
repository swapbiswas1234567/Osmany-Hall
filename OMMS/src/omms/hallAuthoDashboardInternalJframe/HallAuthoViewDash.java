/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms.hallAuthoDashboardInternalJframe;

import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import omms.StdInfoViewCur;
import omms.StdInfoViewPrev;

/**
 *
 * @author Asus
 */
public class HallAuthoViewDash extends javax.swing.JInternalFrame {

    /**
     * Creates new form HallAuthoViewDash
     */
    public HallAuthoViewDash() {
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
        curStdInfBtn = new javax.swing.JButton();
        prevStdinfBtn = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        curStdInfBtn.setBackground(new java.awt.Color(255, 255, 255));
        curStdInfBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        curStdInfBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        curStdInfBtn.setText("Current Students Info ");
        curStdInfBtn.setBorder(null);
        curStdInfBtn.setFocusPainted(false);
        curStdInfBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        curStdInfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curStdInfBtnActionPerformed(evt);
            }
        });

        prevStdinfBtn.setBackground(new java.awt.Color(255, 255, 255));
        prevStdinfBtn.setFont(new java.awt.Font("Bell MT", 1, 28)); // NOI18N
        prevStdinfBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        prevStdinfBtn.setText("Previous Students Info ");
        prevStdinfBtn.setBorder(null);
        prevStdinfBtn.setFocusPainted(false);
        prevStdinfBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        prevStdinfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevStdinfBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(255, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(prevStdinfBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(curStdInfBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(244, 244, 244))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(curStdInfBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(prevStdinfBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void curStdInfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curStdInfBtnActionPerformed
        // TODO add your handling code here:
        StdInfoViewCur st = new StdInfoViewCur();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_curStdInfBtnActionPerformed

    private void prevStdinfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevStdinfBtnActionPerformed
        // TODO add your handling code here:
        StdInfoViewPrev st = new StdInfoViewPrev();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_prevStdinfBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton curStdInfBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton prevStdinfBtn;
    // End of variables declaration//GEN-END:variables
}
