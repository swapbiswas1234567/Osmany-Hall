package omms;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class StoreInSum extends javax.swing.JFrame {
    
     ///Variable declaration
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    DefaultTableModel tm = null;
    StoredItem st ;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatter2;
    TableModel model;
    DefaultTableModel tablemodel = null;
    DecimalFormat dec;
    int selectedRow;
    DecimalFormat dec2;
   
    int flag;
    PreparedStatement psmt1 = null;
    ResultSet rs1 = null;
    int ser=0;
    
    
    public StoreInSum() {
        initComponents();
        initialize();
        tabledecoration();
        dateNtableset();
        itemcombo_set();
        flag=1;
        initialtbl();
    }

           /**Initializing Variable Function **/
    public void initialize()
    {
        conn= Jconnection.ConnecrDb();
        psmt=null;
        rs=null;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  
        formatter2 = new SimpleDateFormat("MMM dd,yyyy");
        dec2 = new DecimalFormat("#0.00");
        
        selectedRow = -1;
        this.setTitle("Non Stored Item View");
        
    }
    
    
    ///Table decoration
    
    public void tabledecoration(){
        store_tbl.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 20));
        store_tbl.getTableHeader().setOpaque(false);
        store_tbl.getTableHeader().setBackground(new Color(32,136,203));
        store_tbl.getTableHeader().setForeground(new Color(255,255,255));
        store_tbl.setRowHeight(30);
        store_tbl.setFont(new Font("Segeo UI", Font.PLAIN, 18));
        

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        
        store_tbl.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        store_tbl.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        
        
    }

   public void dateNtableset()
    {
        tm=(DefaultTableModel)store_tbl.getModel();
        tm.setRowCount(0);
        /***Date Setting**/
        Date date= new Date();
        fromdt_ch.setDate(date);
        todt_ch.setDate(date);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) fromdt_ch.getDateEditor();
        editor.setEditable(false);
        editor = (JTextFieldDateEditor) todt_ch.getDateEditor();
        editor.setEditable(false);
        
    }
     
    
    
    //Combo Name of item setting
    public void itemcombo_set()
    {
        try{
           psmt=conn.prepareStatement("select name from storeditem");
           rs=psmt.executeQuery();
           
           while(rs.next())
           {
               String item = firstupperCaseMaker(rs.getString(1).toLowerCase());
               Item_cmb.addItem(item);
           }
           
           psmt.close();
           rs.close();
           
       }
       catch(Exception e)
       {
         JOptionPane.showMessageDialog(null, "No item found!", "An Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
            
       }
    }

    
     ///Setting function of  for table to show non stored item
    public void setItemtable(Date from, Date to, String item ){
        
        
        int fromserial = 0, toserial = 0;
      
        String strdate = "";
        ser=0;
        Date date=null;
        tm = (DefaultTableModel) store_tbl.getModel();
       
        try{
            fromserial = Integer.parseInt(formatter1.format(from));
            toserial = Integer.parseInt(formatter1.format(to));
            
            if(fromserial>toserial)
            {
                JOptionPane.showConfirmDialog(null, "Invalid Date","Date Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Date format in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
        }
   
            try{
            if(item.equals("All")){
            psmt = conn.prepareStatement("select  serial,item,inamount,price,memono from storeinout where serial>=? and serial<=? order by serial,item ;  ");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            rs = psmt.executeQuery();
            }
            else
            {
            psmt = conn.prepareStatement("select  serial,item,inamount,price,memono from storeinout where  serial>=? and serial<=? and item=? order by serial,item ;  ");
            psmt.setInt(1, fromserial);
            psmt.setInt(2, toserial);
            psmt.setString(3, item);
            rs = psmt.executeQuery();
                
            }
            while(rs.next()){
                
                try{
                date = formatter1.parse(rs.getString(1));
                strdate = formatter2.format(date);
                }
                catch(ParseException e){
                    JOptionPane.showMessageDialog(null, "Date Parse "
                            + "in setedittablevalue","Date parsing error", JOptionPane.ERROR_MESSAGE);
                }
                
                
                    if(rs.getString(5).equals("###") && rs.getDouble(3)!=0.0){
                
                        ser++;
                        Object o [] = {ser,strdate,rs.getString(2),rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),""};
                        tm.addRow(o);
                
                    }
                    else if(rs.getDouble(3)!=0.0){
                           
                        ser++;
                        Object o [] = {ser,strdate,rs.getString(2),rs.getDouble(3),rs.getDouble(4),dec2.format(rs.getDouble(4)/rs.getDouble(3)),rs.getString(5)};
                        tm.addRow(o);
                    
                    }
                
                    
                
            }
            psmt.close();
            rs.close();
            
        
        }
           
        
       catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data for"
                    + "set table ", "Data fetch error", JOptionPane.ERROR_MESSAGE);
        } 
        
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
            
            Paragraph p=new Paragraph("STORE IN REPORT\n",FontFactory.getFont(FontFactory.HELVETICA, 17, com.itextpdf.text.Font.BOLD));     
             
            p.setAlignment(Element.ALIGN_CENTER);
             
            doc.add(p);
            Date ddt=fromdt_ch.getDate();
            Date dt=todt_ch.getDate();
            
            
            Paragraph q=new Paragraph("From :"+formatter2.format(ddt).toString() +"\t \t"+"To :"+formatter2.format(dt).toString(),FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.UNDERLINE));           
            q.setAlignment(Element.ALIGN_CENTER);
            
            doc.add(q);
            
            Paragraph total=new Paragraph("Total Price:",FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            
            Paragraph newline=new Paragraph("\n");
            doc.add(newline);
             
            String[] header = new String[] { "Serial", "Date", "Name",
            "Quantity", "Price","Avg Price","Memo" };
            
            PdfPTable table = new PdfPTable(header.length);
            table.setHeaderRows(1);
            table.setWidths(new int[] { 2, 3, 4, 3, 2,3,2 });
            table.setWidthPercentage(98);
            table.setSpacingBefore(15);
            table.setSplitLate(false);
            for (String columnHeader : header) {
                PdfPCell headerCell = new PdfPCell();
                headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.BOLD)));
                headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            } 
             
             
             for(int i=0; i<store_tbl.getRowCount(); i++){
                 
                 String ser= store_tbl.getValueAt(i, 0).toString();
                 String Date= store_tbl.getValueAt(i, 1).toString();
                 String Item= store_tbl.getValueAt(i, 2).toString();
                 String Quan= store_tbl.getValueAt(i,3).toString();
                 String pr= store_tbl.getValueAt(i,4).toString();
                 String avg= store_tbl.getValueAt(i,5).toString();
                 String mem= store_tbl.getValueAt(i,6).toString();
                 String[] content = new String[] { ser, Date,
                Item, Quan, pr,avg,mem };
                
                for (String text : content) {
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.NORMAL)));
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setPadding(5);
                    table.addCell(cell);
                }

             }
             doc.add(table);
            doc.add(new Phrase("\n"));
            LineSeparator separator = new LineSeparator();
            separator.setPercentage(98);
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            Chunk linebreak = new Chunk(separator);
            doc.add(linebreak);
             
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
        jLabel1.setText("STORE IN SUMMARY");

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fromdt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(todt_ch, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(pdf_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pdf_btn, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(todt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Item_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(fromdt_ch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        store_tbl.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        store_tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Serial", "Date", "Item", "Quantity", "Price", "Avg Price", "Memo No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        store_tbl.setSelectionBackground(new java.awt.Color(232, 57, 97));
        store_tbl.setSelectionForeground(new java.awt.Color(240, 240, 240));
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
            .addGap(0, 61, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(StoreInSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreInSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreInSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreInSum.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreInSum().setVisible(true);
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
