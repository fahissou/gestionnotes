/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.inscription.Enseigner;

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
    
}
