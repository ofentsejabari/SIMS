package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Batch;
import com.bitri.service.schooladministration.Subject;
import com.bitri.service.schooladministration.UpdateSubjectDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
/**
 *
 * @author ofentse
 */
public class StudentOptionalSubjectsUI extends JFXDialog{
    
    public static JFXListView<HBox> class_listView;
    public static CustomTableView<Student> table;
    
    Subject selectedSubject = null;
    public static int selectedIndex = 0;
    
    public OptionalSubjectStudentService osss;
    public StreamOptionalSubjectService sops;
    
    BaseClass baseClass;
    
    public StudentOptionalSubjectsUI(BaseClass bClass){
        
        baseClass = bClass;
        
        osss = new OptionalSubjectStudentService();
        sops = new StreamOptionalSubjectService();
        
        sops.setOnSucceeded((WorkerStateEvent event) -> {
            osss.restart();
        });
        
        StackPane root = new StackPane();
        BorderPane pane = new BorderPane();
        
        //-- Screen Decoration -------------------------------------------------
        HBox screenDecoration = new HBox();
        screenDecoration.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label(bClass.getName()+" - Optional Subjects");
        title.getStyleClass().add("window-title");
        
        screenDecoration.getChildren().addAll(title, new HSpacer(), btn_close);
        pane.setTop(screenDecoration);
        
        //-- End Screen Decoration ---------------------------------------------
        
        JFXButton add = new JFXButton("Update List");
         add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
            if(selectedSubject != null){
                new UpdateOptionalSubjectsStudentsDialog(baseClass, selectedSubject, sops);
            }
        });
        JFXButton refresh = new JFXButton("Refresh");
        refresh.setTooltip(new Tooltip("Refresh list"));
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-bluegray", 18));
        refresh.setOnAction((ActionEvent event) -> {
            sops.restart();
        });
        
        HBox toolbar = new HBox(5);
        toolbar.setPadding(new Insets(5));
        toolbar.getChildren().addAll(add, refresh, new HSpacer());
              
        class_listView = new JFXListView<>();
        VBox.setVgrow(class_listView, Priority.ALWAYS);
        class_listView.getStyleClass().add("jfx-custom-list");
        class_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                Label lb = (Label)class_listView.getSelectionModel().getSelectedItem().getChildren().get(0);
                selectedSubject = AdminQuery.getSubjectByName(lb.getText());
                add.setTooltip(new Tooltip("Add/ remove students from "+selectedSubject.getName()));
                refresh.setTooltip(new Tooltip("Refresh "+selectedSubject.getName()+" student list"));
                                
                selectedIndex = newValue.intValue();
                osss.restart();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        class_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(sops));
        
        VBox left = new VBox(class_listView);
        left.setPadding(new Insets(0, 5, 0, 0));
        left.setSpacing(5);
        
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(5));
        container.setLeft(left);
        container.setTop(toolbar);
        
        //----------------------------------------------------------------------
        table = new CustomTableView<>();
        
        CustomTableColumn admissionNumber = new CustomTableColumn("ID #");
        admissionNumber.setPercentWidth(25);
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
        fname.setPercentWidth(30);
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
        
        CustomTableColumn contacts = new CustomTableColumn("Gender");
        contacts.setPercentWidth(45);
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
        
        table.getTableView().getColumns().addAll(admissionNumber, fname, contacts);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder(osss);
        table.getTableView().setPlaceholder(ph);
                
        ProgressIndicator pi = new ProgressIndicator("Loading data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(osss.runningProperty());
        table.getTableView().itemsProperty().bind(osss.valueProperty());
        
        StackPane stackPane = new StackPane(table, pi);
        
        container.setCenter(stackPane);
        
        pane.setCenter(container);
        
        sops.start();
        sops.restart();
        
        osss.start();
        
        //----------------------------------------------------------------------
        
        root.getChildren().add(pane);
        root.setPrefSize(800, 400); 
        
        setDialogContainer(PARENT_STACK_PANE);
        setContent(root);
        setOverlayClose(false);
        show();
    }
    
        
    
    public class StreamOptionalSubjectWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            
            Batch batch = AdminQuery.getBatchByID(baseClass.getBatchID());
            ObservableList<Subject> optionalSubjects = AdminQuery.getStreamSubjects(batch.getStreamID(), 0);
        
        
            for (Subject sbj: optionalSubjects) {
                
                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Update class"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateSubjectDialog(sbj);
                });
                
                HBox cn = new HBox(new Label(sbj.getName()), new HSpacer(), edit);
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
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class StreamOptionalSubjectService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new StreamOptionalSubjectWork();
        }
    }
    
    public class OptionalSubjectStudentListWork extends Task<ObservableList<Student>> {       
        @Override 
        protected ObservableList<Student> call() throws Exception {
            
            ObservableList<Student> data; 
          
            if(selectedSubject != null){
                data = AdminQuery.getOptionalSubjectStudentList(baseClass.getClassID(), selectedSubject.getSubjectID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            
            for(Student std: data){
                std.setFirstName(std.getFullName());
            }
            
            return data;
        }
       
    }
    
    public class OptionalSubjectStudentService extends Service<ObservableList<Student>> {

        @Override
        protected Task createTask() {
            return new OptionalSubjectStudentListWork();
        }
    }
    
}
