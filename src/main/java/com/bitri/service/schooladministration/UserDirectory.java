/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.employeemanagement.EmployeeModel;
import com.jfoenix.controls.JFXButton;
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

/**
 *
 * @author ofentse
 */
public class UserDirectory extends BorderPane{
    
    public static CustomTableView<User> usersTable;
    public static ObservableList<User> usersList = FXCollections.observableArrayList();
    
    public static UsersListWorkService usersListWork;
    
    public JFXButton btn_add, btn_refresh;

    public UserDirectory() {
        
        setPadding(new Insets(5));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-info", "panel-heading");
        toolbar.setStyle("-fx-padding:5 5 5 0; -fx-border-color:#fff");
        setTop(toolbar);
        
        
        CustomTextField search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        usersListWork = new UsersListWorkService();
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "text-error", 20));
        clear.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_SEARCH, "text-bluegray", 20));
        src.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
      
        search.setRight(clear);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String str = search.getText().trim(); 
            
            if(usersTable.getTableView().getItems() != null){
                ObservableList<User>  studentList  = SIMS.dbHandler.getUsers();
                usersTable.getTableView().getItems().clear();
            
                if(str != null && str.length() > 0){
                    
                    for(User user : studentList) {
                        
                        if(user.getUserID().toLowerCase().contains(str.toLowerCase())){
                            usersTable.getTableView().getItems().add(user);
                        }
                    }
                    
                }else{
                    usersListWork.restart();
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
        
        btn_add = new JFXButton("Add User");
        btn_add.getStyleClass().addAll("btn", "btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {

        });
        btn_add.setDisable(true);

        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);

        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn", "btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "icon-default", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            usersListWork.restart();
            search.clear();
        });

        
        toolbar.getChildren().addAll(search, new HSpacer(), btn_refresh, btn_add, btn_export);
        
        //-------------------Search bar and table-------------------------------
        usersTable = new CustomTableView<>();

        CustomTableColumn userName = new CustomTableColumn("User Name");
        userName.setPercentWidth(25);
        userName.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Hyperlink description = new Hyperlink(ID);
                        description.getStyleClass().add("tableLink");
                        
                        if(!empty){
                            
                            //description.setText(employee.getFullName());
                            if(ID.equalsIgnoreCase("administrator")){//-- root account
                                
                                description.setText("Administrator");
                                description.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-bluegray", 18));
                            }else{
                                
                                EmployeeModel employee = EmployeeQuery.getEmployeeByID(ID);
                                description.setText(employee.getFullName());
                                description.setOnAction((ActionEvent event) -> {
                                });
                            }
                            setGraphic(description);
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });

        CustomTableColumn lastLogin = new CustomTableColumn("Last Login");
        lastLogin.setPercentWidth(15);
        lastLogin.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
        lastLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        lastLogin.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            Label approved = new Label(ID);
                            setGraphic(approved);
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        CustomTableColumn status = new CustomTableColumn("Status");
        status.setPercentWidth(10);
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        HBox actions = new HBox();
                        actions.setStyle("-fx-padding:0");
                        actions.setSpacing(5);
                        
                        if(!empty){
                            
                            if(status.equals("1")){
                                Label approved = new Label("Active", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT, "text-success", 20));
                                actions.getChildren().addAll(approved);
                                
                            }else{
                                Label approved = new Label("Inactice", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_OFF, "text-error", 20));
                                actions.getChildren().addAll(approved);
                            }
                            setGraphic(actions);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(49.9);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("ID"));
        ctrl.setCellFactory(TextFieldTableCell.forTableColumn());
        ctrl.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String status, boolean empty) {
                        super.updateItem(status, empty);
                        HBox actions = new HBox();
                        actions.setStyle("-fx-padding:0");
                        actions.setSpacing(5);
                        
                        if(!empty){
                            
                            JFXButton close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            close.setTooltip(new Tooltip("Remove Account"));
                            close.getStyleClass().addAll("btn-danger", "btn-xs");
                            close.setOnAction((ActionEvent event) -> {
                              
                            });
                            close.setDisable(true);
                            
                            actions.getChildren().add(close);
                            setGraphic(actions);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });

        usersTable.getTableView().getColumns().addAll(userName, lastLogin, status, ctrl);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(usersListWork);
        usersTable.getTableView().setPlaceholder(ph);

        ProgressIndicator pi = new ProgressIndicator("Loading users data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(usersListWork.runningProperty());
        usersTable.getTableView().itemsProperty().bind(usersListWork.valueProperty());

        setCenter(new StackPane(usersTable, pi));
        
        usersListWork.start();
        usersListWork.restart();

    }


    public class UsersListWork extends Task<ObservableList<User>> {       
        @Override 
        protected ObservableList<User> call() throws Exception {

            usersList  = SIMS.dbHandler.getUsers();
            for(int i=0;i<usersList.size();i++){
                usersList.get(i).setID(i+1+"");
            }
            
            return usersList;
        } 
    }



    public class UsersListWorkService extends Service<ObservableList<User>> {

        @Override
        protected Task createTask() {
            return new UsersListWork();
        }
    }
}
