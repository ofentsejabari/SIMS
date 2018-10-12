/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement.control;

import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author MOILE
 */
public class SuccessController implements Initializable {

    @FXML
    private JFXButton close,successImg;
    
    @FXML
    private Pane stack;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        successImg.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "icon-default", 19));
    }    
    public void setEventHandler(EventHandler event){
        close.setOnAction(event);
            
    }
    
}
