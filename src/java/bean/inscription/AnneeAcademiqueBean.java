
package bean.inscription;
import ejb.inscription.AnneeAcademiqueFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import jpa.inscription.AnneeAcademique;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
public class AnneeAcademiqueBean implements Serializable{
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    
    private AnneeAcademique selectedAnneeAcademique;
    private AnneeAcademique newAnneeAcademique;
    private List<AnneeAcademique> listeAnneeAcademiques;
    private List<AnneeAcademique> filteredList;
    private String anneeAcademique;
    private List<String> listeAnneeUniversitaire  = new ArrayList<>();
    private AnneeAcademique currentAnneeAcademic;
    private static AnneeAcademique anneeAcademicChoisi = new AnneeAcademique();

    public AnneeAcademiqueBean() {
    }

    @PostConstruct
    public void init() {
        currentAnneeAcademic = anneeAcademiqueFacade.getCurrentAcademicYear();
        listeAnneeUniversitaire.add(JsfUtil.nextAcademicYear(currentAnneeAcademic.getDescription()));
        anneeAcademicChoisi = currentAnneeAcademic;
        listeAnneeAcademiques = anneeAcademiqueFacade.findAll();
        prepareCreate();
    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        
        try {
            currentAnneeAcademic.setEtat(0);
            anneeAcademiqueFacade.edit(currentAnneeAcademic);
            newAnneeAcademique.setDescription(anneeAcademique);
            newAnneeAcademique.setEtat(1);
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

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public List<String> getListeAnneeUniversitaire() {
        return listeAnneeUniversitaire;
    }

    public void setListeAnneeUniversitaire(List<String> listeAnneeUniversitaire) {
        this.listeAnneeUniversitaire = listeAnneeUniversitaire;
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

    public  AnneeAcademique getAnneeAcademicChoisi() {
        return anneeAcademicChoisi;
    }

    public  void setAnneeAcademicChoisi(AnneeAcademique anneeAcademicChoisi) {
        AnneeAcademiqueBean.anneeAcademicChoisi = anneeAcademicChoisi;
    }
    
    public static AnneeAcademique getAnneeAcademicChoisi1() {
        return anneeAcademicChoisi;
    }
    
    public void initAnneeAcademique(){
        System.out.println("Init Ann");
        if(anneeAcademicChoisi != null){
            System.out.println("Out her !");
           RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/'"); 
        }
        
    }

    public AnneeAcademique getCurrentAnneeAcademic() {
        return currentAnneeAcademic;
    }

    public void setCurrentAnneeAcademic(AnneeAcademique currentAnneeAcademic) {
        this.currentAnneeAcademic = currentAnneeAcademic;
    }
    
    
}
