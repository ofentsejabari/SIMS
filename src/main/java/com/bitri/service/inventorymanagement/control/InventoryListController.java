
package com.bitri.service.inventorymanagement.control;

import com.jfoenix.controls.JFXButton;
import com.bitri.service.inventorymanagement.InventoryItem;
import com.bitri.service.inventorymanagement.Inventory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.CustomTextField;


public class InventoryListController implements Initializable {

    @FXML
    JFXButton btn_add,btn_export,btn_refresh,btn_edit;
    
     public static String filter = "ALL";
    
    @FXML
    private CustomTextField search;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private Tab inventoryList, sold;
    
    public static ObservableList<Inventory> inventList = FXCollections.observableArrayList();
    
    
    
    InventoryItem inventoryItem= null; 
    
    /**
     * Initializes the controller class.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        inventoryItem = new InventoryItem();
        borderPane.setCenter(inventoryItem);
    } 
}
