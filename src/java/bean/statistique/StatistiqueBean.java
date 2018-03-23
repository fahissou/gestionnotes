/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean.statistique;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartType;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
/**
 *
 * @author Sedjro
 */
@Named(value = "statistiqueBean")
@ViewScoped

public class StatistiqueBean implements Serializable{

    /**
     * Creates a new instance of StatistiqueBean
     */
    public StatistiqueBean() {
    }
    
    private GChartModel chartModel = null;  
    private BarChartModel barModel;

     public GChartModel getChart(){  
        return chartModel;  
    }
     public BarChartModel getBarModel() {
        return barModel;
    }

    @PostConstruct  
    public void init() {  
        chartModel = new GChartModelBuilder().setChartType(GChartType.COLUMN)  
                .addColumns("Year", "Salves","Expenses")  
                .addRow("2010",  1000, 400)  
                .addRow("2011",  1200, 800)  
                .addRow("2012",  2000, 1800)  
                .addRow("2013",  1500, 1800)  
                .addRow("2014",  1300, 1000)  
                .build();  
        createBarModels();
    }  
    
    
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
    private void createBarModels() {
        createBarModel();
        
    }
    
    private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Gender");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
}
