/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.administration;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Journalisation implements Serializable {
    @Id
    private String id;
    private String descriptionActeurs;
    private String descriptionAction;
    private String dateAction;

    public Journalisation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescriptionActeurs() {
        return descriptionActeurs;
    }

    public void setDescriptionActeurs(String descriptionActeurs) {
        this.descriptionActeurs = descriptionActeurs;
    }

    public String getDescriptionAction() {
        return descriptionAction;
    }

    public void setDescriptionAction(String descriptionAction) {
        this.descriptionAction = descriptionAction;
    }

    public String getDateAction() {
        return dateAction;
    }

    public void setDateAction(String dateAction) {
        this.dateAction = dateAction;
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
        if (!(object instanceof Journalisation)) {
            return false;
        }
        Journalisation other = (Journalisation) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
