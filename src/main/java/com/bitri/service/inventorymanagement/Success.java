
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement;

import com.jfoenix.controls.JFXDialog;
import com.bitri.access.SIMS;
import com.bitri.service.inventorymanagement.control.FailedController;
import com.bitri.service.inventorymanagement.control.SuccessController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


public class Success extends JFXDialog{
    
    public SuccessController DIALOG_CONTROLLER;
    public FailedController DIALOG_CONTROLLER1;
    
    public Success(String status,boolean value){
        
        StackPane stack= SIMS.MAIN_UI;
        try{
            FXMLLoader ui;
            AnchorPane pane;
            if(status.equals("success")){
                 ui= new FXMLLoader(getClass().getResource("/views/inventorymanagementViews/success.fxml"));
                 
                pane = ui.load();
                DIALOG_CONTROLLER = ui.getController();
            
                DIALOG_CONTROLLER.setEventHandler((EventHandler) (Event event) -> {
                    close();
                });
                        
            
            }
            else{
                ui= new FXMLLoader(getClass().getResource("/views/inventorymanagementViews/failed.fxml"));
                pane = ui.load();
                DIALOG_CONTROLLER1 = ui.getController();
            
                DIALOG_CONTROLLER1.setEventHandler((EventHandler) (Event event) -> {
                    close();
                });
            }
            if(value==true)
                stack= InventoryManagement.INVENTORY_MAN_STACK;
              
            setDialogContainer(stack);
            setContent(pane);
            setAlignment(Pos.CENTER);
            setOverlayClose(false);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

