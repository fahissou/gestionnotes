/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.ProgrammerCoursFacade;
import ejb.formation.HistoriquesFacade;
import ejb.inscription.AnneeAcademiqueFacade;
import ejb.inscription.EnseignantFacade;
import ejb.inscription.EnseignerFacade;
import ejb.inscription.GroupePedagogiqueFacade;
import ejb.module.MatiereFacade;
import ejb.module.SemestreFacade;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.administration.ProgrammerCours;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Enseignant;
import jpa.inscription.Enseigner;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */

@Named(value = "programmerCoursBean")
@ViewScoped
public class ProgrammerCoursBean implements Serializable {
    @EJB
    private SemestreFacade semestreFacade;
    @EJB
    private EnseignantFacade enseignantFacade;

    @EJB
    private HistoriquesFacade historiquesFacade;
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;

    @EJB
    private EnseignerFacade enseignerFacade;

    @EJB
    private GroupePedagogiqueFacade groupePedagogiqueFacade;
    private GroupePedagogique selectedGroupePedagogique;

    @EJB
    private MatiereFacade matiereFacade;
    private Matiere selectedMatiere;

    @EJB
    private ProgrammerCoursFacade programmerCoursFacade;

    private ProgrammerCours selectedProgrammerCours;
    private ProgrammerCours newProgrammerCours;
    private List<ProgrammerCours> listeProgrammerCourss;
    private List<ProgrammerCours> listeProgrammerCourssDynamic;
    private List<ProgrammerCours> filteredList;
    private Filiere selectedFiliere;
    private List<Semestre> listeSemestres;
    private Semestre selectedSemestre;
    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Matiere> listMatieres;
    private List<Enseignant> listeEnseignants;
    private AnneeAcademique anneeAcademique;
    private Enseignant enseignantResp;
    private List<Enseignant> listeEnseignantResp;
    /**
     * Creates a new instance of ProgrammerCoursBean
     */
    public ProgrammerCoursBean() {
    }

    @PostConstruct
    public void init() {
//        listeProgrammerCourss = programmerCoursFacade.findAll();
        prepareCreate();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
        listeEnseignantResp = enseignantFacade.findAllEnseignantResponsa();
    }

    public void initEnseignants() {
        listeEnseignants = getEnseignants(enseignerFacade.findEnseignerByMatiere(newProgrammerCours.getMatiere()));
    }
    
    public void initSemestre() {
        listeSemestres = semestreFacade.getSemetreByGP(selectedGroupePedagogique);
    }
    
    public void initGroupePedagogique() {
        listGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(selectedFiliere);
    }
    public void initMatiere() {
        listMatieres = getMatieresNP(selectedGroupePedagogique, selectedSemestre);
    }
    
    public void initTable() {
        listeProgrammerCourssDynamic = programmerCoursFacade.listeProgrammeByGroupe(selectedGroupePedagogique, anneeAcademique, selectedSemestre);
    }
    
    public List<Enseignant> getEnseignants(List<Enseigner> liste) {
        List<Enseignant> list = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            list.add(liste.get(i).getEnseignant());
        }
        return list;
    }

    public void doCreate(ActionEvent event) {
        String msg;
        try {
            if (JsfUtil.compareDate(newProgrammerCours.getDateDebut(), newProgrammerCours.getDateFin())) {
                newProgrammerCours.setAnneeAcademique(anneeAcademique);
                newProgrammerCours.setEtat("red");
                newProgrammerCours.setGroupePedagogique(selectedGroupePedagogique);
                newProgrammerCours.setResponsable(enseignantResp);
                programmerCoursFacade.create(newProgrammerCours);
                msg = JsfUtil.getBundleMsg("ProgrammerCoursCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
                prepareCreate();
                listeProgrammerCourss = programmerCoursFacade.findAll();
                
            } else {
                msg = JsfUtil.getBundleMsg("DateError");
                JsfUtil.addErrorMessage(msg);
            }

        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {
        String msg;
        try {
            programmerCoursFacade.edit(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeProgrammerCourss = programmerCoursFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
//            matiereFacade.edit(selectedProgrammerCours.getMatiere());
            programmerCoursFacade.remove(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            prepareCreate();
            listeProgrammerCourssDynamic = programmerCoursFacade.listeProgrammeByGroupe(selectedGroupePedagogique, anneeAcademique, selectedSemestre);
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public List<Enseignant> getListeEnseignantResp() {
        return listeEnseignantResp;
    }

    public void setListeEnseignantResp(List<Enseignant> listeEnseignantResp) {
        this.listeEnseignantResp = listeEnseignantResp;
    }

    public Enseignant getEnseignantResp() {
        return enseignantResp;
    }

    public void setEnseignantResp(Enseignant enseignantResp) {
        this.enseignantResp = enseignantResp;
    }

    public List<Enseignant> getListeEnseignants() {
        return listeEnseignants;
    }

    public void setListeEnseignants(List<Enseignant> listeEnseignants) {
        this.listeEnseignants = listeEnseignants;
    }

    public ProgrammerCours getSelectedProgrammerCours() {
        return selectedProgrammerCours;
    }

    public void setSelectedProgrammerCours(ProgrammerCours selectedProgrammerCours) {
        this.selectedProgrammerCours = selectedProgrammerCours;
    }

    public ProgrammerCours getNewProgrammerCours() {
        return newProgrammerCours;
    }

    public void setNewProgrammerCours(ProgrammerCours newProgrammerCours) {
        this.newProgrammerCours = newProgrammerCours;
    }

    public List<ProgrammerCours> getListeProgrammerCourss() {
        return listeProgrammerCourss;
    }

    public void setListeProgrammerCourss(List<ProgrammerCours> listeProgrammerCourss) {
        this.listeProgrammerCourss = listeProgrammerCourss;
    }

    public List<ProgrammerCours> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<ProgrammerCours> filteredList) {
        this.filteredList = filteredList;
    }

    public Filiere getSelectedFiliere() {
        return selectedFiliere;
    }

    public void setSelectedFiliere(Filiere selectedFiliere) {
        this.selectedFiliere = selectedFiliere;
    }

    public GroupePedagogique getSelectedGroupePedagogique() {
        return selectedGroupePedagogique;
    }

    public void setSelectedGroupePedagogique(GroupePedagogique selectedGroupePedagogique) {
        this.selectedGroupePedagogique = selectedGroupePedagogique;
    }

    public Matiere getSelectedMatiere() {
        return selectedMatiere;
    }

    public void setSelectedMatiere(Matiere selectedMatiere) {
        this.selectedMatiere = selectedMatiere;
    }

    public List<GroupePedagogique> getListGroupePedagogiques() {
        return listGroupePedagogiques;
    }

    public void setListGroupePedagogiques(List<GroupePedagogique> listGroupePedagogiques) {
        this.listGroupePedagogiques = listGroupePedagogiques;
    }

    public List<Matiere> getListMatieres() {
        return listMatieres;
    }

    public void setListMatieres(List<Matiere> listMatieres) {
        this.listMatieres = listMatieres;
    }

    public void prepareCreate() {
        this.newProgrammerCours = new ProgrammerCours();
    }

    public List<Semestre> getListeSemestres() {
        return listeSemestres;
    }

    public void setListeSemestres(List<Semestre> listeSemestres) {
        this.listeSemestres = listeSemestres;
    }

    public Semestre getSelectedSemestre() {
        return selectedSemestre;
    }

    public void setSelectedSemestre(Semestre selectedSemestre) {
        this.selectedSemestre = selectedSemestre;
    }

//    public void initList() {
//        listeProgrammerCourssDynamic = programmerCoursFacade.listeProgrammeByGroupe(selectedGroupePedagogique, anneeAcademique);
//    }

    public void reset(ActionEvent e) {
        this.newProgrammerCours.reset();
    }

    public List<ProgrammerCours> getListeProgrammerCourssDynamic() {
        return listeProgrammerCourssDynamic;
    }

    public void setListeProgrammerCourssDynamic(List<ProgrammerCours> listeProgrammerCourssDynamic) {
        this.listeProgrammerCourssDynamic = listeProgrammerCourssDynamic;
    }
    

//    public void validationDate(FacesContext context, UIComponent component, Object value) {
//        String validateDate = "composantDate";
//        UIInput composantDate = (UIInput) component.getAttributes().get(validateDate);
//        Date dateFin = (Date) value;
//        Date dateDebut = (Date)composantDate.getValue();
//        String msg;
//        try {
//            if (!JsfUtil.compareDate(dateDebut, dateFin)) {
//                ((UIInput) component).setValid(false);
//                msg = JsfUtil.getBundleMsg("InvalideDate");
//                JsfUtil.addErrorMessage(msg);
////                FacesMessage message = new FacesMessage(msg);
////                context.addMessage(component.getClientId(context), message);
//            }
//        } catch (Exception e) {
//        }
//    }
    public List<Matiere> getMatieresNP(GroupePedagogique groupePedagogique, Semestre semestre) {
        List<Matiere> matieres = new ArrayList<>();
        List<Matiere> matieresM = matiereFacade.getMatiereByGroupe(groupePedagogique, semestre);
        List<Matiere> matieresPC = programmerCoursFacade.listeMatieresPC(groupePedagogique);
        for (int i = 0; i < matieresM.size(); i++) {
            int compteur = 0;
            for (int j = 0; j < matieresPC.size(); j++) {
                if (matieresPC.get(j).equals(matieresM.get(i))) {
                    compteur++;
                    break;
                }
            }
            if (compteur == 0) {
                matieres.add(matieresM.get(i));
            }
        }
        return matieres;
    }

    public void genererEtatCoursProgrammer() {
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
        try {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> parametreEntetes = new HashMap<>();
            parametreEntetes.put("annee", anneeAcademique.getDescription());
            parametreEntetes.put("filiere", selectedGroupePedagogique.getFiliere().getLibelle().toUpperCase() + " :  " + selectedGroupePedagogique.getDescription());
            parametreEntetes.put("matiere", selectedProgrammerCours.getMatiere().getLibelle().toUpperCase());
            Enseignant enseignant = selectedProgrammerCours.getEnseignant();
            parametreEntetes.put("enseignant", JsfUtil.getLabelGradeEnseignant(enseignant.getGrade()) + " " + enseignant.getPrenom() + " " + enseignant.getNom().toUpperCase());
            String datedebut = formatDate.format(selectedProgrammerCours.getDateDebut());
            parametreEntetes.put("datedebut", datedebut);
            String datefin = formatDate.format(selectedProgrammerCours.getDateFin());
            parametreEntetes.put("datefin", datefin);
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            parametreEntetes.put("responsable", selectedProgrammerCours.getResponsable().getResponsabilite());
            parametreEntetes.put("nomResp", JsfUtil.getLabelGradeEnseignant(selectedProgrammerCours.getResponsable().getGrade()) + " " + selectedProgrammerCours.getResponsable().getPrenom() + " " + selectedProgrammerCours.getResponsable().getNom());
            JsfUtil.generateurXDOCReportStatic(pathIn + "/programmationcours.docx", parametreEntetes, repertoire1.getAbsolutePath() + "/", "coursprogramme_" + selectedProgrammerCours.getMatiere().getLibelle());
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "coursprogramme_" + selectedProgrammerCours.getMatiere().getLibelle());
            // enregistrement dans historique
            Historiques historique = new Historiques();
            historique.setLibelle(selectedGroupePedagogique.getDescription() + "CoursProgramme"+" "+selectedProgrammerCours.getMatiere().getLibelle());
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + "coursprogramme_" + selectedProgrammerCours.getMatiere().getLibelle()));
            historique.setGroupePedagogique(selectedGroupePedagogique.getDescription());
            historique.setDateEdition(JsfUtil.getDateEdition());
            historique.setAnneeAcademique(anneeAcademique);
            historiquesFacade.create(historique);
            File fileDowload = new File(pathOutPDF + nomFichier + "coursprogramme_" + selectedProgrammerCours.getMatiere().getLibelle() + ".pdf");
            JsfUtil.flushToBrowser(fileDowload, nomFichier + "coursprogramme_" + selectedProgrammerCours.getMatiere().getLibelle() + ".pdf");

        } catch (Exception ex) {
            System.out.println("ExceptionR " + ex.getMessage());

        }

    }

}
