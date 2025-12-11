module org.example.inventorymanagerda {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.inventorymanagerda to javafx.fxml;
    exports org.example.inventorymanagerda;
    exports model;
    opens model to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
}