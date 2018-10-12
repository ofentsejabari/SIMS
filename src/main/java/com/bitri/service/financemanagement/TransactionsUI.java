package com.bitri.service.financemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.FeesQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Batch;
import com.bitri.service.studentmanagement.BaseClassProfileUI;
import com.bitri.service.studentmanagement.StudentOptionalSubjectsUI;
import com.bitri.service.studentmanagement.StudentProfileStage;
import com.bitri.service.studentmanagement.UpdateStudentProfile;
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
 * @author ofentse
 */
public class TransactionsUI extends BorderPane{

    public static JFXListView<HBox> class_listView;
    public static TransactionWorkService cws = null;
    public static CustomTableView<Transaction> table;
    ObservableList<Fee> allFees = FXCollections.observableArrayList();
    JFXComboBox<String> feeType;
    
    BaseClass selectedClass = null;
    Batch selectedBatch = null;
    public static int selectedIndex = 0;
    
    public BatchClassListService bcls = new BatchClassListService();
    private final JFXComboBox<String> batch;
    
    public TransactionsUI() {
        
        setPadding(new Insets(15, 5, 5, 5));
        cws = new TransactionWorkService();
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 0));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll( "secondary-toolbar");
        ssn_center.setTop(toolbar);
        
        batch = new JFXComboBox<>(AdminQuery.getBatchNames());
        batch.setPromptText("Select Batch");
        batch.setLabelFloat(true);
        batch.setPrefWidth(220);
        
        JFXButton settings = new JFXButton("");
        settings.getStyleClass().addAll("btn-xs", "btn-info");
        settings.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        settings.setOnAction((ActionEvent event) -> {
           new TableFiltersPopup(settings, toolbar);
           
        });
        
        feeType = new JFXComboBox<>();
        feeType.setPromptText("Select fee type");
        feeType.setLabelFloat(true);
        feeType.setPrefWidth(180);
        feeType.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
             
        });
        
        VBox right = new VBox(5);
        right.setPadding(new Insets(0,0,0,10));
        GridPane gpane = new GridPane();
        gpane.add(feeType,0,1);
        
        gpane.setPadding(new Insets(10));
        
        
        
        HBox filterTool = new HBox(5);
        filterTool.getStyleClass().add("primary-toolbar");
        filterTool.getChildren().addAll(new Label("FILTERS"));
        
        try{
             allFees = FeesQuery.getAllFees("all");
        }
        catch(Exception e){

        }
        
        for (Fee fee :allFees){
            feeType.getItems().add(fee.getName());
        }
        
        right.getChildren().addAll(filterTool,gpane);
        ssn_center.setRight(right);
        
        
        JFXButton add = new JFXButton("Capture Payment");
        
        add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
           new FeesCollectionDialog();
        });
        
        Label title = new Label();
        title.getStyleClass().add("title-label");
        
        
        batch.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            selectedBatch  = AdminQuery.getBatchByName(newValue);
            bcls.restart();
        });
        
        HBox.setHgrow(batch, Priority.ALWAYS);
        
          
        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setTooltip(new Tooltip("Refresh batch data"));
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            ObservableList<String> batches = AdminQuery.getBatchNames();
            
            if(!batches.isEmpty()){
                batch.setItems(batches);
                bcls.restart();
            }
        });
        
        JFXButton export = new JFXButton();
        export.setText("Export");
        export.setTooltip(new Tooltip("Optional subjects allocation for students"));
        export.getStyleClass().addAll("btn-xs", "btn-primary");
        export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        export.setOnAction((ActionEvent event) -> {
            if(selectedClass != null){
                new StudentOptionalSubjectsUI(selectedClass);
            }
        });
        
        toolbar.getChildren().addAll( new HSpacer(), add, export, refresh);
        
        setCenter(ssn_center);
        
        class_listView = new JFXListView<>();
        VBox.setVgrow(class_listView, Priority.ALWAYS);
        class_listView.getStyleClass().add("jfx-custom-list");
        class_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                Label lb = (Label)class_listView.getSelectionModel().getSelectedItem().getChildren().get(0);
                selectedClass = AdminQuery.getBaseClassByName(lb.getText(), selectedBatch.getId());
                
                title.setText(selectedBatch.getDescription()+" - "+ selectedClass.getName());
                title.setTooltip(new Tooltip(selectedClass.getName()+"\n"+ selectedBatch.getDescription()));
                
                selectedIndex = newValue.intValue();
                cws.restart();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        class_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(bcls));
        
        
        HBox hb = new HBox(batch);
        hb.setAlignment(Pos.BOTTOM_CENTER);
        hb.getStyleClass().add("primary-toolbar");
        hb.setStyle("-fx-pref-height:35; -fx-max-height:35; -fx-padding:0 0 2 0;");
        
        BorderPane left = new BorderPane(class_listView);
        left.setPadding(new Insets(10, 5, 0, 0));
        left.setTop(hb);
        
        setLeft(left);
        //----------------------------------------------------------------------
        table = new CustomTableView<>();
     
        CustomTableColumn fname = new CustomTableColumn("NAME");
        fname.setPercentWidth(20);
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
        
        CustomTableColumn amountPaid = new CustomTableColumn("AMOUNT (BWP)");
        amountPaid.setPercentWidth(20);
        amountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        amountPaid.setCellFactory(TextFieldTableCell.forTableColumn());
        amountPaid.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        
        CustomTableColumn receivedBy = new CustomTableColumn("RECEIVED BY");
        receivedBy.setPercentWidth(20);
        receivedBy.setCellValueFactory(new PropertyValueFactory<>("receivedBy"));
        receivedBy.setCellFactory(TextFieldTableCell.forTableColumn());
        receivedBy.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        
        CustomTableColumn date = new CustomTableColumn("DATE");
        date.setPercentWidth(20);
        date.setCellValueFactory(new PropertyValueFactory<>("capturedDate"));
        date.setCellFactory(TextFieldTableCell.forTableColumn());
        date.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        ctrl.setPercentWidth(19.9);
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
        
        table.getTableView().getColumns().addAll(fname, amountPaid, balance, receivedBy, date);
        
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
       
        if(!batch.getItems().isEmpty()){ batch.setValue(batch.getItems().get(0));}
    }

    
    
    public class BatchClassListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            
            ObservableList<BaseClass> baseClass = AdminQuery.getBatchBaseClassList(selectedBatch.getId());
        
            
            for (BaseClass bc: baseClass) {
                
                JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                view.setTooltip(new Tooltip("View class"));
                view.getStyleClass().addAll("btn-info", "btn-xs");
                view.setOnAction((ActionEvent event) -> {
                    new BaseClassProfileUI(AdminQuery.getBaseClassByID(bc.getClassID()));
                });
                
                HBox cn = new HBox(new Label(bc.getName()), new HSpacer(), view);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
            
            
            Platform.runLater(() -> {
                try {
                    if(!data.isEmpty()){
                        class_listView.setItems(data);

                        if(selectedClass != null){
                            class_listView.getSelectionModel().select(selectedIndex);
                        }
                    }
                   
                } catch (Exception e) {
                }
                
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

    
    
    public class TransactionListWork extends Task<ObservableList<Transaction>> {       
        @Override 
        protected ObservableList<Transaction> call() throws Exception {
            ObservableList<Transaction> data; 
          
            if(selectedClass != null){
                data = FeesQuery.getAllTransactions();
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            return data;
        }
    }
    
    public class TransactionWorkService extends Service<ObservableList<Transaction>> {

        @Override
        protected Task createTask() {
            return new TransactionListWork();
        }
    }
   
    
}
