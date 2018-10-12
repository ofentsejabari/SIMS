package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;
import static com.bitri.service.schooladministration.AccessControlRights.usersListWork;

/**
 *
 * @author jabari
 */
public class UpdateUserRole extends JFXDialog{

    private JFXTextField description;
    private ObservableList<String> currentRoles;
    private final SelectionListView view;
    public UserRoles userRoles = null;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateUserRole(UserRoles accessRoles) {
                    
        userRoles = accessRoles;

        //-- A list of student IDs in the current class --
        currentRoles = FXCollections.observableArrayList();

        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:15 5 5 5;");
        contentGrid.setVgap(10);
        contentGrid.setHgap(2);
        
        description = new JFXTextField();
        description.setLabelFloat(true);
        description.setPromptText("User Role Name");
        description.setPrefWidth(250);
        contentGrid.add(description, 0, 0, 2, 1);

        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Update User Role");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);

        view = new SelectionListView();
        view.setTargetHeader("Granted Access Right");
         
        contentGrid.add(view, 0, 1, 2, 1);
        
        /**********************************************************************/
        title.setText("Create User Roles");

        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Save Department"));
        save.setOnAction((ActionEvent event) -> {
            
            if(!view.getTarget().getItems().isEmpty() || !currentRoles.isEmpty()){
                
                if(!description.getText().trim().equals("")){
                
                    //-- User priviledges are required as a comma separated list --
                    String priv = "";
                    for(String ur : view.getTarget().getItems()){ priv += ur+","; }
                        
                    if(accessRoles != null){
                        
                        accessRoles.setDescription(description.getText().trim());
                        accessRoles.setPriviledges(priv);
            
                        if(AdminQuery.updateUserRole(accessRoles, true)){

                            new DialogUI("User role has been update successfully",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();

                            usersListWork.restart();
                            close();
                            
                        }else{
                            new DialogUI("Exception occurred while trying to update user roles.",
                            DialogUI.ERROR_NOTIF, stackPane, null).show();
                        }
                        
                    }else{
                        
                        UserRoles role = new UserRoles("0", description.getText().trim(), priv);
            
                        if(AdminQuery.updateUserRole(role, false)){

                            new DialogUI("New access role has been added successfully",
                            DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, this).show();

                            usersListWork.restart();
                            close();
                        }else{
                            new DialogUI("Exception occurred while trying to add new access role.",
                            DialogUI.ERROR_NOTIF, stackPane, null).show();
                        }
                    }

                }else{
                    new DialogUI( "Role name cannot be empty. Please provide you custom role name before saving changes.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
                }
               
            }else{
                new DialogUI( "No previledges selected for this user role. Please select priviledges(s) before trying to save changes.",
                    DialogUI.ERROR_NOTIF, stackPane, null).show();
            }
            
        });

        if(accessRoles != null){
            title.setText("Update User Role");
            save.setText("Update");
        }

        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(SIMS.MAIN_UI);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(580, 480);
        show();

        updateList();
    }
    
    
    
    
    public void updateList(){
        
        ObservableList<String> source = FXCollections.observableArrayList();
        ObservableList<String> target = FXCollections.observableArrayList();
        
        if(userRoles != null){
            description.setText(userRoles.getDescription());
            for(String std: userRoles.getPriviledges().split(",")){
                target.add(std);
            }
        }
        
        for(String std: UserRoles.DEFAULT_SYSTEM_ROLES){
            if(!target.contains(std.trim())) {
                source.add(std.trim());
            }
        }
        
        view.getSource().setItems(source);
        view.getTarget().setItems(target);
    }
}







































/*import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.CheckListView;
import org.controlsfx.tools.Borders;

/**
 *
 * @author jabari
 *
public class UpdateUserRole extends Stage{

    private final JFXTextField description;
    public String priviledges = "";
    public static CheckListView checkListView;

    public UpdateUserRole(Stage parent, UserRoles employeeRole) {
    
        /****************************** Content *******************************
        
        BorderPane UI = new BorderPane();
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(5);
        
        description = new JFXTextField();
        description.setLabelFloat(true);
        description.setPromptText("USER ROLE");
        description.setPrefWidth(250);
        grid.add(description, 0, 0);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(2));
            
        checkListView = new CheckListView(UserRoles.DEFAULT_SYSTEM_ROLES);
        
        checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change c) -> {
            String tmp = checkListView.getCheckModel().getCheckedItems().toString();
            priviledges = tmp.substring(1, tmp.length()-1);
            
        });
           
        VBox ListCon = new VBox(checkListView);
        ListCon.setStyle("-fx-border-color: #F0F0F0;-fx-border-width: 1px;-fx-padding:4px");
        ListCon.setSpacing(10);
            
        CheckBox check = new CheckBox("Grant All");
        check.setSelected(false);
        check.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(!newValue){
                checkListView.getCheckModel().clearChecks();
            }else{
                checkListView.getCheckModel().checkAll();
            }
        });
        
        HBox checkCon = new HBox(check);
        checkCon.setStyle("-fx-border-color: #F0F0F0;-fx-border-width: 1px;-fx-padding:5px");
        checkCon.setAlignment(Pos.CENTER_LEFT);
         
        content.getChildren().addAll(ListCon, checkCon);
        grid.add(content, 0, 2, 2, 1);
        
        /**********************************************************************
        setTitle("Create User Roles");
        
        if(employeeRole != null){
            description.setText(employeeRole.getDescription());
            checkListView.getCheckModel().clearChecks();
            
            if(!"0".equals(employeeRole.getPriviledges())){//-- No priviledges
                if(employeeRole.getPriviledges().contains(",")){//-- More than one priviledges
                    String []prv = employeeRole.getPriviledges().split(",");
                    for(String str: prv){
                        checkListView.getCheckModel().check(str.trim());
                    }
                }else{
                    checkListView.getCheckModel().check(employeeRole.getPriviledges());
                }
            }
           setTitle("Update User Role");
        }
        
        JFXButton save = new JFXButton("Save");
        save.setTooltip(new Tooltip("Save user role"));
        save.getStyleClass().addAll("btn", "btn-xs", "btn-primary");
        save.setOnAction((ActionEvent event) -> {
            /*if(!vSupport.isInvalid()){
                
                if(employeeRole != null){
                    employeeRole.setDescription(description.getText().trim());
                    employeeRole.setPriviledges(priviledges);
                    
                    if(dbHandler.updateUserRole(employeeRole, true)){
                        new DialogUI(this, "Update User Role", 
                                "User role has been updated successfully",
                                true, DialogUI.SUCCESS);
                        close();
                        employeeRoleWorkService.restart();
                    }else{
                        new DialogUI(this, "Operation failure", 
                                "Exception occurred while trying to update user role",
                                false, DialogUI.ERROR);
                    }
                }else{
                    UserRoles role = new UserRoles("0", description.getText().trim(), priviledges);
                    
                    if(dbHandler.updateUserRole(role, false)){
                        new DialogUI(this, "Create User Role", 
                                "User roles has been created successfully",
                                true, DialogUI.SUCCESS);
                        close();
                        employeeRoleWorkService.restart();
                    }else{
                        new DialogUI(this, "Operation failure", 
                                "Exception occurred while trying to update user roles.",
                                false, DialogUI.ERROR);
                    }
                }
            }else{
                new DialogUI(this, "Input Error", 
                    "Ensure that mandatory field are filled up... ",
                    false, DialogUI.ERROR);
            }*
            
        });
        
        HBox footer = new HBox();
        footer.setStyle("-fx-border-width:1 0 0 0; -fx-padding:5");
        
        footer.getChildren().addAll(save);
        footer.setSpacing(10);
        footer.setPadding(new Insets(5));
        footer.setAlignment(Pos.CENTER);
        
        UI.setCenter(Borders.wrap(grid).lineBorder().title("User Roles").innerPadding(5).buildAll());
        UI.setBottom(footer);
        
        StackPane stack = new StackPane(UI);
        Scene scene = new Scene(new Group(stack), 380,450, Color.TRANSPARENT);
        //scene.getStylesheets().add(ISchool.class.getResource("res/css/"+ISchool.THEME+".css").toExternalForm());
        
        stack.prefWidthProperty().bind(scene.widthProperty());
        stack.prefHeightProperty().bind(scene.heightProperty());
        
        setScene(scene);
        initStyle(StageStyle.UTILITY);
        centerOnScreen();
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        show();
        
    }
}*/
