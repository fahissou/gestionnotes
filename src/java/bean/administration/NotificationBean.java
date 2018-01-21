/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import ejb.administration.NotificationFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import jpa.administration.Notification;
import util.JsfUtil;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "notificationBean")
public class NotificationBean {
    @EJB
    private NotificationFacade notificationFacade;
    private Notification newNotification;
    private List<Notification> listeNotifications;
    private List<Notification> filteredList;
    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
    }
    @PostConstruct
    public void init(){
        listeNotifications = notificationFacade.findAll();
        prepareCreate();
        
    }
    
    public void doCreate(ActionEvent event){
        String msg;
        try {
            notificationFacade.create(newNotification);
            msg = JsfUtil.getBundleMsg("");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = JsfUtil.getBundleMsg("");
            JsfUtil.addErrorMessage(msg);
        }
    }
    public void prepareCreate(){
        this.newNotification = new Notification();
    }

    public Notification getNewNotification() {
        return newNotification;
    }

    public void setNewNotification(Notification newNotification) {
        this.newNotification = newNotification;
    }

    public List<Notification> getListeNotifications() {
        return listeNotifications;
    }

    public void setListeNotifications(List<Notification> listeNotifications) {
        this.listeNotifications = listeNotifications;
    }

    public List<Notification> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Notification> filteredList) {
        this.filteredList = filteredList;
    }
    
}
