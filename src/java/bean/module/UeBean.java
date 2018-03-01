/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.formation.FiliereFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.formation.Filiere;
import jpa.inscription.GroupePedagogique;
import jpa.module.Semestre;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "ueBean")
@ViewScoped
public class UeBean implements Serializable{
    @EJB
    private FiliereFacade filiereFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private SemestreFacade semestreFacade;
    
    private List<GroupePedagogique> listeGroupePedagogiques;
    private Ue newUe;
    private Ue selectedUe;
    private List<Ue> listeUes;
    private List<Ue> filteredList;
    private List<Semestre> listeSemestres;
    private GroupePedagogique groupePedagogique;
    private List<Filiere> listeFilieres;
    private Filiere filiere;
    /**
     * Creates a new instance of UeBean
     */
    public UeBean() {
    }
    @PostConstruct
    public void init(){
        prepareCreate();
    }
    
    public void initGroupePedagogique(){
        listeGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(filiere);
    }
    
    public void initSemestre() {
        listeSemestres = semestreFacade.getSemetreByGP(newUe.getGroupePedagogique());
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

    public List<GroupePedagogique> getListeGroupePedagogiques() {
        return listeGroupePedagogiques;
    }

    public void setListeGroupePedagogiques(List<GroupePedagogique> listeGroupePedagogiques) {
        this.listeGroupePedagogiques = listeGroupePedagogiques;
    }

    

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
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

    
    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Semestre> getListeSemestres() {
        return listeSemestres;
    }

    public void setListeSemestres(List<Semestre> listeSemestres) {
        this.listeSemestres = listeSemestres;
    }
    
    public void prepareCreate(){
        newUe = new Ue();
    }
    public void reset(){
        newUe.reset();
    }
    public void initListUE() {
        listeUes = ueFacade.getUeByGroupePedagogique(newUe.getGroupePedagogique(), newUe.getSemestre());
    }
    
}
