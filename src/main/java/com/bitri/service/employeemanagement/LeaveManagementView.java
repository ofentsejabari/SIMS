/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.studentmanagement.Student;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;

/**
 *
 * @author MOILE
 */
public class LeaveManagementView extends BorderPane{

    JFXButton btn_addDesignation, btn_apply, btn_refresh, btn_export;
    CustomTextField search;
    Label count;
    StackPane stackpane;
    
    public static String filter = "ALL";
    
  //  public static UpdateEmployeeProfile profileStage;
    public static CustomTableView<EmployeeModel> employeeLeaveTable;
    public static ObservableList<EmployeeModel> employeeList = FXCollections.observableArrayList();
    public static EmployeeListWorkService employeeListWork;
    
    public LeaveManagementView() {
        
        setPadding(new Insets(10));
        
        count = new Label("");
        stackpane = new StackPane();
        employeeListWork = new EmployeeListWorkService();
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-error", 20));
        clear.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 20));
        src.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        search.setRight(clear);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String str = search.getText().trim(); 
            
            if(employeeLeaveTable.getTableView().getItems() != null){
                ObservableList<Student>  studentList  = SIMS.dbHandler.getStudentList(filter);
                employeeLeaveTable.getTableView().getItems().clear();
            
                if(str != null && str.length() > 0){
                    
                    for(EmployeeModel employee : employeeList) {
                        
                        if(employee.getEmployeeID().toLowerCase().contains(str.toLowerCase())){
                            employeeLeaveTable.getTableView().getItems().add(employee);
                        }

                        if(employee.getFirstName().toLowerCase().contains(str.toLowerCase())){
                            employeeLeaveTable.getTableView().getItems().add(employee);
                        }
                        
                        if(employee.getDesignation().toLowerCase().contains(str.toLowerCase())){
                            employeeLeaveTable.getTableView().getItems().add(employee);
                        } 
                    }
                    
                }else{
                    employeeListWork.restart();
                }
            }
        });
        
        search.setRight(src);
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!newValue.trim().equalsIgnoreCase("")){
                search.setRight(clear);
            }else{
                search.setRight(src);
            }
        });
        
        btn_apply = new JFXButton("Apply");
        btn_apply.getStyleClass().addAll("btn-xs", "btn-success");
        btn_apply.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 20));
        btn_apply.setOnAction((ActionEvent event) -> {
             //after login has been attended to can an employee apply for leave
             new AddEmployeeLeaveApplication();
        });
        
        btn_export = new JFXButton("Export");
        btn_export.getStyleClass().addAll("btn-xs", "btn-default");
        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 20));
        btn_export.setOnAction((ActionEvent event) -> {
            
        });
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Employee List"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 20));
        btn_refresh.setOnAction((ActionEvent event) -> {
            employeeListWork.restart();
            search.clear();
        });
        
        HBox toolbar = new HBox();
        toolbar.setSpacing(2);
        toolbar.getStyleClass().add("secondary-toolbar");
        setTop(toolbar);
        
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        toolbar.getChildren().addAll(search, new HSpacer(),btn_apply, btn_export, btn_refresh);
      
        //-------------------Search bar and table-------------------------------
        employeeLeaveTable = new CustomTableView<>();
       
        CustomTableColumn staffID = new CustomTableColumn("EMPLOYEE ID");
        staffID.setPercentWidth(15);
        staffID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        staffID.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn fname = new CustomTableColumn("EMPLOYEE NAME");
        fname.setPercentWidth(20);
        fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        fname.setCellFactory(TextFieldTableCell.forTableColumn());
        fname.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn designation = new CustomTableColumn("DESIGNATION");
        designation.setPercentWidth(20);
        designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        designation.setCellFactory(TextFieldTableCell.forTableColumn());
        designation.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn contacts = new CustomTableColumn("EMAIL ADDRESS");
        contacts.setPercentWidth(20);
        contacts.setCellValueFactory(new PropertyValueFactory<>("email"));
        contacts.setCellFactory(TextFieldTableCell.forTableColumn());
        contacts.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        if(!empty){
                            
                            setGraphic(new Label(status));
                           
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        //-------------
        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(39.9);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        ctrl.setCellFactory(TextFieldTableCell.forTableColumn());
        ctrl.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        HBox actions = new HBox(10);
                        actions.setStyle("-fx-padding:0");
                        
                        if(!empty){
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit Employee information"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateEmployeeStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                            view.setTooltip(new Tooltip("View Employee profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                new EmployeeProfileStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            
                            actions.getChildren().addAll(view, edit);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });
        
        employeeLeaveTable.getTableView().getColumns().addAll( staffID, fname, designation, contacts,ctrl);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        employeeLeaveTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(employeeListWork.runningProperty());
        employeeLeaveTable.getTableView().itemsProperty().bind(employeeListWork.valueProperty());
        
        stackpane.getChildren().addAll(employeeLeaveTable, pi);
        setCenter(stackpane);
        
        employeeListWork.start();
        employeeListWork.restart();
    }   
    
  
 
 
 public class EmployeeListWork extends Task<ObservableList<EmployeeModel>> {       
        @Override 
        protected ObservableList<EmployeeModel> call() throws Exception {
            
            Platform.runLater(() -> {               
                employeeLeaveTable.getTableView().setPlaceholder(new VBox());
            });
            
           employeeList  = EmployeeQuery.getEmployeeList();
           for(EmployeeModel em: employeeList){
               em.setFirstName(em.getFullName());
               em.setDesignation(EmployeeQuery.getEmployeeDesignation(em.getDesignation()));
           }
                        
            Platform.runLater(() -> {
                employeeLeaveTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder());
            });

            return employeeList;
        } 
    }

    
    public class EmployeeListWorkService extends Service<ObservableList<EmployeeModel>> {

        @Override
        protected Task createTask() {
            return new EmployeeListWork();
        }
    }
}
