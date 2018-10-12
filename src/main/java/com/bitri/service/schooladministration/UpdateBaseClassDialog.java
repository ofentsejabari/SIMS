package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.studentmanagement.StudentAdmin;
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
public class UpdateBaseClassDialog extends JFXDialog{

    private JFXTextField name;
    private JFXComboBox<String> batch, house, teacher;
    
    //private final ValidationSupport vSupport;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateBaseClassDialog(BaseClass baseClass) {
                    
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
        
        house = new JFXComboBox<>(AdminQuery.getHouseNames());
        house.setPromptText("Class House");
        house.setLabelFloat(true);
        house.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(house);
        new AutoCompleteComboBoxListener(house);
        contentGrid.add(house, 0, 2);
        
        
        teacher = new JFXComboBox<>(EmployeeQuery.getEmployeeNameList());
        teacher.setPromptText("Assigned Base Teacher");
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
        
        Label title = new Label("Add Base Class");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(baseClass != null){
            
            name.setText(baseClass.getName());
            teacher.setValue(EmployeeQuery.getEmployeeByID(baseClass.getBaseTeacherID()).getFullName());
            
            batch.setValue(AdminQuery.getBatchByID(baseClass.getBatchID()).getDescription());
            house.setValue(AdminQuery.getHouseByID(baseClass.getHouse()).getHouseName());
            
            title.setText("Update Base Class");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Department"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!"".equals(name.getText().trim()) 
                    && (batch.getValue() != null && !batch.getValue().trim().equals("")) 
                    && (house.getValue() != null && !house.getValue().trim().equals(""))){

                    if(baseClass != null){
                        BaseClass cls = new BaseClass(baseClass.getClassID(),
                                        name.getText().trim(), 
                                        (teacher.getValue()!= null)?EmployeeQuery.getEmployeeByName(teacher.getValue()).getEmployeeID():"",
                                        (house.getValue() != null)?AdminQuery.getHouseByName(house.getValue()).getID():"",
                                         AdminQuery.getBatchByName(batch.getValue()).getId(), "");

                        if(AdminQuery.updateBaseClass(cls, true)){

                                new DialogUI("Base class details been updated successfully",
                                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                                batchClassesController.sws.restart();
                                StudentAdmin.baseClass.bcls.restart();
                                close();

                            }else{
                                new DialogUI("Exception occurred while trying to update base class details",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                            }
                    }else{

                        BaseClass cls = new BaseClass(SIMS.generateDBID(), name.getText().trim(),
                                (teacher.getValue()!= null)?EmployeeQuery.getEmployeeByName(teacher.getValue()).getEmployeeID():"",
                                (house.getValue() != null)?AdminQuery.getHouseByName(house.getValue()).getID():"",
                                 AdminQuery.getBatchByName(batch.getValue()).getId(),
                                "");

                        if(!AdminQuery.isClassExists(cls)){
                            if(AdminQuery.updateBaseClass(cls, false)){

                                new DialogUI("Base class details has been added successfully",
                                DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                batchClassesController.sws.restart();
                                StudentAdmin.baseClass.bcls.restart();
                                close();
                            }else{
                                new DialogUI("Exception occurred while trying to add base class details.",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                            }

                       }else{
                            new DialogUI("Base class name has been used already. Please use a different class name.",
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
