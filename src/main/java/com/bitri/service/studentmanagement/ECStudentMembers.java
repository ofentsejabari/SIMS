package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.Node;
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
public class ECStudentMembers extends BorderPane{
    
    public ActivityMembersService ams = null;
    public static CustomTableView<ActivityMember> table;
    public CustomTextField search;
    public static ObservableList<ActivityMember> data = FXCollections.observableArrayList();
    public boolean all = false;
    

    public ECStudentMembers() {
        
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
                        Hyperlink description = new Hyperlink(ID);
                        description.getStyleClass().add("tableLink");
                        
                        if(!empty){
                            setGraphic(description);                           
                            description.setOnAction((ActionEvent event) -> {
                                
                            });
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn clss = new CustomTableColumn("CLASS");
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
        
        CustomTableColumn batch = new CustomTableColumn("BATCH");
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
                new UpdateActivityStudentMembersDialog(StudentManagement.extraCurriculaController.selectedActivity, ams);
            }
        });
        
        
        JFXButton settings = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.FILTER, "text-white", 18));
        settings.setTooltip(new Tooltip("Filter table"));
        settings.getStyleClass().addAll("btn-primary", "btn-xs");
        settings.setOnAction((ActionEvent event) -> {
            
            openTableFilter(settings);
        });
        
        
        HBox searchBar = new HBox(5);
        searchBar.setStyle("-fx-padding:10 5 0 5; -fx-alignment:center;");
        searchBar.getChildren().addAll(search, settings, new HSpacer(), btn_add_member);
        
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
    
    

    private void openTableFilter(Node owner) {
        
        JFXPopup filterFXPopup  = new JFXPopup();
        BorderPane container = new BorderPane();
        container.setPrefWidth(200);
        
        filterFXPopup.setPopupContent(container);
        filterFXPopup.setAutoHide(true);
        container.setPrefHeight(160);
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("alert-info");
        
        Label title = new Label("Table Filters");
        title.getStyleClass().addAll("title-label");
        
        toolbar.getChildren().add(title);
        
        container.setTop(toolbar);
        
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(40);
        
        
        JFXToggleButton all_students = new JFXToggleButton();
        all_students.setText("Show All Students");
        all_students.setSelected(all);
        all_students.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            all = newValue;
            extraCurriculaController.ecws.restart();
        });
        
        gridPane.add(all_students, 0, 0);
        
        Label nt = new Label("Show students from all batches, including 'GRADUATE' batches.");
        nt.setWrapText(true);
        nt.getStyleClass().addAll("alert-warning");
        gridPane.add(nt, 0, 1);
        
        container.setCenter(gridPane);
        
        filterFXPopup.show(owner, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
    }
    
    public class ActivityMembersWork extends Task<ObservableList<ActivityMember>> {       
        @Override 
        protected ObservableList<ActivityMember> call() throws Exception {
            
            ObservableList<ActivityMember> members = FXCollections.observableArrayList();
            
            ObservableList<ActivityMember> data = AdminQuery.getExtraCurriculaActivitiesMembers(extraCurriculaController.selectedActivity.getId(), "Student", all); 
            
            for(ActivityMember ac: data){
                members.add(ac);
            }
            Platform.runLater(() -> {
                extraCurriculaController.setStudentsTab("Students ("+data.size()+")");
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
