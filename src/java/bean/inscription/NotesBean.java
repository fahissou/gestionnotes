/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.administration.AnneeAcademiqueFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.scene.chart.PieChart.Data;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.convert.out.pdf.PdfConversion;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
public class NotesBean implements Serializable {

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;

    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;

    @EJB
    private InscriptionFacade inscriptionFacade;

    @EJB
    private NotesFacade notesFacade;
    private Notes selectedNotes;
    private Notes newNotes;
    private List<Notes> listeNotess;

    private List<Notes> listeNotesGroupPeda;
    private List<Notes> listeNotesEtudiant;
    private List<Notes> liste;
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
    private Map<GroupePedagogique, List<Ue>> data1; // = new HashMap<>();
    private Map<Ue, List<Matiere>> data2;
    private Map<GroupePedagogique, List<Semestre>> data3;
    private Map<Ue, Ue> mapUE;
    private Map<GroupePedagogique, GroupePedagogique> mapGroupePedagogique;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Ue> listeUE;
    private List<Ue> ues;
    private List<Matiere> listeMatieres;
    private List<Matiere> tmpMatieres;
    private GroupePedagogique groupePedagogique;
    private Ue ue;
    private String pathIn;
    private String pathIn1;
    private String pathIn2;
    private String pathIn3;
    private String pathOut;
    private Semestre semestre;
    private List<Semestre> semestres;
    private List<Semestre> listeSemestre;

    /**
     * Creates a new instance of NotesBean
     */
    public NotesBean() {
    }

    @PostConstruct
    public void init() {
        listGroupePedagogiques = groupePedagogiqueFacade.findAll();
        listeSemestre = semestreFacade.findAll();
        listeNotess = notesFacade.findAll();
        listeUE = ueFacade.findAll();
        listeMatieres = matiereFacade.findAll();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
        loadData();
        prepareCreate();
        pathIn = new File("").getAbsolutePath() + "/releve/";
        InputStream inputStream =   this.getClass().getResourceAsStream("");
        
//        pathIn1 = new File("/gestionnotes/web/fichiersetats/proces.docx").getAbsolutePath();
//        pathIn2 = new File("").getPath();
//        pathIn3 = new File("").getParentFile().getAbsolutePath();
        System.out.println("okk1 "+pathIn1);
//        System.out.println("okk2 "+pathIn2);
//        System.out.println("okk3 "+pathIn3);
//        String nomSession = System.getProperty("user.home");
//        String val = nomSession +"\\Documents\\rapportgestionnotes\\";
//        pathOut = new File("").getAbsolutePath() +"/sortie/";
        pathOut = System.getProperty("user.home") + "\\Documents\\" + "/rapportgestionnotes/";
    }

    public void loadData() {
        data1 = new HashMap<>();
        data2 = new HashMap<>();
        data3 = new HashMap<>();
        for (int i = 0; i < listGroupePedagogiques.size(); i++) {
            List<Ue> list1 = ueFacade.getUeByGroupePedagogique(listGroupePedagogiques.get(i));
            data1.put(listGroupePedagogiques.get(i), list1);

        }

        for (int j = 0; j < listeUE.size(); j++) {
            List<Matiere> list2 = matiereFacade.getMatiereByUe(listeUE.get(j));
            data2.put(listeUE.get(j), list2);
        }

        for (int i = 0; i < listGroupePedagogiques.size(); i++) {
            List<Semestre> list3 = semestreFacade.getSemetreByGP(listGroupePedagogiques.get(i));
            data3.put(listGroupePedagogiques.get(i), list3);

        }

    }

    public void onGroupePedagogiqueChange() {
        if (groupePedagogique != null) {
            ues = data1.get(groupePedagogique);
        } else {
            ues = new ArrayList<>();
        }

    }

    public void onGroupePedagogiqueChangeS() {
        if (groupePedagogique != null) {
            semestres = data3.get(groupePedagogique);
        } else {
            semestres = new ArrayList<>();
        }

    }

    public void onUeChange() {
        if (ue != null) {
            tmpMatieres = data2.get(ue);
        } else {
            tmpMatieres = new ArrayList<>();
        }

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

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public List<Semestre> getSemestres() {
        return semestres;
    }

    public void setSemestres(List<Semestre> semestres) {
        this.semestres = semestres;
    }

    public List<Matiere> getTmpMatieres() {
        return tmpMatieres;
    }

    public void setTmpMatieres(List<Matiere> tmpMatieres) {
        this.tmpMatieres = tmpMatieres;
    }

    public List<Semestre> getListeSemestre() {
        return listeSemestre;
    }

    public void setListeSemestre(List<Semestre> listeSemestre) {
        this.listeSemestre = listeSemestre;
    }

    public void prepareCreate() {
        this.newNotes = new Notes();
    }

    public void reset(ActionEvent e) {
        this.newNotes.reset();
        groupePedagogique = null;
        ue = null;
        listGroupePedagogiques = null;
        listeUE = null;
        data1 = null;
        ues = null;
        tmpMatieres = null;

    }

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
    }

    public String getPathIn() {
        return pathIn;
    }

    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
    }

    public String getPathOut() {
        return pathOut;
    }

    public void setPathOut(String pathOut) {
        this.pathOut = pathOut;
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

    public List<Notes> getListeNotesGroupPeda() {
        return listeNotesGroupPeda;
    }

    public void setListeNotesGroupPeda(List<Notes> listeNotesGroupPeda) {
        this.listeNotesGroupPeda = listeNotesGroupPeda;
    }

    public List<Notes> getListe() {
        return liste;
    }

    public void setListe(List<Notes> liste) {
        this.liste = liste;
    }

    public Map<Ue, Ue> getMapUE() {
        return mapUE;
    }

    public void setMapUE(Map<Ue, Ue> mapUE) {
        this.mapUE = mapUE;
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

    public Map<GroupePedagogique, GroupePedagogique> getMapGroupePedagogique() {
        return mapGroupePedagogique;
    }

    public void setMapGroupePedagogique(Map<GroupePedagogique, GroupePedagogique> mapGroupePedagogique) {
        this.mapGroupePedagogique = mapGroupePedagogique;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
    }

    public List<Notes> getListeNotesEtudiant() {
        return listeNotesEtudiant;
    }

    public void setListeNotesEtudiant(List<Notes> listeNotesEtudiant) {
        this.listeNotesEtudiant = listeNotesEtudiant;
    }

    public String getPathIn1() {
        return pathIn1;
    }

    public void setPathIn1(String pathIn1) {
        this.pathIn1 = pathIn1;
    }

    public String getPathIn2() {
        return pathIn2;
    }

    public void setPathIn2(String pathIn2) {
        this.pathIn2 = pathIn2;
    }

    public String getPathIn3() {
        return pathIn3;
    }

    public void setPathIn3(String pathIn3) {
        this.pathIn3 = pathIn3;
    }

    
    public void docreateCollective() throws IOException, FileNotFoundException, ParseException {
        if (uploadedFile != null) {
            System.out.println("fichier chargé");
            fileName = FilenameUtils.getName(uploadedFile.getFileName());
            fichier = (FileInputStream) uploadedFile.getInputstream();
            fileExtension = fileName.split("\\.")[1];
            System.out.println("extension " + fileExtension);
            insertInscription(fichier);

        } else {
            System.out.println("fichier non chargé");
        }

    }

    public void insertInscription(FileInputStream fichier) throws FileNotFoundException, IOException, ParseException {
        String msg = null;
        if (fichier == null) {
            System.out.println("OK in fonction null");
        } else {
            if (fileExtension.toUpperCase().equals("xls")) {
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
                        if (contenuLigne.size() == 4) {
                            inscription = inscriptionFacade.getInscriptionsByEtudiant(String.valueOf(contenuLigne.get(0)), anneeAcademique);
                            newNotes.setInscription(inscription);
                            double notes = Double.parseDouble(contenuLigne.get(3));
                            newNotes.setNote(notes);
                            if (notes >= 12.0) {
                                newNotes.setEtatValidation("Validé");
                            } else {
                                newNotes.setEtatValidation("Non Validé");
                            }
                            notesFacade.create(newNotes);
                        } else {
                            msg = JsfUtil.getBundleMsg("formatFichierNonPriseEncompte");
                            JsfUtil.addErrorMessage(msg);
                            break;
                        }

                    }

                }
            } else if (fileExtension.toUpperCase().equals("xlsx")) {
                msg = JsfUtil.getBundleMsg("extensionNonpriseCompte");
                JsfUtil.addErrorMessage(msg);
            } else {
                msg = JsfUtil.getBundleMsg("extensionNonpriseCompte1");
                JsfUtil.addErrorMessage(msg);
            }

        }

    }

    public List<Matiere> getListeMatieres() {
        return listeMatieres;
    }

    public void setListeMatieres(List<Matiere> listeMatieres) {
        this.listeMatieres = listeMatieres;
    }

    public String affichage() {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique.getDescription(), newMatiere);
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);
            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }
        return "succes";
    }

    public String affichage1() {
        liste = new ArrayList<>();
        try {
            System.out.println(anneeAcademique + " " + groupePedagogique.getDescription() + " " + newMatiere.getLibelle());
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique.getDescription(), newMatiere);
            System.out.println("apres " + liste.size());
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);
            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.out.println("okk");
        }

        return "succes1";
    }

    public String updateNotes() {
        for (int i = 0; i < listeNotesGroupPeda.size(); i++) {
            notesFacade.edit(listeNotesGroupPeda.get(i));
        }
        return "succes";
    }

    public void afficherUE(List<Ue> ues) {
        for (int i = 0; i < ues.size(); i++) {
            System.out.println(ues.get(i).getLibelle());
        }

    }

    public void genererRelevet() {

        File repertoire = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle() + "/");
        File repertoire1 = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        File repertoire3 = new File(pathOut + "/" + "ProcesPDF" + "/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        repertoire2.mkdir();
        repertoire3.mkdir();
        try {

            Map<String, Object> pocheParametres = new HashMap<>();
            List<Ue> ues = ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre);
            afficherUE(ues);
            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique.getDescription(), anneeAcademique);
            // Paramètres d'entete du proces
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("annee", anneeAcademique);
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() +" :  "+ groupePedagogique.getDescription());
            parametreEntetes.put("semestre", semestre.getLibelle());

            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("C");
            champs.add("nom");
            champs.add("TC");
            

            int nombreColonneTable1 = 7;
            for (int i = 0; i < nombreColonneTable1; i++) {
                    champs.add("m" + (i + 1));
                    champs.add("o" + (i + 1));
                    champs.add("c" + (i + 1));
            }
            // Table contenant les enregistrements du fichier proces 1
            List< Map<String, Object>> conteneur = new ArrayList<>();
           

            for (int j = 0; j < inscriptions.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Etudiant student = inscriptions.get(j).getEtudiant();

                // Enregistrement des coordonnées de 
                pocheParametres.put("aa", inscriptions.get(j).getAnneeAcademique().getDescription());
                pocheParametres.put("a", inscriptions.get(j).getAnneeAcademique().getDescription().split("- ")[1].trim());
                pocheParametres.put("nom", student.getNom());
                pocheParametres.put("prenom", student.getPrenom());
                pocheParametres.put("dte", student.getDateNaissance().toString());
//                pocheParametres.put("dateNaiss", student.getDateCreation().toString());
//                pocheParametres.put("filiere", student.getGroupePedagogique().getDescription());
                row.put("C", (j + 1));
                row.put("nom", student.getNom() + " " + student.getPrenom());
                
                int nombreCreditValider = 0;
                double somMoySemestre = 0;
                for (int i = 0; i < ues.size(); i++) {

                    double som = 0.0;
                    List<Matiere> matieres = null;
                    Ue currentUE = ues.get(i);
                    matieres = matiereFacade.getMatiereByUe(currentUE);
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                        som = +notes.getNote();
//                                pocheParametres.put("N1", formatNote(notes.getNote()));
//                                pocheParametres.put("Co1", matieres.get(k).getCoefficiant());
                    }
                    double moyUE = som / matieres.size();
                    nombreCreditValider += isValide(moyUE, currentUE.getCredit());
                    somMoySemestre = +moyUE;
                    pocheParametres.put("N" + (i + 1), formatNote(moyUE));
                    pocheParametres.put("O" + (i + 1), decision(moyUE));
                    pocheParametres.put("C" + (i + 1), currentUE.getCredit());
                    pocheParametres.put("S" + (i + 1), inscriptions.get(j).getAnneeAcademique().getDescription());
                        
                        row.put("m" + (i + 1), formatNote(moyUE));
                        row.put("o" + (i + 1), decision2(moyUE));
                        row.put("c" + (i + 1), currentUE.getCredit());


                }
                row.put("TC", nombreCreditValider);
                double moyenneSemestre = somMoySemestre / ues.size();
                pocheParametres.put("T", nombreCreditValider);
                pocheParametres.put("M", moyenneSemestre);
                conteneur.add(row);

                genererRelevetNotes(pathIn + "/releveS7.docx", pocheParametres, repertoire.getAbsolutePath() + "/", student.getNom() + student.getPrenom() + student.getLogin());

            }
            docxToPDF(repertoire.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            mergePDF(repertoire2.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
//            genererProcesVerval(pathIn + "/proces.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale" + "_" + groupePedagogique.getDescription(), parametreEntetes);
            genererProcesVerval(pathIn + "/proces4.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale" + "_" + groupePedagogique.getDescription()+"1", parametreEntetes);
            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

    }

    public int isValide(double val, int credit) {
        int som = 0;
        if (val >= 12.0) {
            som = credit;
        }
        return som;
    }

    public String formatDate(String date) {
        String[] tmp = date.split("-");
        return tmp[2] + "-" + tmp[1] + "-" + tmp[0];
    }


    public String genereNomFichier() {
        String outputFile = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date()) + "bulletin";
        return outputFile;
    }

    public String genererRelevetNotes(String fichier, Map<String, Object> maps, String chemin, String nomfichier) throws Exception {
        String outputFile = "";
        OutputStream out = null;
        InputStream in = null;
        try {

            in = new FileInputStream(new File(fichier));
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            System.out.println("ok ici");
            // 3) Create context Java model
            IContext context = report.createContext();
            context.putMap(maps);

            FieldsMetadata metadata = new FieldsMetadata();
            report.setFieldsMetadata(metadata);
            outputFile = nomfichier + ".docx";
            // 4) Generate report by merging Java model with the Docx
            out = new FileOutputStream(new File(chemin + outputFile));
            report.process(context, out);

            out.close();
        } catch (IOException | XDocReportException ex) {
            System.out.println(" Exce " + ex.getMessage());
        }
        return out.toString();
    }

    // Test generation releve
    // fin Test
    public boolean genererProcesVerval(String fichier, List<String> champs, List< Map<String, Object>> conteneur, String tableName, String chemin, String fileName, Map<String, Object> parametreEntetes) {
        boolean resultat = false;
        try {
            System.out.println("Proces debut");
            InputStream in = new FileInputStream(new File(fichier));
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            //Put the table
            FieldsMetadata metadata = new FieldsMetadata();
            for (String ch : champs) {
                metadata.addFieldAsList(tableName + "." + ch);
            }
            report.setFieldsMetadata(metadata);
            // 3) Create context Java model
            IContext context = report.createContext();
            context.put(tableName, conteneur);
            context.putMap(parametreEntetes);
            // context.putMap(mapsP);
            // fichier de sortie
            String outputFile = fileName + ".docx";

            // 4) Generate report by merging Java model with the Docx
            OutputStream out = new FileOutputStream(new File(chemin + outputFile));
            report.process(context, out);
            resultat = true;
            System.out.println("Proces fin");
        } catch (IOException | XDocReportException ex) {
        }

        return resultat;
    }

    public void docxToPDF(String folderName, String destination) {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                String fileNam = files[i].getName();
                createPDF(folderName + fileNam, destination + "file" + i + ".pdf");
            }
        } catch (Exception e) {
            System.out.println(" pdf error " + e.getMessage());
        }
    }

    public void createPDF(String pathIn, String pathOut) {
        try {
            long start = System.currentTimeMillis();

            // 1) Load DOCX into XWPFDocument
            InputStream is = new FileInputStream(new File(pathIn));
            XWPFDocument document = new XWPFDocument(is);

            // 2) Prepare Pdf options
            PdfOptions options = PdfOptions.create();
//            PdfOptions options = PdfOptions.create().fontEncoding("windows-1250");
//            PdfOptions options = PdfOptions.create().fontEncoding("iso-8859-15");
            // 3) Convert XWPFDocument to Pdf
            OutputStream out = new FileOutputStream(new File(pathOut));
            PdfConverter.getInstance().convert(document, out, options);
//            System.out.println("Generate pdf/HelloWorld.pdf with "
//                    + (System.currentTimeMillis() - start) + "ms");

        } catch (Throwable e) {
            System.out.println(" pdf error " + e.getMessage());
        }
    }
    // *************************

    public void createPDF1(String pathIn, String pathOut) {
        try {
            long start = System.currentTimeMillis();

            // 1) Load DOCX into WordprocessingMLPackage
            InputStream is = new FileInputStream(new File(pathIn));
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(is);

            // 2) Prepare Pdf settings
            PdfSettings pdfSettings = new PdfSettings();

            // 3) Convert WordprocessingMLPackage to Pdf
            OutputStream out = new FileOutputStream(new File(pathOut));
            PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(wordMLPackage);
            converter.output(out, pdfSettings);
            System.err.println("Generate pdf/HelloWorld.pdf with "
                    + (System.currentTimeMillis() - start) + "ms");

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    //************************

    // ********************************
    public void createPDF2(String pathIn, String pathOut) {
        long startTime = System.currentTimeMillis();
        try {
            System.out.println("ok1 " + pathIn);
            XWPFDocument document = new XWPFDocument(Data.class.getResourceAsStream(pathIn));
            System.out.println("ok2 " + pathIn);
//            File outFile = new File("target/DocxBig.pdf");
//            outFile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(new File(pathOut));
//            PdfOptions options=null;
            System.out.println("ok3 ");
            PdfOptions options = null;
//            PdfOptions.create().fontEncoding("windows-1250");
            PdfConverter.getInstance().convert(document, out, options);
            System.out.println("ok4 ");
        } catch (Throwable e) {
            System.out.println("error " + e.getMessage());
        }
//        System.out.println("Generate DocxBig.pdf with " + (System.currentTimeMillis() - startTime) + " ms.");

    }

    //*********************
    public void mergePDF(String folderName, String pathOut) throws IOException {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        try {
            for (int i = 0; i < files.length; i++) {
                PDFmerger.addSource(files[i]);
            }
            PDFmerger.setDestinationFileName(pathOut + "IGISA2" + ".pdf");
            PDFmerger.mergeDocuments();
        } catch (Exception e) {
        }

    }

    public static String formatNote(double note) {
        note = (double) Math.round((note) * 100) / 100;
        String noteString = String.valueOf(note);
        String[] args = noteString.split("\\.");
        String t = args[0] + "," + args[1];
        return t;
    }

    public String decision(double note) {
        String arg = "NON VALIDER";
        if (note >= 12.0) {
            arg = "VALIDER";
        }
        return arg;
    }

    public String decision2(double note) {
        String arg = "N.V";
        if (note >= 12.0) {
            arg = "V";
        }
        return arg;
    }

    public String decision1(double note) {
        String arg = "Réfusé";
        if (note >= 12.0) {
            arg = "Admis";
        }
        return arg;
    }

    public void testFile() throws IOException {
//        String nomRep = genereNomFichier() + groupePedagogique.getDescription();
//        File repertoire = new File("/semestre1/" + nomRep + "/");
//        repertoire.mkdir();

        System.out.println(pathOut);
        Map<String, Object> pocheParametres = new HashMap<>();
        File repertoire = new File("");

//        try {
        pocheParametres.put("toto1", "Toto est gentile");
        pocheParametres.put("toto2", "Toto est ridicule");

//        genererRelevetNotes(pathIn + "/test.docx", pocheParametres, pathOut, genereNomFichier());
//            System.out.println("ok "+repertoire.getAbsolutePath());
//        } catch (Exception e) {
//            System.out.println("ici Exce"+e.getMessage());
//        }
    }

    public String affichage2() {
        liste = new ArrayList<>();
        try {
            liste = toutesLesNotesEtudiant();
            System.out.println("apres " + liste.size());
            if (!liste.isEmpty()) {
                setListeNotesEtudiant(liste);
            } else {
                listeNotesEtudiant = new ArrayList<>();
            }
        } catch (Exception ex) {
            System.out.println("okk je suis là ");
        }
        return "succes2";
    }

    public List<Notes> toutesLesNotesEtudiant() {
        List<Notes> liste = new ArrayList<>();
        List<Notes> notes1 = new ArrayList<>();
        List<Notes> notes2 = new ArrayList<>();
        List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByEtudiant(etudiant);
        System.out.println("ok " + inscriptions.size());
        for (int i = 0; i < inscriptions.size(); i++) {
            if (i == 0) {
                notes1 = notesFacade.listeNoteByInscription(inscriptions.get(i));
                liste = notes1;
                System.out.println(" i == 0" + liste.size());
            } else {
                notes2 = notesFacade.listeNoteByInscription(inscriptions.get(i));
                notes1 = concateListe(notes1, notes2);
                liste = notes1;
            }
        }
        System.out.println("sortie fonction " + liste.size());
        return liste;
    }

    public List<Notes> concateListe(List<Notes> liste1, List<Notes> liste2) {
        List<Notes> liste = new ArrayList<>();
        if (liste1.size() > liste2.size()) {
            for (int i = 0; i < liste1.size(); i++) {
                liste.add(liste1.get(i));
                if (i < liste2.size()) {
                    liste.add(liste2.get(i));
                }

            }
        } else if (liste1.size() < liste2.size()) {
            for (int i = 0; i < liste2.size(); i++) {
                liste.add(liste2.get(i));
                if (i < liste1.size()) {
                    liste.add(liste1.get(i));
                }

            }
        } else {
            for (int i = 0; i < liste1.size(); i++) {
                liste.add(liste1.get(i));
                liste.add(liste2.get(i));

            }
        }
        return liste;
    }
    
    public void validationNote(FacesContext context, UIComponent component, Object value) throws ValidatorException{
        Double note = (Double) value;
        String msg;
        try{
            if(note < 0 || note > 20){
                ((UIInput) component).setValid(false);
                msg = JsfUtil.getBundleMsg("InvalideNote");
                JsfUtil.addErrorMessage(msg);
                FacesMessage message = new FacesMessage(msg);
                context.addMessage(component.getClientId(context), message);
            }
        }catch(Exception e){
        }
    }


}
