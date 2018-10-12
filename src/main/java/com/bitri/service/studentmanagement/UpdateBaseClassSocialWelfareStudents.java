package com.bitri.service.studentmanagement;

import com.bitri.access.CustomCheckListView2;
import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.studentmanagement.BaseClassSocialWelfare.SocialWelfareListWorkService;
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
public class UpdateBaseClassSocialWelfareStudents extends JFXDialog{

    public BaseClass base_class;
    private final CustomCheckListView2 view;
    private ObservableList<String> socialWelfareStudent;
    private ObservableList<Student> classStudents;
    public ObservableList<StudentSocialWelfareModel> studentWelfares;
    public ObservableList<StudentSocialWelfareModel> studentModel;
    public ObservableList<String> socialWelfares;
    private Student selectedStudent;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateBaseClassSocialWelfareStudents(BaseClass base,SocialWelfareListWorkService service) {
        
        //-- A list of student IDs in the current class --
        socialWelfareStudent = FXCollections.observableArrayList();
        socialWelfares = FXCollections.observableArrayList();
        
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
        
        Label title = new Label(base.getName()+"  Social Welfare");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        
        view = new CustomCheckListView2();
        view.setTargetHeader(new Label("Social Welfare"));
        view.getSource().getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            socialWelfares.clear();
            view.getTarget().getItems().clear();
            ObservableList<HBox> target = FXCollections.observableArrayList();
            selectedStudent = SIMS.dbHandler.getStudentByName(newValue);
            ObservableList<SocialWelfare> allSocialWelfares= SIMS.dbHandler.getSocialWelfares();
            studentWelfares = StudentQuery.getSocialWelfareFor(selectedStudent.getStudentID());
            
            
            for(SocialWelfare std: allSocialWelfares){
                HBox hneeds = new HBox();
                CheckBox chbox = new CheckBox();
                chbox.setOnAction((ActionEvent event) -> {
                    
                    if(chbox.isSelected()){
                        System.out.println(std.getName().trim());
                        new StudentSpecificWelfares(std.getName().trim(),selectedStudent,service,chbox,false);
                    }
                    else{
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Are you sure you want to remove social welfare");
                            Optional <ButtonType> action= alert.showAndWait();
                            
                            if(action.get()==ButtonType.OK){
                                SocialWelfare sn = SIMS.dbHandler.getSocialWelfareByName(std.getName().trim());
                                if(StudentQuery.deleteStudentSocialWelfare(selectedStudent,sn))
                                {
                                    new DialogUI("Social welfare successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
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
                
                for(StudentSocialWelfareModel  ssw: studentWelfares){
                    
                    if(ssw.getSwID().equals(std.getId()))
                    {
                        
                        chbox.setSelected(true);
                    }
                
                }
                
                hneeds.getChildren().addAll(chbox,new Label(std.getName().trim()));
                target.add(hneeds);
                
            }
            view.getTarget().setItems(target);
            
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
