
package bean.inscription;

import ejb.inscription.SpecialiteFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import jpa.inscription.Specialite;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */

@Named(value = "specialiteBean")
@ViewScoped
public class SpecialiteBean implements Serializable{

    @EJB
    private SpecialiteFacade specialiteFacade;
    private Specialite selectedSpecialite;
    private Specialite newSpecialite;
    private List<Specialite> listeSpecialites;
    private List<Specialite> filteredList;

    /**
     * Creates a new instance of SpecialiteBean
     */
    public SpecialiteBean() {
    }

    @PostConstruct
    public void init() {
        listeSpecialites = specialiteFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            specialiteFacade.create(newSpecialite);
            msg = JsfUtil.getBundleMsg("SpecialiteCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeSpecialites = specialiteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SpecialiteCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            specialiteFacade.edit(selectedSpecialite);
            msg = JsfUtil.getBundleMsg("SpecialiteEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeSpecialites = specialiteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SpecialiteEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            specialiteFacade.remove(selectedSpecialite);
            msg = JsfUtil.getBundleMsg("SpecialiteDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeSpecialites = specialiteFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("SpecialiteDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Specialite getSelectedSpecialite() {
        return selectedSpecialite;
    }

    public void setSelectedSpecialite(Specialite selectedSpecialite) {
        this.selectedSpecialite = selectedSpecialite;
    }

    public Specialite getNewSpecialite() {
        return newSpecialite;
    }

    public void setNewSpecialite(Specialite newSpecialite) {
        this.newSpecialite = newSpecialite;
    }

    public List<Specialite> getListeSpecialites() {
        return listeSpecialites;
    }

    public void setListeSpecialites(List<Specialite> listeSpecialites) {
        this.listeSpecialites = listeSpecialites;
    }

    public List<Specialite> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Specialite> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newSpecialite = new Specialite();
    }
    
    public void reset(ActionEvent e) {
        this.newSpecialite.reset();
    }
    
    
}
