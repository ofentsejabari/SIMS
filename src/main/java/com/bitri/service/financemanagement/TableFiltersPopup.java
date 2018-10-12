/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.financemanagement;

import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author OJabari
 */
public class TableFiltersPopup  extends JFXPopup{

    public TableFiltersPopup(Node owner, HBox parent) {
        
        BorderPane container = new BorderPane();
        container.setPrefWidth(parent.getWidth());
        
        setPopupContent(container);
        setAutoHide(true);
        show(owner, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT);
        container.setPrefHeight(200);
                
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("alert-info");
        
        Label title = new Label("Table Filters");
        title.getStyleClass().addAll("title-label");
        
        toolbar.getChildren().add(title);
        
        container.setTop(toolbar);
        
        
        
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(40);
        
        JFXButton export = new JFXButton("Save");
        export.setTooltip(new Tooltip("Save"));
        export.getStyleClass().addAll("btn-xs", "btn-success");
        export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        export.setOnAction((ActionEvent event) -> {
        });
        
        gridPane.add(export, 0, 0);
        
        
        container.setCenter(gridPane);
        
        
//        PopupButton pdf = new PopupButton("Portable Document Format(PDF)", com.bitri.access.SIMS.getIcon("PDF_96px.png", 24));
//        pdf.setPrefWidth(240);
//        pdf.setOnAction((ActionEvent event) -> {
//            hide();
//        });
//        
//        
//        PopupButton xls = new PopupButton("Excel Document (xls)", com.bitri.access.SIMS.getIcon("XLS_96px.png", 24));
//        xls.setPrefWidth(240);
//        xls.setOnAction((ActionEvent event) -> {
//            hide();
//        });
//        
//        
//        PopupButton txt = new PopupButton("Plain Text File (txt)", com.bitri.access.SIMS.getIcon("TXT_96px.png", 24));
//        txt.setPrefWidth(240);
//        txt.setOnAction((ActionEvent event) -> {
//            hide();
//        });
//        
//        container.getChildren().addAll(new HBox(com.bitri.access.SIMS.getIcon("FAQ_96px.png")), pdf, xls, txt);
    }
       
}
