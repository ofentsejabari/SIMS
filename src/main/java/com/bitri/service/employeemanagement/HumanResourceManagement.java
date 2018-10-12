package com.bitri.service.employeemanagement;

import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 *
 * @author ofentse
 */
public class HumanResourceManagement extends BorderPane{
    
    private JFXListView<HBox> mainMenu;
    private AnchorPane dashboardUI;
    private BorderPane employeeManagementView, employeeAssignmentsView,leaveManagementView;
    public static StackPane HR_MAN_STACK;

    public HumanResourceManagement() {
        
        getStyleClass().add("container");
        
        //-- Student Management Menu --
        mainMenu = new JFXListView<>();
        mainMenu.getStyleClass().add("main_menu");
        
        //-- Menu Items --
        VBox dash = new VBox(SIMS.getIcon("14_System_Task.png",   24));
        dash.getStyleClass().add("graphic-badge");
        HBox dashboard = new HBox(new Label("Dashboard",dash));
        
        VBox em = new VBox(SIMS.getIcon("applicant_100px.png",   24));
        em.getStyleClass().add("graphic-badge");
        HBox employeeManagement = new HBox(new Label("Employee Management",em));
        
        VBox ea = new VBox(SIMS.getIcon("applicant_100px.png",   24));
        ea.getStyleClass().add("graphic-badge");
        HBox employeeAssignments = new HBox(new Label("Subject Class Assignment",ea));
        
        
        VBox lvs = new VBox(SIMS.getIcon("inspection_100px.png",   24));
        lvs.getStyleClass().add("graphic-badge");
        HBox leaves = new HBox(new Label("Leave Management",lvs));
        
        VBox appr = new VBox(SIMS.getIcon("bunk_bed.png",   24));
        appr.getStyleClass().add("graphic-badge");
        HBox appraisal = new HBox(new Label("Employee Appraisal",appr));
        
        mainMenu.getItems().addAll(//dashboard,
                                    employeeManagement,
                                    employeeAssignments
                                    //leaves,appraisal
                                  );
        
        //-- set the first item selected --
        mainMenu.getSelectionModel().select(0);
        
        mainMenu.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            switch (newValue.intValue()){
                case 0:
                    employeeManagementView.toFront();
                    break;
                case 1:
                    employeeAssignmentsView.toFront();
                    break;
                case 2:
                    leaveManagementView.toFront();
                    break;
                default:
                    break;
            }
        });
        
        setLeft(mainMenu);
        
        try {
            //-- Student Management Views
            dashboardUI = FXMLLoader.load(getClass().getResource("/views/employeemanagementViews/dashboard.fxml"));
            employeeManagementView = new EmployeeManagementView();
            employeeAssignmentsView = new EmployeeAssignmentsView();
            leaveManagementView = new EmployeeLeaveManagementView();

        } catch (IOException ex) {
        }
        
        HR_MAN_STACK = new StackPane(employeeAssignmentsView,employeeManagementView);
        HR_MAN_STACK.setPadding(new Insets(5));
        setCenter(HR_MAN_STACK);
    }
    
}
