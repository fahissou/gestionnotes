package bean.inscription;

import ejb.administration.AnneeAcademiqueFacade;
import ejb.inscription.EtudiantFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.UeFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import jpa.administration.AnneeAcademique;
import jpa.inscription.EnumGenre;
import jpa.inscription.Etudiant;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Ue;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.ExceptionsGestionnotes;
import util.JsfUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "inscriptionBean")
@ViewScoped
public class InscriptionBean implements Serializable {

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;

    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private NotesFacade notesFacade;

    @EJB
    private EtudiantFacade etudiantFacade;

    private Etudiant newEtudiant;

    @EJB
    private InscriptionFacade inscriptionFacade;
    private Inscription selectedInscription;
    private Inscription newInscription;
    private List<Inscription> listeInscriptions;
    private List<Inscription> filteredList;
    private List<String> listeAnneeUniversitaire;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private String cycle;
    private String niveau;
    private Map<String, String> listeCycles;
    private Map<String, String> listeNiveau;
    private String fileName;
    private List<String> contenuLigne;
    private UploadedFile uploadedFile;
    private String typeInscription;
    private String fileExtension;
    private FileInputStream fichier;
    private Notes newNote;
    private String anneeAcademique;
    private AnneeAcademique newAnneeAcademique;

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
        newAnneeAcademique = new AnneeAcademique();

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

        listeCycles = new HashMap<String, String>();
        listeCycles.put("Cycle 1", "Cycle 1");
        listeCycles.put("Cycle 2", "Cycle 2");
        listeCycles.put("Cycle 3", "Cycle 3");

        Map<String, String> map = new HashMap<String, String>();
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

    public void doCreate(ActionEvent event) throws ExceptionsGestionnotes {
        String msg;
        try {
            inscriptionFacade.create(newInscription);
            msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeInscriptions = inscriptionFacade.findAll();
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

    public Etudiant getNewEtudiant() {
        return newEtudiant;
    }

    public void setNewEtudiant(Etudiant newEtudiant) {
        this.newEtudiant = newEtudiant;
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

    public String getTypeInscription() {
        return typeInscription;
    }

    public void setTypeInscription(String typeInscription) {
        this.typeInscription = typeInscription;
    }

    public void prepareCreate() {
        this.newInscription = new Inscription();
        this.newEtudiant = new Etudiant();
    }

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public void reset(ActionEvent e) {
        this.newInscription.reset();
    }

    public void onCycleChange() {
        if (cycle != null && !cycle.equals("")) {
            listeNiveau = data.get(cycle);
        } else {
            listeNiveau = new HashMap<String, String>();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        fileName = uploadedFile.getFileName();
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void docreateCollective() throws IOException, FileNotFoundException, ParseException {
        String msg;
        try {
            if (uploadedFile != null) {
                fileName = FilenameUtils.getName(uploadedFile.getFileName());
                fichier = (FileInputStream) uploadedFile.getInputstream();
                fileExtension = fileName.split("\\.")[1];
                System.out.println("extension " + fileExtension);
                AnneeAcademique academicYear = anneeAcademiqueFacade.getCurrentAcademicYear();
                int result = JsfUtil.valideAcademicYear(academicYear.getDescription(), anneeAcademique);
                if(result == 1) {
                    newAnneeAcademique = academicYear;
                    insertInscription(fichier);
                }else if(result == 0){
                    academicYear.setEtat(result);
                    anneeAcademiqueFacade.edit(academicYear);
                    newAnneeAcademique.setDescription(anneeAcademique);
                    anneeAcademiqueFacade.create(newAnneeAcademique);
                    insertInscription(fichier);
                }else{
                    msg = JsfUtil.getBundleMsg("AnneeAcademiqueError");
                            JsfUtil.addErrorMessage(msg);
                }
                

            } else {
                System.out.println("fichier non chargé");
            }
        } catch (Exception e) {
        }

    }

    public void insertInscription(FileInputStream fichier) throws FileNotFoundException, IOException, ParseException {
        String msg = null;
        if (fichier == null) {
            System.out.println("OK in fonction null");
        } else {
            if (fileExtension.equals("xls")) {
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

                    if (compteurLigne > 1) {
                        // Etudiant save
                        if (contenuLigne.size() == 7) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
                            newEtudiant.setLogin(String.valueOf(contenuLigne.get(0)));
                            newEtudiant.setNom(String.valueOf(contenuLigne.get(1)));
                            newEtudiant.setPrenom(String.valueOf(contenuLigne.get(2)));
                            EnumGenre enumGenre = EnumGenre.valueOf(String.valueOf(contenuLigne.get(3)));
                            newEtudiant.setGenre(enumGenre);
                            Date dateNaiss = JsfUtil.stringToDate(String.valueOf(contenuLigne.get(4)));
                            newEtudiant.setDateNaissance(dateNaiss );
                            newEtudiant.setTelephone(String.valueOf(contenuLigne.get(5)));
                            newEtudiant.setMail(String.valueOf(contenuLigne.get(6)));
                            // Inscritption save
                            newInscription.setEtudiant(newEtudiant);
                            newInscription.setAnneeAcademique(newAnneeAcademique);
                            etudiantFacade.create(newEtudiant);
                            inscriptionFacade.create(newInscription);
                            createDiffaultNotes(newInscription);
                        } else {
                            msg = JsfUtil.getBundleMsg("formatFichierNonPriseEncompte");
                            JsfUtil.addErrorMessage(msg);
                            break;
                        }

                    }

                }
            } else if (fileExtension.equals("xlsx")) {
                msg = JsfUtil.getBundleMsg("extensionNonpriseCompte");
                JsfUtil.addErrorMessage(msg);

            } else {
                msg = JsfUtil.getBundleMsg("extensionNonpriseCompte1");
                JsfUtil.addErrorMessage(msg);
            }

        }

    }

    public void doCreateIndividuelle(ActionEvent event) throws ExceptionsGestionnotes {
        String msg;
        try {
            newEtudiant = newInscription.getEtudiant();
            newEtudiant.setFiliere(newInscription.getGroupePedagogique().getFiliere());
            etudiantFacade.create(newEtudiant);
            inscriptionFacade.create(newInscription);
            msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeInscriptions = inscriptionFacade.findAll();

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }

    }

    public void createDiffaultNotes(Inscription inscription) {
        List<Ue> listeUe = ueFacade.getUeByGroupePedagogique(inscription.getGroupePedagogique());
        try {
            for (int i = 0; i < listeUe.size(); i++) {
                List<Matiere> matieres = matiereFacade.getMatiereByUe(listeUe.get(i));
                newNote = new Notes();
                for (int j = 0; j < matieres.size(); j++) {
                    newNote.setMatiere(matieres.get(j));
                    newNote.setInscription(inscription);
                    newNote.setEtatValidation("NON VALIDÉ");
                    newNote.setNote(0.0);
                    notesFacade.create(newNote);
                }

            }
        } catch (Exception ex) {

        }

    }

    public String inscriptionRedirect() {
        String page = "";
        switch (typeInscription) {
            case "collective":
                page = this.typeInscription;
                break;
            case "individuelle":
                page = this.typeInscription;
                break;
        }
        return page;
    }
}
