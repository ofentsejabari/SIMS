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
public class BaseClassSocialWelfare extends BorderPane{

    CustomTableView<StudentSocialWelfareModel> studentSocialWelfare;
    SocialWelfareListWorkService socialWelfareListWork;
    BaseClass base_class; 
    public static ObservableList<String> special = FXCollections.observableArrayList();
    public static ObservableList<StudentSocialWelfareModel> socialWelfareList = FXCollections.observableArrayList();
    public String selectedWelfare;
    
    public BaseClassSocialWelfare(BaseClass base_class) {
        
        this.base_class=base_class;
        
        socialWelfareListWork = new SocialWelfareListWorkService();
        
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
            socialWelfareListWork.restart();
        });
        
        JFXButton add = new JFXButton("Add");
        add.getStyleClass().addAll("btn-xs","btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
            
            new UpdateBaseClassSocialWelfareStudents(base_class,socialWelfareListWork);
        
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        
        
        special = SIMS.dbHandler.getSocialWelfareNames();
        JFXComboBox<String> combo = new JFXComboBox();
        combo.setItems(special);
        combo.setPrefWidth(210);
        combo.setPromptText("Select Social welfare");
        
        
        combo.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            selectedWelfare = newValue;
            socialWelfareListWork.restart();
        });
        
        toolbar.getChildren().addAll(combo,new HSpacer(),export,refresh,add);
        content.setTop(toolbar);
        
        //---------------------------------------------------------------------------------
        studentSocialWelfare = new CustomTableView<>();
        
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
        
        
        
        CustomTableColumn aid = new CustomTableColumn("AID");
        aid.setPercentWidth(25);
        aid.setCellValueFactory(new PropertyValueFactory<>("aidID"));
        aid.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
                            edit.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            edit.getStyleClass().addAll("btn-xs", "btn-default");
                            edit.setOnAction((ActionEvent event) -> {
                                new StudentSpecificWelfares(ID,socialWelfareListWork);
                            });
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 16));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {
                                
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure you want to remove social welfare");
                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()==ButtonType.OK){
                                    
                                    StudentSocialWelfareModel sswm= StudentQuery.getStudentSocialWelfareBySSWID(ID);
                                    SocialWelfare sn = SIMS.dbHandler.getSocialWelfareByID(sswm.getSwID());
                                    Student student = SIMS.dbHandler.getStudentByID(sswm.getStudentID());
                                    
                                    if(StudentQuery.deleteStudentSocialWelfare(student,sn))
                                    {
                                        new DialogUI("Social welfare successfully updated",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        socialWelfareListWork.restart();
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
        
        studentSocialWelfare.getTableView().getColumns().addAll(studentName,gender,aid,controls);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        studentSocialWelfare.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(socialWelfareListWork.runningProperty());
        studentSocialWelfare.getTableView().itemsProperty().bind(socialWelfareListWork.valueProperty());
        
        studentSocialWelfare.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(socialWelfareListWork));
        
        //------------------------------------------------------
        
        content.setCenter(studentSocialWelfare);
        
        //--------------------
        setCenter(root);
        
        socialWelfareListWork.start();
        socialWelfareListWork.restart();
        
        
    }
    
     public class SocialWelfareListWork extends Task<ObservableList<StudentSocialWelfareModel>> {       
        @Override 
        protected ObservableList<StudentSocialWelfareModel> call() throws Exception {
            
           SocialWelfare sneed = SIMS.dbHandler.getSocialWelfareByName(selectedWelfare.trim());
           socialWelfareList  = StudentQuery.getSelectedStudentSocialWelfare(base_class.getClassID(),sneed.getId());
           for(StudentSocialWelfareModel n : socialWelfareList)
           {
               n.setAidID(SIMS.dbHandler.getSocialWelfareAidByID(n.getAidID()).getName());
               
           }
           return socialWelfareList;
        } 
    }

    
    public class SocialWelfareListWorkService extends Service<ObservableList<StudentSocialWelfareModel>> {

        @Override
        protected Task createTask() {
            return new SocialWelfareListWork();
        }
    }
    
    
    
    
}
