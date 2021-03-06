/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement.control;

import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.inventorymanagement.AssetAllocationList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.bitri.service.inventorymanagement.StudentAllocatedResourceDialog.studentAllocationWork;

/**
 * FXML Controller class
 *
 * @author MOILE
 */
public class AssetAllocationController implements Initializable {

    @FXML
    private BorderPane borderPane;
   
    @FXML
    private JFXButton btn_export,buttonRefresh;
    @FXML
    private VBox listview;
    @FXML
    private Label totalStreams;
    @FXML
    private JFXListView<String> stream_ListView;
    @FXML
    private VBox streamClasses;
    
    @FXML
    private HBox streamToolBar;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        ObservableList<String> items = AdminQuery.getStreamNames();
        
        totalStreams.setText(""+items.size());
        
        stream_ListView.setItems(items);
        AssetAllocationList asset = new  AssetAllocationList();
        streamClasses.getChildren().add(asset);
        stream_ListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                AssetAllocationList.stream_ID=newValue;
                asset.studentAllocationWork.restart();
         });
        
        JFXButton btn_refresh = new JFXButton();
        btn_refresh.getStyleClass().add("jfx-tool-button");
        streamToolBar.getChildren().add(1, btn_refresh);
        
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_refresh.setOnAction((ActionEvent event) -> {
                ObservableList<String> items1 = AdminQuery.getStreamNames();
                totalStreams.setText(""+items1.size());
                stream_ListView.setItems(items1);
                AssetAllocationList.stream_ID="ALL";
                try{
                    studentAllocationWork.restart();
                }
                catch(Exception e){
                    System.out.println(e);

                }
        
        });
        
    }    
}    
    
