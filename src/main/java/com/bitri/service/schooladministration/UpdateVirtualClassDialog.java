package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.schooladministration.SchoolAdministartion.batchClassesController;

/**
 *
 * @author jabari
 */
public class UpdateVirtualClassDialog extends JFXDialog{

    private JFXTextField name;
    private JFXComboBox<String> batch, subject, teacher;
    
    //private final ValidationSupport vSupport;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateVirtualClassDialog(VirtualClass virtualClass) {
                    
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
        name.setPromptText("Class Name");
        name.setPrefWidth(360);
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        
        CCValidator.setFieldValidator(name, "Class name required.");
                
        batch = new JFXComboBox<>(AdminQuery.getBatchNames());
        batch.setPromptText("Batch Name");
        batch.setLabelFloat(true);
        batch.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(batch);
        new AutoCompleteComboBoxListener(batch);
        contentGrid.add(batch, 0, 1);
        if(batchClassesController.selectedBatch != null){
            batch.setValue(batchClassesController.selectedBatch.getDescription());
        }        
        
        subject = new JFXComboBox<>(AdminQuery.getSubjectNameList(false, 0));
        subject.setPromptText("Optional Subject");
        subject.setLabelFloat(true);
        subject.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(subject);
        new AutoCompleteComboBoxListener(subject);
        contentGrid.add(subject, 0, 2);
        
        
        teacher = new JFXComboBox<>(EmployeeQuery.getEmployeeNameList());
        teacher.setPromptText("Assigned Teacher");
        teacher.setLabelFloat(true);
        teacher.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(teacher);
        new AutoCompleteComboBoxListener(teacher);
        contentGrid.add(teacher, 0, 3);
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Create Optional Subject Class");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        if(virtualClass != null){
            
            name.setText(virtualClass.getName());
            teacher.setValue(EmployeeQuery.getEmployeeByID(virtualClass.getTeacherID()).getFullName());
            
            batch.setValue(AdminQuery.getBatchByID(virtualClass.getBatchID()).getDescription());
            subject.setValue(AdminQuery.getSubjectByID(virtualClass.getSubjectID()).getName());
            
            title.setText("Update Class Details");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!"".equals(name.getText().trim()) 
                    && (batch.getValue() != null && !batch.getValue().trim().equals("")) 
                    && (subject.getValue() != null && !subject.getValue().trim().equals(""))){

                    if(virtualClass != null){
                        VirtualClass cls = new VirtualClass(virtualClass.getClassID(),
                                        name.getText().trim(), 
                                        (teacher.getValue()!= null)?EmployeeQuery.getEmployeeByName(teacher.getValue()).getEmployeeID():"",
                                        (subject.getValue() != null)?AdminQuery.getSubjectByName(subject.getValue()).getSubjectID():"",
                                         AdminQuery.getBatchByName(batch.getValue()).getId(), "");

                        if(AdminQuery.updateVirtualClass(cls, true)){

                                new DialogUI("Class details been updated successfully",
                                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                                batchClassesController.sws.restart();
                                close();

                            }else{
                                new DialogUI("Exception occurred while trying to update base class details",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                            }
                    }else{

                        VirtualClass cls = new VirtualClass(SIMS.generateDBID(), name.getText().trim(),
                                (teacher.getValue()!= null)?EmployeeQuery.getEmployeeByName(teacher.getValue()).getEmployeeID():"",
                                (subject.getValue() != null)?AdminQuery.getSubjectByName(subject.getValue()).getSubjectID():"",
                                 AdminQuery.getBatchByName(batch.getValue()).getId(),
                                "");

                        if(!AdminQuery.isVirtualClassExists(cls)){
                            if(AdminQuery.updateVirtualClass(cls, false)){

                                new DialogUI("Class details has been added successfully",
                                DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                batchClassesController.sws.restart();
                                close();
                            }else{
                                new DialogUI("Exception occurred while trying to add class details.",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                            }

                       }else{
                            new DialogUI("Class name has been used already. Please use a different class name.",
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
        setDialogContainer(PARENT_STACK_PANE);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 200);
        show();
        
    }
    
   
}
