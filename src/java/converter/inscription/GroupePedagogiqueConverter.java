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
import jpa.inscription.GroupePedagogique;

/**
 *
 * @author Sedjro
 */
@FacesConverter("groupePedagogiqueConverter")
public class GroupePedagogiqueConverter implements Converter{
    private static Map<String, GroupePedagogique> cache = new HashMap<>();
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
        if (value instanceof GroupePedagogique) {
            GroupePedagogique model = (GroupePedagogique) value;
            id = String.valueOf(model.getId());
            if (id != null) {
                cache.put(id, model);
            }
        }
        return id;
    }
    
}
