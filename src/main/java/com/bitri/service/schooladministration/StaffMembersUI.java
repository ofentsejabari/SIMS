package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.employeemanagement.EmployeeModel;
import com.bitri.service.employeemanagement.EmployeeProfileStage;
import com.bitri.service.employeemanagement.UpdateEmployeeStage;
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
public class StaffMembersUI extends BorderPane{
    
    public static CustomTableView<EmployeeModel> table;
    public EmployeeWorkService ews;

    public StaffMembersUI() {
        
        getStyleClass().add("container");
        
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(5));
        
        ews = new EmployeeWorkService();
        
        HBox toolbar = new HBox(5);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(2));
        container.setTop(toolbar);
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);

        toolbar.getChildren().addAll(new HSpacer(), btn_export);
        
        table = new CustomTableView<>();
        
        CustomTableColumn id = new CustomTableColumn("ID #");
        id.setPercentWidth(19.9);
        id.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        setGraphic(new Label(ID));
                    }
                };
            }
        });
        
        CustomTableColumn fullname = new CustomTableColumn("FULL NANME");
        fullname.setPercentWidth(20);
        fullname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        fullname.setCellFactory(TextFieldTableCell.forTableColumn());
        fullname.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label name = new Label(type);
                        
                        if(!empty){
                            setGraphic(name);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn designation = new CustomTableColumn("DESIGNATION");
        designation.setPercentWidth(20);
        designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        designation.setCellFactory(TextFieldTableCell.forTableColumn());
        designation.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String cont, boolean empty) {
                        super.updateItem(cont, empty);
                        
                        if(!empty){
                            
                            setGraphic(new Label(cont));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn cn = new CustomTableColumn("");
        cn.setPercentWidth(40);
        cn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        cn.setCellFactory(TextFieldTableCell.forTableColumn());
        cn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        HBox con = new HBox(10);
                        
                        if(!empty){
                            
                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 16));
                            edit.setTooltip(new Tooltip("Edit class details"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new EmployeeProfileStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 16));
                            view.setTooltip(new Tooltip("Preview class profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                new UpdateEmployeeStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            
                            con.getChildren().addAll(view, edit);
                            
                            setGraphic(con);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });

        table.getTableView().getColumns().addAll(id, fullname, designation, cn);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        ProgressIndicator pi = new ProgressIndicator("Loading class data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(ews.runningProperty());
        table.getTableView().itemsProperty().bind(ews.valueProperty());
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(ews));
        
        StackPane stackPane = new StackPane(table, pi);
        container.setCenter(stackPane);
        
        setCenter(container);
        
        ews.start();
        ews.restart();
    }
    
    
    public class EmployeeListWork extends Task<ObservableList<EmployeeModel>> {       
        @Override 
        protected ObservableList<EmployeeModel> call() throws Exception {
            
            ObservableList<EmployeeModel> data; 
          
            if(departmentsController.selectedDepartment != null){
                data = EmployeeQuery.getDepartmentEmployeeList(departmentsController.selectedDepartment.getID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            for(EmployeeModel employee: data){
                employee.setFirstName(employee.getFullName());
                employee.setDesignation(EmployeeQuery.getEmployeeDesignationListById(employee.getDesignation()).getDescription());
            }
            return data;
        }
       
    }

    
    
    public class EmployeeWorkService extends Service<ObservableList<EmployeeModel>> {

        @Override
        protected Task createTask() {
            return new EmployeeListWork();
        }
    }
   
    
}
