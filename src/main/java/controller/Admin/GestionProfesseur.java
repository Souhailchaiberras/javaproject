package controller.Admin;

import dao.ModuleImp;
import dao.ProfesseurImp;
import dao.UserImp;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.StringConverter;
import model.Etudiant;
import model.Module;
import model.Professeur;
import model.User;

import java.io.IOException;
import java.util.List;

public class GestionProfesseur {


    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField speField;


    @FXML
    private TableView<Professeur> professeursTable;

    @FXML
    private TableColumn<Professeur, Integer> idColumn;

    @FXML
    private TableColumn<Professeur, String> nameColumn;

    @FXML
    private TableColumn<Professeur, String> PrenomColumn;

    @FXML
    private TableColumn<Professeur, String> emailColumn;

    @FXML
    private TableColumn<Professeur, String> spColumn;

    @FXML
    private TextField searchField;

    @FXML
    private VBox formContainer;

    @FXML
    private ComboBox<Module> moduleComboBox;

    private ProfesseurImp professeurImp = new ProfesseurImp();
    private UserImp userImp = new UserImp();
    private ObservableList<Professeur> professeursList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        PrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        spColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        emailColumn.setCellValueFactory(param -> {
            Professeur prof = param.getValue();
            String email = userImp.getemailUser(prof.getId());
            if (email == null) {
                System.out.println("Email non trouvé pour le professeur avec ID: " + prof.getId());
            }
            return new SimpleStringProperty(email != null ? email : "Email non disponible");
        });

        loadModules();
        loadProfesseurs();
    }


    @FXML
    private void loadModules() {
        ModuleImp moduleImp = new ModuleImp();
        List<Module> modules = moduleImp.getAll();

        if (modules != null) {
            moduleComboBox.setItems(FXCollections.observableArrayList(modules));
            moduleComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Module module) {
                    return module != null ? module.getNomModule() : "";
                }

                @Override
                public Module fromString(String string) {
                    return moduleComboBox.getItems()
                            .stream()
                            .filter(module -> module.getNomModule().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        }
    }


    private void loadProfesseurs() {
        Module selectedModule = moduleComboBox.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            Integer idmodule = selectedModule.getId();
            List<Professeur> professeurs = professeurImp.getProfesseurByIdMod(idmodule);
            if (professeurs != null) {
                professeursList.setAll(professeurs);
                professeursTable.setItems(professeursList);
            } else {
                showWarning("Aucun professeur trouvé", "La liste des professeurs est vide.");
            }
        } else {
            showWarning("Module non sélectionné", "Veuillez sélectionner un module.");
        }
    }

    @FXML
    private void onContactButtonClick(ActionEvent actionEvent) {
        Professeur selectedProfesseur = professeursTable.getSelectionModel().getSelectedItem();
        if (selectedProfesseur != null) {
            try {
                String emailProf = userImp.getemailUser(selectedProfesseur.getId());
                if (emailProf == null || emailProf.isEmpty()) {
                    showAlert("Erreur", "L'email du professeur sélectionné est introuvable.", Alert.AlertType.WARNING);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/ADMIN/GestionProfesseurs/EnvoyerMessage.fxml"));
                Parent root = loader.load();

                EnvoyerMessageController controller = loader.getController();
                controller.setEmailDestinataire(emailProf);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger la scène.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un professeur.", Alert.AlertType.WARNING);
        }
    }



    @FXML
    private void onAjouterModuleButtonClick() {
            showModuleSelectionDialog();
        }

    private void showModuleSelectionDialog() {
        ModuleImp moduleImp = new ModuleImp();
        List<Module> modules = moduleImp.getAll();

        // Création du ChoiceDialog avec les modules
        ChoiceDialog<Module> dialog = new ChoiceDialog<>();
        dialog.setTitle("Sélectionner un module");
        dialog.setHeaderText("Choisissez un module à assigner");
        dialog.getItems().setAll(modules);

        // Utilise toString() pour afficher le nom du module dans la liste
        dialog.setContentText("Sélectionner un module");

        // Affichage du choix du module et assignation si un module est sélectionné
        dialog.showAndWait().ifPresent(selectedModule -> {
            assignModuleToProfesseur(selectedModule);
        });
    }




    private void assignModuleToProfesseur(Module selectedModule) {
        Professeur selectedProfesseur = professeursTable.getSelectionModel().getSelectedItem();
        if (selectedProfesseur == null) {
            showAlert("Erreur", "Aucun professeur sélectionné.", Alert.AlertType.WARNING);
            return;
        }
        professeurImp.AssignerMod(selectedProfesseur.getId(), selectedModule.getId());
        loadProfesseurs();
        showAlert("Succès", "Le module a été assigné au professeur.", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void onDeleteButtonClick() {
        Professeur selectedProf = professeursTable.getSelectionModel().getSelectedItem();
        if(selectedProf != null) {
            professeurImp.deleteProf(selectedProf);
            showInfo("Professeur supprimé", "Le professeur a été supprimé avec succès.");
        } else {
            showWarning("Aucune sélection", "Veuillez sélectionner un professeur à supprimer.");
        }

        loadProfesseurs();
    }
    @FXML
    private void onModuleSelected() {
        loadProfesseurs();
    }

    public void onSearchButtonClick() {
        String searchQuery = searchField.getText().trim();  // Retrieve the text from the search field
        if (!searchQuery.isEmpty()) {
            List<Professeur> professeurs = professeurImp.searchProfByName(searchQuery);
            if (professeurs != null && !professeurs.isEmpty()) {
                professeursTable.getItems().setAll(professeurs);  // Display the professors in the table
            } else {
                System.out.println("No professors found with the given name.");
                // Optionally, show an alert message to the user
            }
        } else {
            System.out.println("Please enter a search query.");
        }
    }
    @FXML
    public void onAddButtonClick() {
        loadScene("/vues/ADMIN/GestionProfesseurs/AjoutProfesseur.fxml");
    }

    @FXML
    private void onEditButtonClick() {
        Professeur selectedProf = professeursTable.getSelectionModel().getSelectedItem();
        if (selectedProf != null) {
            formContainer.setVisible(true);
            nomField.setText(selectedProf.getNom());
            prenomField.setText(selectedProf.getPrenom());
            emailField.setText(userImp.getemailUser(selectedProf.getId()));
            speField.setText(selectedProf.getSpecialite());

        } else {
            showAlert("Erreur", "Veuillez sélectionner un professeur à modifier.", Alert.AlertType.WARNING);
        }
    }



    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            Stage stage = (Stage) professeursTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveButtonClick() {
        Professeur selectedProfesseur = professeursTable.getSelectionModel().getSelectedItem();
        if (selectedProfesseur == null) {
            showAlert("Erreur", "Aucun professeur sélectionné pour la modification.", Alert.AlertType.WARNING);
            return;
        }

        String newNom = nomField.getText().trim();
        String newPrenom = prenomField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newSpecialite = speField.getText().trim();


        if (newNom.isEmpty() || newPrenom.isEmpty() || newEmail.isEmpty() || newSpecialite.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        Professeur updatedProf = new Professeur();
        updatedProf.setId(selectedProfesseur.getId());
        updatedProf.setNom(newNom);
        updatedProf.setPrenom(newPrenom);
        updatedProf.setSpecialite(newSpecialite);

        User updatedUser = new User();
        updatedUser.setId(selectedProfesseur.getId());
        updatedUser.setEmail(newEmail);

        boolean isUpdated = professeurImp.UpdateProf(updatedProf, updatedUser);
        if (!isUpdated) {
            showAlert("Erreur", "Échec de la mise à jour des informations.", Alert.AlertType.ERROR);
            return;
        }

        loadProfesseurs();

        showAlert("Succès", "Les informations du professeur ont été mises à jour.", Alert.AlertType.INFORMATION);
        onCancelButtonClick();
    }

    @FXML
    private void onCancelButtonClick() {
        // Réinitialiser les champs du formulaire
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        speField.clear();

        // Cacher le formulaire
        formContainer.setVisible(false);
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


