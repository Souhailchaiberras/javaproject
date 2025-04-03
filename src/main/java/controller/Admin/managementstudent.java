package controller.Admin;

import dao.EtudiantImp;
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
import util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class managementstudent {
    private Etudiant selectedStudentForEdit;
    @FXML
    private Label welcomeText;
    @FXML
    private VBox formContainer;
    @FXML
    private TextField matriculeField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField promotionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;



    @FXML
    private TableView<Etudiant> studentTable;
    @FXML
    private TableColumn<Etudiant, String> matriculeColumn;
    @FXML
    private TableColumn<Etudiant, String> nomColumn;
    @FXML
    private TableColumn<Etudiant, String> prenomColumn;
    @FXML
    private TableColumn<Etudiant, String> promotionColumn;
    @FXML
    private TableColumn<Etudiant, Void> actionsColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button modifyButton;

    private ObservableList<Etudiant> studentList;
    private EtudiantImp etudiantDAO;

    // Initialize the connection from the DatabaseConnection singleton
    public void setConnection() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn != null) {
            this.etudiantDAO = new EtudiantImp(conn);  // Initialize the DAO with the connection
        } else {
            showError("Error", "Database connection is not available.");
        }
    }

    @FXML
    public void initialize() {
        // Set up TableView columns
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("Matricule"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        promotionColumn.setCellValueFactory(new PropertyValueFactory<>("Promotion"));

        // Set connection and load students
        setConnection();
        loadStudents();
    }

    private void loadStudents() {
        if (etudiantDAO == null) {
            showError("Error", "Database connection is not initialized.");
            return;
        }

        try {
            studentList = FXCollections.observableArrayList(etudiantDAO.getAllStudents());
            if (studentList.isEmpty()) {
                showInfo("No Students", "There are no students in the database.");
            } else {
                studentTable.setItems(studentList);
            }
        } catch (SQLException e) {
            showError("Error loading students", "Could not retrieve student data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteButtonClick() {
        Etudiant selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                etudiantDAO.deleteStudentById(selectedStudent.getId());
                studentList.remove(selectedStudent);
                showInfo("Student Deleted", "The student has been successfully deleted.");
            } catch (SQLException e) {
                showError("Error deleting student", "Could not delete the student.");
                e.printStackTrace();
            }
        } else {
            showWarning("No Selection", "Please select a student to delete.");
        }
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
        selectedStudentForEdit = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudentForEdit != null) {
            // Show form and populate fields with selected student's data
            formContainer.setVisible(true);
            matriculeField.setText(selectedStudentForEdit.getMatricule());
            nomField.setText(selectedStudentForEdit.getNom());
            prenomField.setText(selectedStudentForEdit.getPrenom());
            promotionField.setText(selectedStudentForEdit.getPromotion());
        } else {
            showWarning("No Selection", "Please select a student to modify.");
        }
    }

    @FXML
    public void onSaveButtonClick() {
        if (selectedStudentForEdit != null) {
            selectedStudentForEdit.setMatricule(matriculeField.getText());
            selectedStudentForEdit.setNom(nomField.getText());
            selectedStudentForEdit.setPrenom(prenomField.getText());
            selectedStudentForEdit.setPromotion(promotionField.getText());

            try {
                etudiantDAO.updateStudent(selectedStudentForEdit);
                loadStudents(); // Refresh the table with updated data
                formContainer.setVisible(false); // Hide form
                showInfo("Success", "Student details updated successfully.");
            } catch (SQLException e) {
                showError("Error", "Failed to update student details: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onCancelButtonClick() {
        formContainer.setVisible(false); // Hide form without saving
    }

    @FXML
    private void onStatistiqueButtonClick() {
        loadScene("/vues/ADMIN/AdminPageInit.fxml");
    }
    @FXML
    public void onGestiondesProfesseurButtonClick(ActionEvent actionEvent) {

        loadScene("/vues/ADMIN/GestionModule/GestionModule");
    }
    @FXML
    public void onGestiondesSecretaireButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/GestionProfesseurs/gestionProf.fxml");
    }

    @FXML
    public void onGestiondesEtudiantButtonClick(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/etudiantmanagment.fxml");

    }
    @FXML
    public void onAjouteEtudiant(ActionEvent actionEvent) {
        loadScene("/vues/ADMIN/ajouterunetudiant.fxml");
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
            Stage stage = (Stage) studentTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
