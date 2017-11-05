/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.formation;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import jpa.formation.Filiere;
import jpa.formation.GroupeEtudiant;

/**
 *
 * @author Sedjro
 */
@FacesConverter("filiereConverter")
public class FiliereConverter implements Converter{
    private static Map<String, Filiere> cache = new HashMap<>();
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         if (value == null || value.isEmpty()) {
            return null;
        }
        return cache.get(value.trim());
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        String id = null;
        if (value instanceof Filiere) {
            Filiere model = (Filiere) value;
            id = String.valueOf(model.getId());
            if (id != null) {
                cache.put(id, model);
            }
        }
        return id;
    }
    
}
