/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import bean.inscription.NotesBean;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
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
import jpa.inscription.AnneeAcademique;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "matiereBean")
@ViewScoped
public class MatiereBean implements Serializable {

    @EJB
    private NotesFacade notesFacade;
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB

    private MatiereFacade matiereFacade;
    private Matiere newMatiere;
    private Matiere selectedMatiere;
    private List<Matiere> listeMatieres;
    private List<Matiere> filteredList;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Ue> listeUE;
    private List<Ue> listeUEDynamic;
    private GroupePedagogique newGroupePedagogique;
    private Ue ue;
    private Map<GroupePedagogique, List<Ue>> data1; // = new HashMap<>();
    private AnneeAcademique anneeAcademique;
    private Notes newNote;
    private NotesBean notesbean;

    /**
     * Creates a new instance of MatiereBean
     */
    public MatiereBean() {
    }

    @PostConstruct
    public void init() {
        
        listGroupePedagogiques = groupePedagogiqueFacade.findAll();
        listeUE = ueFacade.findAll();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
        loadData();
        listeMatieres = matiereFacade.findAll();
        prepareCreate();

    }

    public void loadData() {
        data1 = new HashMap<>();
        for (int i = 0; i < listGroupePedagogiques.size(); i++) {
            List<Ue> list1 = ueFacade.getUeByGroupePedagogique(listGroupePedagogiques.get(i));
            data1.put(listGroupePedagogiques.get(i), list1);

        }

    }

    public void onGroupePedagogiqueChange() {
        if (newGroupePedagogique != null) {
            listeUEDynamic = data1.get(newGroupePedagogique);
        } else {
            listeUEDynamic = new ArrayList<>();
        }

    }

    public void doCreate(ActionEvent event) {
        String msg;
        List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(newGroupePedagogique, anneeAcademique);

        try {
            if (inscriptions.isEmpty()) {
                matiereFacade.create(newMatiere);
            }else{
                matiereFacade.create(newMatiere);
                createDiffaultNotes(inscriptions);
            }

            msg = JsfUtil.getBundleMsg("MatiereCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            matiereFacade.edit(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
             
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            matiereFacade.remove(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Matiere getNewMatiere() {
        return newMatiere;
    }

    public void setNewMatiere(Matiere newMatiere) {
        this.newMatiere = newMatiere;
    }

    public Matiere getSelectedMatiere() {
        return selectedMatiere;
    }

    public void setSelectedMatiere(Matiere selectedMatiere) {
        this.selectedMatiere = selectedMatiere;
    }

    public List<Matiere> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<Matiere> listeMatieres) {
        this.listeMatieres = listeMatieres;
    }

    public List<Matiere> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Matiere> filteredList) {
        this.filteredList = filteredList;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public List<Ue> getListeUE() {
        return listeUE;
    }

    public void setListeUE(List<Ue> listeUE) {
        this.listeUE = listeUE;
    }

    public GroupePedagogique getNewGroupePedagogique() {
        return newGroupePedagogique;
    }

    public void setNewGroupePedagogique(GroupePedagogique newGroupePedagogique) {
        this.newGroupePedagogique = newGroupePedagogique;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }

    public List<Ue> getListeUEDynamic() {
        return listeUEDynamic;
    }

    public void setListeUEDynamic(List<Ue> listeUEDynamic) {
        this.listeUEDynamic = listeUEDynamic;
    }

    public void prepareCreate() {
        newMatiere = new Matiere();
    }

    public void reset() {
        newMatiere.reset();
    }

    public Notes getNewNote() {
        return newNote;
    }

    public void setNewNote(Notes newNote) {
        this.newNote = newNote;
    }

    public void createDiffaultNotes(List<Inscription> listeInscription) {

        try {
            for (int i = 0; i < listeInscription.size(); i++) {
                newNote = new Notes();
                newNote.setMatiere(newMatiere);
                newNote.setInscription(listeInscription.get(i));
                newNote.setEtatValidation("UENV");
                newNote.setNote(0.0);
                notesFacade.create(newNote);
                notesFacade.findAll();
            }
        } catch (Exception ex) {

        }

    }

}
