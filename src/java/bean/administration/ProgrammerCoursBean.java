/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.ProgrammerCoursFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.MatiereFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.ProgrammerCours;
import jpa.formation.Filiere;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "programmerCoursBean")
public class ProgrammerCoursBean implements Serializable {

    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    private GroupePedagogique selectedGroupePedagogique;

    @EJB
    private MatiereFacade matiereFacade;
    private Matiere selectedMatiere;

    @EJB
    private ProgrammerCoursFacade programmerCoursFacade;
    private ProgrammerCours selectedProgrammerCours;
    private ProgrammerCours newProgrammerCours;
    private List<ProgrammerCours> listeProgrammerCourss;
    private List<ProgrammerCours> filteredList;
    private Filiere selectedFiliere;

    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Matiere> listMatieres;

    /**
     * Creates a new instance of ProgrammerCoursBean
     */
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
            newProgrammerCours.getMatiere().setEtat(1);
            matiereFacade.edit(newProgrammerCours.getMatiere());
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
            selectedProgrammerCours.getMatiere().setEtat(0);
            matiereFacade.edit(selectedProgrammerCours.getMatiere());
            programmerCoursFacade.remove(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            initList();
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

    public Filiere getSelectedFiliere() {
        return selectedFiliere;
    }

    public void setSelectedFiliere(Filiere selectedFiliere) {
        this.selectedFiliere = selectedFiliere;
    }

    public GroupePedagogique getSelectedGroupePedagogique() {
        return selectedGroupePedagogique;
    }

    public void setSelectedGroupePedagogique(GroupePedagogique selectedGroupePedagogique) {
        this.selectedGroupePedagogique = selectedGroupePedagogique;
    }

    public Matiere getSelectedMatiere() {
        return selectedMatiere;
    }

    public void setSelectedMatiere(Matiere selectedMatiere) {
        this.selectedMatiere = selectedMatiere;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public List<Matiere> getListMatieres() {
        return listMatieres;
    }

    public void setListMatieres(List<Matiere> listMatieres) {
        this.listMatieres = listMatieres;
    }

    public void prepareCreate() {
        this.newProgrammerCours = new ProgrammerCours();
        initList();
    }

    public void initList() {
        listGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(selectedFiliere);
        listMatieres = matiereFacade.getMatiereByGroupe(newProgrammerCours.getGroupePedagogique());
        listeProgrammerCourss = programmerCoursFacade.listeProgrammeByGroupe(newProgrammerCours.getGroupePedagogique());
    }

    public void reset(ActionEvent e) {
        this.newProgrammerCours.reset();
    }

//    public void validationDate(FacesContext context, UIComponent component, Object value) {
//        String validateDate = "composantDate";
//        UIInput composantDate = (UIInput) component.getAttributes().get(validateDate);
//        Date dateFin = (Date) value;
//        Date dateDebut = (Date)composantDate.getValue();
//        String msg;
//        try {
//            if (!JsfUtil.compareDate(dateDebut, dateFin)) {
//                ((UIInput) component).setValid(false);
//                msg = JsfUtil.getBundleMsg("InvalideDate");
//                JsfUtil.addErrorMessage(msg);
////                FacesMessage message = new FacesMessage(msg);
////                context.addMessage(component.getClientId(context), message);
//            }
//        } catch (Exception e) {
//        }
//    }

}
