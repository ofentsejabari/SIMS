package com.bitri.service.studentmanagement;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author ofentse
 */
public class SocialWelfareAid extends BorderPane{

    public static JFXListView<HBox> swsAid_listView;
    
    Aid selectedAid = null;
    public static int selectedIndex = 0;
    private final JFXTextField name;
    private final JFXTextArea description;
    private final JFXTextField sws;
    private final JFXTextField cooperation;
    
    public static SocialWelfareAidService swas;
    
    public SocialWelfareAid() {
        
        swas = new SocialWelfareAidService();
        
        setPadding(new Insets(15, 5, 5, 5));
        
        BorderPane ssn_center = new BorderPane();
        ssn_center.setPadding(new Insets(0, 0, 0, 10));
        
        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-heading", "panel-info");
        ssn_center.setTop(toolbar);
        
        
        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            swas.restart();
        });
        
        JFXButton btn_add = new JFXButton("Add Aid");
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateSWAidDialog(null);
        });
        
        Label title = new Label();
        title.getStyleClass().add("title-label");
        
        toolbar.getChildren().addAll(title, new HSpacer(), refresh, btn_add);
        
        setCenter(ssn_center);
        
        swsAid_listView = new JFXListView<>();
        swsAid_listView.getStyleClass().add("jfx-custom-list");
        
        setLeft(swsAid_listView);
        
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setAlignment(Pos.CENTER);
        contentGrid.setStyle("-fx-padding:25 5");
        contentGrid.setVgap(20);
        contentGrid.setHgap(2);
        
        name = new JFXTextField();
        name.prefWidthProperty().bind(contentGrid.widthProperty().subtract(25));
        name.setPromptText("Aid Name");
        name.setLabelFloat(true);
        name.setDisable(true);
        contentGrid.add(name, 0, 0);
        
        sws = new JFXTextField();
        sws.setPromptText("Social Welfare Support");
        sws.setLabelFloat(true);
        sws.setDisable(true);
        contentGrid.add(sws, 0, 1);
        
        cooperation = new JFXTextField();
        cooperation.setPromptText("Aid Offered by");
        cooperation.setLabelFloat(true);
        cooperation.setDisable(true);
        contentGrid.add(cooperation, 0, 2);
        
        description = new JFXTextArea();
        description.setPrefRowCount(4);
        description.setPromptText("Aid Description");
        description.setLabelFloat(true);
        description.setDisable(true);
        contentGrid.add(description, 0, 3);
                
        VBox cont = new VBox(SIMS.setBorderContainer(contentGrid, "Social Welfare Aid Details "));
        cont.setPadding(new Insets(10, 0, 0, 0));
        
        ssn_center.setCenter(cont);
        
        swsAid_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                Label lb = (Label)swsAid_listView.getSelectionModel().getSelectedItem().getChildren().get(0);
                selectedAid = SIMS.dbHandler.getSocialWelfareAidByName(lb.getText());
                title.setText(selectedAid.getName());
                title.setTooltip(new Tooltip(selectedAid.getName()+"\n"+ selectedAid.getCooperation()));
                
                name.setText(selectedAid.getName());
                sws.setText(SIMS.dbHandler.getSocialWelfareByID(selectedAid.getSocialWelfareID()).getName());
                cooperation.setText(selectedAid.getCooperation());
                description.setText(selectedAid.getDescription());
                
                selectedIndex = newValue.intValue();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        
        swas.start();
        swas.restart();
    }
    
        
    public class SocialWelfareAidWork extends Task<ObservableList<HBox>> {       
        @Override 
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            
            ObservableList<Aid> ssn = SIMS.dbHandler.getAids();
        
            for(Aid spn: ssn){
                
                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "text-bluegray", 16));
                edit.setTooltip(new Tooltip("Update special need"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateSWAidDialog(spn);
                });
                                
                HBox cn = new HBox(new Label(spn.getName()), new HSpacer(), edit);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }
                        
            Platform.runLater(() -> {
                try {
                    swsAid_listView.setItems(data);
                
                    if(selectedAid != null){
                        swsAid_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
            });
            
            return data;
        }
       
    }

    public class SocialWelfareAidService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new SocialWelfareAidWork();
        }
    }

    
    
    
//    public class SWSListWork extends Task<ObservableList<Student>> {       
//        @Override 
//        protected ObservableList<Student> call() throws Exception {
//            
//            
//            ObservableList<Student> data = FXCollections.observableArrayList(); 
//            
//            if(selectedAid != null){
//                //data = com.bitri.access.SIMS.dbHandler.getStudentsWithSocialWelfareSupport(selectedAid.getId());
//            }else{ 
//                data = FXCollections.observableArrayList();
//            }
//            return data;
//        }
//       
//    }
//
//    public class SWSAidWorkService extends Service<ObservableList<Student>> {
//
//        @Override
//        protected Task createTask() {
//            return new SWSListWork();
//        }
//    }
    
}
