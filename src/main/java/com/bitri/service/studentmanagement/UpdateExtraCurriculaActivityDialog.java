package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.studentmanagement.StudentManagement.extraCurriculaController;

/**
 *
 * @author jabari
 */
public class UpdateExtraCurriculaActivityDialog extends JFXDialog{

    private JFXTextField name;
    private JFXComboBox<String> coach, type;
    
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateExtraCurriculaActivityDialog(ExtraCurriculaActivity activity) {
                    
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 5 15 20;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(2);
        
        name = new JFXTextField();
        name.setPromptText("Activity Name");
        name.setPrefWidth(360);
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        
        CCValidator.setFieldValidator(name, "Name required.");
                
        coach = new JFXComboBox<>(EmployeeQuery.getEmployeeNameList());
        coach.setPromptText("Activity Coach");
        coach.setLabelFloat(true);
        coach.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(coach);
        new AutoCompleteComboBoxListener(coach);
        contentGrid.add(coach, 0, 1);
        if(!coach.getItems().isEmpty()){
            coach.setValue(coach.getItems().get(0));
        }
        
        type = new JFXComboBox<>(FXCollections.observableArrayList("Sport", "Club"));
        type.setPromptText("Activity Type");
        type.setLabelFloat(true);
        type.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(type);
        new AutoCompleteComboBoxListener(type);
        contentGrid.add(type, 0, 2);
        type.setValue(type.getItems().get(0));
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Extra Curricula Activity");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(activity != null){
            
            name.setText(activity.getName());
            coach.setValue(EmployeeQuery.getEmployeeByID(activity.getCoach()).getFullName());
            type.setValue(activity.getType());
            title.setText("Update Extra Curricula Activity");
                        
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Activity"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!"".equals(name.getText().trim())){
                
               if(activity != null){
                    activity.setType(type.getValue());
                    activity.setName(name.getText().trim());
                    activity.setCoach((coach.getValue() == null)? "":
                            EmployeeQuery.getEmployeeByName(coach.getValue()).getEmployeeID());
                                        
                    if(AdminQuery.updateActivity(activity, true)){
                        
                        new DialogUI("Activity details has been updated successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                        extraCurriculaController.ecws.restart();
                        close();
                    }else{
                        new DialogUI("Exception occurred while trying to update activity details",
                        DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                    
                }else{
                    ExtraCurriculaActivity ecactivity = new ExtraCurriculaActivity("0", name.getText().trim(), 
                            (coach.getValue() == null)? "":EmployeeQuery.getEmployeeByName(coach.getValue()).getEmployeeID(),
                            type.getValue());
                    
                    if(AdminQuery.updateActivity(ecactivity, false)){
                        
                        new DialogUI("Activity details has been added successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                        
                        extraCurriculaController.ecws.restart();
                        close();
                       
                    }else{
                        new DialogUI("Exception occurred while trying to add activity details.",
                        DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                }
            }else{
                name.validate();
                new DialogUI( "Ensure that mandatory field are filled up... ",
                        DialogUI.ERROR_NOTIF, stackPane, null).show();
            }
            
        });
        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(StudentManagement.STUDENT_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 180);
        show();
        
    }
}
