
package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.access.ProgressIndicator;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author ofentse
 */
public class AccessControlRights extends BorderPane{

    public static CustomTableView<UserRoles> usersTable;


    public static UsersListWorkService usersListWork;

    public JFXButton btn_add, btn_refresh;

    public AccessControlRights() {

        setPadding(new Insets(5));

        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-info", "panel-heading");
        toolbar.setStyle("-fx-padding:5 5 5 0; -fx-border-color:#fff");
        setTop(toolbar);

        usersListWork = new UsersListWorkService();

        btn_add = new JFXButton("Add Access Rights");
        btn_add.getStyleClass().addAll("btn", "btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ACCOUNT_KEY, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateUserRole(null);
        });

        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        MenuButton btn_export = new MenuButton("Export", SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18), pdf, excel, text);
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.setDisable(true);

        btn_refresh = new JFXButton("Refresh");
        btn_refresh.getStyleClass().addAll("btn", "btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "icon-default", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            usersListWork.restart();
        });

        toolbar.getChildren().addAll(  btn_export, btn_add, btn_refresh, new HSpacer());

        //-------------------Search bar and table-------------------------------
        usersTable = new CustomTableView<>();

        CustomTableColumn roleName = new CustomTableColumn("Description");
        roleName.setPercentWidth(25);
        roleName.setCellValueFactory(new PropertyValueFactory<>("description"));
        roleName.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
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

        CustomTableColumn ctrl = new CustomTableColumn("");
        ctrl.setPercentWidth(80);
        ctrl.setCellValueFactory(new PropertyValueFactory<>("ID"));
        ctrl.setCellFactory(new Callback<TableColumn<String, String>, TableCell<String, String>>() {
            @Override
            public TableCell<String, String> call(TableColumn<String, String> clientID) {
                return new TableCell<String, String>() {

                    @Override
                    public void updateItem(final String ID, boolean empty) {
                        super.updateItem(ID, empty);
                        HBox con = new HBox(10);

                        if(!empty){

                            JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                            edit.setTooltip(new Tooltip("update access role"));
                            edit.getStyleClass().addAll("btn-default", "btn-xs");
                            edit.setOnAction((ActionEvent event) -> {
                                new UpdateUserRole(AdminQuery.getUserRoleByID(ID));
                            });

                            con.getChildren().addAll(edit);

                            setGraphic(con);

                        }else{ setGraphic(null); }
                    }
                };
            }
        });

        usersTable.getTableView().getColumns().addAll(roleName,ctrl);

        VBox ph = SIMS.setDataNotAvailablePlaceholder(usersListWork);
        usersTable.getTableView().setPlaceholder(ph);

        ProgressIndicator pi = new ProgressIndicator("Loading access rights data", "If network connection is very slow,"
                + " this might take some few more seconds.");

        pi.visibleProperty().bind(usersListWork.runningProperty());
        usersTable.getTableView().itemsProperty().bind(usersListWork.valueProperty());

        setCenter(new StackPane(usersTable, pi));

        usersListWork.start();
        usersListWork.restart();
    }




    public class UsersListWork extends Task<ObservableList<UserRoles>> {
        @Override
        protected ObservableList<UserRoles> call() throws Exception {
            ObservableList<UserRoles> usersList =  AdminQuery.getUserRoles();
            return usersList;
        }
    }

    public class UsersListWorkService extends Service<ObservableList<UserRoles>> {

        @Override
        protected Task createTask() {
            return new UsersListWork();
        }
    }
}
