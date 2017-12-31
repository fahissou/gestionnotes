package ejb.inscription;

import ejb.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.Inscription;
import jpa.inscription.Inscription_;
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
        if (notes.getNote() >= 12.0) {
            notes.setEtatValidation("VALIDÉ");
        }
        super.edit(notes);
    }

    public List<Notes> listeNoteGpAnnee(String inscriptionID, String groupePedagogique, Matiere matiere) {
        try {
            String matiereID = matiere.getId();
            
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.anneeUniversitaire = :inscriptionID AND N.inscription.groupePedagogique.description = :groupePedagogique AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("inscriptionID", inscriptionID);
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("matiereID", matiereID);
            List<Notes> list = query.getResultList();
            return list;
        } catch (Exception ex) {
            return null;
        }

    }

    public Notes getNotesByInscriptionMatiere(Inscription inscription, Matiere matiere) {

        try {

            String matiereID = matiere.getId();
            String inscriptionID = inscription.getId();
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.id = :inscriptionID  AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("inscriptionID", inscriptionID);
            query.setParameter("matiereID", matiereID);
            return (Notes) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }

    }
    
    public List<Notes> listeNoteByInscription(Inscription inscription) {
        try {
            String inscriptionId = inscription.getId();
            
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.id = :inscriptionId");
            // set parameters
            query.setParameter("inscriptionId", inscriptionId);
            List<Notes> list = query.getResultList();
            return list;
        } catch (Exception ex) {
            return null;
        }

    }

   
}
