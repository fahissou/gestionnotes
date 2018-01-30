package bean.inscription;

import ejb.administration.AnneeAcademiqueFacade;
import ejb.formation.FiliereFacade;
import ejb.inscription.EtudiantFacade;
import ejb.inscription.GroupePedagogiqueFacade;
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
import jpa.formation.Filiere;
import jpa.inscription.EnumGenre;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
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
//@Named(value = "inscriptionBean")
//@ViewScoped
public class InscriptionBean implements Serializable {

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
//    private String currentAcademicYear;
    private AnneeAcademique currentAcademicYear;
    private GroupePedagogique groupePedagogique;
    private GroupePedagogique groupePedagogiqueNext;
    private GroupePedagogique groupePedagogiquePrev;
    private String resultats;
    private List<String> listeResultat;
    private Map<Filiere, List<GroupePedagogique>> data1;
    private Map<GroupePedagogique, List<Inscription>> data2; // = new HashMap<>();
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Filiere> listFiliere;
    private Filiere filiere;
//    private AnneeAcademique currentAcademicYear;

    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }

    @PostConstruct
    public void init() {
        listeInscriptions = inscriptionFacade.findAll();
        listFiliere = filiereFacade.findAll();
        listGroupePedagogiques = groupePedagogiqueFacade.findAll();
        prepareCreate();
        currentAcademicYear = anneeAcademiqueFacade.getCurrentAcademicYear();

        listeAnneeUniversitaire = new ArrayList<>();
//        currentAcademicYear = new AnneeAcademique();

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
        map.put("ThÃ¨se 1", "ThÃ¨se 1");
        map.put("ThÃ¨se 2", "ThÃ¨se 2");
        map.put("ThÃ¨se 3", "ThÃ¨se 3");
        data.put("Cycle 3", map);
        System.out.println("ok4");
        loadData();
        System.out.println("ok5");
        // Resultats
        listeResultat = new ArrayList<>();
        listeResultat.add("Admissible");
        listeResultat.add("RÃ©fusÃ©");

    }

    public void loadData() {
        data1 = new HashMap<>();
        data2 = new HashMap<>();
        for (int i = 0; i < listFiliere.size(); i++) {
            List<GroupePedagogique> list1 = groupePedagogiqueFacade.getListGpByFilire(listFiliere.get(i));
            data1.put(listFiliere.get(i), list1);

        }
        for (int j = 0; j < listGroupePedagogiques.size(); j++) {
            List<Inscription> list2 = inscriptionFacade.getListInscriptionByGP(listGroupePedagogiques.get(j).getDescription(), currentAcademicYear.getDescription());
            data2.put(listGroupePedagogiques.get(j), list2);

        }

    }

    public void onFiliereChange() {
        if (filiere != null) {
            listGroupePedagogiques = data1.get(filiere);
        } else {
            listGroupePedagogiques = new ArrayList<>();

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
                prepareCreate();
                listeInscriptions = inscriptionFacade.findAll();
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
                for (Row ligne : sheet) {//parcourir les lignes
                    contenuLigne = new ArrayList<>();
                    compteurLigne++;
                    for (Cell cell : ligne) {//parcourir les colonnes
                        //Ã©valuer le type de la cellule

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
                            newEtudiant.setLogin(String.valueOf(contenuLigne.get(0)));
                            newEtudiant.setNom(String.valueOf(contenuLigne.get(1)));
                            newEtudiant.setPrenom(String.valueOf(contenuLigne.get(2)));
                            EnumGenre enumGenre = EnumGenre.valueOf(String.valueOf(contenuLigne.get(3)));
                            newEtudiant.setGenre(enumGenre);
                            Date dateNaiss = JsfUtil.stringToDate(String.valueOf(contenuLigne.get(4)));
                            newEtudiant.setDateNaissance(dateNaiss);
                            newEtudiant.setTelephone(String.valueOf(contenuLigne.get(5)));
                            newEtudiant.setMail(String.valueOf(contenuLigne.get(6)));
                            etudiantFacade.create(newEtudiant);
                            // Inscritption save
                            newInscription.setEtudiant(newEtudiant);
                            newInscription.setGroupePedagogique(groupePedagogique);
                            newInscription.setAnneeAcademique(currentAcademicYear);
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
                    newNote.setEtatValidation("UENV");
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
            inscriptions = inscriptionFacade.listeReinscription(groupePedagogique.getDescription(), currentAcademicYear.getDescription());
            if(!inscriptions.isEmpty()){
                setListeInscriptions1(inscriptions);
                resul = "succes3";
            }else{
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

    public String updateReinscription() {
        String msg;
        try {

            for (int i = 0; i < listeInscriptions1.size(); i++) {
                listeInscriptions1.get(i).setAnneeAcademique(currentAcademicYear);
                inscriptionFacade.edit(listeInscriptions1.get(i));
                createDiffaultNotes(listeInscriptions1.get(i));
            }
            listeInscriptions = inscriptionFacade.findAll();

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("AnneeAcademiqueError");
            JsfUtil.addErrorMessage(msg);
        }
        return "succes3";
    }

    public String reinscriptionParticuliere() {
        String msg;
        Inscription inscription = null;
        try {
            if (!inscriptionFacade.isInscriptionExist(etudiant.getLogin(), currentAcademicYear.getDescription())) {
                inscription = new Inscription();
                inscription.setAnneeAcademique(currentAcademicYear);
                inscription.setGroupePedagogique(groupePedagogique);
                inscription.setEtudiant(etudiant);
                inscriptionFacade.create(inscription);
                createDiffaultNotes(inscription);
                listeInscriptions = inscriptionFacade.findAll();
                msg = JsfUtil.getBundleMsg("InscriptionDelSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = JsfUtil.getBundleMsg("InscriptionEditErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception e) {

        }
        return null;
    }

}
