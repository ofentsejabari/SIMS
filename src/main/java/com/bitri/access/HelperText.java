package com.bitri.access;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 *
 * @author jabari
 */
public class HelperText extends JFXButton{

    
    public HelperText(String helpText, PopOver.ArrowLocation loc) {
        
        getStyleClass().add("");
        setGraphic(SIMS.getGraphics(MaterialIcon.HELP, "text-info", 18));
                
        Label text = new Label(helpText);
        text.setWrapText(true);        
        text.getStyleClass().add("title-label");
        text.setStyle("-fx-font-size:11;-fx-text-fill:black;");
                 
        VBox ht = new VBox(text);
        ht.setPadding(new Insets(10));
        ht.setAlignment(Pos.TOP_CENTER);

        PopOver po = new PopOver();
        po.getStyleClass().add("slidemenu");
        po.setContentNode(ht);
        po.setArrowLocation(loc);
        po.setCornerRadius(5);
        po.setArrowIndent(1);
        
        setOpacity(1);
        
        setOnMouseEntered((MouseEvent event) -> {
            po.show(this);
        });
        setOnMouseExited((MouseEvent event) -> {
            po.hide();
        });
    }
}
