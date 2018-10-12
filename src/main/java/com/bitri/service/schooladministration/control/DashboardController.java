package com.bitri.service.schooladministration.control;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author ofentse
 */
public class DashboardController implements Initializable {

    @FXML
    private BorderPane dashboard;
    @FXML
    private VBox students;
         
    private Gauge    activeCalories;  
    private Gauge    weight;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM);
        
        weight         = builder.decimals(1).maxValue(100).unit("KG").skinType(Gauge.SkinType.SLIM).build();
        activeCalories = builder.decimals(0).maxValue(2200).unit("KCAL").skinType(Gauge.SkinType.FLAT).build(); 
        
        VBox weightBox       = getTopicBox("WEIGHT", Color.rgb(149,117,205), weight);   
        VBox actvCaloriesBox = getTopicBox("ACTIVE CALORIES", Color.rgb(229,115,115), activeCalories);  
           
        activeCalories.setValue(347);   
        weight.setValue(78.7);
        students.getChildren().addAll(weightBox, actvCaloriesBox);
        
    }  
    
    
    
    private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE){ 
        
        Rectangle bar = new Rectangle(200, 3);  
        bar.setArcWidth(6);  
        bar.setArcHeight(6);  
        bar.setFill(COLOR);  
  
        Label label = new Label(TEXT);  
        label.setTextFill(COLOR);  
        label.setAlignment(Pos.CENTER);  
        label.setPadding(new Insets(0, 0, 10, 0));  
  
        GAUGE.setBarColor(COLOR);  
        GAUGE.setBarBackgroundColor(Color.web("#eceff1"));//39,44,50));  
        GAUGE.setAnimated(true);  
  
        VBox vBox = new VBox(bar, label, GAUGE);  
        vBox.setSpacing(3);  
        vBox.setAlignment(Pos.CENTER); 
        
        return vBox;  
     }  
    
}
