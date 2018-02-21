/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.formation.FiliereFacade;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.inscription.EtudiantFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import jpa.formation.Filiere;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Ue;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "reinscriptionBean")
@javax.faces.view.ViewScoped
public class ReinscriptionBean implements Serializable {

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private NotesFacade notesFacade;
    @EJB
    private MatiereFacade matiereFacade;

    @EJB
    private InscriptionFacade inscriptionFacade;
    @EJB
    private EtudiantFacade etudiantFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private FiliereFacade filiereFacade;

    private Filiere filiere;
    private List<Filiere> listeFilieres;
    private GroupePedagogique groupePedagogique;
    private List<GroupePedagogique> listeGroupePedagogiques;
    private Etudiant etudiant;
    private List<Inscription> listeInscriptions;
    private List<Inscription> filteredList;
    private Inscription selectedInscription;
    private List<String> listValidation;
    private AnneeAcademique anneeAcademic;
    private String notification ;

    public ReinscriptionBean() {
    }

    @PostConstruct
    public void init() {
        listeFilieres = filiereFacade.findAll();
        anneeAcademic = anneeAcademiqueFacade.getCurrentAcademicYear();
        listValidation = new ArrayList<>();
        listValidation.add("Séléctionner");
        listValidation.add("Validé");
        listValidation.add("Non Validé");
    }

    public void initGroupePedagogique() {
        listeGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(filiere);
    }

    public void updateDataTable() {
        notification = "";
        if(groupePedagogique.getOrdre() == 2){
            notification = "Veuillez faire une réinscription particulière";
        }else{
            GroupePedagogique gp = groupePedagogiqueFacade.getPrevGroupePedagogique(groupePedagogique, filiere);
            if (!gp.getId().equals(groupePedagogique.getId())) {
            listeInscriptions = initValidation(inscriptionFacade.listeReinscription(gp, groupePedagogique, "A", "R"));
        } else {
            listeInscriptions = initValidation(inscriptionFacade.listeReinscription(gp, groupePedagogique, "R", "R"));
        }
    }
      
        if(listeInscriptions.isEmpty()){
            notification = "Vous devez créer une nouvelle année académique";
        }
    }

    public List<Inscription> initValidation(List<Inscription> liste) {
        for (int i = 0; i < liste.size(); i++) {
            liste.get(i).setValidation("");
        }
        return liste;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
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

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public List<Inscription> getListeInscriptions() {
        return listeInscriptions;
    }

    public void setListeInscriptions(List<Inscription> listeInscriptions) {
        this.listeInscriptions = listeInscriptions;
    }

    public List<Inscription> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Inscription> filteredList) {
        this.filteredList = filteredList;
    }

    public Inscription getSelectedInscription() {
        return selectedInscription;
    }

    public void setSelectedInscription(Inscription selectedInscription) {
        this.selectedInscription = selectedInscription;
    }

    public List<String> getListValidation() {
        return listValidation;
    }

    public void setListValidation(List<String> listValidation) {
        this.listValidation = listValidation;
    }

    public void reinscription() {
        Inscription newInscription = null;
        if (!listeInscriptions.isEmpty()) {
            for (int i = 0; i < listeInscriptions.size(); i++) {
                if (listeInscriptions.get(i).getValidation().equals("Validé")) {
                    newInscription = new Inscription();
                    newInscription.setAnneeAcademique(anneeAcademic);
                    newInscription.setCompteurCredit(0);
                    newInscription.setEtudiant(listeInscriptions.get(i).getEtudiant());
                    newInscription.setGroupePedagogique(groupePedagogique);
                    newInscription.setResultat("R");
                    newInscription.setValidation("V");
                    inscriptionFacade.create(newInscription);
                    createDiffaultNotes(newInscription, groupePedagogique);
                }

            }
        } 

    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public void createDiffaultNotes(Inscription inscription, GroupePedagogique groupePedagogique1) {
        List<Ue> listeUe = ueFacade.getUeByGroupePedagogique(groupePedagogique1);
        Notes newNote = null;
        try {
            if (!listeUe.isEmpty()) {
                for (int i = 0; i < listeUe.size(); i++) {
                    List<Matiere> matieres = matiereFacade.getMatiereByUe(listeUe.get(i));
                    newNote = new Notes();
                    for (int j = 0; j < matieres.size(); j++) {
                        newNote.setMatiere(matieres.get(j));
                        newNote.setInscription(inscription);
                        newNote.setEtatValidation("UENV");
                        newNote.setNote(0.0);
                        notesFacade.create(newNote);
                    }

                }
            }

        } catch (Exception ex) {

        }

    }
    
    public List<Inscription> getRealInscription( List<Inscription> listeInscriptions) {
        List<Inscription> inscrip = new ArrayList<>();
        for (int i = 0; i < listeInscriptions.size(); i++) {
            if(!inscriptionFacade.isInscriptionExist(listeInscriptions.get(i),anneeAcademic)){
                inscrip.add(listeInscriptions.get(i));
            }
        }
        return inscrip;
    }

}
