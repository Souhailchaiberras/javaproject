package main;

import dao.ModuleImp;
import dao.ProfesseurImp;
import model.Module;
import model.Professeur;
import model.User;

import java.util.List;

public class test {

    public static void main(String[] args) {

        ProfesseurImp pfi = new ProfesseurImp();
        User pu = new User(null,"ali@gmail.com",null,null);
        Professeur p = new Professeur(null,"Ali","Midar","Technique");
        if(pfi.AjouterProf(p,pu)){
            System.out.println("Ajouter prof avec succes!");
        }

    }
}
