/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.formation;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.formation.Filiere;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class FiliereFacade extends AbstractFacade<Filiere> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FiliereFacade() {
        super(Filiere.class);
    }
    
    @Override
    public void create(Filiere filiere) {
        filiere.setId(filiere.getLibelle());
        super.create(filiere);
    }
    
    @Override
    public List<Filiere> findAll() {
        List<Filiere> liste = null;
        String defaultFiliere = "TOUTES FILIERES";
        try {
            Query query = em.createQuery("SELECT F FROM Filiere F WHERE F.id != :defaultFiliere");
            query.setParameter("defaultFiliere", defaultFiliere);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }
    
     public List<Filiere> findDefaultFiliere() {
        List<Filiere> liste = null;
        String defaultFiliere = "TOUTES FILIERES";
        try {
            Query query = em.createQuery("SELECT F FROM Filiere F WHERE F.id = :defaultFiliere");
            query.setParameter("defaultFiliere", defaultFiliere);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }
}
