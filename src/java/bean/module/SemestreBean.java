/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.module.SemestreFacade;
import java.io.Serializable;
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
public class SemestreBean implements Serializable {

    @EJB
    private SemestreFacade semestreFacade;

    private Semestre newSemestre;
    private Semestre selectedSemestre;
    private List<Semestre> listeSemestres;
    private List<Semestre> filteredList;
    private List<Integer> defaulteSemestres;
    private int valeurSemestre;

    public SemestreBean() {
    }

    @PostConstruct
    public void init() {

        listeSemestres = semestreFacade.findAll();
        prepareCreate();
        defaulteSemestres = JsfUtil.getListSemestre(11);
    }

    public void doCreate(ActionEvent event) {
        String msg;

        try {
            newSemestre.setValeur(valeurSemestre);
            String[] parametres = JsfUtil.getParametres(newSemestre.getValeur());
            int ordre = Integer.parseInt(parametres[0]);
            String libelle = parametres[1];
            if (ordre != -1) {
                newSemestre.setOrdre(ordre);
                newSemestre.setLibelle(libelle);
                semestreFacade.create(newSemestre);
                msg = JsfUtil.getBundleMsg("SemestreCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                prepareCreate();
                listeSemestres = semestreFacade.findAll();
            } else {
                msg = JsfUtil.getBundleMsg("SemestreCreateErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }

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

    public List<Integer> getDefaulteSemestres() {
        return defaulteSemestres;
    }

    public void setDefaulteSemestres(List<Integer> defaulteSemestres) {
        this.defaulteSemestres = defaulteSemestres;
    }

    public int getValeurSemestre() {
        return valeurSemestre;
    }

    public void setValeurSemestre(int valeurSemestre) {
        this.valeurSemestre = valeurSemestre;
    }

    public void prepareCreate() {
        newSemestre = new Semestre();
    }

    public void reset() {
        newSemestre.reset();
    }

}
