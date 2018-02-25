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
import org.primefaces.context.RequestContext;
import util.JsfUtil;

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
    private String notification;
    private String idEtudiant;

    public ReinscriptionBean() {
    }

    @PostConstruct
    public void init() {
        listeFilieres = filiereFacade.findAll();
        anneeAcademic = anneeAcademiqueFacade.getCurrentAcademicYear();
        listValidation = new ArrayList<>();
        listValidation.add("SÉLÉCTIONNER");
        listValidation.add("VALIDÉE");
        listValidation.add("NON VALIDÉE");
    }

    public void initGroupePedagogique() {
        listeGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(filiere);
    }

    public void updateDataTable() {
        notification = "";
        GroupePedagogique gp = groupePedagogiqueFacade.getPrevGroupePedagogique(groupePedagogique, filiere);
        if (groupePedagogique.getOrdre() == 0 || groupePedagogique.getOrdre() == 2) {
            listeInscriptions = getRealListResinscription(initValidation(inscriptionFacade.listeReinscription(gp, groupePedagogique, "R", "R", "R")));
//            notification = "Veuillez faire une réinscription particulière";
        } else {
            listeInscriptions = getRealListResinscription(initValidation(inscriptionFacade.listeReinscription(gp, groupePedagogique, "T", "A", "R")));
        }
        if (listeInscriptions.isEmpty()) {
            notification = "Créer une nouvelle année académique ou vérifier si les étudiants sont enregistrés pour la filière ou bien les étudiants sont réinscrits !";
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

    public String getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(String idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public void reinscription() {
        
        Inscription newInscription = null;
        Inscription oldInscription = null;
        if (!listeInscriptions.isEmpty()) {
            System.out.println("ok rein");
            for (int i = 0; i < listeInscriptions.size(); i++) {
                oldInscription = listeInscriptions.get(i);
                if (oldInscription.getValidation().equals("VALIDÉE")) {
                    System.out.println("ok rein validé");
                    if ("R".equals(oldInscription.getResultat())) {
                        oldInscription.setSessions(anneeAcademic);
                        oldInscription.setValidation("V");
                        inscriptionFacade.edit(oldInscription);
                    } else if ("A".equals(oldInscription.getResultat())) {
                        oldInscription.setSessions(anneeAcademic);
                        oldInscription.setValidation("V");
                        inscriptionFacade.edit(oldInscription);
                        // nouvelle inscription
                        newInscription = new Inscription();
                        newInscription.setAnneeAcademique(anneeAcademic);
                        newInscription.setCompteurCredit(0);
                        newInscription.setEtudiant(oldInscription.getEtudiant());
                        newInscription.setGroupePedagogique(groupePedagogique);
                        newInscription.setResultat("R");
                        newInscription.setValidation("V");
                        inscriptionFacade.create(newInscription);
                        createDiffaultNotes(newInscription, groupePedagogique);
                    }else if("T".equals(oldInscription.getResultat())){
                        oldInscription.setSessions(oldInscription.getAnneeAcademique());
                        // nouvelle inscription
                        newInscription = new Inscription();
                        newInscription.setAnneeAcademique(anneeAcademic);
                        newInscription.setCompteurCredit(0);
                        newInscription.setEtudiant(oldInscription.getEtudiant());
                        newInscription.setGroupePedagogique(groupePedagogique);
                        newInscription.setResultat("R");
                        newInscription.setValidation("V");
                        inscriptionFacade.create(newInscription);
                        createDiffaultNotes(newInscription, groupePedagogique);
                    }
                    
                }

            }
        }
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/inscription/reinscriptions/'");
       
    }
    
    public List<Inscription> getRealListResinscription(List<Inscription> listeEntree) {
        List<Inscription> listeSortie = new ArrayList<>();
        for (int i = 0; i < listeEntree.size(); i++) {
            String idNewInscription = listeEntree.get(i).getEtudiant().getLogin()+"_"+anneeAcademic.getDescription();
            if(inscriptionFacade.find(idNewInscription) == null && listeEntree.get(i).getSessions() == null) {
                listeSortie.add(listeEntree.get(i));
            }
        }
        return listeSortie;
    }

    public void updateOldInscription(Inscription inscription) {

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
    
    public void initNotification() {
        System.out.println("idEtu "+idEtudiant);
        etudiant = etudiantFacade.find(this.idEtudiant);
        Inscription inscription2 = getInscriptionNonValide(etudiant);
        notification = "qdqdqsqd";
        if(inscription2 != null) {
            notification = "cet étudiant n'a pas fini sa formation en "+inscription2.getGroupePedagogique().getDescription()+" pour l'année académique "
                    + "    "+inscription2.getAnneeAcademique().getDescription()+" !";
        }
    }

    public void reinscriptionParticuliere() {
        etudiant = etudiantFacade.find(idEtudiant);
        String msg;
        Inscription inscription = null;
        try {
                inscription = new Inscription();
                inscription.setAnneeAcademique(anneeAcademic);
                inscription.setGroupePedagogique(groupePedagogique);
                inscription.setEtudiant(etudiant);
                inscription.setCompteurCredit(0);
                inscription.setValidation("V");
                inscription.setResultat("R");
                inscriptionFacade.create(inscription);
                createDiffaultNotes(inscription, groupePedagogique);
                listeInscriptions = inscriptionFacade.findAll();
                msg = JsfUtil.getBundleMsg("InscriptionDelSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            
        } catch (Exception e) {
               msg = JsfUtil.getBundleMsg("InscriptionEditErrorMsg");
               JsfUtil.addErrorMessage(msg);
        }
       
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/inscription/reinscriptions/'");
    }
    
    public Inscription getInscriptionNonValide(Etudiant etudiant) {
            Inscription inscription1 = null;
        try {
             List<Inscription> listeInscr = inscriptionFacade.getListInscriptionByEtudiant(etudiant);
             if(!listeInscr.isEmpty()){
                 for (int i = 0; i < listeInscr.size(); i++) {
                     if(!listeInscr.get(i).getResultat().equals("T")) {
                         inscription1 = listeInscr.get(i);
                         break;
                     }
                 }
             }
             
        } catch (Exception e) {
            
        }
        return inscription1;
    } 

}
