package controller.Professeur;

import javafx.scene.layout.HBox;
import model.Module;
import dao.ProfesseurImp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Etudiant;
import model.User;
import util.Session;

import java.io.IOException;
import java.util.List;

public class AfficheModules {

    @FXML
    private Label welcomeText;

    @FXML
    private TableView<Module> modulesTable;

    @FXML
    private TableColumn<Module, Integer> moduleIdColumn;
    @FXML
    private TableColumn<Module, String> moduleNameColumn;

    @FXML
    private TableColumn<Module, String> moduleCodeColumn;

    private ProfesseurImp professeurImp = new ProfesseurImp();
    User ue = Session.getCurrentUser();

    @FXML
    public void initialize() {
        // Initialisation des colonnes du tableau des étudiants
        moduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        moduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("NomModule"));  // Assurez-vous que le getter s'appelle getNom()
        moduleCodeColumn.setCellValueFactory(new PropertyValueFactory<>("CodeModule"));  // Assurez-vous que le getter s'appelle getPrenom()

        loadModules();
    }

    private void loadModules() {
        List<Module> modules = professeurImp.getModuleAssigner(ue.getId());
        if (modules != null) {
            modulesTable.getItems().setAll(modules);
        } else {
            System.out.println("Aucun module trouvé pour ce professeur.");
        }
    }





    @FXML
    private void onStudentManagementButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ProfesseurInterface/ProfesseurPageInit.fxml"));
            BorderPane root = loader.load();
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCoursesButtonClick() {
        System.out.println("Gestion des modules button clicked");
    }

    @FXML
    private void onReportsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ProfesseurInterface/ListesEtudiants.fxml"));
            BorderPane root = loader.load();
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void onLogoutButtonClick() {
        System.out.println("Logout button clicked");
    }

    @FXML
    private void onRefreshButtonClick() {
        System.out.println("Refresh button clicked");
        loadModules();
    }
}
