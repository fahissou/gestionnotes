
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Notes;
import jpa.module.Matiere;
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
    
    @Override
    public void edit(Notes notes) {
        if(notes.getNote() >= 12.0){
            notes.setEtatValidation("VALIDÉ");
        }
        super.edit(notes);
    }
    
    
    public List<Notes> listeNoteGpAnnee(String anneeAcademique, String groupePedagogique, Matiere matiere) {
       String matiereLibelle = matiere.getLibelle();
        Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.anneeUniversitaire = :anneeAcademique AND N.inscription.groupePedagogique.description = :groupePedagogique AND N.matiere.libelle = :matiereLibelle");
        // set parameters
        query.setParameter("anneeAcademique", anneeAcademique);
        query.setParameter("groupePedagogique", groupePedagogique);
        query.setParameter("matiereLibelle", matiereLibelle);      
        List<Notes> list = query.getResultList();
        return list; 
    }
    
    
}
