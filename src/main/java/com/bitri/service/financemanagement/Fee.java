package com.bitri.service.financemanagement;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

/**
 *
 * @author tbontsokwane
 */
@Data
public class Fee {
   
    SimpleStringProperty id;
    SimpleStringProperty name;
    SimpleStringProperty paymentMode;
    SimpleStringProperty description;
    SimpleStringProperty defaultAmount;
    SimpleStringProperty academicYear;
       

    public Fee(String id, String name, String payment, String description,String defaultAmount, String year) {

        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.paymentMode = new SimpleStringProperty(payment);
        this.description = new SimpleStringProperty(description);
        this.defaultAmount = new SimpleStringProperty(defaultAmount);
        this.academicYear = new SimpleStringProperty(year);
    }

    public String getId() { return id.get(); }
    public void setId(String id){ this.id.set(id);}

    public String getName() { return name.get(); }
    public void setName(String name){ this.name.set(name);}

    public String getPaymentMode() { return paymentMode.get(); }
    public void setPaymentMode(String payment){ this.paymentMode.set(payment);}

    public String getDescription() { return description.get(); }
    public void setDescription(String description){ this.description.set(description);}

    public String getDefaultAmount() { return defaultAmount.get(); }
    public void setDefaultAmount(String amount){ this.defaultAmount.set(amount);}

    public String getAcademicYear() { return academicYear.get(); }
    public void setAcademicYear(String year){ this.academicYear.set(year);}

}
