package com.bitri.service.schooladministration;

import com.bitri.access.*;
import com.bitri.resource.dao.AdminQuery;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.controlsfx.control.PopOver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.bitri.access.control.MainUIFXMLController.PARENT_STACK_PANE;

/**
 *
 * @author jabari
 */
public class UpdateSchoolDialog extends JFXDialog{

    private JFXTextField email, fax, tel, website, name;
    private JFXComboBox<String>  region;
    private JFXDatePicker established;
    private JFXTextArea postalAddress, physicalAddress;
    public ImageView schoolLogo;
    private File picture = null;


    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public UpdateSchoolDialog(School school) {

        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);

        //-- Create form fields and add to content container -------------------
        GridPane grid = new GridPane();
        grid.getStyleClass().add("container");
        grid.setStyle("-fx-padding:25 5 25 20;");
        grid.setVgap(20);
        grid.setHgap(10);

        name = new JFXTextField();
        name.setPromptText("School Name");
        name.setLabelFloat(true);
        name.setPrefWidth(300);
        CCValidator.setFieldValidator(name, "School name required.");
        grid.add(name, 0, 0);

        website = new JFXTextField();
        website.setPrefWidth(300);
        website.setPromptText("Web Site");
        website.setLabelFloat(true);
        grid.add(website, 1, 0);

        tel = new JFXTextField();
        tel.setPromptText("Telephone");
        tel.setLabelFloat(true);
        grid.add(tel, 0, 1);
        CCValidator.setFieldValidator(tel, "Telephone required.");

        fax = new JFXTextField();
        fax.setLabelFloat(true);
        fax.setPromptText("Fax");
        grid.add(fax, 1, 1);
        CCValidator.setFieldValidator(tel, "Fax required.");

        email = new JFXTextField();
        email.setLabelFloat(true);
        email.setPromptText("Email");
        grid.add(email, 0, 2);
        CCValidator.setFieldValidator(fax, "Email required.");

        established = new JFXDatePicker();
        established.setPrefWidth(300);
        established.setPromptText("Established");
        new PopOverToolTip(established, "School establishment date.", 250, 50, PopOver.ArrowLocation.BOTTOM_CENTER);
        grid.add(established, 1, 2);

        region = new JFXComboBox(AdminQuery.getRegionNames());
        region.setLabelFloat(true);
        region.setPrefWidth(300);
        region.setEditable(true);
        region.setPromptText("Region");
        grid.add(region, 0, 3);

        postalAddress = new JFXTextArea();
        postalAddress.setPrefRowCount(4);
        postalAddress.setPromptText("Postal Address");
        postalAddress.setLabelFloat(true);
        CCValidator.setFieldValidator(postalAddress, "Postal address required.");

        grid.add(postalAddress, 0, 4);

        physicalAddress = new JFXTextArea();
        physicalAddress.setPrefRowCount(4);
        physicalAddress.setPromptText("Physical Address");
        physicalAddress.setLabelFloat(true);
        CCValidator.setFieldValidator(physicalAddress, "Postal address required.");

        grid.add(physicalAddress, 1, 4);

        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");

        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.WINDOW_CLOSE, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });

        Label title = new Label("Add Subject");
        title.setText("Create School Profile");
        title.getStyleClass().add("window-title");

        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);



        schoolLogo = SIMS.getIcon("default.png", 100);
        schoolLogo.setOnMouseEntered((MouseEvent event) -> {
            schoolLogo.setStyle("-fx-cursor:hand");
        });

        schoolLogo.setOnMouseExited((MouseEvent event) -> {
            schoolLogo.setStyle("-fx-cursor:hand");
            new PopOverToolTip(schoolLogo, "Choose from your file system the school logo to upload", 250, 60, PopOver.ArrowLocation.BOTTOM_CENTER);
        });


        schoolLogo.setOnMousePressed((MouseEvent event) -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open image file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png; *.jpg; *.jpeg; *.gif")
            );

            picture = fileChooser.showOpenDialog(SIMS.PARENT_STAGE);
            if (picture != null) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(picture);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                    schoolLogo.setImage(image);
                    schoolLogo.setFitWidth(100);
                    schoolLogo.setPreserveRatio(true);
                } catch (IOException ex) {

                }
            }
        });

//        try{
//            if(com.bitri.access.SIMS.ACTIVE_SCHOOL.getSchoolName().equalsIgnoreCase("")){
//
//                File file = new File("temp/"+ISchool.SCHOOL.getLogo());
//                BufferedImage bufferedImage = ImageIO.read(file);
//                Image img = SwingFXUtils.toFXImage(bufferedImage, null);
//
//                schoolLogo.setImage(img);
//                schoolLogo.setFitWidth(100);
//                schoolLogo.setPreserveRatio(true);
//
//            }
//        }catch(Exception ex){
//
//            schoolLogo.setImage(getIcon("logo2.png", 50).getImage());
//            schoolLogo.setFitWidth(100);
//            schoolLogo.setPreserveRatio(true);
//
//        }

        HBox lg = new HBox(10);
        lg.setAlignment(Pos.CENTER_LEFT);
        lg.setStyle("-fx-padding:15; -fx-background-color:#fff");

        Label nt = new Label("Update your school logo with an image from your file system.");
        nt.setStyle("-fx-font-family: 'Romanesco', cursive; -fx-font-size: 22px;");
        nt.getStyleClass().addAll("text-success", "panel-success", "panel-body");

        lg.getChildren().addAll(schoolLogo, nt);


        VBox cn = new VBox(10);
        cn.setPadding(new Insets(10));
        cn.getChildren().addAll(SIMS.setBorderContainer(grid, "School Information"), SIMS.setBorderContainer(lg, "School Logo"));
        container.setCenter(cn);

        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
        save.getStyleClass().addAll("btn","btn-primary");
        save.setTooltip(new ToolTip("Update school"));
        save.setOnAction((ActionEvent event) -> {

            if(!"".equals(name.getText().trim())&&
                    !"".equals(tel.getText().trim())&&
                    !"".equals(fax.getText().trim())){

                if(school != null){

                    school.setSchoolName(name.getText().trim());
                    school.setWebsite(website.getText().trim());
                    school.setTel(tel.getText().trim());
                    school.setFax(fax.getText().trim());
                    school.setEmail(email.getText().trim());
                    school.setPostalAddress(postalAddress.getText().trim());
                    school.setPhysicalAddress(physicalAddress.getText().trim());
                    school.setEstablished((established.getValue() == null)? "": established.getValue().toString());
                    school.setRegion(AdminQuery.getRegionByName(region.getValue()).getID());

                    if(SIMS.dbHandler.updateSchoolDetails(school)){
                        new DialogUI("School details has been updated successfully",
                                DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                        SchoolAdministartion.schoolInformation.sic.restart();
                    }else{
                        new DialogUI("An error encountered while trying to update school details",
                                DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null);
                    }

                }else{
                    School sch = new School();
                    sch.setSchoolName(name.getText().trim());
                    sch.setWebsite(website.getText().trim());
                    sch.setTel(tel.getText().trim());
                    sch.setFax(fax.getText().trim());
                    sch.setEmail(email.getText().trim());
                    sch.setPostalAddress(postalAddress.getText().trim());
                    sch.setPhysicalAddress(physicalAddress.getText().trim());
                    sch.setEstablished((established.getValue() != null) ? "": established.getValue().toString());
                    sch.setRegion(AdminQuery.getRegionByName(region.getValue()).getID());

                    if(SIMS.dbHandler.updateSchoolDetails(sch)){
                        new DialogUI("School details has been updated successfully",
                                DialogUI.SUCCESS_NOTIF, PARENT_STACK_PANE, null);
                        SchoolAdministartion.schoolInformation.sic.restart();
                        close();
                    }else{
                        new DialogUI("An error encountered while trying to update school details",
                                DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null);
                    }
                }

            }else{
                name.validate();
                tel.validate();
                fax.validate();
                postalAddress.validate();
                physicalAddress.validate();
                email.validate();

                new DialogUI("Ensure that mandatory fields has been captured before saving changes",
                        DialogUI.ERROR_NOTIF, PARENT_STACK_PANE, null);
            }
        });

        //-- Update form entries  ----------------------------------------------

        if(school != null){

            tel.setText(school.getTel());
            name.setText(school.getSchoolName());
            website.setText(school.getWebsite());
            postalAddress.setText(school.getPostalAddress());
            physicalAddress.setText(school.getPhysicalAddress());
            fax.setText(school.getFax());
            email.setText(school.getEmail());
            region.setValue(AdminQuery.getRegionByID(school.getRegion()).getName());
            established.setValue(SIMS.getLocalDate(school.getEstablished()));

            title.setText("Update School Profile");
            save.setText("Save Changes");

        }



        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("primary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(PARENT_STACK_PANE);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(700, 560);
        show();

    }


}
