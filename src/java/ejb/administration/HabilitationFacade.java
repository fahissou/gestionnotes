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
import jpa.administration.Habilitation;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Stateless
public class HabilitationFacade extends AbstractFacade<Habilitation> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HabilitationFacade() {
        super(Habilitation.class);
    }
    @Override
    public void create(Habilitation habilitation) {
        habilitation.setId(habilitation.getUtilisateur().getLogin()+"_"+habilitation.getGroupeUtilisateur().getLibelle());
        super.create(habilitation);
    }
}
