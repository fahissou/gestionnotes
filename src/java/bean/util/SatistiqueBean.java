/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.inscription.EtudiantFacade;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import jpa.inscription.EnumGenre;
import statistiques.Data;

/**
 *
 * @author AHISSOU Florent
 */
@Named(value = "satistiqueBean")
@ViewScoped
public class SatistiqueBean implements Serializable {

    @EJB
    private EtudiantFacade etudiantFacade;
    private Data[] datas;
    private String dataJson;

    /**
     * Creates a new instance of SatistiqueBean
     */
    public SatistiqueBean() {
    }

    @PostConstruct
    public void init() {
        datas = new Data[2];
        Data masculin = new Data();
        Data feminin = new Data();
        masculin.setLabel("Garçon");
        int sizeMasculin= etudiantFacade.sizeOfGenre(EnumGenre.M);
        int sizeFeminin= etudiantFacade.sizeOfGenre(EnumGenre.F);
        masculin.setData((sizeMasculin*100)/(sizeMasculin+sizeFeminin));
        feminin.setLabel("Fille");
        feminin.setData((sizeFeminin*100)/(sizeMasculin+sizeFeminin));
        datas[0] = masculin;
        datas[1] = feminin;
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            dataJson = mapper.writeValueAsString(datas);
            System.out.println(dataJson);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        
    }

    

    public Data[] getDatas() {
        return datas;
    }

    public void setDatas(Data[] datas) {
        this.datas = datas;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    
    
}
