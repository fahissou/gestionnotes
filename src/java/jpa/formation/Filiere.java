/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.formation;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import jpa.inscription.Enseignant;
import jpa.inscription.Etudiant;
import jpa.inscription.Inscription;

/**
 *
 * @author Sedjro
 */
@Entity
public class Filiere implements Serializable {
    @OneToMany(mappedBy = "filiere")
    private List<Etudiant> etudiants;
    @OneToMany(mappedBy = "filiere")
    private List<GroupeEtudiant> groupeEtudiants;
    @Id
    private String id;
    private String libelle;
    @OneToMany(mappedBy = "filiere")
    private List<Enseignant> enseignants;
    @OneToMany(mappedBy = "filiere")
    private List<Inscription> inscriptions;
    
    
    public Filiere() {
    }
    
    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public List<Enseignant> getEnseignants() {
        return enseignants;
    }

    public void setEnseignants(List<Enseignant> enseignants) {
        this.enseignants = enseignants;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
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

    public List<GroupeEtudiant> getGroupeEtudiants() {
        return groupeEtudiants;
    }

    public void setGroupeEtudiants(List<GroupeEtudiant> groupeEtudiants) {
        this.groupeEtudiants = groupeEtudiants;
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
        if (!(object instanceof Filiere)) {
            return false;
        }
        Filiere other = (Filiere) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public void reset(){
        this.id = null;
        this.etudiants = null;
        this.groupeEtudiants = null;
        this.libelle = null;
    }
}
