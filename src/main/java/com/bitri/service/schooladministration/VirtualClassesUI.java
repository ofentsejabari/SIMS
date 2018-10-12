package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.schooladministration.SchoolAdministartion.batchClassesController;

/**
 *
 * @author ofentse
 */
public class VirtualClassesUI extends BorderPane{
    
    public static CustomTableView<VirtualClass> table;
    public ClassWorkService classWorkService;
    private final JFXButton btn_add;

    public VirtualClassesUI() {
        
        getStyleClass().add("container");
        
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(5));
        
        classWorkService = new ClassWorkService();
        
        HBox toolbar = new HBox(5);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(2));
        container.setTop(toolbar);
        
        btn_add = new JFXButton("Create Class");
        btn_add.setTooltip(new Tooltip("Create new optional subject class"));
        btn_add.getStyleClass().addAll("btn-success", "btn-xs");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
             new UpdateVirtualClassDialog(null);
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);
        
        toolbar.getChildren().addAll(new HSpacer(), btn_add, btn_export);
        
        table = new CustomTableView<>();
        
        CustomTableColumn className = new CustomTableColumn("CLASS NAME");
        className.setPercentWidth(15);
        className.setCellValueFactory(new PropertyValueFactory<>("name"));
        className.setCellFactory(TextFieldTableCell.forTableColumn());
        className.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        setGraphic(new Label(ID));
                    }
                };
            }
        });
        
        CustomTableColumn classTeacher = new CustomTableColumn("TEACHER");
        classTeacher.setPercentWidth(15);
        classTeacher.setCellValueFactory(new PropertyValueFactory<>("teacherID"));
        classTeacher.setCellFactory(TextFieldTableCell.forTableColumn());
        classTeacher.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn subject = new CustomTableColumn("SUBJECT");
        subject.setPercentWidth(20);
        subject.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        subject.setCellFactory(TextFieldTableCell.forTableColumn());
        subject.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String cont, boolean empty) {
                        super.updateItem(cont, empty);
                        
                        if(!empty){
                            
                            setGraphic(new Label(cont));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn totalStudents = new CustomTableColumn("# OF STUDENTS");
        totalStudents.setPercentWidth(15);
        totalStudents.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        totalStudents.setCellFactory(TextFieldTableCell.forTableColumn());
        totalStudents.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String cont, boolean empty) {
                        super.updateItem(cont, empty);
                        
                        if(!empty){
                            
                            setGraphic(new Label(cont));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn cn = new CustomTableColumn("");
        cn.setPercentWidth(34.9);
        cn.setCellValueFactory(new PropertyValueFactory<>("classID"));
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
                            edit.setTooltip(new Tooltip("Edit class details"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateVirtualClassDialog(AdminQuery.getVirtualClassByID(ID));
                            });
                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                            view.setTooltip(new Tooltip("Preview class profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setDisable(true);
                            view.setOnAction((ActionEvent event) -> {
                                //new ClassProfileUI(AdminQuery.getVirtualClassByID(ID));
                               
                            });
                            
                            JFXButton delete = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 20));
                            delete.setTooltip(new Tooltip("Delete class"));
                            delete.getStyleClass().addAll("btn-danger", "btn-xs");
                            delete.setOnAction((ActionEvent event) -> {
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Class",
                                        "This operation will delete the class reference completely from the system. "
                                      + "Are you sure you want to proceed with this operation?",
                                        ButtonType.YES, ButtonType.CANCEL);
                                
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {

                                        if(AdminQuery.removeVirtualClass(ID)){
                                            new DialogUI("Class has been deleted successfully",
                                                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                                            batchClassesController.sws.restart();
                                        }else{
                                            new DialogUI("Exception occurred while trying to delete class from the system.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                    }else{alert.close();}
                                });
                                
                                
                            });
                            
                            if(!batchClassesController.selectedBatch.getStreamID().equalsIgnoreCase("Graduate")){
                                con.getChildren().addAll(view, edit,  delete);
                            }
                            
                            setGraphic(con);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        
        table.getTableView().getColumns().addAll(className, classTeacher, subject, totalStudents, cn);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        ProgressIndicator pi = new ProgressIndicator("Loading class data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(classWorkService.runningProperty());
        table.getTableView().itemsProperty().bind(classWorkService.valueProperty());
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(classWorkService));
        
        
        StackPane stackPane = new StackPane(table, pi);
        container.setCenter(stackPane);
        
        setCenter(container);
    }
    
    
    public class ClassListWork extends Task<ObservableList<VirtualClass>> {       
        @Override 
        protected ObservableList<VirtualClass> call() throws Exception {
            
            ObservableList<VirtualClass> data; 
          
            if(batchClassesController.selectedBatch != null){
                data = AdminQuery.getBatchVirtualClassList(batchClassesController.selectedBatch.getId());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            for (int i = 0; i < data.size(); i++) {
                
                data.get(i).setBatchID(SIMS.dbHandler.getVirtualClassStudentList(data.get(i).getClassID()).size()+"");
                data.get(i).setSubjectID(AdminQuery.getSubjectByID(data.get(i).getSubjectID()).getName());
                data.get(i).setTeacherID(EmployeeQuery.getEmployeeByID(data.get(i).getTeacherID()).getFullName());
            }
            
            
            Platform.runLater(() -> {
                if(batchClassesController.selectedBatch.getStreamID().equalsIgnoreCase("Graduate")){
                    btn_add.setDisable(true);
                }else{btn_add.setDisable(false);}
            });
            
            return data;
        }
       
    }

    public class ClassWorkService extends Service<ObservableList<VirtualClass>> {

        @Override
        protected Task createTask() {
            return new ClassListWork();
        }
    }
   
    
}
