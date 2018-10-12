
package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
public class AccessRights extends BorderPane{
    
    public static CustomTableView<UserRoles> usersTable;
    public static ObservableList<UserRoles> usersList = FXCollections.observableArrayList();
    
    public static UsersListWorkService usersListWork;
    
    public JFXButton btn_add, btn_refresh;

    public AccessRights() {
        
        setPadding(new Insets(10));
        
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("secondary-toolbar");
        setTop(toolbar);
                
        CustomTextField search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        usersListWork = new UsersListWorkService();
        
        
        btn_add = new JFXButton("Add User");
        btn_add.getStyleClass().add("jfx-tool-button");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 24));
        btn_add.setOnAction((ActionEvent event) -> {
            
        });
      
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().add("jfx-tool-button");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 24));
        btn_refresh.setOnAction((ActionEvent event) -> {
            usersListWork.restart();
            search.clear();
        });
        
        
        toolbar.getChildren().addAll(new HSpacer(), btn_add,  btn_refresh);
        
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
        
        CustomTableColumn userName = new CustomTableColumn("Description");
        userName.setPercentWidth(35);
        userName.setCellValueFactory(new PropertyValueFactory<>("description"));
        userName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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

        
        CustomTableColumn status = new CustomTableColumn("#");
        status.setPercentWidth(59.9);
        status.setCellValueFactory(new PropertyValueFactory<>("priviledges"));
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        usersTable.getTableView().getColumns().addAll(cnt, userName,status);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(usersListWork);
        usersTable.getTableView().setPlaceholder(ph);
        
        
        ProgressIndicator pi = new ProgressIndicator("Loading access rights data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(usersListWork.runningProperty());
        usersTable.getTableView().itemsProperty().bind(usersListWork.valueProperty());
        
        
        setCenter(new StackPane(usersTable, pi));
        
        usersListWork.start();
        usersListWork.restart();
        
        
    }
    
    public class UsersListWork extends Task<ObservableList<UserRoles>> {       
        @Override 
        protected ObservableList<UserRoles> call() throws Exception {
            
            Platform.runLater(() -> {               
                usersTable.getTableView().setPlaceholder(new VBox());
            });
            usersList  = AdminQuery.getUserRoles();
            for(int i=0;i<usersList.size();i++){
                usersList.get(i).setID(i+1+"");
                usersList.get(i).setPriviledges(i+1+"");
            }
            

            return usersList;
        } 
    }

    public class UsersListWorkService extends Service<ObservableList<UserRoles>> {

        @Override
        protected Task createTask() {
            return new UsersListWork();
        }
    }
}
