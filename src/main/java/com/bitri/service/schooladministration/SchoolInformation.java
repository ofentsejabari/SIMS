package com.bitri.service.schooladministration;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.service.schooladministration.control.SchoolProfileController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author ofentse
 */
public class SchoolInformation extends BorderPane{

    private static JFXTextField schoolHead;

    public static JFXListView<HBox> schools_listView;

    public static School selectedSchool = null;
    public static int selectedIndex = 0;

    public static SchoolInfoService sic;
    private BorderPane profile;
    private SchoolProfileController schoolProfileController;

    public SchoolInformation(){

        sic = new SchoolInfoService();

        getStyleClass().addAll("container");

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 5, 5, 5));
        setCenter(pane);

        BorderPane content = new BorderPane();
        content.setPadding(new Insets(0, 0, 0, 5));
        pane.setCenter(content);

        schools_listView = new JFXListView<>();
        schools_listView.getStyleClass().add("jfx-custom-list");

        /**********************************************************************/
        BorderPane leftPanel = new BorderPane();
        leftPanel.getStyleClass().addAll("panel-info");
        leftPanel.setStyle("-fx-border-width:0");

        Label heading = new Label("Schools");
        heading.getStyleClass().addAll("title-label");

        VBox body = new VBox(schools_listView);
        VBox.setVgrow(schools_listView, Priority.ALWAYS);
        body.getStyleClass().addAll("panel-info");
        leftPanel.setCenter(body);

        JFXButton add = new JFXButton("Add School");
        add.setTooltip(new Tooltip("Add new school"));
        add.getStyleClass().addAll("btn-xs", "btn-primary");
        add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 20));
        add.setOnAction((ActionEvent event) -> {
            new UpdateSchoolDialog(null);
        });

        HBox header = new HBox(heading, new HSpacer(), add);
        header.setSpacing(5);
        header.getStyleClass().addAll("panel-heading");

        leftPanel.setTop(header);

        pane.setLeft(leftPanel);

        /**********************************************************************/

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/schooladministrationViews/SchoolProfile.fxml"));
            profile = loader.load();
            schoolProfileController = loader.getController();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }


        content.setCenter(profile);

        schools_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try {

                Label lb = (Label)schools_listView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedSchool = SIMS.dbHandler.getSchoolByName(lb.getTooltip().getText().trim());

                schoolProfileController.pus.restart();

                selectedIndex = newValue.intValue();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        schools_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(sic));

        sic.start();
        sic.restart();
    }


    public class SchoolInfoWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {

            ObservableList<School> ssn = SIMS.dbHandler.getAllSchools();
            ObservableList<HBox> data = FXCollections.observableArrayList();

            for(School school: ssn){

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit School"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateSchoolDialog(school);
                });


                String [] nam = school.getSchoolName().split(" ");
                String sname = nam[0].substring(0, 1).toUpperCase() + nam[0].substring(1).toLowerCase()+" ";

                for(int i = 1;i < nam.length;i++){
                    sname+=nam[i].substring(0, 1).toUpperCase();}

                Label lb = new Label(sname);
                lb.setTooltip(new Tooltip(school.getSchoolName()));

                HBox cn = new HBox(lb, new HSpacer(), edit);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try {
                    schools_listView.setItems(data);
                    if(schools_listView != null){
                        schools_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }
            });

            return data;
        }
    }


    public class SchoolInfoService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new SchoolInfoWork();
        }
    }

}
