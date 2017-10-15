
package jpa.inscription;

/**
 *
 * @author AHISSOU Florent
 */
public enum EnumGenre {
    
    M("Masculin"),
    F("Féminin");
    private final String label;
    
    private EnumGenre(String label) {
        this.label = label;
    }

    public static EnumGenre getM() {
        return M;
    }

    public static EnumGenre getF() {
        return F;
    }

    public String getLabel() {
        return label;
    }
    
}
