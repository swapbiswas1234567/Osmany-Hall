/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms.hallAuthoDashboardInternalJframe;

import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import omms.NonStoredItemUpdate;
import omms.ShowLedger;
import omms.StoreInSum;
import omms.StoreOutSum;
import omms.StoredItemUpdate;
import omms.TempFoodView;
import omms.TmpFoodUpdate;

/**
 *
 * @author Asus
 */
public class HallAuthoFoodDash extends javax.swing.JInternalFrame {

    /**
     * Creates new form HallAuthoStoreDash
     */
    public HallAuthoFoodDash() {
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
        showLedgerBtn = new javax.swing.JButton();
        tempFoodViewBtn = new javax.swing.JButton();
        tempFoodUpdateBtn = new javax.swing.JButton();
        storeInUpdateBtn = new javax.swing.JButton();
        storeInSumBtn = new javax.swing.JButton();
        storeOutUpdateBtn = new javax.swing.JButton();
        storeOutSumBtn = new javax.swing.JButton();
        nonStrUpdtBtn = new javax.swing.JButton();
        nonStrViewBtn = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        showLedgerBtn.setBackground(new java.awt.Color(255, 255, 255));
        showLedgerBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        showLedgerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/todo_list_64px.png"))); // NOI18N
        showLedgerBtn.setText("  Show Ledger");
        showLedgerBtn.setBorder(null);
        showLedgerBtn.setFocusPainted(false);
        showLedgerBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        showLedgerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLedgerBtnActionPerformed(evt);
            }
        });

        tempFoodViewBtn.setBackground(new java.awt.Color(255, 255, 255));
        tempFoodViewBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        tempFoodViewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/mug.png"))); // NOI18N
        tempFoodViewBtn.setText("  Temporary Food View");
        tempFoodViewBtn.setBorder(null);
        tempFoodViewBtn.setFocusPainted(false);
        tempFoodViewBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        tempFoodViewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempFoodViewBtnActionPerformed(evt);
            }
        });

        tempFoodUpdateBtn.setBackground(new java.awt.Color(255, 255, 255));
        tempFoodUpdateBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        tempFoodUpdateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update_64px.png"))); // NOI18N
        tempFoodUpdateBtn.setText("Temporay Food Update");
        tempFoodUpdateBtn.setBorder(null);
        tempFoodUpdateBtn.setFocusPainted(false);
        tempFoodUpdateBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        tempFoodUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempFoodUpdateBtnActionPerformed(evt);
            }
        });

        storeInUpdateBtn.setBackground(new java.awt.Color(255, 255, 255));
        storeInUpdateBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        storeInUpdateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update_72px.png"))); // NOI18N
        storeInUpdateBtn.setText(" Store In Item Update");
        storeInUpdateBtn.setBorder(null);
        storeInUpdateBtn.setFocusPainted(false);
        storeInUpdateBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        storeInUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeInUpdateBtnActionPerformed(evt);
            }
        });

        storeInSumBtn.setBackground(new java.awt.Color(255, 255, 255));
        storeInSumBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        storeInSumBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/business.png"))); // NOI18N
        storeInSumBtn.setText("  Store In Summary");
        storeInSumBtn.setBorder(null);
        storeInSumBtn.setFocusPainted(false);
        storeInSumBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        storeInSumBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeInSumBtnActionPerformed(evt);
            }
        });

        storeOutUpdateBtn.setBackground(new java.awt.Color(255, 255, 255));
        storeOutUpdateBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        storeOutUpdateBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/update_72px.png"))); // NOI18N
        storeOutUpdateBtn.setText(" Store Out Item Update");
        storeOutUpdateBtn.setBorder(null);
        storeOutUpdateBtn.setFocusPainted(false);
        storeOutUpdateBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        storeOutUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeOutUpdateBtnActionPerformed(evt);
            }
        });

        storeOutSumBtn.setBackground(new java.awt.Color(255, 255, 255));
        storeOutSumBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        storeOutSumBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/business.png"))); // NOI18N
        storeOutSumBtn.setText(" Store Out Summary");
        storeOutSumBtn.setBorder(null);
        storeOutSumBtn.setFocusPainted(false);
        storeOutSumBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        storeOutSumBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeOutSumBtnActionPerformed(evt);
            }
        });

        nonStrUpdtBtn.setBackground(new java.awt.Color(255, 255, 255));
        nonStrUpdtBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
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
        nonStrViewBtn.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        nonStrViewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_64px.png"))); // NOI18N
        nonStrViewBtn.setText("  Non Store Item View");
        nonStrViewBtn.setBorder(null);
        nonStrViewBtn.setFocusPainted(false);
        nonStrViewBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(storeInUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeInSumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nonStrUpdtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nonStrViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(storeOutSumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeOutUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tempFoodUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tempFoodViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(showLedgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(245, 245, 245))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(storeInUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeOutUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(storeOutSumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeInSumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempFoodUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nonStrUpdtBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nonStrViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tempFoodViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(showLedgerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 530));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showLedgerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showLedgerBtnActionPerformed
        // TODO add your handling code here:
        ShowLedger st = new ShowLedger();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_showLedgerBtnActionPerformed

    private void tempFoodViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempFoodViewBtnActionPerformed
        // TODO add your handling code here:
        TempFoodView st = new TempFoodView();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_tempFoodViewBtnActionPerformed

    private void tempFoodUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempFoodUpdateBtnActionPerformed
        // TODO add your handling code here:
        TmpFoodUpdate st = new TmpFoodUpdate();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_tempFoodUpdateBtnActionPerformed

    private void storeInUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeInUpdateBtnActionPerformed
        // TODO add your handling code here:
        StoredItemUpdate st = new StoredItemUpdate();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_storeInUpdateBtnActionPerformed

    private void storeInSumBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeInSumBtnActionPerformed
        // TODO add your handling code here:
        StoreInSum st = new StoreInSum();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_storeInSumBtnActionPerformed

    private void storeOutUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeOutUpdateBtnActionPerformed
        // TODO add your handling code here:
        StoredItemUpdate st = new StoredItemUpdate();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_storeOutUpdateBtnActionPerformed

    private void storeOutSumBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeOutSumBtnActionPerformed
        // TODO add your handling code here:
        StoreOutSum st = new StoreOutSum();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_storeOutSumBtnActionPerformed

    private void nonStrUpdtBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nonStrUpdtBtnActionPerformed
        // TODO add your handling code here:
        NonStoredItemUpdate st = new NonStoredItemUpdate();
        st.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }//GEN-LAST:event_nonStrUpdtBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nonStrUpdtBtn;
    private javax.swing.JButton nonStrViewBtn;
    private javax.swing.JButton showLedgerBtn;
    private javax.swing.JButton storeInSumBtn;
    private javax.swing.JButton storeInUpdateBtn;
    private javax.swing.JButton storeOutSumBtn;
    private javax.swing.JButton storeOutUpdateBtn;
    private javax.swing.JButton tempFoodUpdateBtn;
    private javax.swing.JButton tempFoodViewBtn;
    // End of variables declaration//GEN-END:variables
}
