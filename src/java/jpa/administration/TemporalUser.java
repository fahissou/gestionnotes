
package jpa.administration;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 *
 * @author AHISSOU Florent
 */
@Entity       
public class TemporalUser implements Serializable {
    @Id
    private String id;
    private String userName;
    private String libelleGroupePedagogique;
    private String libelleMatiere;
   

    public TemporalUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLibelleGroupePedagogique() {
        return libelleGroupePedagogique;
    }

    public void setLibelleGroupePedagogique(String libelleGroupePedagogique) {
        this.libelleGroupePedagogique = libelleGroupePedagogique;
    }

    public String getLibelleMatiere() {
        return libelleMatiere;
    }

    public void setLibelleMatiere(String libelleMatiere) {
        this.libelleMatiere = libelleMatiere;
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
        if (!(object instanceof TemporalUser)) {
            return false;
        }
        TemporalUser other = (TemporalUser) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    public void reset(){
        this.id = null;
        this.userName = null;
    }
    
    
}
