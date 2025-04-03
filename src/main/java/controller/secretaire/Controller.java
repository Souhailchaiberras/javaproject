package controller.secretaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import model.User;
import util.DatabaseConnection;
import util.Session;
import dao.EtudiantImp;
import dao.InscriptionImp;
import dao.GererEtudiantIMP;

import java.io.IOException;
import java.sql.*;

public class Controller {

    @FXML
    private Label welcomeText;

    @FXML
    private TextField matriculeField, nomField, prenomField, promotionField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private ComboBox<String> moduleComboBox;

    private Connection connection;
    private EtudiantImp etudiantDAO;
    private InscriptionImp inscriptionDAO;
    private GererEtudiantIMP gererEtudiantDAO;

    public void initialize() {
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            if (connection == null) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to establish a database connection.");
                return;
            }

            etudiantDAO = new EtudiantImp(connection);
            inscriptionDAO = new InscriptionImp(connection);
            gererEtudiantDAO = new GererEtudiantIMP(connection);

            populateModules();
        } catch (Exception e) {
            handleException(e, "An error occurred while initializing the controller.");
        }
    }

    private void populateModules() {
        String sql = "SELECT nommodule FROM Modules";

        new Thread(() -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                ObservableList<String> moduleNames = FXCollections.observableArrayList();
                while (rs.next()) {
                    String moduleName = rs.getString("NomModule");
                    if (moduleName != null && !moduleName.trim().isEmpty()) {
                        moduleNames.add(moduleName);
                    }
                }

                Platform.runLater(() -> moduleComboBox.setItems(moduleNames));

            } catch (SQLException e) {
                handleException(e, "Error fetching modules from the database.");
            }
        }).start();
    }

    @FXML
    private void onSubmitForm() {
        String matricule = matriculeField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String promotion = promotionField.getText();
        String dateNaissance = dateNaissancePicker.getValue() != null ? dateNaissancePicker.getValue().toString() : null;
        String selectedModule = moduleComboBox.getValue();

        if (!validateFields(matricule, nom, prenom, promotion, dateNaissance, selectedModule)) {
            return;
        }

        try {
            connection.setAutoCommit(false);

            if (etudiantDAO.isStudentExists(matricule)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Student with this matricule already exists.");
                return;
            }

            // Register the student
            int idEtudiant = etudiantDAO.insertEtudiant(matricule, nom, prenom, promotion, dateNaissance);

            // Link the student to the current logged-in secretary
            User currentUser = Session.getCurrentUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Session Error", "No user is logged in.");
                return;
            }

            // Insert the student record into GererEtudiant
            gererEtudiantDAO.insertGererEtudiant(idEtudiant);

            // Register the student to the selected module
            inscriptionDAO.insertInscription(idEtudiant, selectedModule);

            connection.commit();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student registered and linked to secretary successfully!");
        } catch (SQLException e) {
            rollbackTransaction();
            handleException(e, "Failed to register student.");
        }
    }

    private boolean validateFields(String matricule, String nom, String prenom, String promotion, String dateNaissance, String selectedModule) {
        if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty() || promotion.isEmpty() || dateNaissance == null || selectedModule == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    private void rollbackTransaction() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException rollbackEx) {
            rollbackEx.printStackTrace();
        }
    }

    private void handleException(Exception e, String message) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onStatistiqueButtonClick() {
        loadScene("/vues/secretaire/SecretairePageInit.fxml");
    }

    @FXML
    private void onAjouterButtonClick() {
        loadScene("/vues/secretaire/addstudent.fxml");
    }

    @FXML
    private void onDisplaylisteofstudentButtonclick() {
        loadScene("/vues/secretaire/studentlist.fxml");
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

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane root = loader.load();
            Stage stage = (Stage) moduleComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the scene.");
        }
    }
}
