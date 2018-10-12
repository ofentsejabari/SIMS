package com.bitri.access;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author ofentse
 */
public class DialogUI extends JFXDialog{

    //-- Nitification types --
    public static int ERROR_NOTIF = 1, SUCCESS_NOTIF = 2, INFORMATION_NOTIF = 3;

    public DialogUI(String notif, int type, StackPane dialogContainer, JFXDialog jfxd){

        BorderPane container = new BorderPane();

        Label notification = new Label(notif);

        notification.setWrapText(true);

        HBox content = new HBox(20);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getStyleClass().add("container");
        content.setStyle("-fx-padding:10");

        container.setCenter(content);
        container.getStyleClass().add("notification-dialog");

        JFXButton ok = new JFXButton("OK");
        ok.setOnAction((ActionEvent event) -> {
            close();
        });

        switch(type){
            case 1:
                content.getChildren().addAll(SIMS.getGraphics(MaterialDesignIcon.ALERT, "text-error", 54), notification);
                ok.getStyleClass().addAll("btn","btn-danger");
                notification.getStyleClass().add("text-error");
                setOverlayClose(false);
                break;

            case 2:
                ok.getStyleClass().addAll("btn","btn-success");
                content.getChildren().addAll(SIMS.getGraphics(MaterialDesignIcon.CHECKBOX_MARKED_CIRCLE, "text-success", 54), notification);
                notification.getStyleClass().add("text-success");
                setOverlayClose(true);
                break;

            case 3:
                ok.getStyleClass().addAll("btn","btn-info");
                notification.getStyleClass().add("text-info");
                content.getChildren().addAll(SIMS.getGraphics(MaterialDesignIcon.INFORMATION, "text-info", 54), notification);
                setOverlayClose(true);
                break;
        }

        //-- footer ------------------------------------------------------------

        HBox footer = new HBox(ok);
        footer.setStyle("-fx-padding: 0 4 4 0");
        footer.getStyleClass().add("primary-toolbar");
        footer.setAlignment(Pos.CENTER_RIGHT);
        container.setBottom(footer);

        if(jfxd != null){
            jfxd.close();
        }

        setDialogContainer(dialogContainer);
        setContent(container);
        container.setPrefWidth(400);
        show();

    }

}
