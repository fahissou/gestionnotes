
package jpa.inscription;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import jpa.module.Matiere;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Notes implements Serializable {
    
    @Id
    private String id;
    private double note;
    private double oldNote;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEnregistrement;
    @ManyToOne
    private Inscription inscription;
    private String sessions;
    
    @ManyToOne
    private Matiere matiere;
    private String etatValidation;
    
    public Notes() {
        
        this.oldNote = -1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public double getOldNote() {
        return oldNote;
    }

    public void setOldNote(double oldNote) {
        this.oldNote = oldNote;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }

   

    

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public String getEtatValidation() {
        return etatValidation;
    }

    public void setEtatValidation(String etatValidation) {
        this.etatValidation = etatValidation;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notes)) {
            return false;
        }
        Notes other = (Notes) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

   
    
    
    public void reset() {
        id = null;
        note = 0.0;
        oldNote = 0.0;
        dateEnregistrement = null;
    }
    
}
