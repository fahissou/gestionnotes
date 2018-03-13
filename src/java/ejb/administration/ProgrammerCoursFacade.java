/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.administration;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.administration.ProgrammerCours;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Enseignant;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class ProgrammerCoursFacade extends AbstractFacade<ProgrammerCours> {

    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgrammerCoursFacade() {
        super(ProgrammerCours.class);
    }

    @Override
    public void create(ProgrammerCours programmerCours) {
        try {
            programmerCours.setId(programmerCours.getMatiere().getLibelle()+"_"+programmerCours.getEnseignant().getLogin());
            super.create(programmerCours);
        } catch (Exception e) {
        }
        
    }

    public List<ProgrammerCours> listeProgrammeByGroupe(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique, Semestre semestre) {
        List<ProgrammerCours> liste = null;
        try {
            Query query = em.createQuery("SELECT P FROM ProgrammerCours P WHERE P.groupePedagogique = :groupePedagogique AND P.anneeAcademique = :anneeAcademique AND P.matiere.ue.semestre = :semestre ORDER BY P.matiere.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("anneeAcademique", anneeAcademique);
            query.setParameter("semestre", semestre);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }

        return liste;
    }

    public List<Matiere> listeMatieresPC(GroupePedagogique groupePedagogique) {
        List<Matiere> liste = null;
        try {
            Query query = em.createQuery("SELECT P.matiere FROM ProgrammerCours P WHERE P.groupePedagogique =:groupePedagogique ORDER BY P.matiere.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }

    public Enseignant findEnseignantByMatiere(Matiere matiere) {
        try {
            Query query = em.createQuery("SELECT P.enseignant FROM ProgrammerCours P WHERE P.matiere = :matiere");
            query.setParameter("matiere", matiere);
            return (Enseignant) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    public ProgrammerCours findProgrCoursByAnneeGpMat(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique, Matiere matiere) {
        ProgrammerCours programmerCours = null;
        try {
            Query query = em.createQuery("SELECT P FROM ProgrammerCours P WHERE P.matiere = :matiere AND P.groupePedagogique = :groupePedagogique AND P.anneeAcademique = :anneeAcademique");
            query.setParameter("matiere", matiere);
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("anneeAcademique", anneeAcademique);
            programmerCours = (ProgrammerCours) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return programmerCours;
    }
}
