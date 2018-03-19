/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.administration;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.administration.Responsabilite;
import jpa.inscription.Enseignant;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class ResponsabiliteFacade extends AbstractFacade<Responsabilite> {

    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponsabiliteFacade() {
        super(Responsabilite.class);
    }

    @Override
    public void create(Responsabilite responsabilite) {
        responsabilite.setId(responsabilite.getRole() + "_" + responsabilite.getFiliere().getLibelle());
        super.create(responsabilite);
    }

    public List<Responsabilite> findAllEnseignantResponsa() {
        List<Responsabilite> liste = null;
        try {
            Query query = em.createQuery("SELECT R.enseignant FROM Responsabilite R");
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }

    public Enseignant getEnseignantByResponsabilite(String responsabilite) {
        Enseignant enseignant = null;
        Query query = em.createQuery("SELECT R.enseignant FROM Responsabilite R WHERE R.role = :responsabilite");
        query.setParameter("responsabilite", responsabilite);
        try {
            enseignant = (Enseignant) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return enseignant;
    }
    
    
    public List<Responsabilite> findResponsabilite() {
        List<Responsabilite> liste = null;
        String rf = "RESPONSABLE DE FORMATION";
        try {
            Query query = em.createQuery("SELECT R FROM Responsabilite R WHERE R.role != :rf");
            query.setParameter("rf",rf);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }

}
