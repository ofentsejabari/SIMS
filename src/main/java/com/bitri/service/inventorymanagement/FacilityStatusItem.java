/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.inventorymanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.InventoryQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 * @author MOILE
 */
public class FacilityStatusItem extends BorderPane{

    public static CustomTableView<FacilitiesStatus> facilityStatusTable;
    public FacilityStatusWorkService facilityStatusWork;
    private final StackPane stackPane;
    public String filter = "";
    
    public static ObservableList<FacilitiesStatus> facilityStatusList = FXCollections.observableArrayList();
    
    public FacilityStatusItem() {
        
        facilityStatusWork = new FacilityStatusWorkService();
    
        getStyleClass().add("container");
        stackPane = new StackPane();
        
//        setPadding(new Insets(10));
        
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("secondary-toolbar");
        setTop(toolbar);
                
        JFXButton btn_refresh = new JFXButton("Refresh");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_refresh.setOnAction((ActionEvent event) -> {
            facilityStatusWork.restart();
        });
        JFXButton btn_add = new JFXButton("Add");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_add.setOnAction((ActionEvent event) -> {
             new AddFacilityResources(filter).show();
        });
        
        btn_refresh.getStyleClass().add("jfx-tool-button");
        btn_add.getStyleClass().add("jfx-tool-button");
        toolbar.getChildren().addAll(new HSpacer(), btn_refresh, btn_add);
        
        /*
            CREATE facilityType TABLE
        */
        facilityStatusTable = new CustomTableView<>();
        
        CustomTableColumn facilityStatusID = new CustomTableColumn("#");
        facilityStatusID.setPercentWidth(4.9);
        facilityStatusID.setCellValueFactory(new PropertyValueFactory<>("facilitiesStatusID"));
        facilityStatusID.setCellFactory(TextFieldTableCell.forTableColumn());
        facilityStatusID.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            setGraphic(new Label(ID));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn facilityStatusResource = new CustomTableColumn("RESOURCE");
        facilityStatusResource.setCellValueFactory(new PropertyValueFactory<>("facilitiesResourceID"));
        
        facilityStatusResource.setPercentWidth(70);
        facilityStatusResource.setCellFactory(TextFieldTableCell.forTableColumn());
        facilityStatusResource.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
        
        @Override 
        public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            setGraphic(new Label(InventoryQuery.getResourceName(ID)));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn facilityStatusName = new CustomTableColumn("FACILITY");
        facilityStatusName.setCellValueFactory(new PropertyValueFactory<>("facilitiesID"));
        facilityStatusName.setCellFactory(TextFieldTableCell.forTableColumn());
        facilityStatusName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            setGraphic(new Label(InventoryQuery.getFacilitiesName(ID).get(0)));
                        }else{ setGraphic(null); }
                    }
                };
            }
        }); 
        
        CustomTableColumn facilityStatusAvailable = new CustomTableColumn("AVAILABLE");
        facilityStatusAvailable.setPercentWidth(15);
        facilityStatusAvailable.setCellValueFactory(new PropertyValueFactory<>("facilitiesStatusAvailable"));
        facilityStatusAvailable.setCellFactory(TextFieldTableCell.forTableColumn());
        facilityStatusAvailable.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            setGraphic(new Label(ID));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn facilityStatusDamage = new CustomTableColumn("DAMAGED");
        facilityStatusDamage.setPercentWidth(15);
        facilityStatusDamage.setCellFactory(TextFieldTableCell.forTableColumn());
        facilityStatusDamage.setCellValueFactory(new PropertyValueFactory<>("facilitiesStatusDamage"));
        facilityStatusDamage.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            setGraphic(new Label(ID));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        
        
        facilityStatusTable.getTableView().getColumns().addAll(facilityStatusID
                ,facilityStatusResource,facilityStatusAvailable,facilityStatusDamage);
        VBox.setVgrow(facilityStatusTable, Priority.ALWAYS);
        
        //-- SET DATA
        facilityStatusTable.getTableView().setItems(InventoryQuery.facilitiesStatusList("ALL"));
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        facilityStatusTable.getTableView().setPlaceholder(ph);
        
        
        ProgressIndicator pi = new ProgressIndicator("Loading users data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(facilityStatusWork.runningProperty());
        facilityStatusTable.getTableView().itemsProperty().bind(facilityStatusWork.valueProperty());
        
        stackPane.getChildren().addAll(pi,facilityStatusTable);
        setCenter(stackPane);
        
        facilityStatusWork.start();
        facilityStatusWork.restart();
        
    }
    
    
    public class FacilityStatusWork extends Task<ObservableList<FacilitiesStatus>> {       
        @Override 
        protected ObservableList<FacilitiesStatus> call() throws Exception {
            
            Platform.runLater(() -> {               
                facilityStatusTable.getTableView().setPlaceholder(new VBox());
            });
            facilityStatusList  =  InventoryQuery.getFacilitiesStatus(filter);
            for(int i=0;i<facilityStatusList.size();i++){
                facilityStatusList.get(i).setFacilitiesStatusID(i+1+"");
            }
                        
            Platform.runLater(() -> {  
                //count.setText(studentList.size()+" Student(s)");
                facilityStatusTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder());
            });

            return facilityStatusList;
        } 
    }

    public class FacilityStatusWorkService extends Service<ObservableList<FacilitiesStatus>> {

        @Override
        protected Task createTask() {
            return new FacilityStatusWork();
        }
    }
    
}
