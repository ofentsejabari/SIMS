package com.bitri.service.studentmanagement;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.BaseClass;
import com.bitri.service.schooladministration.Subject;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author ofentse
 */
public class BaseClassProfileUI extends JFXDialog{
    
    public static Student student = null;
   
    public JFXComboBox<String> parents;
    
    private Guardian selectedParent = null;
    private FlowPane subjectsPane;
    public static JFXListView<HBox> subjectList;
    JFXButton base_btn;
            
    Subject selectedSubject = null;
    
    public static int selectedIndex = 0;
    
    public BaseClassProfileUI(BaseClass bClass){
                
        StackPane root = new StackPane();
        BorderPane pane = new BorderPane();
        
        //-- Screen Decoration -------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label(bClass.getName());
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        pane.setTop(toolBar);
        //-- End Screen Decoration ---------------------------------------------
        
        
        //-- Profile Picture ---------------------------------------------------
        Image place_holder = new Image(SIMS.class.getResourceAsStream("/icons/crosswalk_100px.png"));
        
        Circle classProfile = new Circle(45);
        classProfile.setTranslateX(0);
        classProfile.setTranslateY(0);
        classProfile.setCenterX(30);
        classProfile.setCenterY(30);
        classProfile.setEffect(new DropShadow(3, Color.web("#EAEAEA")));
        classProfile.setStroke(Color.web("#cfd8dc"));
        classProfile.setStrokeWidth(1);
        classProfile.setFill(Color.web("#EAEAEA"));
        classProfile.setFill(new ImagePattern(place_holder));
        
        
        VBox con = new VBox(classProfile);
        con.setAlignment(Pos.CENTER);
        con.setPadding(new Insets(10));
        con.getStyleClass().add("profile-container");

        Label fullname = new Label();
        fullname.getStyleClass().add("title-label");
        subjectList = new JFXListView<>();
        subjectList.getStyleClass().add("jfx-custom-list");
        VBox.setVgrow(subjectList, Priority.ALWAYS);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-padding:20 0 0 0;");
        grid.setVgap(15);
       
        base_btn = new JFXButton("Base Class Management");
        base_btn.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "blue-gray",20));
        base_btn.setPrefWidth(210);
        base_btn.getStyleClass().addAll("btn-xs", "btn-default");
        grid.add(base_btn, 0, 0);
        
        
        grid.add(subjectList, 0,1);
        
        VBox leftVBox = new VBox();
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.getStyleClass().add("profile-view");
        leftVBox.getChildren().addAll(con, fullname, grid);
        pane.setLeft(leftVBox);
        
        //-- End Profile Picture -----------------------------------------------
        StackPane contentStack = new StackPane();
        BorderPane content = new BorderPane();//base class
        BorderPane subject_content = new BorderPane();//base class
        
        content.getStyleClass().add("container");
        subject_content.getStyleClass().add("container");
        
        contentStack.getChildren().addAll(subject_content,content);
        content.setStyle("-fx-padding:10;");
        
        JFXTabPane jfxtp = new JFXTabPane();
        jfxtp.getStyleClass().add("jfx-tab-flatpane");
        
        Tab assessment = new Tab("Assessments");
        Tab attendance = new Tab("Attendance");
        Tab finance = new Tab("Finance");
        Tab socialWelfare = new Tab("Social Welfare Support");
        Tab specialNeeds = new Tab("Special Needs");
        
        jfxtp.getTabs().addAll(assessment,attendance, finance, specialNeeds, socialWelfare);
        
        //--tab content
        
        specialNeeds.setContent(new BaseClassSpecialNeeds(bClass));
        socialWelfare.setContent(new BaseClassSocialWelfare(bClass));
        attendance.setContent(new BaseClassAttendanceRecord(bClass));
        finance.setContent(new BaseClassFinanceRecords(bClass));
        
        content.setCenter(jfxtp);
        
        JFXTabPane subjectTab = new JFXTabPane();
        subjectTab.getStyleClass().add("jfx-tab-flatpane");
        Tab sub_assessment = new Tab("Assessments");
        
        subjectTab.getTabs().addAll(sub_assessment);
        subject_content.setCenter(subjectTab);
        
        base_btn.setOnAction((ActionEvent event) -> {
            content.toFront();
        
        });
        
        
//----------------------------------------------------------------------
        pane.setCenter(contentStack);
        
        
        root.getChildren().add(pane);
        root.setPrefSize(1000, 520); 
        
        setDialogContainer(PARENT_STACK_PANE);
        setContent(root);
        setOverlayClose(false);
        show();
        
    }
    
    public class SubjectListWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {
            
            });
            ObservableList<Subject> subject = AdminQuery.getAllSubjectList();
        
            for (Subject b: subject) {
                
                JFXButton total = new JFXButton("");
                total.setTooltip(new Tooltip("Number of students"));
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
                    subjectList.setItems(data);
                    if(selectedSubject!=null)
                    {
                        subjectList.getSelectionModel().select(selectedIndex);
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
}
