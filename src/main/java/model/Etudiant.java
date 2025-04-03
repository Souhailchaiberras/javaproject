package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Etudiant {
    private Integer id;
    private String Nom;
    private String prenom;
    private String Matricule;
    private String DateNaissance;
    private String Promotion;


    public Etudiant(String promotion, String matricule, String dateNaissance, String prenom, String nom, Integer id) {
        Promotion = promotion;
        Matricule = matricule;
        DateNaissance = dateNaissance;
        this.prenom = prenom;
        Nom = nom;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromotion() {
        return Promotion;
    }

    public void setPromotion(String promotion) {
        Promotion = promotion;
    }

    public String getDateNaissance() {
        return DateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        DateNaissance = dateNaissance;
    }

    public String getMatricule() {
        return Matricule;
    }

    public void setMatricule(String matricule) {
        Matricule = matricule;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + Nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matricule='" + Matricule + '\'' +
                ", dateNaissance='" + DateNaissance + '\'' +
                ", promotion='" +   Promotion + '\'' +
                '}';
    }


}
