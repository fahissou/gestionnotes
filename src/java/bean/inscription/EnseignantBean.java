/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.EnseignantFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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
    private EnseignantFacade enseignantFacade;
    private Enseignant selectedEnseignant;
    private Enseignant newEnseignant;
    private List<Enseignant> listeEnseignants;
    private List<Enseignant> filteredList;
    private List<String> listeResponsabilite;
    private List<String> listeGrade;
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
        listeResponsabilite = new ArrayList<>();
        listeResponsabilite.add("Directeur");
        listeResponsabilite.add("Directeur Adjoint");
        listeResponsabilite.add("Responsable de Formation");
        listeResponsabilite.add("Aucune");
        // liste des grades 
        listeGrade = new ArrayList<>();
        listeGrade.add("Professeur");
        listeGrade.add("Docteur");
        listeGrade.add("Ingénieur");
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
    
    
}
