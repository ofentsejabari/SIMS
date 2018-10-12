package com.bitri.service.schooladministration.control;

import com.bitri.access.SIMS;
import com.bitri.resource.dao.AdminQuery;
import com.bitri.service.schooladministration.Region;
import com.bitri.service.schooladministration.SchoolInformation;
import com.bitri.service.schooladministration.UpdateSchoolDialog;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author OJabari
 */
public class SchoolProfileController implements Initializable {

    @FXML
    private JFXButton  refresh, editProfile;
    @FXML
    private Label established, website, deputy, profileName,
            postalAddress, schoolHead, physicalAddress,
            tel, fax, region, email;
    @FXML
    private HBox panel;
    @FXML
    private Circle logo;

    private Gauge totalStudents, totalStaff;
    public ProfileUpdateService pus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        pus = new ProfileUpdateService();
        Image img = SIMS.getIcon("crosswalk_100px.png").getImage();
        logo.setFill(new ImagePattern(img));

        refresh.setTooltip(new Tooltip("Refresh school information"));
        refresh.getStyleClass().addAll("btn-xs", "btn-default");
        refresh.setGraphic(SIMS.getGraphics(MaterialDesignIcon.ROTATE_3D, "", 18));
        refresh.setOnAction((ActionEvent event) -> {
            SchoolInformation.sic.restart();
        });

        editProfile.setTooltip(new Tooltip("Upload school logo"));
        editProfile.getStyleClass().addAll("btn-xs", "btn-primary");
        editProfile.setGraphic(SIMS.getGraphics(MaterialDesignIcon.PENCIL, "text-white", 18));
        editProfile.setOnAction((ActionEvent event) -> {
            new UpdateSchoolDialog(SchoolInformation.selectedSchool);
        });

        GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM);

        totalStudents = builder.decimals(0).maxValue(100).unit("ACTIVE").titleColor(Color.RED).skinType(Gauge.SkinType.FLAT).build();
        totalStaff = builder.decimals(1).maxValue(1000).unit("").skinType(Gauge.SkinType.SLIM).build();

        VBox studentBox = getTopicBox("STUDENTS", Color.rgb(140,117,205), totalStudents);
        VBox staffBox = getTopicBox("STAFF", Color.rgb(229,115,115), totalStaff);

        totalStudents.setValue(0);
        totalStaff.setValue(0);

        //panel.getChildren().addAll(staffBox, studentBox);

        pus.start();
        pus.restart();

    }



    private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE){

        Rectangle bar = new Rectangle(200, 3);
        bar.setArcWidth(6);
        bar.setArcHeight(6);
        bar.setFill(COLOR);

        Label label = new Label(TEXT);
        label.setTextFill(COLOR);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 10, 0));

        GAUGE.setBarColor(COLOR);
        GAUGE.setBarBackgroundColor(Color.web("#eceff1"));//39,44,50));
        GAUGE.setAnimated(true);

        VBox vBox = new VBox(bar, label, GAUGE);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }



    public class ProfileUpdateWork extends Task<Integer> {
        @Override
        protected Integer call() throws Exception {

            Platform.runLater(() -> {
                try {

                    website.setText(SchoolInformation.selectedSchool.getWebsite());
                    profileName.setText(SchoolInformation.selectedSchool.getSchoolName());
                    postalAddress.setText(SchoolInformation.selectedSchool.getPostalAddress());
                    physicalAddress.setText(SchoolInformation.selectedSchool.getPhysicalAddress());
                    tel.setText(SchoolInformation.selectedSchool.getTel());
                    fax.setText(SchoolInformation.selectedSchool.getFax());
                    email.setText(SchoolInformation.selectedSchool.getEmail());

                    deputy.setText("Tshepo Moile");
                    established.setText(SchoolInformation.selectedSchool.getEstablished());
                    schoolHead.setText("Ofentse Jabari");
                    Region rg = AdminQuery.getRegionByID(SchoolInformation.selectedSchool.getRegion());

                    if(!rg.getID().equalsIgnoreCase("")){
                        region.setText(rg.getName()+ " - "+ rg.getDistrict());
                    }

                    totalStudents.setMaxValue(100);
                    totalStaff.setMaxValue(100);

                    totalStudents.setValue(50);
                    totalStaff.setValue(40);

                } catch (Exception e) {
                }
            });

            return 1;
        }
    }


    public class ProfileUpdateService extends Service<Integer> {

        @Override
        protected Task createTask() {
            return new ProfileUpdateWork();
        }
    }


}
