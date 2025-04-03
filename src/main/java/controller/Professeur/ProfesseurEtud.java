package controller.Professeur;
import dao.ProfesseurImp;
import dao.UserImp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Etudiant;
import model.Module;
import model.User;
import util.Session;

import java.io.File;
import java.io.IOException;
import java.util.List;
import controller.Professeur.PdfGenerator;

public class ProfesseurEtud {
    @FXML
    private Button generatePdfButton;

    @FXML
    private Label welcomeText;

    @FXML
    private ComboBox<Module> moduleComboBox;

    @FXML
    private TableView<Etudiant> studentTable;

    @FXML
    private TableColumn<Etudiant, Integer> idColumn;
    @FXML
    private TableColumn<Etudiant, String> nameColumn;

    @FXML
    private TableColumn<Etudiant, String> PrenomColumn;


    @FXML
    private TextField searchField;

    private ProfesseurImp professeurImp = new ProfesseurImp();

    private User ue = Session.getCurrentUser();


    @FXML
    public void initialize() {
        // Initialisation des colonnes du tableau des étudiants
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));  // Assurez-vous que le getter s'appelle getNom()
        PrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));  // Assurez-vous que le getter s'appelle getPrenom()

        loadModules();
    }

    private void loadModules() {
        // Récupérer l'utilisateur authentifié
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Aucun utilisateur authentifié.");
            return;
        }

        // Récupérer l'ID de l'utilisateur
        int userId = currentUser.getId();
        System.out.println("Utilisateur authentifié avec l'ID : " + userId);

        // Charger les modules assignés à cet utilisateur
        List<Module> modules = professeurImp.getModuleAssigner(userId);
        if (modules != null && !modules.isEmpty()) {
            moduleComboBox.getItems().addAll(modules);

            moduleComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Module module) {
                    return module != null ? module.getNomModule() : "";
                }

                @Override
                public Module fromString(String string) {
                    return null;
                }
            });
        } else {
            System.out.println("Aucun module assigné trouvé.");
        }
    }




    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onDashboardButtonClick() {
        // Logique pour revenir au tableau de bord
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ProfesseurInterface/Dashboard.fxml"));
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
    protected void onStudentManagementButtonClick() {
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ProfesseurInterface/AffichageModules.fxml"));
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
    protected void onReportsButtonClick() {
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
    protected void onGeneratePdfButtonClick(ActionEvent event) {
        // Get the list of students from the TableView
        List<Etudiant> students = studentTable.getItems();

        if (students != null && !students.isEmpty()) {
            // Open a file chooser to select the destination of the PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(generatePdfButton.getScene().getWindow());

            if (selectedFile != null) {
                // Call the PDF generator and pass the students list
                PdfGenerator.generatePdf(students, selectedFile);
                System.out.println("PDF generated successfully at " + selectedFile.getAbsolutePath());
            }
        } else {
            System.out.println("No students found to generate PDF.");
        }
    }


    @FXML
    protected void onLogoutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/login.fxml"));
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
    protected void onAddStudentButtonClick() {
        System.out.println("Add student button clicked");
    }

    @FXML
    protected void onEditStudentButtonClick() {
        System.out.println("Edit student button clicked");
    }

    @FXML
    protected void onDeleteStudentButtonClick() {
        System.out.println("Delete student button clicked");
    }

    @FXML
    protected void onSearchButtonClick() {
        String searchQuery = searchField.getText().trim();  // Retrieve the text from the search field
        if (!searchQuery.isEmpty()) {
            List<Etudiant> etudiants = professeurImp.searchEtudiantByName(searchQuery);
            if (etudiants != null && !etudiants.isEmpty()) {
                studentTable.getItems().setAll(etudiants);  // Display the students in the table
            } else {
                System.out.println("No students found with the given name.");
                // Optionally, show an alert message to the user
            }
        } else {
            System.out.println("Please enter a search query.");
        }
    }


    @FXML
    protected void onModuleSelected() {

        Module selectedModule = moduleComboBox.getValue();
        if (selectedModule != null) {

            int moduleId = selectedModule.getId();
            List<Etudiant> students = professeurImp.listeEtudiants(moduleId);
            System.out.println("Étudiants du module : " + students);


            studentTable.getItems().clear();

            // Ajouter les étudiants à la TableView
            studentTable.getItems().addAll(students);


            for (Etudiant etudiant : students) {
                Integer id = etudiant.getId();
                String studentName = etudiant.getNom();
                String studentPrenom = etudiant.getPrenom();
                System.out.println(studentName + " " + studentPrenom + " - " + studentPrenom);
            }
        }
    }





}