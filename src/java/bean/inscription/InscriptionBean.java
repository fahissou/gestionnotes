/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.InscriptionFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.Inscription;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "inscriptionBean")
public class InscriptionBean {

    private InscriptionFacade inscriptionFacade;
    private Inscription selectedInscription;
    private Inscription newInscription;
    private List<Inscription> listeInscriptions;
    private List<Inscription> filteredList;
    public InscriptionBean() {
    }
    
    @PostConstruct
    public void init() {
        listeInscriptions = inscriptionFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            inscriptionFacade.create(newInscription);
            msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeInscriptions = inscriptionFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            inscriptionFacade.edit(selectedInscription);
            msg = JsfUtil.getBundleMsg("InscriptionEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeInscriptions = inscriptionFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            inscriptionFacade.remove(selectedInscription);
            msg = JsfUtil.getBundleMsg("InscriptionDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeInscriptions = inscriptionFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Inscription getSelectedInscription() {
        return selectedInscription;
    }

    public void setSelectedInscription(Inscription selectedInscription) {
        this.selectedInscription = selectedInscription;
    }

    public Inscription getNewInscription() {
        return newInscription;
    }

    public void setNewInscription(Inscription newInscription) {
        this.newInscription = newInscription;
    }

    public List<Inscription> getListeInscriptions() {
        return listeInscriptions;
    }

    public void setListeInscriptions(List<Inscription> listeInscriptions) {
        this.listeInscriptions = listeInscriptions;
    }

    public List<Inscription> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Inscription> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newInscription = new Inscription();
    }
    
    public void reset(ActionEvent e) {
        this.newInscription.reset();
    }
    
}
