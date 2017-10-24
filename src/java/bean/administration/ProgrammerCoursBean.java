/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.ProgrammerCoursFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.administration.ProgrammerCours;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "programmerCoursBean")
public class ProgrammerCoursBean implements Serializable{
private ProgrammerCoursFacade programmerCoursFacade;
    private ProgrammerCours selectedProgrammerCours;
    private ProgrammerCours newProgrammerCours;
    private List<ProgrammerCours> listeProgrammerCourss;
    private List<ProgrammerCours> filteredList;
    public ProgrammerCoursBean() {
    }
    
    @PostConstruct
    public void init() {
        listeProgrammerCourss = programmerCoursFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            programmerCoursFacade.create(newProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeProgrammerCourss = programmerCoursFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            programmerCoursFacade.edit(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeProgrammerCourss = programmerCoursFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            programmerCoursFacade.remove(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeProgrammerCourss = programmerCoursFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public ProgrammerCours getSelectedProgrammerCours() {
        return selectedProgrammerCours;
    }

    public void setSelectedProgrammerCours(ProgrammerCours selectedProgrammerCours) {
        this.selectedProgrammerCours = selectedProgrammerCours;
    }

    public ProgrammerCours getNewProgrammerCours() {
        return newProgrammerCours;
    }

    public void setNewProgrammerCours(ProgrammerCours newProgrammerCours) {
        this.newProgrammerCours = newProgrammerCours;
    }

    public List<ProgrammerCours> getListeProgrammerCourss() {
        return listeProgrammerCourss;
    }

    public void setListeProgrammerCourss(List<ProgrammerCours> listeProgrammerCourss) {
        this.listeProgrammerCourss = listeProgrammerCourss;
    }

    public List<ProgrammerCours> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<ProgrammerCours> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newProgrammerCours = new ProgrammerCours();
    }
    
    public void reset(ActionEvent e) {
        this.newProgrammerCours.reset();
    }
    
}
