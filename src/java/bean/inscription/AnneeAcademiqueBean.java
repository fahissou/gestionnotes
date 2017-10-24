
package bean.inscription;

import ejb.inscription.AnneeAcademiqueFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.AnneeAcademique;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ViewScoped
@Named(value = "anneeAcademiqueBean")
public class AnneeAcademiqueBean implements Serializable{

    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    private AnneeAcademique selectedAnneeAcademique;
    private AnneeAcademique newAnneeAcademique;
    private List<AnneeAcademique> listeAnneeAcademiques;
    private List<AnneeAcademique> filteredList;
    public AnneeAcademiqueBean() {
    }
    
    @PostConstruct
    public void init() {
        listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            anneeAcademiqueFacade.create(newAnneeAcademique);
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            anneeAcademiqueFacade.edit(selectedAnneeAcademique);
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            anneeAcademiqueFacade.remove(selectedAnneeAcademique);
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public AnneeAcademique getSelectedAnneeAcademique() {
        return selectedAnneeAcademique;
    }

    public void setSelectedAnneeAcademique(AnneeAcademique selectedAnneeAcademique) {
        this.selectedAnneeAcademique = selectedAnneeAcademique;
    }

    public AnneeAcademique getNewAnneeAcademique() {
        return newAnneeAcademique;
    }

    public void setNewAnneeAcademique(AnneeAcademique newAnneeAcademique) {
        this.newAnneeAcademique = newAnneeAcademique;
    }

    public List<AnneeAcademique> getListeAnneeAcademiques() {
        return listeAnneeAcademiques;
    }

    public void setListeAnneeAcademiques(List<AnneeAcademique> listeAnneeAcademiques) {
        this.listeAnneeAcademiques = listeAnneeAcademiques;
    }

    public List<AnneeAcademique> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<AnneeAcademique> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newAnneeAcademique = new AnneeAcademique();
    }
    
    public void reset(ActionEvent e) {
        this.newAnneeAcademique.reset();
    }
    
}
