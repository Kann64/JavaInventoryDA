module org.example.inventorymanagerda {
    requires javafx.controls;
    requires javafx.fxml;

    exports model;
    exports controller;
    exports gui;
    opens gui to javafx.fxml;
}