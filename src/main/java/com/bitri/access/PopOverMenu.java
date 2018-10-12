package com.bitri.access;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;

/**
 *
 * @author jabari
 */
public class PopOverMenu extends PopOver{

    
    public PopOverMenu(Node parent, GridPane contents, String title, int width, int height, PopOver.ArrowLocation loc, boolean showHeader) {
        
        contents.setPrefSize(width, height);
        
        getStyleClass().add("slidemenu");
        setContentNode(contents);
        setArrowLocation(loc);
        setCornerRadius(3);
        setDetachable(false);
        setArrowIndent(5);
        
        setOpacity(1);
        
        setHeaderAlwaysVisible(showHeader);
        setTitle(title);
        
        show(parent);
    }
    

}
