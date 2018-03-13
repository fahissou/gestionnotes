/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.etats;

import bean.inscription.AnneeAcademiqueBean;
import bean.util.ParametragesBean;
import ejb.inscription.EtudiantFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.SemestreFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Semestre;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "rechercheAvanceeBean")
@ViewScoped
public class RechercheAvanceeBean implements Serializable{
    @EJB
    private EtudiantFacade etudiantFacade;
    @EJB
    private NotesFacade notesFacade;
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    
    private Etudiant etudiant = new Etudiant();
    private Semestre semestre;
    private List<Semestre> listSemestres;
    private Inscription inscription;
    private List<Inscription> listInscriptions;
    private List<Notes> listNotes;
    private List<Notes> filteredList;
    private Notes selectedNotes;
    private String notification;
    private String color;
    
    public RechercheAvanceeBean() {
    }
    
    @PostConstruct
    public void init() {
        String id = ParametragesBean.getIdEtudiant();
        initListeInscription(id);
    }

    public void initListeInscription(String id) {
        etudiant = etudiantFacade.find(id);
        if(etudiant != null){
           listInscriptions = inscriptionFacade.getListInscriptionByEtudiant(etudiant); 
           notification = ""; color ="white";
        }else{
           listInscriptions = null;
           notification = "Le numéro matricule entré est incorrect veuillez vérifier et réessayer !"; color ="red";
        }
    }
    
    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public List<Semestre> getListSemestres() {
        return listSemestres;
    }

    public void setListSemestres(List<Semestre> listSemestres) {
        this.listSemestres = listSemestres;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    

    public List<Inscription> getListInscriptions() {
        return listInscriptions;
    }

    public void setListInscriptions(List<Inscription> listInscriptions) {
        this.listInscriptions = listInscriptions;
    }

    public List<Notes> getListNotes() {
        return listNotes;
    }

    public void setListNotes(List<Notes> listNotes) {
        this.listNotes = listNotes;
    }

   public void initSemestre(){
       listSemestres = semestreFacade.getSemetreByGP(inscription.getGroupePedagogique());
   }
     
   public void updatetable() {
       listNotes = notesFacade.listeNoteByInscriptionBySem(inscription, semestre);
   }

    public List<Notes> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Notes> filteredList) {
        this.filteredList = filteredList;
    }

    public Notes getSelectedNotes() {
        return selectedNotes;
    }

    public void setSelectedNotes(Notes selectedNotes) {
        this.selectedNotes = selectedNotes;
    }
   
   
}
