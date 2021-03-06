package jpa.inscription;

import jpa.administration.Responsabilite;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import jpa.administration.ProgrammerCours;
import jpa.formation.Filiere;

/**
 *
 * @author AHISSOU Florent
 */
@Entity
public class Enseignant implements Serializable {
    
    @Id
    private String login;
    private String nom;
    private String prenom;
    private String mail;
    private String password;
    private String oldPassword;
    private String telephone;
    @Enumerated(EnumType.STRING)
    private EnumGenre genre;
    @Enumerated(EnumType.STRING)
    private EnumMissionnaire statut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String adresse;
    private String grade;

    @ManyToOne
    private Filiere filiere;
    @OneToMany(mappedBy = "enseignant")
    private List<ProgrammerCours> programmerCourss;
    @OneToMany(mappedBy = "enseignant")
    private List<Enseigner> enseigners;
    @ManyToOne
    private Specialite specialite;
    
    @OneToMany(mappedBy = "enseignant")
    private List<Responsabilite> responsabilites;
    

    public Enseignant() {
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

    public EnumGenre getGenre() {
        return genre;
    }

    public void setGenre(EnumGenre genre) {
        this.genre = genre;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public EnumMissionnaire getStatut() {
        return statut;
    }

    public void setStatut(EnumMissionnaire statut) {
        this.statut = statut;
    }
    
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<ProgrammerCours> getProgrammerCourss() {
        return programmerCourss;
    }

    public void setProgrammerCourss(List<ProgrammerCours> programmerCourss) {
        this.programmerCourss = programmerCourss;
    }

    public List<Enseigner> getEnseigners() {
        return enseigners;
    }

    public void setEnseigners(List<Enseigner> enseigners) {
        this.enseigners = enseigners;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }
    
    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Responsabilite> getResponsabilites() {
        return responsabilites;
    }

    public void setResponsabilites(List<Responsabilite> responsabilites) {
        this.responsabilites = responsabilites;
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
        if (!(object instanceof Enseignant)) {
            return false;
        }
        Enseignant other = (Enseignant) object;
        return !((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login)));
    }

    public void reset() {
        login = null;
        nom = null;
        prenom = null;
        telephone = null;
        adresse = null;
        mail = null;
        password = null;
        genre = null;
        specialite = null;
    }
    
    
}
