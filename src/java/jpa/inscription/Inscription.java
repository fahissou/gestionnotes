
package jpa.inscription;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.formation.GroupeEtudiant;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Inscription implements Serializable {
    @Id
    private String id;
    @OneToMany(mappedBy = "inscription")
    private List<Notes> notes;
    @ManyToOne
    private Etudiant etudiant;
    @ManyToOne
    private GroupeEtudiant groupeEtudiant;
    @OneToMany(mappedBy = "inscription")
    private List<AnneeAcademique> anneeAcademiques;

    public Inscription() {
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(object instanceof Inscription)) {
            return false;
        }
        Inscription other = (Inscription) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    public void reset() {
        id = null;  
    }
}
