package com.bitri.access;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author ofentse
 */
public class JFXAlert extends Alert{
    public static int INFORMATION = 0, SUCCESS = 1, ERROR = 2, WARNING = 3, CONFIRMATION = 4;
    
    
    public JFXAlert(AlertType alertType, String title, String contentText, ButtonType ... button) {
        super(AlertType.CONFIRMATION);
                
        setHeaderText(null);
        setTitle(title);
        setContentText(contentText);
        
        
        DialogPane dialogPane = getDialogPane();
                    
        //-- Scene --
        Scene scene = dialogPane.getScene();
        scene.getStylesheets().addAll(SIMS.class.getResource("/css/bootstrapfx.css").toExternalForm());
                    
        //-- Get the Stage.
        Stage stage = (Stage) scene.getWindow();
                                        
        //-- Add a custom icon.
        stage.getIcons().add(new Image(SIMS.class.getResourceAsStream("/icons/1_studentsw.png")));

    }
    
    public JFXAlert(int alertType, String title, String contentText, ButtonType ... button) {
        super(AlertType.CONFIRMATION);
    }
    
}
