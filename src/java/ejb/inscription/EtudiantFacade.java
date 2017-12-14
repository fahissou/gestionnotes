/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;
import jpa.inscription.Inscription;
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
    
    
    
    
}
