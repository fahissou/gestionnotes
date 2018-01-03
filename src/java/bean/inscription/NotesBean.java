/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.odftoolkit.odfdom.converter.pdf.PdfConverter;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
public class NotesBean implements Serializable {

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
        System.out.println("avant lordData");
        loadData();
        System.out.println("après lordData");
        prepareCreate();
        pathIn = new File("").getAbsolutePath() + "/releve/";
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
            List<Ue> list1 = ueFacade.getUeByGroupePedagogique(listGroupePedagogiques.get(i).getDescription());
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
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        try {

            Map<String, Object> pocheParametres = new HashMap<>();
            List<Ue> ues = ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre);
            afficherUE(ues);
            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique.getDescription(), anneeAcademique);
            // Definition des champs du proces 
            List<String> champs = new ArrayList<>();
            champs.add("C");
            champs.add("nom");
            int nombreColonneTable = 7;
            for (int i = 0; i < nombreColonneTable; i++) {
                champs.add("m"+(i+1));
                champs.add("o"+(i+1));
                champs.add("c"+(i+1));
            }
            // Table contenant les enregistrements
            List< Map<String, Object>> conteneur = new ArrayList<>();

            for (int j = 0; j < inscriptions.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Etudiant student = inscriptions.get(j).getEtudiant();

                // Enregistrement des coordonnées de 
                pocheParametres.put("aa", inscriptions.get(j).getAnneeUniversitaire());
                pocheParametres.put("a", inscriptions.get(j).getAnneeUniversitaire().split("- ")[1].trim());
                pocheParametres.put("nom", student.getNom());
                pocheParametres.put("prenom", student.getPrenom());
//                pocheParametres.put("dateNaiss", student.getDateCreation().toString());
//                pocheParametres.put("filiere", student.getGroupePedagogique().getDescription());
                row.put("C", (j+1));
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
                    nombreCreditValider = +isValide(moyUE, currentUE.getCredit());
                    somMoySemestre = +moyUE;
                    pocheParametres.put("N" + (i + 1), formatNote(moyUE));
                    pocheParametres.put("O" + (i + 1), decision(moyUE));
                    pocheParametres.put("C" + (i + 1), currentUE.getCredit());
                    pocheParametres.put("S" + (i + 1), inscriptions.get(j).getAnneeUniversitaire());
                    row.put("m" + (i + 1), formatNote(moyUE));
                    row.put("o" + (i + 1), decision2(moyUE));
                    row.put("c" + (i + 1), currentUE.getCredit());
                }
                double moyenneSemestre = somMoySemestre / ues.size();
                pocheParametres.put("T", nombreCreditValider);
                pocheParametres.put("M", moyenneSemestre);
                conteneur.add(row);
//                pocheParametres.put("ds", decision1(somMoySemestre));
                genererRelevetNotes(pathIn + "/relevetS7.docx", pocheParametres, repertoire.getAbsolutePath() + "/", student.getNom() + student.getPrenom() + student.getLogin());
//                  genererRelevetNotes(pathIn+"/relevetS7.docx", pocheParametres, repertoire.getAbsolutePath()+"/","IGISA2");
//                  genererRelevetNotes(pathIn+"/test.docx", pocheParametres, pathOut,"IGISA1");
            }
            genererProcesVerval(pathIn + "/proces.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/","Proces_Verbale"+"_"+groupePedagogique.getDescription());

            
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

    }

    // essai de gestion de releve de notes en continu 
    
    public void genererRelevet2() {

        File repertoire = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle() + "/");
        File repertoire1 = new File(pathOut + "/" + groupePedagogique.getDescription() + "_" + anneeAcademique + "/");
        repertoire.mkdir();    //création d'un repertoir de stockage des relevets pour la promotion
        repertoire1.mkdir();
        try {

            
            List<Ue> ues = ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre);
            afficherUE(ues);
            List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique.getDescription(), anneeAcademique);
            // Definition des champs du proces 
            List<String> champs = new ArrayList<>();
            champs.add("C");
            champs.add("nom");
            int nombreColonneTable = 7;
            for (int i = 0; i < nombreColonneTable; i++) {
                champs.add("m"+(i+1));
                champs.add("o"+(i+1));
                champs.add("c"+(i+1));
            }
            
            // les variables
            
            int nombreVariables = 47;
            List<String> champs1 = new ArrayList<>();
            for (int i = 0; i < nombreVariables; i++) {
                champs1.add("var"+(i+1));
            }
            
            // Table contenant les enregistrements des releves de notes
            List< Map<String, Object>> conteneur1 = new ArrayList<>();
            
            // Table contenant les enregistrements du proces
            List< Map<String, Object>> conteneur = new ArrayList<>();

            for (int j = 0; j < inscriptions.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Map<String, Object> pocheParametres = new HashMap<>();
                Etudiant student = inscriptions.get(j).getEtudiant();

                // Enregistrement des coordonnées de 
                pocheParametres.put("var1", inscriptions.get(j).getAnneeUniversitaire().split("- ")[1].trim());
                pocheParametres.put("var2", inscriptions.get(j).getAnneeUniversitaire());
                pocheParametres.put("var3", student.getNom());
                pocheParametres.put("var4", student.getPrenom());
                pocheParametres.put("var5", "Unités d'Enseignement");
                pocheParametres.put("var6", "Notes sur 20");
                pocheParametres.put("var7", "Observations");
                pocheParametres.put("var8", "Crédits");
                pocheParametres.put("var9", "Session");
//                pocheParametres.put("dateNaiss", student.getDateCreation().toString());
//                pocheParametres.put("filiere", student.getGroupePedagogique().getDescription());
                row.put("C", (j+1));
                row.put("nom", student.getNom() + " " + student.getPrenom());
                int nombreCreditValider = 0;
                double somMoySemestre = 0;
                int compt = 10;
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
                    nombreCreditValider = +isValide(moyUE, currentUE.getCredit());
                    somMoySemestre = +moyUE;
                    pocheParametres.put("var" + (compt), currentUE.getLibelle());
                    pocheParametres.put("var" + (compt+1), formatNote(moyUE));
                    pocheParametres.put("var" + (compt+2), decision(moyUE));
                    pocheParametres.put("var" + (compt+3), currentUE.getCredit());
                    pocheParametres.put("var" + (compt+4), inscriptions.get(j).getAnneeUniversitaire());
                    row.put("m" + (i + 1), formatNote(moyUE));
                    row.put("o" + (i + 1), decision2(moyUE));
                    row.put("c" + (i + 1), currentUE.getCredit());
                    compt = compt + 5;
                }
                double moyenneSemestre = somMoySemestre / ues.size();
                pocheParametres.put("var45", nombreCreditValider);
                pocheParametres.put("var46", moyenneSemestre);
                conteneur.add(row);
                conteneur1.add(pocheParametres);
//                pocheParametres.put("ds", decision1(somMoySemestre));
//                genererRelevetNotes(pathIn + "/relevetS7.docx", pocheParametres, repertoire.getAbsolutePath() + "/", student.getNom() + student.getPrenom() + student.getLogin());
//                  genererRelevetNotes(pathIn+"/relevetS7.docx", pocheParametres, repertoire.getAbsolutePath()+"/","IGISA2");
//                  genererRelevetNotes(pathIn+"/test.docx", pocheParametres, pathOut,"IGISA1");
            }
            genererProcesVerval(pathIn + "/relevetNotesS7.docx", champs1, conteneur1, "T", repertoire.getAbsolutePath() + "/","Releves"+"_"+groupePedagogique.getDescription());
            genererProcesVerval(pathIn + "/proces.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/","Proces_Verbale"+"_"+groupePedagogique.getDescription());

            
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }

    }
    
    
    // fin de gestion de releve de notes 
    
    
    
    
    
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

    public void gnererEtat() {
        String nomRep = genereNomFichier() + listeNotesGroupPeda.get(0).getInscription().getGroupePedagogique() + listeNotesGroupPeda.get(0).getInscription().getAnneeUniversitaire();
        File repertoire = new File(pathOut + "/semestre1/" + nomRep + "/");
        repertoire.mkdir(); // création d'un repertoir de stockage des relevets pour la promotion
        Map<String, Object> maps = new HashMap<>();

    }

    public String genereNomFichier() {
        String outputFile = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date()) + "bulletin";
        return outputFile;
    }

    public String genererRelevetNotes(String fichier, Map<String, Object> maps, String chemin, String nomfichier) throws Exception {
        String outputFile = "";
        OutputStream out = null;
        InputStream in = null;

//        genererRelevetNotes1();
        try {

            in = new FileInputStream(new File(fichier));
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            System.out.println("ok ici");
            // 3) Create context Java model
            IContext context = report.createContext();
            context.putMap(maps);

            FieldsMetadata metadata = new FieldsMetadata();
            report.setFieldsMetadata(metadata);

//            Options options = Options.getTo(ConverterTypeTo.PDF);
            outputFile = nomfichier + ".docx";
            // 4) Generate report by merging Java model with the Docx
            out = new FileOutputStream(new File(chemin + outputFile));
            report.process(context, out);

            out.close();
        } catch (IOException | XDocReportException ex) {
            System.out.println(" Exce " + ex.getMessage());
        }
        return outputFile;
    }

    public boolean genererProcesVerval(String fichier, List<String> champs, List< Map<String, Object>> conteneur, String tableName, String chemin, String fileName) {
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

    public String genererRelevetNotes1() throws Exception {
        String outputFile = "";
        OutputStream out = null;
        InputStream in = null;
        try {

            in = new FileInputStream(new File("C:\\Users\\AHISSOU Florent\\Desktop\\florent.odt"));
            System.out.println("ok avant");
            OdfTextDocument document = OdfTextDocument.loadDocument(in);
            System.out.println("ok après");
            // 2) Prepare Pdf options
            PdfOptions options = PdfOptions.create();

            // 3) Convert OdfTextDocument to PDF via IText
            out = new FileOutputStream(new File("C:\\Users\\AHISSOU Florent\\Desktop\\florent1.pdf"));
            PdfConverter.getInstance().convert(document, out, options);

            out.close();
        } catch (IOException | XDocReportException ex) {
            System.out.println(" Exce " + ex.getMessage());
        }
        return outputFile;
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

}
