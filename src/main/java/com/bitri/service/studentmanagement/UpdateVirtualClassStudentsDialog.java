package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.schooladministration.Batch;
import com.bitri.service.schooladministration.VirtualClass;
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
public class UpdateVirtualClassStudentsDialog extends JFXDialog{

    public VirtualClass vClass;
    private final SelectionListView view;
    private ObservableList<String> currentVClassStudentNames;
    private ObservableList<Student> allStudent;
    Batch selectedBatch;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateVirtualClassStudentsDialog(VirtualClass vClass, Batch selectedBatch, Service service) {
        
        //-- A list of student IDs in the current class --
        currentVClassStudentNames = FXCollections.observableArrayList();
        this.selectedBatch = selectedBatch;
        
        this.vClass = vClass;
                    
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
        
        Label title = new Label(vClass.getName()+" Class List");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        view = new SelectionListView();
        view.setTargetHeader(vClass.getName()+" student(s)");
        
        container.setCenter(view);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentVClassStudentNames.isEmpty()){
                
                //-- Newly added students --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getItems()){
                    
                    //-- if the student wasn`t part of the class
                    if(!currentVClassStudentNames.contains(sname)){
                        
                        addList.add(getIDFromName(sname));
                        System.out.println(sname+" added");
                    }else{
                        System.out.println(sname+" already part of class");
                    }
                }
                
                //-- Students who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String sID: currentVClassStudentNames){
                    
                    //-- if student is not in the target list
                    if(!view.getTarget().getItems().contains(sID)){
                        removeList.add(getIDFromName(sID));
                        System.out.println(sID+" has been removed from class");
                    }
                }
                
            
                if(StudentQuery.addVirtualClassStudents(addList, removeList, vClass.getClassID())){
                    
                    new DialogUI("Class list has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    service.restart();
                    close();
                }else{
                    new DialogUI("Exception occurred while trying to update class list.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No student selected for this class. Please select student(s) before trying to save changes.",
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
        setDialogContainer(StudentManagement.STUDENT_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(550, 400);
        show(); 
    }
    
    

    private String getIDFromName(String name) {
        
        //-- look up for his ID --
        for(Student student: allStudent){
            
            if( student.getFullName().trim().equalsIgnoreCase(name.trim())){
                System.out.println(student.getStudentID());
                return student.getStudentID();
            }
        }
        return "";
    }
    
    
    public void updateList(){
        
        allStudent = AdminQuery.getStreamStudentOptionalList(selectedBatch.getStreamID(), vClass.getSubjectID());
                
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        ObservableList<Student> allocatedStudents = AdminQuery.getVirtualClassStudentList(vClass.getClassID(), vClass.getSubjectID());
        
        
        for(Student std: allocatedStudents){
            target.add(std.getFullName().trim());
            currentVClassStudentNames.add(std.getFullName().trim());
        }
        
        for(Student std: allStudent){
            if(!target.contains(std.getFullName())){
                source.add(std.getFullName());
            }
        }
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
        
    }
}
