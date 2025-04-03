package controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import model.User;
import model.Secraitaires;
import dao.SecretaireImp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

public class AjouteSecretaireController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label welcomeText;

    private SecretaireImp secretaireDao;

    // Constructor to initialize SecretaireImp DAO
    public AjouteSecretaireController() {
        secretaireDao = new SecretaireImp();
    }

    // Method called when the "Ajouter une Secrétaire" button is clicked
    @FXML
    public void onAddButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validation: Ensure email and password are not empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Email and password cannot be empty!", AlertType.ERROR);
            return;
        }

        // Validate email format (basic validation)
        if (!email.contains("@")) {
            showAlert("Error", "Invalid email format!", AlertType.ERROR);
            return;
        }

        // Create new User and Secretary
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(2);  // Assuming 2 represents the role of "Secrétaire"

        // Insert user into the database
        boolean isUserAdded = secretaireDao.addUser(user);

        if (isUserAdded) {
            // Add user as secrétaire
            int userId = user.getId();
            Secraitaires secretaire = new Secraitaires(userId);

            boolean isSecretaireAdded = secretaireDao.addSecretaire(secretaire);

            if (isSecretaireAdded) {
                showAlert("Success", "Secrétaire added successfully!", AlertType.INFORMATION);
                navigateToGestionSecretaire();  // Navigate after successful save
            } else {
                showAlert("Error", "Failed to add Secrétaire.", AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Failed to add user.", AlertType.ERROR);
        }
    }

    // Helper method to display alerts
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to generate a random password when the email is changed
    @FXML
    public void initialize() {
        emailField.setTextFormatter(new TextFormatter<String>(change -> {
            String email = change.getControlNewText();

            // If the email is not empty, generate a password
            if (!email.isEmpty()) {
                String generatedPassword = generateRandomPassword();
                passwordField.setText(generatedPassword);  // Set the generated password in the password field
            }
            return change;
        }));
    }

    // Utility method to generate a random password
    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {  // Password length of 8 characters
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    // Method to handle the "Cancel" button click
    @FXML
    public void onCancelButtonClick(ActionEvent event) {
        navigateToGestionSecretaire();  // Navigate to /vues/ADMIN/gestiondessecretaire.fxml
    }

    // Method to navigate to /vues/ADMIN/gestiondessecretaire.fxml
    private void navigateToGestionSecretaire() {
        try {
            // Load the target FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ADMIN/gestiondessecretaire.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the page.", AlertType.ERROR);
        }
    }

    @FXML
    public void onStatistiqueButtonClick(ActionEvent actionEvent) {
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
    public void onGestiondesModuleButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
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
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void onEmailFieldChanged(KeyEvent keyEvent) {
    }

    public void onEmailFieldChanged(javafx.scene.input.KeyEvent keyEvent) {
    }
}
