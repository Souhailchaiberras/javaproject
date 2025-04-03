package main;

import util.DatabaseConnection;

import java.sql.*;

public class MainTest {

    public static void main(String[] args) {
        try {
            // Initialize the email and password
            String email = "test@domain.com";
            String password = "password123";

            // Simulate adding a secretaire
            addSecretaire(email, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSecretaire(String email, String password) throws SQLException {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password cannot be empty");
        }

        // Get database connection
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Disable auto-commit for transaction management
            conn.setAutoCommit(false);

            try {
                // Check if the user already exists
                if (isUserExist(conn, email)) {
                    throw new SQLException("User with this email already exists.");
                }

                // Insert into users table
                String sqlInsertUser = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlInsertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    stmt.setInt(3, 2); // Role 2 for 'secretaire'
                    int affectedRows = stmt.executeUpdate();

                    if (affectedRows > 0) {
                        // Retrieve the generated user ID
                        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int iduser = generatedKeys.getInt(1);
                                System.out.println("Generated user ID: " + iduser);

                                // Insert into secretaire table with the generated iduser
                                String sqlInsertSecretaire = "INSERT INTO secretaire (iduser) VALUES (?)";
                                try (PreparedStatement stmtSecretaire = conn.prepareStatement(sqlInsertSecretaire)) {
                                    stmtSecretaire.setInt(1, iduser);
                                    stmtSecretaire.executeUpdate(); // Insert into the 'secretaire' table
                                }
                            } else {
                                throw new SQLException("Failed to retrieve the generated user ID.");
                            }
                        }
                    } else {
                        throw new SQLException("Failed to insert the user into the 'users' table.");
                    }
                }

                // Commit the transaction if both inserts are successful
                conn.commit();

            } catch (SQLException e) {
                // If there is an exception, rollback the transaction
                conn.rollback();
                throw new SQLException("Error while adding the secretary: " + e.getMessage(), e);
            } finally {
                // Ensure auto-commit is restored
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new SQLException("Database connection error: " + e.getMessage(), e);
        }
    }

    // Method to check if the user already exists by email
    public static boolean isUserExist(Connection conn, String email) throws SQLException {
        String sqlCheckUser = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCheckUser)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a result is found, user exists
            }
        }
    }
}
