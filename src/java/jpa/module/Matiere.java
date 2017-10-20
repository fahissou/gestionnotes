/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.module;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.administration.ProgrammerCours;
import jpa.inscription.Enseignant;
import jpa.inscription.Notes;

/**
 *
 * @author Sedjro
 */
@Entity
public class Matiere implements Serializable {
    @OneToMany(mappedBy = "matiere")
    private List<ProgrammerCours> programmerCours;
    @OneToMany(mappedBy = "matiere")
    private List<Notes> notes;
    @Id
    private String id;
    private String libelle;
    private Double coefficiant;
    @ManyToOne
    private Ue ue;
    @ManyToOne
    private Enseignant enseignant;
    
    
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

    public Double getCoefficiant() {
        return coefficiant;
    }

    public void setCoefficiant(Double coefficiant) {
        this.coefficiant = coefficiant;
    }

    public Ue getUe() {
        return ue;
    }

    public void setUe(Ue ue) {
        this.ue = ue;
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
        if (!(object instanceof Matiere)) {
            return false;
        }
        Matiere other = (Matiere) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "jpa.module.Matiere[ id=" + id + " ]";
//    }
    
}
