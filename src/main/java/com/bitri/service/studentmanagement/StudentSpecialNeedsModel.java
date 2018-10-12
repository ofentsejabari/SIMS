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
public class StudentSpecialNeedsModel {

    SimpleStringProperty id,sn_id,studentID,description,solution,studentName,gender;
    StudentSpecialNeedsModel reference;
    
    public StudentSpecialNeedsModel() {
        this.id = new SimpleStringProperty("");
        this.sn_id = new SimpleStringProperty("");
        this.studentID = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.solution = new SimpleStringProperty("");
        this.studentName = new SimpleStringProperty("");
        this.gender = new SimpleStringProperty("");
    }

    public StudentSpecialNeedsModel(String id, String sn_id,String studentID,String studentName ,String description, String solution,String gender) {
        this.id = new SimpleStringProperty(id);
        this.sn_id = new SimpleStringProperty(sn_id);
        this.studentID = new SimpleStringProperty(studentID);
        this.description = new SimpleStringProperty(description);
        this.solution = new SimpleStringProperty(solution);
        this.studentName = new SimpleStringProperty(studentName);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSn_id() {
        return sn_id.get();
    }

    public void setSn_id(String sn_id) {
        this.sn_id.set(sn_id);
    }

    public String getStudentID() {
        return studentID.get();
    }

    public void setStudentID(String studentID) {
        this.studentID.set(studentID);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getSolution() {
        return solution.get();
    }

    public void setSolution(String solution) {
        this.solution.set(solution);
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
 
    public void setReference(StudentSpecialNeedsModel sn)
    {
       reference = sn;
    }
    
    public StudentSpecialNeedsModel getReference()
    {
       return reference;
    }
}
