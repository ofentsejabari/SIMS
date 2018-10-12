package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
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

import static com.bitri.service.schooladministration.SchoolAdministartion.departmentsController;

/**
 *
 * @author ofentse
 */
public class DepartmentSubjectsUI extends BorderPane{
    
    public SubjectWorkService subjectWorkService = null;
    public static CustomTableView<Subject> table;

    public DepartmentSubjectsUI() {
        
        subjectWorkService = new SubjectWorkService();
        setPadding(new Insets(5));
        
        HBox toolbar = new HBox(5);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(2));
        setTop(toolbar);
        
        JFXButton btn_add = new JFXButton("Add Subject");
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateSubjectDialog(null).show();
        });
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);

        toolbar.getChildren().addAll(new HSpacer(), btn_add, btn_export);
        
        table = new CustomTableView<>();
        
        CustomTableColumn subjectName = new CustomTableColumn("SUBJECT NAME");
        subjectName.setPercentWidth(20);
        subjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        subjectName.setCellFactory(TextFieldTableCell.forTableColumn());
        subjectName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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
        
        CustomTableColumn type = new CustomTableColumn("TYPE");
        type.setPercentWidth(10);
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.setCellFactory(TextFieldTableCell.forTableColumn());
        type.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label();
                        HBox con = new HBox(10);
                        
                        if(!empty){
                            if(type.equalsIgnoreCase("0")){
                                //subjectType.getStyleClass().addAll("lbl", "lbl-danger");
                                subjectType.setText("Optional");
                            }else{
                                subjectType.setText("Core");
                                //subjectType.getStyleClass().addAll("lbl", "lbl-info");
                            }
                            con.getChildren().add(subjectType);
                            con.getStyleClass().add("cell-content");
                            setGraphic(con);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn cn = new CustomTableColumn("");
        cn.setPercentWidth(70);
        cn.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        cn.setCellFactory(TextFieldTableCell.forTableColumn());
        cn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        HBox actions = new HBox(10);
                        actions.setStyle("-fx-padding:0");
                        
                        if(!empty){
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit subject"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateSubjectDialog(AdminQuery.getSubjectByID(ID));
                            });
                            actions.getChildren().addAll(edit);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        table.getTableView().getColumns().addAll( subjectName, type, cn);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(subjectWorkService));
        
        ProgressIndicator pi = new ProgressIndicator("Loading subjects data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(subjectWorkService.runningProperty());
        table.getTableView().itemsProperty().bind(subjectWorkService.valueProperty());
        
        StackPane stackPane = new StackPane(table, pi);
        
        setCenter(stackPane);
        
        subjectWorkService.start();
    }
    
    
    public class SubjectListWork extends Task<ObservableList<Subject>> {       
        @Override 
        protected ObservableList<Subject> call() throws Exception {
            
            ObservableList<Subject> data; 
            
            if(departmentsController.selectedDepartment != null){
                data = AdminQuery.getSubjectListFor(departmentsController.selectedDepartment.getID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            return data;
        }
    }


    public class SubjectWorkService extends Service<ObservableList<Subject>> {

        @Override
        protected Task createTask() {
            return new SubjectListWork();
        }
    }
    
}
