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
import jpa.inscription.GroupePedagogique;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@Stateless
public class GroupePedagogiqueFacade extends AbstractFacade<GroupePedagogique> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupePedagogiqueFacade() {
        super(GroupePedagogique.class);
    }
    @Override
    public void create(GroupePedagogique groupePedagogique) {
        groupePedagogique.setId(JsfUtil.generateId());
        super.create(groupePedagogique);
    }
}
