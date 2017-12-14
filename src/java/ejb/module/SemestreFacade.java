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
import jpa.module.Matiere;
import jpa.module.Semestre;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class SemestreFacade extends AbstractFacade<Semestre> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SemestreFacade() {
        super(Semestre.class);
    }
    
    @Override
    public void create(Semestre semestre) {
        semestre.setId(JsfUtil.generateId());
        super.create(semestre);
    }
    
}
