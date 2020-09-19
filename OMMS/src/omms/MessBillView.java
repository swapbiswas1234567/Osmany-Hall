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
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Asus
 */
public class MessBillView extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    TableModel model;
    DefaultTableModel tablemodel = null;

    SimpleDateFormat tableDateFormatter = new SimpleDateFormat("MMM dd,yyyy");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");

    int flag = 0;
    String saveyear="";

    /**
     * Creates new form MessBillView
     */
    public MessBillView() {
        initComponents();
        initialize();
        setTitle("Mess Bill View");
    }

    public void initialize() {
        conn = Jconnection.ConnecrDb(); // set connection with database        
        setDateChoosers();
        Tabledecoration();
        idTxt.requestFocus(); // setitng the focus to the Hall Id searchDate button
        closeBtn();
        
         try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PresentDue.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void closeBtn() {
        JFrame frame = this;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                try {
                    conn.close();
                    if (UserLog.name.equals("accountant")) {
                        DashboardAccountant das = new DashboardAccountant();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("provost")) {
                        DashboardHallAutho das = new DashboardHallAutho();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("mess")) {
                        DashboardMess das = new DashboardMess();
                        das.setVisible(true);
                        frame.setVisible(false);
                    } else if (UserLog.name.equals("captain")) {
                        DashboardMessCap das = new DashboardMessCap();
                        das.setVisible(true);
                        frame.setVisible(false);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Oops! There are some problems!", "Unknown Error Occured!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setDateChoosers() {
        Date date = new Date();
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        monthCombo.setSelectedIndex(Integer.parseInt(month.format(date)) - 1);
        yearTxt.setText(year.format(date));
    }
    
    
    public void Tabledecoration() {
        showBillTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 16));
        showBillTable.getTableHeader().setOpaque(false);
        showBillTable.getTableHeader().setBackground(new Color(32, 136, 203));
        showBillTable.getTableHeader().setForeground(new Color(255, 255, 255));
        showBillTable.setRowHeight(25);
        showBillTable.setFont(new Font("Segeo UI", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();   //alignment of table to center
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        showBillTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(3).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(4).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(5).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(6).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(7).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(8).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(9).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(10).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(11).setCellRenderer(centerRender);
        showBillTable.getColumnModel().getColumn(12).setCellRenderer(centerRender);
    }
    
    public String findHallId() {
        String id = idTxt.getText();
        if (!id.equals("")) {
            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE hallid = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            try {
                ps = conn.prepareStatement("SELECT hallid FROM stuinfo WHERE roll = ?");
                ps.setString(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String hallid = String.valueOf(rs.getInt(1));
                    System.out.println("Id - " + hallid + " Roll - " + id);
                    ps.close();
                    rs.close();
                    return hallid;
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data cannot be fethced", "Database Error", JOptionPane.ERROR_MESSAGE);
                return "";
            }

            JOptionPane.showMessageDialog(null, "Inserted Hall Id/Roll can't be found", "Data No Found", JOptionPane.ERROR_MESSAGE);
            return "";
        }
        return "";
    }

    public void showBill() {
        int month = monthCombo.getSelectedIndex() + 1;
        int year;
        if (!yearTxt.getText().equals("")) {
            try{
                year = Integer.parseInt(yearTxt.getText());
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Year format error", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (year < 0) {
                JOptionPane.showMessageDialog(null, "Year Can't be Negetive", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Year field is empty", "Wrong Insertion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int serial = 0;

        tablemodel = (DefaultTableModel) showBillTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            tablemodel.setRowCount(0);
        }
        saveyear = yearTxt.getText().trim();
        
        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, bill, others, fine, waive, due FROM stuinfo JOIN billhistory USING(hallid) WHERE month = ? AND year = ? ORDER BY hallid");
            ps.setInt(1, month);
            ps.setInt(2, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                int bill = rs.getInt(5);
                int others = rs.getInt(6);
                int fine = rs.getInt(7);
                int waive = rs.getInt(8);
                int due = rs.getInt(9);
                String str = "";
                serial++;
                int total = bill + others + fine - waive + due;
                str = Integer.toString(total);
                if (total < 0) {
                    total *= -1;
                    str = Integer.toString(total);
                    str += "(A)";
                }
                if (due < 0) {
                    due *= -1;
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, 0, due, str,"Current"};
                    tablemodel.addRow(obj);
                } else {
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, due, 0, str,"Current"};
                    tablemodel.addRow(obj);
                }

            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error From stuinfo and billhistory", "Database ERROR!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            ps = conn.prepareStatement("SELECT hallid, name, roll, roomno, bill, others, fine, waive, due FROM previousstudents JOIN billhistory USING(hallid) WHERE month = ? AND year = ? ORDER BY hallid");
            ps.setInt(1, month);
            ps.setInt(2, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                int bill = rs.getInt(5);
                int others = rs.getInt(6);
                int fine = rs.getInt(7);
                int waive = rs.getInt(8);
                int due = rs.getInt(9);
                String str = "";
                serial++;
                int total = bill + others + fine - waive + due;
                str = Integer.toString(total);
                if (total < 0) {
                    total *= -1;
                    str = Integer.toString(total);
                    str += "(A)";
                }
                if (due < 0) {
                    due *= -1;
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, 0, due, str,"Ex"};
                    tablemodel.addRow(obj);
                } else {
                    Object obj[] = {serial, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), bill, others, fine, waive, due, 0, str,"Ex"};
                    tablemodel.addRow(obj);
                }

            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Fetching Error From perviousstudents and billhistory", "Database ERROR!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public void highlightStd(String id) {
        tablemodel = (DefaultTableModel) showBillTable.getModel();
        if (tablemodel.getRowCount() > 0) {
            int chk = 0;
            for (int j = 1; j <= 3; j += 2) {//For each column
                for (int i = 0; i < tablemodel.getRowCount(); i++) {//For each row in that column
                    if (tablemodel.getValueAt(i, j).toString().equals(id)) {//Search the model
                        showBillTable.requestFocus();
                        showBillTable.changeSelection(i, j, false, false);
                        chk++;
                        break;
                    }
                }//For loop inner
            }//For loop outer
            if (chk == 0) {
                JOptionPane.showMessageDialog(null, id + " Not Found On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Values On This Table", "Not Found!!!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    public void genpdf()
    {
        String path="", month="",date="",sl="",hallid="", name="", roll="", room="", mess="",
                others="", fine="", waive="", due ="",total="";
        Double prev=0.0, advance=0.0;
        int serial=1;
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
            
            month = monthCombo.getSelectedItem().toString();
            Paragraph p=new Paragraph("MESS Bill ("+month+","+saveyear+")\n",FontFactory.getFont(FontFactory.TIMES_ROMAN, 17, com.itextpdf.text.Font.NORMAL));     
             
            p.setAlignment(Element.ALIGN_CENTER);
             
            doc.add(p);
            
            Date todaysdate =new Date();
            date = tableDateFormatter.format(todaysdate);
            
            
            Paragraph q=new Paragraph("Date: "+date+"\n\n",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL));           
            q.setAlignment(Element.ALIGN_CENTER);
            
            doc.add(q);
            
//            Paragraph total=new Paragraph("Total Price:",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD));
//            total.setAlignment(Element.ALIGN_RIGHT);
//            doc.add(total);
            
            Paragraph newline=new Paragraph("\n");
            doc.add(newline);
             
            String[] header = new String[] { "Sl","Hall ID","Name","Roll","Room No","Mess Bill","Others","Fine","Waive","Due","Total"};
            
            PdfPTable table = new PdfPTable(header.length);
            table.setHeaderRows(1);
            table.setWidths(new int[] { 2, 3, 4, 3, 3, 3, 3, 3, 3, 3, 3});
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
             
             
            for(int i=0; i<showBillTable.getRowCount(); i++){
                
                sl= showBillTable.getValueAt(i, 0).toString();
                hallid= showBillTable.getValueAt(i, 1).toString();
                name= showBillTable.getValueAt(i, 2).toString();
                roll= showBillTable.getValueAt(i, 3).toString();
                room = showBillTable.getValueAt(i, 4).toString();
                mess = showBillTable.getValueAt(i, 5).toString();
                others = showBillTable.getValueAt(i, 6).toString();
                fine = showBillTable.getValueAt(i, 7).toString();
                waive = showBillTable.getValueAt(i, 8).toString();
                prev = Double.parseDouble(showBillTable.getValueAt(i, 9).toString());
                advance = Double.parseDouble(showBillTable.getValueAt(i, 10).toString());
                total = showBillTable.getValueAt(i, 11).toString();
                
                if( prev != 0){
                    due = Double.toString(prev);
                }
                else if( advance != 0){
                    due = Double.toString(advance)+" (A)";
                }
                else{
                    due = "0";
                }
                
                String[] content = new String[] {sl, hallid, name , roll, room, mess,
                others, fine, waive, due, total};

                for (String text : content) {
                    PdfPCell cell = new PdfPCell();
                    if( text.contains("(A)")){
                        cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD)));
                    }else{
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
        monthCombo = new javax.swing.JComboBox<>();
        yearTxt = new javax.swing.JTextField();
        idTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        showBillTable = new javax.swing.JTable();
        genpdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 229));

        jLabel1.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/billMess.png"))); // NOI18N
        jLabel1.setText("MESS BILL VIEW");

        monthCombo.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        monthCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        monthCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthComboActionPerformed(evt);
            }
        });

        yearTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        yearTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearTxtActionPerformed(evt);
            }
        });

        idTxt.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        idTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idTxtActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/monthMess.png"))); // NOI18N
        jLabel2.setText(" Month");

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/yearMess.png"))); // NOI18N
        jLabel3.setText(" Year");

        jLabel4.setFont(new java.awt.Font("Bell MT", 0, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/identification_documents_24px.png"))); // NOI18N
        jLabel4.setText(" Hall Id / Roll");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(145, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(171, 171, 171)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(183, 183, 183)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 100, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        showBillTable.setAutoCreateRowSorter(true);
        showBillTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SN", "Hall Id", "Name", "Roll", "Room No", "Meal Charge", "Others", "Fine", "Waive", "Previous Due", "Advance", "Total", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showBillTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(showBillTable);
        if (showBillTable.getColumnModel().getColumnCount() > 0) {
            showBillTable.getColumnModel().getColumn(0).setMinWidth(45);
            showBillTable.getColumnModel().getColumn(0).setMaxWidth(60);
            showBillTable.getColumnModel().getColumn(1).setMinWidth(80);
            showBillTable.getColumnModel().getColumn(1).setMaxWidth(100);
            showBillTable.getColumnModel().getColumn(2).setMinWidth(225);
            showBillTable.getColumnModel().getColumn(2).setMaxWidth(500);
            showBillTable.getColumnModel().getColumn(9).setMinWidth(125);
            showBillTable.getColumnModel().getColumn(9).setMaxWidth(150);
        }

        genpdf.setFont(new java.awt.Font("Bell MT", 0, 24)); // NOI18N
        genpdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagepackage/pdf.png"))); // NOI18N
        genpdf.setText("Generate PDF");
        genpdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genpdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(genpdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(genpdf, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void yearTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearTxtActionPerformed
        // TODO add your handling code here:
        showBill();
    }//GEN-LAST:event_yearTxtActionPerformed

    private void monthComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthComboActionPerformed
        // TODO add your handling code here:
        if (flag == 1) {
            showBill();
        }
        flag = 1;
    }//GEN-LAST:event_monthComboActionPerformed

    private void idTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idTxtActionPerformed
        // TODO add your handling code here:
        if (!idTxt.getText().equals("")) {
            String id = idTxt.getText();
            highlightStd(id);
        }
    }//GEN-LAST:event_idTxtActionPerformed

    private void genpdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genpdfActionPerformed
        // TODO add your handling code here:
        genpdf();
    }//GEN-LAST:event_genpdfActionPerformed

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
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MessBillView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MessBillView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton genpdf;
    private javax.swing.JTextField idTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> monthCombo;
    private javax.swing.JTable showBillTable;
    private javax.swing.JTextField yearTxt;
    // End of variables declaration//GEN-END:variables
}
