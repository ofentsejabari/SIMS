package com.bitri.service.employeemanagement;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.Subject;
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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.Optional;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author MOILE
 */
public class AcademicSubjectTableView extends BorderPane{
    
    CustomTableView<EmployeeModel> aceademicDepartmentTable;
    public static ObservableList<EmployeeModel> employeeSubjectList = FXCollections.observableArrayList();
    public static EmployeeSubjectListWorkService employeeSubjectListWork;
    public static SubjectWorkService subjectWork;
    public Label subjectName;
    JFXButton btn_refresh,btn_addTeacher;
    public static JFXListView<HBox> deptList;
    
    
    Subject selectedSubject = null;
    
    public static int selectedIndex = 0;
    
    public AcademicSubjectTableView(){
        
        deptList = new JFXListView<>();
        deptList.getStyleClass().add("jfx-custom-list");
        subjectWork = new SubjectWorkService();
        subjectName = new  Label();
        subjectName.getStyleClass().add("title-label");
        
        //--- deptList ---
        employeeSubjectListWork = new EmployeeSubjectListWorkService();
        
        VBox leftChild = new VBox(deptList);
        leftChild.setPadding(new Insets(2));
        setLeft(leftChild);
        VBox.setVgrow(deptList, Priority.ALWAYS);


        HBox header = new HBox(new Label("All Subjects"));
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");
        header.setAlignment(Pos.CENTER_LEFT);

        BorderPane leftPanel = new BorderPane(deptList);
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");
        leftPanel.setTop(header);

        HBox con = new HBox(leftPanel);
        con.setStyle("-fx-padding:0 5 0 0;");

        setLeft(con);
        
        
        setPadding(new Insets(10, 5, 5, 5));
        //------------------------------------------------------
        StackPane root = new StackPane();
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(0,5,0,5));
        root.getChildren().add(container);
        
        //---------------toolbar--------------------------------
        HBox toolBar = new HBox(5);
        
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
        
        MenuButton export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        export.setDisable(true);
        
        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Employee List"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-bluegray", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            subjectWork.restart();
        });
        
        btn_addTeacher = new JFXButton("Update Teachers");
        btn_addTeacher.getStyleClass().addAll("btn-xs", "btn-success");
        btn_addTeacher.setTooltip(new Tooltip("Add Teachers to subject"));
        btn_addTeacher.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_MULTIPLE_PLUS, "text-white", 18));
        btn_addTeacher.setOnAction((ActionEvent event) -> {
            if(selectedSubject!=null)
                  new UpdateSubjectTeachers(selectedSubject);
        });
        aceademicDepartmentTable = new CustomTableView<>();

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
                            
                            setGraphic(new Label(ID));
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
        
        CustomTableColumn department = new CustomTableColumn("DEPARTMENT");
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
                            
                            JFXButton edit = new JFXButton();
                            edit.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PENCIL,"text-bluegray", 18));
                            edit.setTooltip(new Tooltip("Edit Employee Profile"));
                            edit.getStyleClass().addAll("btn-xs", "btn-default");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateEmployeeStage(EmployeeQuery.getEmployeeByID(ID));
                            });

                            
                            JFXButton view = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.EYE, "text-bluegray", 18));
                            view.setTooltip(new Tooltip("View Employee profile"));
                            view.getStyleClass().addAll("btn-default", "btn-xs");
                            view.setOnAction((ActionEvent event) -> {
                                new EmployeeProfileStage(EmployeeQuery.getEmployeeByID(ID));
                            });
                            view.setDisable(true);
                            
                            JFXButton del = new JFXButton();
                            del.setGraphic(SIMS.getGraphics(MaterialDesignIcon.DELETE, "text-white", 18));
                            del.setTooltip(new Tooltip("Edit Student Social Welfare"));
                            del.getStyleClass().addAll("btn-danger", "btn-xs");
                            del.setOnAction((ActionEvent event) -> {
                                
                                EmployeeModel employee = EmployeeQuery.getEmployeeByID(ID);
                                
                                JFXAlert alert = new JFXAlert(Alert.AlertType.CONFIRMATION,"Remove Teacher",
                                        "Are you sure you want to remove this Teacher : ("+employee.getFullName()+")",
                                        ButtonType.YES, ButtonType.CANCEL);
                                
                                Optional <ButtonType> action= alert.showAndWait();

                                if(action.get()== ButtonType.OK){
                                    
                                    ObservableList<String> add = FXCollections.observableArrayList();
                                    ObservableList<String> remove = FXCollections.observableArrayList();
                                    remove.add(employee.getEmployeeID());
                                    if(EmployeeQuery.updateSubjectTeachers(add, remove,selectedSubject.getSubjectID())){

                                        new DialogUI("Subject Teacher removed",DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null).show();
                                        employeeSubjectListWork.restart();
                                    }
                                    else{
                                        new DialogUI("Exception occurred while trying to remove student from the class.",
                                                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
                                    }
                                }    
                            });
                            del.setDisable(true);
                            
                            actions.getChildren().addAll(view,edit,del);
                            setGraphic(actions);
                            
                        }else{ setGraphic(null); }
                        
                    }
                };
            }
        });
        
        aceademicDepartmentTable.getTableView().getColumns().addAll( staffID, fname,department ,controls);
        
        VBox ph = SIMS.setDataNotAvailablePlaceholder();
        aceademicDepartmentTable.getTableView().setPlaceholder(ph);
        
        ProgressIndicator pi = new ProgressIndicator("Loading Employee Data", "If network connection is very slow,"
                                           + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(employeeSubjectListWork.runningProperty());
        aceademicDepartmentTable.getTableView().itemsProperty().bind(employeeSubjectListWork.valueProperty());
        
        aceademicDepartmentTable.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(employeeSubjectListWork));
        
        container.setCenter(aceademicDepartmentTable);
        
        
        deptList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
       
            try {
                Label lb = (Label)deptList.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedSubject = AdminQuery.getSubjectByName(lb.getText());
                selectedIndex=newValue.intValue();
                subjectName.setText(lb.getText());
                
                employeeSubjectListWork.restart();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
       });
        
        
        deptList.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(subjectWork));
        
        toolBar.getChildren().addAll(subjectName,new HSpacer(),btn_refresh, btn_addTeacher, export);
      
        toolBar.getStyleClass().addAll("panel-heading", "panel-info");
        container.setTop(toolBar);
        setCenter(root);
        
        subjectWork.start();
        subjectWork.restart();
        
    }
    
    
    public class SubjectListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();

            ObservableList<Subject> subject = AdminQuery.getAllSubjectList();
        
            for (Subject b: subject) {
                
                JFXButton total = new JFXButton(""+EmployeeQuery.getSubjectEmployees(b.getSubjectID()).size());
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
                    deptList.setItems(data);
                    if(selectedSubject!=null)
                    {
                        deptList.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class SubjectWorkService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new SubjectListWork();
        }
    }
     
    public class EmployeeSubjectListWork extends Task<ObservableList<EmployeeModel>> {
        @Override 
        protected ObservableList<EmployeeModel> call() throws Exception {

           employeeSubjectList  = EmployeeQuery.getSubjectEmployees(selectedSubject.getSubjectID());
           for(EmployeeModel em: employeeSubjectList){
               em.setFirstName(em.getFullName());
           }
           return employeeSubjectList;
        } 
    }

    
    public class EmployeeSubjectListWorkService extends Service<ObservableList<EmployeeModel>> {

        @Override
        protected Task createTask() {
            return new EmployeeSubjectListWork();
        }
    }
}
