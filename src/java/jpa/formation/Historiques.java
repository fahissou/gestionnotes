
package jpa.formation;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Historiques implements Serializable{
    @Id
    private String id;
    private String libelle;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String groupePedagogique;
    private String lienFile;

    public Historiques() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(String groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public String getLienFile() {
        return lienFile;
    }

    public void setLienFile(String lienFile) {
        this.lienFile = lienFile;
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
        if (!(object instanceof Historiques)) {
            return false;
        }
        Historiques other = (Historiques) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
}
