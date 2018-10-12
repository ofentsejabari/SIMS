package com.bitri.service.employeemanagement;

import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author MOILE
 */
public class EmployeeAssignmentsView extends BorderPane{

    JFXTabPane academicStaff;
    
    public EmployeeAssignmentsView() {
    
        setPadding(new Insets(10));
        getStyleClass().add("container");
        
        academicStaff = new JFXTabPane();
        Tab departments = new Tab("Subject Assignments");
        Tab class_assign = new Tab("Class Assignments");
        
        academicStaff.getStyleClass().add("jfx-tab-flatpane");
        academicStaff.getTabs().addAll(departments,class_assign);
        
        departments.setContent(new AcademicSubjectTableView());
        class_assign.setContent(new AcademicClassesView());
        
        setCenter(academicStaff);
    
    }

    
    
}
