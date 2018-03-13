
package ejb.formation;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.formation.Historiques;
import jpa.inscription.AnneeAcademique;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class HistoriquesFacade extends AbstractFacade<Historiques> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoriquesFacade() {
        super(Historiques.class);
    }
    
    @Override
    public void create(Historiques historiques) {
        historiques.setId(JsfUtil.generateId());
        super.create(historiques);
    }
    
    public List<Historiques> findAll1(AnneeAcademique anneeAcademique) {
        List<Historiques> liste = null;
        try {
            Query query = em.createQuery("SELECT H FROM Historiques H WHERE H.anneeAcademique = :anneeAcademique");
            query.setParameter("anneeAcademique", anneeAcademique);
            liste = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
        }
        return liste;
    }
}
