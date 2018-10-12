package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Subject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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
public class UpdateOptionalSubjectsStudentsDialog extends JFXDialog{

    public BaseClass baseClass;
    public Subject subject;
    private final SelectionListView view;
    private ObservableList<String> currentStudentIDs;
    private ObservableList<Student> allClassStudents;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateOptionalSubjectsStudentsDialog(BaseClass baseClass, Subject subject, Service service) {
        
        //-- A list of studentIDs currently enrolled --
        currentStudentIDs = FXCollections.observableArrayList();
        
        this.baseClass = baseClass;
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
        
        Label title = new Label(baseClass.getName()+" Students");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        view = new SelectionListView();
        view.setTargetHeader("Selected Subjects");
        
        container.setCenter(view);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentStudentIDs.isEmpty()){
                
                //-- Newly added subjects --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getItems()){
                    
                    //-- if the subjects isn`t part of the stream
                    if(!currentStudentIDs.contains(sname)){ 
                        addList.add(getIDFromName(sname));
                        System.out.println(sname+" added");
                    }
                }
                
                //-- Subjects who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String studentNames: currentStudentIDs){
                    
                    //-- if subject is not in the target list
                    if(!view.getTarget().getItems().contains(studentNames)){
                        removeList.add(getIDFromName(studentNames));
                        System.out.println(studentNames+" removed");
                    }
                }
                
                if(AdminQuery.updateOptionalSubjectStudent(addList, removeList, subject.getSubjectID())){
                    
                    new DialogUI(subject.getName()+" student list has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    service.restart();
                    close();
                }else{
                    new DialogUI("Exception occurred while trying to update "+subject.getName()+" student list.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No student selected for this stream. Please select student(s) before trying to save changes.",
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
        setDialogContainer(PARENT_STACK_PANE);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(550, 400);
        show();
        
        
    }

    /**
     * Get student ID given their name.
     * @param name
     * @return 
     */
    private String getIDFromName(String name) {
        
        //-- look up for subject ID --
        for(Student student: allClassStudents){
            
            if( student.getFullName().trim().equalsIgnoreCase(name.trim())){
                return student.getStudentID();
            }
        }
        return "";
    }
    
    
    
    public void updateList(){
        
        allClassStudents = SIMS.dbHandler.getBaseClassStudentList(baseClass.getClassID());
        
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        ObservableList<Student> subjectstudent = AdminQuery.getOptionalSubjectStudentList(baseClass.getClassID(), subject.getSubjectID());
        
        for(Student std: subjectstudent){
            target.add(std.getFullName());
            currentStudentIDs.add(std.getFullName());
        }
        
        for(Student std: allClassStudents){
            if(!target.contains(std.getFullName())){
                source.add(std.getFullName());
            }
        }
        
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
        
    }
}
