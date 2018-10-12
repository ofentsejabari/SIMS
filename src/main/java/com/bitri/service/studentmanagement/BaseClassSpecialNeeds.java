/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.StudentQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class BaseClassSpecialNeeds extends BorderPane{

    CustomTableView<StudentSpecialNeedsModel> studentNeedsTable;
    EmployeeSubjectListWorkService specialListWork;
    BaseClass base_class; 
    public static ObservableList<String> special = FXCollections.observableArrayList();
    public static ObservableList<StudentSpecialNeedsModel> specialList = FXCollections.observableArrayList();
    public String selectedNeed;
    
    public BaseClassSpecialNeeds(BaseClass base_class) {
        
        this.base_class=base_class;
        
        specialListWork = new EmployeeSubjectListWorkService();
        
        setPadding(new Insets(10,5,5,5));
        StackPane root = new StackPane();
        BorderPane content = new BorderPane();
        root.getChildren().add(content);
        
        //----------------------------------------
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().add("primary-toolbar");
        
        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON,"blue-gray", 18));
        refresh.setOnAction((ActionEvent event) -> {
            specialListWork.restart();
        });
        
        JFXButton add = new JFXButton("Add");
        add.getStyleClass().addAll("btn-xs","btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
            
            new UpdateBaseClassSpecialNeedStudents(base_class,specialListWork);
        
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        
        
        JFXComboBox<String> combo = new JFXComboBox(SIMS.dbHandler.getSpecialNeedNames());
        combo.setPrefWidth(210);
        combo.setPromptText("Select a special need");
        
        
        combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            selectedNeed = newValue;
            specialListWork.restart();
        });
        
        toolbar.getChildren().addAll(combo,new HSpacer(),export,refresh,add);
        content.setTop(toolbar);
        
        //---------------------------------------------------------------------------------
        studentNeedsTable = new CustomTableView<>();
        
        CustomTableColumn studentName = new CustomTableColumn("STUDENT NAME");
        studentName.setPercentWidth(30);
        studentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn gender = new CustomTableColumn("GENDER");
        gender.setPercentWidth(15);
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gender.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn specialNeed = new CustomTableColumn("SPECIAL NEED(S)");
        specialNeed.setPercentWidth(25);
        specialNeed.setCellValueFactory(new PropertyValueFactory<>("description"));
        specialNeed.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn solution = new CustomTableColumn("SOLUTION(S)");
        solution.setPercentWidth(25);
        solution.setCellValueFactory(new PropertyValueFactory<>("solution"));
        solution.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        controls.setPercentWidth(24.5);
        controls.setCellValueFactory(new PropertyValueFactory<>("id"));
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
                            
                            JFXButton edit = new JFXButton();
                            edit.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON,"text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit Employee Profile"));
                            edit.getStyleClass().addAll("btn-xs", "btn-default");
                            edit.setOnAction((ActionEvent event) -> {
                                
                                new StudentSpecificNeed(ID,specialListWork);
                            });
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 16));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {
                                
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure you want to remove special need");
                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()==ButtonType.OK){
                                    
                                    StudentSpecialNeedsModel sswm= StudentQuery.getSpecialNeedStudentBySSNID(ID);
                                    SpecialNeed sn = StudentQuery.getSpecialNeedByID(sswm.getSn_id());
                                    Student student = SIMS.dbHandler.getStudentByID(sswm.getStudentID());
                                    
                                    if(StudentQuery.deleteStudentSpecialNeed(student,sn))
                                    {
                                        new DialogUI("Special need successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        specialListWork.restart();
                                    }
                                    else{
                                            new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                }    
                            });
                            
                            actions.getChildren().addAll(edit,del);
                            
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        studentNeedsTable.getTableView().getColumns().addAll(studentName,gender,specialNeed,solution,controls);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        studentNeedsTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(specialListWork.runningProperty());
        studentNeedsTable.getTableView().itemsProperty().bind(specialListWork.valueProperty());
        
        studentNeedsTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(specialListWork));
        
        //------------------------------------------------------
        
        content.setCenter(studentNeedsTable);
        
        //--------------------
        setCenter(root);
        
        specialListWork.start();
        specialListWork.restart();
        
        
    }
    
     public class SpecialNeedListWork extends Task<ObservableList<StudentSpecialNeedsModel>> {       
        @Override 
        protected ObservableList<StudentSpecialNeedsModel> call() throws Exception {
           
           SpecialNeed sneed = SIMS.dbHandler.getSpecialNeedByName(selectedNeed.trim());
           specialList  = StudentQuery.getSelectedNeedStudents(base_class.getClassID(),sneed.getId());
           
           
           for(StudentSpecialNeedsModel n : specialList)
           {
               n.setReference(n);
               
           }
           return specialList;
        } 
    }

    
    public class EmployeeSubjectListWorkService extends Service<ObservableList<StudentSpecialNeedsModel>> {

        @Override
        protected Task createTask() {
            return new SpecialNeedListWork();
        }
    }
    
    
    
    
}
