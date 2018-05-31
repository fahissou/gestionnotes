/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.etats;

import bean.inscription.AnneeAcademiqueBean;
import bean.util.ParametragesBean;
import ejb.administration.ParametresFacade;
import ejb.administration.ResponsabiliteFacade;
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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Responsabilite;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Enseignant;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "releveNotesBean")
@ViewScoped
public class ReleveNotesBean implements Serializable {

    @EJB
    private ResponsabiliteFacade responsabiliteFacade;

    @EJB
    private HistoriquesFacade historiquesFacade;
    @EJB
    private NotesFacade notesFacade;
    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private UeFacade ueFacade;
    @EJB
    private EnseignantFacade enseignantFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    @EJB
    private ParametresFacade parametresFacade;
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private FiliereFacade filiereFacade;

    private Filiere filiere;
    private GroupePedagogique groupePedagogique;
    private Semestre semestre;
    private List<Filiere> listFilieres;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Semestre> listSemestres;
    private AnneeAcademique anneeAcademique;
    private List<Inscription> listInscription;
    private Inscription inscription;
    private Responsabilite responsabilite;
    private List<Responsabilite> listeResponsables;

    public ReleveNotesBean() {
    }

    @PostConstruct
    public void init() {
        listFilieres = filiereFacade.findAll();
        anneeAcademique = AnneeAcademiqueBean.getAnneeAcademicChoisi1();
        listeResponsables = responsabiliteFacade.findResponsabilite();
    }

    public void initGroupePedagogique() {
        if (filiere != null) {
            listGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(filiere);
        } else {
            listGroupePedagogiques = new ArrayList<>();
        }

    }

    public void initSemetre() {
        listSemestres = semestreFacade.getSemetreByGP(groupePedagogique);
        listInscription = inscriptionFacade.getListInscriptionByGP(groupePedagogique, anneeAcademique);
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public List<Filiere> getListFilieres() {
        return listFilieres;
    }

    public void setListFilieres(List<Filiere> listFilieres) {
        this.listFilieres = listFilieres;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public List<Semestre> getListSemestres() {
        return listSemestres;
    }

    public void setListSemestres(List<Semestre> listSemestres) {
        this.listSemestres = listSemestres;
    }

    public List<Inscription> getListInscription() {
        return listInscription;
    }

    public void setListInscription(List<Inscription> listInscription) {
        this.listInscription = listInscription;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public Responsabilite getResponsabilite() {
        return responsabilite;
    }

    public void setResponsabilite(Responsabilite responsabilite) {
        this.responsabilite = responsabilite;
    }

    public List<Responsabilite> getListeResponsables() {
        return listeResponsables;
    }

    public void setListeResponsables(List<Responsabilite> listeResponsables) {
        this.listeResponsables = listeResponsables;
    }

    // dynamique releve notes
    public void genererReleveDynamic() {
        List<Inscription> inscriptions1 = new ArrayList<>();
        if (inscription != null) {
            inscriptions1.add(inscription);
            genererRelevetNotes1(inscriptions1, inscription.getEtudiant().getNom() + "_" + inscription.getEtudiant().getPrenom());
        } else {
            genererRelevetNotes1(listInscription, "_Releve_");
        }
//        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/historiques/'");
    }

    // END dynamique releve
    public void genererRelevetNotes1(List<Inscription> inscriptions, String nameFileGen) {
        String absolutPath = ParametragesBean.getPathRoot();
        String pathOut = absolutPath + "fichiergenerer/rapportgestionnotes/";
        String pathIn = absolutPath + "resources/releve/";
        String pathOutPDF = absolutPath + "fichiergenerer/rapportgestionnotes/touslesrapports/";
        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdirs();
        repertoire2.mkdirs();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");
        try {
            // Definition des champs du proces fichier 1
            List<String> champs = new ArrayList<>();
            champs.add("UE");
            champs.add("N");
            champs.add("O");
            champs.add("C");
            champs.add("S");

            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("a", anneeAcademique.getDescription().split("-")[1].trim());
            parametreEntetes.put("aa", anneeAcademique.getDescription());
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());

            // signature du responsable pédagogique
//            Enseignant respo = enseignantFacade.getEnseignantByResponsabilite(signataire.toUpperCase());
            if (responsabilite.getRole().equals("DIRECTEUR")) {
                parametreEntetes.put("Signataire", "Le Directeur");
                parametreEntetes.put("da", "");
                parametreEntetes.put("nomS", JsfUtil.getLabelGradeEnseignant(responsabilite.getEnseignant().getGrade()) + " " + responsabilite.getEnseignant().getPrenom() + " " + responsabilite.getEnseignant().getNom());
            } else {
                parametreEntetes.put("Signataire", "Pour Le Directeur et P.O,");
                parametreEntetes.put("da", "Le Directeur Ajoint");
                parametreEntetes.put("nomS", JsfUtil.getLabelGradeEnseignant(responsabilite.getEnseignant().getGrade()) + " " + responsabilite.getEnseignant().getPrenom() + " " + responsabilite.getEnseignant().getNom());
            }

            String nomFichier = JsfUtil.generateId();
            for (int j = 0; j < inscriptions.size(); j++) {

                Etudiant student = inscriptions.get(j).getEtudiant();
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
//                Map<String, Object> pocheParametres = new HashMap<>();

                // student
                parametreEntetes.put("nom", student.getNom());
                parametreEntetes.put("prenom", student.getPrenom());
                String date = formatDate.format(student.getDateNaissance());
                parametreEntetes.put("dte", date);
                parametreEntetes.put("lieu", student.getLieuNaissance());
                // Table contenant les enregistrements du fichier resultat
                List< Map<String, Object>> conteneur = new ArrayList<>();

                int nombreCreditValider = 0;
                double somMoySemestre = 0;
                int totalCredit = 0;
                List<String> uenv = new ArrayList<>();
                Map<String, Object> row1 = null;
                List<Ue> ues1 = null;
                List<Semestre> semestresTmp = semestreFacade.getSemetreByGP(groupePedagogique);

                if (semestre.getValeur() == semestresTmp.get(0).getValeur()) {
                    ues1 = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
                    totalCredit = JsfUtil.getCreditTotal(ues1);
                    parametreEntetes.put("S1", semestresTmp.get(0).getValeur());
                } else {
                    ues1 = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
                    List<Ue> ues0 = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestresTmp.get(0)));
                    totalCredit = JsfUtil.getCreditTotal(ues0) + JsfUtil.getCreditTotal(ues1);
                    parametreEntetes.put("S1", semestresTmp.get(0).getValeur());
                    parametreEntetes.put("S2", semestresTmp.get(1).getValeur());
                }

                for (int i = 0; i < ues1.size(); i++) {
                    List<String> matieresByUE = new ArrayList<>();
                    row1 = new HashMap<>();
                    double som = 0.0;
                    List<Matiere> matieres = null;
                    Ue currentUE = ues1.get(i);
//                    totalCredit += currentUE.getCredit();
                    matieres = matiereFacade.getMatiereByUe(currentUE);
                    String session = "***";
                    int m = 0;
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                        som += notes.getNote();
                        matieresByUE.add(matieres.get(k).getLibelle());
                        if (notes.getSessions() != null) {
                            if (m == 0) {
                                session = notes.getSessions();
                                m++;
                            } else {
                                session = JsfUtil.getSessionValidation(session, notes.getSessions());
                            }
                        }
                    }
                    double moyUE = som / matieres.size();

                    if (moyUE < groupePedagogique.getParametres().getMoyenneUE()) {
                        uenv.add(currentUE.getLibelle());
                    }

                    nombreCreditValider += isValide(moyUE, currentUE.getCredit(), groupePedagogique);
                    somMoySemestre = +moyUE;

                    if (matieresByUE.size() > 1) {
                        row1.put("UE", currentUE.getLibelle() + " (" + listeToString(matieresByUE) + ")");
                    } else {
                        row1.put("UE", currentUE.getLibelle());
                    }
                    String [] c = {"2","3","5"};
                    row1.put("N", JsfUtil.formatNote(moyUE));
                    row1.put("O", decision(moyUE, groupePedagogique));
                    row1.put("C", currentUE.getCredit());
//                    row1.put("S", session);
                    row1.put("S", c);
                    conteneur.add(row1);
                }

                parametreEntetes.put("TC", totalCredit);
                parametreEntetes.put("UENV", listeToString(uenv));
                if (semestre.getValeur() == semestresTmp.get(0).getValeur()) {
                    parametreEntetes.put("TCV", nombreCreditValider);
                    JsfUtil.generateurXDOCReport(pathIn + "/dynamiqueReleve.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Releve" + "_" + student.getNom() + " " + student.getPrenom(), parametreEntetes);
                } else {
                    parametreEntetes.put("TCV", inscriptions.get(j).getCompteurCredit());
                    parametreEntetes.put("decision", JsfUtil.decisionFinal(inscriptions.get(j).getCompteurCredit(), totalCredit, groupePedagogique));
                    JsfUtil.generateurXDOCReport(pathIn + "/dynamiqueReleveS2.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Releve" + "_" + student.getNom() + " " + student.getPrenom(), parametreEntetes);
                }

            }
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + groupePedagogique.getDescription() + "releves" + semestre.getValeur());
            Historiques historique = new Historiques();
            historique.setLibelle(nomFichier + groupePedagogique.getDescription() + "releves" + semestre.getValeur()+".pdf");
            historique.setLienFile(pathOutPDF);
            historique.setGroupePedagogique(groupePedagogique.getDescription());
            historique.setDateEdition(JsfUtil.getDateEdition());
            historique.setAnneeAcademique(anneeAcademique);
            historiquesFacade.create(historique);
            File fileDowload = new File(pathOutPDF + historique.getLibelle());
            JsfUtil.flushToBrowser(fileDowload, "application/pdf");

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
        }

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

    public void reset() {
        this.filiere.reset();
        this.groupePedagogique.reset();
        this.inscription.reset();
        this.semestre.reset();
    }

    public int isValide(double val, int credit, GroupePedagogique groupeP) {
        int som = 0;
        if (val >= groupeP.getParametres().getMoyenneUE()) {
            som = credit;
        }
        return som;
    }

    public String decision(double note, GroupePedagogique groupePedagogique) {
        String arg = "***";
        if (note != 0.0) {
            if (note >= groupePedagogique.getParametres().getMoyenneUE()) {
                arg = "VALIDÉ";
            } else {
                arg = "NON VALIDÉ";
            }
        }

        return arg;
    }

}
