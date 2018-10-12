package com.bitri.service.schooladministration.control;

import com.bitri.access.CustomTableView;
import com.bitri.access.HSpacer;
import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.resource.dao.EmployeeQuery;
import com.bitri.service.schooladministration.*;
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
public class DepartmentsController implements Initializable {

    @FXML
    private JFXButton btn_add, btn_refresh;
    @FXML
    private MenuButton btn_export;
    @FXML
    private JFXListView<HBox> depart_ListView;
    @FXML
    private Label departmentName;
    @FXML
    private Tab subjectsTab, subjectTeachersTab;
    @FXML
    private HBox toolbar;

    public Department selectedDepartment = null;

    //-- Selected index in the department listview --
    public int selectedIndex = 0;
    public static CustomTableView<Subject> table;

    DepartmentSubjectsUI departmentSubjects = null;
    StaffMembersUI subjectTeachers = null;

    public DepartmentWorkService dws = new DepartmentWorkService();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        departmentName.setText("");
        toolbar.getStyleClass().addAll("panel-default", "panel-heading");

        departmentSubjects = new DepartmentSubjectsUI();
        subjectTeachers = new StaffMembersUI();

        departmentSubjects.subjectWorkService.setOnSucceeded((WorkerStateEvent event) -> {
            subjectTeachers.ews.restart();
        });

        btn_add.getStyleClass().addAll("btn-xs", "btn-success");
        btn_add.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PLUS, "text-white", 18));
        btn_add.setOnAction((ActionEvent event) -> {
            new UpdateDepartmentDialog(null).show();
        });

        // Create MenuItems
        MenuItem pdf = new MenuItem("Portable Document Format", SIMS.getIcon("pdf.png", 24));
        MenuItem excel = new MenuItem("Spread Sheet", SIMS.getIcon("excel.png", 24));
        MenuItem text = new MenuItem("Plain Text", SIMS.getIcon("text.png", 24));

        btn_export.setText("Export");
        btn_export.setDisable(true);
        btn_export.setGraphic(SIMS.getGraphics(MaterialDesignIcon.FILE_EXPORT, "text-white", 18));
        btn_export.getStyleClass().addAll("split-menu-btn", "split-menu-btn-xs", "split-menu-btn-primary");
        btn_export.getItems().addAll(pdf, excel, text);

        btn_refresh.getStyleClass().addAll("btn-xs", "btn-default");
        btn_refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        btn_refresh.setOnAction((ActionEvent event) -> {
            dws.restart();
        });

        depart_ListView.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

                    try{
                        Label lb = (Label)depart_ListView.getItems().get(newValue.intValue()).getChildren().get(0);
                        selectedDepartment = AdminQuery.getDepartmentByName(lb.getText());
                        selectedIndex = newValue.intValue();
                        departmentName.setText(selectedDepartment.getDepartmentName());

                        departmentSubjects.subjectWorkService.restart();
                    }catch(Exception ex){}

                });
        depart_ListView.setPlaceholder(SIMS.setDataNotAvailablePlaceholder(dws));

        subjectsTab.setContent(departmentSubjects);

        subjectTeachersTab.setContent(subjectTeachers);
        //--
        dws.start();
        dws.restart();
    }


    public class DepartmentListWork extends Task<ObservableList<HBox>> {
        @Override
        protected ObservableList<HBox> call() throws Exception {
            ObservableList<HBox> data = FXCollections.observableArrayList();

            ObservableList<Department> dt = AdminQuery.getDepartments();

            for (Department department: dt) {

                JFXButton total = new JFXButton( EmployeeQuery.getDepartmentEmployeeNamesList(department.getID()).size()+"");
                total.setTooltip(new Tooltip("Total employee"));
                total.getStyleClass().addAll("btn-success", "btn-xs", "always-visible");
                total.setPrefWidth(32);

                JFXButton edit = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-bluegray", 18));
                edit.setTooltip(new Tooltip("Edit Stream"));
                edit.getStyleClass().addAll("btn-default", "btn-xs");
                edit.setOnAction((ActionEvent event) -> {

                    new UpdateDepartmentDialog(department);

                });

                HBox cn = new HBox(new Label(department.getDepartmentName()), new HSpacer(), edit, total);
                cn.getStyleClass().add("cell-content");
                cn.setAlignment(Pos.CENTER);
                cn.setSpacing(5);
                data.add(cn);
            }

            Platform.runLater(() -> {
                try {
                    depart_ListView.setItems(data);

                    if(selectedDepartment != null){
                        depart_ListView.getSelectionModel().select(selectedIndex);
                    }
                } catch (Exception e) {
                }

            });

            return data;
        }

    }

    public class DepartmentWorkService extends Service<ObservableList<HBox>> {

        @Override
        protected Task createTask() {
            return new DepartmentListWork();
        }
    }

}
