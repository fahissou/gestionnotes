/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.administration.AnneeAcademiqueFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.administration.AnneeAcademique;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "anneeAcademiqueBean")
public class AnneeAcademiqueBean implements Serializable{

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    private AnneeAcademique selectedAnneeAcademique;
    private AnneeAcademique newAnneeAcademique;
    private List<AnneeAcademique> listeAnneeAcademiques;
    private List<AnneeAcademique> filteredList;
    private List<String> listeAnneeUniversitaire;
    private String anneeAcademique;

    public AnneeAcademiqueBean() {
    }

    @PostConstruct
    public void init() {
        listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        prepareCreate();

        listeAnneeUniversitaire = new ArrayList<>();

        listeAnneeUniversitaire.add("2014 - 2015");
        listeAnneeUniversitaire.add("2015 - 2016");
        listeAnneeUniversitaire.add("2016 - 2017");
        listeAnneeUniversitaire.add("2017 - 2018");
        listeAnneeUniversitaire.add("2018 - 2019");
        listeAnneeUniversitaire.add("2019 - 2020");
        listeAnneeUniversitaire.add("2020 - 2021");
        listeAnneeUniversitaire.add("2021 - 2022");
        listeAnneeUniversitaire.add("2022 - 2023");
        listeAnneeUniversitaire.add("2023 - 2024");
        listeAnneeUniversitaire.add("2024 - 2025");
        listeAnneeUniversitaire.add("2025 - 2026");
        listeAnneeUniversitaire.add("2026 - 2027");
        listeAnneeUniversitaire.add("2027 - 2028");
        listeAnneeUniversitaire.add("2028 - 2029");
        listeAnneeUniversitaire.add("2029 - 2030");
        listeAnneeUniversitaire.add("2030 - 2031");
        listeAnneeUniversitaire.add("2031 - 2032");
        listeAnneeUniversitaire.add("2032 - 2033");
        listeAnneeUniversitaire.add("2033 - 2034");
        listeAnneeUniversitaire.add("2034 - 2035");
        listeAnneeUniversitaire.add("2035 - 2036");
        listeAnneeUniversitaire.add("2036 - 2037");
        listeAnneeUniversitaire.add("2037 - 2038");
        listeAnneeUniversitaire.add("2038 - 2039");
        listeAnneeUniversitaire.add("2040 - 2040");


    }

    public void doCreate(ActionEvent event) {
        String msg;
        try {

            AnneeAcademique currentAcademicYear = anneeAcademiqueFacade.getCurrentAcademicYear();
            if (JsfUtil.valideAcademicYear(currentAcademicYear.getDescription(), anneeAcademique) == 1) {
                currentAcademicYear.setEtat(0);
                anneeAcademiqueFacade.edit(currentAcademicYear);
                newAnneeAcademique.setDescription(anneeAcademique);
                newAnneeAcademique.setEtat(1);
                anneeAcademiqueFacade.create(newAnneeAcademique);
                prepareCreate();
                listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
                msg = JsfUtil.getBundleMsg("AnneeAcademiqueCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                
            } else {
                msg = JsfUtil.getBundleMsg("AnneeAcademiqueCreateErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            anneeAcademiqueFacade.edit(selectedAnneeAcademique);
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            anneeAcademiqueFacade.remove(selectedAnneeAcademique);
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public AnneeAcademique getSelectedAnneeAcademique() {
        return selectedAnneeAcademique;
    }

    public void setSelectedAnneeAcademique(AnneeAcademique selectedAnneeAcademique) {
        this.selectedAnneeAcademique = selectedAnneeAcademique;
    }

    public AnneeAcademique getNewAnneeAcademique() {
        return newAnneeAcademique;
    }

    public void setNewAnneeAcademique(AnneeAcademique newAnneeAcademique) {
        this.newAnneeAcademique = newAnneeAcademique;
    }

    public List<AnneeAcademique> getListeAnneeAcademiques() {
        return listeAnneeAcademiques;
    }

    public void setListeAnneeAcademiques(List<AnneeAcademique> listeAnneeAcademiques) {
        this.listeAnneeAcademiques = listeAnneeAcademiques;
    }

    public List<AnneeAcademique> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<AnneeAcademique> filteredList) {
        this.filteredList = filteredList;
    }

    public void prepareCreate() {
        this.newAnneeAcademique = new AnneeAcademique();
    }

    public void reset(ActionEvent e) {
        this.newAnneeAcademique.reset();
    }

    public List<String> getListeAnneeUniversitaire() {
        return listeAnneeUniversitaire;
    }

    public void setListeAnneeUniversitaire(List<String> listeAnneeUniversitaire) {
        this.listeAnneeUniversitaire = listeAnneeUniversitaire;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

}
