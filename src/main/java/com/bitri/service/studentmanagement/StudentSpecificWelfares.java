package com.bitri.service.studentmanagement;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.studentmanagement.BaseClassSocialWelfare.SocialWelfareListWorkService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
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
public class StudentSpecificWelfares extends JFXDialog{

    public static String specialNeedName;
    StackPane stackPane;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public StudentSpecificWelfares(String welfareName,Student selected,SocialWelfareListWorkService tableService,CheckBox chbox,boolean flag) {
        
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
        
        Label title = new Label(selected.getFullName()+" - "+welfareName);
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
        
        SocialWelfare sw = SIMS.dbHandler.getSocialWelfareByName(welfareName);

        
        JFXComboBox aidList = new JFXComboBox();
        aidList.setPromptText("Student Social Welfare Aid");
        aidList.setPrefWidth(320);
        aidList.setLabelFloat(true);
        aidList.setItems(StudentQuery.getSocialWelfareAidNamesBySWID(sw.getId()));
        aidList.getItems().add("No Aid");
        grid.add(aidList, 0, 0);
        
        container.setCenter(grid);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
                Aid aid = SIMS.dbHandler.getSocialWelfareAidByName(aidList.getValue().toString());
                if(StudentQuery.updateStudentSocialWelfare(selected,sw,aid.getId(),false)){
                    new DialogUI("Social welfare successfully updated",
                                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    close();
                    tableService.restart();
                }
                else{
                    new DialogUI("An error encountered while trying to save data.",
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
        stackPane.setPrefSize(400, 150);
        
        show();
        
        
    }
    public StudentSpecificWelfares(String ssid,SocialWelfareListWorkService tableService) {
        
        StudentSocialWelfareModel ssw = StudentQuery.getStudentSocialWelfareBySSWID(ssid);
        SocialWelfare sn = SIMS.dbHandler.getSocialWelfareByID(ssw.getSwID());
        Aid aid = SIMS.dbHandler.getSocialWelfareAidByID(ssw.getAidID());
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
        
        Label title = new Label(ssw.getStudentName()+" - "+sn.getName());
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
        
        JFXComboBox aidList = new JFXComboBox();
        aidList.setPromptText("Student Social Welfare Aid");
        aidList.setLabelFloat(true);
        aidList.setItems(StudentQuery.getSocialWelfareAidNamesBySWID(ssw.getSwID()));
        aidList.getItems().add("No Aid");
        aidList.setPrefWidth(320);
        aidList.setValue(aid.getName());
        grid.add(aidList, 0, 0);
        
        
        container.setCenter(grid);
        
        //-- Validate and save the form  ---------------------------------------
        save.setOnAction((ActionEvent event) -> {
            
                Student student = SIMS.dbHandler.getStudentByID(ssw.getStudentID());
                Aid aid2 = SIMS.dbHandler.getSocialWelfareAidByName(aidList.getValue().toString());
                if(StudentQuery.updateStudentSocialWelfare(student,sn,aid2.getId(),true)){
                    new DialogUI("Social welfare successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    close();
                    tableService.restart();
                }
                else{
                    new DialogUI("An error encountered while trying to save data.",
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
        stackPane.setPrefSize(400, 150);
        show();
        
        
    }
}
