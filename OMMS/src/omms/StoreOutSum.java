/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class StoreOutSum extends javax.swing.JFrame {

    /**
     * Creates new form StoreOutSum
     */
    public StoreOutSum() {
        initComponents();
    }

    
    
     /* intial data set*/
    public void initialtbl()
    {
            
        
        Date from=null, to =null;
        String item="";
        String stat="";
        from = fromdt_ch.getDate();
         to = todt_ch.getDate(); 
 
       
        tm = (DefaultTableModel) store_tbl.getModel();
        if(tm.getColumnCount() > 0){
            tm.setRowCount(0);
        }
        
        if( from != null && to != null && flag==1){
            item = firstupperCaseMaker(Item_cmb.getSelectedItem().toString().toLowerCase());
    
            setItemtable(from, to, item);
        }

    
    }
    
    //Generate pdf function
    public void genpdf()
    {
        String path="";
        JFileChooser j=new JFileChooser();
        //j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int x=j.showSaveDialog(this);
        
        if(x==JFileChooser.APPROVE_OPTION)
        {
            path=j.getSelectedFile().getPath();
        }
        
        Document doc=new Document ();
        
        try{
            
         
            PdfWriter.getInstance(doc,new FileOutputStream(path+".pdf"));
             
                doc.open();
        
            Paragraph p=new Paragraph("STORE IN REPORT \n\n\n");     
             
            p.setAlignment(Element.ALIGN_CENTER);
             
             doc.add(p);
             Date ddt=fromdt_ch.getDate();
             Date dt=todt_ch.getDate();
             
             Paragraph q=new Paragraph("From :"+formatter2.format(ddt).toString() +"\t \t"+"To :"+formatter2.format(dt).toString()+"\n\n");           
             q.setAlignment(Element.ALIGN_CENTER);
             doc.add(q);
             
             PdfPTable tbl =new PdfPTable(7);
             
             
             
             //Adding columns
             tbl.addCell("Serial");
             tbl.addCell("Date");
             tbl.addCell("Item");
             tbl.addCell("Quantity");
             tbl.addCell("Price");
             tbl.addCell("Average Price");
             tbl.addCell("Memo");
             
             
             
             for(int i=0; i<store_tbl.getRowCount(); i++){
                 
                 String ser= store_tbl.getValueAt(i, 0).toString();
                 String Date= store_tbl.getValueAt(i, 1).toString();
                 String Item= store_tbl.getValueAt(i, 2).toString();
                 String Quan= store_tbl.getValueAt(i,3).toString();
                 String pr= store_tbl.getValueAt(i,4).toString();
                 String avg= store_tbl.getValueAt(i,5).toString();
                 String mem= store_tbl.getValueAt(i,6).toString();
                 
                 
                 tbl.addCell(ser);
                 tbl.addCell(Date);
                 tbl.addCell(Item);
                 tbl.addCell(Quan);
                 tbl.addCell(pr);
                 tbl.addCell(avg);
                 tbl.addCell(mem);
             }
             
             doc.add(tbl);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Data not inserted","File Error", JOptionPane.ERROR_MESSAGE);
        }
        
        doc.close();
    }
    
    //get item unit
    
    public String firstupperCaseMaker(String s){
        int len = s.length();
        char[] c = s.toCharArray();
        int temp = (int)c[0] - 32;
        c[0] = (char)temp;
        
        return new String(c);
    }
    
 

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Item_cmb = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        fromdt_ch = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        todt_ch = new com.toedter.calendar.JDateChooser();
        pdf_btn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        store_tbl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/summary.png"))); // NOI18N
        jLabel1.setText("STORE OUT SUMMARY");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/list.png"))); // NOI18N
        jLabel2.setText("ITEM :");

        Item_cmb.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Item_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALL" }));
        Item_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Item_cmbActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel3.setText("FROM:");

        fromdt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        fromdt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fromdt_chPropertyChange(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/from-to date.png"))); // NOI18N
        jLabel4.setText("TO :");

        todt_ch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        todt_ch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                todt_chPropertyChange(evt);
            }
        });

        pdf_btn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pdf_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/power (1).png"))); // NOI18N
        pdf_btn.setText("Generate");
        pdf_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdf_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(pdf_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fromdt_ch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(todt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pdf_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(58, 58, 58))))
        );

        store_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Date", "Item", "Breakfast", "Lunch", "Dinner", "Average Price", "Memo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        store_tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(store_tbl);

        jPanel2.setBackground(new java.awt.Color(117, 175, 182));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 58, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Item_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Item_cmbActionPerformed
        initialtbl();
    }//GEN-LAST:event_Item_cmbActionPerformed

    private void fromdt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fromdt_chPropertyChange
        initialtbl();
    }//GEN-LAST:event_fromdt_chPropertyChange

    private void todt_chPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_todt_chPropertyChange
        initialtbl();
    }//GEN-LAST:event_todt_chPropertyChange

    private void pdf_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdf_btnActionPerformed
        genpdf();
    }//GEN-LAST:event_pdf_btnActionPerformed

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
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreOutSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreOutSum().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Item_cmb;
    private com.toedter.calendar.JDateChooser fromdt_ch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pdf_btn;
    private javax.swing.JTable store_tbl;
    private com.toedter.calendar.JDateChooser todt_ch;
    // End of variables declaration//GEN-END:variables
}
