/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.financemanagement;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 *
 * @author tbontsokwane
 */
public class FeeInformationDialog extends JFXDialog {
    
    
     @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public FeeInformationDialog() {
                  
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 15 5 90;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(60);
        
        
        final Label StudentId = new Label("Student Id");
        StudentId.setFont(Font.font("Verdana", 20));
        GridPane.setConstraints(StudentId, 0, 0);
        
        final Label StudentFirstName = new Label("Student FName");
        StudentFirstName.setFont(Font.font("Verdana", 20));
        GridPane.setConstraints(StudentFirstName, 0, 1);
        
        final Label StudentLastName = new Label("Student LName");
        StudentLastName.setFont(Font.font("Verdana", 20));
        GridPane.setConstraints(StudentLastName, 0, 2);
        
        final Label StudentID = new Label();
        StudentID.setFont(Font.font("Verdana", 20));
        GridPane.setConstraints(StudentID, 2, 0);
        StudentID.setText("2010101010");
        
        final Separator sepVert1 = new Separator();
        sepVert1.setOrientation(Orientation.VERTICAL);
        sepVert1.setValignment(VPos.CENTER);
        sepVert1.setPrefHeight(80);
        GridPane.setConstraints(sepVert1, 1, 0);
        GridPane.setRowSpan(sepVert1, 10);
        
        
        contentGrid.getChildren().addAll(StudentId,StudentID,StudentFirstName,StudentLastName,sepVert1);
      
        
        
        
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("FEE DETAILS");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        



        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(FinanceManagement.FIN_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(850, 600);
        show();
        
    }
    
    
}
