/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.message;

import ejb.formation.HistoriquesFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.formation.Historiques;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ManagedBean
@ViewScoped
public class HistoriqueBean {
    @EJB
    private HistoriquesFacade historiquesFacade;

    private Historiques selectedHistoriques;
    private Historiques newHistoriques;
    private List<Historiques> listeHistoriquess;
    private List<Historiques> filteredList;
    
    
    public HistoriqueBean() {
    }
    
    @PostConstruct
    public void init() {
        listeHistoriquess = historiquesFacade.findAll();
        prepareCreate();
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
    
    
    
}
