/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.module;

import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.MatiereFacade;
import ejb.module.UeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Named(value = "matiereBean")
@ViewScoped
public class MatiereBean implements Serializable{
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private MatiereFacade matiereFacade;
    private Matiere newMatiere;
    private Matiere selectedMatiere;
    private List<Matiere>listeMatieres;
    private List<Matiere>filteredList;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Ue> listeUE;
    private GroupePedagogique newGroupePedagogique;
    private Ue ue;
    private Map<GroupePedagogique, List<Ue>> data1; // = new HashMap<>();
    
    /**
     * Creates a new instance of MatiereBean
     */
    public MatiereBean() {
    }
    @PostConstruct
    public void init(){
        listeMatieres = matiereFacade.findAll();
        listGroupePedagogiques = groupePedagogiqueFacade.findAll();
        listeUE = ueFacade.findAll();
        loadData();
        prepareCreate();
        
    }
    
    public void loadData() {
        data1 = new HashMap<>();
        for (int i = 0; i < listGroupePedagogiques.size(); i++) {
            List<Ue> list1 = ueFacade.getUeByGroupePedagogique(listGroupePedagogiques.get(i).getDescription());
            data1.put(listGroupePedagogiques.get(i), list1);

        }

        

    }
    
    public void onGroupePedagogiqueChange() {
        System.out.println("ok dans la fonc");
        if (newGroupePedagogique != null) {
            listeUE = data1.get(newGroupePedagogique);
            System.out.println("bon" + listeUE.size());
        } else {
            listeUE = new ArrayList<>();
            System.out.println("c'est pas bon");
        }

    }
    
    public void doCreate(ActionEvent event) {
        String msg;
        try {
            matiereFacade.create(newMatiere);
            msg = JsfUtil.getBundleMsg("MatiereCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doEdit(ActionEvent event) {
        String msg;
        try {
            matiereFacade.edit(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void doDel(ActionEvent event) {
        String msg;
        try {
            matiereFacade.remove(selectedMatiere);
            msg = JsfUtil.getBundleMsg("MatiereDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeMatieres = matiereFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("MatiereDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Matiere getNewMatiere() {
        return newMatiere;
    }

    public void setNewMatiere(Matiere newMatiere) {
        this.newMatiere = newMatiere;
    }

    public Matiere getSelectedMatiere() {
        return selectedMatiere;
    }

    public void setSelectedMatiere(Matiere selectedMatiere) {
        this.selectedMatiere = selectedMatiere;
    }

    public List<Matiere> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<Matiere> listeMatieres) {
        this.listeMatieres = listeMatieres;
    }

    public List<Matiere> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Matiere> filteredList) {
        this.filteredList = filteredList;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public List<Ue> getListeUE() {
        return listeUE;
    }

    public void setListeUE(List<Ue> listeUE) {
        this.listeUE = listeUE;
    }

    public GroupePedagogique getNewGroupePedagogique() {
        return newGroupePedagogique;
    }

    public void setNewGroupePedagogique(GroupePedagogique newGroupePedagogique) {
        this.newGroupePedagogique = newGroupePedagogique;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }
    
    
    public void prepareCreate(){
        newMatiere = new Matiere();
    }
    public void reset(){
        newMatiere.reset();
    }
    
}
