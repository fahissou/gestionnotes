/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.inscription;

import bean.administration.JournalisationBean;
import bean.authentificate.AuthentificateBean;
import ejb.administration.JournalisationFacade;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.administration.NotificationFacade;
import ejb.administration.ParametresFacade;
import ejb.administration.ProgrammerCoursFacade;
import ejb.administration.UtilisateurFacade;
import ejb.formation.FiliereFacade;
import ejb.formation.HistoriquesFacade;
import ejb.inscription.EnseignantFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import jpa.administration.Journalisation;
import jpa.inscription.AnneeAcademique;
import jpa.administration.Notification;
import jpa.administration.Parametres;
import jpa.administration.ProgrammerCours;
import jpa.administration.Utilisateur;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.Enseignant;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "notesBean")
@ViewScoped

public class NotesBean implements Serializable {
    @EJB
    private JournalisationFacade journalisationFacade;

    @EJB
    private ProgrammerCoursFacade programmerCoursFacade;
    @EJB
    private ParametresFacade parametresFacade;
    @EJB
    private EnseignantFacade enseignantFacade;

    @EJB
    private HistoriquesFacade historiquesFacade;

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
    private Etudiant etudiant = null;
    private AnneeAcademique anneeAcademique;
    private AnneeAcademique anneeAcademiqueObjet;
    private Matiere newMatiere;
    private GroupePedagogique newGroupePedagogique;
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

    private Semestre semestre;
    private List<Semestre> semestres;
    private List<Semestre> listeSemestre;
    private List<Notes> listeNotesEnregistrement;
    private List<Filiere> listFiliere;
    private Filiere filiere;
    private List<Notes> listeOldNotes;
    private Enseignant enseignant;
    private List<GroupePedagogique> listGroupePedagogiqueEtudiant;
    private String signataire;
    private List<String> listeSignataires;
    List<GroupePedagogique> list1GP;
    private String notification;
    private String color;

    /**
     * Creates a new instance of NotesBean
     */
    public NotesBean() {
        newUtilisateur = new Utilisateur();
        newNotification = new Notification();
    }

    @PostConstruct
    public void init() {

        listeNotess = notesFacade.findAll();
        listFiliere = filiereFacade.findAll();
        anneeAcademique = AnneeAcademiqueBean.getAnneeAcademicChoisi1();
        prepareCreate();

    }

    public void initGroupePedagogique() {
        if (filiere != null) {
            listGroupePedagogiques1 = groupePedagogiqueFacade.getListGpByFilire(filiere);
            notification = "";
        } else {
            notification = "";
            listGroupePedagogiques1 = new ArrayList<>();
        }
    }

    public void initUE() {
        if (groupePedagogique != null) {
            notification = "";
            ues = ueFacade.getUeByGroupePedagogique(groupePedagogique);
        } else {
            ues = new ArrayList<>();
            notification = "";
        }

    }

    public void initMatiere() {
        if (ue != null) {
            tmpMatieres = matiereFacade.getMatiereByUe(ue);
            notification = "";
        } else {
            tmpMatieres = new ArrayList<>();
            notification = "";
        }
    }

    public void updateTable() {
        if (newMatiere != null) {
            listeNotesGroupPeda = notesFacade.listeNoteGpAnnee1(anneeAcademique, groupePedagogique, newMatiere);
        } else {
            listeNotesGroupPeda = new ArrayList<>();
        }
    }

    public void updateTablesN() {
        if (newMatiere != null) {
            enseignant = programmerCoursFacade.findEnseignantByMatiere(newMatiere);
            if (enseignant != null) {
                color = "blue";
                listeNotesEnregistrement = notesFacade.listeNotesNonValide(anneeAcademique, groupePedagogique, newMatiere);
                notification = "Enseignant : " + JsfUtil.getLabelGradeEnseignant(enseignant.getGrade()) + " " + enseignant.getPrenom() + " " + enseignant.getNom();
            } else {
                notification = "Cette matière n'est pas encore programmée !";
                listeNotesEnregistrement = new ArrayList<>();
                color = "red";
            }
        } else {
            notification = "";
            listeNotesEnregistrement = new ArrayList<>();
        }
    }

    public void updateTable1() {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique, newMatiere);
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);

            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<GroupePedagogique> getList1GP() {
        return list1GP;
    }

    public void setList1GP(List<GroupePedagogique> list1GP) {
        this.list1GP = list1GP;
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
            updateUeNotes(selectedNotes, selectedNotes.getInscription());
            createJournale("Modification de la note de "+selectedNotes.getMatiere().getLibelle()+" ( "+selectedNotes.getInscription().getEtudiant().getNom()+" "+selectedNotes.getInscription().getEtudiant().getPrenom()+" )");
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

    public String getSignataire() {
        return signataire;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
    }

    public List<String> getListeSignataires() {
        return listeSignataires;
    }

    public void setListeSignataires(List<String> listeSignataires) {
        this.listeSignataires = listeSignataires;
    }

    public void reset(ActionEvent e) {
        this.newNotes.reset();
        this.listGroupePedagogiques1 = null;
        this.ues = null;
        this.tmpMatieres = null;
        this.filiere = null;
        this.groupePedagogique = null;
        this.ue = null;
        this.newMatiere = null;

    }

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
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

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
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

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public List<GroupePedagogique> getListGroupePedagogiqueEtudiant() {
        return listGroupePedagogiqueEtudiant;
    }

    public void setListGroupePedagogiqueEtudiant(List<GroupePedagogique> listGroupePedagogiqueEtudiant) {
        this.listGroupePedagogiqueEtudiant = listGroupePedagogiqueEtudiant;
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
                            inscription = inscriptionFacade.getInscriptionsByEtudiant(String.valueOf(contenuLigne.get(0)), anneeAcademique.getDescription());
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

    public void affichage(ActionEvent event) {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique, newMatiere);
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);

            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }
//      RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/insertionconsultation/notes/affichage/'");

    }

    public String affichage1() {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique, newMatiere);
            if (!liste.isEmpty()) {
                setListeNotesGroupPeda(liste);
            } else {
                listeNotesGroupPeda = new ArrayList<>();
            }
        } catch (Exception ex) {
        }

        return "succes1";
    }

    public void affichageNotes() {
        liste = new ArrayList<>();
        try {
            liste = notesFacade.listeNotesNonValide(anneeAcademique, groupePedagogique, newMatiere);
            setGroupePedagogique(groupePedagogique);
            setEnseignant(enseignant);
            if (!liste.isEmpty()) {
                setListeNotesEnregistrement(liste);
            } else {
                listeNotesEnregistrement = new ArrayList<>();
            }

        } catch (Exception e) {

        }
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/insertionconsultation/notes/inserernotes/affichage/'");
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

    public void updateNotes() {
        try {
            for (int i = 0; i < listeNotesEnregistrement.size(); i++) {
                Inscription inscription1 = listeNotesEnregistrement.get(i).getInscription();
                if (listeNotesEnregistrement.get(i).getNote() < groupePedagogique.getParametres().getMoyenneMatiere()) {
                    listeNotesEnregistrement.get(i).setOldNote(listeNotesEnregistrement.get(i).getNote());
                }
                listeNotesEnregistrement.get(i).setSessions(anneeAcademique.getDescription());
                notesFacade.edit(listeNotesEnregistrement.get(i));
                updateUeNotes(listeNotesEnregistrement.get(i), inscription1);
            }
            createJournale("Enregistrement des notes de "+newMatiere.getLibelle()+"( "+groupePedagogique.getDescription()+" )");
            ProgrammerCours programmerCours = programmerCoursFacade.findProgrCoursByAnneeGpMat(groupePedagogique, anneeAcademique, newMatiere);
            programmerCours.setEtat("#33be40");
            programmerCoursFacade.edit(programmerCours);
            liste = new ArrayList<>();
            liste = notesFacade.listeNoteGpAnnee(anneeAcademique, groupePedagogique, newMatiere);
            setListeNotesGroupPeda(liste);
        } catch (Exception e) {
            System.out.println("Error "+e.getMessage());
        }

        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/insertionconsultation/notes/affichage/'");
    }
    
    public void createJournale(String texte) {
            Journalisation journalisation = new Journalisation();
            journalisation.setDateAction(JsfUtil.getDateEdition());
            AuthentificateBean authentificateBean = new AuthentificateBean();
            String idUser = authentificateBean.getIdUserConnected();
            Utilisateur user = utilisateurFacade.find(idUser);
            journalisation.setDescriptionActeurs(user.getNom() + " "+user.getPrenom());
            journalisation.setDescriptionAction(texte);
            journalisationFacade.create(journalisation);
    }
    
    
    
    
    public void updateUeNotes(Notes notes, Inscription inscription) {

        List<Matiere> matieres = matiereFacade.getMatiereByUe(ue);
        double som = 0.0;
        int compteur = 0;
        for (int j = 0; j < matieres.size(); j++) {
            Notes note = notesFacade.getNotesByInscriptionMatiere(notes.getInscription(), matieres.get(j));
            som = som + note.getNote();
            if (note.getNote() == 0.0) {
                compteur++;
            }
        }
        double moyUE = som / matieres.size();

        if (moyUE >= groupePedagogique.getParametres().getMoyenneUE() && compteur == 0) {
            int compteurTotalUe = getTotalCreditUe(groupePedagogique);
            int compteurEtu = inscription.getCompteurCredit() + ue.getCredit();
            inscription.setCompteurCredit(compteurEtu);
            if (groupePedagogique.getParametres().getProportionAdmission() == 1) {
                if(compteurEtu == compteurTotalUe) {
                    inscription.setResultat("T");
                }
                
            } else {
                if (compteurEtu == compteurTotalUe) {
                    inscription.setResultat("T");
                } else if (compteurEtu == compteurTotalUe * groupePedagogique.getParametres().getProportionAdmission()) {
                    inscription.setResultat("A");
                }

            }
            inscriptionFacade.edit(inscription);
            // mise à jour des notes de l'UE
            for (int k = 0; k < matieres.size(); k++) {
                Notes note = notesFacade.getNotesByInscriptionMatiere(notes.getInscription(), matieres.get(k));
                note.setEtatValidation("UEV");
                note.setSessions(anneeAcademique.getDescription());
                notesFacade.edit(note);
            }
        }

    }

    public int getTotalCreditUe(GroupePedagogique groupePedagogique) {
        List<Ue> ues = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique));
        int compteur = 0;
        for (int i = 0; i < ues.size(); i++) {
            compteur += ues.get(i).getCredit();
        }
        return compteur;
    }

    public void afficherUE(List<Ue> ues) {
        for (int i = 0; i < ues.size(); i++) {
            System.out.println(ues.get(i).getLibelle());
        }

    }

    // essai de proces dynamique
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
                if (i == 0) {
                    var += arg.get(i);
                } else {
                    var += " ; " + arg.get(i);
                }

            }
        }
        return var;
    }

    public String genereNomFichier() {
        String outputFile = new SimpleDateFormat("ddMMyyyyHHmmSSsss", Locale.FRENCH).format(new Date()) + "bulletin";
        return outputFile;
    }

    public String decision(double note) {
        String arg = "NON VALIDER";
        if (note >= 12.0) {
            arg = "VALIDER";
        }
        return arg;
    }

    public String affichage3() {

        liste = new ArrayList<>();
        String resultat = "";
        String msg;
        try {
            liste = notesFacade.listeNoteGpAnnee(anneeAcademiqueObjet, groupePedagogique, newMatiere);
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

    public List<GroupePedagogique> getListGrPByEtudiant(List<Inscription> inscriptions) {
        List<GroupePedagogique> gp = new ArrayList<>();
        for (int i = 0; i < inscriptions.size(); i++) {
            gp.add(inscriptions.get(i).getGroupePedagogique());
        }
        return gp;
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

    // Feuille notes
    public void genererFeuilleNotes() {
        String pathOut = JsfUtil.getPathOutTmp();
        String pathIn = JsfUtil.getPathIntModelReleve();
        String pathOutPDF = JsfUtil.getPathOutPDF();
        String nomFichier = JsfUtil.generateId();
        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdir();
        repertoire2.mkdir();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");

        try {
            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademique.getDescription());
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
            parametreEntetes.put("matiere", newMatiere.getLibelle());
            parametreEntetes.put("semestre", ue.getSemestre().getValeur());
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            enseignant = programmerCoursFacade.findEnseignantByMatiere(newMatiere);
            parametreEntetes.put("enseignant", JsfUtil.getLabelGradeEnseignant(enseignant.getGrade()) + " " + enseignant.getPrenom() + " " + enseignant.getNom());
            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("N");
            champs.add("M");
            champs.add("NP");
            champs.add("N");
            champs.add("Ob");

            // Table contenant les enregistrements du fichier resultat
            List< Map<String, Object>> conteneur = new ArrayList<>();
            for (int j = 0; j < listeNotesGroupPeda.size(); j++) {
                Map<String, Object> row = new HashMap<>();
                Notes currentNotes = listeNotesGroupPeda.get(j);
                row.put("N", (j + 1));
                row.put("M", currentNotes.getInscription().getEtudiant().getLogin());
                row.put("NP", currentNotes.getInscription().getEtudiant().getNom() + " " + currentNotes.getInscription().getEtudiant().getPrenom());
                row.put("No", JsfUtil.formatNote(currentNotes.getNote()));
                row.put("Ob", decision(currentNotes.getNote()));
                conteneur.add(row);
            }

            JsfUtil.generateurXDOCReport(pathIn + "/feuilleNotes.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "FeuilleNotes" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes);
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "FeuilleNotes" + groupePedagogique.getDescription());
            Historiques historique = new Historiques();
            historique.setLibelle("FeuilleNotes" + groupePedagogique.getDescription());
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + "FeuilleNotes" + groupePedagogique.getDescription()));
            historique.setGroupePedagogique(groupePedagogique.getDescription());
            historique.setDateEdition(JsfUtil.getDateEdition());
            historique.setAnneeAcademique(anneeAcademique);
            historiquesFacade.create(historique);
            File fileDowload = new File(pathOutPDF + nomFichier + "FeuilleNotes" + groupePedagogique.getDescription() + ".pdf");
            JsfUtil.flushToBrowser(fileDowload, nomFichier + "FeuilleNotes" + groupePedagogique.getDescription() + ".pdf");
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }
    }

}
