package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.inscription.AnneeAcademique;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Inscription;
import jpa.inscription.Notes;
import jpa.module.Matiere;
import jpa.module.Semestre;
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

    public List<Notes> listeNoteGpAnnee(AnneeAcademique anneeAcademique, GroupePedagogique groupePedagogique, Matiere matiere) {
        List<Notes> notes = null;
        String matiereID = matiere.getId();
        String idAnneAca = anneeAcademique.getId();
        String idGP = groupePedagogique.getId();
        try {
            
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.anneeAcademique.id = :idAnneAca AND N.inscription.groupePedagogique.id = :idGP AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("idAnneAca", idAnneAca);
            query.setParameter("idGP", idGP);
            query.setParameter("matiereID", matiereID);
            notes = query.getResultList();
        } catch (Exception ex) {
            notes = new ArrayList<>();
        }
        return notes;
    }

     public List<Notes> listeNoteGpAnnee1(AnneeAcademique anneeAcademique, GroupePedagogique groupePedagogique, Matiere matiere) {
        List<Notes> notes = null;
        String matiereID = matiere.getId();
        String idAnneAca = anneeAcademique.getId();
        String idGP = groupePedagogique.getId();
        try {
            
            Query query = em.createQuery("SELECT N FROM Notes N WHERE (N.inscription.anneeAcademique.id = :idAnneAca OR N.inscription.sessions.id =:idAnneAca) AND N.inscription.groupePedagogique.id = :idGP AND N.matiere.id = :matiereID");
            // set parameters
            query.setParameter("idAnneAca", idAnneAca);
            query.setParameter("idGP", idGP);
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

    public List<Notes> listeNotesNonValide(AnneeAcademique anneeAcademique ,GroupePedagogique  groupePedagogique, Matiere matiere) {
        try {
            String matiereID = matiere.getId();
            String idGroupePedagogique = groupePedagogique.getId();
            String idAnneAca = anneeAcademique.getId();
            String uenv = "UENV";
            String uev = "UEV";
            double valNull = 0.0;
            double moy = groupePedagogique.getParametres().getMoyenneMatiere();
            Query query = em.createQuery("SELECT N FROM Notes N WHERE (N.inscription.anneeAcademique.id = :idAnneAca OR N.inscription.sessions.id =:idAnneAca) AND ((N.etatValidation = :uenv AND N.note < :moy) OR (N.etatValidation = :uev AND N.note = :valNull)) AND (N.inscription.groupePedagogique.id = :idGroupePedagogique) AND (N.matiere.id = :matiereID)");
            // set parameters
            query.setParameter("idAnneAca", idAnneAca);
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
    
    public List<Notes> listeNoteByInscriptionBySem(Inscription inscription, Semestre semestre) {
        try {
            String inscriptionId = inscription.getId();
            String semest = semestre.getId();
            Query query = em.createQuery("SELECT N FROM Notes N WHERE N.inscription.id = :inscriptionId AND N.matiere.ue.semestre.id = :semest");
            // set parameters
            query.setParameter("inscriptionId", inscriptionId);
            query.setParameter("semest", semest);
            List<Notes> list = query.getResultList();
            return list;
        } catch (Exception ex) {
            return null;
        }

    }
   
}
