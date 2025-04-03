package controller.Admin;

import dao.ModuleImp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Module;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import model.User;
import util.Session;

import java.io.IOException;

public class ajouterModule {

    @FXML
    private TextField nomModule;

    @FXML
    private TextField codeModule;

    @FXML
    private Button addModule;

    private ModuleImp moduleImp = new ModuleImp();
    private User ue = Session.getCurrentUser();
    private Module m = new Module();
    private DashboardController dashboardController = new DashboardController();

    @FXML
    private void onSubmitForm(ActionEvent event) {
        // Assurer que les champs sont remplis
        m.setNomModule(nomModule.getText());
        m.setCodeModule(codeModule.getText());

        if (nomModule.getText().isEmpty() || codeModule.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis!", Alert.AlertType.ERROR);
            return;
        }

        // Ajouter le module
        if (moduleImp.AjouterModule(m, ue)) {
            showAlert("Succès", "Module ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();  // Effacer les champs après l'ajout
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du module.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomModule.clear();
        codeModule.clear();
    }

    @FXML
    private void onStatistiqueButtonClick() {
        loadScene("/vues/ADMIN/AdminPageInit.fxml");
    }

    @FXML
    public void onGestiondesProfesseurButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionProfesseurs/gestionProf.fxml");
    }

    @FXML
    public void onGestiondesSecretaireButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/gestiondessecretaire.fxml");
    }

    @FXML
    public void onGestiondesEtudiantButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/etudiantmanagment.fxml");
    }

    @FXML
    private void onGestiondesModulesButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
    }

    @FXML
    private void onDesconnected(ActionEvent event) {
        try {
            // Assuming you're trying to load the login screen after logout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get stage from event
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane root = loader.load();
            Stage stage = (Stage) addModule.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
