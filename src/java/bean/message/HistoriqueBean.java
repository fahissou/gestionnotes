
package bean.message;

import ejb.formation.HistoriquesFacade;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.formation.Historiques;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "historiqueBean")
@ViewScoped
public class HistoriqueBean implements Serializable{
    @EJB
    private HistoriquesFacade historiquesFacade;

    private Historiques selectedHistoriques;
    private Historiques newHistoriques;
    private List<Historiques> listeHistoriquess;
    private List<Historiques> filteredList;
    private  String lien = "/gestionnotes/fichiergenerer/rapportgestionnotes/touslesrapports/";
    private String pathOut;
    
    
    public HistoriqueBean() {
    }
    
    @PostConstruct
    public void init() {
        listeHistoriquess = historiquesFacade.findAll();
        prepareCreate();
        pathOut = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/touslesrapports/";
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            historiquesFacade.create(newHistoriques);
            msg = JsfUtil.getBundleMsg("HistoriquesCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeHistoriquess = historiquesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HistoriquesCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            historiquesFacade.edit(selectedHistoriques);
            msg = JsfUtil.getBundleMsg("HistoriquesEditSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
            listeHistoriquess = historiquesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HistoriquesEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            historiquesFacade.remove(selectedHistoriques);
            removeFile();
            msg = JsfUtil.getBundleMsg("HistoriquesDelSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
            listeHistoriquess = historiquesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("HistoriquesDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Historiques getSelectedHistoriques() {
        return selectedHistoriques;
    }

    public void setSelectedHistoriques(Historiques selectedHistoriques) {
        this.selectedHistoriques = selectedHistoriques;
    }

    public Historiques getNewHistoriques() {
        return newHistoriques;
    }

    public void setNewHistoriques(Historiques newHistoriques) {
        this.newHistoriques = newHistoriques;
    }

    public List<Historiques> getListeHistoriquess() {
        return listeHistoriquess;
    }

    public void setListeHistoriquess(List<Historiques> listeHistoriquess) {
        this.listeHistoriquess = listeHistoriquess;
    }

    public List<Historiques> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Historiques> filteredList) {
        this.filteredList = filteredList;
    }
    
    public void prepareCreate() {
        this.newHistoriques = new Historiques();
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
    
    public void openFile() {
        RequestContext.getCurrentInstance().execute("window.location='"+selectedHistoriques.getLienFile()+".pdf"+ "'");
    }
    
    public void removeFile() {
        File repertoire = new File(pathOut);
        File[] files = repertoire.listFiles();
//        boolean trouve = false;
        try {
            if(files.length != 0) {
                System.out.println("OK4 "+files[0].getName());
                for (int i = 0; i < files.length; i++) {
                    String oldFile = JsfUtil.getFileName2(selectedHistoriques.getLienFile())+".pdf";
                    System.out.println("OK5 "+oldFile);
                    if(files[i].getName().equals(oldFile)) {
                        System.out.println("ok remove");
                        boolean bool = files[i].delete();
                        System.out.println("boolean "+bool);
                        break;
                    }
            }
            }
        } catch (Exception e) {
        }
    }
    
    public  void reflesh(){
        listeHistoriquess = historiquesFacade.findAll();
    }
    
    public void deleteFile(String folderName) {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        try {
            if(files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                boolean bool = files[i].delete();
            }
            }
        } catch (Exception e) {
        }
    }
}
