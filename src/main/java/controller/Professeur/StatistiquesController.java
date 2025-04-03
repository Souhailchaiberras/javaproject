package controller.Professeur;

import dao.ProfesseurImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.User;
import util.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatistiquesController {

    @FXML
    private Label studentCountLabel;

    @FXML
    private Label welcomeText;

    @FXML
    private Label modulesAssignedLabel;

    @FXML
    private Label lastLoginLabel;

    private ProfesseurImp professeurImp = new ProfesseurImp();

    User ue = Session.getCurrentUser();

    @FXML
    public void initialize() {
        // Vérification des éléments FXML
        if (welcomeText == null) {
            System.err.println("Le champ 'welcomeText' n'est pas initialisé. Vérifiez votre fichier FXML.");
        }
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            // Exemple de récupération des données simulées depuis ProfesseurImp
            int studentCount = professeurImp.getNombreEtudiants(ue.getId());
            int modulesCount = professeurImp.getNombreModules(ue.getId());
            String lastLogin = getLastLoginTime();

            // Mise à jour des labels dans l'interface
            studentCountLabel.setText(String.valueOf(studentCount));
            modulesAssignedLabel.setText(String.valueOf(modulesCount));
            lastLoginLabel.setText(lastLogin);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getLastLoginTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return now.format(formatter);
    }

    @FXML
    protected void onStudentManagementButtonClick() {
        loadFXML("/vues/ProfesseurInterface/ProfesseurPageInit.fxml");
    }

    @FXML
    protected void onCoursesButtonClick() {
        loadFXML("/vues/ProfesseurInterface/AffichageModules.fxml");
    }

    @FXML
    protected void onReportsButtonClick() {
        loadFXML("/vues/ProfesseurInterface/ListesEtudiants.fxml");
    }

    @FXML
    protected void onSettingsButtonClick() {
        try {
            // Charge le fichier FXML correspondant
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));

            // Remplace BorderPane par HBox
            HBox root = loader.load();

            // Obtenir la scène et la stage actuelles
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root);

            // Appliquer la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane root = loader.load();
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlPath + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
