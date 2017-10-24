/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.NotesFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.Notes;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@ManagedBean
@ViewScoped
public class NotesBean {

    private NotesFacade notesFacade;
    private Notes selectedNotes;
    private Notes newNotes;
    private List<Notes> listeNotess;
    private List<Notes> filteredList;
    public NotesBean() {
    }
    
    @PostConstruct
    public void init() {
        listeNotess = notesFacade.findAll();
        prepareCreate();
    }  

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            notesFacade.create(newNotes);
            msg = JsfUtil.getBundleMsg("NotesCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeNotess = notesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("NotesCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            notesFacade.edit(selectedNotes);
            msg = JsfUtil.getBundleMsg("NotesEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeNotess = notesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("NotesEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            notesFacade.remove(selectedNotes);
            msg = JsfUtil.getBundleMsg("NotesDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeNotess = notesFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("NotesDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Notes getSelectedNotes() {
        return selectedNotes;
    }

    public void setSelectedNotes(Notes selectedNotes) {
        this.selectedNotes = selectedNotes;
    }

    public Notes getNewNotes() {
        return newNotes;
    }

    public void setNewNotes(Notes newNotes) {
        this.newNotes = newNotes;
    }

    public List<Notes> getListeNotess() {
        return listeNotess;
    }

    public void setListeNotess(List<Notes> listeNotess) {
        this.listeNotess = listeNotess;
    }

    public List<Notes> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Notes> filteredList) {
        this.filteredList = filteredList;
    }            

    public void prepareCreate() {
        this.newNotes = new Notes();
    }
    
    public void reset(ActionEvent e) {
        this.newNotes.reset();
    }
    
}
