/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.module;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class MatiereFacade extends AbstractFacade<Matiere> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatiereFacade() {
        super(Matiere.class);
    }
    
    @Override
    public void create(Matiere matiere) {
        matiere.setId(JsfUtil.generateId());
        super.create(matiere);
    }
    
}
