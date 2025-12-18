package Controllers;

import Models.Product;
import Services.ExportService;
import Services.FileService;
import Services.InventoryService;
import Utils.AlertUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class InventoryController {

    @FXML private TableView<Product> tableView;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, Double> colTotalValue;
    @FXML private TableColumn<Product, String> colStatus;

    @FXML private Label lblTotalProducts;
    @FXML private Label lblTotalValue;
    @FXML private Label lblLowStockCount;

    @FXML private Button btnRefresh;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnExport;
    @FXML private Button btnClose;

    private InventoryService inventoryService;
    private ExportService exportService;
    private FileService fileService;

    public void setInventoryService(InventoryService service) {
        this.inventoryService = service;
        this.exportService = new ExportService();
        this.fileService = new FileService();
        loadInventoryData();
        updateStatistics();
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        setupButtonActions();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCategory() != null ?
                        cellData.getValue().getCategory().getName() : "N/A"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalValue.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTotalValue()));

        colStatus.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().isLowStock() ? "⚠ FAIBLE" : "OK"));

        // Formatage des cellules
        colPrice.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", price));
                }
            }
        });

        colTotalValue.setCellFactory(column -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", value));
                }
            }
        });

        colStatus.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equals("⚠ FAIBLE")) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });
    }

    private void setupButtonActions() {
        btnRefresh.setOnAction(e -> refreshData());
        btnEdit.setOnAction(e -> editSelectedProduct());
        btnDelete.setOnAction(e -> deleteSelectedProduct());
        btnExport.setOnAction(e -> exportData());
        btnClose.setOnAction(e -> closeWindow());

        // Double-click pour éditer
        tableView.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    editSelectedProduct();
                }
            });
            return row;
        });
    }

    private void loadInventoryData() {
        if (inventoryService != null) {
            tableView.setItems(FXCollections.observableArrayList(
                    inventoryService.getAllProducts()));
        }
    }

    private void updateStatistics() {
        if (inventoryService != null) {
            lblTotalProducts.setText(String.valueOf(inventoryService.getTotalProductsCount()));
            lblTotalValue.setText(String.format("%.2f €", inventoryService.getTotalInventoryValue()));
            lblLowStockCount.setText(String.valueOf(inventoryService.getLowStockProducts().size()));
        }
    }

    private void refreshData() {
        loadInventoryData();
        updateStatistics();
        AlertUtils.showInfo("Rafraîchissement", "Données actualisées avec succès.");
    }

    private void editSelectedProduct() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                // Ouvrir la fenêtre d'édition
                // Note: Implémenter l'ouverture de la fenêtre d'édition
                AlertUtils.showInfo("Édition", "Édition du produit: " + selectedProduct.getName());
                refreshData(); // Rafraîchir après édition
            } catch (Exception e) {
                AlertUtils.showError("Erreur", "Impossible d'éditer le produit: " + e.getMessage());
            }
        } else {
            AlertUtils.showWarning("Sélection", "Veuillez sélectionner un produit à éditer.");
        }
    }

    private void deleteSelectedProduct() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            boolean confirm = AlertUtils.showConfirmation(
                    "Confirmation",
                    "Êtes-vous sûr de vouloir supprimer le produit:\n" +
                            selectedProduct.getName() + " ?");

            if (confirm) {
                inventoryService.deleteProduct(selectedProduct.getId());
                refreshData();
                AlertUtils.showInfo("Suppression", "Produit supprimé avec succès.");
            }
        } else {
            AlertUtils.showWarning("Sélection", "Veuillez sélectionner un produit à supprimer.");
        }
    }

    private void exportData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter l'inventaire");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            try {
                String filePath = file.getAbsolutePath();

                if (filePath.endsWith(".csv")) {
                    fileService.exportToCSV(new Models.Inventory(), filePath);
                    AlertUtils.showInfo("Export CSV", "Inventaire exporté en CSV avec succès!");
                } else if (filePath.endsWith(".pdf")) {
                    exportService.exportToPDF(new Models.Inventory(), filePath);
                    AlertUtils.showInfo("Export PDF", "Inventaire exporté en PDF avec succès!");
                } else {
                    AlertUtils.showWarning("Format", "Format de fichier non supporté.");
                }
            } catch (Exception e) {
                AlertUtils.showError("Erreur Export", "Erreur lors de l'export: " + e.getMessage());
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}