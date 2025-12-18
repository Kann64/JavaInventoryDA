package Services;

import Models.Inventory;
import Models.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportService {

    public void exportToPDF(Inventory inventory, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        // Titre
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Rapport d'Inventaire", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Date de génération
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Paragraph dateParagraph = new Paragraph("Généré le: " + date, dateFont);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateParagraph.setSpacingAfter(20);
        document.add(dateParagraph);

        // Tableau des produits
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        // En-têtes
        String[] headers = {"ID", "Nom", "Catégorie", "Quantité", "Prix", "Valeur"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        // Données
        for (Product product : inventory.getProducts()) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getCategory() != null ? product.getCategory().getName() : "N/A");
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.format("%.2f €", product.getPrice()));
            table.addCell(String.format("%.2f €", product.getTotalValue()));
        }

        document.add(table);

        // Totaux
        document.add(new Paragraph("\n\n"));

        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(50);
        totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalsTable.addCell("Total produits:");
        totalsTable.addCell(String.valueOf(inventory.getProducts().size()));

        totalsTable.addCell("Valeur totale:");
        totalsTable.addCell(String.format("%.2f €", inventory.getTotalInventoryValue()));

        totalsTable.addCell("Produits stock faible:");
        totalsTable.addCell(String.valueOf(inventory.getLowStockProducts().size()));

        document.add(totalsTable);

        document.close();
    }
}