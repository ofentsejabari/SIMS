package com.bitri.service.studentmanagement;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ofentse
 */
public class SpecialNeed {
    SimpleStringProperty id, name, description;

    public SpecialNeed() {
        id = new SimpleStringProperty("");
        name = new SimpleStringProperty("");
        description = new SimpleStringProperty("");
    }
    
    public SpecialNeed(String id, String name, String description){
        
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }
    
    
    public String getId(){return id.get();}
    public void setId(String id){this.id.set(id);}
    
    public String getName(){return name.get();}
    public void setName(String id){name.set(id);}
    
    public String getDescription(){return description.get();}
    public void setDescriptions(String titl){description.set(titl);}

}
