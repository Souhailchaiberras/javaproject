package model;

public class Inscription {

    private Integer idEtudiant;
    private Integer idModule;


    public Inscription(Integer idEtudiant, Integer idModule) {
        this.idEtudiant = idEtudiant;
        this.idModule = idModule;
    }

    public Integer getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Integer idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }
}
