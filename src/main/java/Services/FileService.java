package Services;

import Models.Inventory;
import java.io.*;

public class FileService {

    public void saveInventory(Inventory inventory, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(inventory);
        }
    }

    public Inventory loadInventory(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Inventory) ois.readObject();
        }
    }

    public void exportToCSV(Inventory inventory, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID;Nom;Description;Prix;Quantité;Stock Minimum;Catégorie;Fournisseur;SKU;Valeur Totale");

            for (var product : inventory.getProducts()) {
                writer.println(String.format("%d;%s;%s;%.2f;%d;%d;%s;%s;%s;%.2f",
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getMinStockLevel(),
                        product.getCategory() != null ? product.getCategory().getName() : "N/A",
                        product.getSupplier() != null ? product.getSupplier().getName() : "N/A",
                        product.getSku(),
                        product.getTotalValue()));
            }
        }
    }

    public void exportLowStockReport(Inventory inventory, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== RAPPORT STOCK FAIBLE ===\n");
            writer.println("Produits avec stock faible:\n");
            writer.println("ID\tNom\tQuantité\tStock Minimum");

            for (var product : inventory.getLowStockProducts()) {
                writer.println(String.format("%d\t%s\t%d\t\t%d",
                        product.getId(),
                        product.getName(),
                        product.getQuantity(),
                        product.getMinStockLevel()));
            }

            writer.println("\n=== TOTAL ===\n");
            writer.println("Nombre de produits en stock faible: " + inventory.getLowStockProducts().size());
            writer.println("Valeur totale de l'inventaire: " + inventory.getTotalInventoryValue() + " €");
        }
    }
}