/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import java.awt.Color;
import omms.accountantDashboardInternalJframe.AccBillDash;
import omms.accountantDashboardInternalJframe.AccPaymentDash;
import omms.accountantDashboardInternalJframe.AccViewDash;

/**
 *
 * @author Asus
 */
public class DashboardAccountant extends javax.swing.JFrame {

    int avd = 0, abd = 0, apd = 0;

    /**
     * Creates new form NewDashboard
     */
    public DashboardAccountant() {
        initComponents();
        setTitle("Accountant Dashboard");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidePanel = new javax.swing.JPanel();
        billPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        billLbl = new javax.swing.JLabel();
        logoPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        viewPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        viewLbl = new javax.swing.JLabel();
        paymentPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        paymentLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        exitBtn = new javax.swing.JLabel();
        dashhboardPanel = new javax.swing.JDesktopPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidePanel.setBackground(new java.awt.Color(33, 102, 142));
        sidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        billPanel.setBackground(new java.awt.Color(33, 102, 142));
        billPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                billPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                billPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                billPanelMousePressed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(33, 102, 142));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/receipt_dollar_36px.png"))); // NOI18N

        billLbl.setFont(new java.awt.Font("Bell MT", 1, 26)); // NOI18N
        billLbl.setForeground(new java.awt.Color(255, 255, 255));
        billLbl.setText("Bill");

        javax.swing.GroupLayout billPanelLayout = new javax.swing.GroupLayout(billPanel);
        billPanel.setLayout(billPanelLayout);
        billPanelLayout.setHorizontalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(billLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        billPanelLayout.setVerticalGroup(
            billPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addComponent(billLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sidePanel.add(billPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 310, 60));

        logoPanel.setBackground(new java.awt.Color(253, 253, 253));

        jLabel15.setBackground(new java.awt.Color(253, 253, 253));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/mist_pattern_logo.png"))); // NOI18N

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );

        sidePanel.add(logoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 310, 160));

        viewPanel.setBackground(new java.awt.Color(33, 102, 142));
        viewPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                viewPanelMousePressed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(33, 102, 142));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/view_32px.png"))); // NOI18N

        viewLbl.setFont(new java.awt.Font("Bell MT", 1, 26)); // NOI18N
        viewLbl.setForeground(new java.awt.Color(255, 255, 255));
        viewLbl.setText("View");

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(viewLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addComponent(viewLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sidePanel.add(viewPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 310, 60));

        paymentPanel.setBackground(new java.awt.Color(33, 102, 142));
        paymentPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                paymentPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                paymentPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                paymentPanelMousePressed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(33, 102, 142));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/money_bag_28px.png"))); // NOI18N

        paymentLbl.setFont(new java.awt.Font("Bell MT", 1, 26)); // NOI18N
        paymentLbl.setForeground(new java.awt.Color(255, 255, 255));
        paymentLbl.setText("Payment");

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(paymentLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addComponent(paymentLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sidePanel.add(paymentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 310, 60));

        getContentPane().add(sidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 310, 780));

        jPanel2.setBackground(new java.awt.Color(70, 139, 178));

        jLabel13.setFont(new java.awt.Font("Berlin Sans FB", 0, 40)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("WELCOME TO THE DASHBOARD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 860, 170));

        jPanel9.setBackground(new java.awt.Color(253, 253, 253));

        jLabel14.setFont(new java.awt.Font("Berlin Sans FB", 0, 28)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 153, 153));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("OSMANY HALL MESS MANAGEMENT SYSTEM");

        exitBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/criss-cross (1).png"))); // NOI18N
        exitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 860, 80));

        dashhboardPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout dashhboardPanelLayout = new javax.swing.GroupLayout(dashhboardPanel);
        dashhboardPanel.setLayout(dashhboardPanelLayout);
        dashhboardPanelLayout.setHorizontalGroup(
            dashhboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        dashhboardPanelLayout.setVerticalGroup(
            dashhboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        getContentPane().add(dashhboardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 250, 860, 530));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitBtnMouseClicked
        // TODO add your handling code here:
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_exitBtnMouseClicked

    private void billPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billPanelMousePressed
        // TODO add your handling code here:
        billPanel.setBackground(new Color(129, 214, 163));
        paymentPanel.setBackground(new Color(33, 102, 142));
        viewPanel.setBackground(new Color(33, 102, 142));
        abd = 1;
        avd = apd = 0;
        billLbl.setForeground(Color.BLACK); 
        viewLbl.setForeground(Color.WHITE);
        paymentLbl.setForeground(Color.WHITE);     
        
        dashhboardPanel.removeAll();
        AccBillDash sid = new AccBillDash();
        dashhboardPanel.add(sid).setVisible(true);
    }//GEN-LAST:event_billPanelMousePressed

    private void billPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billPanelMouseEntered
        // TODO add your handling code here:
        if (abd != 1) {
            billPanel.setBackground(new Color(45, 45, 45));
        }
    }//GEN-LAST:event_billPanelMouseEntered

    private void billPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billPanelMouseExited
        // TODO add your handling code here:
        if (abd != 1) {
            billPanel.setBackground(new Color(33, 102, 142));
        }
    }//GEN-LAST:event_billPanelMouseExited

    private void paymentPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentPanelMouseEntered
        // TODO add your handling code here:
        if (apd != 1) {
            paymentPanel.setBackground(new Color(45, 45, 45));
        }        
    }//GEN-LAST:event_paymentPanelMouseEntered

    private void paymentPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentPanelMouseExited
        // TODO add your handling code here:
        if (apd != 1) {
            paymentPanel.setBackground(new Color(33, 102, 142));
        }
    }//GEN-LAST:event_paymentPanelMouseExited

    private void paymentPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentPanelMousePressed
        // TODO add your handling code here:
        paymentPanel.setBackground(new Color(129, 214, 163));
        viewPanel.setBackground(new Color(33, 102, 142));
        billPanel.setBackground(new Color(33, 102, 142));
        apd = 1;
        abd = avd = 0;
        paymentLbl.setForeground(Color.BLACK);   
        billLbl.setForeground(Color.WHITE); 
        viewLbl.setForeground(Color.WHITE);  
        
        dashhboardPanel.removeAll();
        AccPaymentDash sid = new AccPaymentDash();
        dashhboardPanel.add(sid).setVisible(true);
    }//GEN-LAST:event_paymentPanelMousePressed

    private void viewPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMousePressed
        // TODO add your handling code here:
        viewPanel.setBackground(new Color(129, 214, 163));
        paymentPanel.setBackground(new Color(33, 102, 142));
        billPanel.setBackground(new Color(33, 102, 142));
        avd = 1;
        abd = apd = 0;
        viewLbl.setForeground(Color.BLACK);   
        billLbl.setForeground(Color.WHITE); 
        paymentLbl.setForeground(Color.WHITE);      
        
        dashhboardPanel.removeAll();
        AccViewDash sid = new AccViewDash();
        dashhboardPanel.add(sid).setVisible(true);
    }//GEN-LAST:event_viewPanelMousePressed

    private void viewPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseExited
        // TODO add your handling code here:
        if (avd != 1) {
            viewPanel.setBackground(new Color(33, 102, 142));
        }
    }//GEN-LAST:event_viewPanelMouseExited

    private void viewPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPanelMouseEntered
        // TODO add your handling code here:
        if (avd != 1) {
            viewPanel.setBackground(new Color(45, 45, 45));
        }
    }//GEN-LAST:event_viewPanelMouseEntered


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
            java.util.logging.Logger.getLogger(NewDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel billLbl;
    private javax.swing.JPanel billPanel;
    private javax.swing.JDesktopPane dashhboardPanel;
    private javax.swing.JLabel exitBtn;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JLabel paymentLbl;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JLabel viewLbl;
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables

}
