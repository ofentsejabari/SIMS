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
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Optional;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author MOILE
 */
public class EmployeeManagementView extends BorderPane{

    JFXButton btn_add, btn_refresh;
    CustomTextField search;
    StackPane stackpane;
    
    public static String filter = "ALL";

    public static CustomTableView<EmployeeModel> employeeTable;
    public static ObservableList<EmployeeModel> employeeList = FXCollections.observableArrayList();
    public static EmployeeListWorkService employeeListWork;
    private JFXTabPane employee_pane;
    public Tab all_emp,designations;
    
    public EmployeeManagementView() {
        
        setPadding(new Insets(5));
        getStyleClass().add("container");

        stackpane = new StackPane();
        
        BorderPane inner_bp = new BorderPane();
        inner_bp.setPadding(new Insets(10,5,5,5));

        employee_pane = new JFXTabPane();
        
        employee_pane.getStyleClass().add("jfx-tab-flatpane");
        all_emp = new Tab("All Employees");

        designations = new Tab("Employee Designations");
        designations.setContent(new EmployeeDesignationStage());
                
        employee_pane.getTabs().addAll(all_emp,designations);
        setCenter(employee_pane);
        
        employeeListWork = new EmployeeListWorkService();
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "text-error", 20));
        clear.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });

        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        export.setDisable(true);

        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_SEARCH, "text-bluegray", 20));
        src.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        search.setRight(clear);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String str = search.getText().trim(); 
            if(employeeTable.getTableView().getItems() != null){
                employeeTable.getTableView().getItems().clear();
                if(!str.equals("") && str.length() > 0){
                     
                     
                    for(EmployeeModel employee : employeeList) {
                        
                        if(employee.getEmployeeID().toLowerCase().contains(str.toLowerCase())){
                            if(!employeeTable.getTableView().getItems().contains(employee)){
                                employeeTable.getTableView().getItems().add(employee);
                            }
                        }
                        if(employee.getFirstName().toLowerCase().contains(str.toLowerCase())){
                            if(!employeeTable.getTableView().getItems().contains(employee)){
                                employeeTable.getTableView().getItems().add(employee);
                            }
                        }
                        
                        if(employee.getDesignation().toLowerCase().contains(str.toLowerCase())){
                            if(!employeeTable.getTableView().getItems().contains(employee)){
                                employeeTable.getTableView().getItems().add(employee);
                            }
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
        
        btn_add = new JFXButton("Add Employee");
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_PLUS, "text-white", 20));
        btn_add.setOnAction((ActionEvent event) -> {
           new UpdateEmployeeStage(null);
        });
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Employee List"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-bluegray", 20));
        btn_refresh.setOnAction((ActionEvent event) -> {
            employeeListWork.restart();
            search.clear();
        });
        
        
        HBox toolbar = new HBox();
        toolbar.setSpacing(5);
        toolbar.getStyleClass().add("");
        inner_bp.setTop(toolbar);

        toolbar.getChildren().addAll(search, new HSpacer(), btn_refresh, btn_add, export);
      
        //-------------------Search bar and table-------------------------------
        employeeTable = new CustomTableView<>();
       
        
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
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit Employee information"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateEmployeeStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                            view.setTooltip(new Tooltip("View Employee profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                new EmployeeProfileStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            view.setDisable(true);
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {

                                EmployeeModel sswm = EmployeeQuery.getEmployeeByID(ID);
                               
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Employee",
                                        "Are you sure you want to remove this Employee ("+sswm.getFullName()+")",
                                        ButtonType.YES, ButtonType.CANCEL);
                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()==ButtonType.OK){

                                    if(EmployeeQuery.deleteEmployee(sswm)) {
                                        new DialogUI("Employee removed successfully",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        employeeListWork.restart();
                                    }
                                    else{
                                         new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                }    
                            });
                            del.setDisable(true);
                            
                            actions.getChildren().addAll(edit,view,del);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });

        
        employeeTable.getTableView().getColumns().addAll( staffID, fname, designation, contacts,ctrl);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(employeeListWork);
        employeeTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(employeeListWork.runningProperty());
        employeeTable.getTableView().itemsProperty().bind(employeeListWork.valueProperty());
        
        stackpane.getChildren().addAll(employeeTable, pi);
        inner_bp.setCenter(stackpane);
        all_emp.setContent(inner_bp);
        
        setCenter(employee_pane);
        
        employeeListWork.start();
        employeeListWork.restart();
    }
 
 
    public class EmployeeListWork extends Task<ObservableList<EmployeeModel>> {
        @Override 
        protected ObservableList<EmployeeModel> call() throws Exception {
           
           if(!employeeList.isEmpty()){ 
               employeeList.clear();
           }
           
           ObservableList<EmployeeModel> employees  = EmployeeQuery.getEmployeeList();
           for(EmployeeModel em: employees){
               em.setDesignation(EmployeeQuery.getEmployeeDesignation(em.getDesignation()));
               em.setFirstName(em.getFullName());
               employeeList.add(em);
           }
            return employees;
        } 
    }

    
    public class EmployeeListWorkService extends Service<ObservableList<EmployeeModel>> {

        @Override
        protected Task createTask() {
            return new EmployeeListWork();
        }
    }
}
