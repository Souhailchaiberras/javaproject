package controller.Admin;

import dao.SecretaireImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public class SecretaireController {

    @FXML
    private TableView<User> secretaireTable;

    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private Button addButton, deleteButton, updateButton;
    @FXML
    private VBox updateForm; // Reference to the update form
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private ObservableList<User> secretaireList;
    private final SecretaireImp secretaireDAO = new SecretaireImp();
    private User selectedSecretary;


    private void loadSecretaires() {
        try {
            secretaireTable.getItems().clear();  // Clear existing data to avoid duplicates
            secretaireTable.getItems().addAll(secretaireDAO.getAllSecretaires());
        } catch (Exception e) {
            showAlert("Error", "Failed to load secretaires: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() throws SQLException {
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        secretaireList = FXCollections.observableArrayList(secretaireDAO.getAllSecretaires());
        secretaireTable.setItems(secretaireList);
        loadSecretaires();
        updateForm.setVisible(false);
    }

    @FXML
    private void onAddButtonClick() {
        loadScene("/vues/ADMIN/ajoutersecretaire.fxml");
        // ajoutersecretaire.fxml
    }

    @FXML
    private void onDeleteButtonClick() throws SQLException {
        User selected = secretaireTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean isDeleted = secretaireDAO.deleteSecretaire(selected.getId());

            if (isDeleted) {
                secretaireList.remove(selected);  // Remove from the TableView list
                showAlert("Success", "Secretary deleted successfully.");
            } else {
                showAlert("Error", "Failed to delete secretary.");
            }
        } else {
            showAlert("Error", "Please select a secretary to delete.");
        }
    }

    @FXML
    private void onUpdateButtonClick() {
        selectedSecretary = secretaireTable.getSelectionModel().getSelectedItem();
        if (selectedSecretary != null) {
            emailField.setText(selectedSecretary.getEmail());
            passwordField.setText(selectedSecretary.getPassword());
            secretaireTable.setVisible(false);
            updateForm.setVisible(true);
        } else {
            showAlert("Error", "Please select a secretary to update.");
        }
    }

    @FXML
    private void onSaveUpdateButtonClick() {
        if (selectedSecretary != null) {
            selectedSecretary.setEmail(emailField.getText());
            selectedSecretary.setPassword(passwordField.getText());

            try {
                boolean isUpdated = secretaireDAO.updateSecretaire(selectedSecretary);
                if (isUpdated) {
                    loadSecretaires(); // Reload the data instead of just refreshing
                    showAlert("Success", "Secretary updated successfully.");
                    secretaireTable.setVisible(true);
                    updateForm.setVisible(false);
                } else {
                    showAlert("Error", "Failed to update secretary.");
                }
            } catch (Exception e) {
                showAlert("Error", "Database error: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onCancelUpdateButtonClick() {
        secretaireTable.setVisible(true);
        updateForm.setVisible(false);
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
    private void onDisconnectButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the login screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onGestiondesModulesButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane root = loader.load();
            Stage stage = (Stage) secretaireTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the requested page.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onDesconnected(ActionEvent event) {
        try {
            // Assuming you're trying to load the login screen after logout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void onGestiondesModuleButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
    }
}
