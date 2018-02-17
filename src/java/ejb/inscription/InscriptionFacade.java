/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
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
//        Query query = em.createNativeQuery("SELECT * FROM Inscription AS I WHERE I. .anneeAcademique. = currentInscription");
        query.setParameter("currentInscription", currentInscription);
        liste = query.getResultList();
            
        } catch (NoResultException | NonUniqueResultException e) {
             liste = new ArrayList<>();
        }
        return liste;
    }
    
    public List<Inscription> findAllByInscription(AnneeAcademique anneeAcademique) {
        List<Inscription> inscriptions = null;
        try {
        String id = anneeAcademique.getId();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.anneeAcademique.id = :id");
        query.setParameter("id", id);
             inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }
    
    
    public void afficher (List<Inscription> inscriptions) {
        for (Inscription inscription : inscriptions) {
            System.out.println(inscription.getEtudiant().getNom());
        }
    }
    
    
    public  Inscription getInscriptionsByEtudiant(String matricule, String anneeAcademique){
        
        try {
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.anneeAcademique.description = :anneeAcademique");
        query.setParameter("matricule", matricule);
        query.setParameter("anneeAcademique", anneeAcademique);
        
             return (Inscription) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
 
    }
    
    public  boolean isInscriptionExist(String matricule, String anneeAcademique){
        try {
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.anneeAcademique.description = :anneeAcademique");
        query.setParameter("matricule", matricule);
        query.setParameter("anneeAcademique", anneeAcademique);
        Inscription inscription = (Inscription) query.getSingleResult();
        return true;
        } catch (NoResultException | NonUniqueResultException e) {
            return false;
        }
 
    }
    
    public List<Inscription> getListInscriptionByGP(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique) {
        String idGP = groupePedagogique.getId();
        String idAnneAca = anneeAcademique.getId();
        List<Inscription> inscriptions = null;
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.id = :idGP AND I.anneeAcademique.id = :idAnneAca");
        query.setParameter("idGP", idGP);
        query.setParameter("idAnneAca", idAnneAca);
        try {
             inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }
    
    public List<Inscription> getListInscriptionByGP1(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique) {
        List<Inscription> inscriptions = null;
        String idGP = groupePedagogique.getId();
        String idAnneeAcad = anneeAcademique.getId();
        String var = "R";
        try {
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.id = :idGP AND I.anneeAcademique.id = :idAnneeAcad AND I.resultat = :var");
        query.setParameter("idGP", idGP);
        query.setParameter("idAnneeAcad", idAnneeAcad);
        query.setParameter("var", var);
        
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
    
    public List<Inscription> getInscriptionByEtudiant(Etudiant etudiant, GroupePedagogique groupePedagogique) {
        List<Inscription> inscriptions = null;
        try {
        String matricule = etudiant.getLogin();
        String idGp = groupePedagogique.getId();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.groupePedagogique.id = :idGp");
        query.setParameter("matricule", matricule);
        query.setParameter("idGp", idGp);
             inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }
    
    
    

    public List<Inscription> listeReinscription(String groupePedagogique, String anneeAcademique) {
        String anneeAc = JsfUtil.previousAcademicYear(anneeAcademique);
        List<Inscription> inscriptions = null;
        String var = "R";
        try {
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.description = :groupePedagogique AND I.anneeAcademique.description = :anneeAc AND I.resultat = :var");
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("anneeAc", anneeAc);
            query.setParameter("var", var);
            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    
}
