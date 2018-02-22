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
import jpa.inscription.Enseigner;
import jpa.module.Matiere;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class EnseignerFacade extends AbstractFacade<Enseigner> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnseignerFacade() {
        super(Enseigner.class);
    }
    
    @Override
    public void create(Enseigner enseigner) {
        enseigner.setId(enseigner.getEnseignant().getLogin()+"_"+enseigner.getMatiere().getId());
        super.create(enseigner);
    }
    
    public List<Enseigner> findEnseignerByMatiere(Matiere matiere) {
        List<Enseigner> liste;
        String idMat = matiere.getId();
        try {
            Query query = em.createQuery("SELECT E FROM Enseigner E WHERE E.matiere.id = :idMat");
            query.setParameter("idMat", idMat);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }
    
}
