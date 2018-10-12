package com.bitri.service.schooladministration.control;

import com.bitri.access.SIMS;
import com.bitri.service.schooladministration.AccessRights;
import com.bitri.service.schooladministration.SystemUsers;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ofentse
 */
public class SystemUsersController implements Initializable {
    
    @FXML
    private Tab rolesTab, usersTab;
    
    public SystemUsers systemUsers;
    public AccessRights userRoles;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        systemUsers = new SystemUsers();
        userRoles = new AccessRights();
        
        usersTab.setContent(systemUsers);
        usersTab.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-secondary", 20));
        
        rolesTab.setContent(userRoles);
        rolesTab.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "icon-secondary", 20));
        
    }   
    

    
}
