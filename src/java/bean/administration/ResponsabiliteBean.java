
package bean.administration;

import ejb.administration.ResponsabiliteFacade;
import ejb.formation.FiliereFacade;
import ejb.inscription.EnseignantFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import jpa.administration.Responsabilite;
import jpa.formation.Filiere;
import jpa.inscription.Enseignant;
import jpa.inscription.Specialite;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */

@Named(value = "responsabiliteBean")
@ViewScoped
public class ResponsabiliteBean implements Serializable{
    @EJB
    private FiliereFacade filiereFacade;
    @EJB
    private EnseignantFacade enseignantFacade;

    @EJB
    private ResponsabiliteFacade responsabiliteFacade;
    private Responsabilite selectedResponsabilite;
    private Responsabilite newResponsabilite;
    private List<Responsabilite> listeResponsabilites;
    private List<Responsabilite> filteredList;
    private Specialite specialite;
    private Enseignant enseignant;
    private List<Enseignant> listeEnseignants;
    private List<String> listeRoles;
    private List<Filiere> listeFilieres = null;
    private Filiere filiere = null;

    /**
     * Creates a new instance of ResponsabiliteBean
     */
    public ResponsabiliteBean() {
    }

    @PostConstruct
    public void init() {
        listeResponsabilites = responsabiliteFacade.findAll();
        listeRoles = JsfUtil.getResponsabilite();
        prepareCreate();
    }  

    public void initEnseignant() {
      listeEnseignants = enseignantFacade.findEnseignantBySpecialite(specialite);
    }
    
    public void initFiliere() {
          if(newResponsabilite.getRole().equals("RESPONSABLE DE FORMATION")){
              listeFilieres = filiereFacade.findAll();
          }else{
              listeFilieres = filiereFacade.findDefaultFiliere();
          }
    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            newResponsabilite.setEnseignant(enseignant);
            newResponsabilite.setFiliere(filiere);
            responsabiliteFacade.create(newResponsabilite);
            msg = JsfUtil.getBundleMsg("ResponsabiliteCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeResponsabilites = responsabiliteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ResponsabiliteCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            responsabiliteFacade.edit(selectedResponsabilite);
            msg = JsfUtil.getBundleMsg("ResponsabiliteEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeResponsabilites = responsabiliteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ResponsabiliteEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            responsabiliteFacade.remove(selectedResponsabilite);
            msg = JsfUtil.getBundleMsg("ResponsabiliteDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeResponsabilites = responsabiliteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ResponsabiliteDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Responsabilite getSelectedResponsabilite() {
        return selectedResponsabilite;
    }

    public void setSelectedResponsabilite(Responsabilite selectedResponsabilite) {
        this.selectedResponsabilite = selectedResponsabilite;
    }

    public Responsabilite getNewResponsabilite() {
        return newResponsabilite;
    }

    public void setNewResponsabilite(Responsabilite newResponsabilite) {
        this.newResponsabilite = newResponsabilite;
    }

    public List<Responsabilite> getListeResponsabilites() {
        return listeResponsabilites;
    }

    public void setListeResponsabilites(List<Responsabilite> listeResponsabilites) {
        this.listeResponsabilites = listeResponsabilites;
    }

    public List<Responsabilite> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Responsabilite> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newResponsabilite = new Responsabilite();
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }
    
    public void reset(ActionEvent e) {
        this.newResponsabilite.reset();
    }

    public List<Enseignant> getListeEnseignants() {
        return listeEnseignants;
    }

    public void setListeEnseignants(List<Enseignant> listeEnseignants) {
        this.listeEnseignants = listeEnseignants;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public List<String> getListeRoles() {
        return listeRoles;
    }

    public void setListeRoles(List<String> listeRoles) {
        this.listeRoles = listeRoles;
    }

    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
    }
    
    public String responsableFiliere(String idFiliere) {
        String responsable = "";
        try {
            Enseignant enseignantResp = responsabiliteFacade.getResponsableByFiliere(idFiliere);
            responsable = enseignantResp.getNom() + " " +enseignantResp.getPrenom();
        } catch (Exception e) {
        }
        return responsable;
    }
    
    
}
