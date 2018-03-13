/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.administration;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.administration.Utilisateur;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }
    
    public Utilisateur recupDirecteurEtude(String responsabilite){
        Utilisateur  utilisateur = null;
        try {
            Query query = em.createQuery("SELECT U FROM Utilisateur U WHERE U.responsabilite = :responsabilite");
            // set parameters
            query.setParameter("responsabilite", responsabilite);
            utilisateur = (Utilisateur) query.getSingleResult();
        } catch (Exception ex) {
        }
        return utilisateur;
    }
    
    public Utilisateur getCurrentUser(String id){
        System.out.println("ok userFacade "+id);
        Utilisateur  utilisateur = null;
        try {
            Query query = em.createQuery("SELECT U FROM Utilisateur U WHERE U.login = :id");
            // set parameters
            query.setParameter("id", id);
            utilisateur = (Utilisateur) query.getSingleResult();
        } catch (Exception ex) {
        }
        return utilisateur;
    }
}
