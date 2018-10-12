package com.bitri.access;


import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

/**
 *
 * @author jabari
 */
public class PopOverToolTip extends PopOver{

    
    public PopOverToolTip(Node owner,String helpText, int width, int height, PopOver.ArrowLocation loc) {
        
        GridPane contents = new GridPane();
        contents.setPrefSize(width, height);
        contents.setPadding(new Insets(10));
        contents.setAlignment(Pos.TOP_LEFT);
        contents.setVgap(10);
        contents.setHgap(10);
        
        Label text = new Label(helpText);
        
        text.setWrapText(true);        
        text.getStyleClass().add("title-label");
        text.setStyle("-fx-font-size:11; -fx-text-fill:black;");
                 
        Label graphic = new Label("", SIMS.getGraphics(MaterialIcon.LIVE_HELP, "text-info", 32));
        VBox grhic = new VBox(graphic);
        grhic.setAlignment(Pos.TOP_CENTER);
        
        contents.add(grhic, 0, 0);
        contents.add(text, 1, 0);
        
        getStyleClass().add("slidemenu");
        setContentNode(contents);
        setArrowLocation(loc);
        setCornerRadius(5);
        //setDetachable(detatch);
        setArrowIndent(1);
        setTitle("");
        
        setOpacity(1);
        
        owner.setOnMouseEntered((MouseEvent event) -> {
            show(owner);
        });
        owner.setOnMouseExited((MouseEvent event) -> {
            hide();
        });
    }
    
    public PopOverToolTip(Node owner, Node graphicNode, String helpText, int width, int height, PopOver.ArrowLocation loc, boolean detatch) {
        
        GridPane contents = new GridPane();
        contents.setPadding(new Insets(10));
        contents.setPrefSize(width, height);
        contents.setAlignment(Pos.TOP_LEFT);
        contents.setVgap(10);
        contents.setHgap(10);
        
        Label text = new Label(helpText);
        
        text.setWrapText(true);        
        text.getStyleClass().add("title-label");
        text.setStyle("-fx-font-size:11;-fx-text-fill:black;");
                 
        Label graphic = new Label("", graphicNode);
        VBox grhic = new VBox(graphic);
        grhic.setAlignment(Pos.TOP_CENTER);
        
        contents.add(grhic, 0, 0);
        contents.add(text, 1, 0);
        
        getStyleClass().add("slidemenu");
        setContentNode(contents);
        setArrowLocation(loc);
        setCornerRadius(5);
        setDetachable(detatch);
        setArrowIndent(1);
        setTitle("");
        
        setOpacity(1);
        
        owner.setOnMouseEntered((MouseEvent event) -> {
            show(owner);
        });
        
    }
}
