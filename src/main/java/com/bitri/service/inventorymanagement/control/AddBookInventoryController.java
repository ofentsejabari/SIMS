/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement.control;

import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author MOILE
 */
public class AddBookInventoryController implements Initializable {
 @FXML
    private JFXComboBox<?> itemDept;

    @FXML
    private VBox background_process;

    @FXML
    private JFXTextField itemYears;

    @FXML
    private VBox personalDetails;

    @FXML
    private JFXTextField itemGov;

    @FXML
    private JFXTextField itemNo;

    @FXML
    private JFXButton btn_toolbar_close;

    @FXML
    private JFXButton btn_update;

    @FXML
    private JFXTextField iname;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //setEditing(false);
        
        
        btn_toolbar_close.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        
        btn_update.getStyleClass().add("dark-blue");
        btn_update.setTooltip(new ToolTip("Save Inventory Entry"));
        
        
        
        btn_update.setOnAction((ActionEvent event) -> {
        });
    }  
    
    
    public void setEditing(boolean val){
        
        
        btn_update.setVisible(val);
        
        iname.setEditable(val);              
        itemNo.setEditable(val);
    }
    public void setEventHandler(EventHandler event){btn_toolbar_close.setOnAction(event);}
}
