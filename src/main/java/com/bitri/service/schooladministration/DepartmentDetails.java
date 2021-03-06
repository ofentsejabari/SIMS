package com.bitri.service.schooladministration;

import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;

/**
 *
 * @author ofentse
 */
public class DepartmentDetails extends BorderPane{

    private final JFXTextField name, hod;
    public DepartmentDetailsService dds;
    
    public PieChart strength, subjects;

    public DepartmentDetails() {
        
        dds = new DepartmentDetailsService();
        
        setPadding(new Insets(10));
        
//        HBox toolbar = new HBox();
//        toolbar.getStyleClass().add("secondary-toolbar");
//        setTop(toolbar);
//        
//        JFXButton btn_add = new JFXButton("Update Grading Scheme");
//        btn_add.getStyleClass().add("jfx-tool-button");
//        btn_add.setGraphic(com.bitri.access.SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
//        btn_add.setOnAction((ActionEvent event) -> {
//            new UpdateDepartmentDialog(null).show();
//        });
//        
//        
//        toolbar.getChildren().addAll(new HSpacer(), btn_add);
        
        
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 5 5 5;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(10);
        
        
        name = new JFXTextField();
        name.setPromptText("DEPARTMENT NAME");
        name.prefWidthProperty().bind(contentGrid.widthProperty());
        name.setLabelFloat(true);
        contentGrid.add(name, 0, 0);
        name.setDisable(true);
        
        
        hod = new JFXTextField();
        hod.setPromptText("HEAD OF DEPARTMENT (HOD)");
        hod.prefWidthProperty().bind(contentGrid.widthProperty());
        hod.setLabelFloat(true);
        contentGrid.add(hod, 1, 0);
        hod.setDisable(true);
        
        //--
        
        strength = new PieChart();
        strength.setLabelLineLength(5);
        
        VBox stBox = new VBox(strength);
        stBox.getStyleClass().add("container");
        
        contentGrid.add(SIMS.setBorderContainer(stBox, "Department Strength"), 0, 1);
        
        //-- 
        subjects = new PieChart();
        subjects.setLabelLineLength(5);
        
        VBox sbBox = new VBox(subjects);
        sbBox.getStyleClass().add("container");
        
        contentGrid.add(SIMS.setBorderContainer(sbBox, "Subject Strengths"), 1, 1);
        
        setCenter(contentGrid);
        
        dds.start();
        dds.restart();
    }
    
    
    public class DepartmentDetailsWork extends Task<Integer> {       
        @Override 
        protected Integer call() throws Exception {
            Department department = SchoolAdministartion.departmentsController.selectedDepartment;
            
            Platform.runLater(() -> {
                
                name.setText(department.getDepartmentName());
                hod.setText(EmployeeQuery.getEmployeeByID(department.getHod()).getFullNameWithInitials());
                
                /*ObservableList<Employee> all = EmployeeQuery.getEmployeeList(true, "");
                ObservableList<Employee> dep = EmployeeQuery.getEmployeeList(true, department.getID());
                
                strength.setData(FXCollections.observableArrayList(
                new PieChart.Data("Other - "+(all.size()- dep.size()), all.size()- dep.size()),
                new PieChart.Data(department.getDepartmentName()+" - "+dep.size(), dep.size())));
                */
                ObservableList<Subject> subjectsList = AdminQuery.getSubjectListFor(department.getID());
                
                ObservableList<PieChart.Data>  data = FXCollections.observableArrayList();
                
                for(Subject subj : subjectsList){
                    int sz = AdminQuery.getSubjectTeachersNames(subj.getSubjectID()).size();
                   data.add(new PieChart.Data(subj.getName()+" - "+sz, sz));
                }
                
                subjects.setData(data);
                
            });
            
            return 1;
        }
    }

    
    public class DepartmentDetailsService extends Service<Integer> {

        @Override
        protected Task createTask() {
            return new DepartmentDetailsWork();
        }
    }
   
    
    
}
