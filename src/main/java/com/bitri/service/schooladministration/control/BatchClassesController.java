
package com.bitri.service.schooladministration.control;

import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.access.ToolTip;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.Batch;
import com.bitri.service.schooladministration.BatchClassesUI;
import com.bitri.service.schooladministration.UpdateBatchDialog;
import com.bitri.service.schooladministration.VirtualClassesUI;
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
public class BatchClassesController implements Initializable {

    @FXML
    private JFXButton btn_add, btn_refresh;
    @FXML
    private JFXListView<HBox> batch_ListView;
    @FXML
    private Label batchName;
    @FXML
    private MenuButton btn_export;
    @FXML
    private Tab optionalSubjectsTab, baseClassTab;
//    @FXML
//    private Label totalBatches;

    public  Batch selectedBatch = null;

    public BatchClassesUI baseClassesList = null;
    public VirtualClassesUI virtualClassList = null;

    public int selectedIndex = 0;

    public BatchWorkService sws = new BatchWorkService();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        baseClassesList = new BatchClassesUI();
        baseClassesList.classWorkService.setOnSucceeded((WorkerStateEvent event) -> {
            virtualClassList.classWorkService.restart();
        });

        virtualClassList = new VirtualClassesUI();

        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setTooltip(new ToolTip("Add new stream"));
        btn_add.getStyleClass().addAll("btn-success", "btn-xs");
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateBatchDialog(null).show();
        });

        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.getItems().addAll(pdf, excel, text);
        btn_export.setDisable(true);

        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setText("Refresh");
        btn_refresh.getStyleClass().addAll("btn-default", "btn-xs");
        btn_refresh.setTooltip(new Tooltip("Refresh stream list"));
        btn_refresh.setOnAction((ActionEvent event) -> {
            sws.restart();
        });

        batch_ListView.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

            try{
                Label lb = (Label)batch_ListView.getItems().get(newValue.intValue()).getChildren().get(0);
                selectedBatch = AdminQuery.getBatchByName(lb.getText());
                selectedIndex = newValue.intValue();


                batchName.setText(selectedBatch.getDescription());

                if(!selectedBatch.getStreamID().equals("")){
                    batchName.setText(selectedBatch.getDescription()+" - ("+ selectedBatch.getStreamID()+")");

                    if(!selectedBatch.getStreamID().equalsIgnoreCase("Graduate")){
                        batchName.setText(selectedBatch.getDescription()+" - ("+ AdminQuery.getStreamByID(selectedBatch.getStreamID()).getDescription()+")");
                    }
                }

                baseClassesList.classWorkService.restart();

            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        });

        batch_ListView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(sws));

        //-- Set Tab --
        baseClassTab.setContent(baseClassesList);
        optionalSubjectsTab.setContent(virtualClassList);

        sws.start();
        sws.restart();
    }



    public class BatchListWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();
            ObservableList<Batch> batch = AdminQuery.getBatches();

            for (Batch b: batch) {

                JFXButton total = new JFXButton( ""+AdminQuery.getBatchBaseClassList(b.getId()).size());
                total.setTooltip(new Tooltip("Total number of base classes"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);

                JFXButton graduate = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.SCHOOL, "", 18));
                graduate.setTooltip(new Tooltip("Alumnus"));
                graduate.getStyleClass().addAll("btn-default", "btn-xs", "always-visible");

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit class details"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {
                    new UpdateBatchDialog(b);
                });

                HBox cn = new HBox();
                if(b.getStreamID().equalsIgnoreCase("Graduate")){
                    cn.getChildren().addAll(new Label(b.getDescription()), new HSpacer(), graduate, edit, total);
                }else{
                    cn.getChildren().addAll(new Label(b.getDescription()), new HSpacer(), edit, total);
                }
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try {
                    batch_ListView.setItems(data);
                    //totalBatches.setText(""+batch.size());
                    if(selectedBatch != null){
                        batch_ListView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            return data;
        }

    }

    public class BatchWorkService extends Service<ObservableList<Label>> {

        @Override
        protected Task createTask() {
            return new BatchListWork();
        }
    }

}
