/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.inscription;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import jpa.inscription.Etudiant;

/**
 *
 * @author AHISSOU Florent
 */
@FacesConverter("etudiantConverter")
public class EtudiantConverter implements Converter{
    private static Map<String, Etudiant> cache = new HashMap<>();

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
        if (value instanceof Etudiant) {
            Etudiant model = (Etudiant) value;
            id = String.valueOf(model.getLogin());
            if (id != null) {
                cache.put(id, model);
            }
        }
        return id;
    }
}
