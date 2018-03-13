
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.AnneeAcademique;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class AnneeAcademiqueFacade extends AbstractFacade<AnneeAcademique> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnneeAcademiqueFacade() {
        super(AnneeAcademique.class);
    }
    @Override
    public void create(AnneeAcademique anneeAcademique) {
        anneeAcademique.setId(JsfUtil.generateId());
        super.create(anneeAcademique);
    }
    
    public AnneeAcademique getCurrentAcademicYear(){
        int etat = 1;
        AnneeAcademique  anneeAcademique = null;
        try {
            Query query = em.createQuery("SELECT A FROM AnneeAcademique A WHERE A.etat = :etat");
            
            // set parameters
            query.setParameter("etat", etat);
            anneeAcademique = (AnneeAcademique) query.getSingleResult();
        } catch (Exception ex) {
        }
        return anneeAcademique;
    }
    
    public List<String> newAnneeAcademique(){
        int etat = 1;
        AnneeAcademique  anneeAcademique = null;
        List<String> anneeAcademic = null;
        try {
            Query query = em.createQuery("SELECT A FROM AnneeAcademique A WHERE A.etat = :etat");
            // set parameters
            query.setParameter("etat", etat);
            anneeAcademique = (AnneeAcademique) query.getSingleResult();
            anneeAcademic.add(JsfUtil.nextAcademicYear(anneeAcademique.getDescription()));
        } catch (Exception ex) {
            
        }
        return anneeAcademic;
    }
}


