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
import jpa.administration.Parametres;
import jpa.administration.ProgrammerCours;
import jpa.formation.Filiere;
import jpa.module.Matiere;
import jpa.module.Ue;

/**
 *
 * @author Sedjro
 */
@Entity
public class GroupePedagogique implements Serializable {
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Matiere> matieres;
    @OneToMany(mappedBy = "groupePedagogique")
    private List<ProgrammerCours> programmerCourss;
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Ue> ues;
    @OneToMany(mappedBy = "groupePedagogique")
    private List<Inscription> inscriptions;
    @Id
    private String id;
    private String description;
    private int cycles;
    private int niveau;
    private int ordre;
    @ManyToOne
    private Filiere filiere;
    @ManyToOne
    private Parametres parametres;

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

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
    }
    
    public List<ProgrammerCours> getProgrammerCourss() {
        return programmerCourss;
    }

    public void setProgrammerCourss(List<ProgrammerCours> programmerCourss) {
        this.programmerCourss = programmerCourss;
    }

    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Parametres getParametres() {
        return parametres;
    }

    public void setParametres(Parametres parametres) {
        this.parametres = parametres;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
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

    public void reset(){
        this.id = null;
        this.filiere = null;
        this.cycles = 0;
        this.niveau = 0;
    }
    
}
