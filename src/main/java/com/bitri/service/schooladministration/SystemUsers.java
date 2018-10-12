/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.schooladministration;

import com.bitri.access.*;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
public class SystemUsers extends BorderPane{
    
    public static CustomTableView<User> usersTable;
    public static ObservableList<User> usersList = FXCollections.observableArrayList();
    
    public static UsersListWorkService usersListWork;
    
    public JFXButton btn_add, btn_export, btn_refresh;

    public SystemUsers() {
        
        setPadding(new Insets(10));
        
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("secondary-toolbar");
        setTop(toolbar);
        
        
        CustomTextField search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        usersListWork = new UsersListWorkService();
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-error", 20));
        clear.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 20));
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
            //count.setText(usersList.size()+" User(s)");
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
        btn_add.getStyleClass().add("jfx-tool-button");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 24));
        btn_add.setOnAction((ActionEvent event) -> {
            
        });
        
        btn_export = new JFXButton("Export");
        btn_export.getStyleClass().add("jfx-tool-button");
        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 24));
        btn_export.setOnAction((ActionEvent event) -> {
           // new ExportMenu(btn_export);
        });
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().add("jfx-tool-button");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 24));
        btn_refresh.setOnAction((ActionEvent event) -> {
            usersListWork.restart();
            search.clear();
        });
        
        
        toolbar.getChildren().addAll(search, new HSpacer(), btn_add, btn_export, btn_refresh);
        
        //-------------------Search bar and table-------------------------------
        usersTable = new CustomTableView<>();
        
        CustomTableColumn cnt = new CustomTableColumn("");
        cnt.setPercentWidth(5);
        cnt.setCellValueFactory(new PropertyValueFactory<>("ID"));
        cnt.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
                        
                        
                        //Employee employee = LoginWindow.dbHandler.getEmployeeByID(ID);
                        if(!empty){
                            
                            //description.setText(employee.getFullName());
                            if(ID.equalsIgnoreCase("administrator")){//-- root account
                                
                                description.setText("Administrator");
                                description.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 18));
                            }else{
                                
                                //if(LoginWindow.USER_ROLES.hasManageUsersPriviledge()){
                                    description.setOnAction((ActionEvent event) -> {
                                        //new UpdateUser(stage, LoginWindow.dbHandler.getUserByID(ID));
                                    });
                                //}
                            }
                            setGraphic(description);
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });

        CustomTableColumn lastLogin = new CustomTableColumn("Last Login");
        lastLogin.setPercentWidth(30);
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
        
        CustomTableColumn status = new CustomTableColumn("Account Status");
        status.setPercentWidth(49.9);
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
                                Label approved = new Label("Active", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-success", 18));
                                actions.getChildren().addAll(approved);
                                
                            }else{
                                Label approved = new Label("Inactice", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-error", 18));
                                actions.getChildren().addAll(approved);
                            }
                            setGraphic(actions);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        usersTable.getTableView().getColumns().addAll(cnt, userName, lastLogin,status);
        
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
            
            Platform.runLater(() -> {               
                usersTable.getTableView().setPlaceholder(new VBox());
            });
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
