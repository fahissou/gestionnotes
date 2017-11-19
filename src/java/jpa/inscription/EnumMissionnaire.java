/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.inscription;

/**
 *
 * @author AHISSOU Florent
 */
public enum EnumMissionnaire {
     E("Missionnaire"),
     L("Locale");
     private final String labelle;
     
     private EnumMissionnaire(String labelle){
         this.labelle = labelle;
     }

    public static EnumMissionnaire getE() {
        return E;
    }

    public static EnumMissionnaire getL() {
        return L;
    }

    public String getLabelle() {
        return labelle;
    }
     
     
     
}
