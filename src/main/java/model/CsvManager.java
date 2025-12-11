package model;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class CsvManager {

    // Importer depuis CSV avec Streams
    public void importCsv(String filePath, Inventory inventory) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.skip(1) // Skip header if present
                 .map(String::trim)
                 .filter(line -> !line.isEmpty())
                 .map(this::parseProduct)
                 .forEach(inventory::addProduct);
        }
    }

    // Exporter vers CSV avec Streams
    public void exportCsv(String filePath, Inventory inventory) throws IOException {
        Path path = Paths.get(filePath);
        StringBuilder csv = new StringBuilder("id,name,price,quantity\n");

        inventory.stream()
                 .sorted((p1, p2) -> Integer.compare(p1.getId(), p2.getId()))
                 .forEach(p -> csv.append(String.format("%d,%s,%.2f,%d%n",
                         p.getId(), p.getName(), p.getPrice(), p.getQuantity())));

        Files.writeString(path, csv.toString());
    }

    // Parser une ligne CSV en Product
    private Product parseProduct(String line) {
        String[] cols = line.split(",");
        if (cols.length != 4) {
            throw new IllegalArgumentException("Format CSV invalide : " + line);
        }
        return new Product(
            Integer.parseInt(cols[0].trim()),
            cols[1].trim(),
            Double.parseDouble(cols[2].trim()),
            Integer.parseInt(cols[3].trim())
        );
    }
}

