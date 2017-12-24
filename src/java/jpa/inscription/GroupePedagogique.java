/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.inscription;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import jpa.formation.Filiere;
import jpa.module.Ue;

/**
 *
 * @author Sedjro
 */
@Entity
public class GroupePedagogique implements Serializable {
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Ue> ues;
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Etudiant> etudiants;
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Inscription> inscriptions;
    @Id
    private String id;
    private String description;
    private String CycleEtude;
    private String niveauEtude;
    @ManyToOne
    private Filiere filiere;

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
    
    public String getCycleEtude() {
        return CycleEtude;
    }

    public void setCycleEtude(String CycleEtude) {
        this.CycleEtude = CycleEtude;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
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
        if (!(object instanceof GroupePedagogique)) {
            return false;
        }
        GroupePedagogique other = (GroupePedagogique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "jpa.inscription.GroupePedagogique[ id=" + id + " ]";
//    }
    public void reset(){
        this.id = null;
        this.CycleEtude = null;
        this.niveauEtude = null;
        this.filiere = null;
    }
    
}
