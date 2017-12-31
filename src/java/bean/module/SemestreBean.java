/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.module.SemestreFacade;
import java.io.Serializable;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "semestreBean")
@ViewScoped
public class SemestreBean implements Serializable{

    @EJB
    private SemestreFacade semestreFacade;
    private Semestre newSemestre;
    private Semestre selectedSemestre;
    private List<Semestre> listeSemestres;
    private List<Semestre> filteredList;
    private List<String> defaultSemestres;
    public SemestreBean() {
    }
    @PostConstruct
    public void init(){
        
       listeSemestres = semestreFacade.findAll();
       prepareCreate();
       defaultSemestres = new ArrayList<>();
       defaultSemestres.add("1");
       defaultSemestres.add("2");
       defaultSemestres.add("3");
       defaultSemestres.add("4");
       defaultSemestres.add("5");
       defaultSemestres.add("6");
       defaultSemestres.add("7");
       defaultSemestres.add("8");
       defaultSemestres.add("9");
       defaultSemestres.add("10");
       
    }
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            
            semestreFacade.create(newSemestre);
            msg = JsfUtil.getBundleMsg("SemestreCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeSemestres = semestreFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SemestreCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            semestreFacade.edit(selectedSemestre);
            msg = JsfUtil.getBundleMsg("SemestreEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeSemestres = semestreFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SemestreEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doDel(ActionEvent event) {
        String msg;
        try {
            semestreFacade.remove(selectedSemestre);
            msg = JsfUtil.getBundleMsg("SemestreDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeSemestres = semestreFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SemestreDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Semestre getNewSemestre() {
        return newSemestre;
    }

    public void setNewSemestre(Semestre newSemestre) {
        this.newSemestre = newSemestre;
    }

    public Semestre getSelectedSemestre() {
        return selectedSemestre;
    }

    public void setSelectedSemestre(Semestre selectedSemestre) {
        this.selectedSemestre = selectedSemestre;
    }

    public List<Semestre> getListeSemestres() {
        return listeSemestres;
    }

    public void setListeSemestres(List<Semestre> listeSemestres) {
        this.listeSemestres = listeSemestres;
    }

    public List<Semestre> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Semestre> filteredList) {
        this.filteredList = filteredList;
    }

    public List<String> getDefaultSemestres() {
        return defaultSemestres;
    }

    public void setDefaultSemestres(List<String> defaultSemestres) {
        this.defaultSemestres = defaultSemestres;
    }

    
    public void prepareCreate(){
        newSemestre = new Semestre();
    }
    public void reset(){
        newSemestre.reset();
    }
    
}
