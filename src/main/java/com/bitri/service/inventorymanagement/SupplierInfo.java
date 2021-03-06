package com.bitri.service.inventorymanagement;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ofentse
 */
public class SupplierInfo extends BorderPane{
    
    BorderPane details;
    public SupplierInfo() {
        
        setPadding(new Insets(5));
        
        try{
            //-- 
            details = FXMLLoader.load(getClass().getResource("/views/inventorymanagementViews/supplierInformation.fxml"));
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        setCenter(details);
    }
    
   
}
