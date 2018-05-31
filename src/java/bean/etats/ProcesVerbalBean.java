/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.etats;

import bean.inscription.AnneeAcademiqueBean;
import bean.util.ParametragesBean;
import ejb.administration.ParametresFacade;
import ejb.formation.FiliereFacade;
import ejb.formation.HistoriquesFacade;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.inscription.NotesFacade;
import ejb.module.MatiereFacade;
import ejb.module.SemestreFacade;
import ejb.module.UeFacade;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
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
@ViewScoped
@Named(value = "procesVerbalBean")
public class ProcesVerbalBean implements Serializable {

    @EJB
    private HistoriquesFacade historiquesFacade;
    @EJB
    private ParametresFacade parametresFacade;
    @EJB
    private NotesFacade notesFacade;
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    @EJB
    private MatiereFacade matiereFacade;
    @EJB
    private UeFacade ueFacade;
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

    public ProcesVerbalBean() {
    }

    @PostConstruct
    public void init() {
        listFilieres = filiereFacade.findAll();
        anneeAcademique = AnneeAcademiqueBean.getAnneeAcademicChoisi1();
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

    public void reset(ActionEvent e) {
        this.filiere.reset();
        this.groupePedagogique.reset();
        this.semestre.reset();

    }

    // essai de proces dynamique
    public void procesVerbal(ActionEvent event) {

        String absolutPath = ParametragesBean.getPathRoot();
        String pathOut = absolutPath + "fichiergenerer/rapportgestionnotes/";
        String pathIn = absolutPath + "resources/releve/releveNouveau/";
        String pathOutPDF = absolutPath + "fichiergenerer/rapportgestionnotes/touslesrapports/";

        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdirs();
        repertoire2.mkdirs();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");
        String msg;
        String nomFichier = JsfUtil.generateId();
        List<Ue> ues = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
        try {
            int nombreUE;
            if (!ues.isEmpty()) {
                nombreUE = ues.size();

                if (nombreUE <= 18 && nombreUE >= 4) {
                    List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP(groupePedagogique, anneeAcademique);
                    // Paramètres d'entete du proces fichier 1
                    Map<String, Object> parametreEntetes1 = new HashMap<>();
                    // Paramètres d'entete du proces fichier 2
                    Map<String, Object> parametreEntetes2 = new HashMap<>();
                    // Paramètres d'entete du proces fichier 3
                    Map<String, Object> parametreEntetes3 = new HashMap<>();

                    parametreEntetes1.put("annee", anneeAcademique.getDescription());
                    parametreEntetes1.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                    parametreEntetes1.put("semestre", semestre.getValeur());
                    parametreEntetes1.put("d", JsfUtil.getDateEdition());
                    parametreEntetes2.put("annee", anneeAcademique.getDescription());
                    parametreEntetes2.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                    parametreEntetes2.put("semestre", semestre.getValeur());
                    parametreEntetes2.put("d", JsfUtil.getDateEdition());
                    parametreEntetes3.put("annee", anneeAcademique.getDescription());
                    parametreEntetes3.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
                    parametreEntetes3.put("semestre", semestre.getValeur());
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
                            String moyUEString = getMoyUE(matieres, inscriptions.get(j), anneeAcademique);
                            String moyUE;
                            String decision;
                            if (moyUEString.equals("***")) {
                                moyUE = moyUEString;
                                decision = moyUEString;
                            } else {
                                nombreCreditValider += isValide(Double.parseDouble(moyUEString), currentUE.getCredit(), groupePedagogique);
                                moyUE = JsfUtil.formatNote(Double.parseDouble(moyUEString));
                                decision = decision2(Double.parseDouble(moyUEString), groupePedagogique);
                            }

                            if (i < max) {
                                row1.put("m" + (i + 1), moyUE);
                                row1.put("o" + (i + 1), decision);
                                row1.put("c" + (i + 1), currentUE.getCredit());
                                parametreEntetes1.put("UE" + (i + 1), JsfUtil.getAbrevUE(currentUE.getLibelle()));
                                parametreEntetes1.put("ue" + (i + 1), currentUE.getLibelle());
                                l = i + 1;
                            } else if (i >= max && i < (2 * max)) {
                                row2.put("m" + (i + 1 - l), moyUE);
                                row2.put("o" + (i + 1 - l), decision);
                                row2.put("c" + (i + 1 - l), currentUE.getCredit());
                                parametreEntetes2.put("UE" + (i + 1), JsfUtil.getAbrevUE(currentUE.getLibelle()));
                                parametreEntetes2.put("ue" + (i + 1), currentUE.getLibelle());
                                p = i + 1;
                            } else {
                                row3.put("m" + (i + 1 - p), moyUE);
                                row3.put("o" + (i + 1 - p), decision);
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

                    if (nombreUE == 4) {
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else if (nombreUE == 5) {
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else if (nombreUE == 6) {
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else {
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[1], champs2, conteneur2, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale2" + "_" + groupePedagogique.getDescription() + "2", parametreEntetes2);
                        JsfUtil.generateurXDOCReport(pathIn + "/" + nameFile[2], champs3, conteneur3, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale3" + "_" + groupePedagogique.getDescription() + "3", parametreEntetes3);
                    }
                    JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
                    JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "procesverbal" + groupePedagogique.getDescription() + semestre.getValeur());
                    Historiques historique = new Historiques();
                    historique.setLibelle(nomFichier + "procesverbal" + groupePedagogique.getDescription() + semestre.getValeur()+".pdf");
                    historique.setLienFile(pathOutPDF);
                    historique.setGroupePedagogique(groupePedagogique.getDescription());
                    historique.setDateEdition(JsfUtil.getDateEdition());
                    historique.setAnneeAcademique(anneeAcademique);
                    historiquesFacade.create(historique);
                    File fileDowload = new File(pathOutPDF + historique.getLibelle());
                    JsfUtil.flushToBrowser(fileDowload, "application/pdf");
                    msg = JsfUtil.getBundleMsg("ProcesGenererSucces");
                    JsfUtil.addSuccessMessage(msg);

                } else {
                    msg = JsfUtil.getBundleMsg("NombreUEInvalid");
                    JsfUtil.addErrorMessage(msg);
                }
            } else {
                msg = JsfUtil.getBundleMsg("ISEmptyUE");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());

        }
    }

    public int isValide(double moyUE, int credit, GroupePedagogique groupP) {
        int var = 0;
        if (moyUE >= groupP.getParametres().getMoyenneUE()) {
            var = credit;
        }
        return var;
    }

    public String decision2(double note, GroupePedagogique groupePedagogique) {
        String arg = "N.V";
        if (note >= groupePedagogique.getParametres().getMoyenneUE()) {
            arg = "V";
        }
        return arg;
    }

    public void reset() {
        this.filiere.reset();
        this.groupePedagogique.reset();
        this.semestre.reset();
    }

    public String getMoyUE(List<Matiere> matieres, Inscription inscription, AnneeAcademique anneeAcademique) {
        String resultat = "***";
        String session = "";
        double som = 0.0;
        int m = 0;
        for (int k = 0; k < matieres.size(); k++) {
            Notes notes = notesFacade.getNotesByInscriptionMatiere(inscription, matieres.get(k));
            som += notes.getNote();
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
        if (session.equals(anneeAcademique.getDescription())) {
            resultat = String.valueOf(moyUE);
        }
        return resultat;
    }

}
