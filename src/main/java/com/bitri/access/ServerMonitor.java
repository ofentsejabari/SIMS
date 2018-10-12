package com.bitri.access;

import com.bitri.resource.dao.MySQLHander;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.scene.text.Text;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bitri.access.Footer.DB_CONN_ICON;

/**
 *
 * @author jabari
 */
public class ServerMonitor {
    private static ScheduledExecutorService scheduler;

    Text img1 = SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 20);
    Text img2 = SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-success", 20);
    
    public ServerMonitor() {
        scheduler = Executors.newScheduledThreadPool(5);
    }
    
    public void stop(){
        scheduler.shutdown();
    }
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void startServerMonitor(){
        
        scheduler.scheduleAtFixedRate(() -> {
              Platform.runLater(() -> {
                  DB_CONN_ICON.setGraphic(img1);
              });
        }, 0, 2, TimeUnit.SECONDS); 
        
        scheduler.scheduleAtFixedRate(() -> {
              Platform.runLater(() -> {
                  DB_CONN_ICON.setGraphic(img2);
              });
        }, 1, 2, TimeUnit.SECONDS); 
        
        
        //-- check database server status -- 
        scheduler.scheduleAtFixedRate(() -> {
            new Thread(() -> {
                SIMS.dbHandler = new MySQLHander();
                
                    if(SIMS.dbHandler.CONNECTION != null){
                        Platform.runLater(() -> {
                            img1 = SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-success", 20);
                        });
                    }else{
                        Platform.runLater(() -> {
                            SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-error", 20);
                        });
                    }
                
            }).start();
        }, 0, 60, TimeUnit.SECONDS);  
    }
    
    public static String getCurrentTime(){
         Date date = new Date();
         return String.format("%tc",date);
    }
}
