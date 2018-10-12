package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.schooladministration.SchoolAdministartion.streamClassesController;

/**
 *
 * @author ofentse
 */
public class StreamSubjectList extends BorderPane{
    
    public static CustomTableView<Subject> table;
    public SubjectWorkService subjectWorkService;

    public StreamSubjectList() {
        
        setPadding(new Insets(5));
        subjectWorkService = new SubjectWorkService();
        
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(2));
        setTop(toolbar);
        
        JFXButton btn_add = new JFXButton("Add Subjects");
        btn_add.getStyleClass().addAll("btn-xs", "btn-default");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            
            if(SchoolAdministartion.streamClassesController.selectedStream != null){
                new UpdateStreamSubjectsDialog(SchoolAdministartion.streamClassesController.selectedStream);
            }else{
                new DialogUI( "No stream selected yet...",
                    DialogUI.ERROR_NOTIF, SchoolAdministartion.ADMIN_MAN_STACK, null).show();
            }
            
        });
        
        toolbar.getChildren().addAll(new HSpacer(), btn_add);
        table = new CustomTableView<>();
        
        CustomTableColumn name = new CustomTableColumn("SUBJECT NAME");
        name.setPercentWidth(25);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Label description = new Label(ID);
                        description.getStyleClass().add("tableLink");
                        
                        if(!empty){
                            setGraphic(description);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn department = new CustomTableColumn("DEPARTMENT");
        department.setPercentWidth(20);
        department.setCellValueFactory(new PropertyValueFactory<>("departmentID"));
        department.setCellFactory(TextFieldTableCell.forTableColumn());
        department.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn totalStudents = new CustomTableColumn("ENROLLED");
        totalStudents.setPercentWidth(15);
        totalStudents.setCellValueFactory(new PropertyValueFactory<>("schoolID"));
        totalStudents.setCellFactory(TextFieldTableCell.forTableColumn());
        totalStudents.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn cn = new CustomTableColumn("");
        cn.setPercentWidth(39.9);
        cn.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        cn.setCellFactory(TextFieldTableCell.forTableColumn());
        cn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        HBox con = new HBox(10);
                        
                        if(!empty){
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit subject details"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {

                                new UpdateSubjectDialog(AdminQuery.getSubjectByID(ID));

                            });
                            
                            JFXButton close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            close.setTooltip(new Tooltip("Delete Subject"));
                            close.getStyleClass().addAll("btn-danger", "btn-xs");
                            close.setOnAction((ActionEvent event) -> {
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Subject",
                                        "Are you sure you want to remove this subject from the list?",
                                        ButtonType.YES, ButtonType.CANCEL);
                                
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {

                                        if(AdminQuery.removeSubject(ID, streamClassesController.selectedStream.getStreamID())){
                                            new DialogUI("Subject has been removed from the list successfully",
                                                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                                            subjectWorkService.restart();
                                        }else{
                                            new DialogUI("Exception occurred while trying to remove subject from the list.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                    }else{alert.close();}
                                });
                            });
                            
                            con.getChildren().addAll(edit, close);
                            setGraphic(con);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll(name, department, totalStudents, cn);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        ProgressIndicator pi = new ProgressIndicator("Loading subject data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(subjectWorkService.runningProperty());
        table.getTableView().itemsProperty().bind(subjectWorkService.valueProperty());
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(subjectWorkService));
        
        StackPane stackPane = new StackPane(table, pi);
        
        setCenter(stackPane);
    }
    
    
    public class SubjectListWork extends Task<ObservableList<Subject>> {       
        @Override 
        protected ObservableList<Subject> call() throws Exception {
            
            ObservableList<Subject> data; 
          
            if(streamClassesController.selectedStream != null){
                data = AdminQuery.getStreamSubjectsList(streamClassesController.selectedStream.getStreamID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            for(Subject sbj: data){
                sbj.setDepartmentID(AdminQuery.getDepartmentByID(sbj.getDepartmentID()).getDepartmentName());
                //-- total number of students enrolled in this subject --
                if(sbj.getType().equalsIgnoreCase("1")){//-- core --
                    
                    sbj.setSchoolID(""+AdminQuery.getStreamStudentCoreList(streamClassesController.selectedStream.getStreamID()).size());
                    
                }else{//-- optional --
                    sbj.setSchoolID(""+AdminQuery.getStreamStudentOptionalList(streamClassesController.selectedStream.getStreamID(), sbj.getSubjectID()).size());
                }
            }
            return data;
        }
    }

    public class SubjectWorkService extends Service<ObservableList<Subject>> {

        @Override
        protected Task createTask() {
            return new SubjectListWork();
        }
    }
 
}
