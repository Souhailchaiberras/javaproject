package dao;

import model.User;
import model.Secraitaires; // Fixing the class name typo
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecretaireImp {

    private Connection connection;

    // Constructor to initialize the database connection
    public SecretaireImp() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Method to add a user to the 'users' table
    public boolean addUser(User user) {
        String query = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getRole());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Get generated user id
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to add a secretary to the 'secretaire' table
    public boolean addSecretaire(Secraitaires secretaire) {
        String query = "INSERT INTO secretaire (iduser) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, secretaire.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllSecretaires() {
        List<User> secretaires = new ArrayList<>();
        String query = "SELECT u.iduser, u.email, u.password, u.role FROM users u " +
                "JOIN secretaire s ON u.iduser = s.iduser " +
                "WHERE u.role = 2";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int iduser = resultSet.getInt("iduser");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                int role = resultSet.getInt("role");
                secretaires.add(new User(iduser, email, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return secretaires;
    }

    public boolean deleteSecretaire(int userId) throws SQLException {
        String deleteSecretaireQuery = "DELETE FROM secretaire WHERE iduser = ?";
        String deleteUserQuery = "DELETE FROM users WHERE iduser = ? AND role = 2"; // Ensure the role is 2 (secrétaire)

        try (PreparedStatement pstmtSecretaire = connection.prepareStatement(deleteSecretaireQuery);
             PreparedStatement pstmtUser = connection.prepareStatement(deleteUserQuery)) {

            // First, delete from the 'secretaire' table
            pstmtSecretaire.setInt(1, userId);
            int affectedRows = pstmtSecretaire.executeUpdate();

            if (affectedRows > 0) {
                // Then, delete from the 'users' table
                pstmtUser.setInt(1, userId);
                pstmtUser.executeUpdate();
                return true;  // Return true if both deletions were successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting secretary", e);
        }

        return false;
}
    public boolean updateSecretaire(User secretaire) throws SQLException {
        String query = "UPDATE users SET email = ?, password = ? WHERE iduser = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, secretaire.getEmail());
            statement.setString(2, secretaire.getPassword());
            statement.setInt(3, secretaire.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}

