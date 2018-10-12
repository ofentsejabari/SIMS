package com.bitri.service.financemanagement;

import com.bitri.access.*;
import com.bitri.resource.dao.FeesQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.bitri.resource.dao.FeesQuery.getStudentsIDList;

/**
 *
 * @author jabari
 */
public class FeesCollectionDialog extends JFXDialog{

    public JFXComboBox<String> studentId;
    public JFXComboBox<String> feesType;
    public JFXTextField amountpaidTextField;
    public JFXTextField paidbyTextField;
    public JFXComboBox<String> paymentType;
    public JFXComboBox<String> termId;
    public JFXTextField balance;
    public Boolean isInEditMode = Boolean.FALSE;
    
     ObservableList<String> paytype = FXCollections.observableArrayList("Cash","Online", "Cellphone Banking", "Cheque");
    
    //private final ValidationSupport vSupport;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public FeesCollectionDialog() {
                  
        //-- Parent Container --
        StackPane stackPane = new StackPane();
        BorderPane container = new BorderPane();
        stackPane.getChildren().add(container);
        
        //-- Create form fields and add to content container ------------------- 
        GridPane contentGrid = new GridPane();
        contentGrid.getStyleClass().add("container");
        contentGrid.setStyle("-fx-padding:25 5 15 20;");
        contentGrid.setVgap(20);
        contentGrid.setHgap(2);
        
        studentId = new JFXComboBox<>(getStudentsIDList());
        studentId.setPromptText("Student Id");
        studentId.setLabelFloat(true);
        studentId.setPrefWidth(360);
        studentId.setEditable(true);
        contentGrid.add(studentId, 0, 0);
        
        feesType = new JFXComboBox<>();
        feesType.setPromptText("Fee Type");
        feesType.setLabelFloat(true);
        feesType.setPrefWidth(360);
        feesType.setEditable(true);
        contentGrid.add(feesType, 0, 1);
        
       
       new AutoCompleteComboBoxListener(studentId);
        
        amountpaidTextField = new JFXTextField();
        amountpaidTextField.setPromptText("Amount Paid");
        amountpaidTextField.setPrefWidth(360);
        amountpaidTextField.setLabelFloat(true);
        contentGrid.add(amountpaidTextField, 0, 2);
        
        CCValidator.setFieldValidator(amountpaidTextField, "Amount Paid required.");
        
        balance = new JFXTextField();
        balance.setPromptText("Balance");
        balance.setPrefWidth(360);
        balance.setLabelFloat(true);
        contentGrid.add(balance, 0, 3);
        
        CCValidator.setFieldValidator( balance, "Balance required.");
        
        
        paidbyTextField = new JFXTextField();
        paidbyTextField.setPromptText("Paid By");
        paidbyTextField.setPrefWidth(360);
        paidbyTextField.setLabelFloat(true);
        contentGrid.add(paidbyTextField, 0, 4);
        
        CCValidator.setFieldValidator(paidbyTextField, "Name of the payer required.");
        
        
        paymentType = new JFXComboBox<>();
        paymentType.setPromptText("Payment Type");
        paymentType.setLabelFloat(true);
        paymentType.setPrefWidth(360);
        paymentType.setEditable(true);
        contentGrid.add(paymentType, 0, 5);
        paymentType.setItems(paytype);
        
        termId = new JFXComboBox<>();
        termId.setPromptText("Term");
        termId.setLabelFloat(true);
        termId.setPrefWidth(360);
        termId.setEditable(true);
    
        contentGrid.add(termId, 0, 6);
        
        
        
        container.setCenter(SIMS.setBorderContainer(contentGrid, null));
        
        //-- Toolbar -----------------------------------------------------------
        HBox toolBar = new HBox();
        toolBar.getStyleClass().add("screen-decoration");
        
        JFXButton btn_close = new JFXButton("", SIMS.getGraphics(MaterialDesignIcon.LED_VARIANT_ON, "close", 20));
        btn_close.getStyleClass().add("window-close");
        btn_close.setOnAction((ActionEvent event) -> {
            close();
        });
        
        Label title = new Label("Add Fee");
        title.getStyleClass().add("window-title");
        
        toolBar.getChildren().addAll(title, new HSpacer(), btn_close);
        container.setTop(toolBar);
        

//        //-- Validate and save the form  ---------------------------------------
        JFXButton save = new JFXButton("Save");
       save.getStyleClass().add("dark-blue");
       save.setTooltip(new ToolTip("Save Fee"));
        save.setOnAction((ActionEvent event) -> {
            
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	LocalDate localDate = LocalDate.now();
        String today = ""+localDate;
        
       
        AddFee ftype = new AddFee(studentId.getValue(),feesType.getValue(),amountpaidTextField.getText(),paidbyTextField.getText(),paymentType.getValue(),"Tefo Bontsokwane",today,termId.getValue(),balance.getText());
        
         if(isInEditMode){
        
        FeesQuery.updateFee(ftype);
        clearFields();
        }else{
         
        FeesQuery.AddFee(ftype);
        clearFields();
        
         }
        });

//        
        //-- footer ------------------------------------------------------------
        HBox footer = new HBox(save);
        footer.getStyleClass().add("secondary-toolbar");
        container.setBottom(footer);

        //-- Set jfxdialog view  -----------------------------------------------
        setDialogContainer(FinanceManagement.FIN_MAN_STACK);
        setContent(stackPane);
        setOverlayClose(false);
        stackPane.setPrefSize(400, 180);
        show();
        
    }
    
    public void clearFields(){
    
     studentId.getSelectionModel().clearSelection();
     feesType.getSelectionModel().clearSelection();
     amountpaidTextField.clear();
     paidbyTextField.clear();
     paymentType.getSelectionModel().clearSelection();
     termId.getSelectionModel().clearSelection();
     balance.clear();
    
    }
}
