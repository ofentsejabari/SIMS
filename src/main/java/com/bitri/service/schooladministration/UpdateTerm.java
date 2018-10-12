package com.bitri.service.schooladministration;

import com.bitri.access.DialogUI;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.eventcalendar.JBCalendarDate;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateTerm extends JFXDialog{

    private JFXComboBox<String> term_name;
    private DatePicker start, end;
    private JFXButton add;
    private JFXComboBox<String> academic_year;
    private JFXToggleButton isCurrent;
    private Term term;
        
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateTerm(Term term) {
                  
        this.term = term;
        
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 5 15 20;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(10);
        
        term_name = new JFXComboBox<>(FXCollections.observableArrayList("First Term", "Second Term", "Third Term", "Fourth Term"));
        term_name.setPromptText("Term Name");
        term_name.setPrefWidth(360);
        term_name.setLabelFloat(true);
        contentGrid.add(term_name, 0, 0, 2, 1);
        
        academic_year = new JFXComboBox<>(AdminQuery.getAcademicYearList());
        academic_year.setPromptText("Acardemic Year");
        academic_year.setLabelFloat(true);
        academic_year.setPrefWidth(360);
        contentGrid.add(academic_year, 0, 1);
        
        add = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        add.setTooltip(new Tooltip("Add acardemic Year"));
        add.getStyleClass().addAll("btn-xs", "btn-success");
        contentGrid.add(add, 1, 1);
        add.setOnAction((ActionEvent event) -> {
            new AddAcademicYear(academic_year);
        });
        
        start = new JFXDatePicker();
        start.setPromptText("Term Start");
        start.setPrefWidth(360);
        contentGrid.add(start, 0, 2);
        
        end = new JFXDatePicker();
        end.setPromptText("Term End");
        end.setPrefWidth(360);
        contentGrid.add(end, 1, 2);
        
        isCurrent = new JFXToggleButton();
        isCurrent.setText("Set as current term");
        contentGrid.add(isCurrent, 0, 3);
        
        start.getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try{
                if(end.getValue() != null && start.getValue() != null){
                    JBCalendarDate tf1 = new JBCalendarDate(start.getValue().toString());
                    JBCalendarDate tf2 = new JBCalendarDate(end.getValue().toString());
                    if(tf2.before(tf1)){
                        
                        new DialogUI("Invalid date range start date cannot be greater that end date.",
                            DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, this).show();
                        
                        end.setValue(null);
                        //days.setText("");
                        //weeks.setText("");
                    }else{
                        /*days.setText(""+JBCalendarDate.getDatesBetween(tf1, tf2, false,
                                     true).size());*/
                        
                    }
                }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        });
        
        end.getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try{
                if(end.getValue() != null && start.getValue() != null){
                    JBCalendarDate tf1 = new JBCalendarDate(start.getValue().toString());
                    JBCalendarDate tf2 = new JBCalendarDate(end.getValue().toString());
                    if(tf2.before(tf1)){
                        
                        new DialogUI("Invalid date range start date cannot be greater that end date.",
                            DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, this).show();
                        
                        end.setValue(null);
                        //days.setText("");
                        //weeks.setText("");
                    }else{
                        //days.setText(""+JBCalendarDate.getDatesBetween(tf1, tf2, false,
                                     //true).size());
                         
                    }
                }
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        });

        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Create Term");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        
        //-- Update form entries  ----------------------------------------------
        
        if(term != null){
            term_name.setValue(term.getDescription());
            start.setValue(SIMS.getLocalDate(term.getStart()));
            end.setValue(SIMS.getLocalDate(term.getEnd()));
            isCurrent.setSelected(term.isCurrentTerm().equalsIgnoreCase("1"));
            academic_year.setValue(term.getYear());
            
            title.setText("Update Term");
        }
        
        
        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Term"));
        save.setOnAction((ActionEvent event) -> {
            save();
        });  
            
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(SchoolAdministartion.ADMIN_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 200);
        show();
        
    }
    
    
    
    public void save(){
    
        if(!"".equals(term_name.getValue()) && !"".equals(start.getValue())&& !"".equals(end.getValue())){
            
            
            if(term != null){
                term.setDescrption(term_name.getValue().toString());
                term.setStart((start.getValue()== null)?"": start.getValue().toString());
                term.setEnd((end.getValue()== null)?"": end.getValue().toString());
                term.setYear((academic_year.getValue() != null)? academic_year.getValue().toString():"");
                term.setCurrentTerm(isCurrent.isSelected()?"1":"0");

                if(AdminQuery.updateTerm(term, true)){
                    
                    SchoolTerms.tls.restart();
                    new DialogUI("Academic term has been updated successfully",
                    DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();
                }

            }else{
                Term term = new Term();
                term.setDescrption(term_name.getValue().toString());
                term.setStart((start.getValue()== null)?"": start.getValue().toString());
                term.setEnd((end.getValue()== null)?"": end.getValue().toString());
                term.setYear((academic_year.getValue() != null)?academic_year.getValue().toString():"");
                term.setCurrentTerm(isCurrent.isSelected()?"1":"0");
                
                if(AdminQuery.updateTerm(term, false)){
                    close();
                    SchoolTerms.tls.restart();
                    new DialogUI("Academic term has been updated successfully.",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this);
                }
            }
        }else{
            new DialogUI( "Ensure that mandatory field are filled up... ",
                    DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null).show();
        }
    }
}
