/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.administration;

/**
 *
 * @author Sedjro
 */
public enum EnumResponsabilite {
    A("Aucun"),
    DE("Directeur Etude"),
    Dr("Directeur");
    private final String label;
    private EnumResponsabilite(String label){
        this.label = label;
    }
    
}
