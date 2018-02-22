/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.formation.FiliereFacade;
import ejb.inscription.EnseignantFacade;
import ejb.inscription.EnseignerFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.SpecialiteFacade;
import ejb.module.MatiereFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.formation.Filiere;
import jpa.inscription.Enseignant;
import jpa.inscription.Enseigner;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Specialite;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "enseignerBean")
public class EnseignerBean implements Serializable{
    @EJB
    private UeFacade ueFacade;
    @EJB
    private SpecialiteFacade specialiteFacade;
    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private FiliereFacade filiereFacade;
    @EJB
    private EnseignantFacade enseignantFacade;
    @EJB
    private EnseignerFacade enseignerFacade;
    
    private Enseigner selectedEnseigner;
    private Enseigner newEnseigner;
    private List<Enseigner> listeEnseigners;
    private List<Enseigner> filteredList;
    private List<Semestre> listeSemestres;
    private Semestre semestre;
    private GroupePedagogique groupePedagogique;
    private List<GroupePedagogique> listeGroupePedagogiques;
    private List<Filiere> listeFilieres;
    private Filiere filiere;
    private List<Ue> listeUE;
    private Ue ue;
    private Matiere matiere;
    private List<Matiere> listeMatieres;
    private List<Enseignant> listeEnseignants;
    private Specialite specialite;
    private List<Specialite> listeSpecialites;
    

    /**
     * Creates a new instance of EnseignerBean
     */
    public EnseignerBean() {
    }
    
    @PostConstruct
    public void init(){
        listeEnseigners = enseignerFacade.findAll();
        prepareCreate();
        
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
    
    public void initMatiere() {
        listeMatieres = matiereFacade.getMatiereByUe(ue);
    }
    
    public void initEnseignant() {
        listeEnseignants = enseignantFacade.findEnseignantBySpecialite(specialite);
    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.create(newEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.edit(selectedEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            enseignerFacade.remove(selectedEnseigner);
            msg = JsfUtil.getBundleMsg("EnseignerDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseigners = enseignerFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignerDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Enseigner getSelectedEnseigner() {
        return selectedEnseigner;
    }

    public void setSelectedEnseigner(Enseigner selectedEnseigner) {
        this.selectedEnseigner = selectedEnseigner;
    }

    public Enseigner getNewEnseigner() {
        return newEnseigner;
    }

    public void setNewEnseigner(Enseigner newEnseigner) {
        this.newEnseigner = newEnseigner;
    }

    public List<Enseigner> getListeEnseigners() {
        return listeEnseigners;
    }

    public void setListeEnseigners(List<Enseigner> listeEnseigners) {
        this.listeEnseigners = listeEnseigners;
    }

    public List<Enseigner> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Enseigner> filteredList) {
        this.filteredList = filteredList;
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

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public List<Matiere> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<Matiere> listeMatieres) {
        this.listeMatieres = listeMatieres;
    }

    public List<Enseignant> getListeEnseignants() {
        return listeEnseignants;
    }

    public void setListeEnseignants(List<Enseignant> listeEnseignants) {
        this.listeEnseignants = listeEnseignants;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public List<Specialite> getListeSpecialites() {
        return listeSpecialites;
    }

    public void setListeSpecialites(List<Specialite> listeSpecialites) {
        this.listeSpecialites = listeSpecialites;
    }

    
    public void prepareCreate(){
        newEnseigner = new Enseigner();
    }
    
}
