/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class UpdateDesignationEmployeeStage extends JFXDialog{

    JFXTextField designationName;
    JFXComboBox<String> designationType;
    EmployeeDesignation employeeDesignation;
    
    public UpdateDesignationEmployeeStage(String designation_id){
       
        
        StackPane root = new StackPane();
        BorderPane pane = new BorderPane();
        root.getChildren().add(pane);
        
        //-- Screen Decoration -------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Employee Designation");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        pane.setTop(toolBar);
        
        //-- End Screen Decoration ---------------------------------------------
        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("container");
        formGrid.setStyle("-fx-padding:25 5 15 20;");
        formGrid.setVgap(20);
        formGrid.setHgap(2);
        
        
        //---form
            ObservableList<String> item =  FXCollections.observableArrayList();
            item.add("Academic Staff");
            item.add("Support Staff");
            designationName = new JFXTextField();
            designationName.setPromptText("Designation Name");
            designationName.setLabelFloat(true);
            designationName.setPrefWidth(360);
            formGrid.add(designationName, 0, 0);
            
            designationType = new JFXComboBox<String>(item);
            designationType.setPromptText("Designation Type");
            designationType.setLabelFloat(true);
            designationType.setPrefWidth(360);
            formGrid.add(designationType, 0, 1);
            
            formGrid.setVgap(15);
            
            if(designation_id!=null){

                employeeDesignation = EmployeeQuery.getEmployeeDesignationListById(designation_id);
                designationName.setText(employeeDesignation.getDescription());
                designationType.setValue(employeeDesignation.getDesignationType());

            }
        
        
            pane.setCenter(SIMS.setBorderContainer(formGrid, null));
        //------form end
        
           //-- Validate and save the form  -------------------------------------
            JFXButton save = new JFXButton("Save");
            save.getStyleClass().addAll("btn", "btn-primary");
            save.setTooltip(new Tooltip("Save Designation"));
            save.setOnAction((ActionEvent event) -> {
                if( !designationName.getText().equals("") && designationType.getValue()!=""){
                        if(designation_id!=null){

                           employeeDesignation.setDescription(designationName.getText());
                           employeeDesignation.setDesignationType(designationType.getValue());

                           if(EmployeeQuery.updateDesignation(employeeDesignation,true)){ 
                               new DialogUI("Designation has been updated successfully",
                                               DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                               EmployeeDesignationsView.employeeDesignationListWork.restart();
                           }else{
                               new DialogUI("Exception occurred while trying to add activity member.",
                                           DialogUI.ERROR_NOTIF, root, null).show();
                           }
                        }
                        else{
                            EmployeeDesignation empdes = new EmployeeDesignation();
                            empdes.setDescription(designationName.getText());
                            empdes.setDesignationType(designationType.getValue());

                            if(EmployeeQuery.updateDesignation(empdes,false)){ 
                               new DialogUI("Designation has been added successfully",
                                               DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                               EmployeeDesignationsView.employeeDesignationListWork.restart();
                           }else{
                               new DialogUI("Exception occurred while trying to add activity member.",
                                           DialogUI.ERROR_NOTIF, root, null).show();
                           }
                        }
                }
                else{
                    new DialogUI( "Ensure that mandatory field are filled up... ",
                            DialogUI.ERROR_NOTIF,root, null).show();
                }
            });
        
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        pane.setBottom(footer);
        
        setDialogContainer(PARENT_STACK_PANE);
        setContent(root);
        setOverlayClose(false);
        show();
    }

}