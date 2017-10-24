/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.EtudiantFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.Etudiant;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "etudiantBean")
public class EtudiantBean implements Serializable{

    private EtudiantFacade etudiantFacade;
    private Etudiant selectedEtudiant;
    private Etudiant newEtudiant;
    private List<Etudiant> listeEtudiants;
    private List<Etudiant> filteredList;
    public EtudiantBean() {
    }
    
    @PostConstruct
    public void init() {
        listeEtudiants = etudiantFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.create(newEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.edit(selectedEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.remove(selectedEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Etudiant getSelectedEtudiant() {
        return selectedEtudiant;
    }

    public void setSelectedEtudiant(Etudiant selectedEtudiant) {
        this.selectedEtudiant = selectedEtudiant;
    }

    public Etudiant getNewEtudiant() {
        return newEtudiant;
    }

    public void setNewEtudiant(Etudiant newEtudiant) {
        this.newEtudiant = newEtudiant;
    }

    public List<Etudiant> getListeEtudiants() {
        return listeEtudiants;
    }

    public void setListeEtudiants(List<Etudiant> listeEtudiants) {
        this.listeEtudiants = listeEtudiants;
    }

    public List<Etudiant> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Etudiant> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newEtudiant = new Etudiant();
    }
    
    public void reset(ActionEvent e) {
        this.newEtudiant.reset();
    }
    
    
}
