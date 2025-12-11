package model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class Inventory {
    private final ConcurrentMap<Integer, Product> products = new ConcurrentHashMap<>();

    // Ajout d'un produit
    public void addProduct(Product p) {
        products.put(p.getId(), p);
    }

    // Suppression d'un produit par ID
    public void removeProduct(int id) {
        products.remove(id);
    }

    // Rechercher un produit
    public Optional<Product> findById(int id) {
        return Optional.ofNullable(products.get(id));
    }

    // Mise à jour d'un produit
    public void updateProduct(Product p) {
        products.replace(p.getId(), p);
    }

    // Liste complète
    public Collection<Product> getProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    // Stream pour opérations avancées
    public Stream<Product> stream() {
        return products.values().stream();
    }

    // Calcul de la valeur totale de l'inventaire (utilisation de Streams)
    public double getTotalInventoryValue() {
        return stream().mapToDouble(Product::getTotalValue).sum();
    }

    // Effacer l'inventaire
    public void clear() {
        products.clear();
    }
}

