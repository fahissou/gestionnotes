/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.formation;

import ejb.formation.FiliereFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.formation.Filiere;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "filiereBean")
@ViewScoped
public class FiliereBean implements Serializable{

    @EJB
    private FiliereFacade filiereFacade;
    private Filiere selectedFiliere;
    private Filiere newFiliere;
    private List<Filiere> listeFilieres;
    private List<Filiere> filteredList;
    
    public FiliereBean() {
    }
    @PostConstruct
    public void init() {
        listeFilieres = filiereFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            filiereFacade.create(newFiliere);
            msg = JsfUtil.getBundleMsg("FiliereCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeFilieres = filiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("FiliereCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            filiereFacade.edit(selectedFiliere);
            msg = JsfUtil.getBundleMsg("FiliereEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeFilieres = filiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("FiliereEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            filiereFacade.remove(selectedFiliere);
            msg = JsfUtil.getBundleMsg("FiliereDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeFilieres = filiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("FiliereDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Filiere getSelectedFiliere() {
        return selectedFiliere;
    }

    public void setSelectedFiliere(Filiere selectedFiliere) {
        this.selectedFiliere = selectedFiliere;
    }

    public Filiere getNewFiliere() {
        return newFiliere;
    }

    public void setNewFiliere(Filiere newFiliere) {
        this.newFiliere = newFiliere;
    }

    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
    }

    public List<Filiere> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Filiere> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate() {
        this.newFiliere = new Filiere();
    }
    
    public void reset(ActionEvent e) {
        this.newFiliere.reset();
    }
    
}
