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
public class FeeType {
    
    String feeName;
    String feeDescription;
    String paymentMode;
    String defaultAmount;
    String termId;

    public FeeType(String feeName, String feeDescription, String paymentMode, String defaultAmount, String termId) {
        this.feeName = feeName;
        this.feeDescription = feeDescription;
        this.paymentMode = paymentMode;
        this.defaultAmount = defaultAmount;
        this.termId = termId;
    }

    public String getFeeName() {
        return feeName;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getDefaultAmount() {
        return defaultAmount;
    }

    public String getTermId() {
        return termId;
    }

   
   
}
