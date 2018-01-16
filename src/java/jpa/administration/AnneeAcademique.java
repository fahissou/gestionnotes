
package jpa.administration;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import jpa.inscription.Inscription;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class AnneeAcademique implements Serializable {
    @Id
    private String id;
    private String description;
    private int etat;
    @OneToMany(mappedBy = "anneeAcademique")
    private List<Inscription> inscriptions;

    public AnneeAcademique() {
        this.etat = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
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
        if (!(object instanceof AnneeAcademique)) {
            return false;
        }
        AnneeAcademique other = (AnneeAcademique) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    public void reset() {
        id = null;
        description = null;
        etat =0;
    }
    
}
