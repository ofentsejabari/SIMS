package com.bitri.resource.dao;

import com.bitri.service.employeemanagement.EmployeeDesignation;
import com.bitri.service.employeemanagement.EmployeeModel;
import com.bitri.service.employeemanagement.NextOfKin;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.bitri.resource.dao.MySQLHander.STATEMENT;

import com.bitri.service.schooladministration.Subject;

/**
 *
 * @author JABARI
 */
public class EmployeeQuery {
    
      
    /********************************************************************************************************
     * 
     * @param ID
     * @return 
     */
    public static EmployeeModel getEmployeeByID(String ID){
        
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                    + " FROM `employee`,`department` WHERE `employeeID` = '"+ID+"' AND `dept_id`=`department`.`id`";
           
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                        , result.getString("departmentName"));
            }
            return new EmployeeModel();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return new EmployeeModel();
        }
    }
    
     public static ObservableList<EmployeeModel> getSubjectEmployees(String subjectId){
        
        ObservableList<EmployeeModel> employee = FXCollections.observableArrayList();
         
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                        + " `designation`, `qualification`,`nationality`,"
                        + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                        + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                        + " FROM `employee` ,`department`,`subject_teachers`"
                        + " WHERE `department`.`id`=`dept_id`"
                        + " AND `employee`.`employeeID`=`subject_teachers`.`teacherID`"
                        + " AND `subject_teachers`.`subjectID`='"+subjectId+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employee.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                ,result.getString("departmentName")));
            }
            return employee;
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return employee;
        }
    }
     
    public static ObservableList<EmployeeModel> getCoreClassAssignedTeachers(String classID){
        ObservableList<EmployeeModel> employee = FXCollections.observableArrayList();
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                        + " `designation`, `qualification`,`nationality`,"
                        + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                        + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`,`subject`.`description`"
                        + " FROM `employee` ,`department`,`assigned_classes`,`subject`"
                        + " WHERE `department`.`id`=`dept_id`"
                        + " AND `subject`.`subjectID`=`assigned_classes`.`subjectID`"
                        + " AND `employee`.`employeeID`=`assigned_classes`.`teacherID`"
                        + " AND `assigned_classes`.`classID`='"+classID+"'";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employee.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                ,result.getString("description")));
            }
            return employee;
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return employee;
        }
    }
    
    public ObservableList<String> getClassSubjectTeachers(String classID, String subjectID){
        ObservableList<String> teacherIDs = FXCollections.observableArrayList();
        try{
            String query = "SELECT DISTINCT `subjectsteachers`.`teacherID` "
                    + " FROM `subjectsteachers`, `assignedclasses` "
                    + " WHERE `subjectsteachers`.`teacherID` = `assignedclasses`.`teacherID` "
                    + " AND `classID` = '"+classID+"' "
                    + " AND `assignedclasses`.`subjectID` = '"+subjectID+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                teacherIDs.add(result.getString("teacherID"));
            }
            return teacherIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return teacherIDs;
        }
    } 
       
    public static ObservableList<EmployeeModel> getBaseClassSubjectTeachers(String subjectId){
        
        ObservableList<EmployeeModel> employee = FXCollections.observableArrayList();
         
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                        + " `designation`, `qualification`,`nationality`,"
                        + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                        + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                        + " FROM `employee` ,`department`,`subject_teachers`"
                        + " WHERE `department`.`id`=`dept_id`"
                        + " AND `employee`.`employeeID`=`subject_teachers`.`teacherID`"
                        + " AND `subject_teachers`.`subjectID`='"+subjectId+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employee.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                ,result.getString("departmentName")));
            }
            return employee;
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return employee;
        }
    }
    
    /**
     * 
     * @param employeeName
     * @return 
     */
    public static EmployeeModel getEmployeeByName(String employeeName){
        
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                    + " FROM `employee` ,`department`"
                    + "WHERE CONCAT_WS(' ',`firstName`,`lastName`) = '"+employeeName+"' "
                    + "AND `department`.`id`=`dept_id`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                ,result.getString("departmentName"));
            }
            return new EmployeeModel();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return new EmployeeModel();
        }
    }
    
    
     public static EmployeeModel getEmployeeByDepartmentId(String department){
        
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `lastName`, `firstName`, `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                    + " FROM `employee`,`department`"
                    + "WHERE `dept_id`= '"+department+"'"
                    + " AND `department`.`id`=`dept_id`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                        ,result.getString("departmentName"));
            }
            return new EmployeeModel();
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            
            return new EmployeeModel();
        }
    }
     /**
     * Get students details
     * @param showAll
     * @param departmentID
     * @return 
     */
    public static ObservableList<EmployeeModel> getEmployeeList(){
        ObservableList<EmployeeModel> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, CONCAT_WS(' ',`firstName`, `lastName`) AS `fullname`, `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                    + " FROM `employee`,`department`"
                    + " WHERE `dept_id`=`department`.`id`";
            
            
            query+=" ORDER BY `fullname`";
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                
                employees.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("fullname"), "",
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                        ,result.getString("departmentName")));
            }
            return employees;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return employees;
        }
    }
    
    
     public static ObservableList<Subject> getSubjectListFor(String teacherID){
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        try{
            String query = "SELECT `subject`.`subjectID`,`description`,`departmentID`,`schoolID`,`type`"
                         + "FROM `subject`,`subject_teachers`"
                         + " WHERE `subject_teachers`.`teacherID` = '"+teacherID+"'"
                         + " AND `subject`.`subjectID`=`subject_teachers`.`subjectID`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                subjects.add(new Subject(result.getString("subjectID"),result.getString("departmentID"),
                        result.getString("description"),result.getString("type"), result.getString("schoolID")));
            }
            return subjects;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return subjects;
        }
    }
    
      public static ObservableList<Subject> getSubjectListFor(String teacherID,String classID){
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        try{
            String query = "SELECT `subject`.`subjectID`,`description`,`departmentID`,`schoolID`,`type`"
                         + "FROM `subject`,`assigned_classes`"
                         + " WHERE `teacherID` = '"+teacherID+"'"
                         + " AND `classID`='"+classID+"'"
                         + " AND `subject`.`subjectID`=`assigned_classes`.`subjectID`";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                subjects.add(new Subject(result.getString("subjectID"),result.getString("departmentID"),
                        result.getString("description"),result.getString("type"), result.getString("schoolID")));
            }
            return subjects;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return subjects;
        }
    }
    
      public static ObservableList<EmployeeModel> getEmployeeByDesignationType(int academic){
        ObservableList<EmployeeModel> employees = FXCollections.observableArrayList();
        
        try{
            String query = "SELECT `employee`.`id`, `employeeID`, `firstName`, `lastName` , `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`departmentName`"
                    + " FROM `employee`,`department`,`employee_position`"
                    + " WHERE `dept_id`=`department`.`id`"
                    + " AND `employee_position`.`designation_type`='"+academic+"'"
                    + " AND  `designation`=`employee_position`.`id`";
            
            
            query+=" ORDER BY `firstname`";
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                
                employees.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                        ,result.getString("departmentName")));
            }
            return employees;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return employees;
        }
    }
    
    
    
    
    /*
    
    */
    
    public static ObservableList<EmployeeModel> getDepartmentEmployeeList(String dept_id){
        ObservableList<EmployeeModel> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT `employee`.`id`, `employeeID`,`firstName`, `lastName`, CONCAT_WS(' ',`firstName`, `lastName`) AS `fullname`, `middleName`, `title`, `dob`,"
                    + " `designation`, `qualification`,`nationality`,"
                    + " `identity`,`postalAddress`, `physicalAddress`, `mobilePhone`,"
                    + " `officePhone`, `email`,`gender`, `enrollDate`, `picture`,`dept_id`"
                    + " FROM `employee`"
                    + " WHERE `dept_id`='"+dept_id+"'";
            
            
            query+=" ORDER BY `fullname`";
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                
                employees.add(new EmployeeModel(result.getString("id"),result.getString("employeeID"),
                        result.getString("firstName"), result.getString("lastName"),
                        result.getString("middleName"), result.getString("title"),
                        result.getString("dob"),result.getString("designation"), 
                        result.getString("qualification"), result.getString("nationality"),
                        result.getString("identity"),result.getString("postalAddress"),
                        result.getString("physicalAddress"), result.getString("mobilePhone"),
                        result.getString("officePhone"), result.getString("gender"), 
                        result.getString("email"),result.getString("enrollDate"), result.getString("picture")
                        ,result.getString("dept_id")));
            }
            return employees;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return employees;
        }
    }
    
    /*
    *   
    */
    public static ObservableList<String> getDepartmentEmployeeNamesList(String dept_id){
        ObservableList<String> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT  CONCAT_WS(' ',`firstName`, `lastName`) AS `fullname`"
                    + " FROM `employee`"
                    + " WHERE   `dept_id`='"+dept_id+"'";
            
           
            query+=" ORDER BY `fullname`";
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employees.add(result.getString("fullname"));
            }
            return employees;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return employees;
        }
    }
    
    
    /**
     * 
     * @return 
     */
    public static ObservableList<String> getEmployeeNameList(){
        ObservableList<String> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT `lastName`, `firstName`, `middleName` FROM `employee`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employees.add(result.getString("firstName")+" "+result.getString("lastName"));
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    public static ObservableList<String> getEmployeeIDList(){
        ObservableList<String> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT `employeeID` FROM `employee`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employees.add(result.getString("employeeID"));
            }
            return employees;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return employees;
        }
    }
    //
     public static String getEmployeeDesignationId(String description){
        String employees ="";
        try{
            String query = "SELECT `id` FROM `employee_position` WHERE `description`='"+description+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                //System.out.println(result.getString("id"));
                employees = result.getString("id");
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    public static String getEmployeeDesignation(String designation){
        String employees ="";
        try{
            String query = "SELECT  `description` FROM `employee_position` WHERE id="+designation;
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                employees = result.getString("description");
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    /*
        methods that fetches employee designations and returns observable list of designations
    */
    public static ObservableList<EmployeeDesignation> getEmployeeDesignations(){
        ObservableList<EmployeeDesignation> employees = FXCollections.observableArrayList();
        try{
            String query = "SELECT  `id`,`description`,`designation_type` FROM `employee_position` WHERE 1";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                String type="";
                if(result.getString("designation_type").equals("0"))
                {
                    type="Academic Staff";}
                else{
                    type="Support Staff";
                }
                employees.add(new EmployeeDesignation(result.getString("id"),result.getString("description"),type));
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    
     public static EmployeeDesignation getEmployeeDesignationListById(String dest){
        EmployeeDesignation employees = null;
        try{
            String query = "SELECT `id`, `description`, `userRole`, `designation_type` FROM `employee_position` WHERE `id`="+dest;
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
               String type="";
                if(result.getString("designation_type").equals("0"))
                {
                    type="Academic Staff";}
                else{
                    type="Support Staff";
                }  
                employees = new EmployeeDesignation(result.getString("id"),result.getString("description"),type);
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    public static ObservableList<String> getEmployeeDesignationList(){
        ObservableList<String> employees =FXCollections.observableArrayList();
        try{
            String query = "SELECT  `description` FROM `employee_position` WHERE 1";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                employees.add(result.getString("description"));
            }
            return employees;
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return employees;
        }
    }
    
    public static boolean updateDesignation(EmployeeDesignation empDes, boolean value){
            
        
        
            String type="";
            if(empDes.getDesignationType().equals("Academic Staff"))
            {
                type="0";
            }
            else{
                type="1";
            }         
                
                
        String query ="INSERT INTO `employee_position`(`id`, `description`, `userRole`, `designation_type`) "
                + "VALUES (0,'"+empDes.getDescription()+"','','"+type+"')";
        if(value){
            query ="UPDATE `employee_position` "
                    + "SET `description`='"+empDes.getDescription()+"',`designation_type`='"+type+"'"
                    + "WHERE `id`="+empDes.getId();
        }
        
        try{
            STATEMENT.executeUpdate(query);
            
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    public static NextOfKin getNextOfKin(String empID){
        
        NextOfKin nextOfKin = null;
        
        String item ="SELECT `id`, `firstname`, `lastname`, `omang`, `employeeID`, `telephone`, `cellphone`, `relationship`,"
                + " `postal`, `physical`, `email` FROM `next_of_kin` WHERE `employeeID`='"+empID+"'";
        
        try
        {
            ResultSet rs =STATEMENT.executeQuery(item);
            if (rs.next()){
                

               nextOfKin= new NextOfKin(
                        rs.getString("employeeID"), 
                        rs.getString("firstname"), 
                        rs.getString("lastname"),
                        rs.getString("omang"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("cellphone"),
                        rs.getString("relationship"),
                        rs.getString("postal"),
                        rs.getString("physical")
                );
                
            }
            return nextOfKin;
        }
        
        catch(Exception e)
        {
            return nextOfKin;
        }
    
    }
    
    /*
    *   
    */
    public static boolean updateEmployeeProfile(EmployeeModel employee,NextOfKin kin, boolean flag)
    {
           
            if (flag){
                String employee_query="UPDATE `employee` SET `title`='"+employee.getTitle()+"',`firstName`='"+employee.getFirstName()+"',"
                    + "`middleName`='"+employee.getMiddleName()+"',`lastName`='"+employee.getLastName()+"',`gender`='"+employee.getGender()+"'"
                    + ",`dob`='"+employee.getDob()+"',`designation`='"+employee.getDesignation()+"',"
                    + "`qualification`='"+employee.getQualification()+"',`nationality`='"+employee.getNationality()+"',`identity`='"+employee.getIdentity()+"'"
                    + ",`postalAddress`='"+employee.getPostalAddress()+"',"
                    + "`physicalAddress`='"+employee.getPhysicalAddress()+"',`officePhone`='"+employee.getTelephone()+"'"
                    + ",`mobilePhone`='"+employee.getCellPhone()+"',`email`='"+employee.getEmail()+"',"
                    + "`enrollDate`='"+employee.getEnrollDate()+"',`picture`='"+employee.getProfilePsicture()+"',"
                    + " `dept_id`='"+employee.getDepartment()+"'"
                        + " WHERE `employeeID`='"+employee.getEmployeeID()+"'";
                
                String kin_query="UPDATE `next_of_kin` SET `firstname`='"+kin.getFirstName()+"',"
                        + "`lastname`='"+kin.getSurname()+"',`omang`='"+kin.getOmang()+"',`employeeID`='"+kin.getEmployeeId()+"'"
                        + ",`telephone`='"+kin.getTelephone()+"',`cellphone`='"+kin.getCellphone()+"',`relationship`='"+kin.getRelationship()+"'"
                        + ",`postal`='"+kin.getPostalAddress()+"',`physical`='"+kin.getPhysicalAddress()+"',`email`='"+kin.getEmail()+"'"
                        + " WHERE `employeeID`='"+employee.getEmployeeID()+"'";
                
                try{
                    STATEMENT.executeUpdate(employee_query);
                    STATEMENT.executeUpdate(kin_query);

                    return true;
                }
                catch(Exception e){
                    System.out.println("EEE"+e);
                    return false;
                }
            }
            else{
            /*
            *  add Employee
            */
                try{
                    
                    
                    String employee_query="INSERT INTO `employee`(`id`, `employeeID`, `dept_id`, `title`, `firstName`, `middleName`, "
                            + "`lastName`, `gender`, `dob`, `designation`, `qualification`, `nationality`, `identity`, `postalAddress`"
                            + ",`physicalAddress`, `officePhone`, `mobilePhone`, `email`, `enrollDate`, `picture`) "
                            + "VALUES(0,'"+employee.getEmployeeID()+"','"+employee.getDepartment()+"','"+employee.getTitle()+"',"
                            + "'"+employee.getFirstName()+"','"+employee.getMiddleName()+"','"+employee.getLastName()+"','"+employee.getGender()+"',"
                            + "'"+employee.getDob()+"','"+employee.getDesignation()+"','"+employee.getQualification()+"','"+employee.getNationality()+"',"
                            + "'"+employee.getIdentity()+"','"+employee.getPostalAddress()+"',"
                            + "'"+employee.getPhysicalAddress()+"','"+employee.getTelephone()+"','"+employee.getCellPhone()+"','"+employee.getEmail()+"',"
                            + "'"+employee.getEnrollDate()+"','"+employee.getProfilePsicture()+"')";
                    System.out.println(employee_query);
                    STATEMENT.executeUpdate(employee_query);
                    
                        
                        String kin_query = "INSERT INTO `next_of_kin`(`id`, `firstname`, `lastname`, `omang`, `employeeID`, `telephone`, `cellphone`, "
                                + "`relationship`, `postal`, `physical`, `email`)"
                                + " VALUES (0,'"+kin.getFirstName()+"','"+kin.getSurname()+"','"+kin.getOmang()+"',"
                                + "'"+kin.getEmployeeId()+"','"+kin.getTelephone()+"','"+kin.getCellphone()+"'"
                                + ",'"+kin.getRelationship()+"','"+kin.getPostalAddress()+"',"
                                + "'"+kin.getPhysicalAddress()+"','"+kin.getEmail()+"')";
                        System.out.println(kin_query);
                    STATEMENT.executeUpdate(kin_query);    
                    

                    return true;
                }
                catch(Exception e){
                    System.out.println(e);
                    return false;
                }
            }
    }
        
        /**
     * Add students to class
     * @param addIDs
     * @param removeIDs
     * @param classID
     * @return 
     */
    public static boolean updateSubjectTeachers(ObservableList<String> addIDs, ObservableList<String> removeIDs, String subjectId){
        
        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String std: removeIDs){
                    query = "DELETE FROM `subject_teachers` WHERE `subjectID` = '"+subjectId+"' AND `teacherID` = '"+std+"'";
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch(); 
            }
            
            if(!addIDs.isEmpty()){
                for(String std: addIDs){
                    query = "INSERT INTO `subject_teachers` (`id`, `teacherID`, `subjectID`)"
                                + " VALUES ('0', '"+std+"', '"+subjectId+"')";
                    System.out.println(query);
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch();
            }
            return true;
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     * 
     * @param addIDs
     * @param removeIDs
     * @param subjectId
     * @return 
     */
    public static boolean updateSubjectClassTeachers(ObservableList<String> addIDs, ObservableList<String> removeIDs, String classId,String teacherID){
        
        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String subjectID: removeIDs){
                    query = "DELETE FROM `assigned_classes` WHERE `subjectID`='"+subjectID+"' AND `classID` = '"+classId+"' AND `teacherID` = '"+teacherID+"'";
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch(); 
            }
            
            if(!addIDs.isEmpty()){
                for(String subjectID: addIDs){
                    query = "INSERT INTO `assigned_classes` (`id`,`classID`,`teacherID`, `subjectID`)"
                                + " VALUES (0,'"+classId+"' ,'"+teacherID+"', '"+subjectID+"')";
                    System.out.println(query);
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch();
            }
            return true;
            
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
       
    public static boolean deleteEmployee(EmployeeModel emp)
    {
        try{ 
            String query = "DELETE FROM `employee` WHERE `employeeID`='"+emp.getEmployeeID()+"'";
            
            STATEMENT.addBatch(query);
            STATEMENT.executeBatch();
            return true;
        }
        catch(Exception e){
            System.err.println(e);
            return false;
        }

    }
    
    public static boolean deleteClassTeacher(EmployeeModel emp,String base,String subject)
    {
        try{ 
            String query = "DELETE FROM `assigned_classes` WHERE `classID`='"+base+"' AND `teacherID`='"+emp.getEmployeeID()+"' AND `subjectID`='"+subject+"'";
            System.err.println(query);
            STATEMENT.addBatch(query);
            STATEMENT.executeBatch();
            return true;
        }
        catch(Exception e){
            System.err.println(e);
            return false;
        }

    }
    
    
    public static boolean deleteEmployeeDesignation(EmployeeDesignation emp)
    {
        try{ 
            String query = "DELETE FROM `employee_position` WHERE `id`='"+emp.getId()+"'";
            
            STATEMENT.addBatch(query);
            STATEMENT.executeBatch();
            return true;
        }
        catch(Exception e){
            System.err.println(e);
            return false;
        }

    }
    
            
}
