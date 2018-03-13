/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import bean.authentificate.AuthentificateBean;
import ejb.administration.JournalisationFacade;
import ejb.administration.UtilisateurFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Journalisation;
import jpa.administration.Utilisateur;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named (value = "journalisationBean")
public class JournalisationBean  implements Serializable{
    @EJB
    private UtilisateurFacade utilisateurFacade;
    @EJB
    private JournalisationFacade journalisationFacade;
    private Journalisation selectedJournalisation;
    private Journalisation newJournalisation;
    private List<Journalisation> listeJournalisations;
    private List<Journalisation> filteredList;

   

    /**
     * Creates a new instance of JournalisationBean
     */
    public JournalisationBean() {
    }
    
    @PostConstruct
    public void init() {
        listeJournalisations = journalisationFacade.findAll();
        prepareCreate();
    }  
//    public void doCreate(ActionEvent event) {
//        String msg;
//        try {
//            journalisationFacade.create(newJournalisation);
//            msg = JsfUtil.getBundleMsg("JournalisationCreateSuccessMsg");
//            JsfUtil.addSuccessMessage(msg);
//            prepareCreate();
//            listeJournalisations = journalisationFacade.findAll();
//        } catch (Exception e) {
//            msg = JsfUtil.getBundleMsg("JournalisationCreateErrorMsg");
//            JsfUtil.addErrorMessage(msg);
//        }
//    }

//    public void doEdit(ActionEvent event) {
//        String msg;
//        try {
//            journalisationFacade.edit(selectedJournalisation);
//            msg = JsfUtil.getBundleMsg("JournalisationEditSuccessMsg");
//            JsfUtil.addSuccessMessage(msg);
//            listeJournalisations = journalisationFacade.findAll();
//        } catch (Exception e) {
//            msg = JsfUtil.getBundleMsg("JournalisationEditErrorMsg");
//            JsfUtil.addErrorMessage(msg);
//        }
//    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            journalisationFacade.remove(selectedJournalisation);
            msg = JsfUtil.getBundleMsg("JournalisationDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeJournalisations = journalisationFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("JournalisationDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Journalisation getSelectedJournalisation() {
        return selectedJournalisation;
    }

    public void setSelectedJournalisation(Journalisation selectedJournalisation) {
        this.selectedJournalisation = selectedJournalisation;
    }

    public Journalisation getNewJournalisation() {
        return newJournalisation;
    }

    public void setNewJournalisation(Journalisation newJournalisation) {
        this.newJournalisation = newJournalisation;
    }

    public List<Journalisation> getListeJournalisations() {
        return listeJournalisations;
    }

    public void setListeJournalisations(List<Journalisation> listeJournalisations) {
        this.listeJournalisations = listeJournalisations;
    }

    public List<Journalisation> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Journalisation> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate(){
        newJournalisation  = new Journalisation();
    }
    
   
}
