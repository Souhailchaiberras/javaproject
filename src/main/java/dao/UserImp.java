package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.User;
import util.DatabaseConnection;

public class UserImp {


    private  Connection conn = DatabaseConnection.getInstance().getConnection();


    public User getUserById(int id) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        User us = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from users where iduser = ?";
        try {
            ps = conn.prepareStatement(sql, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                us = new User(rs.getInt(1), rs.getString(2));
            }
            return us;
        } catch (SQLException e) {
            System.out.println("Une erreur est survenue" + e.getMessage());
        }
        return null;

    }

    public String getemailUser(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT email FROM users WHERE iduser = ?";

        try {
            System.out.println("Exécution de la requête pour l'ID: " + id); // Vérifiez que l'ID est bien passé
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id); // Le paramètre est bien passé ici
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("email"); // Utilisez le nom de la colonne
            }
        } catch (SQLException e) {
            System.out.println("Une erreur est survenue: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
        return null; // Aucun utilisateur trouvé
    }

    public int getNbrEtudiant() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="Select count(*) from etudiants ";
        try{
            ps=conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }

        } catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }
    public int getNbrnonInscEtudiant() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="Select count(*) from etudiants e Join inscrire i On i.idetudiant = e.idetudiant where idmodule = 4  ";
        try{
            ps=conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }

        } catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }




}
