package com.bitri.service.studentmanagement;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.service.studentmanagement.control.ExtraCurriculaController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 *
 * @author ofentse
 */
public class StudentManagement extends BorderPane{
    
    private JFXListView<HBox> mainMenu;
    private AnchorPane dashboardUI, extraCurricula;
    public static StackPane STUDENT_MAN_STACK;
    private BorderPane studentWelfareUI;
    public static ExtraCurriculaController extraCurriculaController;
    
    public static StudentAdmin admin;
    
    public StudentManagement() {
        
        getStyleClass().add("container");
        
        //-- Student Management Menu --
        mainMenu = new JFXListView<>();
        mainMenu.getStyleClass().add("main_menu");
        
        //-- Menu Items --
        JFXButton dashboardHelp = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.HELP, "text-white", 14));
        dashboardHelp.setTooltip(new Tooltip("Update class"));
        dashboardHelp.getStyleClass().addAll("btn-info", "btn-xs");
        
        VBox dash = new VBox(SIMS.getIcon("14_System_Task.png", 26));
        dash.getStyleClass().add("graphic-badge");
        HBox dashboard = new HBox(new Label("Dashboard", dash), new HSpacer());//, dashboardHelp);

        VBox enr = new VBox(SIMS.getIcon("students.png", 26));
        enr.getStyleClass().add("graphic-badge");
        HBox enrolment = new HBox(new Label("Student Administration", enr), new HSpacer());
        
        VBox att = new VBox(SIMS.getIcon("report.png", 26));
        att.getStyleClass().add("graphic-badge");
        HBox attendance = new HBox(new Label("Attendance", att) , new HSpacer());
        
        VBox wel = new VBox(SIMS.getIcon("welfare.png", 26));
        wel.getStyleClass().add("graphic-badge");
        HBox welfare = new HBox(new Label("Student Welfare", wel), new HSpacer());
        
        VBox ass = new VBox(SIMS.getIcon("1_students.png", 26));
        ass.getStyleClass().add("graphic-badge");
        HBox assessment = new HBox(new Label("Assessment", ass), new HSpacer());
        
        VBox ext = new VBox(SIMS.getIcon("team.png", 26));
        ext.getStyleClass().add("graphic-badge");
        HBox extraCurrActivity  = new HBox(new Label("Extra Curricula Activity", ext), new HSpacer());

        mainMenu.getItems().addAll(//dashboard,
                enrolment,// attendance, assessment,
                welfare, extraCurrActivity);
        
        //-- set the first item selected --
        mainMenu.getSelectionModel().select(0);
        
        mainMenu.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            switch (newValue.intValue()){
                case 0:
                    admin.toFront();
                    break;
                case 1:
                    studentWelfareUI.toFront();
                    break;
                case 2:
                    extraCurricula.toFront();
                    break;
                default:
                    break;
            }
        });
        
        setLeft(mainMenu);
        
        try {
            //-- Student Management Views --
            dashboardUI = FXMLLoader.load(getClass().getResource("/views/studentmanagementViews/dashboard.fxml"));
            
            admin = new StudentAdmin();
            
            studentWelfareUI = new StudentWelfare();
            
            FXMLLoader activity = new FXMLLoader(getClass().getResource("/views/studentmanagementViews/extraCurriculaActivity.fxml"));
            extraCurricula = activity.load();
            extraCurriculaController = activity.getController();
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        STUDENT_MAN_STACK = new StackPane(extraCurricula, studentWelfareUI, admin);
        STUDENT_MAN_STACK.setPadding(new Insets(5));
        
        setCenter(STUDENT_MAN_STACK);
        
    }
    
}
