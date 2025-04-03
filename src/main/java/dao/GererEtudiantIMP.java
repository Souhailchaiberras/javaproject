package dao;

import model.User;
import util.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GererEtudiantIMP {

    private Connection connection;

    public GererEtudiantIMP(Connection connection) {
        this.connection = connection;
    }

    // Method to insert a new record into GererEtudiant or GererEtudiantAdmin
    public void insertGererEtudiant(int idEtudiant) throws SQLException {
        User currentUser = Session.getCurrentUser();

        if (currentUser == null) {
            throw new SQLException("No user is logged in.");
        }

        int role = currentUser.getRole();  // Role is now an integer (1 for admin, 2 for secretaire)
        String tableName;
        String userColumn;

        // Determine the table and user column based on the user's role
        switch (role) {
            case 1: // Admin role (role 1)
                tableName = "gereretudiantadmin";
                userColumn = "iduser";
                break;
            case 2: // Secretaire role (role 2)
                tableName = "gereretudiant";
                userColumn = "idusersecretaire";
                break;
            default:
                throw new SQLException("Unsupported user role: " + role + ". Cannot insert record.");
        }

        int userId = getUserIdForInsert(currentUser, role);

        String query = "INSERT INTO " + tableName + " (" + userColumn + ", idetudiant) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, idEtudiant);

            System.out.println("Executing query: " + query);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to insert record into " + tableName, e);
        }
    }

    // Method to get the appropriate user ID for insertion
    private int getUserIdForInsert(User currentUser, int role) throws SQLException {
        String query;

        if (role == 1) { // Admin role (role 1)
            query = "SELECT iduser FROM users WHERE email = ?";
        } else if (role == 2) { // Secretaire role (role 2)
            query = "SELECT u.iduser FROM users u JOIN secretaire s ON u.iduser = s.iduser WHERE u.email = ? LIMIT 1"; // Added LIMIT 1 to avoid multiple rows
        } else {
            throw new SQLException("Unsupported role: " + role);
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            System.out.println("Querying for user with email: " + currentUser.getEmail());
            statement.setString(1, currentUser.getEmail());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("iduser");
                } else {
                    throw new SQLException("User not found for email: " + currentUser.getEmail());
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve user ID for email: " + currentUser.getEmail(), e);
        }
    }
}
