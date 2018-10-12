package com.bitri.service.schooladministration;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author ofentse
 */
public class DashboardStatistics extends BorderPane{

    //private final JFXTextField name, hod;
    public DepartmentDetailsService dds;
    
    public PieChart strength, departments;

    public DashboardStatistics() {
        
        dds = new DepartmentDetailsService();
        
        //setPadding(new Insets(10));
        getStyleClass().add("container");
        
        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("secondary-toolbar");
        //setTop(toolbar);
        
        JFXButton btn_add = new JFXButton("Reload");
        btn_add.getStyleClass().add("jfx-tool-button");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-default", 24));
        btn_add.setOnAction((ActionEvent event) -> {
            dds.restart();
        });
        
        
        toolbar.getChildren().addAll(new HSpacer(), btn_add);
        
        
        GridPane contentGrid = new GridPane();
        contentGrid.setAlignment(Pos.CENTER);
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:15 10 10 10;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(10);
              
        //-- 
        departments = new PieChart();
        departments.setLabelLineLength(5);
        
        VBox sbBox = new VBox(departments);
        sbBox.getStyleClass().add("container");
        
        contentGrid.add(SIMS.setBorderContainer(sbBox, "DEPARTMENTS STRENGTH", "#1E90FF"), 0, 1);
        
        //-- 
        
        strength = new PieChart();
        strength.setLabelLineLength(5);
        
        VBox stBox = new VBox(strength);
        stBox.getStyleClass().add("container");
        
        contentGrid.add(SIMS.setBorderContainer(stBox, "Departments"), 1, 1);
        
        
        
        //setCenter(contentGrid);
        
        dds.start();
        dds.restart();
    }
    
    
    public class DepartmentDetailsWork extends Task<Integer> {       
        @Override 
        protected Integer call() throws Exception {
            Platform.runLater(() -> {
                
                ObservableList<Department> departmentList = AdminQuery.getDepartments();
                
                ObservableList<PieChart.Data>  data = FXCollections.observableArrayList();
                
                for(Department dept : departmentList){
                    int sz = EmployeeQuery.getEmployeeList().size();
                   data.add(new PieChart.Data(dept.getDepartmentName()+" - "+sz, sz));
                }
                
                departments.setData(data);
                
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
   
    
    
    
    
    /*
        import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class Sampler extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {  (1)
        Panel panel = new Panel("This is the title");
        panel.getStyleClass().add("panel-primary");           (2)
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));
        Button button = new Button("Hello BootstrapFX");
        button.getStyleClass().setAll("btn","btn-danger");    (2)
        content.setCenter(button);
        panel.setBody(content);

        Scene scene = new Scene(panel);
        scene.getStylesheets().add("bootstrapfx.css");        (3)

        primaryStage.setTitle("BootstrapFX");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
    */
    
}
