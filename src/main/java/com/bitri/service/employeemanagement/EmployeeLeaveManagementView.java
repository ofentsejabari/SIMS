/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author MOILE
 */
public class EmployeeLeaveManagementView extends BorderPane{

    JFXTabPane leavePane;
    
    public EmployeeLeaveManagementView() {
    
        setPadding(new Insets(10));
        getStyleClass().add("container");
        
        leavePane = new JFXTabPane();
        Tab pending = new Tab("Pending Leaves");
        leavePane.getStyleClass().add("jfx-tab-flatpane");
        leavePane.getTabs().add(pending);
        
        pending.setContent(new LeaveManagementView());
        setCenter(leavePane);
    
    }

    
    
}
