package com.bitri.service.financemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.FeesQuery;
import com.bitri.service.financemanagement.FeesUI.AcademicYearsWorkService;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
public class UpdateFeeDialog extends JFXDialog{

    private JFXTextField feeName,exactDate;
    private JFXTextArea description;
    private JFXComboBox<String> paymentMode;
    private JFXTextField defaultAmount;
    private JFXComboBox<String> academicYear;
     
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateFeeDialog(Fee fee,AcademicYearsWorkService fws) {
                  
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
        
        feeName = new JFXTextField();
        feeName.setPromptText("Fee Name");
        feeName.setPrefWidth(360);
        feeName.setLabelFloat(true);
        contentGrid.add(feeName, 0, 0);
        
        CCValidator.setFieldValidator(feeName, "Fee name required.");
        
        description = new JFXTextArea();
        description.setPromptText("Description");
        description.setPrefWidth(360);
        description.setPrefColumnCount(3);
        description.setLabelFloat(true);
        contentGrid.add(description, 0, 1);
        
        CCValidator.setFieldValidator(description, "Fee description required.");
                
        paymentMode = new JFXComboBox<>();
        paymentMode.setPromptText("Payment Mode");
        paymentMode.setLabelFloat(true);
        paymentMode.setPrefWidth(360);
        contentGrid.add(paymentMode, 0, 2);
        paymentMode.setItems(FXCollections.observableArrayList("Monthly","Term Wise","Specific Day","Annually"));
        paymentMode.selectionModelProperty().addListener((observable, oldValue, newValue) -> {
            

                System.out.println(newValue);
            
        });
        
        defaultAmount = new JFXTextField();
        defaultAmount.setPromptText("Fee Amount");
        defaultAmount.setPrefWidth(360);
        defaultAmount.setLabelFloat(true);
        defaultAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,2})?")) {
                    defaultAmount.setText(oldValue);
                }
            }
        });
        contentGrid.add(defaultAmount, 0, 3);
        
        CCValidator.setFieldValidator(defaultAmount, "Fee amount required.");
                
        academicYear = new JFXComboBox<>(AdminQuery.getAcademicYearList());
        academicYear.setPromptText("Academic Year");
        academicYear.setLabelFloat(true);
        academicYear.setPrefWidth(360);
        //academicYear.setEditable(true);
        contentGrid.add(academicYear, 0, 4);
                
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Fee");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        if(fee != null){
            feeName.setText(fee.getName());
            description.setText(fee.getDescription());
            paymentMode.setValue(fee.getPaymentMode());
            defaultAmount.setText(fee.getDefaultAmount());
            academicYear.setValue(fee.getAcademicYear());
        }

        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn", "btn-primary");
        save.setTooltip(new ToolTip("Save Fee Type"));
       
        save.setOnAction((ActionEvent event) -> {
        
            if(!"".equals(description.getText().trim())){
                
                if(fee != null){
                    
                    fee.setDescription(description.getText().trim());
                    fee.setPaymentMode(paymentMode.getValue().trim());
                    fee.setName(feeName.getText().trim());
                    fee.setAcademicYear(academicYear.getValue().trim());
                    fee.setDefaultAmount(defaultAmount.getText().trim());
                    
                    if(FeesQuery.updateFee(fee, true)){
                        
                        new DialogUI("Fee details has been updated successfully",
                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                        fws.restart();
                        close();
                    }else{
                        new DialogUI("Exception occurred while trying to update batch details",
                        DialogUI.ERROR_NOTIF, stackPane , null).show();
                        close();
                    }
                }else{
                
                    Fee newFee = new Fee("0", feeName.getText().trim(), paymentMode.getValue(),
                                            description.getText().trim(), defaultAmount.getText().trim(),
                                            academicYear.getValue());
                    
                    if(FeesQuery.updateFee(newFee, false)){
                        
                        new DialogUI("New batch has been added successfully",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                            fws.restart();
                        //SchoolAdministartion.batchClassesController.sws.restart();
                        close();
                    }else{
                        new DialogUI("Exception occurred while trying to add new batch",
                            DialogUI.ERROR_NOTIF, stackPane, null).show();
                    
                    }
                }
                
            }else{
                
                description.validate();
                feeName.validate();
                defaultAmount.validate();
                
                new DialogUI( "Ensure that mandatory fields are filled up before saving changes.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
            }
           
        });

        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(SIMS.MAIN_UI);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 180);
        show();
        
    }
}
