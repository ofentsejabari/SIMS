package com.bitri.service.studentmanagement;

import com.bitri.access.CCValidator;
import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.service.studentmanagement.StudentManagement.STUDENT_MAN_STACK;

/**
 *
 * @author jabari
 */
public class UpdateSWSDialog extends JFXDialog{

    private JFXTextField name;
    private JFXTextArea description;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateSWSDialog(SocialWelfare specialWelfare) {
                    
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
        name.setPromptText("Name");
        name.setPrefWidth(360);
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        
        CCValidator.setFieldValidator(name, "Name required");
                
        description = new JFXTextArea();
        description.setPromptText("Description");
        description.setLabelFloat(true);
        description.setPrefWidth(360);
        contentGrid.add(description, 0, 1);
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Social Welfare");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(specialWelfare != null){
            
            name.setText(specialWelfare.getName());
            description.setText(specialWelfare.getDescription());
            title.setText("Update Social Welfare");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new Tooltip("Save Changes"));
        save.setOnAction((ActionEvent event) -> {
            
        if(!"".equals(name.getText().trim())){
                
                if(specialWelfare != null){
                    
                    specialWelfare.setName(name.getText().trim());
                    specialWelfare.setDescriptions((description.getText().trim()));
                    
                    if(SIMS.dbHandler.updateSWS(specialWelfare, true)){
                        
                        new DialogUI("Record has been updated successfully",
                                    DialogUI.SUCCESS_NOTIF, STUDENT_MAN_STACK, this).show();
                        SocialWelfareSupport.SWSWorkService.restart();
                        SocialWelfareSupport.wss.restart();
                        close();
                    }else{
                        new DialogUI("Error encountered while trying to update record.",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                    
                }else{
                
                    SocialWelfare sws = new SocialWelfare("0", name.getText().trim(), description.getText().trim());
                    
                    if(SIMS.dbHandler.updateSWS(sws, false)){
                        
                        new DialogUI("New record has been added successfully",
                                    DialogUI.SUCCESS_NOTIF, STUDENT_MAN_STACK, this).show();
                        SocialWelfareSupport.SWSWorkService.restart();
                        SocialWelfareSupport.wss.restart();
                        close();
                       
                    }else{
                        new DialogUI("Error encountered while trying to add new record",
                                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                }
                
            }else{
                name.validate();
                description.validate();
                new DialogUI("Ensure that required fields are captured before saving changes.",
                                    DialogUI.SUCCESS_NOTIF, stackPane, null).show();
            }
            
        });
        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(StudentManagement.STUDENT_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 220);
        show();
        
    }
}
