package controller;

import model.Inventory;
import model.Product;
import model.CsvManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Contrôleur pour gérer l'inventaire
 * Utilise des Threads pour les opérations asynchrones
 */
public class InventoryController {
    private final Inventory inventory;
    private final CsvManager csvManager;
    private final ExecutorService executorService;

    public InventoryController(Inventory inventory, CsvManager csvManager) {
        this.inventory = inventory;
        this.csvManager = csvManager;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    // Ajouter un produit
    public void addProduct(Product product) {
        inventory.addProduct(product);
    }

    // Afficher l'inventaire avec Streams
    public void showInventory() {
        inventory.stream()
                .sorted(Comparator.comparingInt(Product::getId))
                .forEach(System.out::println);
    }

    // Sauvegarder en CSV (synchrone)
    public void saveCsv(String filePath) {
        try {
            csvManager.exportCsv(filePath, inventory);
            System.out.println("CSV sauvegardé: " + filePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde CSV: " + e.getMessage());
        }
    }

    // Charger depuis CSV (asynchrone avec Thread)
    public void loadCsvAsync(String filePath) {
        executorService.submit(() -> {
            try {
                System.out.println("[Thread-" + Thread.currentThread().getName() + "] Début du chargement CSV...");
                csvManager.importCsv(filePath, inventory);
                System.out.println("[Thread-" + Thread.currentThread().getName() + "] CSV chargé: " + filePath);
            } catch (IOException e) {
                System.err.println("[Thread-" + Thread.currentThread().getName() + "] Erreur lors du chargement CSV: " + e.getMessage());
            }
        });
    }

    // Sauvegarder en CSV (asynchrone avec Thread)
    public void saveCsvAsync(String filePath) {
        executorService.submit(() -> {
            try {
                System.out.println("[Thread-" + Thread.currentThread().getName() + "] Début de la sauvegarde CSV...");
                csvManager.exportCsv(filePath, inventory);
                System.out.println("[Thread-" + Thread.currentThread().getName() + "] CSV sauvegardé: " + filePath);
            } catch (IOException e) {
                System.err.println("[Thread-" + Thread.currentThread().getName() + "] Erreur lors de la sauvegarde CSV: " + e.getMessage());
            }
        });
    }

    // Arrêt propre de l'ExecutorService
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

