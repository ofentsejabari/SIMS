package com.bitri.service.schooladministration;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ofentse
 */
public class Region {
    SimpleStringProperty ID, name, district;

    public Region() {
        ID = new SimpleStringProperty("");
        name = new SimpleStringProperty("");
        district = new SimpleStringProperty("");
    }
    
    public Region(String ID, String name, String hod){
        
       this.ID = new SimpleStringProperty(ID);
       this.name = new SimpleStringProperty(name);
       this.district = new SimpleStringProperty(hod);
    }
    
    
    
    public String getName(){return name.get();}
    public void setName(String id){name.set(id);}
    
    public String getID(){return ID.get();}
    public void setID(String id){ID.set(id);}
    
    public String getDistrict(){return district.get();}
    public void setDistrict(String id){district.set(id);}
   
    

}
