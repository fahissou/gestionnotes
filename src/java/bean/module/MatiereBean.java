/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.module.MatiereFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.module.Matiere;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "matiereBean")
@ManagedBean
@ViewScoped
public class MatiereBean implements Serializable{
    @EJB
    private MatiereFacade matiereFacade;
    private Matiere newMatiere;
    private Matiere selectedMatiere;
    private List<Matiere>listeMatieres;
    private List<Matiere>filteredList;
    
    /**
     * Creates a new instance of MatiereBean
     */
    public MatiereBean() {
    }
    @PostConstruct
    public void init(){
        listeMatieres = matiereFacade.findAll();
        prepareCreate();
        
    }
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            matiereFacade.create(newMatiere);
            msg = JsfUtil.getBundleMsg("MatiereCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            matiereFacade.edit(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doDel(ActionEvent event) {
        String msg;
        try {
            matiereFacade.remove(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Matiere getNewMatiere() {
        return newMatiere;
    }

    public void setNewMatiere(Matiere newMatiere) {
        this.newMatiere = newMatiere;
    }

    public Matiere getSelectedMatiere() {
        return selectedMatiere;
    }

    public void setSelectedMatiere(Matiere selectedMatiere) {
        this.selectedMatiere = selectedMatiere;
    }

    public List<Matiere> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<Matiere> listeMatieres) {
        this.listeMatieres = listeMatieres;
    }

    public List<Matiere> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Matiere> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate(){
        newMatiere = new Matiere();
    }
    public void reset(){
        newMatiere.reset();
    }
    
}
