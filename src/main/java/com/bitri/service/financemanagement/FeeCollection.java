package com.bitri.service.financemanagement;

import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ofentse
 */
public class FeeCollection extends BorderPane{

    public static TransactionsUI transactions;
    
    public FeeCollection() {
        
        transactions = new TransactionsUI();
        
        getStyleClass().add("container");
        setPadding(new Insets(10));
        
        JFXTabPane jfxtp = new JFXTabPane();
        jfxtp.getStyleClass().add("jfx-tab-flatpane");
        
        Tab transaction = new Tab("Transactions");
        transaction.setContent(transactions);
        
        Tab receivables = new Tab("Account Receivables");
        receivables.setContent(null);
        
        Tab Overdue_payments = new Tab("Overdue Payments");
        Overdue_payments.setContent(null);
        
        jfxtp.getTabs().addAll(transaction, receivables, Overdue_payments);
        setCenter(jfxtp);
    }
        
}
