package com.bitri.service.financemanagement;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tbontsokwane
 */
public class Transaction {
    
    SimpleStringProperty ID;
    SimpleStringProperty studentId;
    SimpleStringProperty feeID;
    SimpleStringProperty amountPaid;
    SimpleStringProperty capturedDate;
    SimpleStringProperty receivedBy;
  

    public Transaction(String ID, String studentId, String feeID, String amountPaid, String capturedDate, String receivedBy){
        
        this.ID = new SimpleStringProperty(ID);
        this.studentId = new SimpleStringProperty(studentId);
        this.feeID = new SimpleStringProperty(feeID);
        this.amountPaid = new SimpleStringProperty(amountPaid);
        this.capturedDate = new SimpleStringProperty(capturedDate);
        this.receivedBy = new SimpleStringProperty(receivedBy);
           
    }

    public String getID() {
        return ID.get();
    }

    public String getStudentId() {
        return studentId.get();
    }

    public String getFeeID() {
        return feeID.get();
    }

    public String getAmountPaid() {
        return amountPaid.get();
    }

    public String getCapturedDate() {
        return capturedDate.get();
    }

    public String getReceivedBy() {
        return receivedBy.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public void setStudentId(String studentId) {
        this.studentId.set(studentId);
    }

    public void setFeeID(String feeID) {
        this.feeID.set(feeID);
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid.set(amountPaid);
    }

    public void setCapturedDate(String capturedDate) {
        this.capturedDate.set(capturedDate);
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy.set(receivedBy);
    }
    
    
    
    
    
}

