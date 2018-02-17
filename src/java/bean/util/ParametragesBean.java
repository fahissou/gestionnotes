/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.util;

import ejb.inscription.EtudiantFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import jpa.inscription.Etudiant;
import org.primefaces.context.RequestContext;



/**
 *
 * @author AHISSOU Florent
 */

public class ParametragesBean implements Serializable{
    @EJB
    private EtudiantFacade etudiantFacade;

    private  Etudiant etudiant;
    private static Etudiant etudiant1;
    public ParametragesBean() {
    }

    @PostConstruct
    public void init() {
        String id = "fksjk";
        etudiant = etudiantFacade.find(id);
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    
    public static Etudiant getEtudiantInstance() {
        return etudiant1;
    }
    
    public void update(){
        etudiant1 = etudiant;
        System.out.println("iiii+ "+etudiant1.getNom());
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/rechercheravancee/'");
    } 

    public static Etudiant getEtudiant1() {
        return etudiant1;
    }

    public static void setEtudiant1(Etudiant etudiant1) {
        ParametragesBean.etudiant1 = etudiant1;
    }
    
    
}
