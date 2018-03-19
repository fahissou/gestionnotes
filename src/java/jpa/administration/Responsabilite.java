
package jpa.administration;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.formation.Filiere;
import jpa.inscription.Enseignant;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Responsabilite implements Serializable {
    @Id
    private String id;
    @ManyToOne
    private Enseignant enseignant;
    private String role;
    @ManyToOne
    private Filiere filiere;
    @OneToMany(mappedBy = "responsabilite")
    private List<ProgrammerCours> programmerCourss1;
    
    public Responsabilite() {
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
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
        if (!(object instanceof Responsabilite)) {
            return false;
        }
        Responsabilite other = (Responsabilite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.id = null;
        this.enseignant = null;
        this.filiere = null;
        this.role = null;
    }
    
}
