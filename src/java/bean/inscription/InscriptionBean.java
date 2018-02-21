package bean.inscription;

import ejb.inscription.AnneeAcademiqueFacade;
import ejb.formation.FiliereFacade;
import ejb.formation.HistoriquesFacade;
import ejb.inscription.EtudiantFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.UeFacade;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.ByteArrayInputStream;
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
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import jpa.inscription.AnneeAcademique;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.EnumGenre;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.ExceptionsGestionnotes;
import util.JsfUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.primefaces.context.RequestContext;



/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "inscriptionBean")
@ViewScoped
public class InscriptionBean implements Serializable {
    @EJB
    private HistoriquesFacade historiquesFacade;

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;

    @EJB
    private FiliereFacade filiereFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;

    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private NotesFacade notesFacade;

    @EJB
    private EtudiantFacade etudiantFacade;

    private Etudiant newEtudiant;
    private Etudiant etudiant;

    @EJB
    private InscriptionFacade inscriptionFacade;
    private Inscription selectedInscription;
    private Inscription newInscription;
    private List<Inscription> listeInscriptions;
    private List<Inscription> listeInscriptions1;
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
    private String typeReinscription;
    private String fileExtension;
    private FileInputStream fichier;
    private Notes newNote;

    private GroupePedagogique groupePedagogique;
    private GroupePedagogique groupePedagogiqueNext;
    private GroupePedagogique groupePedagogiquePrev;
    private String resultats;
    private List<String> listeResultat;
    private Map<Filiere, List<GroupePedagogique>> data1;
    private Map<GroupePedagogique, List<Inscription>> data2; // = new HashMap<>();
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Inscription> listeInscriptionsGP;
    private List<GroupePedagogique> listGroupePedagogiqueFilter;
    private List<Filiere> listFiliere;
    private Filiere filiere;
    private String pathIn;
    private String pathIn1;
    private String pathOut1;
    private String pathIn3;
    private String pathOut;
//    private AnneeAcademique currentAcademicYear;

    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }

    @PostConstruct
    public void init() {
        listeInscriptions = inscriptionFacade.findAllByInscription(AnneeAcademiqueBean.getAnneeAcademicChoisi1());
        listFiliere = filiereFacade.findAll();
        
        prepareCreate();

        
        pathIn = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/";
        pathIn1 = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/resources/releve/releveNouveau/";
        pathOut = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/";
        pathOut1 = System.getProperty("user.home") + "\\Documents\\" + "/NetBeansProjects/gestionnotes/web/fichiergenerer/rapportgestionnotes/touslesrapports/";

    }

    public void initGroupePedagogique() {
        if (filiere != null) {
            listGroupePedagogiqueFilter = groupePedagogiqueFacade.getListGpByFilire(filiere);
        } else {
            listGroupePedagogiqueFilter = new ArrayList<>();

        }
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

    public List<GroupePedagogique> getListGroupePedagogiqueFilter() {
        return listGroupePedagogiqueFilter;
    }

    public void setListGroupePedagogiqueFilter(List<GroupePedagogique> listGroupePedagogiqueFilter) {
        this.listGroupePedagogiqueFilter = listGroupePedagogiqueFilter;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public String getTypeReinscription() {
        return typeReinscription;
    }

    public void setTypeReinscription(String typeReinscription) {
        this.typeReinscription = typeReinscription;
    }

    public GroupePedagogique getGroupePedagogiqueNext() {
        return groupePedagogiqueNext;
    }

    public void setGroupePedagogiqueNext(GroupePedagogique groupePedagogiqueNext) {
        this.groupePedagogiqueNext = groupePedagogiqueNext;
    }

    public GroupePedagogique getGroupePedagogiquePrev() {
        return groupePedagogiquePrev;
    }

    public void setGroupePedagogiquePrev(GroupePedagogique groupePedagogiquePrev) {
        this.groupePedagogiquePrev = groupePedagogiquePrev;
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

    public List<String> getListeResultat() {
        return listeResultat;
    }

    public void setListeResultat(List<String> listeResultat) {
        this.listeResultat = listeResultat;
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

    public void reset(ActionEvent e) {
        this.newInscription.reset();
       
        this.groupePedagogique.reset();
        this.listFiliere = null;
        this.filiere.reset();
    }

    public String getResultats() {
        return resultats;
    }

    public void setResultats(String resultats) {
        this.resultats = resultats;
    }

    public void onCycleChange() {
        if (cycle != null && !cycle.equals("")) {
            listeNiveau = data.get(cycle);
        } else {
            listeNiveau = new HashMap<String, String>();
        }
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public List<Inscription> getListeInscriptions1() {
        return listeInscriptions1;
    }

    public void setListeInscriptions1(List<Inscription> listeInscriptions1) {
        this.listeInscriptions1 = listeInscriptions1;
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

    public List<GroupePedagogique> getListesGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListesGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
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
                insertInscription(fichier);
                msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
//                prepareCreate();
//                listeInscriptions = inscriptionFacade.findAll();
            } else {
                System.out.println("fichier non chargé");
            }
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueError");
            JsfUtil.addErrorMessage(msg);
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
                List<Inscription> listeInscriptionsCurrent = new ArrayList<>();
                for (Row ligne : sheet) {//parcourir les lignes
                    contenuLigne = new ArrayList<>();
                    compteurLigne++;
                    for (Cell cell : ligne) {//parcourir les colonnes
                        //Evaluer le type de la cellule

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
                        if (contenuLigne.size() == 8) {
                            newEtudiant.setLogin(String.valueOf(contenuLigne.get(0)));
                            newEtudiant.setNom(String.valueOf(contenuLigne.get(1)));
                            newEtudiant.setPrenom(String.valueOf(contenuLigne.get(2)));
                            EnumGenre enumGenre = EnumGenre.valueOf(String.valueOf(contenuLigne.get(3)));
                            newEtudiant.setGenre(enumGenre);
                            Date dateNaiss = JsfUtil.stringToDate(String.valueOf(contenuLigne.get(4)));
                            newEtudiant.setDateNaissance(dateNaiss);
                            newEtudiant.setLieuNaissance(String.valueOf(contenuLigne.get(5)));
                            newEtudiant.setTelephone(String.valueOf(contenuLigne.get(6)));
                            newEtudiant.setMail(String.valueOf(contenuLigne.get(7)));
                            
                            newInscription.setAnneeAcademique(anneeAcademiqueFacade.getCurrentAcademicYear());
                            newInscription.setValidation("V");
                            etudiantFacade.create(newEtudiant);
                            etudiantFacade.findAll();
                            newInscription.setEtudiant(newEtudiant);
                            newInscription.setGroupePedagogique(groupePedagogique);
                            
                            // Inscritption save
                            inscriptionFacade.create(newInscription);
                            createDiffaultNotes(newInscription,groupePedagogique);
                            prepareCreate();
                            listeInscriptions = inscriptionFacade.findAll();
                            
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

//    public void createDiffaultNotes(Inscription inscription, groupePedagogique) {
//        List<Ue> listeUe = ueFacade.getUeByGroupePedagogique(groupePedagogique);
//        try {
//            if (!listeUe.isEmpty()) {
//                for (int i = 0; i < listeUe.size(); i++) {
//                    List<Matiere> matieres = matiereFacade.getMatiereByUe(listeUe.get(i));
//                    newNote = new Notes();
//                    for (int j = 0; j < matieres.size(); j++) {
//                        newNote.setMatiere(matieres.get(j));
//                        newNote.setInscription(inscription);
//                        newNote.setEtatValidation("UENV");
//                        newNote.setNote(0.0);
//                        notesFacade.create(newNote);
//                    }
//
//                }
//            }
//
//        } catch (Exception ex) {
//
//        }
//
//    }

    public void inscriptionRedirect() {
        switch (typeInscription) {
            case "collective":
                RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/inscription/inscription/collective/'");
                break;
            case "individuelle":
                RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/inscription/inscription/individuelle/'");
                break;
        }
    }

    public String reinscriptionRedirect() {
        String page = "";
        switch (typeReinscription) {
            case "reinscription":
                page = this.typeReinscription;
                break;
            case "particulier":
                page = this.typeReinscription;
                break;
        }
        return page;
    }

    public String affichage3() {
        String msg;
        String resul = "";
        listeInscriptions1 = new ArrayList<>();
        System.out.println("icic");

        List<Inscription> inscriptions = null;
//        inscriptions = inscriptionFacade.listeReinscription(groupePedagogique.getDescription(), currentAcademicYear.getDescription());
        System.out.println("taille "+inscriptions.size());
        if (!inscriptions.isEmpty()) {
            setListeInscriptions1(inscriptions);
            resul = "succes3";
        } else {
            msg = JsfUtil.getBundleMsg("CreateNewAcademicYear");
            JsfUtil.addErrorMessage(msg);
        }
        return resul;
    }

    public void onGroupePedagogiqueChange() {
        if (groupePedagogique != null) {
            listeInscriptions = data2.get(groupePedagogique);
        } else {
            listeInscriptions = new ArrayList<>();
        }
    }
    
    public void onGroupePedagogiqueChange1() {
        if (groupePedagogique != null) {
            listeInscriptionsGP = data2.get(groupePedagogique);
        } else {
            listeInscriptionsGP = new ArrayList<>();
        }
    }

    public List<Inscription> getListeInscriptionsGP() {
        return listeInscriptionsGP;
    }

    public void setListeInscriptionsGP(List<Inscription> listeInscriptionsGP) {
        this.listeInscriptionsGP = listeInscriptionsGP;
    }

    
    public String updateReinscription() {
        String msg;
        try {

            for (int i = 0; i < listeInscriptions1.size(); i++) {
//                listeInscriptions1.get(i).setAnneeAcademique(currentAcademicYear);
                inscriptionFacade.edit(listeInscriptions1.get(i));
                createDiffaultNotes(listeInscriptions1.get(i), groupePedagogique);
            }
            listeInscriptions = inscriptionFacade.findAll();

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueError");
            JsfUtil.addErrorMessage(msg);
        }
        return "succes3";
    }
//
//    public String reinscriptionParticuliere() {
//        String msg;
//        Inscription inscription = null;
//        try {
//            if (!inscriptionFacade.isInscriptionExist(etudiant.getLogin(), currentAcademicYear.getDescription())) {
//                inscription = new Inscription();
//                inscription.setAnneeAcademique(currentAcademicYear);
//                inscription.setGroupePedagogique(groupePedagogique);
//                inscription.setEtudiant(etudiant);
//                inscriptionFacade.create(inscription);
//                createDiffaultNotes(inscription);
//                listeInscriptions = inscriptionFacade.findAll();
//                msg = JsfUtil.getBundleMsg("InscriptionDelSuccessMsg");
//                JsfUtil.addSuccessMessage(msg);
//            } else {
//                msg = JsfUtil.getBundleMsg("InscriptionEditErrorMsg");
//                JsfUtil.addErrorMessage(msg);
//            }
//
//        } catch (Exception e) {
//
//        }
//        return null;
//    }
//    
    
    // Liste des etudiants autorisés
//    public String genererListeEtudiant() {
//        GroupePedagogique gP = groupePedagogique;
//        String nomFichier = JsfUtil.generateId();
//        File repertoire1 = new File(pathOut + "/tmp2/");
//        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
//        repertoire1.mkdir();
//        repertoire2.mkdir();
//        deleteFile(repertoire1.getAbsolutePath() + "/");
//        deleteFile(repertoire2.getAbsolutePath() + "/");
//
//        try {
//            // Paramètres d'entete du resultat final
//            Map<String, Object> parametreEntetes = new HashMap<>();
//            parametreEntetes.put("aa", currentAcademicYear.getDescription());
//            parametreEntetes.put("filiere", gP.getFiliere().getLibelle() + " :  " + gP.getDescription());
//            parametreEntetes.put("d", JsfUtil.getDateEdition());
//            // Definition des champs du proces fichier 1
//            List<String> champs = new ArrayList<>();
//            champs.add("N");
//            champs.add("NP");
//
//            // Table contenant les enregistrements du fichier resultat
//            List< Map<String, Object>> conteneur = new ArrayList<>();
//
//            for (int j = 0; j < listeInscriptionsGP.size(); j++) {
//                Map<String, Object> row = new HashMap<>();
//                Inscription currentInscription = listeInscriptionsGP.get(j);
//
//                row.put("N", (j + 1));
//                row.put("NP", currentInscription.getEtudiant().getNom() + " " + currentInscription.getEtudiant().getPrenom());
//                
//                conteneur.add(row);
//            }
//
//            genererProcesVerval(pathIn + "/listeEtudiant.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "listeEtudiant" + "_" + gP.getDescription() + "1", parametreEntetes);
//            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
////            docxToPDF(repertoire1.getAbsolutePath() + "/", pathOut1);
//            System.out.println("Ok1");
//            mergePDF(repertoire2.getAbsolutePath() + "/", pathOut1, nomFichier + "listeEtudiant" + gP.getDescription());
//            System.out.println("OK2");
//            Historiques historique = new Historiques();
//            historique.setLibelle("listeEtudiant" + gP.getDescription());
//            historique.setLienFile(JsfUtil.getRealPath(pathOut1 + nomFichier + "listeEtudiant" + gP.getDescription()));
//            historique.setGroupePedagogique(gP.getDescription());
//            historique.setDateCreation(new Date());
//            historiquesFacade.create(historique);
//        } catch (Exception ex) {
//            System.out.println("Exception " + ex.getMessage());
//
//        }
//        return "success4";
//    }

    public void deleteFile(String folderName) {
        File repertoire = new File(folderName);
        File[] files = repertoire.listFiles();
        try {
            if (files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    boolean bool = files[i].delete();
                }
            }
        } catch (Exception e) {
        }
    }
    
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
    
    //*********************
    public void mergePDF(String folderName, String pathOut, String fileName) throws IOException {
        System.out.println("toto1");
        File repertoire = new File(folderName);
        System.out.println("toto2");
        File[] files = repertoire.listFiles();
        System.out.println("toto3");
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        System.out.println("icicc " + files.length);
        try {
            for (int i = 0; i < files.length; i++) {
                PDFmerger.addSource(files[i]);
            }
            PDFmerger.setDestinationFileName(pathOut + fileName + ".pdf");
            PDFmerger.mergeDocuments();
        } catch (Exception e) {
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
    
    public void createDiffaultNotes(Inscription inscription, GroupePedagogique groupePedagogique1) {
        List<Ue> listeUe = ueFacade.getUeByGroupePedagogique(groupePedagogique1);
        try {
            if (!listeUe.isEmpty()) {
                for (int i = 0; i < listeUe.size(); i++) {
                    List<Matiere> matieres = matiereFacade.getMatiereByUe(listeUe.get(i));
                    newNote = new Notes();
                    for (int j = 0; j < matieres.size(); j++) {
                        newNote.setMatiere(matieres.get(j));
                        newNote.setInscription(inscription);
                        newNote.setEtatValidation("UENV");
                        newNote.setNote(0.0);
                        notesFacade.create(newNote);
                    }

                }
            }

        } catch (Exception ex) {

        }

    }
    
}
