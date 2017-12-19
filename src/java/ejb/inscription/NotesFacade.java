
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import static jpa.module.Matiere_.notes;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class NotesFacade extends AbstractFacade<Notes> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotesFacade() {
        super(Notes.class);
    }
    
    @Override
    public void create(Notes notes) {
        notes.setId(JsfUtil.generateId());
        super.create(notes);
    }
    
    
    public List<Notes> listeNoteGpAnnee(String anneeAcademique, String groupePedagogique ,String matiere) {
       
        Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.anneeUniversitaire = :anneeAcademique AND N.inscription.groupePedagogique.description = :groupePedagogique AND N.matiere.libelle = :matiere");
        // set parameters
        query.setParameter("anneeAcademique", anneeAcademique);
        query.setParameter("groupePedagogique", groupePedagogique);
        query.setParameter("matiere", matiere);      
        List<Notes> list = query.getResultList();
        return list; 
    }
    
}
