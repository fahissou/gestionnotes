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
    
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Inscription> listeInscriptionsGP;
    private List<GroupePedagogique> listGroupePedagogiqueFilter;
    private List<Filiere> listFiliere;
    private Filiere filiere;
    AnneeAcademique currentAnneeAcademique;
   
    private AnneeAcademique anneeAcademiqueChoisi;
//    private AnneeAcademique currentAcademicYear;

    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }

    @PostConstruct
    public void init() {
//        listeInscriptions = inscriptionFacade.findAllByInscription(AnneeAcademiqueBean.getAnneeAcademicChoisi1());
        anneeAcademiqueChoisi = AnneeAcademiqueBean.getAnneeAcademicChoisi1();
        currentAnneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
        listFiliere = filiereFacade.findAll();
        
        prepareCreate();
    }

    public void initGroupePedagogique() {
        if (filiere != null) {
            listGroupePedagogiqueFilter = groupePedagogiqueFacade.getListGpByFilire(filiere);
        } else {
            listGroupePedagogiqueFilter = new ArrayList<>();

        }
    }
    
    public void updateTable() {
        listeInscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique, anneeAcademiqueChoisi);
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
            etudiantFacade.create(newEtudiant);
            etudiantFacade.findAll();
            newInscription.setAnneeAcademique(anneeAcademiqueFacade.getCurrentAcademicYear());
            newInscription.setValidation("V");
            inscriptionFacade.create(newInscription);
            createDiffaultNotes(newInscription,newInscription.getGroupePedagogique());
            msg = JsfUtil.getBundleMsg("InscriptionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeInscriptions = inscriptionFacade.findAll();
            prepareCreate();

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("InscriptionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }

    }
    

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
   
    
   // Liste des etudiants autorisés
    public void genererListeEtudiant() {
        GroupePedagogique gP = groupePedagogique;
        String nomFichier = JsfUtil.generateId();
        String pathOut = JsfUtil.getPathOutTmp();
        String pathIn = JsfUtil.getPathIntModelReleve();
        String pathOutPDF = JsfUtil.getPathOutPDF();
        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdir();
        repertoire2.mkdir();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");

        try {
            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademiqueChoisi.getDescription());
            parametreEntetes.put("filiere", gP.getFiliere().getLibelle() + " :  " + gP.getDescription());
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("N");
            champs.add("M");
            champs.add("NP");
            champs.add("tel");

            // Table contenant les enregistrements du fichier resultat
            List< Map<String, Object>> conteneur = new ArrayList<>();

            for (int j = 0; j < listeInscriptions.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Inscription currentInscription = listeInscriptions.get(j);

                row.put("N", (j + 1));
                row.put("M", currentInscription.getEtudiant().getLogin());
                row.put("NP", currentInscription.getEtudiant().getNom() + " " + currentInscription.getEtudiant().getPrenom());
                row.put("tel", currentInscription.getEtudiant().getTelephone());
                conteneur.add(row);
            }
            
            JsfUtil.generateurXDOCReport(pathIn + "/listeEtudiant.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "listeEtudiant" + "_" + gP.getDescription() + "1", parametreEntetes);
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "listeEtudiant" + gP.getDescription());
            Historiques historique = new Historiques();
            historique.setLibelle("listeEtudiant" + gP.getDescription());
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + "listeEtudiant" + gP.getDescription()));
            historique.setGroupePedagogique(gP.getDescription());
            historique.setDateEdition(JsfUtil.getDateEdition());
            historique.setAnneeAcademique(anneeAcademiqueChoisi);
            historiquesFacade.create(historique);
            System.out.println("OK icic");
//            File fileDowload = new File(pathOutPDF + nomFichier + "listeEtudiant" + gP.getDescription()+".pdf");
//            JsfUtil.flushToBrowser(fileDowload, nomFichier + "listeEtudiant" + gP.getDescription());
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/historiques/'");
        
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
