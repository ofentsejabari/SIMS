package com.bitri.access;

import com.bitri.resource.dao.MySQLHander;
import com.bitri.service.schooladministration.School;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.tools.Borders;

import java.time.LocalDate;
import java.util.Date;

/**
 * Application launcher for SIMS
 * @author ofentse
 */
public class SIMS extends Application {

    private double initX, initY;
    public static Stage PARENT_STAGE;
    public static MySQLHander dbHandler;
    public static StackPane MAIN_UI;
    public static School ACTIVE_SCHOOL;

    @Override
    public void start(Stage stage) throws Exception {

        dbHandler = new MySQLHander();
        ACTIVE_SCHOOL = dbHandler.getSchoolByID("100");

        PARENT_STAGE = stage;
        PARENT_STAGE.setTitle("School Management System");

        MAIN_UI = new StackPane();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/entryViews/MainUIFXML.fxml"));
        Parent root = loader.load();

        MAIN_UI.getChildren().add(root);

        Scene scene = new Scene(MAIN_UI, Color.TRANSPARENT);
        scene.getStylesheets().addAll(SIMS.class.getResource("/css/bootstrapfx.css").toExternalForm(),
                SIMS.class.getResource("/css/default.css").toExternalForm());
        PARENT_STAGE.setScene(scene);
        PARENT_STAGE.show();
        //PARENT_STAGE.setFullScreen(true);

        MAIN_UI.setOnMousePressed((MouseEvent me) -> {
            initX = me.getScreenX() - PARENT_STAGE.getX();
            initY = me.getScreenY() - PARENT_STAGE.getY();
        });

        MAIN_UI.setOnMouseDragged((MouseEvent me) -> {
            PARENT_STAGE.setX(me.getScreenX() - initX);
            PARENT_STAGE.setY(me.getScreenY() - initY);
        });

        PARENT_STAGE.setOnCloseRequest((WindowEvent event) -> {
            PARENT_STAGE.close();
            Footer.serverMonitor.stop();
            System.exit(0);
        });
    }


    public static void main(String[] args) {

        /**
         * Loading application custom fonts.
         *
         * Load fonts through css may produce the following warning if the file path contains any spaces.(BITRI%20PROJECTS)
         * INFO: Could not load @font-face font
         *  [file:/C:/Users/ojabari/Documents/BITRI%20PROJECTS/sims/target/classes/font/roboto/Roboto-Light.ttf]
         * If there's a space in the font file's pathname, it won't load.
         * This means that we shouldn't load fonts from a CSS file, unless we're sure the file path won't contain any spaces.
         */
        Font.loadFont(SIMS.class.getResourceAsStream("/font/Calligraffitti/Calligraffitti-Regular.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/Romanesco/Romanesco-Regular.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/roboto/Roboto-Light.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/roboto/Roboto-Regular.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/roboto/Roboto-Medium.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/oxygen/Oxygen-Regular.ttf"), 16);
        Font.loadFont(SIMS.class.getResourceAsStream("/font/Do_Hyeon/DoHyeon-Regular.ttf"), 16);

        launch(args);
    }


    /**
     *
     * @param content
     * @param title
     * @return
     */
    public static VBox setBorderContainer(Node content , String title) {
        VBox conDetails = new VBox();

        if(title != null){
            conDetails.getChildren().addAll(Borders.wrap(content)
                    .lineBorder()
                    .title(title)
                    .thickness(2, 1, 1, 1)
                    .innerPadding(0)
                    .outerPadding(20, 5, 5, 5)
                    .radius(5)
                    .color(Color.web("#cfd8dc"), Color.web("#EAEAEA"),
                            Color.web("#EAEAEA"), Color.web("#EAEAEA"))
                    .buildAll());
        }else{
            conDetails.getChildren().addAll(Borders.wrap(content)
                    .lineBorder()
                    .thickness(2, 1, 1, 1)
                    .innerPadding(0)
                    .outerPadding(10, 5, 5, 5)
                    .radius(5)
                    .color(Color.web("#cfd8dc"), Color.web("#EAEAEA"),
                            Color.web("#EAEAEA"), Color.web("#EAEAEA"))
                    .buildAll());
        }

        return conDetails;
    }




    /**
     *
     * @param content
     * @param title
     * @param color
     * @return
     */
    public static VBox setBorderContainer(Node content, String title, String color) {
        VBox conDetails = new VBox();

        if(title != null){
            conDetails.getChildren().addAll(Borders.wrap(content)
                    .lineBorder()
                    .title(title)
                    .thickness(2, 1, 1, 1)
                    .innerPadding(0)
                    .outerPadding(5)
                    .radius(5)
                    .color(Color.web(color), Color.web("#EAEAEA"),
                            Color.web("#EAEAEA"), Color.web("#EAEAEA"))
                    .buildAll());
        }else{
            conDetails.getChildren().addAll(Borders.wrap(content)
                    .lineBorder()
                    .thickness(2, 1, 1, 1)
                    .innerPadding(0)
                    .outerPadding(5)
                    .radius(5)
                    .color(Color.web(color), Color.web("#EAEAEA"),
                            Color.web("#EAEAEA"), Color.web("#EAEAEA"))
                    .buildAll());
        }
        return conDetails;
    }


    /**
     * Load an icon from the resource folder
     * @param iconName - the icon to load
     * @return ImageView
     */
    public static ImageView getIcon(String iconName){
        ImageView img = new ImageView(new Image(SIMS.class.getResourceAsStream("/icons/"+iconName)));
        return img;
    }


    /**
     * Load an icon from the resource folder
     * @param iconName
     * @param height
     * @return ImageView
     */
    public static ImageView getIcon(String iconName, int height){
        ImageView img = new ImageView(new Image(SIMS.class.getResourceAsStream("/icons/"+iconName)));
        img.setFitHeight(height);
        img.setPreserveRatio(true);
        return img;
        
    }



    /**
     *
     * @param icon
     * @param iconColor
     * @param height
     * @return
     */
    public static Text getGraphics(GlyphIcons icon, String iconColor, int height){

        Text icn = GlyphsDude.createIcon(icon, ""+height);
        icn.getStyleClass().add(iconColor);
        return icn;

    }


    /**
     *
     * @param dt
     * @return
     */
    public static LocalDate getLocalDate(String dt){
        try {
            if(dt == null){
                return null; }

            if(!"".equals(dt)){
                String[] date = dt.split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);
                LocalDate localdate = LocalDate.of(year, month, day);

                return localdate;
            }
            return null;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }



    /**
     *
     * @param service
     * @return
     */
    public static VBox setDataNotAvailablePlaceholder(Service service){

        Label lb = new Label("No data available");
        lb.setStyle("-fx-text-fill:orangered; -fx-font-family:'Segoe UI Light';-fx-font-size:22");

        Label lb2 = new Label("Probably due to:\n"
                + "1. No network connection to server.\n"
                + "2. No data captured yet.\n"
                + "3. If non of the above, try to 'refresh'");
        lb2.setStyle("-fx-text-fill: #515151 ;-fx-font-family:'Segoe UI Semibold';-fx-font-size:11");

        JFXButton refresh = new JFXButton("Refresh View", getGraphics(MaterialDesignIcon.ROTATE_3D, "default", 24));
        refresh.getStyleClass().addAll("btn-xs","btn-default" );
        refresh.setOnAction((ActionEvent event) -> {
            service.restart();
        });


        VBox con = new VBox( lb, lb2, refresh);
        con.setSpacing(5);
        con.setStyle("-fx-background-color:#fff");
        con.setAlignment(Pos.CENTER);

        return con;
    }



    /**
     *
     * @return
     */
    public static VBox setDataNotAvailablePlaceholder(){

        Label lb = new Label("No data available at the moment");
        lb.setStyle("-fx-text-fill:orangered; -fx-font-family:'Segoe UI Light';-fx-font-size:22");

        Label lb2 = new Label("No data recorded at the moment. May be due to:\n"
                + "1. No network connection to server"
                + "2. No data bas been captured"
                + "3. If non of the above, try to refresh");
        lb2.setStyle("-fx-text-fill: #515151 ;-fx-font-family:'Segoe UI Semibold'");

        JFXButton refresh = new JFXButton("Refresh View", getGraphics(MaterialDesignIcon.ROTATE_3D, "text-white", 26));
        refresh.getStyleClass().addAll("btn","btn-success" );
        refresh.setOnAction((ActionEvent event) -> {
            //service.restart();
        });

        VBox con = new VBox(refresh, lb, lb2);
        con.setSpacing(5);
        con.setAlignment(Pos.CENTER);

        return con;
    }



    /**
     *
     * @return
     */
    public static String generateDBID(){

        Date date = new Date();
        String str = String.format("%tc",date);
        String st[] = str.split(" ");
        String tym[] = st[3].split(":");

        return(st[2]+""+tym[0]+""+tym[1]+""+tym[2]);
    }

}
