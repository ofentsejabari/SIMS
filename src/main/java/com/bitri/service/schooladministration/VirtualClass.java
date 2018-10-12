package com.bitri.service.schooladministration;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ofentse
 */
public class VirtualClass {
    
    SimpleStringProperty classID, name, teacherID, subjectID, batchID, schoolID;

    public VirtualClass() {
        classID = new SimpleStringProperty("");
        name = new SimpleStringProperty("");
        batchID = new SimpleStringProperty("");
        subjectID = new SimpleStringProperty("");
        teacherID = new SimpleStringProperty("");
        schoolID = new SimpleStringProperty("");
    }
    
    public VirtualClass(String id, String name,String teacherID,String subjectID, String batchID, String schoolID){
        
        this.classID = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.batchID = new SimpleStringProperty(batchID);
        this.subjectID = new SimpleStringProperty(subjectID);
        this.teacherID = new SimpleStringProperty(teacherID);
        this.schoolID = new SimpleStringProperty(schoolID);
    }

    public String getClassID(){return classID.get();}
    public void setClassID(String id){this.classID.set(id);}
    
    public String getName(){return name.get();}
    public void setName(String id){name.set(id);}
    
    public String getBatchID(){return batchID.get();}
    public void setBatchID(String id){batchID.set(id);}
    
    public String getSubjectID(){return subjectID.get();}
    public void setSubjectID(String id){subjectID.set(id);}
    
    public String getTeacherID(){return teacherID.get();}
    public void setTeacherID(String fnam){teacherID.set(fnam);}
    
    public String getSchoolID(){return schoolID.get();}
    public void setSchoolID(String mnam){schoolID.set(mnam);}
}
