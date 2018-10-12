package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.studentmanagement.StudentManagement.extraCurriculaController;

/**
 *
 * @author ofentse
 */
public class ECStaffMembers extends BorderPane{
    
    public ActivityMembersService ams = null;
    public static CustomTableView<ActivityMember> table;
    public CustomTextField search;
    public static ObservableList<ActivityMember> data = FXCollections.observableArrayList();
    public boolean all = false;
    

    public ECStaffMembers() {
        
        getStyleClass().add("container");
        ams = new ActivityMembersService();
        
        
        table = new CustomTableView<>();

        CustomTableColumn subjectName = new CustomTableColumn("FULLNAME");
        subjectName.setPercentWidth(24.9);
        subjectName.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        subjectName.setCellFactory(TextFieldTableCell.forTableColumn());
        subjectName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Label description = new Label(ID);
                        
                        if(!empty){
                            setGraphic(description); 
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn clss = new CustomTableColumn("DESIGNATION");
        clss.setPercentWidth(15);
        clss.setCellValueFactory(new PropertyValueFactory<>("type"));
        clss.setCellFactory(TextFieldTableCell.forTableColumn());
        clss.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label(type);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn batch = new CustomTableColumn("DEPARTMENT");
        batch.setPercentWidth(20);
        batch.setCellValueFactory(new PropertyValueFactory<>("activityID"));
        batch.setCellFactory(TextFieldTableCell.forTableColumn());
        batch.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label(type);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(40);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("id"));
        ctrl.setCellFactory(TextFieldTableCell.forTableColumn());
        ctrl.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        
                        if(!empty){
                            
                            JFXButton close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            close.setTooltip(new Tooltip("Close notification"));
                            close.getStyleClass().addAll("btn-danger", "btn-xs");
                            close.setOnAction((ActionEvent event) -> {
                                
                                JFXAlert alert = new JFXAlert(AlertType.CONFIRMATION,"Remove Member",
                                        "Are you sure you want to remove this member from the team?",
                                        ButtonType.YES, ButtonType.CANCEL);
                        
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {

                                        if(AdminQuery.deleteActivityMember(AdminQuery.getActivityMemberByID(type))){
                                            new DialogUI("Team member has been deregistered successfully",
                                                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                                            ams.restart();
                                        }else{
                                            new DialogUI("Exception occurred while trying to remove team member.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                    }else{alert.close();}
                                });
                                
                            });
                            
                            setGraphic(close);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll(subjectName, batch, clss, ctrl);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "text-error", 20));
        clear.getStyleClass().addAll("btn-xs", "btn-default");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_SEARCH, "text-bluegray", 18));
        src.getStyleClass().addAll("btn-xs", "btn-default");
        
        search.setRight(clear);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            String str = newValue.trim(); 
            try{
                
                for(ActivityMember s : table.getTableView().getItems()){
                    data.add(s);
                }
                
                if(str != null && str.length() > 0){
                    table.getTableView().getItems().clear();
                    for(ActivityMember student : data) {
                        
                        if(student.getMemberID().toLowerCase().contains(str.toLowerCase())){
                            if(!table.getTableView().getItems().contains(student)){
                                table.getTableView().getItems().add(student);
                            }
                        }
                    }
                    
                }else{
                    ams.restart();
                    data.clear();
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
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
        
        
        JFXButton btn_add_member = new JFXButton("Add Members");
        btn_add_member.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add_member.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_MULTIPLE_PLUS, "text-white", 18));
        btn_add_member.setOnAction((ActionEvent event) -> {
            
            if(StudentManagement.extraCurriculaController.selectedActivity != null){
                new UpdateActivityStaffMembersDialog(StudentManagement.extraCurriculaController.selectedActivity, ams);
            }
        });
        
        
        HBox searchBar = new HBox(5);
        searchBar.setStyle("-fx-padding:10 5 0 5; -fx-alignment:center;");
        searchBar.getChildren().addAll(search, new HSpacer(), btn_add_member);
        
        setTop(searchBar);
        
        ProgressIndicator pi = new ProgressIndicator("Loading members data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(ams.runningProperty());
        table.getTableView().itemsProperty().bind(ams.valueProperty());
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(ams));
        
        StackPane stackPane = new StackPane(table, pi);
        stackPane.setPadding(new Insets(5, 5, 0, 5));
        
        setCenter(stackPane);
        
        ams.start();
    }
    
    
    
    public class ActivityMembersWork extends Task<ObservableList<ActivityMember>> {       
        @Override 
        protected ObservableList<ActivityMember> call() throws Exception {
            
            ObservableList<ActivityMember> members = FXCollections.observableArrayList();
            
            ObservableList<ActivityMember> data = AdminQuery.getExtraCurriculaActivitiesMembers(extraCurriculaController.selectedActivity.getId(), "Staff", all); 
            
            for(ActivityMember ac: data){
                members.add(ac);
            }
            Platform.runLater(() -> {
                extraCurriculaController.setStaffTab("Staff ("+data.size()+")");
            });
            
            return members;
        }
       
    }

    public class ActivityMembersService extends Service<ObservableList<ActivityMember>> {

        @Override
        protected Task createTask() {
            return new ActivityMembersWork();
        }
    }
    
}
