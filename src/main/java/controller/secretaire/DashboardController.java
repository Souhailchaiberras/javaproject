package controller.secretaire;

import dao.EtudiantImp;
import dao.UserImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.HBox;

public class DashboardController {



    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label registeredStudentsLabel;

    @FXML
    private Label nonRegisteredStudentsLabel;

    @FXML
    private PieChart studentsPieChart;

    UserImp userImp = new UserImp();


    private int totalStudents = userImp.getNbrEtudiant();
    private int nonRegisteredStudents = userImp.getNbrnonInscEtudiant();
    private int registeredStudents = totalStudents - nonRegisteredStudents;


    @FXML
    private Label welcomeText;


    @FXML
    public void initialize() {
        // Remplir les labels avec des données
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        registeredStudentsLabel.setText(String.valueOf(registeredStudents));
        nonRegisteredStudentsLabel.setText(String.valueOf(nonRegisteredStudents));

        // Remplir le graphique circulaire
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Inscrits", registeredStudents),
                new PieChart.Data("Non Inscrits", nonRegisteredStudents)
        );
        studentsPieChart.setData(pieChartData);

        // Style optionnel pour le graphique
        studentsPieChart.setTitle("Répartition des Étudiants");
        studentsPieChart.setLabelsVisible(true);
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
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
