
package jpa.inscription;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

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
    private Date date;

    public Notes() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        date = null;
    }
    
}
