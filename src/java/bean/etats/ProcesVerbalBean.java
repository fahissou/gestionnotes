/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.etats;

import static bean.inscription.NotesBean.formatNote;
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
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Parametres;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
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
    private String anneeAcademique;
    private List<Parametres> parametres;

    public ProcesVerbalBean() {
    }

    @PostConstruct
    public void init() {
        listFilieres = filiereFacade.findAll();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
        parametres = parametresFacade.findAll();
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
    public void procesVearbal(ActionEvent event) {
        String pathOut = JsfUtil.getPathOutTmp();
        String pathIn = JsfUtil.getPathIntModelProces();
        String pathOutPDF = JsfUtil.getPathOutPDF();
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
                                System.out.println(" ok1 "+currentUE.getLibelle());
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

                    if (nombreUE == 4) {
                        genererProcesVerval(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else if (nombreUE == 5) {
                        genererProcesVerval(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else if (nombreUE == 6) {
                        genererProcesVerval(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                    } else {
                        genererProcesVerval(pathIn + "/" + nameFile[0], champs1, conteneur1, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale1" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes1);
                        genererProcesVerval(pathIn + "/" + nameFile[1], champs2, conteneur2, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale2" + "_" + groupePedagogique.getDescription() + "2", parametreEntetes2);
                        genererProcesVerval(pathIn + "/" + nameFile[2], champs3, conteneur3, "T", repertoire1.getAbsolutePath() + "/", "Proces_Verbale3" + "_" + groupePedagogique.getDescription() + "3", parametreEntetes3);
                    }
                    JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
                    JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "procesverbal" + groupePedagogique.getDescription() + semestre.getLibelle());
                    Historiques historique = new Historiques();
                    historique.setLibelle("proces" + groupePedagogique.getDescription() + "_" + anneeAcademique + "_" + semestre.getLibelle());
                    historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + "procesverbal" + groupePedagogique.getDescription() + semestre.getLibelle()));
                    historique.setGroupePedagogique(groupePedagogique.getDescription());
                    historique.setDateCreation(new Date());
                    historiquesFacade.create(historique);
//                docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
                    msg = JsfUtil.getBundleMsg("ProcesGenererSucces");
                    JsfUtil.addSuccessMessage(msg);

//                boolean fi2 =
//                    new File(repertoire1.getAbsolutePath()).delete();
//                    new File(repertoire2.getAbsolutePath()).delete();

//                RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/fichiergenerer/rapportgestionnotes/procesverval/IGISA2.pdf'");
//                repertoire1.delete();
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
      RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/historiques/'");
    }

    public int isValide(double moyUE, int credit) {
        int var = 0;
        if (moyUE >= parametres.get(0).getMoyenneUE()) {
            var = credit;
        }
        return var;
    }

    public String decision2(double note) {
        String arg = "N.V";
        if (note >= 12.0) {
            arg = "V";
        }
        return arg;
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

}
