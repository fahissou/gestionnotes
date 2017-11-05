/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.formation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import jpa.administration.ProgrammerCours;
import jpa.inscription.Inscription;
import jpa.module.Ue;

/**
 *
 * @author Sedjro
 */
@Entity
public class GroupeEtudiant implements Serializable {
    @OneToMany(mappedBy = "goupeEtudiant")
    private List<Ue> ues;
    @OneToMany(mappedBy = "groupeEtudiant")
    private List<ProgrammerCours> programmerCours;
    @OneToMany(mappedBy = "groupeEtudiant")
    private List<Inscription> inscriptions;
    @Id
    private String id;
    private String libelle;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;
    @ManyToOne
    private Filiere filiere;

    public GroupeEtudiant() {
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
    }

    public List<ProgrammerCours> getProgrammerCours() {
        return programmerCours;
    }

    public void setProgrammerCours(List<ProgrammerCours> programmerCours) {
        this.programmerCours = programmerCours;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
        if (!(object instanceof GroupeEtudiant)) {
            return false;
        }
        GroupeEtudiant other = (GroupeEtudiant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

public void reset(){
    this.id = null;
    this.filiere = null;
    this.dateCreation = null;
    this.inscriptions = null;
    this.programmerCours = null;
    this.ues = null;
}    
}
