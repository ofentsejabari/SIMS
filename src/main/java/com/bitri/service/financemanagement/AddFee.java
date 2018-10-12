/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.financemanagement;

/**
 *
 * @author tbontsokwane
 */
public class AddFee {
    
    String studentId;
    String feeType;
    String amountPaid;
    String payedBy;
    String paymentMode;
    String recievedBy;
    String capturedDate;
    String termId;
    String balance;

    public AddFee(String studentId, String feeType, String amountPaid, String payedBy, String paymentMode, String recievedBy, String capturedDate, String termId, String balance) {
        this.studentId = studentId;
        this.feeType = feeType;
        this.amountPaid = amountPaid;
        this.payedBy = payedBy;
        this.paymentMode = paymentMode;
        this.recievedBy = recievedBy;
        this.capturedDate = capturedDate;
        this.termId = termId;
        this.balance = balance;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFeeType() {
        return feeType;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public String getPayedBy() {
        return payedBy;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getRecievedBy() {
        return recievedBy;
    }

    public String getCapturedDate() {
        return capturedDate;
    }

    public String getTermId() {
        return termId;
    }

    public String getBalance() {
        return balance;
    }

   
    
}
