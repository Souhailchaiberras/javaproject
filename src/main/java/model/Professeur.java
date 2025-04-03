package model;

public class Professeur {

    private Integer id;
    private String Nom;
    private String prenom;
    private String specialite;


    public Professeur(Integer id, String prenom, String nom, String specialite) {
        this.id = id;
        this.prenom = prenom;
        Nom = nom;
        this.specialite = specialite;
    }
    public Professeur() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }


}
