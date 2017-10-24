/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.GroupeFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.administration.Groupe;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "groupeBean")
public class GroupeBean implements Serializable{

    private GroupeFacade groupeFacade;
    private Groupe selectedGroupe;
    private Groupe newGroupe;
    private List<Groupe> listeGroupes;
    private List<Groupe> filteredList;
    public GroupeBean() {
    }
    
    @PostConstruct
    public void init() {
        listeGroupes = groupeFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            groupeFacade.create(newGroupe);
            msg = JsfUtil.getBundleMsg("GroupeCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeGroupes = groupeFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            groupeFacade.edit(selectedGroupe);
            msg = JsfUtil.getBundleMsg("GroupeEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeGroupes = groupeFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            groupeFacade.remove(selectedGroupe);
            msg = JsfUtil.getBundleMsg("GroupeDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeGroupes = groupeFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Groupe getSelectedGroupe() {
        return selectedGroupe;
    }

    public void setSelectedGroupe(Groupe selectedGroupe) {
        this.selectedGroupe = selectedGroupe;
    }

    public Groupe getNewGroupe() {
        return newGroupe;
    }

    public void setNewGroupe(Groupe newGroupe) {
        this.newGroupe = newGroupe;
    }

    public List<Groupe> getListeGroupes() {
        return listeGroupes;
    }

    public void setListeGroupes(List<Groupe> listeGroupes) {
        this.listeGroupes = listeGroupes;
    }

    public List<Groupe> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Groupe> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newGroupe = new Groupe();
    }
    
    public void reset(ActionEvent e) {
        this.newGroupe.reset();
    }
    
}
