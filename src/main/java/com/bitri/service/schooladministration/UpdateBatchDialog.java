package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
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
public class UpdateBatchDialog extends JFXDialog{

    private JFXTextField description;
    private JFXComboBox<String> start, end, stream;
        
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateBatchDialog(Batch batch) {
                    
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
        
        description = new JFXTextField();
        description.setEditable(false);
        description.setPromptText("Batch Name");
        description.setPrefWidth(360);
        description.setLabelFloat(true);
        contentGrid.add(description, 0, 0, 2, 1);
        
        CCValidator.setFieldValidator(description, "Batch name required.");
                
        stream = new JFXComboBox<>(AdminQuery.getAvailableStreamNames());
        stream.setPromptText("Current Stream");
        stream.setLabelFloat(true);
        stream.setPrefWidth(360);
        stream.getItems().add("GRADUATE");
        new AutoCompleteComboBoxListener(stream);
        contentGrid.add(stream, 0, 1, 2, 1);
        
        start = new JFXComboBox<>(FXCollections.observableArrayList("2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"));
        start.setPromptText("Start Year");
        start.setLabelFloat(true);
        start.setPrefWidth(360);
        new AutoCompleteComboBoxListener(start);
        contentGrid.add(start, 0, 2);
        
        
        end = new JFXComboBox<>(FXCollections.observableArrayList("2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"));
        end.setPromptText("End Year");
        end.setLabelFloat(true);
        end.setPrefWidth(360);
        new AutoCompleteComboBoxListener(end);
        contentGrid.add(end, 1, 2);
        
        
        start.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(end.getValue() != null && !"".equals(end.getValue()) && !"".equals(newValue)){
                if(Integer.parseInt(start.getValue()) < Integer.parseInt(end.getValue())){
                    description.setText("BATCH "+ newValue+" - "+end.getValue());
                }else{
                    end.setValue(""+(Integer.parseInt(start.getValue())+1));
                }
                
            }else{
                description.setText("");
            }
        });
        
        
        end.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(start.getValue() != null && !"".equals(start.getValue()) && !"".equals(newValue)){
                if(Integer.parseInt(start.getValue()) < Integer.parseInt(end.getValue())){
                    description.setText("BATCH "+ start.getValue()+" - "+newValue);
                }else{
                    end.setValue(""+(Integer.parseInt(start.getValue())+1));
                }
                
            }else{
                description.setText("");
            }
        });
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Create Batch");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(batch != null){
            
            description.setText(batch.getDescription());
            start.setValue(batch.getStart());
            end.setValue(batch.getEnd());
            if(!batch.getStreamID().equalsIgnoreCase("GRADUATE")){
                stream.setValue(AdminQuery.getStreamByID(batch.getStreamID()).getDescription());
            }else{
               stream.setValue("GRADUATE"); 
            }
            title.setText("Update Batch");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Stream"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!"".equals(description.getText().trim())){
                
                if(batch != null){
                    
                    batch.setDescription(description.getText().trim());
                    batch.setStart(start.getValue().trim());
                    batch.setEnd(end.getValue().trim());
                    
                    if(!stream.getValue().equalsIgnoreCase("GRADUATE")){
                        batch.setStreamID(AdminQuery.getStreamByName(stream.getValue()).getStreamID());
                    }else{
                       batch.setStreamID("GRADUATE"); 
                    }
                    
                    
                    
                    if(!AdminQuery.isBatchExists(batch)){
                        
                        if(AdminQuery.updateBatch(batch, true)){
                            new DialogUI("Batch details has been updated successfully",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                            SchoolAdministartion.batchClassesController.sws.restart();
                            close();
                        }else{
                            new DialogUI("Exception occurred while trying to update batch details",
                            DialogUI.ERROR_NOTIF, stackPane , null).show();
                            close();
                        }
                    }else{
                        new DialogUI("No changes made.",
                        DialogUI.INFORMATION_NOTIF, PARENT_STACK_PANE, null).show();
                    }
                }else{
                
                    Batch strm = new Batch("0", description.getText().trim(), start.getValue(), end.getValue(),
                                           (!(stream.getValue().equalsIgnoreCase("GRADUATE"))? AdminQuery.getStreamByName(stream.getValue()).getStreamID():"GRADUATE"));
                    
                    if(!AdminQuery.isBatchExists(strm)){
                        
                        if(AdminQuery.updateBatch(strm, false)){
                            new DialogUI("New batch has been added successfully",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();

                            SchoolAdministartion.batchClassesController.baseClassesList.classWorkService.restart();
                            close();
                        }else{
                            new DialogUI("Exception occurred while trying to add new batch",
                            DialogUI.ERROR_NOTIF, stackPane, null).show();
                        }
                    }else{
                           new DialogUI("Batch name already in use. Change the start and end year.",
                            DialogUI.ERROR_NOTIF, stackPane, null).show();
                       }
                }
            }else{
                description.validate();
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
