package controller.Admin;

import dao.ProfesseurImp;
import dao.UserImp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Professeur;
import model.User;

import java.io.IOException;


public class ajouterProf {




    @FXML
    private TextField prenomField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField specialiteField;

    ProfesseurImp prof = new ProfesseurImp();
    UserImp user = new UserImp();
    private Professeur pf = new Professeur();
    private User us = new User();


    @FXML
    private void onStatistiqueButtonClick(ActionEvent actionEvent) {
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
        loadScene("/vues/ADMIN/GestionEtudiant.fxml");
    }

    @FXML
    private void onGestiondesModulesButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
    }
    @FXML
    public void onSubmitForm() {
       String prenom = prenomField.getText();
       String nom = nomField.getText();
       String email = emailField.getText();
       String specialite = specialiteField.getText();
       pf.setId(null);
       pf.setPrenom(prenom);
       pf.setNom(nom);
       pf.setSpecialite(specialite);
       us.setEmail(email);
       prof.AjouterProf(pf, us);
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
            Parent root = loader.load();
            Stage stage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
