package converter.administration;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import jpa.administration.Parametres;

/**
 *
 * @author AHISSOU Florent
 */
@FacesConverter("parametresConverter")
public class ParametresConverter implements Converter {
    
    private static Map<String, Parametres> cache = new HashMap<>();
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
        if (value instanceof Parametres) {
            Parametres model = (Parametres) value;
            id = String.valueOf(model.getId());
            if (id != null) {
                cache.put(id, model);
            }
        }
        return id;
    }
    
    
}
