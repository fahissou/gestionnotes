/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import bean.inscription.NotesBean;
import ejb.formation.FiliereFacade;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
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
import jpa.formation.Filiere;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
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
    private SemestreFacade semestreFacade;
    @EJB
    private FiliereFacade filiereFacade;

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
    private AnneeAcademique anneeAcademique;
    private Notes newNote;
    private List<Semestre> listeSemestres;
    private Semestre semestre;
    private GroupePedagogique groupePedagogique;
    private List<GroupePedagogique> listeGroupePedagogiques;
    private List<Filiere> listeFilieres;
    private Filiere filiere;
    private List<Ue> listeUE;
    private Ue ue;
    /**
     * Creates a new instance of MatiereBean
     */
    public MatiereBean() {
    }

    @PostConstruct
    public void init() {
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
//        listeMatieres = matiereFacade.findAll();
        prepareCreate();
//        listeFilieres = filiereFacade.findAll();
        
    }
    
    public void initGroupePedagogique(){
        listeGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(filiere);
    }
    
    public void initSemestre() {
        listeSemestres = semestreFacade.getSemetreByGP(groupePedagogique);
    }
    
    public void initUe() {
        listeUE = ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre);
    }
    public void initListMatiere(){
        listeMatieres = matiereFacade.getMatiereByGroupe(groupePedagogique, semestre);
    }

    
    public UeFacade getUeFacade() {
        return ueFacade;
    }

    public void setUeFacade(UeFacade ueFacade) {
        this.ueFacade = ueFacade;
    }

    public List<Semestre> getListeSemestres() {
        return listeSemestres;
    }

    public void setListeSemestres(List<Semestre> listeSemestres) {
        this.listeSemestres = listeSemestres;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public List<GroupePedagogique> getListeGroupePedagogiques() {
        return listeGroupePedagogiques;
    }

    public void setListeGroupePedagogiques(List<GroupePedagogique> listeGroupePedagogiques) {
        this.listeGroupePedagogiques = listeGroupePedagogiques;
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
    

    public void doCreate(ActionEvent event) {
        String msg;
        List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique, anneeAcademique);
            
        try {
            newMatiere.setGroupePedagogique(groupePedagogique);
            newMatiere.setUe(ue);
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

   

    public List<Ue> getListeUE() {
        return listeUE;
    }

    public void setListeUE(List<Ue> listeUE) {
        this.listeUE = listeUE;
    }

    

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
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
