/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.ParametresFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import jpa.administration.Parametres;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */

@Named(value = "parametresBean")
@ViewScoped
public class ParametresBean implements Serializable{

    @EJB
    private ParametresFacade parametresFacade;
    private Parametres selectedParametres;
    private Parametres newParametres;
    private List<Parametres> listeParametress;
    private List<Parametres> filteredList;

    /**
     * Creates a new instance of ParametresBean
     */
    public ParametresBean() {
    }

    @PostConstruct
    public void init() {
        listeParametress = parametresFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            parametresFacade.create(newParametres);
            msg = JsfUtil.getBundleMsg("ParametresCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeParametress = parametresFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ParametresCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            parametresFacade.edit(selectedParametres);
            msg = JsfUtil.getBundleMsg("ParametresEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeParametress = parametresFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ParametresEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            parametresFacade.remove(selectedParametres);
            msg = JsfUtil.getBundleMsg("ParametresDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeParametress = parametresFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ParametresDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Parametres getSelectedParametres() {
        return selectedParametres;
    }

    public void setSelectedParametres(Parametres selectedParametres) {
        this.selectedParametres = selectedParametres;
    }

    public Parametres getNewParametres() {
        return newParametres;
    }

    public void setNewParametres(Parametres newParametres) {
        this.newParametres = newParametres;
    }

    public List<Parametres> getListeParametress() {
        return listeParametress;
    }

    public void setListeParametress(List<Parametres> listeParametress) {
        this.listeParametress = listeParametress;
    }

    public List<Parametres> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Parametres> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newParametres = new Parametres();
    }
    
    public void reset(ActionEvent e) {
        this.newParametres.reset();
    }
    
    
}
