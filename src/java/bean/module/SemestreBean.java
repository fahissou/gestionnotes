/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.module.SemestreFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "semestreBean")
@ManagedBean
@ViewScoped
public class SemestreBean implements Serializable{
    @EJB
    private SemestreFacade semestreFacade;
    private Semestre newSemestre;
    private Semestre selectedSemestre;
    private List<Semestre> listeSemestres;
    private List<Semestre> filteredList;
    
    protected static final SimpleDateFormat formatCode = new SimpleDateFormat("ddMMyyyyHHmmss");
    protected static final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Creates a new instance of SemestreBean
     */
    public SemestreBean() {
    }
    @PostConstruct
    public void init(){
        listeSemestres = semestreFacade.findAll();
        prepareCreate();
    }
    public String createCode(){
        return formatCode.format(new Date());
    }
    public String recupDateSysteme() {
        return formatDate.format(new Date());
    }
    public void code(){
        this.newSemestre.setId(createCode());
    }
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            code();
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
    
    public void prepareCreate(){
        newSemestre = new Semestre();
    }
    public void reset(){
        newSemestre.reset();
    }
}
