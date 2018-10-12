package com.bitri.service.financemanagement;

import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ofentse
 */
public class FeeManagement extends BorderPane{

    public static FeesUI fees;
    
    public FeeManagement() {
        
        fees = new FeesUI();
        
        getStyleClass().add("container");
        setPadding(new Insets(10));
        
        JFXTabPane jfxtp = new JFXTabPane();
        jfxtp.getStyleClass().add("jfx-tab-flatpane");
        
        Tab transaction = new Tab("Fees Types");
        transaction.setContent(fees);
        
        jfxtp.getTabs().addAll(transaction);
        setCenter(jfxtp);
    }
        
}