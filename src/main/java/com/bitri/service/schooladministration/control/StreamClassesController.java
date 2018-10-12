
package com.bitri.service.schooladministration.control;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.Stream;
import com.bitri.service.schooladministration.StreamGrading;
import com.bitri.service.schooladministration.StreamSubjectList;
import com.bitri.service.schooladministration.UpdateStreamDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
public class StreamClassesController implements Initializable {

    @FXML
    private JFXButton btn_add, btn_refresh;
    @FXML
    private JFXListView<HBox> stream_ListView;
    @FXML
    private MenuButton btn_export;
    @FXML
    private Label streamName;
    @FXML
    private Tab subjectsTab, gradingTab;
    @FXML
    private Label totalStreams;

    public  Stream selectedStream = null;

    public StreamSubjectList subjectList = null;
    public StreamGrading streamGrading = null;

    public int selectedIndex = 0;

    public StreamWorkService sws = new StreamWorkService();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        subjectList = new StreamSubjectList();
        subjectList.subjectWorkService.setOnSucceeded((WorkerStateEvent event) -> {
            streamGrading.gradeSchemeWorkService.restart();
        });

        streamGrading = new StreamGrading();


        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setTooltip(new Tooltip("Add new stream"));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateStreamDialog(null).show();
        });


        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.getItems().addAll(pdf, excel, text);
        btn_export.setDisable(true);

        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setTooltip(new Tooltip("Refresh stream list"));
        btn_refresh.setOnAction((ActionEvent event) -> {
            sws.restart();

        });


        stream_ListView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

            try {
                Label lb = (Label)stream_ListView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedStream = AdminQuery.getStreamByName(lb.getText());
                selectedIndex = newValue.intValue();
                streamName.setText(selectedStream.getDescription());

                subjectList.subjectWorkService.restart();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        stream_ListView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(sws));


        //-- Set Tab --
        gradingTab.setContent(streamGrading);
        subjectsTab.setContent(subjectList);

        //--
        sws.start();
        sws.restart();
    }



    public class StreamListWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            ObservableList<Stream> streams = AdminQuery.getStreams();

            for (Stream stream: streams) {

                JFXButton total = new JFXButton( ""+AdminQuery.getStreamSubjectsList(stream.getStreamID()).size());
                total.setTooltip(new Tooltip("Total number of subjects"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit Stream"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {

                    new UpdateStreamDialog(stream);

                });

                HBox cn = new HBox(new Label(stream.getDescription()), new HSpacer(), edit, total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try{
                    stream_ListView.setItems(data);

                    totalStreams.setText(""+streams.size());
                    if(selectedStream != null){
                        stream_ListView.getSelectionModel().select(selectedIndex);
                    }
                } catch(Exception e){
                }

            });

            return data;
        }

    }

    public class StreamWorkService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new StreamListWork();
        }
    }

}
