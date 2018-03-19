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
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import jpa.inscription.Etudiant;
import org.primefaces.context.RequestContext;



/**
 *
 * @author AHISSOU Florent
 */

public class ParametragesBean implements Serializable{
    
    private static String idEtudiant;
    private String idEtudiantTmp ;
    private static String pathRoot;
    public ParametragesBean() {
    }

    @PostConstruct
    public void init() {
         String id = "";
         idEtudiantTmp = id;
         ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
         pathRoot = servletContext.getRealPath("/");
    }
    
    
    
    public void update(){
        idEtudiant = idEtudiantTmp;
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/rechercheravancee/'");
    } 

    public static String getIdEtudiant() {
        return idEtudiant;
    }

    public static void setIdEtudiant(String idEtudiant) {
        ParametragesBean.idEtudiant = idEtudiant;
    }

    public String getIdEtudiantTmp() {
        return idEtudiantTmp;
    }

    public void setIdEtudiantTmp(String idEtudiantTmp) {
        this.idEtudiantTmp = idEtudiantTmp;
    }

    public static String getPathRoot() {
        return pathRoot;
    }

    public static void setPathRoot(String pathRoot) {
        ParametragesBean.pathRoot = pathRoot;
    }
    
    
}
