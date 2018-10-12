package com.bitri.service.schooladministration;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class AddAcademicYear extends JFXDialog{

    private JFXComboBox<String> academic_year;
        
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public AddAcademicYear(JFXComboBox list) {
                  
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 5 15 20;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(10);
        
        academic_year = new JFXComboBox<>(FXCollections.observableArrayList("2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"));
        academic_year.setPromptText("Acardemic Year");
        academic_year.setLabelFloat(true);
        academic_year.setPrefWidth(360);
        contentGrid.add(academic_year, 0, 0);
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Academic Year");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!"".equals(academic_year.getValue())){
            
                if(!AdminQuery.isAcademicYearExist(academic_year.getValue())){
                    
                    if(AdminQuery.addAcademicYear(academic_year.getValue())){

                        list.setItems(AdminQuery.getAcademicYearList());
                        new DialogUI("Academic year added successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                    }else{
                        new DialogUI("Problem encountered while trying to add academic year.",
                                    DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, this);
                    }

                }else{
                    new DialogUI("Academic year already exists.",
                                DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, this);
                }
            }else{
                new DialogUI( "Select year before proceeding.",
                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
            }
        });  
            
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(SchoolAdministartion.ADMIN_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 150);
        show();
        
    }
    
}
