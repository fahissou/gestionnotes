/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.inscription.EtudiantFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;


/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "notesBean")
@ViewScoped
public class NotesBean implements Serializable{
    @EJB
    private EtudiantFacade etudiantFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    
   @EJB
    private NotesFacade notesFacade;
    private Notes selectedNotes;
    private Notes newNotes;
    private List<Notes> listeNotess;
    private List<Notes> filteredList;
    private String fileName;
    private List<String> contenuLigne;
    private UploadedFile uploadedFile;
    private String fileExtension;
    private FileInputStream fichier;
    private Inscription inscription;
    private Etudiant etudiant;
    private String anneeAcademique;
    private Matiere newMatiere;
    private GroupePedagogique newGroupePedagogique;
    
    
     
    
    /**
     * Creates a new instance of NotesBean
     */
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getContenuLigne() {
        return contenuLigne;
    }

    public void setContenuLigne(List<String> contenuLigne) {
        this.contenuLigne = contenuLigne;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public FileInputStream getFichier() {
        return fichier;
    }

    public void setFichier(FileInputStream fichier) {
        this.fichier = fichier;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public Matiere getNewMatiere() {
        return newMatiere;
    }

    public void setNewMatiere(Matiere newMatiere) {
        this.newMatiere = newMatiere;
    }

    public GroupePedagogique getNewGroupePedagogique() {
        return newGroupePedagogique;
    }

    public void setNewGroupePedagogique(GroupePedagogique newGroupePedagogique) {
        this.newGroupePedagogique = newGroupePedagogique;
    }

  
    public void docreateCollective() throws IOException, FileNotFoundException, ParseException{
        System.out.println("ok dans docreate");
        if(uploadedFile != null) {      
            System.out.println("fichier chargé");
            fileName = FilenameUtils.getName(uploadedFile.getFileName());
            fichier = (FileInputStream) uploadedFile.getInputstream();
            fileExtension = fileName.split("\\.")[1];
            System.out.println("extension "+fileExtension);
            insertInscription(fichier);
            
        }else{
            System.out.println("fichier non chargé");
        }
        
    }
    
    
    public void insertInscription(FileInputStream fichier) throws FileNotFoundException, IOException, ParseException {
       String msg = null;
        if(fichier == null){
            System.out.println("OK in fonction null");
        }else {
            if(fileExtension.equals("xls")){
            HSSFWorkbook wb = new HSSFWorkbook(fichier);
            HSSFSheet sheet = wb.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            int compteurLigne = 0;
            for (Row ligne : sheet) {//parcourir les lignes
            contenuLigne = new ArrayList<>();
            compteurLigne++;
             for (Cell cell : ligne) {//parcourir les colonnes
                //évaluer le type de la cellule
                
                switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                  
                    case Cell.CELL_TYPE_NUMERIC:
                     
                        contenuLigne.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case Cell.CELL_TYPE_STRING:
                     
                        contenuLigne.add(cell.getStringCellValue());
                        break;
                    
                }

            }            
            if(compteurLigne > 1){
                // Etudiant save
                 if(contenuLigne.size() == 4){
                 inscription = inscriptionFacade.getInscriptionsByEtudiant(String.valueOf(contenuLigne.get(0)),anneeAcademique);
                 newNotes.setInscription(inscription);
                 double notes = Double.parseDouble(contenuLigne.get(3));
                 newNotes.setNote(notes);
                 if(notes >= 12.0){
                     newNotes.setEtatValidation("Validé");
                 }else{
                     newNotes.setEtatValidation("Non Validé"); 
                 }
                 notesFacade.create(newNotes);
                 }else{
                     msg = JsfUtil.getBundleMsg("formatFichierNonPriseEncompte");
                     JsfUtil.addErrorMessage(msg);
                     break;
                 }
                 
             }

        }
       }else if(fileExtension.equals("xlsx")){
            msg = JsfUtil.getBundleMsg("extensionNonpriseCompte");
            JsfUtil.addErrorMessage(msg);  
       }else{
            msg = JsfUtil.getBundleMsg("extensionNonpriseCompte1");
            JsfUtil.addErrorMessage(msg);  
       }
                    
      }
             
    }
    
    public List<Notes> affichage() {
        System.out.println(" annee: " +anneeAcademique+" libelleGp: "+newGroupePedagogique.getDescription()+" libelleMatiere: "+newMatiere.getLibelle());
        return notesFacade.listeNoteGpAnnee(anneeAcademique, newGroupePedagogique.getDescription(), newMatiere.getLibelle());
    }
    
    
}
