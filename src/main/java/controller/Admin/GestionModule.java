package controller.Admin;

import dao.EtudiantImp;
import dao.ModuleImp;
import model.Module;
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
import model.Etudiant;
import model.User;
import util.DatabaseConnection;
import util.Session;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestionModule {


    @FXML
    private Label welcomeText;
    @FXML
    private VBox formContainer;
    @FXML
    private TextField IdModule;
    @FXML
    private TextField nomField;
    @FXML
    private TextField CodeM;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;



    @FXML
    private TableView<Module> ModuleTable;
    @FXML
    private TableColumn<Module, Integer> idmodule;
    @FXML
    private TableColumn<Module, String> nomColumn;
    @FXML
    private TableColumn<Module, String> Code;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;

    private ModuleImp moduleImp = new ModuleImp();


    private Module selectedmoduleForEdit ;
    private User ue = Session.getCurrentUser();





    @FXML
    public void initialize() {
        // Set up TableView columns
        idmodule.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NomModule"));
        Code.setCellValueFactory(new PropertyValueFactory<>("CodeModule"));

        loadModules();
    }

    private void loadModules() {
        List<Module> modules = moduleImp.getAll();
        if (modules != null) {
            ModuleTable.getItems().setAll(modules);
        } else {
            System.out.println("Aucun module trouvé pour ce professeur.");
        }
    }


    @FXML
    public void onDeleteButtonClick() {
        Module selectedmodule = ModuleTable.getSelectionModel().getSelectedItem();
        if (selectedmodule != null) {
            moduleImp.deleteModule(selectedmodule.getId());
            showInfo("Professeur supprimer", "Le professeur a etait supprimer avec succes.");
        } else {
            showWarning("Il y'a aucune selection", "Veuillez selectionner un professeur.");
        }

        loadModules();
    }
    private BorderPane root;


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void onModifyButtonClick() {
        selectedmoduleForEdit = ModuleTable.getSelectionModel().getSelectedItem();
        if (selectedmoduleForEdit != null) {
            // Show form and populate fields with selected student's data
            formContainer.setVisible(true);
            IdModule.setText(String.valueOf(selectedmoduleForEdit.getId()));
            nomField.setText(selectedmoduleForEdit.getNomModule());
            CodeM.setText(selectedmoduleForEdit.getCodeModule());
        } else {
            showWarning("No Selection", "Please select a student to modify.");
        }
    }

    @FXML
    public void onSaveButtonClick() {
        if (selectedmoduleForEdit != null) {
            selectedmoduleForEdit.setId(Integer.parseInt(IdModule.getText())); // Ici j'ai juste caste de String a Integer
            selectedmoduleForEdit.setNomModule(nomField.getText());
            selectedmoduleForEdit.setCodeModule(CodeM.getText());

            moduleImp.updateModule(selectedmoduleForEdit,ue);
            loadModules(); // Refresh the table with updated data
            formContainer.setVisible(false); // Hide form
            showInfo("Success", "Student details updated successfully.");
        }
    }

    @FXML
    public void onCancelButtonClick() {
        formContainer.setVisible(false);
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
    public void onAjouteEtudiant(ActionEvent actionEvent) {

        loadScene("/vues/ADMIN/GestionModule/Adminmodule.fxml");
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
    private void onGestiondesModulesButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml");
    }


    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            BorderPane root = loader.load();
            Stage stage = (Stage) ModuleTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

