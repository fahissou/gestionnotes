
package ejb.formation;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.formation.Historiques;

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
    
}
