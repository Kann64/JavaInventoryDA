# How to Run This Project

This project is a console-based inventory management application.

## ✅ How to Run

### Using IntelliJ IDEA (Recommended)
1.  Navigate to `src/main/java/controller/DemoMilestone2.java`
2.  Open the file.
3.  **Right-click** anywhere in the code editor.
4.  Select **"Run 'DemoMilestone2.main()'"** (or press **Shift+F10**).

The program will run in the IntelliJ console, demonstrating all the inventory management features.

### Using Maven (Alternative)
If you have Maven configured, you can run the application from the terminal:
```bash
mvn exec:java
```

## Project Structure
The project is now a pure console application. All JavaFX-related files and configurations have been removed to simplify the structure.

-   **`src/main/java/controller`**: Contains the main application logic (`DemoMilestone2`) and the inventory controller.
-   **`src/main/java/model`**: Contains the data model classes (`Product`, `Inventory`, `CsvManager`).
-   **`pom.xml`**: Contains the project dependencies and build configuration.

The application demonstrates all the requirements for Milestone 2, including class development, file operations, Stream usage, and threading.


---

## What DemoMilestone2 Does

When you run it, you'll see:
1. ✓ Product addition demonstration
2. ✓ Inventory display using Streams
3. ✓ Synchronous CSV export
4. ✓ Asynchronous CSV import (with Thread)
5. ✓ Advanced Stream operations (filter, sum, max)
6. ✓ Asynchronous CSV export (with Thread)
7. ✓ Proper ExecutorService shutdown

Output files created:
- `inventory.csv`
- `inventory_async.csv`

---

## To Make JavaFX GUI Work (Future Development)

You would need to create:
1. **HelloApplication.java** in `src/main/java/org/example/inventorymanagerda/`
2. **HelloController.java** in the same package
3. Then run: `mvn clean javafx:run`

---

## Summary

**YES, the project CAN be run!** ✓

Just run **DemoMilestone2** - it's a complete demonstration of all Milestone 2 requirements and doesn't need JavaFX.

The JavaFX configuration in `pom.xml` is for future GUI development, but the current working application is the console-based demo.

