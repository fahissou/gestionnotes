/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;
import jpa.inscription.Inscription;
import ejb.AbstractFacade;
import ejb.administration.AnneeAcademiqueFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Etudiant;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class EtudiantFacade extends AbstractFacade<Etudiant> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;
    @EJB
    private AnneeAcademiqueFacade anneeAcademiqueFacade;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtudiantFacade() {
        super(Etudiant.class);
    }
    
    
    public List<Inscription> findAllEtudiantInscris(String groupePedagogique){
        List<Inscription> liste = null;
        try {
        String currentInscription = anneeAcademiqueFacade.getCurrentAcademicYear().getDescription();
        Query query = em.createQuery("SELECT I FROM Inscription I WHERE I.anneeAcademique.description = :currentInscription AND I.groupePedagogique.description =:groupePedagogique");
        query.setParameter("currentInscription", currentInscription);
        query.setParameter("groupePedagogique", groupePedagogique);
        liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
             liste = new ArrayList<>();
        }
        return liste;
    }

    
    
}
