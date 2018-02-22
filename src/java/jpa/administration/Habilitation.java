/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.administration;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Sedjro
 */
@Entity
public class Habilitation implements Serializable {
    @Id
    private String id;
    @ManyToOne
    private Utilisateur utilisateur;
    @ManyToOne
    private Groupe groupeUtilisateur;
    @Version
    private Timestamp version;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Groupe getGroupeUtilisateur() {
        return groupeUtilisateur;
    }

    public void setGroupeUtilisateur(Groupe groupeUtilisateur) {
        this.groupeUtilisateur = groupeUtilisateur;
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
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
        if (!(object instanceof Habilitation)) {
            return false;
        }
        Habilitation other = (Habilitation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "jpa.administration.Habilitation[ id=" + id + " ]";
//    }
    public void reset(){
        this.id = null;
        this.utilisateur = null;
        this.groupeUtilisateur = null;
    }
    
}
