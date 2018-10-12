/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement.control;

import com.bitri.access.SIMS;
import com.bitri.service.inventorymanagement.FacilityStatusItem;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author MOILE
 */
public class FacilityStatusController implements Initializable {

    @FXML
    private VBox background_process;
    @FXML
    private BorderPane container;
    @FXML
    private JFXButton btn_toolbar_close;
    @FXML
    private JFXButton btn_cancel;
    @FXML
    private JFXButton btn_update;
    
    @FXML
    private Label facilityN;  

    FacilityStatusItem statusItem = null;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
      
        statusItem = new FacilityStatusItem();
        btn_toolbar_close.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        container.setCenter(statusItem);
    } 
    
    public void setFilter(String filter){
        statusItem.filter = filter;
        statusItem.facilityStatusWork.restart();
    }
    
    public void setEventHandler(EventHandler event){btn_toolbar_close.setOnAction(event);}
}
