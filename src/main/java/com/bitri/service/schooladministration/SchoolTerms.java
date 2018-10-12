package com.bitri.service.schooladministration;

import com.bitri.access.SIMS;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.bitri.access.HSpacer;
import com.bitri.service.eventcalendar.JBCalendarDate;
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
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.bitri.resource.dao.AdminQuery;

/**
 *
 * @author ofentse
 */
public class SchoolTerms extends BorderPane{

    public static JFXListView<HBox> term_listView;

    public Term selectedTerm = null;
    public static int selectedIndex = 0;
    private final JFXTextField name, year, start, end, duration;

    public static TermListService tls;

    public SchoolTerms() {

        tls = new TermListService();

        getStyleClass().addAll("container");
        setPadding(new Insets(15, 0, 0, 0));

        JFXTabPane tabPane = new JFXTabPane();
        tabPane.getStyleClass().addAll("jfx-tab-flatpane");
        Tab basic  = new Tab("School Terms");

        setCenter(tabPane);

        tabPane.getTabs().add(basic);

        BorderPane tabContent = new BorderPane();
        tabContent.setPadding(new Insets(15, 5, 5, 5));
        basic.setContent(tabContent);

        BorderPane centerContainer = new BorderPane();
        centerContainer.setPadding(new Insets(0, 0, 0, 10));

        HBox toolbar = new HBox(5);
        toolbar.getStyleClass().addAll("panel-info", "panel-heading");

        JFXButton refresh = new JFXButton("Refresh");
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            tls.restart();
        });

        JFXButton btn_add = new JFXButton("Add Term");
        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateTerm(null);
        });

        Label title = new Label();
        title.getStyleClass().add("title-label");

        Label yr = new Label("", SIMS.getGraphics(MaterialDesignIcon.CALENDAR, "", 16));
        yr.setStyle("-fx-background-color:transparent; -fx-text-fill: #62757f;");
        yr.setTooltip(new Tooltip("Academic Year"));
        yr.getStyleClass().addAll("btn-success", "btn-xs");
        yr.setVisible(false);

        HBox cn = new HBox(yr, title);
        cn.getStyleClass().add("cell-content");
        cn.setAlignment(Pos.CENTER);
        cn.setSpacing(10);

        toolbar.getChildren().addAll(cn, new HSpacer(), btn_add, refresh);

        term_listView = new JFXListView<>();
        term_listView.getStyleClass().add("jfx-custom-list");

        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setAlignment(Pos.CENTER);
        contentGrid.setStyle("-fx-padding:20 5");
        contentGrid.setVgap(20);
        contentGrid.setHgap(20);

        name = new JFXTextField();
        name.prefWidthProperty().bind(contentGrid.widthProperty().subtract(25));
        name.setPromptText("Term Name");
        name.setLabelFloat(true);
        name.setDisable(true);
        contentGrid.add(name, 0, 0, 3, 1);

        start = new JFXTextField();
        start.setPromptText("Start Date");
        start.setLabelFloat(true);
        start.setDisable(true);
        contentGrid.add(start, 0, 1);

        end = new JFXTextField();
        end.setPromptText("End Date");
        end.setLabelFloat(true);
        end.setDisable(true);
        contentGrid.add(end, 1, 1);

        year = new JFXTextField();
        year.setPromptText("Academic Year");
        year.setLabelFloat(true);
        year.setDisable(true);
        contentGrid.add(year, 2, 1);

        duration = new JFXTextField();
        duration.setPromptText("Term Duration");
        duration.setLabelFloat(true);
        duration.setDisable(true);
        contentGrid.add(duration, 0, 2, 3, 1);

        centerContainer.setCenter(SIMS.setBorderContainer(contentGrid, "Term Details"));
        centerContainer.setTop(toolbar);

        tabContent.setCenter(centerContainer);

        HBox tbar = new HBox();
        tbar.getStyleClass().add("panel-heading");

        Label terms = new Label("Terms");
        terms.getStyleClass().add("title-label");

        tbar.getChildren().addAll(terms, new HSpacer());

        BorderPane lPane = new BorderPane();
        lPane.getStyleClass().addAll("panel-info");
        lPane.setStyle("-fx-border-width:0");


        VBox body = new VBox(term_listView);
        VBox.setVgrow(term_listView, Priority.ALWAYS);
        body.getStyleClass().addAll("panel-info");

        lPane.setCenter(body);

        lPane.setTop(tbar);


        tabContent.setLeft(lPane);

        term_listView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            try{

                HBox hb = term_listView.getSelectionModel().getSelectedItem();
                Label lb = (Label)hb.getChildren().get(0);
                JFXButton y = (JFXButton)hb.getChildren().get(hb.getChildren().size()-1);//-- last child
                selectedTerm = AdminQuery.getTermByName(lb.getText(), y.getText());
                title.setText(selectedTerm.getDescription());

                name.setText(selectedTerm.getDescription());
                start.setText(selectedTerm.getStart());
                end.setText(selectedTerm.getEnd());
                year.setText(selectedTerm.getYear());

                if(!selectedTerm.getYear().equalsIgnoreCase("")){
                    yr.setText(selectedTerm.getYear());
                    yr.setVisible(true);
                }

                selectedIndex = newValue.intValue();

                JBCalendarDate tf1 = new JBCalendarDate(selectedTerm.getStart());
                JBCalendarDate tf2 = new JBCalendarDate(selectedTerm.getEnd());

                int days = JBCalendarDate.getDatesBetween(tf1, tf2, false, true).size();
                duration.setText(days+" Day(s) - " + (days/5)+"."+days%5+" Week(s)");


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });


        term_listView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(tls));

        tls.start();
        tls.restart();
    }


    public class TermListWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();

            ObservableList<Term> allTerms = AdminQuery.getTermList();

            for (Term term: allTerms) {

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Update Term"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateTerm(term);
                });

                JFXButton current = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.CHECKBOX_MARKED_CIRCLE, "text-info", 18));
                current.setTooltip(new Tooltip("Current term"));
                current.getStyleClass().addAll("btn-default", "btn-xs", "always-visible");

                JFXButton year = new JFXButton(term.getYear());
                year.setPrefWidth(40);
                year.setTooltip(new Tooltip("Acardemic Year"));
                year.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");

                HBox cn = new HBox();
                if(term.isCurrentTerm().equalsIgnoreCase("1")){
                    cn.getChildren().addAll(new Label(term.getDescription()), new HSpacer(), current, edit, year);
                }else{
                    cn.getChildren().addAll(new Label(term.getDescription()), new HSpacer(), edit, year);
                }

                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try {
                    if(!data.isEmpty()){
                        term_listView.setItems(data);

                        term_listView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }

            });

            return data;
        }

    }

    public class TermListService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new TermListWork();
        }
    }



}
