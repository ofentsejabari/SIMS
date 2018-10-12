package com.bitri.access;

import com.bitri.resource.dao.DataSource;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 *
 * @author jabari
 */
public class Footer extends HBox{
    public static Label DB_CONN_STAT, DB_CONN_ICON;
    public static ServerMonitor serverMonitor;
    private GetServerWorkService getServerWorkService;
    
    public static DataSource DB_CONFIG = DataSource.deserialiseObject();
    
    public Footer() {
        
        setAlignment(Pos.CENTER_RIGHT);
        
        serverMonitor = new ServerMonitor();
        getServerWorkService = new GetServerWorkService();
        getServerWorkService.start();
        
        DB_CONN_STAT = new Label();
        DB_CONN_STAT.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LAN_CONNECT, "text-success", 20));
        DB_CONN_STAT.setGraphicTextGap(10);
        
        DB_CONN_ICON = new Label();
        DB_CONN_ICON.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LAN_DISCONNECT, "text-success", 20));
                
        getChildren().addAll(new HSpacer(), DB_CONN_STAT);
        setSpacing(20);
    }
    
    
    public class ServerWork extends Task<Integer> {       
        @Override 
        protected Integer call() throws Exception {
            
            Platform.runLater(() -> {
                serverMonitor.startServerMonitor();
            });
            return 1;
        }
    }

    public class GetServerWorkService extends Service<Integer> {

        @Override
        protected Task createTask() {
            return new ServerWork();
        }
    }
    
    
}


