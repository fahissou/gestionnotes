/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.HabilitationFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Habilitation;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named (value = "habilitationBean")
public class HabilitationBean  implements Serializable{
    @EJB
    private HabilitationFacade habilitationFacade;
    private Habilitation selectedHabilitation;
    private Habilitation newHabilitation;
    private List<Habilitation> listeHabilitations;
    private List<Habilitation> filteredList;
    

    /**
     * Creates a new instance of HabilitationBean
     */
    public HabilitationBean() {
    }
    
    @PostConstruct
    public void init() {
        listeHabilitations = habilitationFacade.findAll();
        prepareCreate();
    }  
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            habilitationFacade.create(newHabilitation);
            msg = JsfUtil.getBundleMsg("HabilitationCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeHabilitations = habilitationFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HabilitationCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            habilitationFacade.edit(selectedHabilitation);
            msg = JsfUtil.getBundleMsg("HabilitationEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeHabilitations = habilitationFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HabilitationEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            habilitationFacade.remove(selectedHabilitation);
            msg = JsfUtil.getBundleMsg("HabilitationDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeHabilitations = habilitationFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HabilitationDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Habilitation getSelectedHabilitation() {
        return selectedHabilitation;
    }

    public void setSelectedHabilitation(Habilitation selectedHabilitation) {
        this.selectedHabilitation = selectedHabilitation;
    }

    public Habilitation getNewHabilitation() {
        return newHabilitation;
    }

    public void setNewHabilitation(Habilitation newHabilitation) {
        this.newHabilitation = newHabilitation;
    }

    public List<Habilitation> getListeHabilitations() {
        return listeHabilitations;
    }

    public void setListeHabilitations(List<Habilitation> listeHabilitations) {
        this.listeHabilitations = listeHabilitations;
    }

    public List<Habilitation> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Habilitation> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate(){
        newHabilitation  = new Habilitation();
    }
    
}
