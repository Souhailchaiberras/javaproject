module org.example.projetjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires itextpdf;
    requires layout;
    requires kernel;
    requires jakarta.mail;
    requires java.desktop;

    exports controller; // Add this line to export the controller package
    opens controller to javafx.fxml;
    exports main; // Exporter le package main
    opens main to javafx.fxml;
    opens model to javafx.base;

    opens controller.Professeur to javafx.fxml;
    opens controller.secretaire to javafx.fxml;
    opens controller.Admin to javafx.fxml;

// Ouvrir le package main à JavaFX pour permettre le chargement de fxml
}