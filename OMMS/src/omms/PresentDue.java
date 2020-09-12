/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Ajmir
 */
public class PresentDue extends javax.swing.JFrame {
    
    Connection conn = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    TableModel model;
    DefaultTableModel tablemodel = null;
    int flag=0;

    /**
     * Creates new form PresentDue
     */
    public PresentDue() {
        initComponents();
        Tabledecoration();
        initialize();
        flag=1;
        
    }
    
    
    public void initialize(){
       
        conn = Jconnection.ConnecrDb(); // set connection with database
       
        formatter = new SimpleDateFormat("MMM dd,yyyy");
        formatter1 = new SimpleDateFormat("yyyyMMdd");  //date formate to covert into serial
        
        model = duetable.getModel();
        tablemodel = (DefaultTableModel) duetable.getModel();
        
        //TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tablemodel);
        //duetable.setRowSorter(sorter);
        
        int type= typecombo.getSelectedIndex();
        settable(type);
        idtxt.requestFocus();
    }
    
    
    
    public void Tabledecoration(){
        duetable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        duetable.getTableHeader().setOpaque(false);
        duetable.getTableHeader().setBackground(new Color(32,136,203));
        duetable.getTableHeader().setForeground(new Color(255,255,255));
        duetable.setRowHeight(25);
        
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        duetable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        duetable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
    }
    
    
    public void settable(int type){
        String sql="", strfrom="",strdue="";
        int serial=1;
        Date fromdate=null;
        
        
        if(type == 0){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by totaldue ASC";
        }
        else if(type ==1){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by totaldue desc";
        }
        else if(type ==2){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by hallid";
        }
        else if(type == 3){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by roomno desc";
        }
        else if(type == 4){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by entrydate";
        }
        else if(type == 5){
            sql="select hallid,name,roll,entrydate,dept,roomno,totaldue,contno from stuinfo ORDER by dept";
        }
        
        if(tablemodel.getRowCount() >0){
            tablemodel.setRowCount(0);
        }
        
        try{
            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while(rs.next()){
                try{
                    fromdate = formatter1.parse(rs.getString(4));
                    strfrom = formatter.format(fromdate);
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Failed to set date", "Date set error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(rs.getDouble(7)<0){
                    strdue = Double.toString(rs.getDouble(7)*-1)+"(A)";
                }
                else{
                    strdue = Double.toString(rs.getDouble(7));
                }
                Object o [] = {serial,rs.getInt(1),rs.getString(2),rs.getString(3),strfrom,rs.getString(5),rs.getString(6),strdue,rs.getString(8)};
                tablemodel.addRow(o);
                serial++;
            }
            psmt.close();
            rs.close();
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to fetch data checkdatabase", "Data fetch error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    public void search(String id){
        int totalrow =0, flag=0;
        totalrow = tablemodel.getRowCount();
        for(int i=0; i<totalrow;i++){
            if(model.getValueAt(i, 1).toString().equals(id) || model.getValueAt(i, 3).toString().equals(id)){
                duetable.requestFocus();
                duetable.changeSelection(i,0,false, false);
                flag=1;
                break;
            }
        }
        if( flag == 0){
            JOptionPane.showMessageDialog(null, "Id does not exist", "search", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    
    public void genpdf()
    {
        String path="", date="",ser="",hallid="",name="",roll="",dept="",room="",due="",mobile="";
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
                
                
            Image image1 = Image.getInstance("..\\\\MIST_Logo.png");
            image1.setAlignment(Element.ALIGN_CENTER);
            image1.scaleAbsolute(100, 70);
            //Add to document
            doc.add(image1);
            
            Paragraph osmany=new Paragraph("OSMANY HALL",FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, com.itextpdf.text.Font.NORMAL));
            osmany.setAlignment(Element.ALIGN_CENTER);
            doc.add(osmany);
            
            
            Paragraph p=new Paragraph("DUE MESS BILL REPORT\n",FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, com.itextpdf.text.Font.NORMAL));     
             
            p.setAlignment(Element.ALIGN_CENTER);
             
            doc.add(p);
            
            Date todaysdate =new Date();
            date = formatter.format(todaysdate);
            
            
            Paragraph q=new Paragraph("Date: "+date+"\n\n",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));           
            q.setAlignment(Element.ALIGN_CENTER);
            
            doc.add(q);
            
            Paragraph total=new Paragraph("Total Price:",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
            
            Paragraph newline=new Paragraph("\n");
            doc.add(newline);
             
            String[] header = new String[] { "Sl","Hall Id","Name","Roll","Dept","Room",
            "Due","Mobile No"};
            
            PdfPTable table = new PdfPTable(header.length);
            table.setHeaderRows(1);
            table.setWidths(new int[] { 2, 2, 4, 3,2,2,3,4});
            table.setWidthPercentage(98);
            table.setSpacingBefore(15);
            table.setSplitLate(false);
            for (String columnHeader : header) {
                PdfPCell headerCell = new PdfPCell();
                headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD)));
                headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            } 
             
             
            for(int i=0; i<duetable.getRowCount(); i++){

                ser= duetable.getValueAt(i, 0).toString();
                hallid= duetable.getValueAt(i, 1).toString();
                name= duetable.getValueAt(i, 2).toString();
                roll= duetable.getValueAt(i,3).toString();
                dept= duetable.getValueAt(i,5).toString();
                room= duetable.getValueAt(i,6).toString();
                due= duetable.getValueAt(i,7).toString();
                mobile= duetable.getValueAt(i,8).toString();
                 String[] content = new String[] { ser, hallid,name,roll,
                dept, room, due,mobile};

                for (String text : content) {
                    PdfPCell cell = new PdfPCell();
                    if(text.contains("(A)")){
                        //System.out.println("called");
                        cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD)));
                    }
                    else{
                        cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL)));
                    }
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setPadding(5);
                    table.addCell(cell);
                }

            }
            doc.add(table);
            doc.add(new Phrase("\n\n\n"));
            
            LineSeparator separator = new LineSeparator();
            separator.setPercentage(24);
            separator.setAlignment(Element.ALIGN_RIGHT);
            separator.setLineColor(BaseColor.BLACK);
            Chunk linebreak = new Chunk(separator);
            doc.add(linebreak);
            
            Paragraph name1 = new Paragraph("Asst/Associate Hall Provost   ",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));
            name1.setAlignment(Element.ALIGN_RIGHT);
            doc.add(name1);
            
            doc.add(new Phrase("\n\n\n"));
            
            LineSeparator separator1 = new LineSeparator();
            separator1.setPercentage(24);
            separator1.setAlignment(Element.ALIGN_RIGHT);
            separator1.setLineColor(BaseColor.BLACK);
            Chunk linebreak1 = new Chunk(separator1);
            doc.add(linebreak1);
            
            Paragraph name2 = new Paragraph("Hall Provost                 ",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));
            name2.setAlignment(Element.ALIGN_RIGHT);
            doc.add(name2);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Pdf generation error","File Error", JOptionPane.ERROR_MESSAGE);
        }
        
        doc.close();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        typecombo = new javax.swing.JComboBox<>();
        idtxt = new javax.swing.JTextField();
        searchbtn = new javax.swing.JButton();
        generatebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        duetable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/due-date.png"))); // NOI18N
        jLabel1.setText("Total Due");

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/sort.png"))); // NOI18N
        jLabel2.setText("Sort By");

        typecombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        typecombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Due(asc)", "Due(desc)", "Hall id", "Room No", "Admission Date", "Dept" }));
        typecombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecomboActionPerformed(evt);
            }
        });

        idtxt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idtxtActionPerformed(evt);
            }
        });

        searchbtn.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        searchbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/search.png"))); // NOI18N
        searchbtn.setText("Search");
        searchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbtnActionPerformed(evt);
            }
        });

        generatebtn.setFont(new java.awt.Font("Bell MT", 0, 16)); // NOI18N
        generatebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/pdf.png"))); // NOI18N
        generatebtn.setText("Generate");
        generatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typecombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchbtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(generatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        duetable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl", "Hall Id", "Name", "Roll", "Admission", "Dept", "Room", "Due", "Mobile No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        duetable.setRowHeight(26);
        duetable.setSelectionBackground(new java.awt.Color(237, 57, 97));
        duetable.setSelectionForeground(new java.awt.Color(240, 240, 240));
        duetable.setShowVerticalLines(false);
        duetable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(duetable);
        if (duetable.getColumnModel().getColumnCount() > 0) {
            duetable.getColumnModel().getColumn(0).setMaxWidth(35);
            duetable.getColumnModel().getColumn(2).setMinWidth(110);
            duetable.getColumnModel().getColumn(8).setMinWidth(110);
            duetable.getColumnModel().getColumn(8).setMaxWidth(170);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void typecomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typecomboActionPerformed
        // TODO add your handling code here:
        int type=-1;
        if(flag==1){
            type = typecombo.getSelectedIndex();
            
            settable(type);
        }
        idtxt.requestFocus();
    }//GEN-LAST:event_typecomboActionPerformed

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed
        // TODO add your handling code here:
        String id="";
        id= idtxt.getText().trim();
        if(!id.equals("")){
            search(id);
            idtxt.setText("");
        }
        else{
            JOptionPane.showMessageDialog(null, "Enter an Id", "search", JOptionPane.WARNING_MESSAGE);
        }
        idtxt.requestFocus();
    }//GEN-LAST:event_searchbtnActionPerformed

    private void idtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idtxtActionPerformed
        // TODO add your handling code here:
        searchbtn.doClick();
    }//GEN-LAST:event_idtxtActionPerformed

    private void generatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatebtnActionPerformed
        // TODO add your handling code here:
        genpdf();
    }//GEN-LAST:event_generatebtnActionPerformed

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
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PresentDue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PresentDue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable duetable;
    private javax.swing.JButton generatebtn;
    private javax.swing.JTextField idtxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchbtn;
    private javax.swing.JComboBox<String> typecombo;
    // End of variables declaration//GEN-END:variables
}
