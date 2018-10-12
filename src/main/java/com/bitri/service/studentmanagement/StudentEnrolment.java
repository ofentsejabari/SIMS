package com.bitri.service.studentmanagement;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.bitri.access.*;
import com.bitri.access.SIMS;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.geometry.Pos;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.Batch;
import com.bitri.resource.dao.StudentQuery;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

/**
 * @author ofentse
 */
public class StudentEnrolment extends  BorderPane{

    JFXButton btn_add, btn_refresh;
    CustomTextField search;
    StackPane stackpane;
        
    public static UpdateStudentProfile profileStage;
    public static CustomTableView<Student> studentTable;
    
    public StudentListWorkService slws;
    public BatchWorkService bws;
    
    JFXListView<HBox> batch_listView;
    
    Batch selectedBatch = null;
    public static int selectedIndex = 0;
    
    public static ObservableList<Student> data = FXCollections.observableArrayList();
    
    public StudentEnrolment(){
        
        setPadding(new Insets(15, 5, 5, 5));
        
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(0, 0, 0, 10));
        
        stackpane = new StackPane();
        slws = new StudentListWorkService();
        bws = new BatchWorkService();
        
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
                
                for(Student s : studentTable.getTableView().getItems()){
                    data.add(s);
                }
                
                if(str != null && str.length() > 0){
                    studentTable.getTableView().getItems().clear();
                    for(Student student : data) {
                        
                        if(student.getStudentID().toLowerCase().contains(str.toLowerCase()) ||
                           student.getFirstName().toLowerCase().contains(str.toLowerCase()) ||
                           student.getLastName().toLowerCase().contains(str.toLowerCase())){
                            if(!studentTable.getTableView().getItems().contains(student)){
                                studentTable.getTableView().getItems().add(student);
                            }
                        }
                    }
                    
                }else{
                    slws.restart();
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

        btn_add = new JFXButton("Enroll Student");
        btn_add.setTooltip(new Tooltip("Enroll student into batch."));
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            profileStage = new UpdateStudentProfile(null);
            profileStage.show();
        });


        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.setTooltip(new Tooltip("Refresh class list"));
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            bws.restart();
            search.clear();
        });

        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-heading", "panel-info");
        container.setTop(toolbar);
        
        Label title = new Label();
        title.getStyleClass().add("title-label");
        //----------------------------------------------------------------------
        
        toolbar.getChildren().addAll(search, new HSpacer(), btn_refresh, btn_add, btn_export);
      
        //-------------------Search bar and table-------------------------------
        studentTable = new CustomTableView<>();
        
        CustomTableColumn id = new CustomTableColumn("");
        id.setPercentWidth(5);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            
                            if(ID.equals("0")){
                                Label status = new Label("");
                                status.setGraphic(SIMS.getGraphics(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "text-warning", 18));

                                status.setTooltip(new Tooltip("Student has no class assigned yet."));
                                setGraphic(status);
                            }else{
                                setGraphic(null);
                            }
                            
                        }else{setGraphic(null);}
                    }
                };
            }
        });
        
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
                            
                            setGraphic(new Label(ID));
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
        
        CustomTableColumn contacts = new CustomTableColumn("GENDER");
        contacts.setPercentWidth(15);
        contacts.setCellValueFactory(new PropertyValueFactory<>("gender"));
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
        
        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(34.9);
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
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
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

                            actions.getChildren().addAll(view, edit);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });
        
        studentTable.getTableView().getColumns().addAll(id, admissionNumber, fname, contacts, ctrl);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(slws);
        studentTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Student Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        HBox loader = new HBox(pi);
        loader.setAlignment(Pos.CENTER);
        
        
        loader.visibleProperty().bind(slws.runningProperty());
        studentTable.getTableView().itemsProperty().bind(slws.valueProperty());
        
        stackpane.getChildren().addAll(studentTable, loader);
        container.setCenter(stackpane);
        
        setCenter(container);
        
        batch_listView = new JFXListView<>();
        batch_listView.getStyleClass().add("jfx-custom-list");
        batch_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
               
            try {
                Label lb = (Label)batch_listView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedBatch = AdminQuery.getBatchByName(lb.getText());
                selectedIndex = newValue.intValue(); 

                title.setText(selectedBatch.getDescription());
                slws.restart();
                
                search.clear();
                data.clear();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        batch_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(bws));

        HBox header = new HBox(new Label("Batches"));
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");
        header.setAlignment(Pos.CENTER_LEFT);

        BorderPane leftPanel = new BorderPane(batch_listView);
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");
        leftPanel.setTop(header);

        HBox con = new HBox(leftPanel);
        con.setStyle("-fx-padding:0 5 0 0;");

        setLeft(con);
        
        slws.start();
        
        bws.start();
        bws.restart();
    }   
    
    
    
    
    public class BatchListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            
            ObservableList<Batch> batch = AdminQuery.getBatches();
        
            for (Batch b: batch) {
                
                JFXButton total = new JFXButton( ""+ SIMS.dbHandler.getStudentList(b.getId()).size());
                total.setTooltip(new Tooltip("Total number of students"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);
                
                
                HBox cn = new HBox(new Label(b.getDescription()), new HSpacer(), total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
                        
            Platform.runLater(() -> {
                try {
                    batch_listView.setItems(data);
                
                    if(!data.isEmpty()){
                        batch_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class BatchWorkService extends Service<ObservableList<Label>> {

        @Override
        protected Task createTask() {
            return new BatchListWork();
        }
    }
    
    
    public class StudentListWork extends Task<ObservableList<Student>> {       
        @Override 
        protected ObservableList<Student> call() throws Exception {
           
            ObservableList<Student> studentList  = SIMS.dbHandler.getStudentList(selectedBatch.getId());
            
            ObservableList<String> allocated = StudentQuery.getBaseClassAllocatedStudents(selectedBatch.getId());
            
            for(Student std: studentList){
                
                std.setFirstName(std.getFullName());
                if(!allocated.contains(std.getStudentID())){//-- Student has no base class yet
                    std.setId("0");
                }
            }
              
            return studentList;
        } 
    }

    public class StudentListWorkService extends Service<ObservableList<Student>> {

        @Override
        protected Task createTask() {
            return new StudentListWork();
        }
    }
}
