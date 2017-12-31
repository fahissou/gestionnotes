/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.administration.Utilisateur;
import jpa.inscription.Etudiant;
import static jpa.inscription.Etudiant_.login;
import jpa.inscription.Inscription;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class InscriptionFacade extends AbstractFacade<Inscription> {
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
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.description = :groupePedagogique AND I.anneeUniversitaire = :anneeAcademique");
        query.setParameter("groupePedagogique", groupePedagogique);
        query.setParameter("anneeAcademique", anneeAcademique);
        try {
             return query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
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
    
}
