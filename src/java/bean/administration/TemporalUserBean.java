/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.TemporalUserFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.TemporalUser;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "temporalUserBean")
public class TemporalUserBean implements Serializable{

    /**
     * Creates a new instance of TemporalUserBean
     */
    @EJB
    private TemporalUserFacade temporalUserFacade;
    private TemporalUser selectedTemporalUser;
    private TemporalUser newTemporalUser;
    private List<TemporalUser> listeTemporalUsers;
    private List<TemporalUser> filteredList;
    
    public TemporalUserBean() {
    }
    
    @PostConstruct
    public void init() {
        listeTemporalUsers = temporalUserFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            temporalUserFacade.create(newTemporalUser);
            msg = JsfUtil.getBundleMsg("TemporalUserCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeTemporalUsers = temporalUserFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("TemporalUserCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            temporalUserFacade.edit(selectedTemporalUser);
            msg = JsfUtil.getBundleMsg("TemporalUserEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeTemporalUsers = temporalUserFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("TemporalUserEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            temporalUserFacade.remove(selectedTemporalUser);
            msg = JsfUtil.getBundleMsg("TemporalUserDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeTemporalUsers = temporalUserFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("TemporalUserDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public TemporalUser getSelectedTemporalUser() {
        return selectedTemporalUser;
    }

    public void setSelectedTemporalUser(TemporalUser selectedTemporalUser) {
        this.selectedTemporalUser = selectedTemporalUser;
    }

    public TemporalUser getNewTemporalUser() {
        return newTemporalUser;
    }

    public void setNewTemporalUser(TemporalUser newTemporalUser) {
        this.newTemporalUser = newTemporalUser;
    }

    public List<TemporalUser> getListeTemporalUsers() {
        return listeTemporalUsers;
    }

    public void setListeTemporalUsers(List<TemporalUser> listeTemporalUsers) {
        this.listeTemporalUsers = listeTemporalUsers;
    }

    public List<TemporalUser> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<TemporalUser> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newTemporalUser = new TemporalUser();
    }
    
    public void reset(ActionEvent e) {
        this.newTemporalUser.reset();
    }
    
    
}
