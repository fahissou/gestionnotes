/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.administration;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author easy
 */
@FacesValidator(value = "confirmationMotDePasseValidator")
public class ConfirmationMotDePasseValidator implements Validator {

    private static final String CHAMP_MOT_DE_PASSE = "composantMotDePasse";
    private static final String MOTS_DE_PASSE_DIFFERENTS = "Confirmation non identique au mot de passe.";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIInput composantMotDePasse = (UIInput) component.getAttributes().get(CHAMP_MOT_DE_PASSE);
        String motDePasse = (String) composantMotDePasse.getValue();
        String confirmation = (String) value;
        if (confirmation != null && !confirmation.equals(motDePasse)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MOTS_DE_PASSE_DIFFERENTS, null));
        }
    }

}
