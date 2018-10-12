package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
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
import static com.bitri.service.schooladministration.SchoolAdministartion.streamClassesController;

/**
 *
 * @author jabari
 */
public class UpdateStreamSubjectsDialog extends JFXDialog{

    public Stream stream;
    private final SelectionListView view;
    private ObservableList<String> currentSubjectID;
    private ObservableList<Subject> allSubjects;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateStreamSubjectsDialog(Stream stream) {
        
        //-- A list of subject IDs in the current stream --
        currentSubjectID = FXCollections.observableArrayList();
        
        this.stream = stream;
                    
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
        
        Label title = new Label(stream.getDescription()+" Subjects");
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
            
            if(!view.getTarget().getItems().isEmpty() || !currentSubjectID.isEmpty()){
                
                //-- Newly added subjects --
                ObservableList<String> addList = FXCollections.observableArrayList();
                for(String sname: view.getTarget().getItems()){
                    
                    //-- if the subjects isn`t part of the stream
                    if(!currentSubjectID.contains(sname)){ 
                        addList.add(getIDFromName(sname));
                        System.out.println(sname+" added");
                    }
                }
                
                //-- Subjects who have been removed --
                ObservableList<String> removeList = FXCollections.observableArrayList();
                for(String sID: currentSubjectID){
                    
                    //-- if subject is not in the target list
                    if(!view.getTarget().getItems().contains(sID)){
                        removeList.add(getIDFromName(sID));
                        System.out.println(sID+" removed");
                    }
                }
                
            
                
                if(AdminQuery.addStreamSubjects(addList, removeList, stream.getStreamID())){
                    
                    new DialogUI("Streams subjects list has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    
                    streamClassesController.subjectList.subjectWorkService.restart();
                    close();
                }else{
                    new DialogUI("Exception occurred while trying to update stream subjects list.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No subjects selected for this stream. Please select subject(s) before trying to save changes.",
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
     * Get subject ID give its name.
     * @param name
     * @return 
     */
    private String getIDFromName(String name) {
        
        //-- look up for subject ID --
        for(Subject subject: allSubjects){
            
            if( subject.getName().trim().equalsIgnoreCase(name.trim())){
                return subject.getSubjectID();
            }
        }
        return "";
    }
    
    
    
    public void updateList(){
        
        allSubjects = AdminQuery.getAllSubjectList();
        
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        ObservableList<Subject> streamSubjects = AdminQuery.getStreamSubjectsList(stream.getStreamID());
        
        for(Subject sbj: streamSubjects){
            target.add(sbj.getName());
            currentSubjectID.add(sbj.getName());
        }
        
        for(Subject sbj: allSubjects){
            if(!target.contains(sbj.getName())){
                source.add(sbj.getName());
            }
        }
        
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
        
    }
}
