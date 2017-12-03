/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.inscription;

/**
 *
 * @author Sedjro
 */
public enum EnumGenre {
    M("Masculin"),
    F("Féminin");
    private final String label;

    private EnumGenre(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
