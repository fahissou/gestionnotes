
package bean.inscription;

import ejb.inscription.EnseignantFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.Enseignant;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "enseignant")
public class EnseignantBean implements Serializable{

    private EnseignantFacade enseignantFacade;
    private Enseignant selectedEnseignant;
    private Enseignant newEnseignant;
    private List<Enseignant> listeEnseignants;
    private List<Enseignant> filteredList;
    public EnseignantBean() {
    }
    
    @PostConstruct
    public void init() {
        listeEnseignants = enseignantFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.create(newEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.edit(selectedEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            enseignantFacade.remove(selectedEnseignant);
            msg = JsfUtil.getBundleMsg("EnseignantDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeEnseignants = enseignantFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("EnseignantDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Enseignant getSelectedEnseignant() {
        return selectedEnseignant;
    }

    public void setSelectedEnseignant(Enseignant selectedEnseignant) {
        this.selectedEnseignant = selectedEnseignant;
    }

    public Enseignant getNewEnseignant() {
        return newEnseignant;
    }

    public void setNewEnseignant(Enseignant newEnseignant) {
        this.newEnseignant = newEnseignant;
    }

    public List<Enseignant> getListeEnseignants() {
        return listeEnseignants;
    }

    public void setListeEnseignants(List<Enseignant> listeEnseignants) {
        this.listeEnseignants = listeEnseignants;
    }

    public List<Enseignant> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Enseignant> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newEnseignant = new Enseignant();
    }
    
    public void reset(ActionEvent e) {
        this.newEnseignant.reset();
    }
    
    
}
