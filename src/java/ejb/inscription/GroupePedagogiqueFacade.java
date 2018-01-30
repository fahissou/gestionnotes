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
import jpa.formation.Filiere;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
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

    public List<GroupePedagogique> getListGpByFilire(Filiere filieres) {
        String idFilire = filieres.getId();
        List<GroupePedagogique> liste = null;
        try {
            Query query = em.createQuery("SELECT G FROM GroupePedagogique G WHERE G.filiere.id = :idFilire");
            query.setParameter("idFilire", idFilire);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }
    
    public  GroupePedagogique getGroupePedagogique(String groupeP){
         
        Query query = em.createQuery("SELECT G FROM GroupePedagogique G WHERE G.description = :groupeP");
        query.setParameter("groupeP", groupeP);
        try {
             return (GroupePedagogique) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
 
    }

}
