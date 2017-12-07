
package jpa.inscription;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Inscription implements Serializable {
    @Id
    private String id;
    private String anneeUniversitaire;
    @ManyToOne
    private GroupePedagogique groupePedagogique;
    @ManyToOne
    private Etudiant etudiant;
    
    @OneToMany(mappedBy = "inscription")
    private List<Notes> notes;
    
    public Inscription() {
        this.etudiant = new Etudiant();
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnneeUniversitaire() {
        return anneeUniversitaire;
    }

    public void setAnneeUniversitaire(String anneeUniversitaire) {
        this.anneeUniversitaire = anneeUniversitaire;
    }
    
    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
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
