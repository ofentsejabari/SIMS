package com.bitri.service.studentmanagement;

import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ofentse
 */
public class StudentAdmin extends BorderPane{

    public StudentEnrolment studentEnrolment;
    public static BaseClasses baseClass;
    public static VirtualClasses vitualClasses;
    
    public StudentAdmin() {
        
        studentEnrolment = new StudentEnrolment();
        baseClass = new BaseClasses();
        vitualClasses = new VirtualClasses();
        
        getStyleClass().add("container");
        setPadding(new Insets(10));
        
        JFXTabPane jfxtp = new JFXTabPane();
        jfxtp.getStyleClass().add("jfx-tab-flatpane");
        
        Tab enroll = new Tab("Student Enrolment");
        enroll.setContent(studentEnrolment);
        
        Tab baseClasses = new Tab("Base Classes");
        baseClasses.setContent(baseClass);
        
        Tab optionalClasses = new Tab("Optional Subjects Classes");
        optionalClasses.setContent(vitualClasses);
        
        
        jfxtp.getTabs().addAll(enroll, baseClasses, optionalClasses);
        setCenter(jfxtp);
    }
        
}
