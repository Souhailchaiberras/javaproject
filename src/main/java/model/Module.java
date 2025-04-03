package model;

public class Module {
    private Integer id;
    private String NomModule;
    private String CodeModule;


    public Module() {}
    public Module(Integer id, String nomModule, String codeModule) {
        this.id = id;
        NomModule = nomModule;
        CodeModule = codeModule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomModule() {
        return NomModule;
    }

    public void setNomModule(String nomModule) {
        NomModule = nomModule;
    }

    public String getCodeModule() {
        return CodeModule;
    }

    public void setCodeModule(String codeModule) {
        CodeModule = codeModule;
    }
    @Override
    public String toString() {
        return NomModule;
    }
}
