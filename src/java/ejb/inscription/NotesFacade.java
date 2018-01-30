package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.GroupePedagogique;
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

    public List<Notes> listeNoteGpAnnee(String inscriptionID, String groupePedagogique, Matiere matiere) {
        List<Notes> notes = null;
        try {
            String matiereID = matiere.getId();
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.anneeAcademique.description = :inscriptionID AND N.inscription.groupePedagogique.description = :groupePedagogique AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("inscriptionID", inscriptionID);
            query.setParameter("groupePedagogique", groupePedagogique);
            query.setParameter("matiereID", matiereID);
            notes = query.getResultList();
        } catch (Exception ex) {
            notes = new ArrayList<>();
        }
        return notes;
    }

    public Notes getNotesByInscriptionMatiere(Inscription inscription, Matiere matiere) {
            Notes notes = null;
        try {

            String matiereID = matiere.getId();
            String inscriptionID = inscription.getId();
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.id = :inscriptionID  AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("inscriptionID", inscriptionID);
            query.setParameter("matiereID", matiereID);
            notes = (Notes) query.getSingleResult();
        } catch (Exception ex) {
            notes = new Notes();
        }
        return notes;
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

    public List<Notes> listeNotesNonValide(GroupePedagogique  groupePedagogique, Matiere matiere) {
        try {
            String matiereID = matiere.getId();
            String idGroupePedagogique = groupePedagogique.getId();
            String uenv = "UENV";
            String uev = "UEV";
            double valNull = 0.0;
            double moy = 12.0;
            Query query = em.createQuery("SELECT N FROM Notes N WHERE  ((N.etatValidation = :uenv AND N.note < :moy) OR (N.etatValidation = :uev AND N.note = :valNull)) AND (N.inscription.groupePedagogique.id = :idGroupePedagogique) AND (N.matiere.id = :matiereID)");
            // set parameters
            query.setParameter("idGroupePedagogique", idGroupePedagogique);
            query.setParameter("matiereID", matiereID);
            query.setParameter("uenv", uenv);
            query.setParameter("uev", uev);
            query.setParameter("valNull", valNull);
            query.setParameter("moy", moy);
            
            List<Notes> list = query.getResultList();
            return list;
        } catch (Exception ex) {
            return null;
        }

    }
   
}
