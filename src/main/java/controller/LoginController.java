package controller;

import model.User;
import dao.UserDAO;
import util.TokenUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Email and password cannot be empty.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticateUser(email, password);

        if (user != null) {
            // Generate token
            String token = TokenUtil.generateToken(user);
            System.out.println("Generated Token: " + token);

            // Role-based redirection
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (user.getRole() == 1) {
                loadView("/vues/ADMIN/AdminPageInit.fxml", stage);
            } else if (user.getRole() == 2) {
                loadView("/vues/secretaire/SecretairePageInit.fxml", stage);
            } else if (user.getRole() == 3) {
                loadView("/vues/ProfesseurInterface/ProfesseurPageInit.fxml", stage);
            } else {
                showAlert("Invalid user role. Contact system administrator.");
            }
        } else {
            showAlert("Invalid email or password.");
        }
    }

    private void loadView(String fxmlFile, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading view: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
