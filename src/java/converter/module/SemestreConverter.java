/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter.module;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import jpa.module.Semestre;

/**
 *
 * @author Sedjro
 */
@FacesConverter("semestreConverter")
public class SemestreConverter implements Converter{
    private static Map<String, Semestre> cache = new HashMap<>();

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
        if (value instanceof Semestre) {
            Semestre model = (Semestre) value;
            id = String.valueOf(model.getId());
            if (id != null) {
                cache.put(id, model);
            }
        }
        return id;
    }
    
}
