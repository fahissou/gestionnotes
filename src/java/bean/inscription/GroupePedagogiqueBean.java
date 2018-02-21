/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.SemestreFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.inscription.GroupePedagogique;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "groupePedagogiqueBean")
public class GroupePedagogiqueBean implements Serializable {

    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    private GroupePedagogique newGroupePedagogique;
    private GroupePedagogique selectedGroupePedagogique;
    private List<GroupePedagogique> listeGroupePedagogiques;
    private List<GroupePedagogique> filteredList;
    private int cycle;
    private String niveau;
    private List<Integer> listeCycles;
    private List<String> listesNiveau;

    public GroupePedagogiqueBean() {
    }

    @PostConstruct
    public void init() {
        listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
        prepareCreate();
        listeCycles = JsfUtil.listeCycles();
    }

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            newGroupePedagogique.setCycles(cycle);
            newGroupePedagogique.setNiveau(JsfUtil.getNiveauLabel(niveau));
            int ordre = JsfUtil.getOrder(cycle, JsfUtil.getNiveauLabel(niveau));
            if (ordre != -1) {
                newGroupePedagogique.setOrdre(ordre);
                groupePedagogiqueFacade.create(newGroupePedagogique);
                msg = JsfUtil.getBundleMsg("GroupePedagogiqueCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                prepareCreate();
                listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
            } else {
                msg = JsfUtil.getBundleMsg("GroupePedagogiqueCreateErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            int order = JsfUtil.getOrder(selectedGroupePedagogique.getCycles(), selectedGroupePedagogique.getNiveau());
            if (order != -1) {
                groupePedagogiqueFacade.edit(selectedGroupePedagogique);
                msg = JsfUtil.getBundleMsg("GroupePedagogiqueEditSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
            } else {
                msg = JsfUtil.getBundleMsg("GroupePedagogiqueEditErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            groupePedagogiqueFacade.remove(selectedGroupePedagogique);
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public GroupePedagogique getNewGroupePedagogique() {
        return newGroupePedagogique;
    }

    public void setNewGroupePedagogique(GroupePedagogique newGroupePedagogique) {
        this.newGroupePedagogique = newGroupePedagogique;
    }

    public GroupePedagogique getSelectedGroupePedagogique() {
        return selectedGroupePedagogique;
    }

    public void setSelectedGroupePedagogique(GroupePedagogique selectedGroupePedagogique) {
        this.selectedGroupePedagogique = selectedGroupePedagogique;
    }

    public List<GroupePedagogique> getListeGroupePedagogiques() {
        return listeGroupePedagogiques;
    }

    public void setListeGroupePedagogiques(List<GroupePedagogique> listeGroupePedagogiques) {
        this.listeGroupePedagogiques = listeGroupePedagogiques;
    }

    public List<GroupePedagogique> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<GroupePedagogique> filteredList) {
        this.filteredList = filteredList;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void prepareCreate() {
        this.newGroupePedagogique = new GroupePedagogique();
    }

    public void reset(ActionEvent e) {
        this.newGroupePedagogique.reset();
    }

    public void initNiveauEtude() {
        listesNiveau = JsfUtil.getNiveauEtude(cycle);
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public List<Integer> getListeCycles() {
        return listeCycles;
    }

    public void setListeCycles(List<Integer> listeCycles) {
        this.listeCycles = listeCycles;
    }

    public List<String> getListesNiveau() {
        return listesNiveau;
    }

    public void setListesNiveau(List<String> listesNiveau) {
        this.listesNiveau = listesNiveau;
    }

}
