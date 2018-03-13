/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.module;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
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
        semestre.setId(semestre.getValeur()+"_"+semestre.getOrdre());
        super.create(semestre);
    }
    
    public List<Semestre> getSemetreByGP(GroupePedagogique groupePedagogique) {
       int ordre = groupePedagogique.getOrdre();
       List<Semestre> list = null;
       try {
        Query query = em.createQuery("SELECT S FROM Semestre S WHERE S.ordre = :ordre ORDER BY S.valeur ASC");
        // set parameters
        query.setParameter("ordre", ordre);
        
             list = query.getResultList();
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        
        return list; 
    }
}
