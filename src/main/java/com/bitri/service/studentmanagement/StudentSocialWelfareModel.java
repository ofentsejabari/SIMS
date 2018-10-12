/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.service.studentmanagement;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author MOILE
 */
public class StudentSocialWelfareModel {

    SimpleStringProperty id,swID,studentID,aidID,studentName,gender;
    StudentSocialWelfareModel reference;
    
    public StudentSocialWelfareModel() {
        this.id = new SimpleStringProperty("");
        this.swID = new SimpleStringProperty("");
        this.studentID = new SimpleStringProperty("");
        this.aidID = new SimpleStringProperty("");
        this.studentName = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
    }

    public StudentSocialWelfareModel(String id, String sn_id,String studentID,String studentName ,String aidID, String gender) {
        this.id = new SimpleStringProperty(id);
        this.swID = new SimpleStringProperty(sn_id);
        this.studentID = new SimpleStringProperty(studentID);
        this.aidID = new SimpleStringProperty(aidID);
        this.studentName = new SimpleStringProperty(studentName);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSwID() {
        return swID.get();
    }

    public void setSwID(String sn_id) {
        this.swID.set(sn_id);
    }

    public String getStudentID() {
        return studentID.get();
    }

    public void setStudentID(String studentID) {
        this.studentID.set(studentID);
    }
    
     public String getAidID() {
        return aidID.get();
    }

    public void setAidID(String aidID) {
        this.aidID.set(aidID);
    }

   
    public String getStudentName() {
        return studentName.get();
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }
    
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
 
    public void setReference(StudentSocialWelfareModel sn)
    {
       reference = sn;
    }
    
    public StudentSocialWelfareModel getReference()
    {
       return reference;
    }
}
