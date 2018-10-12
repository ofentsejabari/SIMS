package com.bitri.service.employeemanagement;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author MOILE
 */
public class EmployeeDesignation {

    SimpleStringProperty designationType,id,description;
    
    public EmployeeDesignation() {
        this.id=new SimpleStringProperty("");
        this.description=new SimpleStringProperty("");
        this.designationType= new SimpleStringProperty("");
    }
    
    public EmployeeDesignation(String id,String description,String designationType) {

        this.id=new SimpleStringProperty(id);
        this.designationType = new SimpleStringProperty(designationType);
        this.description=new SimpleStringProperty(description);    
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public String getDesignationType()
    {
        return designationType.get();
    }
    
    public void setDesignationType(String designationType){
    
        this.designationType.set(designationType);
    }

}
