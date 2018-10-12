package com.bitri.service.schooladministration;

import com.bitri.access.CustomTableColumn;
import com.bitri.access.CustomTableView;
import com.bitri.access.ProgressIndicator;
import com.bitri.access.SIMS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import static com.bitri.service.schooladministration.SchoolAdministartion.houseController;

/**
 *
 * @author ofentse
 */
public class HouseClassesList extends BorderPane{
    
    public static CustomTableView<BaseClass> table;
    public HouseClassWorkService hcws;
    
    public HouseClassesList() {
        
        setPadding(new Insets(10, 5, 5, 5));
        hcws = new HouseClassWorkService();
                
        table = new CustomTableView<>();
        
        CustomTableColumn cn = new CustomTableColumn("NAME");
        cn.setPercentWidth(15);
        cn.setCellValueFactory(new PropertyValueFactory<>("name"));
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
        
        CustomTableColumn className = new CustomTableColumn("BASE TEACHER");
        className.setPercentWidth(20);
        className.setCellValueFactory(new PropertyValueFactory<>("baseTeacherID"));
        className.setCellFactory(TextFieldTableCell.forTableColumn());
        className.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Hyperlink description = new Hyperlink("");
                        description.getStyleClass().add("tableLink");
                        
                        if(!empty){
                            description.setText(ID);
                            setGraphic(description);                           
                            description.setOnAction((ActionEvent event) -> {
                                   
                            });
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn students = new CustomTableColumn("# OF STUDENTS");
        students.setPercentWidth(74.9);
        students.setCellValueFactory(new PropertyValueFactory<>("classID"));
        students.setCellFactory(TextFieldTableCell.forTableColumn());
        students.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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

        
        table.getTableView().getColumns().addAll(cn, className, students);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(hcws));
        
        ProgressIndicator pi = new ProgressIndicator("Loading class data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(hcws.runningProperty());
        table.getTableView().itemsProperty().bind(hcws.valueProperty());
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(hcws));
        
        //--
        StackPane stackPane = new StackPane(table, pi);
        setCenter(stackPane);
        
        hcws.start();
        hcws.restart();
    }
    
    
    
    public class HouseClassListWork extends Task<ObservableList<BaseClass>> {       
        @Override 
        protected ObservableList<BaseClass> call() throws Exception {
            
            ObservableList<BaseClass> data; 
          
            if(houseController.selectedHouse != null){
                data = AdminQuery.getHouseClassList(houseController.selectedHouse.getID());
            }else{ 
                data = FXCollections.observableArrayList();
            }
            
            for (BaseClass bc: data) {
               bc.setBaseTeacherID(EmployeeQuery.getEmployeeByID(bc.getBaseTeacherID()).getFullName());
               bc.setClassID(""+ SIMS.dbHandler.getBaseClassStudentList(bc.getClassID()).size());
            }         
            return data;
        }
    }

    public class HouseClassWorkService extends Service<ObservableList<BaseClass>> {

        @Override
        protected Task createTask() {
            return new HouseClassListWork();
        }
    }
      
}
