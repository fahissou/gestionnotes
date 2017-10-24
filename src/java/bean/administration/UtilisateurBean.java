/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.UtilisateurFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Utilisateur;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "utilisateurBean")
public class UtilisateurBean implements Serializable{

    private UtilisateurFacade utilisateurFacade;
    private Utilisateur selectedUtilisateur;
    private Utilisateur newUtilisateur;
    private List<Utilisateur> listeUtilisateurs;
    private List<Utilisateur> filteredList;
    public UtilisateurBean() {
    }
    
    @PostConstruct
    public void init() {
        listeUtilisateurs = utilisateurFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            utilisateurFacade.create(newUtilisateur);
            msg = JsfUtil.getBundleMsg("UtilisateurCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeUtilisateurs = utilisateurFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            utilisateurFacade.edit(selectedUtilisateur);
            msg = JsfUtil.getBundleMsg("UtilisateurEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeUtilisateurs = utilisateurFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UtilisateurEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            utilisateurFacade.remove(selectedUtilisateur);
            msg = JsfUtil.getBundleMsg("UtilisateurDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeUtilisateurs = utilisateurFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("UtilisateurDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Utilisateur getSelectedUtilisateur() {
        return selectedUtilisateur;
    }

    public void setSelectedUtilisateur(Utilisateur selectedUtilisateur) {
        this.selectedUtilisateur = selectedUtilisateur;
    }

    public Utilisateur getNewUtilisateur() {
        return newUtilisateur;
    }

    public void setNewUtilisateur(Utilisateur newUtilisateur) {
        this.newUtilisateur = newUtilisateur;
    }

    public List<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void setListeUtilisateurs(List<Utilisateur> listeUtilisateurs) {
        this.listeUtilisateurs = listeUtilisateurs;
    }

    public List<Utilisateur> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Utilisateur> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newUtilisateur = new Utilisateur();
    }
    
    public void reset(ActionEvent e) {
        this.newUtilisateur.reset();
    }
    
}
