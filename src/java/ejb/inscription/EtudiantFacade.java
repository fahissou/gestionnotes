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
import jpa.inscription.Etudiant;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class EtudiantFacade extends AbstractFacade<Etudiant> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtudiantFacade() {
        super(Etudiant.class);
    }
    
    public Etudiant getByMatricule(String sessionId) {
        Query q = getEntityManager().createQuery("select E FROM Etudiant E WHERE E.sessionId = :sessionId");
        // set parameters
        q.setParameter("sessionId", sessionId);
        try {
            Etudiant etudiant = (Etudiant) q.getSingleResult();
            return etudiant;
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
    
//    @Override
//    public void create(Etudiant etudiant){
//        etudiant.setLogin(JsfUtil.generateId());
//        super.create(etudiant);
//    }
}
