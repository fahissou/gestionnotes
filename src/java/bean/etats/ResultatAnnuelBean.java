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
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.inscription.InscriptionFacade;
import ejb.module.SemestreFacade;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jpa.administration.Parametres;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "resultatAnnuelBean")
@ViewScoped
public class ResultatAnnuelBean implements Serializable{
    @EJB
    private HistoriquesFacade historiquesFacade;
    @EJB
    private InscriptionFacade inscriptionFacade;
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    @EJB
    private FiliereFacade filiereFacade;
    
    private Filiere filiere;
    private GroupePedagogique groupePedagogique;
    private List<Filiere> listFilieres;
    private List<GroupePedagogique> listGroupePedagogiques;
    private AnneeAcademique anneeAcademique;
    
    
    public ResultatAnnuelBean() {
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

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    
    
    // Résultat final
    public void genererResultatFinale() {
        
        String pathOut = JsfUtil.getPathOutTmp();
        String pathIn = JsfUtil.getPathIntModelReleve();
        String pathOutPDF = JsfUtil.getPathOutPDF();
        File repertoire1 = new File(pathOut + "/tmp2/");
        File repertoire2 = new File(pathOut + "/" + "fichierPDF" + "/");
        repertoire1.mkdirs();
        repertoire2.mkdirs();
        JsfUtil.deleteFile(repertoire1.getAbsolutePath() + "/");
        JsfUtil.deleteFile(repertoire2.getAbsolutePath() + "/");
        String nomFichier = JsfUtil.generateId();
        String[] semestress = JsfUtil.semestreGP(semestreFacade.getSemetreByGP(groupePedagogique));
        List<Inscription> inscriptions = inscriptionFacade.getListInscriptionByGP1(groupePedagogique, anneeAcademique);
  
        try {
            // Paramètres d'entete du resultat final
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("aa", anneeAcademique.getDescription());
            parametreEntetes.put("filiere", groupePedagogique.getFiliere().getLibelle() + " :  " + groupePedagogique.getDescription());
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            parametreEntetes.put("semestre", semestress[0] + " & " + semestress[1]);

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
                String result = admissible(inscriptions.get(j).getCompteurCredit(), groupePedagogique.getParametres().getProportionAdmission());
//                createDefaultInscription(inscriptions.get(j), result);
                row.put("Ob", result);
                conteneur.add(row);
            }
            JsfUtil.generateurXDOCReport(pathIn + "/resultatFinal1.docx", champs, conteneur, "T", repertoire1.getAbsolutePath() + "/", "Resultat_Final" + "_" + groupePedagogique.getDescription() + "1", parametreEntetes);
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + groupePedagogique.getDescription() + "ResultatFinal");
            Historiques historique = new Historiques();
            historique.setLibelle(groupePedagogique.getDescription() + "ResultatFinal");
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + groupePedagogique.getDescription() + "ResultatFinal"));
            historique.setGroupePedagogique(groupePedagogique.getDescription());
            historique.setDateCreation(new Date());
            historiquesFacade.create(historique);

        } catch (Exception ex) {
            System.out.println("Exceptionfghff " + ex.getMessage());

        }
        RequestContext.getCurrentInstance().execute("window.location='/gestionnotes/etats/historiques/'");
    }
    
    public String admissible(double a, double admissibilite) {
        String res = "Réfusé";
        if (a >= admissibilite) {
            res = "Admis";
        }
        return res;
    }
    
    
    
}
