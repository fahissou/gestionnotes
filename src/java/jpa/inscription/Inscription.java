
package jpa.inscription;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.administration.AnneeAcademique;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Inscription implements Serializable {
    @Id
    private String id;
    private String resultat;
    private int compteurCredit;
    @ManyToOne
    private GroupePedagogique groupePedagogique;
    @ManyToOne
    private Etudiant etudiant;
    @OneToMany(mappedBy = "inscription")
    private List<Notes> notess;
    @ManyToOne
    private AnneeAcademique anneeAcademique;
    
    public Inscription() {
        this.etudiant = new Etudiant();
        this.resultat = "R";
        this.compteurCredit = 0;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Notes> getNotess() {
        return notess;
    }

    public void setNotess(List<Notes> notess) {
        this.notess = notess;
    }

    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public int getCompteurCredit() {
        return compteurCredit;
    }

    public void setCompteurCredit(int compteurCredit) {
        this.compteurCredit = compteurCredit;
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
        groupePedagogique = null;
        anneeAcademique = null;
        compteurCredit = 0;
        this.resultat = "R";
    }
}
