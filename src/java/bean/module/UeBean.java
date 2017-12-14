/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.module.UeFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "ueBean")
@ManagedBean
@ViewScoped
public class UeBean implements Serializable{
    @EJB
    private UeFacade ueFacade;
    private Ue newUe;
    private Ue selectedUe;
    private List<Ue> listeUes;
    private List<Ue> filteredList;
    
    

    /**
     * Creates a new instance of UeBean
     */
    public UeBean() {
    }
    @PostConstruct
    public void init(){
        prepareCreate();
        listeUes = ueFacade.findAll();
    }
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            ueFacade.create(newUe);
            msg = JsfUtil.getBundleMsg("UeCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeUes = ueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UeCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            ueFacade.edit(selectedUe);
            msg = JsfUtil.getBundleMsg("UeEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeUes = ueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UeEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doDel(ActionEvent event) {
        String msg;
        try {
            ueFacade.remove(selectedUe);
            msg = JsfUtil.getBundleMsg("UeDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeUes = ueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UeDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Ue getNewUe() {
        return newUe;
    }

    public void setNewUe(Ue newUe) {
        this.newUe = newUe;
    }

    public Ue getSelectedUe() {
        return selectedUe;
    }

    public void setSelectedUe(Ue selectedUe) {
        this.selectedUe = selectedUe;
    }

    public List<Ue> getListeUes() {
        return listeUes;
    }

    public void setListeUes(List<Ue> listeUes) {
        this.listeUes = listeUes;
    }

    public List<Ue> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Ue> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate(){
        newUe = new Ue();
    }
    public void reset(){
        newUe.reset();
    }
    
}
