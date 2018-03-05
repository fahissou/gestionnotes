/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.module;

import ejb.AbstractFacade;
import ejb.administration.ProgrammerCoursFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
import jpa.module.Ue;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class MatiereFacade extends AbstractFacade<Matiere> {
    @EJB
    private ProgrammerCoursFacade programmerCoursFacade;
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
    
     public List<Matiere> getMatiereByUe(Ue ue) {
         String idUE = ue.getId();
        Query query = em.createQuery("SELECT M FROM Matiere M WHERE M.ue.id = :idUE");
        // set parameters
        query.setParameter("idUE",idUE);
        List<Matiere> list = query.getResultList();
        return list; 
    }

    public List<Matiere> getMatiereByGroupe(GroupePedagogique groupePedagogique) {
        List<Matiere> liste;
        try {
            Query query = em.createQuery("SELECT M FROM Matiere M WHERE M.groupePedagogique =:groupePedagogique ORDER BY M.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }
    
    public List<Matiere> getMatiereByGroupe(GroupePedagogique groupePedagogique, Semestre semestre) {
        List<Matiere> liste;
        try {
            Query query = em.createQuery("SELECT M FROM Matiere M WHERE M.groupePedagogique =:groupePedagogique AND M.ue.semestre = :semestre ORDER BY M.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("semestre", semestre);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }

     
    
    
}
