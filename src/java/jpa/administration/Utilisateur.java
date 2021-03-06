/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.administration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Utilisateur implements Serializable {
    @Id
    private String login;
    private String nom;
    private String prenom;
    private String mail;
    private String password;
    private String oldPassword;
    private String telephone;
    private boolean passwordinit = false;
    @Enumerated(EnumType.STRING)
    private EnumResponsabilite responsabilite;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    private String compteurMessage;
    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> messages;
    @OneToMany(mappedBy = "utilisateur")
    private List<Habilitation> habilitations;
    
    public Utilisateur() {
    }

    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isPasswordinit() {
        return passwordinit;
    }

    public void setPasswordinit(boolean passwordinit) {
        this.passwordinit = passwordinit;
    }

    public EnumResponsabilite getResponsabilite() {
        return responsabilite;
    }

    public void setResponsabilite(EnumResponsabilite responsabilite) {
        this.responsabilite = responsabilite;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Habilitation> getHabilitations() {
        return habilitations;
    }

    public void setHabilitations(List<Habilitation> habilitations) {
        this.habilitations = habilitations;
    }
    
    public String getCompteurMessage() {
        return compteurMessage;
    }

    public void setCompteurMessage(String compteurMessage) {
        this.compteurMessage = compteurMessage;
    }

    public List<Notification> getMessages() {
        return messages;
    }

    public void setMessages(List<Notification> messages) {
        this.messages = messages;
    }
    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        return !((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login)));
    }
    
    public void reset() {
        login = null;
        nom = null;
        prenom = null;
        telephone=null;
        mail=null;
        password=null;
    }
    
    
}
