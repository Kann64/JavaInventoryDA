package Services;

import Models.*;
import java.util.List;

public class InventoryService {
    private Inventory inventory;

    public InventoryService() {
        this.inventory = new Inventory();
    }

    // ✅ MÉTHODE MANQUANTE (OBLIGATOIRE POUR L’EXPORT)
    public Inventory getInventory() {
        return inventory;
    }

    // Gestion des produits
    public List<Product> getAllProducts() {
        return inventory.getProducts();
    }

    public void addProduct(Product product) {
        inventory.addProduct(product);
    }

    public void updateProduct(Product product) {
        inventory.updateProduct(product);
    }

    public void deleteProduct(int productId) {
        inventory.deleteProduct(productId);
    }

    public Product getProductById(int id) {
        return inventory.getProductById(id);
    }

    // Gestion des catégories
    public List<Category> getAllCategories() {
        return inventory.getCategories();
    }

    public void addCategory(Category category) {
        inventory.addCategory(category);
    }

    // Gestion des fournisseurs
    public List<Supplier> getAllSuppliers() {
        return inventory.getSuppliers();
    }

    public void addSupplier(Supplier supplier) {
        inventory.addSupplier(supplier);
    }

    // Rapports et analyses
    public List<Product> getLowStockProducts() {
        return inventory.getLowStockProducts();
    }

    public double getTotalInventoryValue() {
        return inventory.getTotalInventoryValue();
    }

    public int getTotalProductsCount() {
        return inventory.getProducts().size();
    }
}
