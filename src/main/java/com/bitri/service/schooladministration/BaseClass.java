package com.bitri.service.schooladministration;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ofentse
 */
public class BaseClass {
    
    SimpleStringProperty classID, name, baseTeacherID, house, batchID, schoolID;

    public BaseClass() {
        classID = new SimpleStringProperty("");
        name = new SimpleStringProperty("");
        batchID = new SimpleStringProperty("");
        house = new SimpleStringProperty("");
        baseTeacherID = new SimpleStringProperty("");
        schoolID = new SimpleStringProperty("");
    }
    
    public BaseClass(String classID, String name,String teacherID,String house, String batchID, String schoolID){
        
        this.classID = new SimpleStringProperty(classID);
        this.name = new SimpleStringProperty(name);
        this.batchID = new SimpleStringProperty(batchID);
        this.house = new SimpleStringProperty(house);
        this.baseTeacherID = new SimpleStringProperty(teacherID);
        this.schoolID = new SimpleStringProperty(schoolID);
    }

    public String getClassID(){return classID.get();}
    public void setClassID(String id){classID.set(id);}
    
    public String getName(){return name.get();}
    public void setName(String id){name.set(id);}
    
    public String getBatchID(){return batchID.get();}
    public void setBatchID(String id){batchID.set(id);}
    
    public String getHouse(){return house.get();}
    public void setHouse(String id){house.set(id);}
    
    public String getBaseTeacherID(){return baseTeacherID.get();}
    public void setBaseTeacherID(String fnam){baseTeacherID.set(fnam);}
    
    public String getSchoolID(){return schoolID.get();}
    public void setSchoolID(String mnam){schoolID.set(mnam);}
}
