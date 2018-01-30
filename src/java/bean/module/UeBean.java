/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import bean.inscription.NotesBean;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
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
    private List<String> listesemestres;
    private List<Semestre> listeSemestre;
    private Map<GroupePedagogique, List<Semestre>> data1;
    private GroupePedagogique groupePedagogique;
    private List<Semestre> semestres;
    private NotesBean notesbean;
    /**
     * Creates a new instance of UeBean
     */
    public UeBean() {
    }
    @PostConstruct
    public void init(){
        prepareCreate();
        listeUes = ueFacade.findAll();
        listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
        listeSemestre = semestreFacade.findAll();
        loadData();
        listesemestres = new ArrayList<>();
        listesemestres.add("1");
        listesemestres.add("2");
        listesemestres.add("3");
        listesemestres.add("4");
        listesemestres.add("5");
        listesemestres.add("6");
        listesemestres.add("7");
        listesemestres.add("8");
        listesemestres.add("9");
        listesemestres.add("10");
    }
    
    
    
    public void loadData() {
        data1 = new HashMap<>();
        for (int i = 0; i < listeGroupePedagogiques.size(); i++) {
            List<Semestre> liste = semestreFacade.getSemetreByGP(listeGroupePedagogiques.get(i));
            data1.put(listeGroupePedagogiques.get(i), liste);

        }
    }
    
    public void onGroupePedagogiqueChange() {
        if (groupePedagogique != null) {
            semestres = data1.get(groupePedagogique);
        } else {
            semestres = new ArrayList<>();
        }

    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            newUe.setGroupePedagogique(groupePedagogique);
            
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

    public List<Semestre> getListeSemestre() {
        return listeSemestre;
    }

    public void setListeSemestre(List<Semestre> listeSemestre) {
        this.listeSemestre = listeSemestre;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public List<Semestre> getSemestres() {
        return semestres;
    }

    public void setSemestres(List<Semestre> semestres) {
        this.semestres = semestres;
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

    public List<String> getListesemestres() {
        return listesemestres;
    }

    public void setListesemestres(List<String> listesemestres) {
        this.listesemestres = listesemestres;
    }
    
    public void prepareCreate(){
        newUe = new Ue();
    }
    public void reset(){
        newUe.reset();
    }
    
}
