/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.administration;

import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import jpa.administration.EnumResponsabilite;

/**
 *
 * @author Sedjro
 */
@ViewScoped
@Named(value = "responsabiliteBean")
public class ResponsabiliteBean {

    /**
     * Creates a new instance of ResponsabiliteBean
     */
    public ResponsabiliteBean() {
    }
    public EnumResponsabilite[]getResponsabiliteItems(){
        return EnumResponsabilite.values();
    }
}
