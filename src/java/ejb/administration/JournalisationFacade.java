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
import jpa.administration.Journalisation;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class JournalisationFacade extends AbstractFacade<Journalisation> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public JournalisationFacade() {
        super(Journalisation.class);
    }
    
    @Override
    public void create(Journalisation journalisation) {
        journalisation.setId(JsfUtil.generateId());
        super.create(journalisation);
    } 
}
