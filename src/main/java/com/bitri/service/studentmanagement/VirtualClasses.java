package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.schooladministration.Batch;
import com.bitri.service.schooladministration.UpdateVirtualClassDialog;
import com.bitri.service.schooladministration.VirtualClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author ofentse
 */
public class VirtualClasses extends BorderPane{

    public static JFXListView<HBox> class_listView;
    public static StudentClassWorkService cws = null;
    public static CustomTableView<Student> table;
    
    VirtualClass selectedClass = null;
    Batch selectedBatch = null;
    public static int selectedIndex = 0, selectedBatchIndex = 0;
    
    public static ObservableList<Student> data = FXCollections.observableArrayList();
    
    public BatchClassListService bcls = new BatchClassListService();
    private final JFXComboBox<String> batch;
    
    public VirtualClasses() {
        
        setPadding(new Insets(15, 5, 5, 5));
        cws = new StudentClassWorkService();
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 0));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-info", "panel-heading");
        
        ssn_center.setTop(toolbar);
        
        batch = new JFXComboBox<>(AdminQuery.getBatchNames());
        batch.setPromptText("Select Batch");
        batch.setLabelFloat(true);
        batch.setPrefWidth(180);
        HBox.setHgrow(batch, Priority.ALWAYS);
        
        CustomTextField search = new CustomTextField();
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
                
                for(Student s : table.getTableView().getItems()){
                    data.add(s);
                }
                
                if(str != null && str.length() > 0){
                    table.getTableView().getItems().clear();
                    for(Student student : data) {
                        
                        if(student.getStudentID().toLowerCase().contains(str.toLowerCase()) ||
                           student.getFirstName().toLowerCase().contains(str.toLowerCase()) ||
                           student.getLastName().toLowerCase().contains(str.toLowerCase())){
                            if(!table.getTableView().getItems().contains(student)){
                                table.getTableView().getItems().add(student);
                            }
                        }
                    }
                    
                }else{
                    cws.restart();
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
        
        
        JFXButton add = new JFXButton("Add Students");
        add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
            if(selectedClass != null){
                new UpdateVirtualClassStudentsDialog(selectedClass, selectedBatch, cws);
            }
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);

        batch.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            selectedBatch  = AdminQuery.getBatchByName(batch.getItems().get(newValue.intValue()));
            selectedBatchIndex = newValue.intValue();
            bcls.restart();
        });
        
        HBox.setHgrow(batch, Priority.ALWAYS);
         
        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setTooltip(new Tooltip("Refresh batch data"));
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            ObservableList<String> batches = AdminQuery.getBatchNames();

            if(!batches.isEmpty()){
                batch.setItems(batches);
                if(selectedBatch != null && !selectedBatch.getDescription().equalsIgnoreCase("")){
                    batch.setValue(selectedBatch.getDescription());
                }else{
                    batch.setValue(batches.get(selectedBatchIndex));
                }
                bcls.restart();
            }
        });
        
        toolbar.getChildren().addAll(search, new HSpacer(), refresh, add, btn_export);
        
        setCenter(ssn_center);
        
        class_listView = new JFXListView<>();
        VBox.setVgrow(class_listView, Priority.ALWAYS);
        class_listView.getStyleClass().add("jfx-custom-list");
        class_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                Label lb = (Label)class_listView.getSelectionModel().getSelectedItem().getChildren().get(0);
                selectedClass = AdminQuery.getVirtualClassByName(lb.getText(), selectedBatch.getId());
                
                selectedIndex = newValue.intValue();
                cws.restart();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        class_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(bcls));
        
        
        HBox header = new HBox(batch);
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");
        header.setAlignment(Pos.BOTTOM_CENTER);
        //header.setStyle("-fx-padding:2");
        
        BorderPane leftPanel = new BorderPane(class_listView);
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");
        leftPanel.setTop(header);

        HBox con = new HBox(leftPanel);
        con.setStyle("-fx-padding:0 5 0 0;");
        
        setLeft(con);
        
        //----------------------------------------------------------------------
        table = new CustomTableView<>();
        
        CustomTableColumn admissionNumber = new CustomTableColumn("ID #");
        admissionNumber.setPercentWidth(20);
        admissionNumber.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        admissionNumber.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            Label lb = new Label(ID);
                            setGraphic(lb);
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        CustomTableColumn fname = new CustomTableColumn("NAME");
        fname.setPercentWidth(25);
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
        
        CustomTableColumn className = new CustomTableColumn("CLASS");
        className.setPercentWidth(15);
        className.setCellValueFactory(new PropertyValueFactory<>("status"));
        className.setCellFactory(TextFieldTableCell.forTableColumn());
        className.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        
        CustomTableColumn gender = new CustomTableColumn("GENDER");
        gender.setPercentWidth(15);
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.setCellFactory(TextFieldTableCell.forTableColumn());
        gender.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        
        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(24.9);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("studentID"));
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
                            
                            JFXButton delete = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 16));
                            delete.setTooltip(new Tooltip("Remove student from class"));
                            delete.getStyleClass().addAll("btn-danger", "btn-xs");
                            delete.setOnAction((ActionEvent event) -> {
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Student",
                                        "Are you sure you want to remove student from class?",
                                        ButtonType.YES, ButtonType.CANCEL);
                                
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {

                                        if(StudentQuery.removeStudentFromVirtualClass(ID, selectedClass.getClassID())){
                                            new DialogUI("Student has been removed successfully",
                                                        DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                                            cws.restart();
                                        }else{
                                            new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                    }else{alert.close();}
                                });
                                
                                
                            });
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL,"text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit student information"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                
                                new UpdateStudentProfile(SIMS.dbHandler.getStudentByID(ID));
                            });
                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                            view.setTooltip(new Tooltip("View student profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                 new StudentProfileStage(SIMS.dbHandler.getStudentByID(ID));
                            });
                            view.setDisable(true);
                            
                            actions.getChildren().addAll(view, edit, delete);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll(admissionNumber, fname, gender, className, ctrl);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(cws));
                
        ProgressIndicator pi = new ProgressIndicator("Loading data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(cws.runningProperty());
        table.getTableView().itemsProperty().bind(cws.valueProperty());
        
        StackPane stackPane = new StackPane(table, pi);
        ssn_center.setCenter(stackPane);
        
        bcls.start();
        bcls.restart();
        
        cws.start();
        
        if(!batch.getItems().isEmpty()){ 
            if(selectedBatch != null){
                batch.setValue(selectedBatch.getDescription());
            }
        }
    }

    
    
    public class BatchClassListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            
            ObservableList<VirtualClass> baseClass = AdminQuery.getBatchVirtualClassList(selectedBatch.getId());
        
            for(VirtualClass vc: baseClass){
                
                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Update class"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateVirtualClassDialog(AdminQuery.getVirtualClassByID(vc.getClassID()));
                });
                
                JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                view.setTooltip(new Tooltip("View class"));
                view.getStyleClass().addAll("btn-default", "btn-xs");
                view.setOnAction((ActionEvent event) -> {
                    new VirtualClassProfileUI(AdminQuery.getVirtualClassByID(vc.getClassID()));
                });
                view.setDisable(true);
                
                HBox cn = new HBox(new Label(vc.getName()), new HSpacer(), view, edit);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
                        
            Platform.runLater(() -> {
                try {
                    class_listView.setItems(data);
                    if(!data.isEmpty()){
                        class_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch(Exception e){ }
            });
            return data;
        }
    }
    
    public class BatchClassListService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new BatchClassListWork();
        }
    }

    
    
    public class StudentClassListWork extends Task<ObservableList<Student>> {       
        @Override 
        protected ObservableList<Student> call() throws Exception {
            
            Platform.runLater(() -> {
                
            });
            
            ObservableList<Student> data; 
          
            if(selectedClass != null){
                data = SIMS.dbHandler.getVirtualClassStudentList(selectedClass.getClassID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            
            for(Student std: data){
                std.setFirstName(std.getFullName());
            }
            
            Platform.runLater(() -> {
                
            });
            
            return data;
        }
       
    }
    
    public class StudentClassWorkService extends Service<ObservableList<Student>> {

        @Override
        protected Task createTask() {
            return new StudentClassListWork();
        }
    }
   
    
}
