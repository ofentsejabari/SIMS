package com.bitri.service.schooladministration.control;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.House;
import com.bitri.service.schooladministration.HouseClassesList;
import com.bitri.service.schooladministration.UpdateHouseDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ofentse
 */
public class HousesCategoriesController implements Initializable {

    @FXML
    private JFXButton btn_add, btn_refresh;
    @FXML
    private Label totalHouses, house;
    @FXML
    private JFXListView<HBox> house_ListView;
    @FXML
    private Tab classesTab;
    @FXML
    private MenuButton btn_export;

    private HouseClassesList classesList;

    public House selectedHouse = null;
    public int selectedIndex = 0;
    public HousesWorkService hws = new HousesWorkService();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        classesList = new HouseClassesList();

        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setTooltip(new Tooltip("Add new stream"));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateHouseDialog(null);
        });

        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.getItems().addAll(pdf, excel, text);
        btn_export.setDisable(true);

        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setTooltip(new Tooltip("Refresh stream list"));
        btn_refresh.setOnAction((ActionEvent event) -> {
            hws.restart();
        });

        house_ListView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {
                selectedIndex = newValue.intValue();
                Label lb = (Label)house_ListView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedHouse = AdminQuery.getHouseByName(lb.getText());

                house.setText(selectedHouse.getHouseName());

                if(!selectedHouse.getHOH().equalsIgnoreCase("")){
                    house.setText(selectedHouse.getHouseName()+" ("+
                            EmployeeQuery.getEmployeeByID(selectedHouse.getHOH()).getFullNameWithInitials()+")");
                }

                classesList.hcws.restart();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        house_ListView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(hws));

        //-- Set Tab --
        classesTab.setContent(classesList);

        hws.start();
        hws.restart();
    }


    public class HousesListWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            Platform.runLater(() -> {

            });

            ObservableList<House> houses = AdminQuery.getHouses();

            for (House house: houses) {

                JFXButton total = new JFXButton( ""+AdminQuery.getHouseClassList(house.getID()).size());
                total.setTooltip(new Tooltip("Total number of classes"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit class details"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateHouseDialog(house);
                });

                HBox cn = new HBox(new Label(house.getHouseName()), new HSpacer(), edit, total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try {
                    house_ListView.setItems(data);
                    totalHouses.setText(""+houses.size());

                    if(selectedHouse != null){
                        house_ListView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }

            });

            return data;
        }

    }

    public class HousesWorkService extends Service<ObservableList<House>> {

        @Override
        protected Task createTask() {
            return new HousesListWork();
        }
    }

}
