/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author MOILE
 */
public class AddEmployeeLeaveApplication extends JFXDialog{

    
    public  EmployeeModel employee = null;
   
    StackPane root;
    
    public AddEmployeeLeaveApplication(/*AddEmployeeLeaveApplication(EmployeeModel emp*/){
        //this.employee = emp;
        //
        root = new StackPane();
        BorderPane pane = new BorderPane();
        root.getChildren().add(pane);
        
        //-- Screen Decoration -------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Leave application");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        pane.setTop(toolBar);
        
        //-- End Screen Decoration ---------------------------------------------

       
        
        //-- End Profile Picture -----------------------------------------------
        GridPane contentGrid = new GridPane();
        contentGrid.setStyle("-fx-padding:10;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(10);
        
        JFXComboBox leaveType = new JFXComboBox();
        
        
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new Tooltip("Save Designation"));
        save.setOnAction((ActionEvent event) -> {
            
        });
        
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        pane.setBottom(footer);
        
        setDialogContainer(PARENT_STACK_PANE);
        setContent(root);
        root.setPrefSize(650,350); 
        setOverlayClose(false);
        show();
        
    }
}
