package com.bitri.service.studentmanagement.control;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.studentmanagement.ECStaffMembers;
import com.bitri.service.studentmanagement.ECStudentMembers;
import com.bitri.service.studentmanagement.ExtraCurriculaActivity;
import com.bitri.service.studentmanagement.UpdateExtraCurriculaActivityDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ofentse
 */
public class ExtraCurriculaController implements Initializable {
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_refresh;
    @FXML
    private MenuButton btn_export;
    @FXML
    private JFXListView<HBox> activity_ListView;
    @FXML
    private Label activityName;
    @FXML
    private Label total;
    @FXML
    private Tab membersTab, staffMembers;
    
    public ExtraCurriculaActivity selectedActivity = null;
    
    public int selectedIndex = 0;
    
    public ECStudentMembers ecStudentMembers = null;
    public ECStaffMembers ecStaffMembers = null;
    
    public ExtraCurriculaWorkService ecws;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ecws = new ExtraCurriculaWorkService();
        
        activityName.setText("");
        total.setText("");
        
        ecStudentMembers = new ECStudentMembers();
        ecStaffMembers = new ECStaffMembers();
        
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateExtraCurriculaActivityDialog(null);
        });
        
        // Create MenuItems
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));
 
        btn_export.setText("Export");
        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.getItems().addAll(pdf, excel, text);
        
        

        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            ecws.restart();
        });
        
        activity_ListView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            
            try{
                Label lb = (Label)activity_ListView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedActivity = AdminQuery.getActivityByName(lb.getText());
                selectedIndex = newValue.intValue(); 
                
                activityName.setText(selectedActivity.getName());
                
                if(!selectedActivity.getCoach().equalsIgnoreCase("")){
                    activityName.setText(selectedActivity.getName()+"- "+EmployeeQuery.getEmployeeByID(selectedActivity.getCoach()).getFullName());
                }
                
                ecStudentMembers.ams.restart();
                
            }catch(Exception ex){}
                
        });
        
        ecStudentMembers.ams.setOnSucceeded((WorkerStateEvent event) -> {
            ecStaffMembers.ams.restart();
        });
        
        activity_ListView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(ecws));
           
        membersTab.setContent(ecStudentMembers);
        membersTab.setText("Students");
        
        staffMembers.setContent(ecStaffMembers);
        staffMembers.setText("Staff");
        
        ecws.start();
        ecws.restart();
    }
    
    
    public void setStudentsTab(String st){
        membersTab.setText(st);
    }
    
    public void setStaffTab(String st){
        staffMembers.setText(st);
    }

    
    public class ExtraCurriculaWorker extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            
            ObservableList<ExtraCurriculaActivity> dt = AdminQuery.getExtraCurriculaActivities();
            
            for (ExtraCurriculaActivity activity: dt) {
                
                JFXButton total = new JFXButton( AdminQuery.getExtraCurriculaActivitiesMembers(activity.getId(), "Student", ecStudentMembers.all).size()+
                                                 AdminQuery.getExtraCurriculaActivitiesMembers(activity.getId(), "Staff", false).size()+"");
                total.setTooltip(new Tooltip("Total members"));
                total.getStyleClass().addAll("btn-info", "btn-xs", "always-visible");
                total.setPrefWidth(32);
                
                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit Stream"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateExtraCurriculaActivityDialog(activity);
                });
                
                HBox cn = new HBox(new Label(activity.getName()), new HSpacer(), edit, total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
                
                
            }
                        
            Platform.runLater(() -> {
                try {
                    activity_ListView.setItems(data);
                    total.setText(""+data.size());                    
                    if(selectedActivity != null){
                        activity_ListView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            });
            
            return data;
        }
       
    }

    public class ExtraCurriculaWorkService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new ExtraCurriculaWorker();
        }
    }

}
