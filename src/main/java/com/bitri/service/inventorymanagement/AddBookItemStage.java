/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement;

import com.jfoenix.controls.JFXDialog;
import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

import com.bitri.service.inventorymanagement.control.AddBookInventoryController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
/**
 *
 * @author MOILE
 */
public class AddBookItemStage  extends JFXDialog{
    
    public AddBookInventoryController DIALOG_CONTROLLER;
    
    public AddBookItemStage(){
        try{
            FXMLLoader ui = new FXMLLoader(getClass().getResource("/views/inventorymanagementViews/addBookInventory.fxml"));
            AnchorPane pane = ui.load();
            
            DIALOG_CONTROLLER = ui.getController();
            DIALOG_CONTROLLER.setEventHandler((EventHandler) (Event event) -> {
                close();
            });
                        
            setDialogContainer(PARENT_STACK_PANE);
            setContent(pane);
            setOverlayClose(false);
            show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

