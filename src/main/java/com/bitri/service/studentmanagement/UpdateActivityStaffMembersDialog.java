package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.employeemanagement.EmployeeModel;
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
public class UpdateActivityStaffMembersDialog extends JFXDialog{

    public ExtraCurriculaActivity activity;
    private final SelectionListView view;
    private ObservableList<String> currentActivityStaffID;
    private ObservableList<EmployeeModel> allEmployee;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateActivityStaffMembersDialog(ExtraCurriculaActivity activity, Service service) {
        
        //-- A list of student IDs in the current class --
        currentActivityStaffID = FXCollections.observableArrayList();
        
        this.activity = activity;
                    
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
        
        Label title = new Label(activity.getName()+" Members");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        view = new SelectionListView();
        view.setTargetHeader(" Staff(s)");
        
        container.setCenter(view);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentActivityStaffID.isEmpty()){
                
                //-- Newly added students --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getItems()){
                    
                    //-- if the student wasn`t part of the class
                    if(!currentActivityStaffID.contains(sname)){ 
                        addList.add(getIDFromName(sname));
                        System.out.println(sname+" added");
                    }else{
                        System.out.println(sname+" already part of activity");
                    }
                }
                
                
                //-- Students who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String sID: currentActivityStaffID){
                    
                    //-- if student is not in the target list
                    if(!view.getTarget().getItems().contains(sID)){
                        removeList.add(getIDFromName(sID));
                        System.out.println(sID+" has been removed from list");
                    }
                }
                
            
                if(AdminQuery.updateActivityMembers(addList, removeList, activity.getId(), "Student")){
                    
                    new DialogUI("Activity members list has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    service.restart();
                    close();
                }else{
                    new DialogUI("Exception occurred while trying to update members list.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No student selected for this activity. Please select student(s) before trying to save changes.",
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
        for(EmployeeModel employee: allEmployee){
            
            if( employee.getFullName().trim().equalsIgnoreCase(name.trim())){
                System.out.println(employee.getEmployeeID());
                return employee.getEmployeeID();
            }
        }
        return "";
    }
        
    
    public void updateList(){
        
        allEmployee = EmployeeQuery.getEmployeeList();
        
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        ObservableList<ActivityMember> current = AdminQuery.getExtraCurriculaActivitiesMembers(activity.getId(), "Staff", false);
        
        for(ActivityMember std: current){
            target.add(std.getMemberID().trim());
            currentActivityStaffID.add(std.getMemberID().trim());
        }
        
        for(EmployeeModel std: allEmployee){
            if(!target.contains(std.getFullName().trim())){
                source.add(std.getFullName().trim());
            }
        }
        
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
    }
}
