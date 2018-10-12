package com.bitri.access;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.textfield.CustomTextField;

/**
 *
 * @author ofentse
 */
public class CustomCheckListView extends GridPane{
    
    ListView<String>  source;
    CheckListView<String> target;
    ObservableList<String> data = FXCollections.observableArrayList();
    
    HBox targetHeader, targetFooter;
    
    private final CustomTextField search;

    public CustomCheckListView() {
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(10);
        setVgap(10);
        
        source = new ListView<>();
        target = new CheckListView<>();
        
        //-- 
        targetFooter = new HBox(5);
        targetHeader = new HBox(5);
        targetFooter.setAlignment(Pos.CENTER_RIGHT);
        targetHeader.setAlignment(Pos.CENTER_LEFT);
        
        
        
        
        source.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
            
                       
        });
        
        target.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            
        });
               
        
        //--
        
        search = new CustomTextField();
        search.getStyleClass().add("search-field");
        
        JFXButton clear = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "text-error", 20));
        clear.getStyleClass().addAll("btn-xs", "btn-default");
        clear.setOnAction((ActionEvent event) -> {
            search.clear();
        });
        
        JFXButton src = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "text-bluegray", 20));
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
        add(targetHeader, 1, 0);
        add(target, 1, 1);
        add(targetFooter, 1, 2);
      
    }

    public void setTargetHeader(Node targetHeader) {
        this.targetHeader.getChildren().add(targetHeader);
    }

    public void setTargetFooter(Node node) {
        this.targetFooter.getChildren().add(node);
    }

//    public String getTargetHeader() {
//        return targetHeader.getText();
//    }
//
//    public String getTargetFooter() {
//        return targetFooter.getText();
//    }
    
    public CheckListView<String> getTarget() {
        return target;
    }

    public ListView<String> getSource() {
        return source;
    }

    public void setTarget(CheckListView<String> target) {
        this.target = target;
    }

    public void setSource(JFXListView<String> source) {
        this.source = source;
    }
    
    
}
