package com.bitri.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.bitri.resource.dao.MySQLHander.STATEMENT;

import com.bitri.service.schooladministration.*;
import com.bitri.service.studentmanagement.ActivityMember;
import com.bitri.service.studentmanagement.ExtraCurriculaActivity;
import com.bitri.service.studentmanagement.Student;

/**
 *
 * @author JABARI
 */
public class AdminQuery {

    /**
     *
     * @param all
     * @param type
     * @return
     */
    public static ObservableList<String> getSubjectNameList(boolean all, int type){
        ObservableList<String> terms = FXCollections.observableArrayList();
        try{
            String query;

            if(all){
                query = "SELECT `description` FROM `subject`";
            }else{
                query = "SELECT `description`"
                         + "FROM `subject` WHERE `type` = '"+type+"'";
            }

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                terms.add(result.getString("description"));
            }
            return terms;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return terms;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public static Subject getSubjectByID(String id){
        try{
            String query = "SELECT `subjectID`, `departmentID`, `description`, `type`, `schoolID`"
                         + "FROM `subject` WHERE `subjectID`='"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Subject(result.getString("subjectID"),result.getString("departmentID"),
                        result.getString("description"), result.getString("type"), result.getString("schoolID"));
            }
            return new Subject();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Subject();
        }
    }

    /**
     *
     * @return
     */
    public static ObservableList<Subject> getAllSubjectList(){
        ObservableList<Subject> terms = FXCollections.observableArrayList();
        try{
            String query = "SELECT `subjectID`, `departmentID`, `description`, `type`, `schoolID`"
                         + "FROM `subject`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                terms.add(new Subject(result.getString("subjectID"),result.getString("departmentID"),
                        result.getString("description"),result.getString("type"), result.getString("schoolID")));
            }
            return terms;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return terms;
        }
    }

    /**
     * @param departmentID
     * @return
     */
    public static ObservableList<Subject> getSubjectListFor(String departmentID){
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        try{
            String query = "SELECT `subjectID`, `departmentID`, `description`, `type`, `schoolID`"
                         + "FROM `subject` WHERE `departmentID` = '"+departmentID+"'";

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

    /**
     *
     * @param subject
     * @return
     */
    public static Subject getSubjectByName(String subject){
        try{
            String query = "SELECT `subjectID`, `departmentID`, `description`, `type`, `schoolID`"
                         + "FROM `subject` WHERE `description`='"+subject+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Subject(result.getString("subjectID"),result.getString("departmentID"),
                        result.getString("description"),result.getString("type"), result.getString("schoolID"));
            }
            return null;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return null;
        }
    }

    /**
     *
     * @param subject
     * @param update
     * @return
     */
    public static String updateSubject(Subject subject, boolean update){

        try{
            String insertQuery = "INSERT INTO `subject` (`subjectID`, `departmentID`, `description`, `type`, `schoolID`)"
                    + " VALUES ('"+subject.getSubjectID()+"', '"+subject.getDepartmentID()+"','"+subject.getName()+"',"
                    + "'"+subject.getType()+"','"+subject.getSchoolID()+"')";


            String updateQuery = "UPDATE `subject` SET `description`='"+subject.getName()+"', "
                    + "`departmentID` = '"+subject.getDepartmentID()+"',`type` = '"+subject.getType()+"' "
                    + " WHERE `subjectID`= '"+subject.getSubjectID()+"'";

            if(update){
                STATEMENT.addBatch(updateQuery);
            }else{
                STATEMENT.addBatch(insertQuery);
            }

            STATEMENT.executeBatch();

            return "";
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return ex.getMessage();
        }
    }


    public static boolean deleteSubject(String subjectID){

        try{
            String updateQuery = "DELETE FROM `subject` WHERE `subjectID`= '"+subjectID+"'";
            return STATEMENT.executeUpdate(updateQuery) > 0;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }


    public static UserRoles getUserRoleByID(String ID){
        UserRoles role = new UserRoles();
        try{
            String query = "SELECT `id`, `description`, `priviledges`"
                    + " FROM `userrole` WHERE `id` = '"+ID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new UserRoles(result.getString("id"),result.getString("description"),
                        result.getString("priviledges"));
            }
            return role;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return role;
        }
    }

    public static UserRoles getUserRoleByDescription(String decription){
        try{
            String query = "SELECT `id`, `description`, `priviledges`"
                    + " FROM `userrole` WHERE `description` = '"+decription+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new UserRoles(result.getString("id"),result.getString("description"),
                        result.getString("priviledges"));
            }
            return new UserRoles();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new UserRoles();
        }
    }



    public static ObservableList<UserRoles> getUserRoles(){
        ObservableList<UserRoles> roles = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `description`, `priviledges`"
                          + " FROM `userrole`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                roles.add(new UserRoles(result.getString("id"),result.getString("description"), result.getString("priviledges")));
            }
            return roles;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return roles;
        }
    }

    public ObservableList<String> getUserRoleNames(){
        ObservableList<String> leave = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `description`"
                          + " FROM `userrole`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                leave.add(result.getString("description"));
            }
            return leave;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return leave;
        }
    }



    /**
     *
     * @param cls
     * @param update
     * @return
     */
    public static boolean updateBaseClass(BaseClass cls, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `base_class` (`classID`, `className`, `classTeacher`, `house`, `batchID`, `schoolID`)"
                        + " VALUES ('"+cls.getClassID()+"', '"+cls.getName()+"', '"+cls.getBaseTeacherID()+"',"
                        + " '"+cls.getHouse()+"', '"+cls.getBatchID()+"', '"+cls.getSchoolID()+"')";
                STATEMENT.addBatch(query);
                STATEMENT.executeBatch();
                return true;

            }else{
                query = "UPDATE `base_class` SET `className`='"+cls.getName()+"', `classTeacher`='"+cls.getBaseTeacherID()+"',"
                        + " `batchID`='"+cls.getBatchID()+"' , `house`='"+cls.getHouse()+"'"
                    + "WHERE `classID`= '"+cls.getClassID()+"'";
                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    public static boolean isClassExists(BaseClass cls){
        try{
            return STATEMENT.executeQuery("SELECT * FROM `base_class` WHERE `className`= '"+cls.getName()+"'"
                    + "AND `batchID` = '"+cls.getBatchID()+"'").first();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }




    public static boolean updateVirtualClass(VirtualClass cls, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `virtual_class` (`classID`, `name`, `teacherID`, `subjectID`, `batchID`)"
                        + " VALUES ('"+cls.getClassID()+"', '"+cls.getName()+"', '"+cls.getTeacherID()+"',"
                        + " '"+cls.getSubjectID()+"', '"+cls.getBatchID()+"')";
                STATEMENT.addBatch(query);
                STATEMENT.executeBatch();
                return true;

            }else{
                query = "UPDATE `virtual_class` SET `name`='"+cls.getName()+"', `teacherID`='"+cls.getTeacherID()+"',"
                        + " `batchID`='"+cls.getBatchID()+"' , `subjectID`='"+cls.getSubjectID()+"'"
                    + "WHERE `classID`= '"+cls.getClassID()+"'";
                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    public static boolean isVirtualClassExists(VirtualClass cls){
        try{
            return STATEMENT.executeQuery("SELECT * FROM `virtual_class` WHERE `name`= '"+cls.getName()+"'"
                    + "AND `batchID` = '"+cls.getBatchID()+"'").first();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }


    /**********************************DEPARTMENTS*****************************/
    public static boolean updateDepartment(Department department, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `department` (`id`, `departmentName`, `hod`)"
                        + " VALUES ('0', '"+department.getDepartmentName()+"', '"+department.getHod()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `department` SET `departmentName`='"+department.getDepartmentName()+"',"
                        + "`hod`='"+department.getHod()+"'"
                        + " WHERE `id`= '"+department.getID()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    public static Department getDepartmentByID(String id){
        try{
            String query = "SELECT `id`, `departmentName`, `hod`"
                          + " FROM `department` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new Department(result.getString("id"), result.getString("departmentName"),
                        result.getString("hod"));
            }
            return new Department();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Department();
        }
    }

    public static Department getDepartmentByName(String id){
        try{
            String query = "SELECT `id`, `departmentName`, `hod`"
                          + " FROM `department` WHERE `departmentName`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Department(result.getString("id"), result.getString("departmentName"), result.getString("hod"));
            }
            return new Department();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Department();
        }
    }

    public static ObservableList<String> getDepartmentNames(){
        ObservableList<String> departmentNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `departmentName`"
                          + " FROM `department`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                departmentNames.add((result.getString("departmentName")));
            }
            return departmentNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return departmentNames;
        }
    }



    /**
     *
     * @return
     */
    public static ObservableList<Department> getDepartments(){
        ObservableList<Department> departments = FXCollections.observableArrayList();
        try{
            String query = " SELECT `id`, `departmentName`, `hod`"
                          +" FROM `department`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                departments.add(new Department(result.getString("id"),
                                               result.getString("departmentName"),
                                               result.getString("hod")));
            }
            return departments;
        }
        catch(Exception ex){


             return departments;
        }
    }









    //************************ Extra curricula Activities **********************

    public static boolean addActivityMember(ActivityMember activity){

        try{
            String  query = "INSERT INTO `ec_activity_members` (`id`, `memberID`, `ec_activityID`, `type`)"
                        + " VALUES ('0', '"+activity.getMemberID()+"', '"+activity.getActivityID()+"',"
                        + " '"+activity.getType()+"')";

            return STATEMENT.executeUpdate(query) > 0;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

    }



    /**
     * Add students to class
     * @param addIDs
     * @param removeIDs
     * @param activityID
     * @return
     */
    public static boolean updateActivityMembers(ObservableList<String> addIDs, ObservableList<String> removeIDs, String activityID, String type){

        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String std: removeIDs){
                    query = "DELETE FROM `ec_activity_members` WHERE `ec_activityID` = '"+activityID+"' AND `memberID` = '"+std+"'";
                    System.out.println(query);
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch();
            }

            if(!addIDs.isEmpty()){
                for(String std: addIDs){
                    query = "INSERT INTO `ec_activity_members` (`id`, `memberID`, `ec_activityID`, `type`)"
                        + " VALUES ('0', '"+std+"', '"+activityID+"',"
                        + " '"+type+"')";

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



    public static boolean isActivityMemberExist(ActivityMember member){

        try{
            String  query = " SELECT `memberID`, `ec_activityID` "
                          + " FROM `ec_activity_members`"
                          + " WHERE `memberID` = '"+member.getMemberID()+"' "
                          + " AND `ec_activityID` = '"+member.getActivityID()+"'";

            return STATEMENT.executeQuery(query).first();

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

    }

    public static boolean deleteActivityMember(ActivityMember activity){

        try{
            String  query = "DELETE FROM `ec_activity_members` "
                          + " WHERE `id` = '"+activity.getId()+"'";

            return STATEMENT.executeUpdate(query) > 0;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

    }











    public static boolean removeSubject(String id, String streamID){

        try{
            String  query = "DELETE FROM `stream_subjects` WHERE `subjectID` = '"+id+"' AND `streamID` = '"+streamID+"'";
            System.out.println(query);
            return STATEMENT.executeUpdate(query) > 0;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }

    }


    public static ActivityMember getActivityMemberByID(String id){

        try{
            String query = " SELECT `id`, `memberID`, `ec_activityID`, `type`"
                          +" FROM `ec_activity_members` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new ActivityMember(result.getString("id"),
                                               result.getString("memberID"),
                                               result.getString("ec_activityID"),
                                               result.getString("type"));
            }
            return new ActivityMember();

        }catch(Exception ex){

            System.out.println(ex.getMessage());
            return new ActivityMember();
        }

    }


    /**
     *
     * @param activityID
     * @return
     */
    public static ObservableList<ActivityMember> getExtraCurriculaActivitiesMembers(String activityID, String filter, boolean all){
        ObservableList<ActivityMember> activities = FXCollections.observableArrayList();
        try{

            if(filter.equalsIgnoreCase("Student")){

                String query = " SELECT `ec_activity_members`.`id`, `memberID`, `ec_activityID`, `type`, `batch`.`description`,"
                          + " `base_class`.`className`, CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName`"
                          + " FROM `ec_activity_members`, `student`, `student_base_class`, `base_class`, `batch`"
                          + " WHERE `memberID` = `student`.`studentID` "
                          + " AND `batch`.`id` = `student`.`batchID`"
                          + " AND `student_base_class`.`studentID` = `student`.`studentID`"
                          + " AND `student_base_class`.`classID` = `base_class`.`classID`"
                          + " AND `ec_activityID` = '"+activityID+"'";

                if(!all){
                    query += " AND `student`.`batchID` NOT IN (SELECT `id` FROM `batch` WHERE `streamID` = 'GRADUATE')";
                }
                System.out.println(query);

                ResultSet result = STATEMENT.executeQuery(query);

                while(result.next()){
                    activities.add(new ActivityMember(result.getString("id"),
                                                   result.getString("fullName"),
                                                   result.getString("description"),
                                                   result.getString("className")));
                }
                return activities;
            }else{

                String query = " SELECT `ec_activity_members`.`id`, `memberID`, `ec_activityID`, `type`, `departmentName`, `description`,"
                          + " CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName`"
                          + " FROM `ec_activity_members`, `employee`, `department`, `employee_position`"
                          + " WHERE `memberID` = `employee`.`employeeID` "
                          + " AND `department`.`id` = `employee`.`dept_id`"
                          + " AND `employee_position`.`id` = `employee`.`designation`"
                          + " AND `ec_activityID` = '"+activityID+"'";
                ResultSet result = STATEMENT.executeQuery(query);

                while(result.next()){
                    activities.add(new ActivityMember(result.getString("id"),
                                                   result.getString("fullName"),
                                                   result.getString("departmentName"),
                                                   result.getString("description")));
                }
                return activities;
            }


        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return activities;
        }
    }









     /************************ Extra curricula Activities *********************/

    public static boolean updateActivity(ExtraCurriculaActivity activity, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `ec_activities` (`id`, `name`, `coach`, `type`)"
                        + " VALUES ('0', '"+activity.getName()+"', '"+activity.getCoach()+"',"
                        + " '"+activity.getType()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `ec_activities` SET `name`='"+activity.getName()+"',"
                        + "`coach` = '"+activity.getCoach()+"', `type` = '"+activity.getType()+"'"
                        + " WHERE `id`= '"+activity.getId()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    public static ExtraCurriculaActivity getActitityByID(String id){
        try{
            String query = "SELECT `id`, `name`, `coach`, `type`"
                          + " FROM `ec_activities` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new ExtraCurriculaActivity(result.getString("id"), result.getString("name"),
                        result.getString("coach"), result.getString("type"));
            }
            return new ExtraCurriculaActivity();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new ExtraCurriculaActivity();
        }
    }


    public static ExtraCurriculaActivity getActivityByName(String name){
        try{
            String query = "SELECT `id`, `name`, `coach`, `type`"
                          + " FROM `ec_activities` WHERE `name`= '"+name+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new ExtraCurriculaActivity(result.getString("id"), result.getString("name"),
                        result.getString("coach"), result.getString("type"));
            }
            return new ExtraCurriculaActivity();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new ExtraCurriculaActivity();
        }
    }

    public static ObservableList<String> getActivityNames(){
        ObservableList<String> departmentNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `name`"
                          + " FROM `ec_activities`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                departmentNames.add((result.getString("name")));
            }
            return departmentNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return departmentNames;
        }
    }



    /**
     *
     * @return
     */
    public static ObservableList<ExtraCurriculaActivity> getExtraCurriculaActivities(){
        ObservableList<ExtraCurriculaActivity> activities = FXCollections.observableArrayList();
        try{
            String query = " SELECT `id`, `name`, `coach`, `type`"
                          +" FROM `ec_activities`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                activities.add(new ExtraCurriculaActivity(result.getString("id"),
                                               result.getString("name"),
                                               result.getString("coach"),
                                               result.getString("type")));
            }
            return activities;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return activities;
        }
    }




     /********************************School Houses****************************/
    public static boolean updateHouse(House category, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `house` (`id`, `houseName`, `hoh`)"
                        + " VALUES ('0', '"+category.getHouseName()+"', '"+category.getHOH()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `house` SET `houseName`='"+category.getHouseName()+"',"
                        + "`hoh`='"+category.getHOH()+"'"
                        + " WHERE `id`= '"+category.getID()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    public static House getHouseByID(String id){
        try{
            String query = "SELECT `id`, `houseName`, `hoh`"
                          + " FROM `house` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new House(result.getString("id"), result.getString("houseName"),
                        result.getString("hoh"));
            }
            return new House();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new House();
        }
    }

    public static House getHouseByName(String id){
        try{
            String query = "SELECT `id`, `houseName`, `hoh`"
                          + " FROM `house` WHERE `houseName`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new House(result.getString("id"), result.getString("houseName"), result.getString("hoh"));
            }
            return new House();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new House();
        }
    }

    public static ObservableList<String> getHouseNames(){
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `houseName`"
                          + " FROM `house`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                categoryNames.add((result.getString("houseName")));
            }
            return categoryNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return categoryNames;
        }
    }

    public static ObservableList<House> getHouses(){
        ObservableList<House> categories = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `houseName`, `hoh`"
                          + " FROM `house`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                categories.add(new House(result.getString("id"),result.getString("houseName"),
                                result.getString("hoh")));
            }
            return categories;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return categories;
        }
    }

    public static ObservableList<String> getHouseClassNames(String categoryID){

        ObservableList<String> classNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `className`" +
                            " FROM `base_class`" +
                            " WHERE `house` = '"+categoryID+"'";


            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                classNames.add(result.getString("className"));
            }

            return classNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return classNames;
        }
    }


    public static ObservableList<BaseClass> getHouseClassList(String houseID){
        ObservableList<BaseClass> ischoolclass = FXCollections.observableArrayList();
        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `batchID`,`house`,`schoolID`"
                         + "FROM `base_class`, `batch` WHERE `house` = '"+houseID+"' AND `base_class`.`batchID` = `batch`.`id`"
                         + " AND `streamID` != 'GRADUATE'";

            System.out.println(query);

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                ischoolclass.add(new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID")));
            }
            return ischoolclass;
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
            return ischoolclass;
        }
    }

    /**
     * Get classes belonging to this stream under this house
     * @param houseID
     * @param streamID
     * @return
     */
    public static ObservableList<BaseClass> getHouseClassList(String houseID, String streamID){
        ObservableList<BaseClass> ischoolclass = FXCollections.observableArrayList();
        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `batchID`,`house`,`schoolID`"
                         + "FROM `base_class` WHERE `house` = '"+houseID+"' AND `batchID` = '"+streamID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                ischoolclass.add(new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID")));
            }
            return ischoolclass;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return ischoolclass;
        }
    }

    /************************************** Stream ***************************/
    public static boolean updateStream(Stream stream, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `stream` (`id`, `name`)"
                        + " VALUES ('0', '"+stream.getDescription()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `stream` SET `name`='"+stream.getDescription()+"'"
                        + " WHERE `id`= '"+stream.getStreamID()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }


    public static ObservableList<Stream> getStreams(){
        ObservableList<Stream> stream = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `name`"
                          + " FROM `stream`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                stream.add(new Stream(result.getString("id"),result.getString("name")));
            }
            return stream;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return stream;
        }
    }

    public static Stream getStreamByID(String id){
        try{
            String query = "SELECT `id`, `name`"
                         + " FROM `stream` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Stream(result.getString("id"), result.getString("name"));
            }
            return new Stream();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Stream();
        }
    }

    public static Stream getStreamByName(String name){
        try{
            String query = "SELECT `id`, `name`"
                         + " FROM `stream` WHERE `name`= '"+name+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Stream(result.getString("id"), result.getString("name"));
            }
            return new Stream();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Stream();
        }
    }


    public static boolean isStreamExists(Stream cls){
        try{
            return STATEMENT.executeQuery("SELECT * FROM `stream` WHERE `name`= '"+cls.getDescription()+"'").first();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }


    public static ObservableList<String> getStreamNames(){
        ObservableList<String> streams = FXCollections.observableArrayList();
        try{
            String query = "SELECT `name` FROM `stream` ORDER BY `name` ASC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                streams.add(result.getString("name"));
            }
            return streams;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return streams;
        }
    }



    public static ObservableList<String> getAvailableStreamNames(){
        ObservableList<String> streams = FXCollections.observableArrayList();
        try{
            String query = "SELECT `name` FROM `stream`"
                    + " WHERE `stream`.`id` NOT IN ( SELECT `batch`.`streamID` FROM `batch`) ORDER BY `name` ASC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                streams.add(result.getString("name"));
            }
            return streams;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return streams;
        }
    }


    public static ObservableList<Subject> getStreamSubjects(String streamID, int type){
        ObservableList<Subject> cluster = FXCollections.observableArrayList();
        try{
            String query = " SELECT `id`, `subject`.`subjectID`, `description`, `streamID`, `departmentID`, `type`, `schoolID`"
                         + " FROM `stream_subjects`, `subject` "
                         + " WHERE `streamID`= '"+streamID+"'"
                         + " AND `subject`.`subjectID` = `stream_subjects`.`subjectID`"
                         + " AND `type` = '"+type+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            System.out.println(query);

            while(result.next()){
                cluster.add(new Subject(result.getString("subjectID"), result.getString("departmentID"),
                                        result.getString("description"), result.getString("type"),
                                        result.getString("streamID")));
            }
            return cluster;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return cluster;
        }
    }



    /**
     * Get all students in a batch under the same stream
     * @param streamID
     * @return
     */
    public static ObservableList<Student> getStreamStudentCoreList(String streamID){
        ObservableList<Student> students = FXCollections.observableArrayList();
        try{
            String query = "SELECT `student`.`id`, `student`.`studentID`, `lastName`, `firstName`, "
                    + " `middleName`, `dob`, `batchID`, `gender`, `lastSchoolAttended`, `citizenship`, "
                    + " `email`, `postalAddress`, `physicalAddress`, `schoolID`, `enrollDate`, `status`, "
                    + " `picture`, `psle` "
                    + " FROM `student`, `batch`, `stream` "
                    + " WHERE `student`.`batchID` = `batch`.`id` "
                    + " AND `stream`.`id`=`batch`.`streamID` AND `stream`.`id` = '"+streamID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                students.add(new Student(result.getString("id"),result.getString("studentID"),
                    result.getString("firstName"), result.getString("lastName"), result.getString("middleName"),
                    result.getString("dob"), result.getString("batchID"), result.getString("gender"),
                    result.getString("lastSchoolAttended"), result.getString("psle"), result.getString("citizenship"),
                    result.getString("email"), result.getString("postalAddress"), result.getString("physicalAddress"),
                    result.getString("enrollDate"), result.getString("status"), result.getString("picture"),
                    result.getString("schoolID")));
            }
            return students;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return students;
        }
    }

    /**
     * Get all students in a stream under the same optional subject
     * @param streamID
     * @param subjectID
     * @return
     */
    public static ObservableList<Student> getStreamStudentOptionalList(String streamID, String subjectID){
        ObservableList<Student> students = FXCollections.observableArrayList();
        try{
            String query = "SELECT `student`.`id`, `student`.`studentID`, `lastName`, `firstName`, "
                    + " `middleName`, `dob`, `batchID`, `gender`, `lastSchoolAttended`, `citizenship`, "
                    + " `email`, `postalAddress`, `physicalAddress`, `schoolID`, `enrollDate`, `status`, "
                    + " `picture`, `psle` "
                    + " FROM `student`, `batch`, `student_optional_subjects` "
                    + " WHERE `student`.`batchID` = `batch`.`id` "
                    + " AND `student_optional_subjects`.`studentId` = `student`.`studentID`"
                    + " AND `batch`.`streamID` = '"+streamID+"' AND `student_optional_subjects`.`subjectID` = '"+subjectID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                students.add(new Student(result.getString("id"),result.getString("studentID"),
                    result.getString("firstName"), result.getString("lastName"), result.getString("middleName"),
                    result.getString("dob"), result.getString("batchID"), result.getString("gender"),
                    result.getString("lastSchoolAttended"), result.getString("psle"), result.getString("citizenship"),
                    result.getString("email"), result.getString("postalAddress"), result.getString("physicalAddress"),
                    result.getString("enrollDate"), result.getString("status"), result.getString("picture"),
                    result.getString("schoolID")));
            }
            return students;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return students;
        }
    }


    /**
     * A list of class students enrolled in a particular optional subject
     * @param classID base class
     * @param subjectID Optional subject
     * @return
     */
    public static ObservableList<Student> getOptionalSubjectStudentList(String classID, String subjectID){

        ObservableList<Student> students = FXCollections.observableArrayList();
        try{
            String query = "SELECT `student`.`id`, `student`.`studentID`, `lastName`, `firstName`, `middleName`, `dob`, `student`.`batchID`, `gender`,"
                         + " `lastSchoolAttended`, `citizenship`, `email`, `postalAddress`, `physicalAddress`,"
                         + " `schoolID`, `enrollDate`, `status`, `psle`, `picture`, `schoolID`"
                         + " FROM `student_optional_subjects`, `student`, `student_base_class`"
                         + " WHERE `student`.`studentID` = `student_optional_subjects`.`studentID` "
                         + " AND `student_optional_subjects`.`studentID` = `student_base_class`.`studentID`"
                         + " AND `subjectID` = '"+subjectID+"'"
                         + " AND `classID`= '"+classID+"'";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                students.add(new Student(result.getString("id"),result.getString("studentID"),
                    result.getString("firstName"), result.getString("lastName"), result.getString("middleName"),
                    result.getString("dob"), result.getString("batchID"), result.getString("gender"),
                    result.getString("lastSchoolAttended"), result.getString("psle"), result.getString("citizenship"),
                    result.getString("email"), result.getString("postalAddress"), result.getString("physicalAddress"),
                    result.getString("enrollDate"), result.getString("status"), result.getString("picture"),
                    result.getString("schoolID")));
            }
            return students;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return students;
        }
    }


    /**
     * A list of virtual class students enrolled in a particular optional subject
     * @param classID base class
     * @param subjectID Optional subject
     * @return
     */
    public static ObservableList<Student> getVirtualClassStudentList(String classID, String subjectID){

        ObservableList<Student> students = FXCollections.observableArrayList();
        try{
            String query = "SELECT `student`.`id`, `student`.`studentID`, `lastName`, `firstName`, `middleName`, `dob`, `student`.`batchID`, `gender`,"
                         + " `lastSchoolAttended`, `citizenship`, `email`, `postalAddress`, `physicalAddress`,"
                         + " `schoolID`, `enrollDate`, `status`, `psle`, `picture`, `schoolID`"
                         + " FROM `student_optional_subjects`, `student`, `student_virtual_class`"
                         + " WHERE `student`.`studentID` = `student_optional_subjects`.`studentID` "
                         + " AND `student_optional_subjects`.`studentID` = `student_virtual_class`.`studentID`"
                         + " AND `subjectID` = '"+subjectID+"'"
                         + " AND `classID`= '"+classID+"'";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                students.add(new Student(result.getString("id"),result.getString("studentID"),
                    result.getString("firstName"), result.getString("lastName"), result.getString("middleName"),
                    result.getString("dob"), result.getString("batchID"), result.getString("gender"),
                    result.getString("lastSchoolAttended"), result.getString("psle"), result.getString("citizenship"),
                    result.getString("email"), result.getString("postalAddress"), result.getString("physicalAddress"),
                    result.getString("enrollDate"), result.getString("status"), result.getString("picture"),
                    result.getString("schoolID")));
            }
            return students;

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return students;
        }
    }



    public static boolean updateRegion(Region region, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `region` (`id`, `name`, `district`)"
                        + " VALUES ('0', '"+region.getName()+"', '"+region.getDistrict()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `region` SET `name`='"+region.getName()+"',"
                        + "`district`='"+region.getDistrict()+"'"
                        + " WHERE `id`= '"+region.getID()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }

    public static Region getRegionByID(String id){
        try{
            String query = "SELECT `id`, `name`, `district`"
                    + " FROM `region` WHERE `id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                return new Region(result.getString("id"), result.getString("name"),
                        result.getString("district"));
            }
            return new Region();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return new Region();
        }
    }

    public static Region getRegionByName(String id){
        try{
            String query = "SELECT `id`, `name`, `district`"
                    + " FROM `region` WHERE `name`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Region(result.getString("id"), result.getString("name"), result.getString("district"));
            }
            return new Region();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return new Region();
        }
    }


    public static ObservableList<String> getRegionNames(){

        ObservableList<String> regions = FXCollections.observableArrayList();
        try{
            String query = " SELECT `name`"
                    + " FROM `region`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                regions.add((result.getString("name")));
            }
            return regions;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return regions;
        }
    }



    /**
     *
     * @return
     */
    public static ObservableList<Region> getRegions(){
        ObservableList<Region> regions = FXCollections.observableArrayList();
        try{
            String query = " SELECT `id`, `name`, `district`"
                    +" FROM `region`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                regions.add(new Region(result.getString("id"),
                        result.getString("name"),
                        result.getString("district")));
            }
            return regions;
        }
        catch(Exception ex){
            return regions;
        }
    }





    public static boolean deleteSubjectStream(String streamID){
        try{
            String query = "DELETE FROM `stream_subjects` WHERE `streamID` = '"+streamID+"'";

            return STATEMENT.executeUpdate(query) > 0;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

        /**
     * Add students to class
     * @param addIDs
     * @param removeIDs
     * @param streamID
     * @return
     */
    public static boolean addStreamSubjects(ObservableList<String> addIDs, ObservableList<String> removeIDs, String streamID){

        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String sbjID: removeIDs){
                    query = "DELETE FROM `stream_subjects` WHERE `streamID` = '"+streamID+"' AND `subjectID` = '"+sbjID+"'";
                    System.out.println(query);
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeUpdate(query);
            }

            if(!addIDs.isEmpty()){
                for(String sbjID: addIDs){
                    query = "INSERT INTO `stream_subjects` (`id`, `subjectID`, `streamID`)"
                            + " VALUES ('0', '"+sbjID+"', '"+streamID+"')";
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
     * Students optional subject allocation
     * @param addIDs
     * @param removeIDs
     * @param subjectID
     * @return
     */
    public static boolean updateOptionalSubjectStudent(ObservableList<String> addIDs, ObservableList<String> removeIDs, String subjectID){

        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String stdID: removeIDs){
                    query = "DELETE FROM `student_optional_subjects` WHERE `subjectID` = '"+subjectID+"' AND `studentID` = '"+stdID+"'";
                    System.out.println(query);
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeUpdate(query);
            }

            if(!addIDs.isEmpty()){
                for(String stdID: addIDs){
                    query = "INSERT INTO `student_optional_subjects` (`id`, `subjectID`, `studentID`)"
                            + " VALUES ('0', '"+subjectID+"', '"+stdID+"')";
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


    public static ObservableList<String> getStreamSubjectNameList(String streamID, int type){
        ObservableList<String> cluster = FXCollections.observableArrayList();
        try{
            String query = " SELECT `id`, `description`, `streamID`"
                         + " FROM `stream_subjects`, `subject` "
                         + " WHERE `streamID`= '"+streamID+"'"
                         + " AND `subject`.`subjectID` = `stream_subjects`.`subjectID`"
                         + " AND `type` = '"+type+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                cluster.add(result.getString("description"));
            }
            return cluster;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             //
             return cluster;
        }
    }

    public static ObservableList<Subject> getOptionalSubjectFor(String studentID){
        ObservableList<Subject> optionalSubjects = FXCollections.observableArrayList();

        try{
            String query = "SELECT `description`, `subject`.`subjectID`, `departmentID`, `type`, `schoolID`"
                         + " FROM `student_optional_subjects`, `subject` "
                         + " WHERE `studentID` = '"+studentID+"'"
                         + " AND `student_optional_subjects`.`subjectID` = `subject`.`subjectID`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                optionalSubjects.add(new Subject(result.getString("subjectID"), result.getString("departmentID"),
                        result.getString("description"), result.getString("type"), result.getString("schoolID")));
            }
            return optionalSubjects;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             return optionalSubjects;
        }
    }

    public static ObservableList<String> getSubjectsList(int type){
        ObservableList<String> cluster = FXCollections.observableArrayList();
        try{
            String query = "SELECT `description`"
                         + " FROM `subject` "
                         + " WHERE  `type` = '"+type+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                cluster.add(result.getString("description"));
            }
            return cluster;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return cluster;
        }
    }






    public static ObservableList<Subject> getStreamSubjectsList(String streamID){

        ObservableList<Subject> cluster = FXCollections.observableArrayList();
        try{
            String query = "SELECT `subject`.`subjectID`, `description`, `departmentID`, `type`, `schoolID`"
                         + " FROM `stream_subjects`, `subject` "
                         + " WHERE `streamID`= '"+streamID+"'"
                         + " AND `subject`.`subjectID` = `stream_subjects`.`subjectID`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                cluster.add(new Subject(result.getString("subjectID"), result.getString("departmentID"),
                        result.getString("description"), result.getString("type"), result.getString("schoolID")));
            }
            return cluster;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return cluster;
        }
    }



    public static ObservableList<String> getStreamSubjectIDs(String streamID, int type){
        ObservableList<String> cluster = FXCollections.observableArrayList();
        try{
            String query = " SELECT `subject`.`subjectID`, `description`, `streamID`"
                         + " FROM `stream_subjects`, `subject` "
                         + " WHERE `streamID`= '"+streamID+"'"
                         + " AND `subject`.`subjectID` = `stream_subjects`.`subjectID`"
                         + " AND `type` = '"+type+"'";

            ResultSet result = STATEMENT.executeQuery(query);
            System.out.println(query);

            while(result.next()){
                cluster.add(result.getString("subjectID"));
            }
            return cluster;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             //
             return cluster;
        }
    }


     /*-***************************** SUBJECTS TEACHERS ***********************/

    public static ObservableList<String> getSubjectTeachersNames(String subjectID){

        ObservableList<String> teacherNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `firstName`, `middleName`, `lastName`" +
                            " FROM `subjectteachers`, `employee`" +
                            " WHERE `subjectID` = '"+subjectID+"'" +
                            " AND `employeeID` = `teacherID`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                teacherNames.add(result.getString("firstName")+" "+result.getString("lastName"));
            }

            return teacherNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return teacherNames;
        }
    }


    /*-***************************** SUBJECTS TEACHERS ***********************/

    public static ObservableList<String> getDepartmentSubjectNames(String departmentID){

        ObservableList<String> subjects = FXCollections.observableArrayList();
        try{
            String query = "SELECT `description`" +
                            " FROM `subject`" +
                            " WHERE `departmentID` = '"+departmentID+"'";

            //System.out.println(query);

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                subjects.add(result.getString("description"));
            }

            return subjects;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return subjects;
        }
    }


     /**************************************************************************
     *
     * @return
     */
    public static ObservableList<BaseClass> getClassList(){
        ObservableList<BaseClass> ischoolclass = FXCollections.observableArrayList();
        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `house`, `batchID`,`schoolID`"
                         + "FROM `base_class`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                ischoolclass.add(new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID")));
            }
            return ischoolclass;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return ischoolclass;
        }
    }

    /**
     *
     * @param descr
     * @param batchID
     * @return
     */
    public static BaseClass getBaseClassByName(String descr, String batchID){

        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `house`, `batchID`, `schoolID`"
                         + "FROM `base_class` WHERE `className` = '"+descr+"' AND `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID"));
            }
            return new BaseClass();

        }catch(Exception ex){
             System.out.println(ex.getMessage());

             return new BaseClass();
        }
    }


     /**
     *
     * @param id
     * @return
     */
    public static BaseClass getBaseClassByID(String id){

        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `house`, `batchID`, `schoolID`"
                         + " FROM `base_class` WHERE `classID` = '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID"));
            }
            return new BaseClass();

        }catch(Exception ex){
             System.out.println(ex.getMessage());

             return new BaseClass();
        }
    }


    /**
     *
     * @param id
     * @return
     */
    public static boolean removeBaseClass(String id){

        try{
            String query = "DELETE FROM `base_class` WHERE `classID` = '"+id+"'";
            STATEMENT.addBatch(query);
            STATEMENT.addBatch("DELETE FROM `timetable` WHERE `classID` = '"+id+"'");

            STATEMENT.executeBatch();
            return true;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }


    /**
     *
     * @param descr
     * @return
     */
    public static VirtualClass getVirtualClassByName(String descr, String batchID){

        try{
            String query = "SELECT `classID`, `name`, `teacherID`, `subjectID`, `batchID`"
                         + "FROM `virtual_class` WHERE `name` = '"+descr+"' AND `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new VirtualClass(result.getString("classID"),result.getString("name"),
                        result.getString("teacherID"), result.getString("subjectID"),
                        result.getString("batchID"),"");
            }
            return new VirtualClass();

        }catch(Exception ex){
             System.out.println(ex.getMessage());

             return new VirtualClass();
        }
    }


     /**
     *
     * @param id
     * @return
     */
    public static VirtualClass getVirtualClassByID(String id){

        try{
            String query = "SELECT `classID`, `name`, `teacherID`, `subjectID`, `batchID`"
                         + " FROM `virtual_class` WHERE `classID` = '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new VirtualClass(result.getString("classID"),result.getString("name"),
                        result.getString("teacherID"), result.getString("subjectID"),
                        result.getString("batchID"), "");
            }
            return new VirtualClass();

        }catch(Exception ex){
             System.out.println(ex.getMessage());

             return new VirtualClass();
        }
    }


    /**
     *
     * @param id
     * @return
     */
    public static boolean removeVirtualClass(String id){

        try{
            String query = "DELETE FROM `virtual_class` WHERE `classID` = '"+id+"'";
            STATEMENT.addBatch(query);

            STATEMENT.executeBatch();
            return true;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }

    /**
     *
     * @return
     */
    public static ObservableList<String> getBaseClassNames(){
        ObservableList<String> classNames = FXCollections.observableArrayList();
        try{
            String query = "SELECT `className` FROM `base_class` ORDER BY `className` ASC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                classNames.add(result.getString("className"));
            }
            return classNames;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return classNames;
        }
    }



    public static ObservableList<GradeScheme> getGrades(){
        ObservableList<GradeScheme> grades = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `symbol`, `lowerBound`, `upperBound`, `points`"
                         + " FROM `grading`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                grades.add(new GradeScheme(result.getString("id"), result.getString("symbol"),
                        result.getString("lowerBound"), result.getString("upperBound"), result.getString("points")));
            }
            return grades;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return grades;
        }
    }

    public static boolean updateGrades(ObservableList<GradeScheme> grades, boolean update){
        try{
            String query;
            if(!update){
                for(GradeScheme grade: grades){
                    query = "INSERT INTO `grading` "
                        + "(`id`, `symbol`, `lowerBound`, `upperbound`, `points`)"
                        + " VALUES ('0', '"+grade.getSymbol()+"',"
                        + " '"+grade.getLowerBound()+"',"
                        + " '"+grade.getUpperBound()+"',"
                        + " '"+grade.getPoints()+"')";

                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch();
                return true;

            }else{
                for(GradeScheme grade: grades){
                    query = "UPDATE `grading` "
                            + " SET `symbol`='"+grade.getSymbol()+"',"
                            + " `lowerBound`='"+grade.getLowerBound()+"',"
                            + " `upperBound`='"+grade.getUpperBound()+"',"
                            + " `points`='"+grade.getPoints()+"'"
                            + " WHERE `id`= '"+grade.getId()+"'";

                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch();
                return true;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }




    ///////////////////////////////////BATCHES/////////////////////////////////
     public static boolean updateBatch(Batch batch, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `batch` (`id`, `description`, `start`, `end`, `streamID`)"
                        + " VALUES ('0', '"+batch.getDescription()+"', '"+batch.getStart()+"'"
                        + ", '"+batch.getEnd()+"', '"+batch.getStreamID()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `batch` SET `description`='"+batch.getDescription()+"', `start`='"+batch.getStart()+"',"
                        + " `end`='"+batch.getEnd()+"', `streamID` = '"+batch.getStreamID()+"'"
                        + " WHERE `id`= '"+batch.getId()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return false;
        }
    }


    public static ObservableList<Batch> getBatches(){
        ObservableList<Batch> batch = FXCollections.observableArrayList();
        try{
            String query = "SELECT `batch`.`id`, `start`, `end`, `description`, `streamID`"
                          + " FROM `batch`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                batch.add(new Batch(result.getString("id"),result.getString("description"), result.getString("start")
                        ,result.getString("end"),result.getString("streamID")));
            }
            return batch;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return batch;
        }
    }

    public static Batch getBatchByID(String id){
        try{
            String query = "SELECT `batch`.`id`, `start`, `end`, `description`, `streamID`"
                          + " FROM `batch` WHERE `batch`.`id`= '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Batch(result.getString("id"),result.getString("description"), result.getString("start")
                        ,result.getString("end"),result.getString("streamID"));
            }
            return new Batch();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return new Batch();
        }
    }

    public static Batch getBatchByName(String name){
        try{
            String query = "SELECT `batch`.`id`, `start`, `end`, `description`, `streamID`"
                          + " FROM `batch` WHERE  `description`= '"+name+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Batch(result.getString("id"),result.getString("description"), result.getString("start")
                        ,result.getString("end"),result.getString("streamID"));
            }
            return new Batch();
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return new Batch();
        }
    }


    public static boolean isBatchExists(Batch cls){
        try{
            if(cls.getId().equals("0")){
                return STATEMENT.executeQuery("SELECT * FROM `batch` "
                    + "WHERE `description` = '"+cls.getDescription()+"'").first();
            }else{
                return STATEMENT.executeQuery("SELECT * FROM `batch` "
                    + "WHERE `id` != '"+cls.getId()+"' AND `description` = '"+cls.getDescription()+"'").first();
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }


    public static ObservableList<String> getBatchNames(){
        ObservableList<String> streams = FXCollections.observableArrayList();
        try{
            String query = "SELECT `description` FROM `batch` ORDER BY `description` DESC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                streams.add(result.getString("description"));
            }
            return streams;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return streams;
        }
    }

    public static ObservableList<String> getBatchBaseClassNames(String batchID){
        ObservableList<String> stream = FXCollections.observableArrayList();
        try{
            String query = "SELECT `className` FROM `base_class` WHERE `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                stream.add(result.getString("className"));
            }
            return stream;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return stream;
        }
    }


    public static ObservableList<BaseClass> getBatchBaseClassList(String batchID){
        ObservableList<BaseClass> ischoolclass = FXCollections.observableArrayList();
        try{
            String query = "SELECT `classID`, `className`, `classTeacher`, `batchID`,`house`,`schoolID`"
                         + "FROM `base_class` WHERE `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                ischoolclass.add(new BaseClass(result.getString("classID"),result.getString("className"),
                        result.getString("classTeacher"), result.getString("house"),
                        result.getString("batchID"),result.getString("schoolID")));
            }
            return ischoolclass;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             return ischoolclass;
        }
    }




    public static ObservableList<String> getBatchVirtualClassNames(String batchID){
        ObservableList<String> stream = FXCollections.observableArrayList();
        try{
            String query = "SELECT `name` FROM `virtual_class` WHERE `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                stream.add(result.getString("name"));
            }
            return stream;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return stream;
        }
    }


    public static ObservableList<VirtualClass> getBatchVirtualClassList(String batchID){
        ObservableList<VirtualClass> ischoolclass = FXCollections.observableArrayList();
        try{
            String query = "SELECT `classID`, `name`, `teacherID`, `batchID`,`subjectID`"
                         + "FROM `virtual_class` WHERE `batchID` = '"+batchID+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                ischoolclass.add(new VirtualClass(result.getString("classID"),result.getString("name"),
                        result.getString("teacherID"), result.getString("subjectID"),
                        result.getString("batchID"),""));
            }
            return ischoolclass;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             return ischoolclass;
        }
    }




    public static boolean updateTerm(Term term, boolean update){

        try{
            String insertQuery = "INSERT INTO `term` (`id`, `description`, `from`, `to`, `year`, `currentTerm`)"
                    + " VALUES ('0', '"+term.getDescription()+"','"+term.getStart()+"',"
                    + " '"+term.getEnd()+"', '"+term.getYear()+"','"+term.isCurrentTerm()+"')";


            String updateQuery = "UPDATE `term` SET `description`='"+term.getDescription()+"',"
                    + " `from`='"+term.getStart()+"', `to`='"+term.getEnd()+"', `year`='"+term.getYear()+"',"
                    + " `currentTerm`='"+term.isCurrentTerm()+"'"
                    + " WHERE `id`= '"+term.getTermID()+"'";

            if("1".equals(term.isCurrentTerm())){
                String query = "UPDATE `term` SET `currentTerm`= '0'";
                STATEMENT.addBatch(query);
            }

            if(update){
                STATEMENT.addBatch(updateQuery);
            }else{
                STATEMENT.addBatch(insertQuery);
            }

            STATEMENT.executeBatch();

            return true;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());
             return false;
        }
    }



    public static ObservableList<Term> getTermList(){
        ObservableList<Term> terms = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `description`, `from`, `to`, `year`, `currentTerm`"
                         + "FROM `term` ORDER BY `year` DESC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                terms.add(new Term(result.getString("id"),result.getString("description"), result.getString("from"),
                          result.getString("to"), result.getString("year"), result.getString("currentTerm")));
            }
            return terms;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return terms;
        }
    }

    public static Term getTermByID(String id){
        try{
            String query = "SELECT `id`, `description`, `from`, `to`, `year`, `currentTerm`"
                         + "FROM `term` WHERE `id` = '"+id+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Term(result.getString("id"),result.getString("description"), result.getString("from"),
                          result.getString("to"), result.getString("year"), result.getString("currentTerm"));
            }
            return new Term();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return new Term();
        }
    }



    public static Term getTermByName(String name, String year){
        try{
            String query = "SELECT `id`, `description`, `from`, `to`, `year`, `currentTerm`"
                         + "FROM `term` WHERE `description` = '"+name+"' AND `year` = '"+year+"'";

            ResultSet result = STATEMENT.executeQuery(query);

            if(result.next()){
                return new Term(result.getString("id"),result.getString("description"), result.getString("from"),
                          result.getString("to"), result.getString("year"), result.getString("currentTerm"));
            }
            return new Term();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

            return new Term();
        }
    }


    /**
     *
     * @return
     */
    public static ObservableList<String> getTermNameList(){
        ObservableList<String> terms = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `description`, `from`, `to`, `year`, `currentTerm`"
                         + "FROM `term` ORDER BY `id`";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                terms.add(result.getString("description"));
            }
            return terms;
        }
        catch(Exception ex){
             System.out.println(ex.getMessage());

             return terms;
        }
    }




    public static boolean addAcademicYear(String year){

        try{
            String insertQuery = "INSERT INTO `academic_year` (`id`,`year`) VALUES ('0', '"+year+"')";

            return STATEMENT.executeUpdate(insertQuery) > 0;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean isAcademicYearExist(String year){

        try{
            String query = "SELECT `year` FROM `academic_year` WHERE `year` = '"+year+"'";

            return STATEMENT.executeQuery(query).first();

        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }



    public static ObservableList<String> getAcademicYearList(){
        ObservableList<String> year = FXCollections.observableArrayList();
        try{
            String query = "SELECT `year` FROM `academic_year` ORDER BY `year` ASC";

            ResultSet result = STATEMENT.executeQuery(query);

            while(result.next()){
                year.add(result.getString("year"));
            }
            return year;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return year;
        }
    }


    public static boolean updateUserRole(UserRoles role, boolean update){

        try{
            String query;
            if(!update){
                query = "INSERT INTO `userrole` (`id`, `description`,`priviledges`)"
                        + " VALUES ('0', '"+role.getDescription()+"', '"+role.getPriviledges()+"')";

                return STATEMENT.executeUpdate(query) > 0;

            }else{
                query = "UPDATE `userrole` SET `description`='"+role.getDescription()+"',"
                        + "`priviledges` = '"+role.getPriviledges()+"'"
                        + " WHERE `id`= '"+role.getID()+"'";

                return STATEMENT.executeUpdate(query) > 0;
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

}
