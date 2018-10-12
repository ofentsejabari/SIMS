/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Batch;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Optional;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author MOILE
 */
public class AcademicClassesView extends BorderPane{
    
    CustomTableView<EmployeeModel> academicClassesTable;
    public static ObservableList<EmployeeModel> employeeClassView = FXCollections.observableArrayList();
    public static EmployeeClassListWorkService employeeClassListWork;
    public static ClassWorkService classWork;
    public Label className;
    JFXButton btn_refresh,btn_addTeacher;
    public static JFXListView<HBox> classList;

    BaseClass selectedClass = null;
    
    public static int selectedIndex = 0;
    private final JFXComboBox<String> batch;
    Batch selectedBatch = null;
    
    public AcademicClassesView(){
        
        classList = new JFXListView<>();
        classList.getStyleClass().add("jfx-custom-list");
        classWork = new ClassWorkService();
        className = new  Label();
        className.getStyleClass().add("title-label");
        
        //--- deptList ---
        employeeClassListWork = new EmployeeClassListWorkService();

        batch = new JFXComboBox<>(AdminQuery.getBatchNames());
        batch.setPromptText("Select Batch");
        batch.setLabelFloat(true);
        batch.setPrefWidth(180);
        if(!batch.getItems().isEmpty()){ batch.setValue(batch.getItems().get(0));}
        
         batch.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            selectedBatch  = AdminQuery.getBatchByName(newValue);
            classWork.restart();
        });
         
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        export.setDisable(true);

        HBox header = new HBox(batch);
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");
        header.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane leftPanel = new BorderPane(classList);
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");
        leftPanel.setTop(header);

        HBox con = new HBox(leftPanel);
        con.setStyle("-fx-padding:0 5 0 0;");

        setLeft(con);
        
        
        setPadding(new Insets(10,5,5,5));
        //------------------------------------------------------
        StackPane root = new StackPane();
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(0,5,0,5));
        root.getChildren().add(container);
        
        //---------------toolbar--------------------------------
        HBox toolBar= new HBox(5);
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Employee List"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            refresh();
        });
        
        btn_addTeacher = new JFXButton("Update Teachers");
        btn_addTeacher.getStyleClass().addAll("btn-xs", "btn-success");
        btn_addTeacher.setTooltip(new Tooltip("Add Teachers to class"));
        btn_addTeacher.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_MULTIPLE_PLUS, "text-white", 18));
        btn_addTeacher.setOnAction((ActionEvent event) -> {
            if(selectedClass!=null){
                new UpdateClassTeachers(selectedClass); 
            }
        });
        
        academicClassesTable = new CustomTableView<>();
        
        
        CustomTableColumn staffID = new CustomTableColumn("EMPLOYEE ID");
        staffID.setPercentWidth(20);
        staffID.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        staffID.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            String [] strArr=ID.split(",");
                            setGraphic(new Label(strArr[0]));
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        CustomTableColumn fname = new CustomTableColumn("EMPLOYEE NAME");
        fname.setPercentWidth(20);
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
        
        CustomTableColumn department = new CustomTableColumn("SUBJECT");
        department.setPercentWidth(20);
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        department.setCellFactory(TextFieldTableCell.forTableColumn());
        department.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn controls = new CustomTableColumn("");
        controls.setPercentWidth(40);
        controls.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        controls.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        if(!empty){
                            
                            HBox actions = new HBox(10);
                            actions.setStyle("-fx-padding:0");
                            String [] strArr=ID.split(",");
                            
                            JFXButton edit = new JFXButton();
                            edit.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PENCIL,"text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit Employee Profile"));
                            edit.getStyleClass().addAll("btn-xs", "btn-default");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateEmployeeStage(EmployeeQuery.getEmployeeByID(strArr[0]));
                            });
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {

                                EmployeeModel sswm = EmployeeQuery.getEmployeeByID(strArr[0]);

                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Teacher",
                                        "Are you sure you want to remove this Teacher ("+sswm.getFullName()+")",
                                        ButtonType.YES, ButtonType.CANCEL);

                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()== ButtonType.OK){

                                    if(EmployeeQuery.deleteClassTeacher(sswm,selectedClass.getClassID(),
                                            AdminQuery.getSubjectByName(strArr[1]).getSubjectID())){

                                        new DialogUI("Employee removed successfully",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        classWork.restart();
                                    }else{
                                        new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                    }
                                }    
                            });

                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                            view.setTooltip(new Tooltip("View Employee profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                new EmployeeProfileStage(EmployeeQuery.getEmployeeByID(strArr[0]));
                            });
                            view.setDisable(true);
                            
                            actions.getChildren().addAll(view,edit,del);
                            
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        academicClassesTable.getTableView().getColumns().addAll( staffID, fname,department ,controls);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        academicClassesTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(employeeClassListWork.runningProperty());
        academicClassesTable.getTableView().itemsProperty().bind(employeeClassListWork.valueProperty());
        
        academicClassesTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(employeeClassListWork));
        
        container.setCenter(academicClassesTable);
        
        
        classList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
       
            try {
                Label lb = (Label)classList.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedClass = AdminQuery.getBaseClassByName(lb.getText(),selectedBatch.getId());
                selectedIndex = newValue.intValue();
                className.setText(selectedClass.getName());
                
                employeeClassListWork.restart();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        
        
        classList.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(classWork));
        
        toolBar.getChildren().addAll(className,new HSpacer(),btn_refresh, btn_addTeacher, export);
        toolBar.getStyleClass().addAll("panel-heading", "panel-info");

        container.setTop(toolBar);
        setCenter(root);
        
        classWork.start();
        classWork.restart();
        
    }
    
    
    public class ClassListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            ObservableList<BaseClass> baseclasses = AdminQuery.getBatchBaseClassList(selectedBatch.getId());
        
            for (BaseClass b: baseclasses) {
                
                JFXButton total = new JFXButton(""+EmployeeQuery.getCoreClassAssignedTeachers(b.getClassID()).size());
                total.setTooltip(new Tooltip("Total number of Teachers"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);
                
                
                HBox cn = new HBox(new Label(b.getName()), new HSpacer(), total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
                
            }
                        
            Platform.runLater(() -> {
                try {
                    classList.setItems(data);
                    if(selectedClass!=null)
                    {
                        classList.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class ClassWorkService  extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new ClassListWork();
        }
    }
     
    public class EmployeeClassListWork extends Task<ObservableList<EmployeeModel>> {
        @Override 
        protected ObservableList<EmployeeModel> call() throws Exception {
            
           employeeClassView  = EmployeeQuery.getCoreClassAssignedTeachers(selectedClass.getClassID());
           
           for(EmployeeModel em: employeeClassView){
               em.setFirstName(em.getFullName());
               em.setEmployeeID(em.getEmployeeID()+","+em.getDepartment());
           }
           return employeeClassView;
        } 
    }

    
    public class EmployeeClassListWorkService extends Service<ObservableList<EmployeeModel>> {

        @Override
        protected Task createTask() {
            return new EmployeeClassListWork();
        }
    }



    private void refresh(){
        
        ObservableList<String> data = AdminQuery.getBatchNames();
        if(!data.isEmpty()){
            ObservableList<String> current = batch.getItems();
            if(!current.isEmpty()){
                
                for(String st: data){
                    if(!current.contains(st)){
                        batch.setItems(data);
                        batch.setValue(selectedBatch.getDescription());
                        break;
                    }
                }
            }else{
                batch.setItems(data);
                batch.setValue(batch.getItems().get(0));
                classList.getSelectionModel().select(selectedIndex);
            }
        }
        classWork.restart();
    }
    
}
