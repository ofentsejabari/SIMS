package com.bitri.access;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

/**
 *
 * @author ofentse
 */
public class SelectionListView extends GridPane{
    
    ListView<String> target, source;
    ObservableList<String> data = FXCollections.observableArrayList();
    JFXButton singleInsert, singleRemove, allInsert, allRemove;
    
    Label targetHeader, targetFooter;
    
    private final CustomTextField search;

    public SelectionListView() {
        setPadding(new Insets(5));
        setHgap(10);
        setVgap(10);
        
        source = new ListView<>();
        target = new ListView<>();
        
        //-- 
        targetFooter = new Label("");
        targetHeader = new Label("");
        
        //-- Control buttons --
        singleInsert = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CHEVRON_RIGHT, "text-bluegray", 20));
        singleInsert.setDisable(true);
        singleInsert.getStyleClass().addAll("btn-xs", "btn-default");
        singleInsert.setOnAction((ActionEvent event) -> {
            String val = source.getSelectionModel().getSelectedItem();
            source.getItems().remove(val);
            data.remove(val);
            target.getItems().add(val);
        });
        
        allInsert = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CHEVRON_DOUBLE_RIGHT, "text-bluegray", 18));
        allInsert.setDisable(true);
        allInsert.getStyleClass().addAll("btn-xs", "btn-default");
        allInsert.setOnAction((ActionEvent event) -> {
            
        });
        
        singleRemove = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CHEVRON_LEFT, "text-bluegray", 20));
        singleRemove.setDisable(true);
        singleRemove.getStyleClass().addAll("btn-xs", "btn-default");
        singleRemove.setOnAction((ActionEvent event) -> {
            String val = target.getSelectionModel().getSelectedItem();
            target.getItems().remove(val);
            data.add(val);
            source.getItems().add(val);
        });
        
        allRemove = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CHEVRON_DOUBLE_LEFT, "text-bluegray", 18));
        allRemove.setDisable(true);
        allRemove.getStyleClass().addAll("btn-xs", "btn-default");
        allRemove.setOnAction((ActionEvent event) -> {
            
        });
        
        
        source.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            if(newValue != null && !newValue.equalsIgnoreCase("")){
                singleInsert.setDisable(false);
            }else{singleInsert.setDisable(true);}
                       
        });
        
        target.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            if(newValue != null && !newValue.equalsIgnoreCase("")){
                singleRemove.setDisable(false);
            }else{singleRemove.setDisable(true);}
        });
               
        VBox ctrl = new VBox(singleInsert, allInsert, singleRemove, allRemove);
        ctrl.setSpacing(5);
        
        //--
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "text-error", 20));
        clear.getStyleClass().addAll("btn-xs", "btn-default");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_SEARCH, "text-bluegray", 20));
        src.getStyleClass().addAll("btn-xs", "btn-default");
        
        search.setRight(clear);
        
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String str = newValue.trim(); 
            try{
                
                for(String s : source.getItems()){
                    if(!data.contains(s)){data.add(s);}
                }
                
                if(!data.isEmpty()){
                    source.getItems().clear();
                    
                    for(int i = 0; i < data.size(); i++){
                        
                        if(data.get(i).toLowerCase().contains(str.toLowerCase())) {
                            if(!source.getItems().contains(data.get(i))){
                                source.getItems().add(data.get(i));
                            }                            
                        }
                    }
                }
            }catch(Exception e){
                source.getItems().clear();
            }
        });
        
        
        search.setRight(src);
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!newValue.trim().equalsIgnoreCase("")){
                search.setRight(clear);
            }else{
                search.setRight(src);
            }
        });
        
        //--
        
        add(search, 0, 0);
        add(source, 0, 1);
        add(ctrl, 1, 1);
        add(targetHeader, 2, 0);
        add(target, 2, 1);
        add(targetFooter, 2, 2);
      
    }

    public void setTargetHeader(String targetHeader) {
        this.targetHeader.setText(targetHeader);
    }

    public void setTargetFooter(String targetFooter) {
        this.targetFooter.setText(targetFooter);
    }

    public String getTargetHeader() {
        return targetHeader.getText();
    }

    public String getTargetFooter() {
        return targetFooter.getText();
    }
    
    public ListView<String> getTarget() {
        return target;
    }

    public ListView<String> getSource() {
        return source;
    }

    public void setTarget(JFXListView<String> target) {
        this.target = target;
    }

    public void setSource(JFXListView<String> source) {
        this.source = source;
    }
    
    
}
