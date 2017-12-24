/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.module;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.module.Matiere;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class UeFacade extends AbstractFacade<Ue> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UeFacade() {
        super(Ue.class);
    }
    
    @Override
    public void create(Ue ue) {
        ue.setId(JsfUtil.generateId());
        super.create(ue);
    }
    
    public List<Ue> getUeByGroupePedagogique(String groupePedagogique) {
       
        Query query = em.createQuery("SELECT U FROM Ue U WHERE U.groupePedagogique.description = :groupePedagogique");
        // set parameters
        query.setParameter("groupePedagogique", groupePedagogique);
        List<Ue> list = query.getResultList();
        return list; 
    }
    
    
}
