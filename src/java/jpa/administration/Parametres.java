
package jpa.administration;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Parametres implements Serializable {
    @Id
    private String id;
    private double moyenneUE;
    private double proportionAdmission;
    private double moyenneMatiere;

    public Parametres() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMoyenneUE() {
        return moyenneUE;
    }

    public void setMoyenneUE(double moyenneUE) {
        this.moyenneUE = moyenneUE;
    }

    public double getProportionAdmission() {
        return proportionAdmission;
    }

    public void setProportionAdmission(double proportionAdmission) {
        this.proportionAdmission = proportionAdmission;
    }

    public double getMoyenneMatiere() {
        return moyenneMatiere;
    }

    public void setMoyenneMatiere(double moyenneMatiere) {
        this.moyenneMatiere = moyenneMatiere;
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
        if (!(object instanceof Parametres)) {
            return false;
        }
        Parametres other = (Parametres) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    
    public void reset() {
        id = null;
        moyenneUE = 0.0;
        proportionAdmission = 0.0;
    }
    
    
}
