
package jpa.inscription;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class AnneeAcademique implements Serializable {
    @Id
    private String id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date anneeAcademique;
//    @ManyToOne
//    private Inscription inscription;
//    @ManyToOne
//    private Enseignant enseignant;
    @OneToMany(mappedBy = "anneeAcademiques")
    private List<Inscription> inscriptions;

    public AnneeAcademique() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(Date anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
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
        anneeAcademique = null;
    }
    
}
