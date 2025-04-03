package controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

public class DashboardController {

    @FXML
    private Label studentCountLabel;
    @FXML
    private Label professorCountLabel;
    @FXML
    private Label secretaryCountLabel;
    @FXML
    private Label moduleCountLabel;
    @FXML
    private PieChart studentModulePieChart; // For student percentages
    @FXML
    private PieChart professorModulePieChart; // For professor percentages
    @FXML
    private BarChart<String, Number> moduleBarChart; // For professors and students per module
    @FXML
    private CategoryAxis moduleCategoryAxis;
    @FXML
    private NumberAxis studentCountYAxis;

    private final Connection connection;

    public DashboardController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @FXML
    public void initialize() {
        loadCounts();
        loadCharts();
    }

    private void loadCounts() {
        try {
            studentCountLabel.setText(getCount("SELECT COUNT(*) FROM etudiants"));
            professorCountLabel.setText(getCount("SELECT COUNT(*) FROM professeurs"));
            secretaryCountLabel.setText(getCount("SELECT COUNT(*) FROM secretaire"));
            moduleCountLabel.setText(getCount("SELECT COUNT(*) FROM modules"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getCount(String query) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? String.valueOf(rs.getInt(1)) : "0";
        }
    }

    private void loadCharts() {
        try {
            // Load PieChart for Student Percentage in Modules
            ObservableList<PieChart.Data> studentData = FXCollections.observableArrayList();
            String studentQuery = "SELECT m.nommodule, COUNT(i.idetudiant) AS student_count " +
                    "FROM modules m LEFT JOIN inscrire i ON m.idmodule = i.idmodule " +
                    "GROUP BY m.nommodule";
            try (PreparedStatement stmt = connection.prepareStatement(studentQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String moduleName = rs.getString("nommodule");
                    int studentCount = rs.getInt("student_count");
                    studentData.add(new PieChart.Data(moduleName, studentCount));
                }
            }
            studentModulePieChart.setData(studentData); // Ensure PieChart is properly set with data

            // Load PieChart for Professor Percentage in Modules
            ObservableList<PieChart.Data> professorData = FXCollections.observableArrayList();
            String professorQuery = "SELECT m.nommodule, COUNT(a.iduser) AS professor_count " +
                    "FROM modules m LEFT JOIN assigner a ON m.idmodule = a.idmodule " +
                    "GROUP BY m.nommodule";
            try (PreparedStatement stmt = connection.prepareStatement(professorQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String moduleName = rs.getString("nommodule");
                    int professorCount = rs.getInt("professor_count");
                    professorData.add(new PieChart.Data(moduleName, professorCount));
                }
            }
            professorModulePieChart.setData(professorData); // Ensure PieChart is properly set with data

            // Load BarChart for Students and Professors per Module
            XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
            XYChart.Series<String, Number> professorSeries = new XYChart.Series<>();
            String barChartQuery = "SELECT m.nommodule, COUNT(DISTINCT i.idetudiant) AS student_count, " +
                    "COUNT(DISTINCT a.iduser) AS professor_count " +
                    "FROM modules m LEFT JOIN inscrire i ON m.idmodule = i.idmodule " +
                    "LEFT JOIN assigner a ON m.idmodule = a.idmodule " +
                    "GROUP BY m.nommodule";
            try (PreparedStatement stmt = connection.prepareStatement(barChartQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String moduleName = rs.getString("nommodule");
                    int studentCount = rs.getInt("student_count");
                    int professorCount = rs.getInt("professor_count");
                    studentSeries.getData().add(new XYChart.Data<>(moduleName, studentCount));
                    professorSeries.getData().add(new XYChart.Data<>(moduleName, professorCount));
                }
            }
            moduleBarChart.getData().addAll(studentSeries, professorSeries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStatistiqueButtonClick(ActionEvent event) {
        loadScene(event, "/vues/ADMIN/AdminPageInit.fxml");
    }

    @FXML
    public void onGestiondesProfesseurButtonClick(ActionEvent event) {
        loadScene(event, "/vues/ADMIN/GestionProfesseurs/gestionProf.fxml");
    }

    @FXML
    public void onGestiondesSecretaireButtonClick(ActionEvent event) {
        loadScene(event, "/vues/ADMIN/gestiondessecretaire.fxml");
    }

    @FXML
    public void onGestiondesEtudiantButtonClick(ActionEvent event) {
        loadScene(event, "/vues/ADMIN/etudiantmanagment.fxml");
    }

    @FXML
    private void onGestiondesModulesButtonClick(ActionEvent event) {
        loadScene(event, "/vues/ADMIN/GestionModule/GestionModule.fxml");
    }

    @FXML
    private void onDesconnected(ActionEvent event) {
        loadScene(event, "/vues/login.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
