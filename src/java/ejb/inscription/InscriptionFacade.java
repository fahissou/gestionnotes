/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.Etudiant;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class InscriptionFacade extends AbstractFacade<Inscription> {

    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InscriptionFacade() {
        super(Inscription.class);
    }

    @Override
    public void create(Inscription inscription) {
        inscription.setId(inscription.getEtudiant().getLogin() + "_" + inscription.getAnneeAcademique().getDescription());
        super.create(inscription);
    }

    @Override
    public List<Inscription> findAll() {
        List<Inscription> liste = null;
        try {
            String currentInscription = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.anneeAcademique.description = :currentInscription OR I.sessions.description = :currentInscription");
//        Query query = em.createNativeQuery("SELECT * FROM Inscription AS I WHERE I. .anneeAcademique. = currentInscription");
            query.setParameter("currentInscription", currentInscription);
            liste = query.getResultList();

        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }
        return liste;
    }

    public List<Inscription> findAllByInscription(AnneeAcademique anneeAcademique) {
        List<Inscription> inscriptions = null;
        try {
            String id = anneeAcademique.getId();
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.anneeAcademique.id = :id");
            query.setParameter("id", id);
            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    public void afficher(List<Inscription> inscriptions) {
        for (Inscription inscription : inscriptions) {
            System.out.println(inscription.getEtudiant().getNom());
        }
    }

    public Inscription getInscriptionsByEtudiant(String matricule, String anneeAcademique) {

        try {
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.anneeAcademique.description = :anneeAcademique");
            query.setParameter("matricule", matricule);
            query.setParameter("anneeAcademique", anneeAcademique);

            return (Inscription) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }

    }

    public boolean isInscriptionExist(Inscription inscription, AnneeAcademique anneeAcademique) {
            String idInscip = inscription.getId();
            String idAnnAca = anneeAcademique.getId();
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.id = :idInscip AND I.anneeAcademique.id = :idAnnAca");
            query.setParameter("idInscip", idInscip);
            query.setParameter("idAnnAca", idAnnAca);
            try {
            Inscription inscription1 = (Inscription) query.getSingleResult();
            return true;
        } catch (NoResultException | NonUniqueResultException e) {
            return false;
        }

    }

    public List<Inscription> getListInscriptionByGP(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique) {
        
        List<Inscription> inscriptions = null;
        String idGP = groupePedagogique.getId();
        String idAnneAca = anneeAcademique.getId();
        String res = "R";
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.id = :idGP AND ((I.anneeAcademique.id = :idAnneAca) OR (I.sessions.id = :idAnneAca AND I.resultat = :res))");
        query.setParameter("idGP", idGP);
        query.setParameter("idAnneAca", idAnneAca);
        query.setParameter("res", res);
        try {
            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    public List<Inscription> getListInscriptionByGP1(GroupePedagogique groupePedagogique, AnneeAcademique anneeAcademique) {
        List<Inscription> inscriptions = null;
        String idGP = groupePedagogique.getId();
        String idAnneeAcad = anneeAcademique.getId();
        String var = "R";
        try {
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique.id = :idGP AND I.anneeAcademique.id = :idAnneeAcad AND I.resultat = :var");
            query.setParameter("idGP", idGP);
            query.setParameter("idAnneeAcad", idAnneeAcad);
            query.setParameter("var", var);

            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    public List<Inscription> getListInscriptionByEtudiant(Etudiant etudiant) {
        String matricule = etudiant.getLogin();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule");
        query.setParameter("matricule", matricule);
        try {
            return query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

    public List<Inscription> getInscriptionByEtudiant(Etudiant etudiant, GroupePedagogique groupePedagogique) {
        List<Inscription> inscriptions = null;
        try {
            String matricule = etudiant.getLogin();
            String idGp = groupePedagogique.getId();
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.etudiant.login = :matricule AND I.groupePedagogique.id = :idGp");
            query.setParameter("matricule", matricule);
            query.setParameter("idGp", idGp);
            inscriptions = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    public List<Inscription> listeReinscription(GroupePedagogique groupePedagogique1, GroupePedagogique groupePedagogique2,String var1,String var2,String var3) {
        AnneeAcademique anneeAcademique = anneeAcademiqueFacade.getCurrentAcademicYear();
        String anneeAc = JsfUtil.previousAcademicYear(anneeAcademique.getDescription());
        String idGP1 = groupePedagogique1.getId();
        String idGP2 = groupePedagogique2.getId();
//        List<Inscription> inscriptions1 = null;
//        List<Inscription> inscriptions2 = null;
        List<Inscription> inscriptions = null;
//        String var1 = "A";
//        String var2 = "R";
        Query query1 = em.createQuery("SELECT I FROM Inscription I WHERE (I.groupePedagogique.id = :idGP1 AND I.anneeAcademique.description = :anneeAc AND (I.resultat = :var1 OR I.resultat = :var2)) OR (I.groupePedagogique.id = :idGP2 AND I.anneeAcademique.description = :anneeAc AND I.resultat = :var3)");
        query1.setParameter("idGP1", idGP1);
        query1.setParameter("idGP2", idGP2);
        query1.setParameter("var1", var1);
        query1.setParameter("var2", var2);
        query1.setParameter("var3", var3);
        query1.setParameter("anneeAc", anneeAc);
        try {
            inscriptions = query1.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            inscriptions = new ArrayList<>();
        }
        return inscriptions;
    }

    public List<Inscription> getListConcat(List<Inscription> l1, List<Inscription> l2) {
        int index1 = l1.size();
        for (int i = 0; i < l2.size(); i++) {
            int index = index1 + i;
            l1.add(index, l2.get(i));
        }
        return l1;
    }
    
    public List<Inscription>findListEtudiant(GroupePedagogique groupePedagogique){
        List<Inscription> liste;
        try {
            Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.groupePedagogique =:groupePedagogique");
            // set parameters
            query.setParameter("groupePedagogique", groupePedagogique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            liste = new ArrayList<>();
        }

        return liste;
    }
    

}
