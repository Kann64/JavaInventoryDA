package gui;

import controller.InventoryController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import model.CsvManager;
import model.Inventory;
import model.Product;

import java.io.File;
import java.util.Optional;

public class InventoryGUIController {

    @FXML private TableView<Product> tableView;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Label statusLabel;
    @FXML private TextField searchField;

    private Inventory inventory;
    private InventoryController controller;
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        inventory = new Inventory();
        CsvManager csvManager = new CsvManager();
        controller = new InventoryController(inventory, csvManager);

        tableView.setItems(productList);

        // Add listener to update form when a product is selected
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );

        // Initial data load for demonstration
        loadInitialData();
        refreshTable();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applySearchFilter(newVal));

    }

    private void loadInitialData() {
        controller.addProduct(new Product(1, "Laptop", 1200.00, 10));
        controller.addProduct(new Product(2, "Mouse", 25.50, 50));
        controller.addProduct(new Product(3, "Keyboard", 75.00, 30));
    }

    private void refreshTable() {
        productList.setAll(inventory.getProducts());
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            idField.setText(String.valueOf(product.getId()));
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            quantityField.setText(String.valueOf(product.getQuantity()));
        } else {
            clearForm();
        }
    }

    @FXML
    private void handleAdd() {
        try {
            Product p = getProductFromForm();
            if (inventory.findById(p.getId()).isPresent()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product with ID " + p.getId() + " already exists.");
                return;
            }
            controller.addProduct(p);
            refreshTable();
            clearForm();
            updateStatus("Product added successfully.", true);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for ID, Price, and Quantity.");
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            Product p = getProductFromForm();
            if (inventory.findById(p.getId()).isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product with ID " + p.getId() + " not found.");
                return;
            }
            inventory.updateProduct(p);
            refreshTable();
            clearForm();
            updateStatus("Product updated successfully.", true);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for ID, Price, and Quantity.");
        }
    }

    @FXML
    private void handleDelete() {
        Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a product to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedProduct.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                inventory.removeProduct(selectedProduct.getId());
                refreshTable();
                clearForm();
                updateStatus("Product deleted successfully.", true);
            }
        });
    }

    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(tableView.getScene().getWindow());

        if (file != null) {
            inventory.clear();
            controller.loadCsvAsync(file.getAbsolutePath());
            // Give time for the async operation to complete
            // A more robust solution would use Task for feedback
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simple delay
                    Platform.runLater(() -> {
                        refreshTable();
                        updateStatus("Loaded data from " + file.getName(), true);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Platform.runLater(() -> updateStatus("Error during load delay.", false));
                }
            }).start();
        }
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

        if (file != null) {
            controller.saveCsvAsync(file.getAbsolutePath());
            updateStatus("Saving data to " + file.getName() + "...", true);
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private Product getProductFromForm() throws NumberFormatException {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        return new Product(id, name, price, quantity);
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateStatus(String message, boolean isSuccess) {
        statusLabel.setText("Status: " + message);
        statusLabel.setStyle(isSuccess ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
    private void applySearchFilter(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            productList.setAll(inventory.getProducts());
            return;
        }

        String lower = keyword.toLowerCase();

        productList.setAll(
                inventory.getProducts().stream()
                        .filter(p ->
                                String.valueOf(p.getId()).contains(lower) ||
                                        p.getName().toLowerCase().contains(lower)
                        )
                        .toList()
        );
    }

}

