package controller.Professeur;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.Etudiant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfGenerator {

    public static void generatePdf(List<Etudiant> students, File destination) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(destination));
            document.open();

            // Title of the PDF
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Student List", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            // Line break
            document.add(new Paragraph("\n"));

            // Create table with 3 columns
            PdfPTable table = new PdfPTable(3); // 3 columns for ID, Name, and Prenom

            // Table header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            table.addCell(new Phrase("ID", headerFont));
            table.addCell(new Phrase("Name", headerFont));
            table.addCell(new Phrase("Prenom", headerFont));

            // Add student data
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12);
            for (Etudiant student : students) {
                table.addCell(new Phrase(String.valueOf(student.getId()), contentFont));
                table.addCell(new Phrase(student.getNom(), contentFont));
                table.addCell(new Phrase(student.getPrenom(), contentFont));
            }

            // Add the table to the document
            document.add(table);

            document.close();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
