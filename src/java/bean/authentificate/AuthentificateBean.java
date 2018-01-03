/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.authentificate;

import ejb.administration.UtilisateurFacade;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jpa.administration.Utilisateur;
import org.primefaces.context.RequestContext;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ManagedBean
@SessionScoped
public class AuthentificateBean implements Serializable{

    @EJB
    private UtilisateurFacade utilisateurFacade;
    private Utilisateur currentUser;
    private String login, mdp;

    /**
     * Creates a new instance of AuthentificateBean
     */
    public AuthentificateBean() {
    }

    @PostConstruct
    public void init() {
        prepareCreate();
    }

    public void connexion() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(login, mdp);
            RequestContext.getCurrentInstance().execute("window.location='" + request.getContextPath() + "'");

        } catch (ServletException e) {
            JsfUtil.addErrorMessage(JsfUtil.getBundleMsg("LoginFailMsg"));
        }
    }

    public String deconnexion() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        return "/index?faces-redirect=true";
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Utilisateur currentUser) {
        this.currentUser = currentUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String findUserSession() {
        FacesContext context
                = FacesContext.getCurrentInstance();
        HttpServletRequest request
                = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getRemoteUser();
    }

    public String typeUtilisateur() {
        currentUser = utilisateurFacade.find(findUserSession());
        return currentUser.getGroupe().getLibelle();
    }

    public Utilisateur recupUtilisateur() {
        return utilisateurFacade.find(findUserSession());
    }

    public void prepareCreate() {
        currentUser = new Utilisateur();

    }
    

}
