package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.FeesQuery;
import com.bitri.service.financemanagement.Fee;
import com.bitri.service.financemanagement.Transaction;
import com.bitri.service.schooladministration.BaseClass;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 *
 * @author jabari
 */
public class BaseClassFinanceRecords extends BorderPane{

    public static JFXListView<HBox> fee_listView;
    public static BaseClassTransactionWorkService baseTransactionService = null;
    public static CustomTableView<Transaction> classFeeTable;
    ObservableList<Fee> allFees = FXCollections.observableArrayList();
    JFXComboBox<String> feeType;
    BaseClassListService baseListService;
    Fee selectedFee = null;
    JFXButton add,export,refresh;
    BaseClass base_class = null;
    public static int selectedIndex = 0;
    
    
    public BaseClassFinanceRecords(BaseClass   base_class) {
        
        this.base_class = base_class;   
        baseTransactionService = new BaseClassTransactionWorkService();
        baseListService = new BaseClassListService();
        
        setPadding(new Insets(15, 5, 5, 5));
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 0));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll( "secondary-toolbar");
        ssn_center.setTop(toolbar);
        
        add = new JFXButton("Capture Payment");
        add.setDisable(true);
        add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
            new UpdateBaseClassFinance(base_class,selectedFee);
        });
        
       
        Label title = new Label();
        title.getStyleClass().add("title-label");
         
        refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setTooltip(new Tooltip("Refresh batch data"));
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            
            baseListService.restart();
        });
        
        export = new JFXButton();
        export.setText("Export");
        export.setTooltip(new Tooltip("Base class outstanding fees"));
        export.getStyleClass().addAll("btn-xs", "btn-primary");
        export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        export.setOnAction((ActionEvent event) -> {
            
        });
        
        Label selectedFeeName = new Label() ;
        selectedFeeName.getStyleClass().add("title-label");
        toolbar.getChildren().addAll(selectedFeeName ,new HSpacer(), add, export, refresh);
        
        setCenter(ssn_center);
        
        fee_listView = new JFXListView<>();
        VBox.setVgrow(fee_listView, Priority.ALWAYS);
        fee_listView.getStyleClass().add("jfx-custom-list");
        fee_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
               HBox listHbox = fee_listView.getItems().get(newValue.intValue());
               Label name = (Label)listHbox.getChildren().get(0);
               selectedFeeName.setText(name.getText());
               
               selectedIndex=newValue.intValue();
               
               baseTransactionService.restart();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        
        HBox hb = new HBox();
        Label feeTittle = new Label("School Fees");
        feeTittle.getStyleClass().add("title-label");
        
        hb.setAlignment(Pos.BOTTOM_CENTER);
        hb.getStyleClass().add("primary-toolbar");
        hb.setStyle("-fx-pref-height:35; -fx-max-height:35; -fx-padding:0 0 2 0;");
        hb.getChildren().add(feeTittle);
        
        BorderPane left = new BorderPane(fee_listView);
        left.setPadding(new Insets(10, 5, 0, 0));
        left.setTop(hb);
        
        setLeft(left);
        //----------------------------------------------------------------------
        classFeeTable = new CustomTableView<>();
     
        CustomTableColumn fname = new CustomTableColumn("NAME");
        fname.setPercentWidth(30);
        fname.setCellValueFactory(new PropertyValueFactory<>("studentId"));
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

        
        CustomTableColumn balance = new CustomTableColumn("BALANCE (BWP)");
        balance.setPercentWidth(20);
        balance.setCellValueFactory(new PropertyValueFactory<>("feeID"));
        balance.setCellFactory(TextFieldTableCell.forTableColumn());
        balance.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        ctrl.setPercentWidth(49.9);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("studentId"));
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
                                                        
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit student information"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                
                                new UpdateStudentProfile(SIMS.dbHandler.getStudentByID(ID));
                                
                            });
                            
                            JFXButton print = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                            print.setTooltip(new Tooltip("View student profile"));
                            print.getStyleClass().addAll("btn-default", "btn-xs");
                            print.setOnAction((ActionEvent event) -> {
                                
                                new StudentProfileStage(SIMS.dbHandler.getStudentByID(ID));
                                
                            });
                            
                            actions.getChildren().addAll(print, edit);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });
        
        classFeeTable.getTableView().getColumns().addAll(fname,balance,ctrl);
        
        VBox.setVgrow(classFeeTable, Priority.ALWAYS);
        
        classFeeTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(baseTransactionService));
        ProgressIndicator pi = new ProgressIndicator("Loading data", "If network connection is very slow,w this might take some few more seconds.");
       
        pi.visibleProperty().bind(baseTransactionService.runningProperty());
        classFeeTable.getTableView().itemsProperty().bind(baseTransactionService.valueProperty());
        
        StackPane stackPane = new StackPane(classFeeTable, pi);
        
        ssn_center.setCenter(stackPane);
              
        baseTransactionService.start();
        baseTransactionService.restart();
       
    }

    
    //---------
     public class BaseClassListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            
            ObservableList<Fee> activeFees = FeesQuery.getAllFees("active");
            
            
            for (Fee fee: activeFees) {
                
                JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                view.setTooltip(new Tooltip("View class"));
                view.getStyleClass().addAll("btn-info", "btn-xs");
                view.setOnAction((ActionEvent event) -> {
                    
                });
                
                HBox cn = new HBox(new Label(fee.getName()), new HSpacer(), view);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
            
            
            Platform.runLater(() -> {
                try {
                    if(!data.isEmpty()){
                        fee_listView.setItems(data);

                        if(selectedFee != null){
                            fee_listView.getSelectionModel().select(selectedIndex);
                        }
                    }
                   
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class BaseClassListService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new BaseClassListWork();
        }
    }

    
    
    public class BaseClassTransactionListWork extends Task<ObservableList<Transaction>> {       
        @Override 
        protected ObservableList<Transaction> call() throws Exception {
           ObservableList<Transaction> data; 
           
            HBox listHbox = fee_listView.getItems().get(selectedIndex);
            Label name = (Label)listHbox.getChildren().get(0);
            selectedFee = FeesQuery.getFeeByName(name.getText());
            
            if(selectedFee != null){
                data = FeesQuery.getTransactionByFeeID(selectedFee.getId());
                
                if(selectedFee.getPaymentMode().equals("Specific Day")){

                    add.setDisable(false);
                }
                else{
                    add.setDisable(true);
                }
                
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            return data;
        }
    }
    
    public class BaseClassTransactionWorkService extends Service<ObservableList<Transaction>> {

        @Override
        protected Task createTask() {
            return new BaseClassTransactionListWork();
        }
    }

}


