/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.administration;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.administration.ProgrammerCours;
import jpa.inscription.GroupePedagogique;
import jpa.module.Matiere;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class ProgrammerCoursFacade extends AbstractFacade<ProgrammerCours> {

    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgrammerCoursFacade() {
        super(ProgrammerCours.class);
    }

    @Override
    public void create(ProgrammerCours programmerCours) {
        programmerCours.setId(JsfUtil.generateId());
        super.create(programmerCours);
    }

    public List<ProgrammerCours> listeProgrammeByGroupe(GroupePedagogique groupePedagogique) {
        List<ProgrammerCours> liste;
        try {
            Query query = em.createQuery("SELECT P FROM ProgrammerCours P WHERE P.groupePedagogique =:groupePedagogique ORDER BY P.matiere.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }

        return liste;
    }

    public List<Matiere> listeMatieresPC(GroupePedagogique groupePedagogique) {
        List<Matiere> liste;
        try {
            Query query = em.createQuery("SELECT P.matiere FROM ProgrammerCours P WHERE P.groupePedagogique =:groupePedagogique ORDER BY P.matiere.libelle ASC");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }
}
