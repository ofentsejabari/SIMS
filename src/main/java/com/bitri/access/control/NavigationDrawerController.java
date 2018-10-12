package com.bitri.access.control;

import com.bitri.access.SIMS;
import com.bitri.service.schooladministration.ServerConfigDialog;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

import static com.bitri.access.control.MainUIFXMLController.drawerStack;
import static com.bitri.access.control.MainUIFXMLController.jFXDrawer;

/**
 * FXML Controller class
 *
 * @author ofentse
 */
public class NavigationDrawerController implements Initializable {
    
    @FXML
    private Circle profile_picture;

    @FXML
    private JFXButton closeDrawer, exit, inbox;
    
    @FXML
    private JFXButton dbServer, fileServer;
        
    private Image imageHolder;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //-- Update profile picture --
        imageHolder = SIMS.getIcon("male_user_100px.png").getImage();
        profile_picture.setFill(new ImagePattern(imageHolder));
                
        closeDrawer.setText("");
        closeDrawer.setGraphic(SIMS.getGraphics(MaterialIcon.KEYBOARD_ARROW_LEFT, "text-white", 20));
        closeDrawer.setOnAction((ActionEvent event) -> {
            drawerStack.toggle(jFXDrawer);
        });

        dbServer.setGraphic(SIMS.getGraphics(OctIcon.DATABASE, "text-gray", 24));
        fileServer.setGraphic(SIMS.getGraphics(OctIcon.FILE_SUBMODULE, "text-gray", 24));

        //-- Exit --
        exit.setGraphic(SIMS.getGraphics(MaterialDesignIcon.EXIT_TO_APP, "text-gray", 24));
        exit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        
        //-- inbox --
        inbox.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-gray", 18));
        inbox.setOnAction((ActionEvent event) -> {
            
        });
        
        dbServer.setOnAction((ActionEvent event) -> {
            new ServerConfigDialog();
        });
        
        fileServer.setOnAction((ActionEvent event) -> {
            new ServerConfigDialog();
        });
        
        JFXBadge badge = new JFXBadge(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-gray", 18),
                Pos.TOP_RIGHT);
        badge.getStyleClass().add("icons-badge-green");
        badge.setText("445");
        
        inbox.setGraphic(badge);
    }    
    
}
