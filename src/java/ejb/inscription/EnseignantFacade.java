/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Enseignant;
import jpa.inscription.Specialite;

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
    
    @Override
    public void create(Enseignant enseignant) {
        enseignant.setLogin(enseignant.getNom()+"_"+enseignant.getPrenom());
        super.create(enseignant);
    }
    
//    public  Enseignant getEnseignantByResponsabilite(String responsabilite){
//         Enseignant enseignant = null;
//        Query query = em.createQuery("SELECT E FROM Enseignant E WHERE E.adresse = :responsabilite");
//        query.setParameter("responsabilite", responsabilite);
//        try {
//             enseignant =  (Enseignant) query.getSingleResult();
//        } catch (NoResultException | NonUniqueResultException e) {
//        }
//        return enseignant;
//    }
    
    public List<Enseignant> findEnseignantBySpecialite(Specialite specialite) {
        List<Enseignant> liste = null;
        String idSpecialite = specialite.getId();
        try {
            Query query = em.createQuery("SELECT E FROM Enseignant E WHERE E.specialite.id = :idSpecialite");
            query.setParameter("idSpecialite", idSpecialite);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }
    
//    public List<Enseignant> findAllEnseignantResponsa() {
//        List<Enseignant> liste = null;
//        String reponsabilite = "AUCUNE";
//        try {
//            Query query = em.createQuery("SELECT E FROM Enseignant E WHERE E.responsabilite != :reponsabilite");
//            query.setParameter("reponsabilite", reponsabilite);
//            liste = query.getResultList();
//        } catch (NoResultException | NonUniqueResultException e) {
//        }
//        return liste;
//    }
    
    
}
