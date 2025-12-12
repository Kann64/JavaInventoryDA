package controller;

import model.Inventory;
import model.Product;
import model.CsvManager;

import java.util.Comparator;

/**
 * Classe de démonstration pour le Milestone 2
 * Démontre:
 * 1. Classes de gestion (Product, Inventory, CsvManager)
 * 2. Opérations de fichiers CSV
 * 3. Utilisation des Streams
 * 4. Threads et synchronisation
 */
public class DemoMilestone2 {

    public static void main(String[] args) {
        // Initialisation
        Inventory inventory = new Inventory();
        CsvManager csvManager = new CsvManager();
        InventoryController controller = new InventoryController(inventory, csvManager);

        System.out.println("=== DEMONSTRATION MILESTONE 2 ===\n");

        // 1. Ajout de produits
        System.out.println("1. Ajout de produits:");
        controller.addProduct(new Product(1, "Laptop", 999.99, 10));
        controller.addProduct(new Product(2, "Mouse", 29.99, 50));
        controller.addProduct(new Product(3, "Keyboard", 79.99, 30));
        controller.addProduct(new Product(4, "Monitor", 299.99, 15));
        System.out.println("✓ Produits ajoutés\n");

        // 2. Affichage avec Streams
        System.out.println("2. Affichage de l'inventaire (avec Streams):");
        controller.showInventory();
        System.out.println();

        // 3. Export CSV synchrone
        System.out.println("3. Export CSV synchrone:");
        String csvPath = "inventory.csv";
        controller.saveCsv(csvPath);
        System.out.println("✓ Export terminé: " + csvPath + "\n");

        // 4. Import CSV asynchrone (Thread)
        System.out.println("4. Import CSV asynchrone (avec Thread):");
        inventory.clear();
        controller.loadCsvAsync(csvPath);

        // Attendre que l'import soit terminé
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for CSV import: " + e.getMessage());
        }

        controller.showInventory();
        System.out.println();

        // 5. Opérations avec Streams avancées
        System.out.println("5. Opérations avec Streams:");

        System.out.println("   - Produits avec prix > 50:");
        inventory.stream()
                 .filter(p -> p.getPrice() > 50)
                 .forEach(p -> System.out.println("     " + p));

        System.out.println("\n   - Total des quantités:");
        int totalQty = inventory.stream()
                                .mapToInt(Product::getQuantity)
                                .sum();
        System.out.println("     " + totalQty + " unités");

        System.out.println("\n   - Produit le plus cher:");
        inventory.stream()
                 .max(Comparator.comparingDouble(Product::getPrice))
                 .ifPresent(p -> System.out.println("     " + p));

        System.out.println();

        // 6. Export CSV asynchrone (Thread)
        System.out.println("6. Export CSV asynchrone (avec Thread):");
        String asyncCsvPath = "inventory_async.csv";
        controller.saveCsvAsync(asyncCsvPath);
        System.out.println("✓ Export lancé en arrière-plan: " + asyncCsvPath + "\n");

        // Arrêt propre
        System.out.println("7. Arrêt de l'ExecutorService:");
        controller.shutdown();
        System.out.println("✓ ExecutorService arrêté proprement\n");

        System.out.println("=== FIN DE LA DEMONSTRATION ===");
        System.out.println("\nMilestone 2 ACCOMPLI:");
        System.out.println("✓ Classes de gestion développées (Product, Inventory, CsvManager)");
        System.out.println("✓ Opérations de fichiers CSV implémentées");
        System.out.println("✓ Utilisation des Streams (filter, map, forEach, sorted, sum, max)");
        System.out.println("✓ Threads et synchronisation (ExecutorService, ConcurrentHashMap)");
    }
}

