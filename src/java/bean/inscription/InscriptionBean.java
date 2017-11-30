/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.InscriptionFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import jpa.inscription.Inscription;
import util.ExceptionsGestionnotes;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */

@Named(value = "inscriptionBean")
@ViewScoped
public class InscriptionBean implements Serializable{

    @EJB
    private InscriptionFacade inscriptionFacade;
    private Inscription selectedInscription;
    private Inscription newInscription;
    private List<Inscription> listeInscriptions;
    private List<Inscription> filteredList;
    private List<String> listeAnneeUniversitaire;
    private Map<String,Map<String,String>> data = new HashMap<String, Map<String,String>>();
    private String cycle; 
    private String niveau;  
    private Map<String,String> listeCycles;
    private Map<String,String> listeNiveau;
    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }

    @PostConstruct
    public void init() {
        listeInscriptions = inscriptionFacade.findAll();
        prepareCreate();
        
        listeAnneeUniversitaire = new ArrayList<>();
        listeAnneeUniversitaire.add("2014 - 2015");
        listeAnneeUniversitaire.add("2015 - 2016");
        listeAnneeUniversitaire.add("2016 - 2017");
        listeAnneeUniversitaire.add("2017 - 2018");
        listeAnneeUniversitaire.add("2018 - 2019");
        listeAnneeUniversitaire.add("2019 - 2020");
        listeAnneeUniversitaire.add("2020 - 2021");
        listeAnneeUniversitaire.add("2021 - 2022");
        listeAnneeUniversitaire.add("2022 - 2023");
        listeAnneeUniversitaire.add("2023 - 2024");
        listeAnneeUniversitaire.add("2024 - 2025");
        listeAnneeUniversitaire.add("2025 - 2026");
        listeAnneeUniversitaire.add("2026 - 2027");
        
        listeCycles  = new HashMap<String, String>();
        listeCycles.put("Cycle 1", "Cycle 1");
        listeCycles.put("Cycle 2", "Cycle 2");
        listeCycles.put("Cycle 3", "Cycle 3");
         
        Map<String,String> map = new HashMap<String, String>();
        map.put("Licence 1", "Licence 1");
        map.put("Licence 2", "Licence 2");
        map.put("Licence 3", "Licence 3");
        data.put("Cycle 1", map);
         
        map = new HashMap<String, String>();
        map.put("Master 1", "Master 1");
        map.put("Master 2", "Master 2");
        data.put("Cycle 2", map);
         
        map = new HashMap<String, String>();
        map.put("Thèse 1", "Thèse 1");
        map.put("Thèse 2", "Thèse 2");
        map.put("Thèse 3", "Thèse 3");
        data.put("Cycle 3", map);
        
    }  

    public void doCreate(ActionEvent event) throws ExceptionsGestionnotes{
        String msg;
        try {
            
            newInscription.setCycleFormation(cycle);
            newInscription.setNiveau(niveau);
            if(JsfUtil.validAcademicYear(newInscription.getAnneeUniversitaire())){
                inscriptionFacade.create(newInscription);
                msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                prepareCreate();
                listeInscriptions = inscriptionFacade.findAll();
            }else{
                ExceptionsGestionnotes e = new ExceptionsGestionnotes(JsfUtil.getBundleMsg("AnneeIncriptionInvalde"));
                JsfUtil.addErrorMessage(e.getMessage());
            }
            
            
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            inscriptionFacade.edit(selectedInscription);
            msg = JsfUtil.getBundleMsg("InscriptionEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeInscriptions = inscriptionFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            inscriptionFacade.remove(selectedInscription);
            msg = JsfUtil.getBundleMsg("InscriptionDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeInscriptions = inscriptionFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Inscription getSelectedInscription() {
        return selectedInscription;
    }

    public void setSelectedInscription(Inscription selectedInscription) {
        this.selectedInscription = selectedInscription;
    }

    public Inscription getNewInscription() {
        return newInscription;
    }

    public void setNewInscription(Inscription newInscription) {
        this.newInscription = newInscription;
    }

    public List<Inscription> getListeInscriptions() {
        return listeInscriptions;
    }

    public void setListeInscriptions(List<Inscription> listeInscriptions) {
        this.listeInscriptions = listeInscriptions;
    }

    public List<Inscription> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Inscription> filteredList) {
        this.filteredList = filteredList;
    }            

    public List<String> getListeAnneeUniversitaire() {
        return listeAnneeUniversitaire;
    }

    public void setListeAnneeUniversitaire(List<String> listeAnneeUniversitaire) {
        this.listeAnneeUniversitaire = listeAnneeUniversitaire;
    }

    public InscriptionFacade getInscriptionFacade() {
        return inscriptionFacade;
    }

    public void setInscriptionFacade(InscriptionFacade inscriptionFacade) {
        this.inscriptionFacade = inscriptionFacade;
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

    
    public void prepareCreate() {
        this.newInscription = new Inscription();
    }
    
    public void reset(ActionEvent e) {
        this.newInscription.reset();
    }
    
    public void onCycleChange() {
        if(cycle !=null && !cycle.equals(""))
            listeNiveau = data.get(cycle);
        else
            listeNiveau = new HashMap<String, String>();
    }
    
    
    
}
