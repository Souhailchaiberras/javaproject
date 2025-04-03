package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox; // Import HBox instead of AnchorPane
import javafx.stage.Stage;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));
            HBox root = loader.load(); // Charger le fichier FXML avec HBox
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'exception pour faciliter le débogage
        }
    }

    public static void main(String[] args) {
        launch(args); // Lancer l'application JavaFX
    }
}