/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author MOILE
 * 
 **/

public class EmployeeDesignationStage extends BorderPane{
    
    public EmployeeDesignationStage(){
        
        StackPane root = new StackPane();
        BorderPane pane = new BorderPane();
        
        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(pane);
        
        EmployeeDesignationsView employeDesignation= new EmployeeDesignationsView();
        
      
        pane.setCenter(employeDesignation);
        setCenter(pane);
        
    }
    
}
