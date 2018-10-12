package com.bitri.service.schooladministration;

import com.bitri.access.SIMS;
import com.bitri.service.schooladministration.control.*;
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
public class SchoolAdministartion extends BorderPane{
    
    private JFXListView<HBox> mainMenu;
    private AnchorPane  departments, streamClasses,
            housesCategories, batchesClasses;
    private BorderPane  systemUsers;
    
    public static StackPane ADMIN_MAN_STACK;
    public static BorderPane dashboardUI, academicTerms;

    /**
     * View controllers
     */
    public static DepartmentsController departmentsController;
    public static StreamClassesController streamClassesController;
    public static HousesCategoriesController houseController;
    public static BatchClassesController batchClassesController;
    public static DashboardController dashboardController;
    
    public static SchoolInformation schoolInformation;

    public SchoolAdministartion() {
        
        getStyleClass().add("container");

        //-- Admin Management Menu --
        mainMenu = new JFXListView<>();
        mainMenu.getStyleClass().add("main_menu");
        
        //-- Menu Items --
        VBox dash = new VBox(SIMS.getIcon("14_System_Task.png",   24));
        dash.getStyleClass().add("graphic-badge");
        HBox dashboard = new HBox(new Label("Dashboard",dash));
        
        VBox dept = new VBox(SIMS.getIcon("13_unit.png", 24));
        dept.getStyleClass().add("graphic-badge");
        HBox department_subject = new HBox(new Label("Departments And Subjects",dept));
        
        VBox batc = new VBox(SIMS.getIcon("team.png",   24));
        batc.getStyleClass().add("graphic-badge");
        HBox batches = new HBox(new Label("Batches and Classes",batc));
        
        VBox strm = new VBox(SIMS.getIcon("12_training.png",   24));
        strm.getStyleClass().add("graphic-badge");
        HBox streams = new HBox(new Label("Streams Management",strm));
        
        VBox scHouse = new VBox(SIMS.getIcon("hierarchy.png",  24));
        scHouse.getStyleClass().add("graphic-badge");
        HBox schoolHouses = new HBox(new Label("School Houses", scHouse));
        
        VBox trm = new VBox(SIMS.getIcon("10_settings.png",   24));
        trm.getStyleClass().add("graphic-badge");
        HBox terms = new HBox(new Label("Academic Terms",trm));
        
        VBox usr = new VBox(SIMS.getIcon("14_access.png",   24));
        usr.getStyleClass().add("graphic-badge");
        HBox users = new HBox(new Label("System Access Control", usr));
        
        VBox sch = new VBox(SIMS.getIcon("address_book.png", 24));
        sch.getStyleClass().add("graphic-badge");
        HBox school = new HBox(new Label("School Address Book", sch));
        

        mainMenu.getItems().addAll(department_subject, streams, schoolHouses, batches,
                                   //terms,
                                   users, school);
        
        //-- set the first item selected --
        mainMenu.getSelectionModel().select(0);
        
        mainMenu.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    
            switch (newValue.intValue()){
                case 0:
                    departments.toFront();
                    break;
                    
                case 1:
                    streamClasses.toFront();
                    break;

                case 2:
                    housesCategories.toFront();
                    break;
                    
                case 3:
                    batchesClasses.toFront();
                    break;

                case 4:
                    systemUsers.toFront();
                    break;
                case 5:
                    schoolInformation.toFront();
                    break;
                
                    
                default:
                    break;
            }
        });
        
                
        setLeft(mainMenu);
        
        try{
            //-- Student Management Views
            //dashboardUI = new DashboardStatistics();
            schoolInformation = new SchoolInformation();
            systemUsers = new SystemAccessControl();
            //academicTerms = new SchoolTerms();
            
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/dashboardUI.fxml"));
            dashboardUI = dashboardLoader.load();
            dashboardController = dashboardLoader.getController();
            
            FXMLLoader departmentLoader = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/departments.fxml"));
            departments = departmentLoader.load();
            departmentsController = departmentLoader.getController();
                        
            FXMLLoader streamLoader = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/streamClasses.fxml"));
            streamClasses = streamLoader.load();
            streamClassesController = streamLoader.getController();
            
            FXMLLoader houseLoader = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/housesCategories.fxml"));
            housesCategories = houseLoader.load();
            houseController = houseLoader.getController();
            
            
            FXMLLoader batch = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/batchClasses.fxml"));
            batchesClasses = batch.load();
            batchClassesController = batch.getController();
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        ADMIN_MAN_STACK = new StackPane(/*academicTerms,*/ systemUsers,
                schoolInformation, batchesClasses, housesCategories, streamClasses, departments);
        
        ADMIN_MAN_STACK.setPadding(new Insets(5));
        
        setCenter(ADMIN_MAN_STACK);
    }
    
}
