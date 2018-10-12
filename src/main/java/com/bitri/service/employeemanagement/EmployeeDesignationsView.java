/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Optional;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author MOILE
 */
public class EmployeeDesignationsView extends BorderPane{
    
    JFXButton add,btn_refresh;
    CustomTableView<EmployeeDesignation> employeeDesignationTable;
    public static EmployeeDesignationListWorkService employeeDesignationListWork;
    public static ObservableList<EmployeeDesignation> employeeDesignationList = FXCollections.observableArrayList();
    public static StackPane stackPane;

    public EmployeeDesignationsView(){

        stackPane = new StackPane();
        employeeDesignationListWork = new EmployeeDesignationListWorkService();
        setPadding(new Insets(10));
        
        add = new JFXButton("Add Designation");
        add.setTooltip(new Tooltip("Add Designation"));
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS,"text-white", 20));
        add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setOnAction((ActionEvent event) -> {
            new UpdateDesignationEmployeeStage(null);
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT,"text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        export.setDisable(true);

        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Table"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "icon-default", 20));
        btn_refresh.setOnAction((ActionEvent event) -> {
           employeeDesignationListWork.restart();
        });

        HBox toolbar = new HBox(5);
        toolbar.setPadding(new Insets(0, 0, 5, 0));
        toolbar.getChildren().addAll( export, add, btn_refresh, new HSpacer());
        setTop(toolbar);

        //---------------create designation table---------------------
        employeeDesignationTable = new CustomTableView<>();

        CustomTableColumn description = new CustomTableColumn("DESIGNATION");
        description.setPercentWidth(25);
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn destinationType = new CustomTableColumn("TYPE");
        destinationType.setPercentWidth(25);
        destinationType.setCellValueFactory(new PropertyValueFactory<>("designationType"));
        destinationType.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn controls = new CustomTableColumn("");
        controls.setPercentWidth(49.5);
        controls.setCellValueFactory(new PropertyValueFactory<>("id"));
        controls.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            HBox actions = new HBox(10);
                            actions.setStyle("-fx-padding:0");
                            
                            JFXButton edit = new JFXButton();
                            edit.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PENCIL,"text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit Designation"));
                            edit.getStyleClass().addAll("btn-xs", "btn-default");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateDesignationEmployeeStage(ID);
                            });
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {

                                EmployeeDesignation employee = EmployeeQuery.getEmployeeDesignationListById(ID);
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Designation",
                                        "Are you sure you want to remove this Designation ("+employee.getDescription()+")",
                                        ButtonType.YES, ButtonType.CANCEL);
                               
                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()== ButtonType.OK){

                                    if(EmployeeQuery.deleteEmployeeDesignation(employee)){
                                        new DialogUI("Special need successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        employeeDesignationListWork.restart();

                                    }else{
                                         new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                    }
                                }    
                            });

                            actions.getChildren().addAll(edit,del);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        employeeDesignationTable.getTableView().getColumns().addAll(description,destinationType,controls);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(employeeDesignationListWork);
        employeeDesignationTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Designation Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(employeeDesignationListWork.runningProperty());
        employeeDesignationTable.getTableView().itemsProperty().bind(employeeDesignationListWork.valueProperty());

        stackPane.getChildren().addAll(employeeDesignationTable, pi);
        setCenter(stackPane);
        employeeDesignationListWork.start();
        employeeDesignationListWork.restart();
    }   



    public class EmployeeDesignationListWork extends Task<ObservableList<EmployeeDesignation>> {       
        
        @Override 
        protected ObservableList<EmployeeDesignation> call() throws Exception {

            employeeDesignationList = EmployeeQuery.getEmployeeDesignations();

            return employeeDesignationList;
        } 
    }

    
    public class EmployeeDesignationListWorkService extends Service<ObservableList<EmployeeDesignation>> {

        @Override
        protected Task createTask() {
            return new EmployeeDesignationListWork();
        }
    }
}