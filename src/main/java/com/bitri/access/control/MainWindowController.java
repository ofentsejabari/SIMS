package com.bitri.access.control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author OJabari
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label name, header;

    @FXML
    private TitledPane menu1, menu2;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        JFXButton menu = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.FORMAT_LIST_BULLETED, "text-white", 26));
        menu.getStyleClass().add("menu-icon");
        name.setGraphic(menu);
        
        header.setGraphic(SIMS.getGraphics(MaterialDesignIcon.GLASS_FLUTE, "text-white", 56));
        
        menu1.setText("School Administration");
        menu1.setContent(adminContent());
        
        menu2.setContent(new Button("TTTT"));
    } 
    
    
    
    public JFXListView adminContent(){
        
        JFXListView<HBox> mainMenu = new JFXListView<>();
        
        HBox dashboard = new HBox(new Label("Dashboard", SIMS.getIcon("5_subjects.png", 28)), new HSpacer());

        HBox enrolment = new HBox(new Label("Student Administration", SIMS.getIcon("5_subjects.png", 28)), new HSpacer());
        
        HBox attendance = new HBox(new Label("Attendance", SIMS.getIcon("5_subjects.png", 28)) , new HSpacer());
        
        HBox welfare = new HBox(new Label("Student Welfare", SIMS.getIcon("5_subjects.png", 26)), new HSpacer());
        
        HBox assessment = new HBox(new Label("Assessment", SIMS.getIcon("5_subjects.png", 26)), new HSpacer());
        
        HBox extraCurrActivity  = new HBox(new Label("Extra Curricula Activity", SIMS.getIcon("5_subjects.png", 26)), new HSpacer());

        mainMenu.getItems().addAll(dashboard, enrolment, attendance, assessment, welfare, extraCurrActivity);
        
        //-- set the first item selected --
        mainMenu.getSelectionModel().select(0);
        
        return mainMenu;
    }
    
}
