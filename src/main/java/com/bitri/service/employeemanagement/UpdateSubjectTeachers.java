package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.Subject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateSubjectTeachers extends JFXDialog{

    public Subject subject;
    private final SelectionListView view;
    private ObservableList<String> currentSubjectTeacherId;
    private ObservableList<EmployeeModel> subjectTeachers;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateSubjectTeachers(Subject subject) {
        
        //-- A list of student IDs in the current class --
        currentSubjectTeacherId = FXCollections.observableArrayList();
        
        this.subject = subject;
                    
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label(" Teachers");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        view = new SelectionListView();
        view.setTargetHeader("Subject Teacher(s)");
        
        container.setCenter(view);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentSubjectTeacherId.isEmpty()){
                
                //-- Newly added students --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getItems()){
                    
                    //-- if the student wasn`t part of the class
                    if(!currentSubjectTeacherId.contains(sname)){ 
                        addList.add(getIDFromName(sname));
                        System.out.println(sname+" added");
                    }else{
                        System.out.println(sname+" already part of class");
                    }
                }
                
                //-- Students who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String sID: currentSubjectTeacherId){
                    
                    //-- if student is not in the target list
                    if(!view.getTarget().getItems().contains(sID)){
                        removeList.add(getIDFromName(sID));
                        System.out.println(sID+" has been removed from class");
                    }
                }
                
            
                if(EmployeeQuery.updateSubjectTeachers(addList, removeList, subject.getSubjectID())){
                    
                    new DialogUI("Subject teachers has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    AcademicSubjectTableView.subjectWork.restart();
                    
                    close();
                }else{
                    new DialogUI("Exception occurred while trying to update subject teachers.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No subject teacher selected. Please select teacher(s) before trying to save changes.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
            }
            
        });
        
        updateList();
        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(HumanResourceManagement.HR_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(550, 400);
        show();
        
        
    }

    private String getIDFromName(String name) {
        
        //-- look up for his ID --
        for(EmployeeModel teachers: subjectTeachers){
            
            if( teachers.getFullName().trim().equalsIgnoreCase(name.trim())){
                System.out.println(teachers.getEmployeeID());
                return teachers.getEmployeeID();
            }
        }
        return "";
    }
    
    
    public void updateList(){
        
        subjectTeachers = EmployeeQuery.getEmployeeByDesignationType(0);
        
        
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        ObservableList<EmployeeModel> currentClassStudents = EmployeeQuery.getSubjectEmployees(subject.getSubjectID());
        
        for(EmployeeModel std: currentClassStudents){
            target.add(std.getFullName().trim());
            currentSubjectTeacherId.add(std.getFullName().trim());
        }
        
        for(EmployeeModel std: subjectTeachers)
        {   
            if(!target.contains(std.getFullName()))
                source.add(std.getFullName());
        
        }
        
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
        
    }
}
