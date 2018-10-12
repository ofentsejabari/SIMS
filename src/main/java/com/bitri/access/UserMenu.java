
package com.bitri.access;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * @author OJabari
 */
public class UserMenu extends GridPane{

    
    public UserMenu() {
        
        setPadding(new Insets(5));
        setHgap(20);
        setVgap(5);
        setAlignment(Pos.TOP_CENTER);
        prefWidth(250);
        
        Image place_holder = new Image(SIMS.class.getResourceAsStream("/icons/male_user_100px.png"));
        
        Circle profilePic = new Circle(45);
        profilePic.setTranslateX(0);
        profilePic.setTranslateY(0);
        profilePic.setCenterX(30);
        profilePic.setCenterY(30);
        profilePic.setEffect(new DropShadow(3, Color.web("#EAEAEA")));
        profilePic.setStroke(Color.web("#cfd8dc"));
        profilePic.setStrokeWidth(1);
        profilePic.setFill(Color.web("#EAEAEA"));
        profilePic.setFill(new ImagePattern(place_holder));
                
        Label username = new Label("Ofentse Jabari");
        username.setStyle("-fx-font-size:18");
        
        Label email = new Label("jabariofentse@gmail.com");
        email.getStyleClass().add("title-label");
        
        Label role = new Label("Administrator");
        role.getStyleClass().add("title-label");
        role.setStyle("-fx-font-size:24; -fx-font-family: 'Romanesco', cursive");
                
        JFXButton btn_setting = new JFXButton("Settings");
        btn_setting.getStyleClass().addAll("btn-block", "btn-xs", "btn-success");
        btn_setting.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_STAR_VARIANT, "text-white", 20));
        btn_setting.setOnAction((ActionEvent event) -> {});
        
            
        JFXButton btn_menu = new JFXButton("Logout");
        btn_menu.getStyleClass().addAll("btn-block", "btn-xs", "btn-danger");
        btn_menu.setGraphic(SIMS.getGraphics(OctIcon.GIST_PRIVATE, "text-white", 20));
        btn_menu.setOnAction((ActionEvent event) -> {
        });
        
        add(profilePic, 0, 0, 2, 1);
        add(username, 0, 1, 2, 1);
        add(email, 0, 2, 2, 1);
        add(role, 0, 3, 2, 1);
        add(new Separator(Orientation.HORIZONTAL), 0, 4, 2, 1);
        add(btn_menu, 0, 5);
        add(btn_setting, 1, 5); 
        
    }   
    
}
