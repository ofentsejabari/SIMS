package com.bitri.service.studentmanagement;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.service.schooladministration.BaseClass;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.CustomTextField;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateBaseClassRegister extends JFXDialog{

    private JFXDatePicker reg_date;
    private JFXComboBox<String> coach, type;
    JFXListView<HBox> register;
    ObservableList<HBox> regItems= FXCollections.observableArrayList();
    ObservableList<String> absent;
    JFXButton btn_refresh;
    CustomTextField search;
    BaseClass base_class;
    public static RegistrationWorkService rws;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateBaseClassRegister(BaseClass base_class) {
                    
        this.base_class=base_class;
        rws = new RegistrationWorkService();
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.setStyle("-fx-padding:25 5 15 20;");
        contentGrid.setVgap(10);
        contentGrid.setHgap(2);
        
        reg_date = new JFXDatePicker();
        reg_date.setPromptText("Registration date");
        reg_date.setPrefWidth(360);
        contentGrid.add(reg_date, 0, 0);
        
        register  = new JFXListView<>();
        register.setPrefWidth(360);
        contentGrid.add(register, 0, 2,4,32);
        
        //--search
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CLOSE, "text-error", 18));
        clear.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_SEARCH, "text-bluegray", 18));
        src.setStyle("-fx-background-radius:0 20 20 0; -fx-border-radius:0 20 20 0; -fx-cursor: hand;");
        
        
        HBox listviewControls = new HBox(5);
        listviewControls.getStyleClass().add("secondary-toolbar");
        
        
        btn_refresh = new JFXButton();
        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setTooltip(new Tooltip("Refresh Employee List"));
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-bluegray", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            rws.restart();
            search.clear();
        });
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        search.setRight(clear);
        
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String str = search.getText().trim(); 
            if(!register.getItems().isEmpty()){
                register.getItems().clear();
                if(!str.equals("") && str.length() > 0){
                     
                    System.out.println("str not empty");
                    
                    for(HBox hbox : regItems)
                    {
                        Label temp =(Label)hbox.getChildren().get(1);
                        
                        if(temp.getText().toLowerCase().contains(str.toLowerCase())){
                            if(!register.getItems().contains(hbox)){
                                register.getItems().add(hbox);
                            }
                        }
                    }
                    
                }else{
                    
                    System.out.println("here");
                    rws.restart();
                }
            }
        });
        
        search.setRight(src);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!newValue.trim().equalsIgnoreCase("")){
                search.setRight(clear);
            }else{
                search.setRight(src);
            }
        });
       listviewControls.getChildren().addAll(search,new HSpacer(),btn_refresh);
       contentGrid.add(listviewControls, 0, 1);
       
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Base Class Daily Register");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Activity"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!reg_date.getValue().equals(""))
            {
                
              //  if(StudentQuery.updateClassRegister(studentID, termID, date, classID))
            
            }else{
                
                new DialogUI("Please ensure that required fields are captured, before trying to update"
                                + " register",
                                    DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
            }
            
        });
        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(PARENT_STACK_PANE);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 550);
        show();
        
        
        rws.start();
        rws.restart();
    }
    
   
    public class RegistrationListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
           ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            
            if(!regItems.isEmpty()){ 
               regItems.clear();
           
           }
             ObservableList<Student> base_classStudent = SIMS.dbHandler.getBaseClassStudentList(base_class.getClassID());
        
       
        
            for (Student b: base_classStudent) {
                
                CheckBox att = new CheckBox();
                att.setSelected(true);
                
                
                HBox cn = new HBox(att,new Label(b.getFullName()));
                cn.getStyleClass().add("cell-content");
                cn.setPrefWidth(320);
                cn.setAlignment(Pos.CENTER_LEFT);
                cn.setSpacing(5);
                data.add(cn);
                regItems.add(cn);
                
            }
            Platform.runLater(() -> {
                try {
                    register.setItems(data);
                  
                } catch (Exception e) {
                }
                
            });
            
            return data;
        }
       
    }

    public class RegistrationWorkService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new RegistrationListWork();
        }
    }
}
