package com.bitri.service.studentmanagement;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.studentmanagement.BaseClassSpecialNeeds.EmployeeSubjectListWorkService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author moile
 */
public class StudentSpecificNeed extends JFXDialog{

    public static String specialNeedName;
    StackPane stackPane;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public StudentSpecificNeed(String specialNeedName,Student selected,EmployeeSubjectListWorkService tableService,CheckBox chbox,boolean flag) {
        
        //-- Parent Container --
        stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            if(!flag){
                chbox.setSelected(false);
            }    
            close();
        });
        
        Label title = new Label(selected.getFullName()+" - "+specialNeedName);
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        //-----------------------------------------------------
        GridPane grid = new GridPane();
        grid.getStyleClass().add("container");
        grid.setStyle("-fx-padding:20");
        grid.setVgap(20); 
        
        
        JFXTextArea description = new JFXTextArea();
        description.setPromptText("Student Special Need Description");
        description.setLabelFloat(true);
        grid.add(description, 0, 0);
      
        JFXTextArea solution = new JFXTextArea();
        solution.setPromptText("Student Special Need Solution");
        solution.setLabelFloat(true);
        grid.add(solution, 0, 1);
        
       
        
        container.setCenter(grid);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!description.getText().equals("") && !solution.getText().equals(""))
            {
                
                SpecialNeed sn = SIMS.dbHandler.getSpecialNeedByName(specialNeedName);
                if(StudentQuery.updateStudentSpecialNeed(selected,sn,description.getText(),solution.getText(),false)){
                    new DialogUI("Special need successfully updated",
                                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    close();
                    tableService.restart();
                }
                else{
                    new DialogUI("An error encountered while trying to save data.",
                                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
            }
            else{
                new DialogUI("Please ensure that required fields are captured,"
                                + " before trying to save data.",
                                    DialogUI.ERROR_NOTIF,stackPane, null).show();
                
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
        stackPane.setPrefSize(400, 320);
        show();
        
        
    }
    public StudentSpecificNeed(String ssid,EmployeeSubjectListWorkService tableService) {
        StudentSpecialNeedsModel ssn= StudentQuery.getSpecialNeedStudentBySSNID(ssid);
        SpecialNeed sn =StudentQuery.getSpecialNeedByID(ssn.getSn_id());
        //-- Parent Container --
        stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
               
            close();
        });
        
        Label title = new Label(ssn.getStudentName()+" - "+sn.getName());
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Changes"));
        
        //-----------------------------------------------------
        GridPane grid = new GridPane();
        grid.getStyleClass().add("container");
        grid.setStyle("-fx-padding:20");
        grid.setVgap(20); 
        
        
        JFXTextArea description = new JFXTextArea();
        description.setPromptText("Student Special Need Description");
        description.setLabelFloat(true);
        description.setText(ssn.getDescription());
        grid.add(description, 0, 0);
        
        
        JFXTextArea solution = new JFXTextArea();
        solution.setPromptText("Student Special Need Solution");
        solution.setLabelFloat(true);
        solution.setText(ssn.getSolution());
        grid.add(solution, 0, 1);
        
        container.setCenter(grid);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
            if(!description.getText().equals("") && !solution.getText().equals(""))
            {
                
                
                Student student = SIMS.dbHandler.getStudentByID(ssn.getStudentID());
                if(StudentQuery.updateStudentSpecialNeed(student,sn,description.getText(),solution.getText(),true)){
                    new DialogUI("Special need successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    close();
                    tableService.restart();
                }
                else{
                    new DialogUI("An error encountered while trying to save data.",
                                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
            }
            else{
                new DialogUI("Please ensure that required fields are captured,"
                                + " before trying to save data.",
                                    DialogUI.ERROR_NOTIF,stackPane, null).show();
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
        stackPane.setPrefSize(400, 320);
        show();
        
        
    }
}
