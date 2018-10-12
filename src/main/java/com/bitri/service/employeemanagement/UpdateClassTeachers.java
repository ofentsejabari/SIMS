package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Subject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateClassTeachers extends JFXDialog{

    public BaseClass base_class;
    private final CustomCheckListView view;
    private ObservableList<String> currentCoreClassTeacher;
    private ObservableList<EmployeeModel> coreClassTeachers;
    public ObservableList<Subject> assignedSubjects;
    public ObservableList<String> currentClassAssignedSubjects;
    private EmployeeModel selectedTeacher;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateClassTeachers(BaseClass base) {
        
        //-- A list of student IDs in the current class --
        currentCoreClassTeacher = FXCollections.observableArrayList();
        currentClassAssignedSubjects = FXCollections.observableArrayList();
        
        this.base_class = base;
                    
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
        
        Label title = new Label(base.getName()+" Subject Teachers");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        view = new CustomCheckListView();
        view.setTargetHeader(new Label("Core Class Teacher(s)"));
        view.getSource().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            currentClassAssignedSubjects.clear();
            view.getTarget().getItems().clear();
            ObservableList<String> target = FXCollections.observableArrayList();
            selectedTeacher = EmployeeQuery.getEmployeeByName(newValue);
            ObservableList<Subject> classAssignedSubjects = EmployeeQuery.getSubjectListFor(selectedTeacher.getEmployeeID(),
                    base_class.getClassID());
            
            assignedSubjects = EmployeeQuery.getSubjectListFor(selectedTeacher.getEmployeeID());
            
            for( Subject sbj: classAssignedSubjects){
                currentClassAssignedSubjects.add(sbj.getName().trim());
            } 
            
            for(Subject std: assignedSubjects){
                target.add(std.getName().trim());
            }
            view.getTarget().setItems(target);
            
            for(Subject std: assignedSubjects){
                if(currentClassAssignedSubjects.contains(std.getName().trim())){
                    view.getTarget().getCheckModel().check(std.getName().trim());
                    currentCoreClassTeacher.add(std.getName());
                }
                
            }
        
        });
        view.setTargetFooter(save);
        container.setCenter(view);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentClassAssignedSubjects.isEmpty()){
                
                //-- Newly added students --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getCheckModel().getCheckedItems()){
                    
                    //-- if the student wasn`t part of the class
                    if(!currentClassAssignedSubjects.contains(sname)){ 
                        addList.add(getIDFromName(sname));
                    }
                }
                
                //-- Students who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String sID: view.getTarget().getItems()){
                    
                    //-- if student is not in the target list
                    if(currentClassAssignedSubjects.contains(sID) && !view.getTarget().getCheckModel().isChecked(sID)){
                        removeList.add(getIDFromName(sID));
                        System.out.println(sID+" has been removed from class");
                    }
                }
                
            
                if(EmployeeQuery.updateSubjectClassTeachers(addList, removeList, base.getClassID(),selectedTeacher.getEmployeeID())){
                    
                    new DialogUI("Subject teachers has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    AcademicClassesView.classWork.restart();
                    
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
        
         //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(HumanResourceManagement.HR_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(530, 400);
        show();
        
        
    }

    private String getIDFromName(String name) {
        
        //-- look up for his ID --
        for(Subject subjects: assignedSubjects){
            
            if( subjects.getName().equalsIgnoreCase(name.trim())){
                return subjects.getSubjectID();
            }
        }
        return "";
    }
    
    
    public void updateList(){
        
        coreClassTeachers = EmployeeQuery.getEmployeeByDesignationType(0);
        ObservableList<String> source = FXCollections.observableArrayList();
        for(EmployeeModel std: coreClassTeachers)
        {   
            source.add(std.getFullName());
        }
        
        view.getSource().setItems(source);
    }
}
