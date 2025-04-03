package dao;

import util.Session;

import java.sql.*;

public class InscriptionImp {

    private final Connection connection;

    public InscriptionImp(Connection connection) {
        this.connection = connection;
    }

    public void insertInscription(int idEtudiant, String moduleName) throws SQLException {
        String sql = "INSERT INTO Inscrire (idetudiant, idmodule, dateinscription) " +
                "VALUES (?, (SELECT idmodule FROM modules WHERE nommodule = ?), CURRENT_DATE)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEtudiant);
            stmt.setString(2, moduleName);

            stmt.executeUpdate();
        }
    }
}