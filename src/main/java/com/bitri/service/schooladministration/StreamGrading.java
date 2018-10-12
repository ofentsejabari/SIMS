package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.util.Callback;

/**
 *
 * @author ofentse
 */
public class StreamGrading extends BorderPane{
    
    public GradingSchemeWorkService gradeSchemeWorkService = null;
    public static CustomTableView<GradeScheme> table;

    public StreamGrading() {
        
        gradeSchemeWorkService = new GradingSchemeWorkService();
        
        setPadding(new Insets(5));
        
        HBox toolbar = new HBox();
        toolbar.setPadding(new Insets(2));
        setTop(toolbar);
        
        JFXButton btn_add = new JFXButton("Update Gradings");
        btn_add.getStyleClass().addAll("btn-xs", "btn-default");
        btn_add.setTooltip(new Tooltip("Update grading scheme"));
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateGradingScheme();
        });
        
        
        toolbar.getChildren().addAll(new HSpacer(), btn_add);
        
        table = new CustomTableView<>();
        
        CustomTableColumn cn = new CustomTableColumn("");
        cn.setPercentWidth(4.9);
        cn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cn.setCellFactory(TextFieldTableCell.forTableColumn());
        cn.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        
                        if(!empty){
                            setGraphic(new Label(ID));
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        
        CustomTableColumn lowerBound = new CustomTableColumn("Lower Bound");
        lowerBound.setPercentWidth(20);
        lowerBound.setCellValueFactory(new PropertyValueFactory<>("lowerBound"));
        lowerBound.setCellFactory(TextFieldTableCell.forTableColumn());
        lowerBound.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        Label subjectType = new Label(ID);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn upper = new CustomTableColumn("Upper Bound");
        upper.setPercentWidth(20);
        upper.setCellValueFactory(new PropertyValueFactory<>("upperBound"));
        upper.setCellFactory(TextFieldTableCell.forTableColumn());
        upper.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label(type);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn points = new CustomTableColumn("Points");
        points.setPercentWidth(15);
        points.setCellValueFactory(new PropertyValueFactory<>("points"));
        points.setCellFactory(TextFieldTableCell.forTableColumn());
        points.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label(type);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        CustomTableColumn symbol = new CustomTableColumn("Grade Symbol");
        symbol.setPercentWidth(40);
        symbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        symbol.setCellFactory(TextFieldTableCell.forTableColumn());
        symbol.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override 
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {
                    
                    @Override 
                    public void updateItem(final String type, boolean empty) {
                        super.updateItem(type, empty);
                        Label subjectType = new Label(type);
                        
                        if(!empty){
                            setGraphic(subjectType);
                        }else{ setGraphic(null); }
                    }
                };
            }
        });
        
        
        table.getTableView().getColumns().addAll(cn, lowerBound, upper, points, symbol);
        VBox.setVgrow(table, Priority.ALWAYS);
        
        
        ProgressIndicator pi = new ProgressIndicator("Loading grades data", "If network connection is very slow,"
                                                   + " this might take some few more seconds.");
        
        pi.visibleProperty().bind(gradeSchemeWorkService.runningProperty());
        table.getTableView().itemsProperty().bind(gradeSchemeWorkService.valueProperty());
        table.getTableView().setPlaceholder(SIMS.setDataNotAvailablePlaceholder(gradeSchemeWorkService));
        
        StackPane stackPane = new StackPane(table, pi);
        
        setCenter(stackPane);
        
        gradeSchemeWorkService.start();
        gradeSchemeWorkService.restart();
        
    }
    
    
    
    
    public class GradingSchemeWork extends Task<ObservableList<GradeScheme>> {       
        @Override 
        protected ObservableList<GradeScheme> call() throws Exception {
            ObservableList<GradeScheme>  grades = SIMS.dbHandler.getGrades();
            
            for (int i = 0; i < grades.size(); i++) {
                grades.get(i).setId(i+1+"");
            }
            
            return grades;
        }
       
    }

    public class GradingSchemeWorkService extends Service<ObservableList<GradeScheme>> {

        @Override
        protected Task createTask() {
            return new GradingSchemeWork();
        }
    }
    
}
