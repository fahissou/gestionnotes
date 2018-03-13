/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.module;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
public class UeFacade extends AbstractFacade<Ue> {

    @EJB
    private MatiereFacade matiereFacade;

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

    public List<Ue> getUeByGroupePedagogique(GroupePedagogique groupePeda) {
        List<Ue> list = null;
        try {
            Query query = em.createQuery("SELECT U FROM Ue U WHERE U.groupePedagogique = :groupePeda");
            // set parameters
            query.setParameter("groupePeda", groupePeda);
            list = query.getResultList();
        } catch (Exception e) {
        }

        return list;
    }

    public List<Ue> getUeByGroupePedagogique(GroupePedagogique groupePedagogique, Semestre semestre) {
        List<Ue> list = null;
        String idGroupePedagogique = groupePedagogique.getId();
        String idSemestre = semestre.getId();
        try {
            Query query = em.createQuery("SELECT U FROM Ue U WHERE U.groupePedagogique.id = :idGroupePedagogique AND U.semestre.id = :idSemestre ORDER BY U.libelle");
            // set parameters
            query.setParameter("idGroupePedagogique", idGroupePedagogique);
            query.setParameter("idSemestre", idSemestre);
            list = query.getResultList();
        } catch (Exception e) {
        }
        return list;
    }

    public List<Ue> getRealUE(List<Ue> ues) {
        List<Ue> uesReal = new ArrayList<>();
        for (int i = 0; i < ues.size(); i++) {
            int val = matiereFacade.getMatiereByUe(ues.get(i)).size();
            if (val != 0) {
                uesReal.add(ues.get(i));
            }
        }
        return uesReal;
    }
}
