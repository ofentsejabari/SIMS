package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
public class AddMemberDialog extends JFXDialog{

    private JFXComboBox<String> member;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public AddMemberDialog(String activityID) {
                    
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
        
        HBox con = new HBox(10);
        con.setAlignment(Pos.CENTER_LEFT);
        ToggleGroup tg = new ToggleGroup();
        
        JFXRadioButton teacher = new JFXRadioButton("Teacher/ Staff");
        RadioButton student = new JFXRadioButton("Student");
        
        teacher.setToggleGroup(tg);
        student.setToggleGroup(tg);
        tg.selectToggle(student);
        
        con.getChildren().addAll(student, teacher);
        contentGrid.add(con, 0, 0);
            
        member = new JFXComboBox<>(SIMS.dbHandler.getStudentsNameList());
        member.setPromptText("Student Name");
        member.setLabelFloat(true);
        member.setPrefWidth(360);
        new AutoCompleteComboBoxListener(member);
        contentGrid.add(member, 0, 1);   
        
        teacher.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                member.setPromptText("Teacher/ Staff Name");
                member.setItems(EmployeeQuery.getEmployeeNameList());
            }else{
                member.setPromptText("Student Name"); 
                member.setItems(SIMS.dbHandler.getStudentsNameList());
            }
        });
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Members");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save changes"));
        save.setOnAction((ActionEvent event) -> {
            
            if(member.getValue() != null && !"".equals(member.getValue())){
                
                String memberID = "", type = "";
                if(student.isSelected()){
                    memberID = SIMS.dbHandler.getStudentByName(member.getValue()).getStudentID();
                    type = "Student";
                }else{
                    memberID = EmployeeQuery.getEmployeeByName(member.getValue()).getEmployeeID();
                    type = "Staff";
                }
                
                ActivityMember ac = new ActivityMember("0", memberID, activityID, type);
                
                if(!AdminQuery.isActivityMemberExist(ac)){
                    
                    if(AdminQuery.addActivityMember(ac)){
                            new DialogUI("Activity member has been added successfully",
                                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                            extraCurriculaController.ecStudentMembers.ams.restart();
                            close();
                    }else{
                        new DialogUI("Exception occurred while trying to add activity member.",
                                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                }else{
                    new DialogUI("Sport/ club member already exists !!!",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
            
            }else{
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
        //stackPane.setPrefSize(400, 200);
        show();
        
    }
    
    
}
