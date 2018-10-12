package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.DataSource;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 *
 * @author jabari
 */
public class ServerConfigDialog extends JFXDialog{

    private JFXTextField dbHost, dbName, dbUsername;
    private JFXPasswordField dbpassword;    
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public ServerConfigDialog() {
        
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane dbGrid = new GridPane();
        dbGrid.getStyleClass().add("gridpane");
        dbGrid.setAlignment(Pos.CENTER);
        dbGrid.setVgap(20);
        dbGrid.setHgap(2);
        
        dbHost = new JFXTextField();
        dbHost.setPromptText("Database Host");
        dbHost.setPrefWidth(400);
        dbHost.setLabelFloat(true);
        dbGrid.add(dbHost, 0, 0);
        CCValidator.setFieldValidator(dbHost, "Database host required.");
        
        dbName = new JFXTextField();
        dbName.setPromptText("Database Name");
        dbName.setPrefWidth(400);
        dbName.setLabelFloat(true);
        dbGrid.add(dbName, 0, 1);
        CCValidator.setFieldValidator(dbName, "Database name required.");
        
        dbUsername = new JFXTextField();
        dbUsername.setPromptText("Username");
        dbUsername.setPrefWidth(400);
        dbUsername.setLabelFloat(true);
        dbGrid.add(dbUsername, 0, 2);
        CCValidator.setFieldValidator(dbUsername, "Username required.");
        
        dbpassword = new JFXPasswordField();
        dbpassword.setPromptText("Password");
        dbpassword.setPrefWidth(400);
        dbpassword.setLabelFloat(true);
        dbGrid.add(dbpassword, 0, 3);

        GridPane ftpGrid = new GridPane();
        ftpGrid.setAlignment(Pos.CENTER);
        ftpGrid.getStyleClass().add("gridpane");
        ftpGrid.setVgap(20);
        ftpGrid.setHgap(2);
        
        JFXTextField ftpHost = new JFXTextField();
        ftpHost.setPromptText("File Server Host");
        ftpHost.setPrefWidth(400);
        ftpHost.setLabelFloat(true);
        ftpGrid.add(ftpHost, 0, 0);
        
        JFXTextField ftpUsername = new JFXTextField();
        ftpUsername.setPromptText("Username");
        ftpUsername.setPrefWidth(400);
        ftpUsername.setLabelFloat(true);
        ftpGrid.add(ftpUsername, 0, 1);
        CCValidator.setFieldValidator(ftpUsername, "Username required.");
        
        
        JFXPasswordField ftpPassword = new JFXPasswordField();
        ftpPassword.setPromptText("Password");
        ftpPassword.setPrefWidth(400);
        ftpPassword.setLabelFloat(true);
        ftpGrid.add(ftpPassword, 0, 2);
        
        VBox cn = new VBox(SIMS.setBorderContainer(dbGrid, "Database Configurations", "#00bcd4"),
                                     SIMS.setBorderContainer(ftpGrid, "File Server Configurations", "#00bcd4"));
        cn.setSpacing(20);
        cn.setPadding(new Insets(10));
        
        BorderPane center = new BorderPane(cn);
               
        container.setCenter(center);
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        //toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("STORAGE SERVERS", SIMS.getIcon("network-drive.png", 96));
        title.setWrapText(true);
        title.setGraphicTextGap(10);
        title.setContentDisplay(ContentDisplay.TOP);
        title.getStyleClass().add("window-title");
                
        toolBar.getChildren().addAll(btn_close, new HSpacer());
        
        VBox left = new VBox(5);
        left.setPrefWidth(200);
        left.setAlignment(Pos.TOP_CENTER);
        left.setStyle("-fx-background-color: linear-gradient(to right, #007DBB 0%, #22B5FF 100%);");
        left.getChildren().addAll(toolBar, new VSpacer(),title, new VSpacer());
        container.setLeft(left);
        
        //-- Update form entries  ----------------------------------------------
        
        DataSource config = DataSource.deserialiseObject();
        dbHost.setText(config.getDBHost());
        dbName.setText(config.getDBName());
        dbUsername.setText(config.getDBUserName());
        dbpassword.setText(config.getDBPassword());
        
        ftpHost.setText(config.getFTPHost());
        ftpUsername.setText(config.getFTPUserName());
        ftpPassword.setText(config.getFTPPassword());
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Update Configuration");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save changes"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!dbHost.getText().trim().equals("")& 
                    !dbName.getText().trim().equals("")&
                    !dbUsername.getText().trim().equals("")){

                        config.setDBHost(dbHost.getText().trim());
                        config.setDBName(dbName.getText().trim());
                        config.setDBUserName(dbUsername.getText().trim());
                        config.setDBPassword(dbpassword.getText().trim());
                        
                        config.setFTPHost(ftpHost.getText().trim());
                        config.setFTPUserName(ftpUsername.getText().trim());
                        config.setFTPPassword(ftpPassword.getText().trim());

                        //-- Save configuration file with update details --
                        DataSource.serializeObject(config);

                        new DialogUI("Database configuration details has been updated successfully. "
                                + "Restart the application for changes to effect.",
                            DialogUI.SUCCESS_NOTIF, SIMS.MAIN_UI, this).show();
            }else{
                dbHost.validate();
                dbName.validate();
                dbUsername.validate();

                new DialogUI( "Ensure that mandatory field are filled up... ",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
            }
            
        });
        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        center.setBottom(footer);

        //-- Set JFXDialog view  -----------------------------------------------
        setDialogContainer(SIMS.MAIN_UI);
        setTransitionType(JFXDialog.DialogTransition.LEFT);
        setContent(stackPane);
        setOverlayClose(true);
        stackPane.setPrefSize(550, 500);
        show();        
    }
}
