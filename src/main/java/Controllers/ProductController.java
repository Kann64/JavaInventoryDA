package Controllers;

import Models.*;
import Services.InventoryService;
import Utils.AlertUtils;
import Utils.ValidationUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class ProductController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtMinStock;
    @FXML private ComboBox<Category> comboCategory;
    @FXML private ComboBox<Supplier> comboSupplier;
    @FXML private TextField txtSku;
    @FXML private DatePicker dateExpiration;
    @FXML private TextField txtLocation;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnNewCategory;
    @FXML private Button btnNewSupplier;

    private InventoryService inventoryService;
    private Product currentProduct;
    private boolean isEditMode = false;

    public void setInventoryService(InventoryService service) {
        this.inventoryService = service;
        loadComboBoxData();
    }

    @FXML
    private void initialize() {
        setupValidations();
        setupButtonActions();
        disableIdField();
    }

    private void setupValidations() {
        // Validation numérique pour les champs prix/quantité
        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!ValidationUtils.isValidPrice(newValue)) {
                txtPrice.setStyle("-fx-border-color: red;");
            } else {
                txtPrice.setStyle("");
            }
        });

        txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!ValidationUtils.isValidQuantity(newValue)) {
                txtQuantity.setStyle("-fx-border-color: red;");
            } else {
                txtQuantity.setStyle("");
            }
        });
    }

    private void setupButtonActions() {
        btnSave.setOnAction(e -> saveProduct());
        btnCancel.setOnAction(e -> closeWindow());
        btnNewCategory.setOnAction(e -> createNewCategory());
        btnNewSupplier.setOnAction(e -> createNewSupplier());
    }

    private void disableIdField() {
        txtId.setDisable(true);
        txtId.setStyle("-fx-opacity: 0.7;");
    }

    private void loadComboBoxData() {
        if (inventoryService != null) {
            comboCategory.setItems(FXCollections.observableArrayList(
                    inventoryService.getAllCategories()));
            comboSupplier.setItems(FXCollections.observableArrayList(
                    inventoryService.getAllSuppliers()));
        }
    }

    public void setProductForEdit(Product product) {
        if (product != null) {
            this.currentProduct = product;
            this.isEditMode = true;
            populateFields(product);
        }
    }

    private void populateFields(Product product) {
        txtId.setText(String.valueOf(product.getId()));
        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription());
        txtPrice.setText(String.valueOf(product.getPrice()));
        txtQuantity.setText(String.valueOf(product.getQuantity()));
        txtMinStock.setText(String.valueOf(product.getMinStockLevel()));
        txtSku.setText(product.getSku());
        txtLocation.setText(product.getLocation());

        if (product.getExpirationDate() != null) {
            dateExpiration.setValue(product.getExpirationDate());
        }

        // Sélectionner la catégorie
        if (product.getCategory() != null) {
            comboCategory.getSelectionModel().select(product.getCategory());
        }

        // Sélectionner le fournisseur
        if (product.getSupplier() != null) {
            comboSupplier.getSelectionModel().select(product.getSupplier());
        }
    }

    private void saveProduct() {
        if (!validateInputs()) {
            return;
        }

        try {
            Product product = createProductFromFields();

            if (isEditMode) {
                inventoryService.updateProduct(product);
                AlertUtils.showInfo("Succès", "Produit mis à jour avec succès!");
            } else {
                inventoryService.addProduct(product);
                AlertUtils.showInfo("Succès", "Produit ajouté avec succès!");
            }

            clearFields();
            if (isEditMode) {
                closeWindow();
            }

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (!ValidationUtils.isNotEmpty(txtName.getText())) {
            errors.append("Le nom est requis.\n");
        }

        if (!ValidationUtils.isValidPrice(txtPrice.getText())) {
            errors.append("Le prix doit être un nombre valide (≥ 0).\n");
        }

        if (!ValidationUtils.isValidQuantity(txtQuantity.getText())) {
            errors.append("La quantité doit être un nombre entier valide (≥ 0).\n");
        }

        if (!ValidationUtils.isValidStockLevel(txtMinStock.getText())) {
            errors.append("Le stock minimum doit être un nombre valide (≥ 0).\n");
        }

        if (!ValidationUtils.isNotEmpty(txtSku.getText())) {
            errors.append("Le SKU est requis.\n");
        }

        if (comboCategory.getSelectionModel().getSelectedItem() == null) {
            errors.append("Veuillez sélectionner une catégorie.\n");
        }

        if (errors.length() > 0) {
            AlertUtils.showError("Validation", errors.toString());
            return false;
        }

        return true;
    }

    private Product createProductFromFields() {
        Product product = new Product();

        if (isEditMode && currentProduct != null) {
            product.setId(currentProduct.getId());
        }

        product.setName(txtName.getText());
        product.setDescription(txtDescription.getText());
        product.setPrice(Double.parseDouble(txtPrice.getText()));
        product.setQuantity(Integer.parseInt(txtQuantity.getText()));
        product.setMinStockLevel(Integer.parseInt(txtMinStock.getText()));
        product.setCategory(comboCategory.getSelectionModel().getSelectedItem());
        product.setSupplier(comboSupplier.getSelectionModel().getSelectedItem());
        product.setSku(txtSku.getText());
        product.setLocation(txtLocation.getText());

        if (dateExpiration.getValue() != null) {
            product.setExpirationDate(dateExpiration.getValue());
        }

        return product;
    }

    private void createNewCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouvelle Catégorie");
        dialog.setHeaderText("Créer une nouvelle catégorie");
        dialog.setContentText("Nom de la catégorie:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(categoryName -> {
            if (ValidationUtils.isNotEmpty(categoryName)) {
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                inventoryService.addCategory(newCategory);
                loadComboBoxData();
                comboCategory.getSelectionModel().select(newCategory);
                AlertUtils.showInfo("Succès", "Catégorie créée avec succès!");
            }
        });
    }

    private void createNewSupplier() {
        // Implémentation simplifiée - à étendre avec un formulaire complet
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau Fournisseur");
        dialog.setHeaderText("Créer un nouveau fournisseur");
        dialog.setContentText("Nom du fournisseur:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(supplierName -> {
            if (ValidationUtils.isNotEmpty(supplierName)) {
                Supplier newSupplier = new Supplier();
                newSupplier.setName(supplierName);
                inventoryService.addSupplier(newSupplier);
                loadComboBoxData();
                comboSupplier.getSelectionModel().select(newSupplier);
                AlertUtils.showInfo("Succès", "Fournisseur créé avec succès!");
            }
        });
    }

    private void clearFields() {
        if (!isEditMode) {
            txtId.clear();
            txtName.clear();
            txtDescription.clear();
            txtPrice.clear();
            txtQuantity.clear();
            txtMinStock.clear();
            txtSku.clear();
            txtLocation.clear();
            dateExpiration.setValue(null);
            comboCategory.getSelectionModel().clearSelection();
            comboSupplier.getSelectionModel().clearSelection();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}