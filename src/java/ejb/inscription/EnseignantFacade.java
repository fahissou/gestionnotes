/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Enseignant;
import jpa.inscription.GroupePedagogique;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class EnseignantFacade extends AbstractFacade<Enseignant> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnseignantFacade() {
        super(Enseignant.class);
    }
    
    public  Enseignant getEnseignantByResponsabilite(String responsabilite){
         Enseignant enseignant = null;
        Query query = em.createQuery("SELECT E FROM Enseignant E WHERE E.responsabilite = :responsabilite");
        query.setParameter("responsabilite", responsabilite);
        try {
             enseignant =  (Enseignant) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            enseignant = new Enseignant();
        }
        return enseignant;
    }
    
    
}
