/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.etats;

import bean.inscription.AnneeAcademiqueBean;
import ejb.administration.ParametresFacade;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.Parametres;
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
public class ReleveNotesBean implements Serializable{
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
    private List<Parametres> parametres;
    private List<Inscription> listInscription;
    private Inscription inscription;
    private String signataire;
   
    
    public ReleveNotesBean() {
    }
    
    @PostConstruct
    public void init() {
        listFilieres = filiereFacade.findAll();
        anneeAcademique = AnneeAcademiqueBean.getAnneeAcademicChoisi1();
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
        listInscription = inscriptionFacade.getListInscriptionByGP(groupePedagogique, anneeAcademique);
    }
    
    public List<String> initSignataire(){
        List<String> listeSignataires = new ArrayList<>();
        listeSignataires.add("Directeur");
        listeSignataires.add("Directeur Adjoint");
        return listeSignataires;
    }

    public String getSignataire() {
        return signataire;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
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

    public List<Parametres> getParametres() {
        return parametres;
    }

    public void setParametres(List<Parametres> parametres) {
        this.parametres = parametres;
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

     // dynamique releve notes
    public void genererReleveDynamic() {
        List<Inscription> inscriptions1 = new ArrayList<>();
        if(inscription != null){
            inscriptions1.add(inscription);
             genererRelevetNotes1(inscriptions1,inscription.getEtudiant().getNom()+"_"+inscription.getEtudiant().getPrenom());
        }else{
            genererRelevetNotes1(listInscription,"_Releve_");
        }
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/historiques/'");
    }
    
    // END dynamique releve

    public void genererRelevetNotes1(List<Inscription> inscriptions, String nameFileGen) {
        String pathOut = JsfUtil.getPathOutTmp();
        String pathIn = JsfUtil.getPathIntModelReleve();
        String pathOutPDF = JsfUtil.getPathOutPDF();
        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdirs();
        repertoire2.mkdirs();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");
        try {
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
            parametreEntetes.put("a", anneeAcademique.getDescription().split("-")[1].trim());
            parametreEntetes.put("aa", anneeAcademique.getDescription());
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
            parametreEntetes.put("semestre", semestre.getLibelle());
            
            // signature du responsable pédagogique
            Enseignant respo = enseignantFacade.getEnseignantByResponsabilite(signataire);
            if(signataire.equals("Directeur")){
                parametreEntetes.put("Signataire", "Le "+ signataire);
                parametreEntetes.put("da", "");
                parametreEntetes.put("nomS", respo.getGrade() +" " + respo.getPrenom() + " " +respo.getNom());
            }else{
                parametreEntetes.put("Signataire", "Pour Le Directeur et P.O,");
                parametreEntetes.put("da", "Le "+signataire);
                parametreEntetes.put("nomS", respo.getGrade() +" " + respo.getPrenom() + " " +respo.getNom());
            }
            
//            if(semestre.getLibelle() == listSemestres.get(1).getLibelle()) {
//              parametreEntetes.put("da", "Le "+signataire);
//            }
            
            
            
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
                List<Ue> ues1 = ueFacade.getRealUE(ueFacade.getUeByGroupePedagogique(groupePedagogique, semestre));
                for (int i = 0; i < ues1.size(); i++) {
                    List<String> matieresByUE = new ArrayList<>();
                    row1 = new HashMap<>();
                    double som = 0.0;
                    List<Matiere> matieres = null;
                    Ue currentUE = ues1.get(i);
                    totalCredit += currentUE.getCredit();
                    matieres = matiereFacade.getMatiereByUe(currentUE);
                    for (int k = 0; k < matieres.size(); k++) {
                        Notes notes = notesFacade.getNotesByInscriptionMatiere(inscriptions.get(j), matieres.get(k));
                        som = +notes.getNote();
                        matieresByUE.add(matieres.get(k).getLibelle());
                    }
                    
                    double moyUE = som / matieres.size();
                    if (moyUE < parametres.get(0).getMoyenneUE()) {
                        uenv.add(currentUE.getLibelle());
                    }
                    nombreCreditValider += isValide(moyUE, currentUE.getCredit());
                    somMoySemestre = +moyUE;
                    
                    if(matieresByUE.size() > 1){
                       row1.put("UE", currentUE.getLibelle()+" ("+listeToString(matieresByUE)+")");
                    }else{
                        row1.put("UE", currentUE.getLibelle());
                    }
                    row1.put("N", JsfUtil.formatNote(moyUE));
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

                JsfUtil.generateurXDOCReport(pathIn + "/dynamiqueReleve.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Releve" + "_" + student.getNom() + " " + student.getPrenom(), parametreEntetes);

//            docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire3.getAbsolutePath() + "/");
            }
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + groupePedagogique.getDescription() + "releves" + semestre.getLibelle());
            Historiques historique = new Historiques();
            historique.setLibelle(groupePedagogique.getDescription() + nameFileGen + semestre.getLibelle());
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + groupePedagogique.getDescription() + "releves" + semestre.getLibelle()));
            historique.setGroupePedagogique(groupePedagogique.getDescription());
            historique.setDateCreation(new Date());
            historiquesFacade.create(historique);

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
                if(i == 0){
                    var += arg.get(i);
                }else{
                   var += " ; " + arg.get(i); 
                }
                
            }
        }
        return var;
    }
    
    public int isValide(double val, int credit) {
        int som = 0;
        if (val >= parametres.get(0).getMoyenneUE()) {
            som = credit;
        }
        return som;
    }
    
    public String decision(double note) {
        String arg = "NON VALIDER";
        if (note >= 12.0) {
            arg = "VALIDER";
        }
        return arg;
    }
    

}
