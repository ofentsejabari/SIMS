package com.bitri.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.bitri.resource.dao.MySQLHander.STATEMENT;
import com.bitri.service.studentmanagement.SocialWelfare;
import com.bitri.service.studentmanagement.SpecialNeed;
import com.bitri.service.studentmanagement.Student;
import com.bitri.service.studentmanagement.StudentSocialWelfareModel;
import com.bitri.service.studentmanagement.StudentSpecialNeedsModel;

/**
 *
 * @author JABARI
 */
public class StudentQuery {
    
    
    
    public static ObservableList<String> getBaseClassAllocatedStudents(String batchID){
        
        ObservableList<String> studentIDs = FXCollections.observableArrayList();
        
        try{
            String query = " SELECT `studentID` FROM `student_base_class`, `base_class`"
                    + " WHERE `base_class`.`classID` = `student_base_class`.`classID` "
                    + " AND `batchID` = '"+batchID+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                studentIDs.add(result.getString("studentID"));
            }
            return studentIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return studentIDs;
        }
    }
    
    
    public static ObservableList<String> getBaseClassUnAllocatedStudents(String batchID){
        
        ObservableList<String> studentIDs = FXCollections.observableArrayList();
        
        try{
            String query = "SELECT CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName`"
                            + " FROM `student`"
                            + " WHERE `batchID` = '"+batchID+"'"
                            + " AND `student`.`studentID` NOT IN ( SELECT `student`.`studentID`"
                            + " FROM `student_base_class`,`student` WHERE `student`.`studentID`=`student_base_class`.`studentID` "
                            + " AND `batchID` = '"+batchID+"')";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                studentIDs.add(result.getString("fullName"));
            }
            return studentIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return studentIDs;
        }
    }
    
    
        
    public static ObservableList<StudentSpecialNeedsModel> getBatchSpecialNeedStudents(String batchID,String specialNeedID){
        
        ObservableList<StudentSpecialNeedsModel> studentIDs = FXCollections.observableArrayList();
        
        try{
            String query ="SELECT `special_needs`.`name`, `sn_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` ,`student_sn`.`studentID`,`gender`, `student_sn`.`description`, `student_sn`.`solution`  "
                         + " FROM `student`,`student_sn`,`special_needs`"
                         + " WHERE `student`.`batchID` = '"+batchID+"'"
                         + " AND `special_needs`.`id`="+specialNeedID
                         + " AND `student_sn`.`studentID`=`student`.`studentID` "
                         + " AND `special_needs`.`id`=`student_sn`.`sn_id`";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                studentIDs.add(new StudentSpecialNeedsModel(result.getString("name"),result.getString("sn_id"),result.getString("studentID"),result.getString("fullName"), result.getString("description"), result.getString("solution"),result.getString("gender")));
            }
            return studentIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return studentIDs;
        }
    }

    
      public static ObservableList<StudentSocialWelfareModel> getBatchSocialWelfareStudents(String batchID,String socialWelfareID){
        
        ObservableList<StudentSocialWelfareModel> studentIDs = FXCollections.observableArrayList();
        
        try{
             String query = "SELECT `social_welfare`.`name`, `sw_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` "
                        + ",`student_sw`.`studentID`,`gender`, `student_sw`.`sw_aid_id`  "
                         + " FROM `student`,`student_sw`,`social_welfare`"
                         + " WHERE `student_sw`.`studentID`=`student`.`studentID`"
                         + " AND `social_welfare`="+socialWelfareID
                         + " AND `student_sw`.`sw_id`=`social_welfare`.`id`"
                         + " AND `student`.`batchID` = '"+batchID+"' ";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                studentIDs.add(new StudentSocialWelfareModel(result.getString("name"),result.getString("sw_id"),result.getString("studentID"),result.getString("fullName"), result.getString("sw_aid_id"),result.getString("gender")));
            }
            return studentIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return studentIDs;
        }
    }
    
    
     public static StudentSpecialNeedsModel getSpecialNeedStudentBySSNID(String ssnId){
        
  
         StudentSpecialNeedsModel student= new StudentSpecialNeedsModel();
        
        try{
            String query = "SELECT `special_needs`.`name`, `sn_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` ,`student_sn`.`studentID`,`gender`, `student_sn`.`description`, `student_sn`.`solution`  "
                         + " FROM `student`,`student_sn`,`special_needs`"
                         + " WHERE `student_sn`.`studentID`=`student`.`studentID`"
                         + " AND `student_sn`.`id`='"+ssnId+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new StudentSpecialNeedsModel(result.getString("name"),result.getString("sn_id"),result.getString("studentID"),result.getString("fullName"), result.getString("description"), result.getString("solution"),result.getString("gender"));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
    }
     
     public static StudentSocialWelfareModel getStudentSocialWelfareBySSWID(String sswId){
        
        StudentSocialWelfareModel student= new StudentSocialWelfareModel();
        
        try{
            String query = "SELECT `social_welfare`.`name`, `sw_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` "
                        + ",`student_sw`.`studentID`,`gender`, `student_sw`.`sw_aid_id`  "
                         + " FROM `student`,`student_sw`,`social_welfare`"
                         + " WHERE `student_sw`.`studentID`=`student`.`studentID`"
                         + " AND `student_sw`.`id`='"+sswId+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new StudentSocialWelfareModel(result.getString("name"),result.getString("sw_id"),result.getString("studentID"),result.getString("fullName"), result.getString("sw_aid_id"),result.getString("gender"));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
     
     }
     
       public static StudentSocialWelfareModel getStudentSocialWelfareBySSWName(String sswName){
        
        StudentSocialWelfareModel student= new StudentSocialWelfareModel();
        
        try{
            String query = "SELECT `social_welfare`.`name`, `sw_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` "
                        + ",`student_sw`.`studentID`,`gender`, `student_sw`.`sw_aid_id`  "
                         + " FROM `student`,`student_sw`,`social_welfare`"
                         + " WHERE `student_sw`.`studentID`=`student`.`studentID`"
                         + " AND `student_sw`.`sw_id`=`social_welfare`.`id` "
                        + " AND `social_welfare`.`name`='"+sswName+"'";
            System.out.println(query);
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new StudentSocialWelfareModel(result.getString("name"),result.getString("sw_id"),result.getString("studentID"),result.getString("fullName"), result.getString("sw_aid_id"),result.getString("gender"));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
     
     }
   
     public static ObservableList<StudentSpecialNeedsModel> getBaseClassSpecialNeedStudents(String class_id){
        
        ObservableList<StudentSpecialNeedsModel> student= FXCollections.observableArrayList();
        
        try{
            String query = "SELECT `student_sn`.`id`, `sn_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` ,`student_sn`.`studentID`,`gender`, `student_sn`.`description`, `student_sn`.`solution`  "
                         + " FROM `student`,`student_base_class`,`student_sn`"
                         + " WHERE `student_base_class`.`classID` = '"+class_id+"'"
                         + " AND `student_sn`.`studentID`=`student`.`studentID`"
                         + " AND `student`.`studentID`=`student_base_class`.`studentID`";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                student.add(new StudentSpecialNeedsModel(result.getString("id"),result.getString("sn_id"),result.getString("studentID"),result.getString("fullName"), result.getString("description"), result.getString("solution"),result.getString("gender")));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
    }
    
    public static ObservableList<StudentSpecialNeedsModel> getSelectedNeedStudents(String class_id,String selected){
        
        ObservableList<StudentSpecialNeedsModel> student= FXCollections.observableArrayList();
        
        try{
            String query = "SELECT `student_sn`.`id`, `sn_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` ,`student_sn`.`studentID`,`gender`, `student_sn`.`description`, `student_sn`.`solution`  "
                         + " FROM `student`,`student_base_class`,`student_sn`"
                         + " WHERE `student_base_class`.`classID` = '"+class_id+"'"
                         + " AND `student_sn`.`studentID`=`student`.`studentID`"
                         + " AND `student`.`studentID`=`student_base_class`.`studentID`"
                         + "AND `sn_id`='"+selected+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                student.add(new StudentSpecialNeedsModel(result.getString("id"),result.getString("sn_id"),result.getString("studentID"),result.getString("fullName"), result.getString("description"), result.getString("solution"),result.getString("gender")));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
    }
     
    
    /**
     * 
     * @param sn
     * @return 
     */
    public static ObservableList<String> getSocialWelfareAidNamesBySWID(String swID){
        
        ObservableList<String> sws = FXCollections.observableArrayList();
        try{
            String query = "SELECT `id`, `name`, `cooperation`, `swID`, `description`"
                         + "FROM `sw_aid` WHERE `swID`='"+swID+"'";
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                sws.add(result.getString("name"));
            }
            return sws;
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return sws;
        }
    }
    
    public static String getStudentSpecialNeeds(String studentID)
    {
        String specialneed = "";
        
        try{
            String query="SELECT `description` FROM `student_sn` WHERE `studentID`='"+studentID+"'";
            ResultSet resultSet= STATEMENT.executeQuery(query);
            
            while(resultSet.next()){
                if(specialneed.equals("")){
                    specialneed=resultSet.getString("description");
                }
                else{
                    specialneed+= ", "+resultSet.getString("description");
                }
            }
            
            return specialneed;
        }
        catch(Exception e){
            return specialneed;
        }
    }
    
    
     public static ObservableList<StudentSocialWelfareModel> getSelectedStudentSocialWelfare(String class_id,String selected){
        
        ObservableList<StudentSocialWelfareModel> student= FXCollections.observableArrayList();
        
        try{
            String query = "SELECT `student_sw`.`id`, `sw_id`,CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName` ,`student_sw`.`studentID`,`gender`, `sw_aid_id` "
                         + " FROM `student`,`student_base_class`,`student_sw`"
                         + " WHERE `student_base_class`.`classID` = '"+class_id+"'"
                         + " AND `student_sw`.`studentID`=`student`.`studentID`"
                         + " AND `student`.`studentID`=`student_base_class`.`studentID`"
                         + "AND `sw_id`='"+selected+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                
                student.add(new StudentSocialWelfareModel(result.getString("id"),result.getString("sw_id"),result.getString("studentID"),result.getString("fullName"), result.getString("sw_aid_id"),result.getString("gender")));
            }
            return student;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return student;
        }
    }
    
    public static boolean updateStudentSpecialNeed(Student student, SpecialNeed specialNeed, String description,String solution,boolean flag){
    
    
        if(flag){
            try{
                String query = "UPDATE `student_sn` SET "
                        + "`description`='"+description+"',`solution`='"+solution+"' WHERE `studentID`='"+student.getStudentID()+"' AND `sn_id`='"+specialNeed.getId()+"'";
                STATEMENT.executeUpdate(query);
                
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
        }
        else{
            try{
                String query = "INSERT INTO `student_sn`(`id`, `sn_id`, `studentID`, `description`, `solution`) VALUES (0,'"+specialNeed.getId()+"','"+student.getStudentID()+"','"+description+"','"+solution+"')";
                STATEMENT.executeUpdate(query);
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
        }
    
    }
    
      public static boolean updateStudentSocialWelfare(Student student, SocialWelfare socialWelfare, String aid,boolean flag){
    
    
        if(flag){
            try{
                String query = "UPDATE `student_sw` SET "
                        + "`sw_aid_id`='"+aid+"' WHERE `studentID`='"+student.getStudentID()+"' AND `sw_id`='"+socialWelfare.getId()+"'";
                System.out.println(query);
                STATEMENT.executeUpdate(query);
                
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
        }
        else{
            try{
                String query = "INSERT INTO `student_sw`(`id`, `studentID`, `sw_id`, `sw_aid_id`) VALUES (0,'"+student.getStudentID()+"','"+socialWelfare.getId()+"','"+aid+"')";
                STATEMENT.executeUpdate(query);
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
        }
    
    }
    
     /**
     * 
     * @param sn
     * @return 
     */
    public static SpecialNeed getSpecialNeedByID(String sn){
        try{
            String query = "SELECT `id`, `name`, `description`"
                         + "FROM `special_needs` WHERE `id`="+sn;
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return new SpecialNeed(result.getString("id"),result.getString("name"), result.getString("description"));
            }
            return new SpecialNeed();
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return new SpecialNeed();
        }
    }
    
    public static boolean deleteStudentSpecialNeed(Student student, SpecialNeed specialNeed){
    
    
            try{
                String query = "DELETE FROM `student_sn` WHERE `sn_id`='"+specialNeed.getId()+"' AND `studentID`='"+student.getStudentID()+"'";
                STATEMENT.executeUpdate(query);
                
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
      
    
    }
    
    
    public static boolean deleteStudentSocialWelfare(Student student, SocialWelfare socialWelfare){
    
    
            try{
                String query = "DELETE FROM `student_sw` WHERE `sw_id`='"+socialWelfare.getId()+"' AND `studentID`='"+student.getStudentID()+"'";
                STATEMENT.executeUpdate(query);
                
                return true;
            }
            catch(Exception e){
                System.out.print(e);
                return false;
            }
      
    
    }
    public static ObservableList<StudentSocialWelfareModel> getSocialWelfareFor(String studentID){
        
        ObservableList<StudentSocialWelfareModel> socialWelfare= FXCollections.observableArrayList();
        
           try{
                    String query="SELECT CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName`,`student_sw`.`id`,`name`, `sw_id`,`gender`,`student_sw`.`studentID`, `student_sw`.`sw_aid_id`"
                            + " FROM `student_sw`,`social_welfare`,`student`"
                            + " WHERE `student`.`studentID`=`student_sw`.`studentID` AND `sw_id`=`social_welfare`.`id` "
                            + "AND `student_sw`.`studentID`='"+studentID+"'";
                 
                    ResultSet resultSet= STATEMENT.executeQuery(query);

                    while(resultSet.next()){
                        socialWelfare.add(new StudentSocialWelfareModel(resultSet.getString("id"),resultSet.getString("sw_id"), resultSet.getString("studentID"),resultSet.getString("fullName"),
                                resultSet.getString("sw_aid_id"), resultSet.getString("gender")));
                    }
                    return socialWelfare;
           }
           catch(Exception e){
               System.err.print("getSocialWelfareFor has error "+ e);
                return socialWelfare;
           }
    }
    
    public static ObservableList<StudentSpecialNeedsModel> getSpecialNeedsFor(String studentID){
        
        ObservableList<StudentSpecialNeedsModel> specialNeed= FXCollections.observableArrayList();
        
           try{
                    String query="SELECT CONCAT_WS(' ', `firstName`, `lastName`) AS `fullName`,`student_sn`.`id`,`name`, `sn_id`,`gender`,`student_sn`.`studentID`, `student_sn`.`description`, `solution`"
                            + " FROM `student_sn`,`special_needs`,`student`"
                            + " WHERE `student`.`studentID`=`student_sn`.`studentID` AND `sn_id`=`special_needs`.`id` "
                            + "AND `student_sn`.`studentID`='"+studentID+"'";
                 
                    ResultSet resultSet= STATEMENT.executeQuery(query);

                    while(resultSet.next()){
                        specialNeed.add(new StudentSpecialNeedsModel(resultSet.getString("id"),resultSet.getString("sn_id"), resultSet.getString("studentID"),resultSet.getString("fullName"),
                                resultSet.getString("description"),resultSet.getString("solution"), resultSet.getString("gender")));
                    }
                    return specialNeed;
           }
           catch(Exception e){
               System.out.print(e);
                return specialNeed;
           }
    }
     
    public static boolean removeStudentFromBaseClass(String id){
        
        try{
            return STATEMENT.executeUpdate("DELETE FROM `student_base_class` WHERE `studentID` = '"+id+"'") > 0;
        
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public static boolean removeStudentFromVirtualClass(String id, String classID){
        
        try{
            return STATEMENT.executeUpdate("DELETE FROM `student_virtual_class` WHERE `studentID` = '"+id+"' AND `classID` = '"+classID+"'") > 0;
        
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    
    
    /**
     * Add students to class
     * @param addIDs
     * @param removeIDs
     * @param classID
     * @return 
     */
    public static boolean addBaseClassStudents(ObservableList<String> addIDs, ObservableList<String> removeIDs, String classID){
        
        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String std: removeIDs){
                    query = "DELETE FROM `student_base_class` WHERE `classID` = '"+classID+"' AND `studentID` = '"+std+"'";
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeBatch(); 
            }
            
            if(!addIDs.isEmpty()){
                for(String std: addIDs){
                    query = "INSERT INTO `student_base_class` (`id`, `studentID`, `classID`)"
                                + " VALUES ('0', '"+std+"', '"+classID+"')";
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
    
    
    
    
    
    
    //-------------------
    public static ObservableList<String> getVirtualClassAllocatedStudents(String batchID){
        
        ObservableList<String> studentIDs = FXCollections.observableArrayList();
        
        try{
            String query = " SELECT `student`.`studentID` FROM `student_virtual_class`,`student` "
                    + " WHERE `student`.`studentID`=`student_virtual_class`.`studentID` "
                    + " AND `batchID` = '"+batchID+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            while(result.next()){
                studentIDs.add(result.getString("studentID"));
            }
            return studentIDs;
        } 
        catch(Exception ex){
            System.out.println(ex.getMessage());
            return studentIDs;
        }
    }
    
    
//    public static boolean removeStudentFromVirtualClass(String id){
//        
//        try{
//            return STATEMENT.executeUpdate("DELETE FROM `student_virtual_class` WHERE `studentID` = '"+id+"'") > 0;
//        
//        }catch(Exception ex){
//            System.out.println(ex.getMessage());
//            return false;
//        }
//    }
    
    
    /**
     * Add students to class
     * @param addIDs
     * @param removeIDs
     * @param classID
     * @return 
     */
    public static boolean addVirtualClassStudents(ObservableList<String> addIDs, ObservableList<String> removeIDs, String classID){
        
        try{
            String query = "";
            if(!removeIDs.isEmpty()){
                for(String std: removeIDs){
                    query = "DELETE FROM `student_virtual_class` WHERE `classID` = '"+classID+"' AND `studentID` = '"+std+"'";
                    STATEMENT.addBatch(query);
                }
                STATEMENT.executeUpdate(query); 
            }
            
            if(!addIDs.isEmpty()){
                for(String std: addIDs){
                    query = " INSERT INTO `student_virtual_class` (`id`, `studentID`, `classID`)"
                          + " VALUES ('0', '"+std+"', '"+classID+"')";
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
    
    public static boolean updateClassRegister(ObservableList<String> studentID, String termID,String date,String classID){
    
        try{
            if(!studentID.isEmpty()){
                for(String str : studentID){
                    String query ="INSERT INTO `base_classRegister`(`id`, `studentID`, `date`, `termID`, `classId`) "
                            + "VALUES (0,'"+str+"','"+date+"','"+termID+"','"+classID+"')";
                    STATEMENT.addBatch(query);
                }
                    STATEMENT.executeBatch();
            }       
             return true;
        }
        catch(Exception e){
                System.err.println(e);
                return false;
         }
    }
     
    
}
