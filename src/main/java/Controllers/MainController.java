package Controllers;

import Services.InventoryService;
import Services.ExportService;       // ✅ Import ajouté
import Models.Inventory;             // ✅ Import ajouté
import Utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML private Button btnManageProducts;
    @FXML private Button btnViewInventory;
    @FXML private Button btnExportReports;
    @FXML private Button btnExit;

    private InventoryService inventoryService;

    public MainController() {
        this.inventoryService = new InventoryService();
    }

    @FXML
    private void initialize() {
        setupButtonActions();
    }

    private void setupButtonActions() {
        btnManageProducts.setOnAction(e -> openProductManagement());
        btnViewInventory.setOnAction(e -> openInventoryView());
        btnExportReports.setOnAction(e -> openExportDialog());
        btnExit.setOnAction(e -> System.exit(0));
    }

    private void openProductManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/product-form.fxml"));
            Parent root = loader.load();

            ProductController controller = loader.getController();
            controller.setInventoryService(inventoryService);

            Stage stage = new Stage();
            stage.setTitle("Gestion des Produits");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la fenêtre de gestion des produits.");
        }
    }

    private void openInventoryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/inventory-view.fxml"));
            Parent root = loader.load();

            InventoryController controller = loader.getController();
            controller.setInventoryService(inventoryService);

            Stage stage = new Stage();
            stage.setTitle("Vue de l'Inventaire");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showError("Erreur", "Impossible d'ouvrir la fenêtre d'inventaire.");
        }
    }

    // ✅ Méthode openExportDialog() corrigée
    private void openExportDialog() {
        try {
            Inventory inventory = inventoryService.getInventory();

            if (inventory.getProducts().isEmpty()) {
                AlertUtils.showInfo("Export", "Aucun produit à exporter.");
                return;
            }

            ExportService exportService = new ExportService();
            String filePath = "inventory_report.pdf";

            exportService.exportToPDF(inventory, filePath);

            AlertUtils.showInfo(
                    "Export réussi",
                    "Rapport exporté avec succès.\nFichier : " + filePath
            );

        } catch (Exception e) {
            AlertUtils.showError("Erreur d'export", e.getMessage());
        }
    }
}
