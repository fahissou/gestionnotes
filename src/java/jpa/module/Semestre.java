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
import jpa.inscription.GroupePedagogique;

/**
 *
 * @author Sedjro
 */
@Entity
public class Semestre implements Serializable {
    @Id
    private String id;
    private int libelle;
    @ManyToOne
    private GroupePedagogique groupePedagogique;
    @OneToMany(mappedBy = "semestre")
    private List<Ue> ues;
    
    
    public Semestre() {
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLibelle() {
        return libelle;
    }

    public void setLibelle(int libelle) {
        this.libelle = libelle;
    }

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
    }

  

    public GroupePedagogique getGroupePedagogique() {
        return groupePedagogique;
    }

    public void setGroupePedagogique(GroupePedagogique groupePedagogique) {
        this.groupePedagogique = groupePedagogique;
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
        if (!(object instanceof Semestre)) {
            return false;
        }
        Semestre other = (Semestre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

public void reset(){
    this.id = null;
    this.libelle = 0;

}    
}
