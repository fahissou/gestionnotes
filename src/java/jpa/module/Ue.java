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
import jpa.formation.GroupeEtudiant;

/**
 *
 * @author Sedjro
 */
@Entity
public class Ue implements Serializable {
    @OneToMany(mappedBy = "ue")
    private List<Matiere> matieres;
    @Id
    private String id;
    private String libelle;
    private String credit;
    @ManyToOne
    private Semestre semestre;
    @ManyToOne
    private GroupeEtudiant goupeEtudiant;

    public Ue() {
    }

    
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public GroupeEtudiant getGoupeEtudiant() {
        return goupeEtudiant;
    }

    public void setGoupeEtudiant(GroupeEtudiant goupeEtudiant) {
        this.goupeEtudiant = goupeEtudiant;
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
        if (!(object instanceof Ue)) {
            return false;
        }
        Ue other = (Ue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

public void reset(){
    this.id = null;
    this.credit = null;
    this.goupeEtudiant = null;
    this.libelle = null;
    this.matieres = null;
    this.semestre = null;
}
    
}
