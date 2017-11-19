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
    private List<String> niveauCycle1;
    private List<String> niveauCycle2;
    private List<String> niveauCycle3;
    private List<String> Cycle;
    
    public InscriptionBean() {
    }
    
    @PostConstruct
    public void init() {
        listeInscriptions = inscriptionFacade.findAll();
        prepareCreate();
        niveauCycle1 = JsfUtil.listeNiveauCycle1();
        niveauCycle2 = JsfUtil.listeNiveauCycle2();
        niveauCycle3 = JsfUtil.listeNiveauCycle3();
        Cycle        = JsfUtil.listeCycle();
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

    public InscriptionFacade getInscriptionFacade() {
        return inscriptionFacade;
    }

    public void setInscriptionFacade(InscriptionFacade inscriptionFacade) {
        this.inscriptionFacade = inscriptionFacade;
    }

    public List<String> getNiveauCycle1() {
        return niveauCycle1;
    }

    public void setNiveauCycle1(List<String> niveauCycle1) {
        this.niveauCycle1 = niveauCycle1;
    }

    public List<String> getNiveauCycle2() {
        return niveauCycle2;
    }

    public void setNiveauCycle2(List<String> niveauCycle2) {
        this.niveauCycle2 = niveauCycle2;
    }

    public List<String> getNiveauCycle3() {
        return niveauCycle3;
    }

    public void setNiveauCycle3(List<String> niveauCycle3) {
        this.niveauCycle3 = niveauCycle3;
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

    public List<String> getCycle() {
        return Cycle;
    }

    public void setCycle(List<String> Cycle) {
        this.Cycle = Cycle;
    }

    
    public void prepareCreate() {
        this.newInscription = new Inscription();
    }
    
    public void reset(ActionEvent e) {
        this.newInscription.reset();
    }
    
}
