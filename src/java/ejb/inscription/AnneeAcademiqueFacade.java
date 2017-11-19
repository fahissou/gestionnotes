/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.inscription.AnneeAcademique;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class AnneeAcademiqueFacade extends AbstractFacade<AnneeAcademique> {

    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnneeAcademiqueFacade() {
        super(AnneeAcademique.class);
    }

    public static String generate(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer pass = new StringBuffer();
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * (chars.length() - 1));
            pass.append(chars.charAt(i));
        }
        return pass.toString();
    }
    
    @Override
    public void create(AnneeAcademique anneeAcademique){
        anneeAcademique.setId(generate(10));
        super.create(anneeAcademique);
    }

}
