/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.formation;

import ejb.formation.GroupeEtudiantFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.formation.GroupeEtudiant;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "groupeEtudiantBean")
@ManagedBean
@ViewScoped
public class GroupeEtudiantBean implements Serializable{
    @EJB
    private GroupeEtudiantFacade groupeEtudiantFacade;
    private GroupeEtudiant newGroupeEtudiant;
    private GroupeEtudiant selectedGroupeEtudiant;
    private List<GroupeEtudiant> listesGroupeEtudiants;
    private List<GroupeEtudiant>filteredList;
    
    /**
     * Creates a new instance of GroupeEtudiantBean
     */
    public GroupeEtudiantBean() {
    }
    @PostConstruct
    public void init(){
        listesGroupeEtudiants = groupeEtudiantFacade.findAll();
        prepareCreate();
        
    }
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            groupeEtudiantFacade.create(newGroupeEtudiant);
            msg = JsfUtil.getBundleMsg("GroupeEtudiantCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listesGroupeEtudiants = groupeEtudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeEtudiantCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            groupeEtudiantFacade.edit(selectedGroupeEtudiant);
            msg = JsfUtil.getBundleMsg("GroupeEtudiantEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listesGroupeEtudiants = groupeEtudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeEtudiantEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doDel(ActionEvent event) {
        String msg;
        try {
            groupeEtudiantFacade.remove(selectedGroupeEtudiant);
            msg = JsfUtil.getBundleMsg("GroupeEtudiantDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listesGroupeEtudiants = groupeEtudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupeEtudiantDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public GroupeEtudiant getNewGroupeEtudiant() {
        return newGroupeEtudiant;
    }

    public void setNewGroupeEtudiant(GroupeEtudiant newGroupeEtudiant) {
        this.newGroupeEtudiant = newGroupeEtudiant;
    }

    public GroupeEtudiant getSelectedGroupeEtudiant() {
        return selectedGroupeEtudiant;
    }

    public void setSelectedGroupeEtudiant(GroupeEtudiant selectedGroupeEtudiant) {
        this.selectedGroupeEtudiant = selectedGroupeEtudiant;
    }

    public List<GroupeEtudiant> getListesGroupeEtudiants() {
        return listesGroupeEtudiants;
    }

    public void setListesGroupeEtudiants(List<GroupeEtudiant> listesGroupeEtudiants) {
        this.listesGroupeEtudiants = listesGroupeEtudiants;
    }

    public List<GroupeEtudiant> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<GroupeEtudiant> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate(){
        newGroupeEtudiant = new GroupeEtudiant();
    }
    public void reset(){
        newGroupeEtudiant.reset();
    }
    
}
