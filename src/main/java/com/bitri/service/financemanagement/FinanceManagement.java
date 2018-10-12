package com.bitri.service.financemanagement;

import com.bitri.access.SIMS;
import com.bitri.service.studentmanagement.*;
import com.jfoenix.controls.JFXListView;
import com.bitri.access.HSpacer;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author ofentse
 */
public class FinanceManagement extends BorderPane{
    
    private JFXListView<HBox> mainMenu;
    public static StackPane FIN_MAN_STACK;
    
    private FeeCollection financeAdministration;
    private FeeManagement fees;
    
    public static StudentAdmin admin;
    
    public FinanceManagement() {
        
        getStyleClass().add("container");
        
        //-- Finance Management Menu --
        mainMenu = new JFXListView<>();
        mainMenu.getStyleClass().add("main_menu");
        
        //-- Menu Items --
        VBox fee = new VBox(SIMS.getIcon("students.png", 26));
        fee.getStyleClass().add("graphic-badge");
        HBox feemanagement = new HBox(new Label("Fee Management", fee), new HSpacer());
        
        VBox col = new VBox(SIMS.getIcon("report.png", 26));
        col.getStyleClass().add("graphic-badge");
        HBox collection = new HBox(new Label("Fees Collection", col), new HSpacer());
        
        mainMenu.getItems().addAll(feemanagement, collection);
        
        //-- set the first item selected --
        mainMenu.getSelectionModel().select(0);
        
        mainMenu.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            switch (newValue.intValue()){
                case 0:
                    fees.toFront();
                    break;
                case 1:
                    financeAdministration.toFront();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        });
        
        setLeft(mainMenu);
        
        financeAdministration = new FeeCollection();
        fees = new FeeManagement();
        
        FIN_MAN_STACK = new StackPane(financeAdministration,fees);
        
        FIN_MAN_STACK.setPadding(new Insets(5));
        
        setCenter(FIN_MAN_STACK);
        
    }
    
}
