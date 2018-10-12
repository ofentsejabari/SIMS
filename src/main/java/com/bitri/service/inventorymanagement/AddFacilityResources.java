/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement;

import com.jfoenix.controls.JFXDialog;
import com.bitri.access.SIMS;
import com.bitri.service.inventorymanagement.control.AddFacilityResourcesController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
/**
 *
 * @author MOILE
 */
public class AddFacilityResources  extends JFXDialog{
    
    public AddFacilityResourcesController DIALOG_CONTROLLER;
    public String identifier;
    
    
    public AddFacilityResources(String identifier){
        this.identifier=identifier;
        try{
            FXMLLoader ui = new FXMLLoader(getClass().getResource("/views/inventorymanagementViews/addFacilityResources.fxml"));
            AnchorPane pane = ui.load();
            
            DIALOG_CONTROLLER = ui.getController();
            DIALOG_CONTROLLER.setDialog(this);
            DIALOG_CONTROLLER.setId(identifier);
            DIALOG_CONTROLLER.setEventHandler((EventHandler) (Event event) -> {
                close();
            });
                        
            setDialogContainer(SIMS.MAIN_UI);
            setContent(pane);
            setOverlayClose(false);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}

