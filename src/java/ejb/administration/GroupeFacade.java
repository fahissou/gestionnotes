/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.administration;

import ejb.AbstractFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.administration.Groupe;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class GroupeFacade extends AbstractFacade<Groupe> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;
    protected static final SimpleDateFormat formatCode = new SimpleDateFormat("ddMMyyyyHHmmss");
    private final String code = formatCode.format(new Date());
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeFacade() {
        super(Groupe.class);
    }
    
    @Override
    public void create(Groupe groupe) {
        groupe.setId(groupe.getLibelle()+code);
        super.create(groupe);
    }
    
}
