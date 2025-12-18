package Models;

import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int minStockLevel;
    private Category category;
    private Supplier supplier;
    private String sku; // Stock Keeping Unit
    private LocalDate expirationDate;
    private String location;

    public Product() {
        this.expirationDate = LocalDate.now().plusYears(1);
    }

    public Product(int id, String name, String description, double price, int quantity,
                   int minStockLevel, Category category, Supplier supplier, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.category = category;
        this.supplier = supplier;
        this.sku = sku;
        this.expirationDate = LocalDate.now().plusYears(1);
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // Méthodes utilitaires
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }

    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return name + " (SKU: " + sku + ", Stock: " + quantity + ")";
    }
}