/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.inscription;

import ejb.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.formation.Filiere;
import jpa.inscription.GroupePedagogique;
import jpa.inscription.Notes;
import jpa.inscription.Specialite;
import util.JsfUtil;

/**
 *
 * @author AHISSOU Florent
 */
@Stateless
public class SpecialiteFacade extends AbstractFacade<Specialite> {
    @PersistenceContext(unitName = "gestionnotesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SpecialiteFacade() {
        super(Specialite.class);
    }
    
    @Override
    public void create(Specialite specialite) {
        specialite.setId(specialite.getLibelle().toUpperCase());
        super.create(specialite);
    }
    
    
}
