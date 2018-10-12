package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.jfoenix.controls.*;
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
public class UpdateSWAidDialog extends JFXDialog{

    private JFXTextField name;
    private final JFXComboBox<String> sw_name;
    private final JFXComboBox<String> coorporation;
    private final JFXTextArea description;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateSWAidDialog(Aid aid) {
                    
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
        name.setPromptText("Aid Name");
        name.setPrefWidth(360);
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        
        CCValidator.setFieldValidator(name, "Aid name required");
                
        sw_name = new JFXComboBox<>(SIMS.dbHandler.getSocialWelfareNames());
        sw_name.setPromptText("Social Welfare");
        sw_name.setLabelFloat(true);
        sw_name.setPrefWidth(360);
        new AutoCompleteComboBoxListener(sw_name);
        contentGrid.add(sw_name, 0, 1);
        
        coorporation = new JFXComboBox<>();
        coorporation.setPromptText("Cooperation");
        coorporation.setLabelFloat(true);
        coorporation.setPrefWidth(360);
        new AutoCompleteComboBoxListener(coorporation);
        contentGrid.add(coorporation, 0, 2);
        
        description = new JFXTextArea();
        description.setPrefWidth(360);
        description.setPrefRowCount(4);
        description.setPromptText("Description");
        description.setLabelFloat(true);
        contentGrid.add(description, 0, 3);
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Social Welfare Aid");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(aid != null){
            
            name.setText(aid.getName());
            coorporation.setValue(aid.getCooperation());
            sw_name.setValue(SIMS.dbHandler.getSocialWelfareByID(aid.getSocialWelfareID()).getName());
            description.setText(aid.getDescription());
            title.setText("Update Social Welfare Aid");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new Tooltip("Save changes"));
        save.setOnAction((ActionEvent event) -> {
            
        if(!"".equals(name.getText().trim())){
                
                if(aid != null){
                    
                    aid.setName(name.getText().trim());
                    aid.setSocialWelfare(SIMS.dbHandler.getSocialWelfareByName(sw_name.getValue()).getId());
                    aid.setCooperation(coorporation.getValue());
                    aid.setDescription(description.getText().trim());
                    
                    if(SIMS.dbHandler.updateAid(aid, true)){
                        
                        new DialogUI("Record has been updated successfully.",
                                    DialogUI.SUCCESS_NOTIF, STUDENT_MAN_STACK, this).show();
                        SocialWelfareAid.swas.restart();
                        close();
                    }else{
                        new DialogUI("Error encountered while trying to update record.",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                    
                }else{
                
                    Aid sws = new Aid("0", SIMS.dbHandler.getSocialWelfareByName(sw_name.getValue()).getId(),
                                      name.getText().trim(), coorporation.getValue(), description.getText().trim());
                    
                    if(SIMS.dbHandler.updateAid(sws, false)){
                        new DialogUI("New record has been added successfully.",
                                    DialogUI.SUCCESS_NOTIF, STUDENT_MAN_STACK, this).show();
                        SocialWelfareAid.swas.restart();
                        close();
                       
                    }else{
                        new DialogUI("Error encountered while trying to add new record.",
                                DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                }
                
            }else{
                name.validate();
                new DialogUI("Ensure that required fields are captured before saving changes.",
                               DialogUI.ERROR_NOTIF, stackPane, null).show(); 
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
        //stackPane.setPrefSize(400, 220);
        show();
        
    }
}
