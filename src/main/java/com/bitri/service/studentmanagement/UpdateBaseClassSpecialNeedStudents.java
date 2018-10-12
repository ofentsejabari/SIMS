package com.bitri.service.studentmanagement;


import com.bitri.access.CustomCheckListView2;
import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.studentmanagement.BaseClassSpecialNeeds.EmployeeSubjectListWorkService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Optional;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateBaseClassSpecialNeedStudents extends JFXDialog{

    public BaseClass base_class;
    private final CustomCheckListView2 view;
    private ObservableList<String> specialNeedStudent;
    private ObservableList<Student> classStudents;
    public ObservableList<StudentSpecialNeedsModel> studentNeeds;
    public ObservableList<StudentSpecialNeedsModel> studentModel;
    public ObservableList<String> specialNeeds;
    private Student selectedStudent;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateBaseClassSpecialNeedStudents(BaseClass base,EmployeeSubjectListWorkService service) {
        
        //-- A list of student IDs in the current class --
        specialNeedStudent = FXCollections.observableArrayList();
        specialNeeds = FXCollections.observableArrayList();
        
        
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
        
        Label title = new Label(base.getName()+"  Special Need Students");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        
        view = new CustomCheckListView2();
        view.setTargetHeader(new Label("Special Needs"));
        view.getSource().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            specialNeeds.clear();
            view.getTarget().getItems().clear();
            ObservableList<HBox> target = FXCollections.observableArrayList();
            selectedStudent = SIMS.dbHandler.getStudentByName(newValue);
            ObservableList<SpecialNeed> allSpecialNeeds= SIMS.dbHandler.getSpecialNeeds();
            studentNeeds = StudentQuery.getSpecialNeedsFor(selectedStudent.getStudentID());
            
            
            for(SpecialNeed std: allSpecialNeeds){
                HBox hneeds = new HBox();
                CheckBox chbox = new CheckBox();
                chbox.setOnAction((ActionEvent event) -> {
                    
                    if(chbox.isSelected()){
                        System.out.println(std.getName().trim());
                        new StudentSpecificNeed(std.getName().trim(),selectedStudent,service,chbox,false);
                    }
                    else{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Are you sure you want to remove special need");
                            Optional <ButtonType> action= alert.showAndWait();
                            
                            if(action.get()==ButtonType.OK){
                                SpecialNeed sn = SIMS.dbHandler.getSpecialNeedByName(std.getName().trim());
                                if(StudentQuery.deleteStudentSpecialNeed(selectedStudent,sn)){
                                    new DialogUI("Special need successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                                    close();
                                    service.restart();
                                }
                                else{
                                     new DialogUI("error occured trying to save data", DialogUI.ERROR_NOTIF,stackPane, null).show();
                                }
                                
                            }
                            if(action.get()==ButtonType.CANCEL){
                            
                                chbox.setSelected(true);
                            }
                            
                       
                    }
                
                });
                
                for(StudentSpecialNeedsModel  ssnm: studentNeeds){
                     
                    if(ssnm.getSn_id().equals(std.getId()))
                    {
                        chbox.setSelected(true);
                    }
                
                }
                
                
                hneeds.getChildren().addAll(chbox,new Label(std.getName().trim()));
                target.add(hneeds);
                
            }
            view.getTarget().setItems(target);
            
            //--checking defaults
            
        
        });
        
        container.setCenter(view);
        updateList();
        
         //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(PARENT_STACK_PANE);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(530, 400);
        show();
        
        
    }

  
    
    
    public void updateList(){
        
        classStudents = SIMS.dbHandler.getBaseClassStudentList(base_class.getClassID());
        ObservableList<String> source = FXCollections.observableArrayList();
        for(Student std: classStudents)
        {   
            source.add(std.getFullName());
        }
        
        view.getSource().setItems(source);
    }
}
