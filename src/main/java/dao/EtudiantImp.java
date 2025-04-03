package dao;

import model.Etudiant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantImp {

    private final Connection connection;


    public EtudiantImp(Connection connection) {
        this.connection = connection;
    }

    // Insert a new student into the database
    public int insertEtudiant(String matricule, String nom, String prenom, String promotion, String dateNaissance) throws SQLException {
        String sql = "INSERT INTO etudiants (matricule, nom, prenom, datenaissance, promotion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, matricule);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setDate(4, Date.valueOf(dateNaissance));
            stmt.setString(5, promotion);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated ID
                } else {
                    throw new SQLException("Failed to insert student, no ID obtained.");
                }
            }
        }
    }

    // Check if a student exists based on matricule
    public boolean isStudentExists(String matricule) throws SQLException {
        String sql = "SELECT COUNT(*) FROM etudiants WHERE matricule = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, matricule);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0; // Return true if student exists
            }
        }
    }

    // Retrieve all students from the database
    public List<Etudiant> getAllStudents() throws SQLException {
        String sql = "SELECT idetudiant, matricule, nom, prenom, datenaissance, promotion FROM etudiants";
        List<Etudiant> students = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Etudiant student = new Etudiant(
                        rs.getString("Promotion"),
                        rs.getString("Matricule"),
                        rs.getString("DateNaissance"),
                        rs.getString("Prenom"),
                        rs.getString("Nom"),
                        rs.getInt("IdEtudiant") // Use 'IdEtudiant' instead of 'id'
                );
                students.add(student);
            }
        }
        return students;
    }

    // Delete a student by ID
    public boolean deleteStudentById(int id) throws SQLException {
        String deleteInscrire = "DELETE FROM inscrire WHERE idetudiant = ?";
        String deleteGererEtudiant = "DELETE FROM gereretudiant WHERE idetudiant = ?";
        String deleteGererEtudiantAdmin = "DELETE FROM gereretudiantadmin WHERE idetudiant = ?";
        String deleteEtudiant = "DELETE FROM etudiants WHERE idetudiant = ?";

        try (PreparedStatement stmtInscrire = connection.prepareStatement(deleteInscrire);
             PreparedStatement stmtGererEtudiant = connection.prepareStatement(deleteGererEtudiant);
             PreparedStatement stmtGererEtudiantAdmin = connection.prepareStatement(deleteGererEtudiantAdmin);
             PreparedStatement stmtEtudiant = connection.prepareStatement(deleteEtudiant)) {

            connection.setAutoCommit(false); // Start transaction

            // Delete from inscrire
            stmtInscrire.setInt(1, id);
            stmtInscrire.executeUpdate();
            System.out.println("Deleted from inscrire for idetudiant: " + id);

            // Delete from gereretudiant
            stmtGererEtudiant.setInt(1, id);
            stmtGererEtudiant.executeUpdate();
            System.out.println("Deleted from gereretudiant for idetudiant: " + id);

            // Delete from gereretudiantadmin
            stmtGererEtudiantAdmin.setInt(1, id);
            stmtGererEtudiantAdmin.executeUpdate();
            System.out.println("Deleted from gereretudiantadmin for idetudiant: " + id);

            // Delete from etudiants
            stmtEtudiant.setInt(1, id);
            boolean result = stmtEtudiant.executeUpdate() > 0;
            if (result) {
                System.out.println("Deleted from etudiants for idetudiant: " + id);
            } else {
                System.out.println("No record found in etudiants for idetudiant: " + id);
            }

            connection.commit(); // Commit transaction
            return result;
        } catch (SQLException ex) {
            System.err.println("An error occurred during deletion. Rolling back changes. Error: " + ex.getMessage());
            connection.rollback(); // Rollback on failure
            throw ex;
        } finally {
            connection.setAutoCommit(true); // Restore auto-commit
        }
    }

    public void updateStudent(Etudiant student) throws SQLException {
        // Disable auto-commit if it's turned off
        connection.setAutoCommit(false);

        String query = "UPDATE etudiants SET matricule = ?, nom = ?, prenom = ?, promotion = ? WHERE idetudiant = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getMatricule());
            stmt.setString(2, student.getNom());
            stmt.setString(3, student.getPrenom());
            stmt.setString(4, student.getPromotion());
            stmt.setInt(5, student.getId());
            stmt.executeUpdate();

            // Commit the transaction after the update
            connection.commit();
        } catch (SQLException e) {
            // Rollback in case of an error
            connection.rollback();
            throw e;
        } finally {
            // Reset auto-commit to true (if it was previously false)
            connection.setAutoCommit(true);
        }
    }




}