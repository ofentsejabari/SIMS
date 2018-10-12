/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement.control;

import com.bitri.access.SIMS;
import com.bitri.service.inventorymanagement.AddSupplierStage;
import com.bitri.service.inventorymanagement.SupplierInfo;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author MOILE
 */
public class SupplierManagementController implements Initializable {
    
    
    
    @FXML
    private JFXButton btn_add, btn_export ;
    
    @FXML
    private BorderPane borderPane;
    
    BorderPane details ;
    
    public SupplierInfo supplierInfo;
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
      try{
            //-- 
            details = FXMLLoader.load(getClass().getResource("/views/inventorymanagementViews/supplierInformation.fxml"));
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        borderPane.setCenter(details);
        
        
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_add.setOnAction((ActionEvent event) -> {
            new AddSupplierStage().show();
        });
        
        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_export.setOnAction((ActionEvent event) -> {
            
        });
        
        
        
        //supplierDetails.setContent(supplierManagement);
    }    
    
   
}
