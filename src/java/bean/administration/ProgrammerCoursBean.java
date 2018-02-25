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
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.administration.ProgrammerCours;
import jpa.formation.Filiere;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Enseignant;
import jpa.inscription.Enseigner;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "programmerCoursBean")
public class ProgrammerCoursBean implements Serializable {
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
    private List<ProgrammerCours> filteredList;
    private Filiere selectedFiliere;

    private List<GroupePedagogique> listGroupePedagogiques;
    private List<Matiere> listMatieres;
    private List<Enseignant> listeEnseignants;
    private AnneeAcademique anneeAcademique;
    private Enseignant enseignantResp;
    private List<Enseignant> listeEnseignantResp;
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    /**
     * Creates a new instance of ProgrammerCoursBean
     */
    public ProgrammerCoursBean() {
    }

    @PostConstruct
    public void init() {
        listeProgrammerCourss = programmerCoursFacade.findAll();
        prepareCreate();
        anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
        listeEnseignantResp = enseignantFacade.findAllEnseignantResponsa();
    }

    public void initEnseignants() {
        listeEnseignants = getEnseignants(enseignerFacade.findEnseignerByMatiere(newProgrammerCours.getMatiere()));
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
                programmerCoursFacade.create(newProgrammerCours);
                matiereFacade.edit(newProgrammerCours.getMatiere());
                genererEtatCoursProgrammer();
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
            listeProgrammerCourss = programmerCoursFacade.findAll();
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("ProgrammerCoursEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        String msg;
        try {
            matiereFacade.edit(selectedProgrammerCours.getMatiere());
            programmerCoursFacade.remove(selectedProgrammerCours);
            msg = JsfUtil.getBundleMsg("ProgrammerCoursDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            initList();
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
        initList();
    }

    public void initList() {
        listGroupePedagogiques = groupePedagogiqueFacade.getListGpByFilire(selectedFiliere);
        listMatieres = getMatieresNP(newProgrammerCours.getGroupePedagogique());
        listeProgrammerCourss = programmerCoursFacade.listeProgrammeByGroupe(newProgrammerCours.getGroupePedagogique(), anneeAcademique);
    }

    public void reset(ActionEvent e) {
        this.newProgrammerCours.reset();
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
    public List<Matiere> getMatieresNP(GroupePedagogique groupePedagogique) {
        List<Matiere> matieres = new ArrayList<>();
        List<Matiere> matieresM = matiereFacade.getMatiereByGroupe(groupePedagogique);
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
            parametreEntetes.put("filiere", newProgrammerCours.getGroupePedagogique().getFiliere().getLibelle().toUpperCase() + " :  " + newProgrammerCours.getGroupePedagogique().getDescription());
            parametreEntetes.put("matiere", newProgrammerCours.getMatiere().getLibelle().toUpperCase());
            Enseignant enseignant = newProgrammerCours.getEnseignant();
            parametreEntetes.put("enseignant", JsfUtil.getLabelGradeEnseignant(enseignant.getGrade()) + " " + enseignant.getPrenom().toUpperCase() + " " + enseignant.getNom());
            String datedebut = formatDate.format(newProgrammerCours.getDateDebut());
            parametreEntetes.put("datedebut", datedebut);
            String datefin = formatDate.format(newProgrammerCours.getDateFin());
            parametreEntetes.put("datefin", datefin);
            parametreEntetes.put("d", JsfUtil.getDateEdition());
            parametreEntetes.put("responsable", enseignantResp.getResponsabilite());
            parametreEntetes.put("nomResp", JsfUtil.getLabelGradeEnseignant(enseignantResp.getGrade()) + " " + enseignantResp.getPrenom() + " " + enseignantResp.getNom());
            JsfUtil.generateurXDOCReportStatic(pathIn + "/programmationcours.docx", parametreEntetes, repertoire1.getAbsolutePath() + "/", "coursprogramme_" + newProgrammerCours.getMatiere().getLibelle());
            JsfUtil.docxToPDF(repertoire1.getAbsolutePath() + "/", repertoire2.getAbsolutePath() + "/");
            JsfUtil.mergePDF(repertoire2.getAbsolutePath() + "/", pathOutPDF, nomFichier + "coursprogramme_" + newProgrammerCours.getMatiere().getLibelle());
            // enregistrement dans historique
            Historiques historique = new Historiques();
            historique.setLibelle(newProgrammerCours.getGroupePedagogique().getDescription() + "CoursProgramme"+" "+newProgrammerCours.getMatiere().getLibelle());
            historique.setLienFile(JsfUtil.getRealPath(pathOutPDF + nomFichier + "coursprogramme_" + newProgrammerCours.getMatiere().getLibelle()));
            historique.setGroupePedagogique(newProgrammerCours.getGroupePedagogique().getDescription());
            historique.setDateEdition(JsfUtil.getDateEdition());
            historiquesFacade.create(historique);

        } catch (Exception ex) {
            System.out.println("ExceptionR " + ex.getMessage());

        }

    }

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

}
