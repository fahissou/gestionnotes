/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import ejb.administration.AnneeAcademiqueFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Etudiant;
import jpa.inscription.Inscription;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class InscriptionFacade extends AbstractFacade<Inscription> {
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InscriptionFacade() {
        super(Inscription.class);
    }
    
    @Override
    public void create(Inscription inscription) {
        inscription.setId(JsfUtil.generateId());
        super.create(inscription);
    }
    
    @Override
    public List<Inscription> findAll(){
        List<Inscription> liste = null;
        try {
        String currentInscription = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.anneeAcademique.description = :currentInscription");
        query.setParameter("currentInscription", currentInscription);
        liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
             liste = new ArrayList<>();
        }
        return liste;
    }
    
    public  Inscription getInscriptionsByEtudiant(String matricule, String anneeAcademique){
         
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.anneeUniversitaire = :anneeAcademique");
        query.setParameter("matricule", matricule);
        query.setParameter("anneeAcademique", anneeAcademique);
        try {
             return (Inscription) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
 
    }
    
    public List<Inscription> getListInscriptionByGP(String groupePedagogique, String anneeAcademique) {
        List<Inscription> inscriptions = null;
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.description = :groupePedagogique AND I.anneeAcademique.description = :anneeAcademique");
        query.setParameter("groupePedagogique", groupePedagogique);
        query.setParameter("anneeAcademique", anneeAcademique);
        try {
             inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }
    
    public List<Inscription> getListInscriptionByEtudiant(Etudiant etudiant) {
        String matricule = etudiant.getLogin();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule");
        query.setParameter("matricule", matricule);
        try {
             return query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
    
    

    public List<Inscription> listeReinscription(String groupePedagogique, String anneeAcademique, String resultat) {
        String anneeAc = JsfUtil.previousAcademicYear(anneeAcademique);
        List<Inscription> inscriptions = null;
        try {
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.description = :groupePedagogique AND I.anneeAcademique.description = :anneeAc AND I.resultat = :resultat");
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("anneeAc", anneeAc);
            query.setParameter("resultat", resultat);
            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    
}
