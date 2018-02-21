/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.EnseignerFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.inscription.Enseigner;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "enseignerBean")
public class EnseignerBean implements Serializable{
    @EJB
    private EnseignerFacade enseignerFacade;
    private Enseigner selectedEnseigner;
    private Enseigner newEnseigner;
    private List<Enseigner> listeEnseigners;
    private List<Enseigner> filteredList;
    private List<String> listeResponsabilite;
    private List<String> listeGrade;

    /**
     * Creates a new instance of EnseignerBean
     */
    public EnseignerBean() {
    }
    
    @PostConstruct
    public void init(){
        listeEnseigners = enseignerFacade.findAll();
        prepareCreate();
    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.create(newEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.edit(selectedEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.remove(selectedEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Enseigner getSelectedEnseigner() {
        return selectedEnseigner;
    }

    public void setSelectedEnseigner(Enseigner selectedEnseigner) {
        this.selectedEnseigner = selectedEnseigner;
    }

    public Enseigner getNewEnseigner() {
        return newEnseigner;
    }

    public void setNewEnseigner(Enseigner newEnseigner) {
        this.newEnseigner = newEnseigner;
    }

    public List<Enseigner> getListeEnseigners() {
        return listeEnseigners;
    }

    public void setListeEnseigners(List<Enseigner> listeEnseigners) {
        this.listeEnseigners = listeEnseigners;
    }

    public List<Enseigner> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Enseigner> filteredList) {
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
    
    public void prepareCreate(){
        newEnseigner = new Enseigner();
    }
    
}
