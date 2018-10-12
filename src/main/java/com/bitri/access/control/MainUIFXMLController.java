package com.bitri.access.control;

import com.bitri.access.*;
import com.bitri.service.employeemanagement.HumanResourceManagement;
import com.bitri.service.eventcalendar.JBEventCalendar;
import com.bitri.service.schooladministration.SchoolAdministartion;
import com.bitri.service.studentmanagement.StudentManagement;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXTabPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;
import org.controlsfx.tools.Borders;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author ofentse
 */
        public class MainUIFXMLController implements Initializable {

            @FXML
            private Circle profile_picture;

            @FXML
            private BorderPane parentContainer;

            @FXML
            private JFXButton btn_toolbar_help, btn_toolbar_about,  btn_drawer, btn_menu;

            @FXML
            private StackPane drawerStackPane;

            @FXML
            private HBox footer;

            public Image imageHolder;
            public static StackPane PARENT_STACK_PANE;

            public AnchorPane drawerContent;

            public static BorderPane studentManagement, employeeManagement,
                                     admin, financeManagement, inventoryManagement;//, messangerUI;
            public static JFXDrawersStack drawerStack;
            public static JFXDrawer jFXDrawer;

            public static BorderPane dashboard;

            public JFXTabPane jFXTabPane;

            @Override
            public void initialize(URL url, ResourceBundle rb) {

                btn_drawer.setText("");
                btn_drawer.setGraphic(SIMS.getGraphics(MaterialIcon.MENU, "text-white", 22));

                btn_toolbar_about.setGraphic(SIMS.getGraphics(MaterialIcon.LIVE_HELP, "text-white", 22));
                new PopOverToolTip(btn_toolbar_about, "System Help Manual", 250, 50, PopOver.ArrowLocation.TOP_RIGHT);

                btn_toolbar_help.setGraphic(SIMS.getGraphics(OctIcon.TOOLS, "text-white", 22));
                new PopOverToolTip(btn_toolbar_help, "System Configurations", 250, 50, PopOver.ArrowLocation.TOP_RIGHT);

                btn_menu.setText("");
                btn_menu.setGraphic(SIMS.getGraphics(MaterialIcon.KEYBOARD_ARROW_DOWN, "text-white", 22));
                btn_menu.setOnAction((ActionEvent event) -> {
                    new PopOverMenu(btn_menu, new UserMenu(), "", 300, 250,
                            PopOver.ArrowLocation.TOP_RIGHT, false);
                });

                //-- Update profile picture --
                imageHolder = SIMS.getIcon("male_user_100px.png").getImage();
                profile_picture.setFill(new ImagePattern(imageHolder));

                //-- Modules (Student Management, Inventory, ) --
                studentManagement   = new StudentManagement();
                employeeManagement  = new HumanResourceManagement();
                admin = new SchoolAdministartion();
                //financeManagement   = new FinanceManagement();
                //inventoryManagement = new InventoryManagement();
                //messangerUI         = new MessangerManagement();

                FlowPane fpane = new FlowPane(Orientation.HORIZONTAL);
                fpane.setPadding(new Insets(20));
                fpane.setAlignment(Pos.CENTER_LEFT);
                fpane.setHgap(10);
                fpane.setVgap(10);

                dashboard = new BorderPane(fpane);

                //-- Event calendar --
                JBEventCalendar calendar = new JBEventCalendar();

                VBox vBox = new VBox();
                vBox.getChildren().addAll(Borders.wrap(calendar)
                        .lineBorder()
                        .thickness(2, 1, 1, 1)
                        .innerPadding(0)
                        .radius(5)
                        .color(Color.web("#F0582F"), Color.web("#EAEAEA"),
                                Color.web("#EAEAEA"), Color.web("#EAEAEA"))
                        .buildAll());

                HBox.setHgrow(vBox, Priority.ALWAYS);
                dashboard.setRight(vBox);

                //-- End event calendar --
        try{
            drawerContent = FXMLLoader.load(getClass().getResource("/views/entryViews/NavigationDrawer.fxml"));
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }


        VBox vb = new VBox(5);
        vb.setAlignment(Pos.CENTER);

        ImageView bitri = SIMS.getIcon("bitri_logo.jpg");

        Label label = new Label("School Information Management System Under Development.");
        label.getStyleClass().add("title-label");
        label.setStyle("-fx-font-size:24");

        Label cont_label = new Label("CONTACT DETAILS");
        cont_label.getStyleClass().add("text-warning");
        cont_label.setStyle("-fx-font-size:18");

        Label m = new Label("Tshepo Moile");
        m.getStyleClass().add("title-label");

        Label moile = new Label("tmoile@bitri.co.bw | 73664426 | 3670640");
        moile.getStyleClass().add("text-label");
        moile.setStyle("-fx-font-size:14");

        Label o = new Label("Ofentse Jabari");
        o.getStyleClass().add("title-label");

        Label jabari = new Label("ojabari@bitri.co.bw | 72177941 | 3670642");
        jabari.getStyleClass().add("text-label");
        jabari.setStyle("-fx-font-size:14");

        vb.getChildren().addAll(bitri, label, cont_label, m, moile, o, jabari);

        dashboard.setCenter(vb);



        
        jFXTabPane = new JFXTabPane();
        jFXTabPane.getStyleClass().add("jfx-tab-pane");
        
        Tab db = new Tab("Dashboard", dashboard);
        db.setGraphic(SIMS.getGraphics(MaterialIcon.DASHBOARD, "text-indigo", 28));
        
        Tab emlm = new Tab("Employees", employeeManagement);
        emlm.setGraphic(SIMS.getGraphics(MaterialIcon.GROUP, "text-indigo", 28));

        Tab stdm = new Tab("Students", studentManagement);
        stdm.setGraphic(SIMS.getGraphics(FontAwesomeIcon.MORTAR_BOARD, "text-indigo", 28));

        Tab lbm = new Tab("Administration", admin);
        lbm.setGraphic(SIMS.getGraphics(FontAwesomeIcon.COGS, "text-indigo", 28));
        
//        Tab invm = new Tab("Inventory", inventoryManagement);
//        invm.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT, "text-indigo", 26));

//        Tab finm = new Tab("Finance", financeManagement);
//        finm.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-indigo", 26));

//        Tab timetable = new Tab("Timetable", null);
//        timetable.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-indigo", 26));

        jFXTabPane.getTabs().addAll(db,  lbm, stdm, emlm);

        PARENT_STACK_PANE = new StackPane(jFXTabPane);
        
        parentContainer.setCenter(PARENT_STACK_PANE);

        drawerStack = new JFXDrawersStack();
        drawerStack.setContent(parentContainer);
        
        jFXDrawer = new JFXDrawer();
        
        jFXDrawer.setDirection(JFXDrawer.DrawerDirection.LEFT);
        jFXDrawer.setDefaultDrawerSize(200);
        jFXDrawer.setSidePane(drawerContent);
        jFXDrawer.setOverLayVisible(true);
        jFXDrawer.setResizableOnDrag(false);
        
        btn_drawer.setOnAction((ActionEvent event) -> {
            drawerStack.toggle(jFXDrawer);
        });
        
        drawerStackPane.getChildren().addAll(drawerStack);
        
        footer.getChildren().add(new Footer());

    }  
    
    public StackPane getParentStackPane(){
        return drawerStackPane;
    }
}
