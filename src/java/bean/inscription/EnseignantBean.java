/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.administration.ResponsabiliteFacade;
import ejb.formation.FiliereFacade;
import ejb.inscription.EnseignantFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import jpa.formation.Filiere;
import jpa.inscription.Enseignant;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */

@Named(value = "enseignantBean")
@ViewScoped
public class EnseignantBean implements Serializable{
    @EJB
    private ResponsabiliteFacade responsabiliteFacade;
    @EJB
    private FiliereFacade filiereFacade;
    
    @EJB
    private EnseignantFacade enseignantFacade;
    
    private Enseignant selectedEnseignant;
    private Enseignant newEnseignant;
    private List<Enseignant> listeEnseignants;
    private List<Enseignant> filteredList;
    private List<String> listeResponsabilite;
    private List<String> listeGrade;
    private String directeur, directeurAj;
    private List<Filiere> listeFilieres = null;
    /**
     * Creates a new instance of EnseignantBean
     */
    public EnseignantBean() {
    }

    @PostConstruct
    public void init() {
        listeEnseignants = enseignantFacade.findAll();
        prepareCreate();
        // liste des responsabilites
        listeResponsabilite = JsfUtil.getResponsabilite();
        directeur = getResponsable(responsabiliteFacade.getEnseignantByResponsabilite("DIRECTEUR"));
        directeurAj = getResponsable(responsabiliteFacade.getEnseignantByResponsabilite("DIRECTEUR AJOINT"));
        // liste des grades 
        listeGrade = JsfUtil.getGradeEnseignant();
       
    }  
    
    public void initFilires() {
//        if(newEnseignant.getResponsabilite().equals("RESPONSABLE DE FORMATION")){
//            listeFilieres = filiereFacade.findAll();
//        }
    }
    
    public String getResponsable(Enseignant enseignant) {
        String val = "";
        if(enseignant != null) {
            val = JsfUtil.getLabelGradeEnseignant(enseignant.getGrade())+" "+enseignant.getPrenom()+" " +enseignant.getNom();
        }
        return val;
    }

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.create(newEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.edit(selectedEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.remove(selectedEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
    }

    public Enseignant getSelectedEnseignant() {
        return selectedEnseignant;
    }

    public void setSelectedEnseignant(Enseignant selectedEnseignant) {
        this.selectedEnseignant = selectedEnseignant;
    }

    public Enseignant getNewEnseignant() {
        return newEnseignant;
    }

    public void setNewEnseignant(Enseignant newEnseignant) {
        this.newEnseignant = newEnseignant;
    }

    public List<Enseignant> getListeEnseignants() {
        return listeEnseignants;
    }

    public void setListeEnseignants(List<Enseignant> listeEnseignants) {
        this.listeEnseignants = listeEnseignants;
    }

    public List<Enseignant> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Enseignant> filteredList) {
        this.filteredList = filteredList;
    }            

    public List<String> getListeResponsabilite() {
        return listeResponsabilite;
    }

    public void setListeResponsabilite(List<String> listeResponsabilite) {
        this.listeResponsabilite = listeResponsabilite;
    }

    public List<String> getListeGrade() {
        return listeGrade;
    }

    public void setListeGrade(List<String> listeGrade) {
        this.listeGrade = listeGrade;
    }

    public void prepareCreate() {
        this.newEnseignant = new Enseignant();
    }
    
    public void reset(ActionEvent e) {
        this.newEnseignant.reset();
    }

    public String getDirecteur() {
        return directeur;
    }

    public void setDirecteur(String directeur) {
        this.directeur = directeur;
    }

    public String getDirecteurAj() {
        return directeurAj;
    }

    public void setDirecteurAj(String directeurAj) {
        this.directeurAj = directeurAj;
    }

    
}
