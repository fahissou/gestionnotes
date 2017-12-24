/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.inscription.GroupePedagogique;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "groupePedagogiqueBean")
public class GroupePedagogiqueBean implements Serializable{
    @EJB
    private UeFacade ueFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    private GroupePedagogique newGroupePedagogique;
    private GroupePedagogique selectedGroupePedagogique;
    private List<GroupePedagogique> listeGroupePedagogiques;
    private List<GroupePedagogique> filteredList;
    private Map<String,Map<String,String>> data = new HashMap<>();
    private String cycle; 
    private String niveau;  
    private Map<String,String> listeCycles;
    private Map<String,String> listeNiveau;
//    private String groupePedaName;
//    private List<GroupePedagogique> groupePedagogique;
//    private List<Ue> ue;
//    private String libelleUE;
    /**
     * Creates a new instance of GroupePedagogiqueBean
     */
    
    public GroupePedagogiqueBean() {
    }
    @PostConstruct
    public void init(){
        listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
        prepareCreate();  
        listeCycles  = new HashMap<>();
        listeCycles.put("Cycle 1", "Cycle 1");
        listeCycles.put("Cycle 2", "Cycle 2");
        listeCycles.put("Cycle 3", "Cycle 3");
         
        Map<String,String> map = new HashMap<>();
        map.put("Licence 1", "Licence 1");
        map.put("Licence 2", "Licence 2");
        map.put("Licence 3", "Licence 3");
        data.put("Cycle 1", map);
         
        map = new HashMap<>();
        map.put("Master 1", "Master 1");
        map.put("Master 2", "Master 2");
        data.put("Cycle 2", map);
         
        map = new HashMap<>();
        map.put("Thèse 1", "Thèse 1");
        map.put("Thèse 2", "Thèse 2454");
        map.put("Thèse 3", "Thèse 3");
        data.put("Cycle 3", map);
    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            newGroupePedagogique.setCycleEtude(cycle);
            newGroupePedagogique.setNiveauEtude(niveau);
            groupePedagogiqueFacade.create(newGroupePedagogique);
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            groupePedagogiqueFacade.edit(selectedGroupePedagogique);
            msg = JsfUtil.getBundleMsg("GroupePedagogiqueEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeGroupePedagogiques = groupePedagogiqueFacade.findAll();
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

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Map<String, String> getListeCycles() {
        return listeCycles;
    }

    public void setListeCycles(Map<String, String> listeCycles) {
        this.listeCycles = listeCycles;
    }

    public Map<String, String> getListeNiveau() {
        return listeNiveau;
    }
   
    public void setListeNiveau(Map<String, String> listeNiveau) {
        this.listeNiveau = listeNiveau;
    }
    
   
    public void prepareCreate(){
        this.newGroupePedagogique = new GroupePedagogique();
    }
    public void reset(ActionEvent e) {
        this.newGroupePedagogique.reset();
    }
    public void onCycleChange() {
        
        if(cycle !=null && !cycle.equals(""))
            listeNiveau = data.get(cycle);
        else
            listeNiveau = new HashMap<>();
    }
 
   
    
    
}
