
package jpa.inscription;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.formation.Filiere;
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
//    @OneToMany(mappedBy = "inscription")
//    private List<AnneeAcademique> anneeAcademiques;
    @ManyToOne
    private AnneeAcademique anneeAcademique;
    @ManyToOne
    private Filiere filiere;
    private String cycleFormation;
    private String niveau;
    

    
    public Inscription() {
    }

    public String getCycleFormation() {
        return cycleFormation;
    }

    public void setCycleFormation(String cycleFormation) {
        this.cycleFormation = cycleFormation;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    
    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public GroupeEtudiant getGroupeEtudiant() {
        return groupeEtudiant;
    }

    public void setGroupeEtudiant(GroupeEtudiant groupeEtudiant) {
        this.groupeEtudiant = groupeEtudiant;
    }
    
    public AnneeAcademique getAnneeAcademique() {
        return anneeAcademique;
    }

//    public List<AnneeAcademique> getAnneeAcademiques() {
//        return anneeAcademiques;
//    }
//
//    public void setAnneeAcademiques(List<AnneeAcademique> anneeAcademiques) {
//        this.anneeAcademiques = anneeAcademiques;
//    }
    public void setAnneeAcademique(AnneeAcademique anneeAcademique) {    
        this.anneeAcademique = anneeAcademique;
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
