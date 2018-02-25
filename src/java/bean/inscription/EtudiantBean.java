
package bean.inscription;

import ejb.inscription.EtudiantFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import jpa.formation.Filiere;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "etudiantBean")
public class EtudiantBean implements Serializable{
    @EJB
    private InscriptionFacade inscriptionFacade;
    private List <Inscription> listeInscriptionByGroupePedagogigue;
    
    
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    private List<GroupePedagogique> listGroupePedagogiques;
    @EJB
    private EtudiantFacade etudiantFacade;
    
    private Etudiant selectedEtudiant;
    private Etudiant newEtudiant;
    private List<Etudiant> listeEtudiants;
    private List<Inscription> listeEtudiantsInscris;
    private List<Etudiant> filteredList;
//    String descriptionGroupePedagogique;
    private Map<GroupePedagogique, List<Inscription>> data1; // = new HashMap<>();
    private List<GroupePedagogique> groupePedagogiques;
    private GroupePedagogique groupePedagogique;
    private Filiere selectedFiliere;
    private GroupePedagogique selectedGroupePedagogique;
    public EtudiantBean() {
    }
    
    @PostConstruct
    public void init() {
        prepareCreate();
    }  
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.create(newEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.edit(selectedEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            etudiantFacade.remove(selectedEtudiant);
            msg = JsfUtil.getBundleMsg("EtudiantDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEtudiants = etudiantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EtudiantDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Etudiant getSelectedEtudiant() {
        return selectedEtudiant;
    }

    public void setSelectedEtudiant(Etudiant selectedEtudiant) {
        this.selectedEtudiant = selectedEtudiant;
    }

    public Etudiant getNewEtudiant() {
        return newEtudiant;
    }

    public void setNewEtudiant(Etudiant newEtudiant) {
        this.newEtudiant = newEtudiant;
    }

    public List<Etudiant> getListeEtudiants() {
        return listeEtudiants;
    }

    public void setListeEtudiants(List<Etudiant> listeEtudiants) {
        this.listeEtudiants = listeEtudiants;
    }

    public List<Etudiant> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Etudiant> filteredList) {
        this.filteredList = filteredList;
    }            

    public List<Inscription> getListeEtudiantsInscris() {
        return listeEtudiantsInscris;
    }

    public void setListeEtudiantsInscris(List<Inscription> listeEtudiantsInscris) {
        this.listeEtudiantsInscris = listeEtudiantsInscris;
    }

//    public String getDescriptionGroupePedagogique() {
//        return descriptionGroupePedagogique;
//    }
//
//    public void setDescriptionGroupePedagogique(String descriptionGroupePedagogique) {
//        this.descriptionGroupePedagogique = descriptionGroupePedagogique;
//    }
//    
    public void prepareCreate() {
        this.newEtudiant = new Etudiant();
        initList();
    }
    
    public void initList(){
        listGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(selectedFiliere);
        listeInscriptionByGroupePedagogigue = inscriptionFacade.findListEtudiant(selectedGroupePedagogique);
        listeEtudiantByIncription();
    }
     public void listeEtudiantByIncription(){
         listeEtudiants = new ArrayList<>();
        for (Inscription tmp : listeInscriptionByGroupePedagogigue) {            
            listeEtudiants.add(tmp.getEtudiant());
        }
     }
    
    public void reset(ActionEvent e) {
        this.newEtudiant.reset();
    }
    

    public String redirect() {
        return "listEtuGrpePeda";
    }
    
    public String recupDescriptionGroupe() {
        HttpServletRequest params = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
        String nomDossier = params.getParameter("description");
        return nomDossier;
    }

    public List<GroupePedagogique> getGroupePedagogiques() {
        return groupePedagogiques;
    }

    public void setGroupePedagogiques(List<GroupePedagogique> groupePedagogiques) {
        this.groupePedagogiques = groupePedagogiques;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public List<Inscription> getListeInscriptionByGroupePedagogigue() {
        return listeInscriptionByGroupePedagogigue;
    }

    public void setListeInscriptionByGroupePedagogigue(List<Inscription> listeInscriptionByGroupePedagogigue) {
        this.listeInscriptionByGroupePedagogigue = listeInscriptionByGroupePedagogigue;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
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
    

    
}
