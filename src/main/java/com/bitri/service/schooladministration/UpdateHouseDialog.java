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
import static com.bitri.service.schooladministration.SchoolAdministartion.houseController;


/**
 *
 * @author jabari
 */
public class UpdateHouseDialog extends JFXDialog{

    private JFXTextField name;
    private JFXComboBox<String> hoh;
    
    //private final ValidationSupport vSupport;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateHouseDialog(House house) {
                    
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
        name.setPromptText("House Name");
        name.setPrefWidth(360);
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        
        CCValidator.setFieldValidator(name, "House name required.");
                
        hoh = new JFXComboBox<>();
        hoh.setPromptText("Head Of House");
        hoh.setLabelFloat(true);
        hoh.setPrefWidth(360);
        AutoCompleteComboBoxListener.setAutoCompleteValidator(hoh);
        new AutoCompleteComboBoxListener(hoh);
        contentGrid.add(hoh, 0, 1);
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add House");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(house != null){
            
            hoh.setItems(EmployeeQuery.getEmployeeNameList());
            
            name.setText(house.getHouseName());
            hoh.setValue(EmployeeQuery.getEmployeeByID(house.getHOH()).getFullName());
            
            title.setText("Update House");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save House"));
        save.setOnAction((ActionEvent event) -> {
            
        if(!"".equals(name.getText().trim())){
                
                if(house != null){
                    
                    house.setHouseName(name.getText().trim());
                    house.setHOH((hoh.getValue() == null)? "":
                            EmployeeQuery.getEmployeeByName(hoh.getValue().toString()).getEmployeeID());
                            
                    
                    if(AdminQuery.updateHouse(house, true)){
                        
                        new DialogUI("House details has been updated successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                        houseController.hws.restart();
                        close();
                    }else{
                        new DialogUI("Exception occurred while trying to update house details",
                        DialogUI.ERROR_NOTIF, stackPane, null).show();
                    }
                    
                }else{
                
                    House newHouse = new House("0", name.getText().trim(), 
                            (hoh.getValue() == null)? "":EmployeeQuery.getEmployeeByName(hoh.getValue()).getID());
                    
                    if(AdminQuery.updateHouse(newHouse, false)){
                        
                        new DialogUI("New house has been created successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                        houseController.hws.restart();
                        close();
                       
                    }else{
                        new DialogUI("Exception occurred while trying to add new house details.",
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
        setDialogContainer(SchoolAdministartion.ADMIN_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 200);
        show();
        
    }
}
