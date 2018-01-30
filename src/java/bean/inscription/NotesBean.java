/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import ejb.administration.AnneeAcademiqueFacade;
import ejb.administration.NotificationFacade;
import ejb.administration.UtilisateurFacade;
import ejb.formation.FiliereFacade;
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
import java.awt.Desktop;
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
import javax.servlet.http.HttpServletRequest;
import jpa.administration.AnneeAcademique;
import jpa.administration.Notification;
import jpa.administration.Utilisateur;
import jpa.formation.Filiere;
import jpa.inscription.Enseignant;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
public class NotesBean implements Serializable {

    @EJB
    private FiliereFacade filiereFacade;

    @EJB
    private NotificationFacade notificationFacade;
    private Notification newNotification;
    @EJB
    private UtilisateurFacade utilisateurFacade;
    private Utilisateur newUtilisateur;
    private Utilisateur utilisateurDE;
    private StreamedContent file;

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
    private AnneeAcademique anneeAcademiqueObjet;
    private Matiere newMatiere;
    private GroupePedagogique newGroupePedagogique;
    private Map<GroupePedagogique, List<Ue>> data1; // = new HashMap<>();
    private Map<Ue, List<Matiere>> data2;
    private Map<GroupePedagogique, List<Semestre>> data3;
    private Map<Filiere, List<GroupePedagogique>> data4;
    private Map<Ue, Ue> mapUE;
    private Map<GroupePedagogique, GroupePedagogique> mapGroupePedagogique;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<GroupePedagogique> listGroupePedagogiques1;
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
    private List<Notes> listeNotesEnregistrement;
    private List<Filiere> listFiliere;
    private Filiere filiere;
    private List<Notes> listeOldNotes;
    private Enseignant enseignant;
    /**
     * Creates a new instance of NotesBean
     */
    public NotesBean() {
        newUtilisateur = new Utilisateur();
        newNotification = new Notification();
    }

    @PostConstruct
    public void init() {
        System.out.println("ok ici");
        listGroupePedagogiques = groupePedagogiqueFacade.findAll();
        listeSemestre = semestreFacade.findAll();
        listeNotess = notesFacade.findAll();
        listeUE = ueFacade.findAll();
        listeMatieres = matiereFacade.findAll();
        listFiliere = filiereFacade.findAll();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
        loadData();
        prepareCreate();

        pathIn = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/";
        pathIn1 = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/releveNouveau/";
        pathOut = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/";
    }

    public void loadData() {
        data1 = new HashMap<>();
        data2 = new HashMap<>();
        data3 = new HashMap<>();
        data4 = new HashMap<>();

        for (int i = 0; i < listFiliere.size(); i++) {
            List<GroupePedagogique> list1 = groupePedagogiqueFacade.getListGpByFilire(listFiliere.get(i));
            data4.put(listFiliere.get(i), list1);
        }

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
//            cleaAll();
            loadData();
        } else {
            ues = new ArrayList<>();
        }

    }

    public void onGroupePedagogiqueChangeS() {
        if (groupePedagogique != null) {
            semestres = data3.get(groupePedagogique);
//            cleaAll();
            loadData();
        } else {
            semestres = new ArrayList<>();
        }

    }

    public void onFiliereChange() {
        if (filiere != null) {
            listGroupePedagogiques1 = data4.get(filiere);
//            cleaAll();
            loadData();
        } else {
            listGroupePedagogiques1 = new ArrayList<>();

        }

    }

    public void onUeChange() {
        if (ue != null) {
            tmpMatieres = data2.get(ue);
            System.out.println(tmpMatieres.get(0).getLibelle());
//            cleaAll();
            loadData();
        } else {
            System.out.println("ok3");
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

    public List<Filiere> getListFiliere() {
        return listFiliere;
    }

    public void setListFiliere(List<Filiere> listFiliere) {
        this.listFiliere = listFiliere;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Notes> getListeNotesEnregistrement() {
        return listeNotesEnregistrement;
    }

    public void setListeNotesEnregistrement(List<Notes> listeNotesEnregistrement) {
        this.listeNotesEnregistrement = listeNotesEnregistrement;
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

    public Notification getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Notification newNotification) {
        this.newNotification = newNotification;
    }

    public Utilisateur getNewUtilisateur() {
        return newUtilisateur;
    }

    public void setNewUtilisateur(Utilisateur newUtilisateur) {
        this.newUtilisateur = newUtilisateur;
    }

    public Utilisateur getUtilisateurDE() {
        return utilisateurDE;
    }

    public void setUtilisateurDE(Utilisateur utilisateurDE) {
        this.utilisateurDE = utilisateurDE;
    }

    public List<GroupePedagogique> getListGroupePedagogiques1() {
        return listGroupePedagogiques1;
    }

    public void setListGroupePedagogiques1(List<GroupePedagogique> listGroupePedagogiques1) {
        this.listGroupePedagogiques1 = listGroupePedagogiques1;
    }

    public AnneeAcademique getAnneeAcademiqueObjet() {
        return anneeAcademiqueObjet;
    }

    public void setAnneeAcademiqueObjet(AnneeAcademique anneeAcademiqueObjet) {
        this.anneeAcademiqueObjet = anneeAcademiqueObjet;
    }

    public List<Notes> getListeOldNotes() {
        return listeOldNotes;
    }

    public void setListeOldNotes(List<Notes> listeOldNotes) {
        this.listeOldNotes = listeOldNotes;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    
 
    public void reset(ActionEvent e) {
        this.newNotes.reset();
        this.anneeAcademique = null;
        this.getUe().reset();
        this.etudiant.reset();
        this.groupePedagogique.reset();
        this.filiere.reset();
        this.semestre.reset();
        this.inscription.reset();
        ue = null;
        listGroupePedagogiques = null;
        listeUE = null;
        data1 = null;
        ues = null;
        tmpMatieres = null;
    }

    public void cleaAll() {
        this.filiere = null;
        this.groupePedagogique = null;
        this.ue = null;
        this.newMatiere = null;
    }

    public void resetOnchange() {
        data1.clear();
        data2.clear();
        data3.clear();
        data4.clear();
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

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
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
//                cleaAll();
            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }

        return "succes6";
    }

    public String affichage1() {
        liste = new ArrayList<>();
        try {
            System.out.println(anneeAcademique + " " + groupePedagogique.getDescription() + " " + newMatiere.getLibelle());
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique.getDescription(), newMatiere);
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);
            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }

        return "succes1";
    }

    public String affichageNotes() {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNotesNonValide(groupePedagogique, newMatiere);
            setGroupePedagogique(groupePedagogique);
            setEnseignant(enseignant);
            if (!liste.isEmpty()) {
                setListeNotesEnregistrement(liste);
            } else {
                listeNotesEnregistrement = new ArrayList<>();
            }

        } catch (Exception e) {

        }
        return "succes1";
    }

    public String updateNotes1() {
        for (int i = 0; i < listeNotesGroupPeda.size(); i++) {
            notesFacade.edit(listeNotesGroupPeda.get(i));
        }

        utilisateurDE = utilisateurFacade.recupDirecteurEtude("DE");
        HttpServletRequest params = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String loginCurrentUser = params.getParameter("utilisateurSession");
        newUtilisateur = utilisateurFacade.find(loginCurrentUser);
        this.newNotification.setDescription("Le professeur " + loginCurrentUser + "vient d'enregistrer ces notes");
        this.newNotification.setUtilisateur(utilisateurDE);
        notificationFacade.create(newNotification);
        int cpt;
        if (utilisateurDE.getCompteurMessage().isEmpty()) {
            cpt = 1;
            this.utilisateurDE.setCompteurMessage(String.valueOf(cpt));
            utilisateurFacade.edit(utilisateurDE);
        } else {
            cpt = Integer.parseInt(utilisateurDE.getCompteurMessage());
            cpt++;
            this.utilisateurDE.setCompteurMessage(String.valueOf(cpt));
            utilisateurFacade.edit(utilisateurDE);
        }

        return "succes";
    }

    public String updateNotes() {

        try {
            for (int i = 0; i < listeNotesEnregistrement.size(); i++) {
                Inscription inscription = listeNotesEnregistrement.get(i).getInscription();
                if (listeNotesEnregistrement.get(i).getNote() < 12.0) {
                    listeNotesEnregistrement.get(i).setOldNote(listeNotesEnregistrement.get(i).getNote());
                }
                notesFacade.edit(listeNotesEnregistrement.get(i));
                List<Matiere> matieres = matiereFacade.getMatiereByUe(ue);
                double som = 0.0;
                int compteur = 0;
                for (int j = 0; j < matieres.size(); j++) {
                    Notes note = notesFacade.getNotesByInscriptionMatiere(listeNotesEnregistrement.get(i).getInscription(), matieres.get(j));
                    som = som + note.getNote();
                    if (note.getNote() == 0.0) {
                        compteur++;
                    }
                }
                double moyUE = som / matieres.size();
                if (moyUE >= 12.0 && compteur == 0) {
                    inscription.setCompteurCredit(inscription.getCompteurCredit() + ue.getCredit());
                    inscriptionFacade.edit(inscription);
                    // mise à jour des notes de l'UE
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes note = notesFacade.getNotesByInscriptionMatiere(listeNotesEnregistrement.get(i).getInscription(), matieres.get(k));
                        note.setEtatValidation("UEV");
                        notesFacade.edit(note);
                    }
                }
            }
            liste = new ArrayList<>();
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique.getDescription(), newMatiere);
            setListeNotesGroupPeda(liste);
        } catch (Exception e) {
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
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> pocheParametres = new HashMap<>();
            List<Ue> ues = ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre);

            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique.getDescription(), anneeAcademique);
            afficherUE(ues);
            // Paramètres d'entete du proces
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("annee", anneeAcademique);
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
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
                String date = formatDate.format(student.getDateNaissance());
                pocheParametres.put("dte", date);
//                pocheParametres.put("dateNaiss", student.getDateCreation().toString());
//                pocheParametres.put("filiere", student.getGroupePedagogique().getDescription());
                row.put("C", (j + 1));
                row.put("nom", student.getNom() + " " + student.getPrenom());

                int nombreCreditValider = 0;
                double somMoySemestre = 0;
                int totalCredit = 0;
                List<String> uenv = new ArrayList<>();
                for (int i = 0; i < ues.size(); i++) {

                    double som = 0.0;
                    List<Matiere> matieres = null;
                    Ue currentUE = ues.get(i);
                    totalCredit += currentUE.getCredit();
                    matieres = matiereFacade.getMatiereByUe(currentUE);
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                        som = +notes.getNote();
//                                pocheParametres.put("N1", formatNote(notes.getNote()));
//                                pocheParametres.put("Co1", matieres.get(k).getCoefficiant());
                    }
                    double moyUE = som / matieres.size();
                    if (moyUE < 12.0) {
                        uenv.add(currentUE.getLibelle());
                    }
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
                pocheParametres.put("UENV", listeToString(uenv));
                pocheParametres.put("T", nombreCreditValider);
                pocheParametres.put("M", totalCredit);
                conteneur.add(row);

                genererRelevetNotes(pathIn + "/releveS7.docx", pocheParametres, repertoire.getAbsolutePath() + "/", student.getNom() + student.getPrenom() + student.getLogin());

            }
            docxToPDF(repertoire.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            mergePDF(repertoire2.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
//            genererProcesVerval(pathIn + "/proces.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale" + "_" + groupePedagogique.getDescription(), parametreEntetes);
            genererProcesVerval(pathIn + "/proces4.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes);
            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

    }

    // essai de proces dynamique
    public String genererRelevet2() {
        
        File repertoire = new File(pathOut + "/procesverbal/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle() + "/");
        File repertoire1 = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        File repertoire3 = new File(pathOut + "/procesverval/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        repertoire2.mkdir();
        repertoire3.mkdir();
        String msg;
        try {

            List<Ue> ues = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
            int nombreUE = ues.size();
            if (nombreUE <= 18 && nombreUE >= 4) {
                List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique.getDescription(), anneeAcademique);
                // Paramètres d'entete du proces fichier 1
                Map<String, Object> parametreEntetes1 = new HashMap<>();
                // Paramètres d'entete du proces fichier 2
                Map<String, Object> parametreEntetes2 = new HashMap<>();
                // Paramètres d'entete du proces fichier 3
                Map<String, Object> parametreEntetes3 = new HashMap<>();

                parametreEntetes1.put("annee", anneeAcademique);
                parametreEntetes1.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                parametreEntetes1.put("semestre", semestre.getLibelle());
                parametreEntetes1.put("d", JsfUtil.getDateEdition());

                parametreEntetes2.put("annee", anneeAcademique);
                parametreEntetes2.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                parametreEntetes2.put("semestre", semestre.getLibelle());
                parametreEntetes2.put("d", JsfUtil.getDateEdition());

                parametreEntetes3.put("annee", anneeAcademique);
                parametreEntetes3.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                parametreEntetes3.put("semestre", semestre.getLibelle());
                parametreEntetes3.put("d", JsfUtil.getDateEdition());

                // Definition des champs du proces fichier 1
                List<String> champs1 = new ArrayList<>();
                champs1.add("C");
                champs1.add("nom");
                champs1.add("TC");
                // Definition des champs du proces fichier 2
                List<String> champs2 = new ArrayList<>();
                champs2.add("C");
                champs2.add("nom");
                champs2.add("TC");
                // Definition des champs du proces fichier 2
                List<String> champs3 = new ArrayList<>();
                champs3.add("C");
                champs3.add("nom");
                champs3.add("TC");
                  System.out.println("");
                int max = 6; // nombre d'UE pouvant s'affiché sur une page format A4
                int[] ordreRaport = JsfUtil.choiseParameter(nombreUE);
                for (int i = 0; i < max; i++) {
                    if (i < max - ordreRaport[0]) {
                        // champ fichier 1
                        champs1.add("m" + (i + 1));
                        champs1.add("o" + (i + 1));
                        champs1.add("c" + (i + 1));
                    }
                    if (i < max - ordreRaport[1]) {
                        // champ fichier 2
                        champs2.add("m" + (i + 1));
                        champs2.add("o" + (i + 1));
                        champs2.add("c" + (i + 1));
                    }
                    if (i < max - ordreRaport[2]) {
                        // champ fichier 3
                        champs3.add("m" + (i + 1));
                        champs3.add("o" + (i + 1));
                        champs3.add("c" + (i + 1));
                    }
                }

                // Table contenant les enregistrements du fichier proces 1
                List< Map<String, Object>> conteneur1 = new ArrayList<>();

                // Table contenant les enregistrements du fichier proces 2
                List< Map<String, Object>> conteneur2 = new ArrayList<>();

                // Table contenant les enregistrements du fichier proces 3
                List< Map<String, Object>> conteneur3 = new ArrayList<>();

                for (int j = 0; j < inscriptions.size(); j++) {
                    // enregistrement fichier 1
                    Map<String, Object> row1 = new HashMap<>();
                    // enregistrement fichier 2
                    Map<String, Object> row2 = new HashMap<>();
                    // enregistrement fichier 3
                    Map<String, Object> row3 = new HashMap<>();

                    Etudiant student = inscriptions.get(j).getEtudiant();

//                pocheParametres.put("dateNaiss", student.getDateCreation().toString());
//                pocheParametres.put("filiere", student.getGroupePedagogique().getDescription());
                    row1.put("C", (j + 1));
                    row1.put("nom", student.getNom() + " " + student.getPrenom());

                    row2.put("C", (j + 1));
                    row2.put("nom", student.getNom() + " " + student.getPrenom());

                    row3.put("C", (j + 1));
                    row3.put("nom", student.getNom() + " " + student.getPrenom());

                    int nombreCreditValider = 0;
                    double somMoySemestre = 0;
                    int totalCredit = 0;
                    int l = 0;
                    int p = 0;
                    for (int i = 0; i < nombreUE; i++) {
                        double som = 0.0;
                        List<Matiere> matieres = null;
                        Ue currentUE = ues.get(i);
                        totalCredit += currentUE.getCredit();
                        matieres = matiereFacade.getMatiereByUe(currentUE);
                        for (int k = 0; k < matieres.size(); k++) {
                            Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                            som = +notes.getNote();
                        }
                        double moyUE = som / matieres.size();
                        nombreCreditValider += isValide(moyUE, currentUE.getCredit());
                        somMoySemestre = +moyUE;

                        if (i < max) {
                            row1.put("m" + (i + 1), formatNote(moyUE));
                            row1.put("o" + (i + 1), decision2(moyUE));
                            row1.put("c" + (i + 1), currentUE.getCredit());
                            parametreEntetes1.put("UE" + (i + 1), JsfUtil.getAbrevUE(currentUE.getLibelle()));
                            parametreEntetes1.put("ue" + (i + 1), currentUE.getLibelle());
                            l = i + 1;
                        } else if (i >= max && i < (2 * max)) {
                            row2.put("m" + (i + 1 - l), formatNote(moyUE));
                            row2.put("o" + (i + 1 - l), decision2(moyUE));
                            row2.put("c" + (i + 1 - l), currentUE.getCredit());
                            parametreEntetes2.put("UE" + (i + 1), JsfUtil.getAbrevUE(currentUE.getLibelle()));
                            parametreEntetes2.put("ue" + (i + 1), currentUE.getLibelle());
                            p = i + 1;
                        } else {
                            row3.put("m" + (i + 1 - p), formatNote(moyUE));
                            row3.put("o" + (i + 1 - p), decision2(moyUE));
                            row3.put("c" + (i + 1 - p), currentUE.getCredit());
                            parametreEntetes3.put("UE" + (i + 1), JsfUtil.getAbrevUE(currentUE.getLibelle()));
                            parametreEntetes3.put("ue" + (i + 1), currentUE.getLibelle());
                        }

                    }
                    row1.put("TC", nombreCreditValider);

                    conteneur1.add(row1);
                    conteneur2.add(row2);
                    conteneur3.add(row3);
                }
                String[] nameFile = JsfUtil.getFileNameRapport(nombreUE);
//            docxToPDF(repertoire.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
//            mergePDF(repertoire2.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
//            genererProcesVerval(pathIn + "/proces.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale" + "_" + groupePedagogique.getDescription(), parametreEntetes);
                if (nombreUE == 4) {
                    genererProcesVerval(pathIn1 + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                } else if (nombreUE == 5) {
                    genererProcesVerval(pathIn1 + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                } else if (nombreUE == 6) {
                    genererProcesVerval(pathIn1 + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                } else {
                    genererProcesVerval(pathIn1 + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    genererProcesVerval(pathIn1 + "/" + nameFile[1], champs2, conteneur2, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale2" + "_" + groupePedagogique.getDescription() + "2", parametreEntetes2);
                    genererProcesVerval(pathIn1 + "/" + nameFile[2], champs3, conteneur3, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale3" + "_" + groupePedagogique.getDescription() + "3", parametreEntetes3);
                }
               docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
               mergePDF(repertoire2.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
//            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
                msg = JsfUtil.getBundleMsg("ProcesGenererSucces");
                JsfUtil.addSuccessMessage(msg);
                System.out.println("chemin1 "+repertoire1.getAbsolutePath());
                System.out.println("chemin2 "+repertoire2.getAbsolutePath());
//                boolean fi2 =
                        new File(repertoire1.getAbsolutePath()).deleteOnExit();
                boolean fi1 = new File(repertoire2.getAbsolutePath()).delete();
                
//                RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/fichiergenerer/rapportgestionnotes/procesverval/IGISA2.pdf'");
                
//                repertoire1.delete();
                
            } else {
                msg = JsfUtil.getBundleMsg("NombreUEInvalid");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }
        return "success4";
    }

    // fin proces dynamique
    
    
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

    public String listeToString(List<String> arg) {
        String var = "";
        if (arg.size() == 0) {
            var = "NEANT";
        } else {
            for (int i = 0; i < arg.size(); i++) {
                var += " ; " + arg.get(i);
            }
        }
        return var;
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
            int i;
            for (i = 0; i < files.length; i++) {
                String fileNam = files[i].getName();
                createPDF(folderName + fileNam, destination + "file" + i + ".pdf");
            }
//            if(i == 1){
//            Desktop d = Desktop.getDesktop();
//            d.open(new File("destination + file0.pdf"));
//            }

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
            if (!liste.isEmpty()) {
                setListeNotesEtudiant(liste);
            } else {
                listeNotesEtudiant = new ArrayList<>();
            }
        } catch (Exception ex) {
        }
        return "succes2";
    }

    public String affichage3() {
        liste = new ArrayList<>();
        String resultat = "";
        String msg;
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademiqueObjet.getDescription(), groupePedagogique.getDescription(), newMatiere);
            if (!liste.isEmpty()) {
                setListeOldNotes(liste);
                resultat = "succes5";
            } else {
                listeOldNotes = new ArrayList<>();
                msg = JsfUtil.getBundleMsg("AucunResultat");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception ex) {
        }
        return resultat;
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

    public void validationNote(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Double note = (Double) value;
        String msg;
        try {
            if (note < 0 || note > 20) {
                ((UIInput) component).setValid(false);
                msg = JsfUtil.getBundleMsg("InvalideNote");
                JsfUtil.addErrorMessage(msg);
                FacesMessage message = new FacesMessage(msg);
                context.addMessage(component.getClientId(context), message);
            }
        } catch (Exception e) {
        }
    }

    // Résultat final
    public String genererResultatFinale() {
        File repertoireS = new File(pathOut + "/" + "ResultatFinal" + "_" + groupePedagogique.getDescription() + "/");
        pathOut = repertoireS.getAbsolutePath();
        repertoireS.mkdir();

        File repertoire = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle() + "/");
        File repertoire1 = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        File repertoire3 = new File(pathOut + "/" + "ResultatPDF" + "/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        repertoire2.mkdir();
        repertoire3.mkdir();
        try {
            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP1(groupePedagogique.getDescription(), anneeAcademique);
            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademique);
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
//            parametreEntetes.put("semestre", semestre.getLibelle());

            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("N");
            champs.add("NP");
            champs.add("TC");
            champs.add("Ob");

            // Table contenant les enregistrements du fichier resultat
            List< Map<String, Object>> conteneur = new ArrayList<>();

            for (int j = 0; j < inscriptions.size(); j++) {

                Map<String, Object> row = new HashMap<>();
                Etudiant student = inscriptions.get(j).getEtudiant();

                row.put("N", (j + 1));
                row.put("NP", student.getNom() + " " + student.getPrenom());

                row.put("TC", inscriptions.get(j).getCompteurCredit());
                System.out.println("ok1");
                String result = admissible(inscriptions.get(j).getCompteurCredit());
                System.out.println("ok2");
                createDefaultInscription(inscriptions.get(j), result);
                System.out.println("ok3");
                row.put("Ob", result);
                conteneur.add(row);
            }

            genererProcesVerval(pathIn + "/resultatFinal.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Resultat_Final" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes);
            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

        return "success4";

    }

    // Feuille notes
    public String genererFeuilleNotes() {
        System.out.println(listeNotesEnregistrement.size());
        GroupePedagogique gP = listeNotesEnregistrement.get(0).getInscription().getGroupePedagogique();
        File repertoireS = new File(pathOut + "/" + "FeuilleNotes" + "_" + gP.getDescription() + "/");
        pathOut = repertoireS.getAbsolutePath();
        repertoireS.mkdir();
        
//        String dateUp = jour +"/"+(mois+1)+"/"+annee;
        File repertoire = new File(pathOut + "/" + gP.getDescription() + "_" + anneeAcademique + "_" + "semestre" + "/");
        File repertoire1 = new File(pathOut + "/" + gP.getDescription() + "_" + anneeAcademique + "/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        File repertoire3 = new File(pathOut + "/" + "FeuilleNotesPDF" + "/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        repertoire2.mkdir();
        repertoire3.mkdir();
        try {
            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademique);
            parametreEntetes.put("filiere", gP.getFiliere().getLibelle() + " :  " + gP.getDescription());
            parametreEntetes.put("matiere", listeNotesEnregistrement.get(0).getMatiere().getLibelle());
            parametreEntetes.put("semestre", listeNotesEnregistrement.get(0).getMatiere().getUe().getSemestre().getLibelle());
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            parametreEntetes.put("enseignant", enseignant.getNom() +" "+enseignant.getPrenom());
            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("N");
            champs.add("NP");
            champs.add("N");
            champs.add("Ob");

            // Table contenant les enregistrements du fichier resultat
            List< Map<String, Object>> conteneur = new ArrayList<>();

            for (int j = 0; j < listeNotesEnregistrement.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Notes currentNotes = listeNotesEnregistrement.get(j);

                row.put("N", (j + 1));
                row.put("NP", currentNotes.getInscription().getEtudiant().getNom() + " " + currentNotes.getInscription().getEtudiant().getPrenom());

                row.put("No", currentNotes.getNote());

                row.put("Ob", decision(currentNotes.getNote()));
                conteneur.add(row);
            }

            genererProcesVerval(pathIn + "/feuilleNotes.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "FeuilleNotes" + "_" + gP.getDescription() + "1", parametreEntetes);
            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
//            System.out.println(" ok ici");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

        return "success4";

    }

    // dynamique releve notes
    public String genererReleveDynamic() {
        File repertoireS = new File(pathOut + "/" + "ResultatFinal" + "_" + groupePedagogique.getDescription() + "/");
        pathOut = repertoireS.getAbsolutePath();
        repertoireS.mkdir();

        File repertoire = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle() + "/");
        File repertoire1 = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        File repertoire3 = new File(pathOut + "/" + "ReleveNotesPDF" + "/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        repertoire2.mkdir();
        repertoire3.mkdir();
        try {

            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP1(groupePedagogique.getDescription(), anneeAcademique);

//            parametreEntetes.put("semestre", semestre.getLibelle());
            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("UE");
            champs.add("N");
            champs.add("O");
            champs.add("C");
            champs.add("S");

            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademique);
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
            parametreEntetes.put("semestre", semestre.getLibelle());

            for (int j = 0; j < inscriptions.size(); j++) {

                Etudiant student = inscriptions.get(j).getEtudiant();
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
                Map<String, Object> pocheParametres = new HashMap<>();

                // student
                parametreEntetes.put("nom", student.getNom());
                parametreEntetes.put("prenom", student.getPrenom());
                String date = formatDate.format(student.getDateNaissance());
                parametreEntetes.put("dte", date);

                // Table contenant les enregistrements du fichier resultat
                List< Map<String, Object>> conteneur = new ArrayList<>();

                int nombreCreditValider = 0;
                double somMoySemestre = 0;
                int totalCredit = 0;
                List<String> uenv = new ArrayList<>();
                Map<String, Object> row1 = null;
                List<Ue> ues1 = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
                for (int i = 0; i < ues1.size(); i++) {

                    row1 = new HashMap<>();
                    double som = 0.0;
                    List<Matiere> matieres = null;
                    Ue currentUE = ues1.get(i);
                    totalCredit += currentUE.getCredit();
                    matieres = matiereFacade.getMatiereByUe(currentUE);
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                        som = +notes.getNote();
                    }
                    double moyUE = som / matieres.size();
                    if (moyUE < 12.0) {
                        uenv.add(currentUE.getLibelle());
                    }
                    nombreCreditValider += isValide(moyUE, currentUE.getCredit());
                    somMoySemestre = +moyUE;
                    row1.put("UE", currentUE.getLibelle());
                    row1.put("N", formatNote(moyUE));
                    row1.put("O", decision(moyUE));
                    row1.put("C", currentUE.getCredit());
                    row1.put("S", inscriptions.get(j).getAnneeAcademique().getDescription());
                    conteneur.add(row1);
                }

                parametreEntetes.put("TC", totalCredit);
                parametreEntetes.put("TCV", nombreCreditValider);
//                double moyenneSemestre = somMoySemestre / ues.size();
                parametreEntetes.put("UENV", listeToString(uenv));
//                pocheParametres.put("M", totalCredit);

                genererProcesVerval(pathIn + "/dynamiqueReleve.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Resultat_Final" + "_" + student.getNom() + " " + student.getPrenom(), parametreEntetes);

//            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
            }
            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            mergePDF(repertoire2.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

        return "";

    }

    // END dynamique releve
    public String admissible(int a) {
        String res = "Réfusé";
        if (a >= 50) {
            res = "Admis";
        }
        return res;
    }

    public void createDefaultInscription(Inscription inscription, String arg) {

        try {
            if (arg.equals("Admis")) {
                Etudiant etudiant1 = inscription.getEtudiant();
                GroupePedagogique groupePedagogique1 = inscription.getGroupePedagogique();
                AnneeAcademique anneeAca = inscription.getAnneeAcademique();
                String groupeP = JsfUtil.nextGP(groupePedagogique1.getDescription());
                if (groupeP.equals(groupePedagogique1.getDescription())) {
                    inscription.setResultat("T");
                    inscriptionFacade.edit(inscription);
                } else {
                    inscription.setResultat("A");
                    inscriptionFacade.edit(inscription);
                    Inscription defaultInscription = new Inscription();
                    defaultInscription.setAnneeAcademique(anneeAca);
                    defaultInscription.setEtudiant(etudiant1);
                    defaultInscription.setGroupePedagogique(groupePedagogique1);
                    GroupePedagogique gP = groupePedagogiqueFacade.getGroupePedagogique(groupeP);
                    defaultInscription.setGroupePedagogique(gP);
                    inscriptionFacade.create(defaultInscription);
                }
            }
        } catch (Exception e) {
            System.out.println("erreur" + e.getMessage());
        }

    }

}
