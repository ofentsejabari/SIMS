package com.bitri.access;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;


/**
 *
 * @author ofentse
 */
public class CCValidator {
    
    public static void setFieldValidator(JFXTextField field, String label) {
        
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        field.getValidators().add(rfv);
        rfv.setMessage("");
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> observable,
                                             Boolean oldValue, Boolean newValue) -> {
            if(!newValue){field.validate();}
        });
        
        Label ic = new Label(label, SIMS.getGraphics(MaterialDesignIcon.ALERT, "text-error", 14));
        ic.setContentDisplay(ContentDisplay.RIGHT);
        ic.getStyleClass().add("input-error-label");
        rfv.setIcon(ic);
    } 
    
    public static void setFieldValidator(JFXTextArea field, String label) {
        
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        field.getValidators().add(rfv);
        rfv.setMessage("");
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> observable,
                                             Boolean oldValue, Boolean newValue) -> {
            if(!newValue){field.validate();}
        });
        
        Label ic = new Label(label, SIMS.getGraphics(MaterialDesignIcon.ALERT, "text-error", 14));
        ic.setContentDisplay(ContentDisplay.RIGHT);
        ic.getStyleClass().add("input-error-label");
        rfv.setIcon(ic);
    } 
    
    
    
    public  void setPasswordFieldValidator(JFXPasswordField field, String label) {
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        field.getValidators().add(rfv);
        rfv.setMessage("");
        field.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, 
                                             Boolean oldValue, Boolean newValue) -> {
            if(!newValue){field.validate();}
        });
        
        Label ic = new Label(label, SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-error", 14));
        ic.setContentDisplay(ContentDisplay.RIGHT);
        ic.getStyleClass().add("input-error-label");
        rfv.setIcon(ic);
    }

}
