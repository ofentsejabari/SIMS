package com.bitri.service.financemanagement;


import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.FeesQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
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

/**
 *
 * @author ofentse
 */
public class FeesUI extends BorderPane{

   
    public static FeesWorkService cws = null;
    public static CustomTableView<Fee> table;
    public static int selectedYearIndex = 0;
     //----------------------------------
    public static AcademicYearsWorkService academicWork;
    public static JFXListView<HBox> academicYearListView ;
    ObservableList<Fee> yearsList;
    String selectedYear;
    Label selected;
    //--------

    public FeesUI() {
        
        setPadding(new Insets(10, 5, 5, 5));
        cws = new FeesWorkService();
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 10));
        
        yearsList = FXCollections.observableArrayList();
        
        academicWork = new AcademicYearsWorkService();
        
        //-------- listview
        academicYearListView = new JFXListView<>();
        academicYearListView.getStyleClass().add("jfx-custom-list");
        academicYearListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            
                Label lb = (Label)academicYearListView.getItems().get(newValue.intValue()).getChildren().get(0);
                yearsList = FeesQuery.getFeesByAcademicYear(lb.getText());
                selectedYear= lb.getText();
                selectedYearIndex = newValue.intValue();
                selected.setText("Academic Year: "+lb.getText());
                cws.restart();
       });
        
        
        Label sideTittle = new Label("ACADEMIC YEARS");
        sideTittle.getStyleClass().add("title-label");
        
        
        HBox sideTool = new HBox();
        sideTool.getChildren().add(sideTittle);
        sideTool.setSpacing(2);
        sideTool.getStyleClass().add("secondary-toolbar");
        
        
        VBox leftChild = new VBox();
        leftChild.getChildren().addAll(sideTool,academicYearListView);
        
        setLeft(leftChild);
        VBox.setVgrow(academicYearListView, Priority.ALWAYS);
        
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().add("secondary-toolbar");
        ssn_center.setTop(toolbar);
 
        JFXButton add = new JFXButton("Add Fee");
        add.getStyleClass().addAll("btn-xs", "btn-success");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        add.setOnAction((ActionEvent event) -> {
           // if(selectedClass != null){
                //new UpdateBaseClassStudentsDialog(selectedClass);
                new UpdateFeeDialog(null,academicWork);
          //  }
        });
        
        Label title = new Label();
        title.getStyleClass().add("title-label");
     
         MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
          
        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setTooltip(new Tooltip("Refresh fees data"));
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            ObservableList<String> batches = AdminQuery.getBatchNames();
             academicWork.restart();
        });
        
        JFXButton optionalSubjects = new JFXButton("Export");
        optionalSubjects.setTooltip(new Tooltip("Fees"));
        optionalSubjects.getStyleClass().addAll("btn-xs", "btn-default");
        optionalSubjects.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "", 18));
        optionalSubjects.setOnAction((ActionEvent event) -> {
            
            
        });
        selected = new Label();
        selected.getStyleClass().add("title-label");
        toolbar.getChildren().addAll(selected,new HSpacer(), add,export,refresh);
        
        setCenter(ssn_center);
        
       
        //----------------------------------------------------------------------
        table = new CustomTableView<>();
        
        CustomTableColumn name = new CustomTableColumn("NAME");
        name.setPercentWidth(20);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn payment = new CustomTableColumn("PAYMENT PERIOD");
        payment.setPercentWidth(20);
        payment.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
        payment.setCellFactory(TextFieldTableCell.forTableColumn());
        payment.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        
        CustomTableColumn defaultamount = new CustomTableColumn("AMOUNT");
        defaultamount.setPercentWidth(15);
        defaultamount.setCellValueFactory(new PropertyValueFactory<>("defaultAmount"));
        defaultamount.setCellFactory(TextFieldTableCell.forTableColumn());
        defaultamount.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>(){
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
        ctrl.setPercentWidth(45);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("id"));
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
                            Fee fee = FeesQuery.getFeeById(ID);
                            JFXButton delete = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 16));
                            delete.setTooltip(new Tooltip("Delete transaction"));
                            delete.getStyleClass().addAll("btn-danger", "btn-xs");
                            delete.setOnAction((ActionEvent event) -> {
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Delete Fees",
                                        "Are you sure you want to delete this fee ("+fee.getName()+")",
                                        ButtonType.YES, ButtonType.CANCEL);
                                
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {

                                        if(FeesQuery.deleteFee(ID)){
                                            academicWork.restart();
                                            new DialogUI("Fee has been removed successfully",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                                           
                                        }else{
                                             new DialogUI("Exception occurred while trying to to delete the Fee.", DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                        }
                                    }else{alert.close();}
                                });
                                
                                
                            });
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit student information"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                
                                new UpdateFeeDialog(fee,academicWork);
                            });
                            
                            actions.getChildren().addAll(edit, delete);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                      
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll(name,payment,defaultamount,ctrl);
        
        VBox.setVgrow(table, Priority.ALWAYS);
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(cws));
              
       ProgressIndicator pi = new ProgressIndicator("Loading data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
       
        pi.visibleProperty().bind(cws.runningProperty());
        table.getTableView().itemsProperty().bind(cws.valueProperty());
        
        StackPane stackPane = new StackPane(table, pi);
        
        ssn_center.setCenter(stackPane);
       
              
        cws.start();
       
        

    
    }
    
    public class AcademicYearsWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
                
            });
            ObservableList<String> years = AdminQuery.getAcademicYearList();
            
            Platform.runLater(() -> {
                try {
                      for (String b: years) {
                
                            JFXButton total = new JFXButton(""+FeesQuery.getFeesByAcademicYear(b).size());
                            total.setTooltip(new Tooltip("Total number of fees"));
                            total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                            total.setPrefWidth(32);

                            HBox cn = new HBox(new Label(b), new HSpacer(), total);
                            cn.getStyleClass().add("cell-content");
                            cn.setSpacing(5);
                            data.add(cn);

                        }
                      academicYearListView.setItems(data);
                } 
                catch (Exception e) 
                {
                }
                
            });
            
            Platform.runLater(() -> {
                try {
                    academicYearListView.setItems(data);
                    if(selectedYear!=null)
                    {
                        academicYearListView.getSelectionModel().select(selectedYearIndex);
                    }
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class AcademicYearsWorkService extends Service<ObservableList<String>> {

        @Override
        protected Task createTask() {
            return new AcademicYearsWork();
        }
    }
    
    
    public class FeesListWork extends Task<ObservableList<Fee>> {       
        @Override 
        protected ObservableList<Fee> call() throws Exception {
            ObservableList<Fee> data = FXCollections.observableArrayList(); 
          
                if(!selectedYear.equals(""))
                    data =  FeesQuery.getFeesByAcademicYear(selectedYear);
   
            return data;
        }
    }
    
    public class FeesWorkService extends Service<ObservableList<Fee>> {

        @Override
        protected Task createTask() {
            return new FeesListWork();
        }
    }
   
    
}
