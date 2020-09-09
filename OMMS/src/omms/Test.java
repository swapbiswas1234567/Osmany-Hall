/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omms;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.pdf.draw.LineSeparator;
/**
 *
 * @author Asus
 */
public class Test {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

//    public void test() throws SQLException {
////        conn = Jconnection.ConnecrDb();
////        try {
////
////            ps = conn.prepareStatement("Select * From stuinfo");
////            rs = ps.executeQuery();
////            while (rs.next()) {
////                System.out.println(" " + rs.getString(2) + " " + rs.getString(3) + " \n" );
////            }
////            ps.close();
////            rs.close();
////        } catch (Exception e) {
////        }
//    }
    
    public void gen(String dest) throws DocumentException, IOException{
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.setMargins(15, 15, 55, 35);
        document.open();
        String[] header = new String[] { "Header1", "Header2", "Header3",
            "Header4", "Header5" };
	String[] content = new String[] { "column 1", "column 2",
            "some Text in column 3", "Test data ", "column 5" };
        PdfPTable table = new PdfPTable(header.length);
	table.setHeaderRows(1);
	table.setWidths(new int[] { 3, 2, 4, 3, 2 });
        table.setWidthPercentage(98);
        table.setSpacingBefore(15);
        table.setSplitLate(false);
	for (String columnHeader : header) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(8);
            table.addCell(headerCell);
	}
        for (int i = 0; i < 10; i++) {
            int j = 0;
            for (String text : content) {
                if (i == 13 && j == 3) {
                    text = "Test data changed\n";
                }
                j++;
                PdfPCell cell = new PdfPCell();
                cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
                cell.setBorderColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }
        }
        document.add(table);
        document.add(new Phrase("\n"));
        LineSeparator separator = new LineSeparator();
        separator.setPercentage(98);
        separator.setLineColor(BaseColor.LIGHT_GRAY);
        Chunk linebreak = new Chunk(separator);
        document.add(linebreak);
//        for (int k = 0; k < 5; k++) {
//            Paragraph info = new Paragraph("Some title", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
//            info.setSpacingBefore(12f);
//            document.add(info);
//            table = new PdfPTable(header.length);
//            table.setHeaderRows(1);
//            table.setWidths(new int[] { 3, 2, 4, 3, 2 });
//            table.setWidthPercentage(98);
//            table.setSpacingBefore(15);
//            table.setSplitLate(false);
////            for (String columnHeader : header) {
////                PdfPCell headerCell = new PdfPCell();
////                headerCell.addElement(new Phrase(columnHeader, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
////                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
////                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
////                headerCell.setBorderColor(BaseColor.LIGHT_GRAY);
////                headerCell.setPadding(8);
////                table.addCell(headerCell);
////            }
////            for (String text : content) {
////                PdfPCell cell = new PdfPCell();
////                cell.addElement(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL)));
////                cell.setBorderColor(BaseColor.LIGHT_GRAY);
////                cell.setPadding(5);
////                table.addCell(cell);
////            }
//            //document.add(table);
//            separator = new LineSeparator();
//            separator.setPercentage(98);
//            separator.setLineColor(BaseColor.LIGHT_GRAY);
//            linebreak = new Chunk(separator);
//            document.add(linebreak);
//        }
        document.close();
    }

   

}
