package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Product> products;
    private List<Category> categories;
    private List<Supplier> suppliers;

    public Inventory() {
        this.products = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.suppliers = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Catégories par défaut
        categories.add(new Category(1, "Électronique", "Appareils électroniques"));
        categories.add(new Category(2, "Bureau", "Fournitures de bureau"));
        categories.add(new Category(3, "Nourriture", "Produits alimentaires"));

        // Fournisseurs par défaut
        suppliers.add(new Supplier(1, "TechCorp", "Jean Dupont", "0123456789", "contact@techcorp.com", "Paris"));
        suppliers.add(new Supplier(2, "OfficePlus", "Marie Martin", "0987654321", "info@officeplus.com", "Lyon"));

        // Produits par défaut
        products.add(new Product(1, "Ordinateur Portable", "PC portable haute performance", 999.99, 10, 2,
                categories.get(0), suppliers.get(0), "ELEC-001"));
        products.add(new Product(2, "Stylo", "Stylo bleu à bille", 1.99, 100, 20,
                categories.get(1), suppliers.get(1), "BUR-001"));
    }

    // Getters
    public List<Product> getProducts() { return new ArrayList<>(products); }
    public List<Category> getCategories() { return new ArrayList<>(categories); }
    public List<Supplier> getSuppliers() { return new ArrayList<>(suppliers); }

    // Méthodes de gestion des produits
    public void addProduct(Product product) {
        if (product.getId() == 0) {
            product.setId(generateProductId());
        }
        products.add(product);
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                break;
            }
        }
    }

    public void deleteProduct(int productId) {
        products.removeIf(p -> p.getId() == productId);
    }

    public Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Méthodes de gestion des catégories
    public void addCategory(Category category) {
        if (category.getId() == 0) {
            category.setId(generateCategoryId());
        }
        categories.add(category);
    }

    // Méthodes de gestion des fournisseurs
    public void addSupplier(Supplier supplier) {
        if (supplier.getId() == 0) {
            supplier.setId(generateSupplierId());
        }
        suppliers.add(supplier);
    }

    // Méthodes utilitaires
    private int generateProductId() {
        return products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
    }

    private int generateCategoryId() {
        return categories.stream().mapToInt(Category::getId).max().orElse(0) + 1;
    }

    private int generateSupplierId() {
        return suppliers.stream().mapToInt(Supplier::getId).max().orElse(0) + 1;
    }

    public List<Product> getLowStockProducts() {
        return products.stream()
                .filter(Product::isLowStock)
                .toList();
    }

    public double getTotalInventoryValue() {
        return products.stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
    }
}