package com.bitri.service.studentmanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
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
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 *
 * @author ofentse
 */
public class SocialWelfareSupport extends BorderPane{

    public static JFXListView<HBox> sws_listView;
    public static SWSWorkService SWSWorkService = null;
    public static CustomTableView<Student> table;
    public static WelfareSupportService wss;
    
    SocialWelfare selectedSocialWelfare = null;
    public static int selectedIndex = 0;
    
    private final JFXComboBox<String> batches;
    Batch selectedBatch = null;
    
    public SocialWelfareSupport() {
        
        setPadding(new Insets(15, 5, 5, 5));
        SWSWorkService = new SWSWorkService();
        wss = new WelfareSupportService();
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 5));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-heading", "panel-info");
        ssn_center.setTop(toolbar);
        
        JFXButton btn_add = new JFXButton("Add Social Welfare");
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateSWSDialog(null);
        });
        
        // Create MenuItems
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        export.setDisable(true);

        Label title = new Label();
        title.getStyleClass().add("title-label");
        
        setCenter(ssn_center);
        
        batches = new JFXComboBox<>(AdminQuery.getBatchNames());
        batches.setTooltip(new Tooltip("Select Batch"));
        batches.setPrefWidth(180);
        batches.setValue("Select Batch");
        batches.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            selectedBatch  = AdminQuery.getBatchByName(newValue);
            System.out.println(selectedBatch.getDescription());
            wss.restart();
        });

        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            
            ObservableList<String> batchData = AdminQuery.getBatchNames();
            
            if(batches.getItems().isEmpty()){
                batches.setItems(batchData);
                batches.setValue(batchData.get(0));
                
            }else{
            
                if((!batchData.isEmpty()) && (batches.getItems().size() != batchData.size())){
                    batches.setItems(batchData);

                    if(selectedBatch != null && !selectedBatch.getDescription().equalsIgnoreCase("")){
                        batches.setValue(selectedBatch.getDescription());
                    }else{
                        batches.setValue(batchData.get(0));
                    }
                }
            }
            wss.restart();
        });
        
        toolbar.getChildren().addAll(title, new HSpacer(), refresh, btn_add, export);
        
        sws_listView = new JFXListView<>();
        sws_listView.getStyleClass().add("jfx-custom-list");
        sws_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try{
                Label lb = (Label)sws_listView.getSelectionModel().getSelectedItem().getChildren().get(0);
                selectedSocialWelfare = SIMS.dbHandler.getSocialWelfareByName(lb.getText());
                title.setText(selectedSocialWelfare.getName());
                title.setTooltip(new Tooltip(selectedSocialWelfare.getDescription()));
                
                selectedIndex = newValue.intValue();
                SWSWorkService.restart();
                
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        });
        
        sws_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(SWSWorkService));

        HBox header = new HBox(batches);
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");
        header.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane leftPanel = new BorderPane(sws_listView);
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");
        leftPanel.setTop(header);

        HBox con = new HBox(leftPanel);
        con.setStyle("-fx-padding:0 5 0 0;");

        setLeft(con);
        //----------------------------------------------------------------------
        table = new CustomTableView<>();
        
        CustomTableColumn cn = new CustomTableColumn("STUDENT NAME");
        cn.setPercentWidth(24.9);
        cn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        cn.setCellFactory(TextFieldTableCell.forTableColumn());
        cn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        gender.setCellFactory(TextFieldTableCell.forTableColumn());
        gender.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Label description = new Label("");
                        
                        if(!empty){
                            description.setText(ID);
                            setGraphic(description);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn type = new CustomTableColumn("CLASS");
        type.setPercentWidth(60);
        type.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        type.setCellFactory(TextFieldTableCell.forTableColumn());
        type.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label();
                        
                        if(!empty){
                            if(type.equalsIgnoreCase("0")){
                                subjectType.setText("Optional");
                            }else{
                                subjectType.setText("Core");
                            }
                            
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll(cn, gender, type);
        VBox.setVgrow(table, Priority.ALWAYS);
                
        ProgressIndicator pi = new ProgressIndicator(" Loading data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(SWSWorkService.runningProperty());
        table.getTableView().itemsProperty().bind(SWSWorkService.valueProperty());
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(wss));
        
        StackPane stackPane = new StackPane(table, pi);
        
        ssn_center.setCenter(stackPane);
        SWSWorkService.start();
        
        wss.start();
        
        if(!batches.getItems().isEmpty()){
            batches.setValue(batches.getItems().get(0));
        }
        wss.restart();
    }
    

    
    
    public class WelfareSupportWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            
            ObservableList<SocialWelfare> ssn = SIMS.dbHandler.getSocialWelfares();
        
            for(SocialWelfare spn: ssn){
                
                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Update special need"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateSWSDialog(spn);
                });
                
                JFXButton total = new JFXButton( ""+ SIMS.dbHandler.getStudentsWithSocialWelfareSupport(spn.getId(), selectedBatch.getId()).size());
                total.setTooltip(new Tooltip("Total number of students with "+spn));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);                        

                
                HBox cn = new HBox(new Label(spn.getName()), new HSpacer(), edit, total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
                        
            Platform.runLater(() -> {
                try {
                    sws_listView.setItems(data);
                
                    if(selectedSocialWelfare != null){
                        sws_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
            });
            
            return data;
        }
       
    }

    public class WelfareSupportService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new WelfareSupportWork();
        }
    }
    
    
    
    public class SWSListWork extends Task<ObservableList<Student>> {       
        @Override 
        protected ObservableList<Student> call() throws Exception {
            
            ObservableList<Student> data = null; 
            
            if(selectedSocialWelfare != null){
                data = SIMS.dbHandler.getStudentsWithSocialWelfareSupport(selectedSocialWelfare.getId(), selectedBatch.getId());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            return data;
        }
       
    }

    public class SWSWorkService extends Service<ObservableList<Student>> {

        @Override
        protected Task createTask() {
            return new SWSListWork();
        }
    }
    
}
