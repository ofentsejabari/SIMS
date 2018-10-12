/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitri.resource.dao;
/**
 *
 * @author tbontsokwane
 */

import com.bitri.service.financemanagement.AddFee;
import com.bitri.service.financemanagement.Fee;
import com.bitri.service.financemanagement.Transaction;
import com.bitri.service.financemanagement.TransactionsUI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.bitri.resource.dao.MySQLHander.CONNECTION;
import static com.bitri.resource.dao.MySQLHander.STATEMENT;

public class FeesQuery {
    
  
   public static void AddFee(AddFee addit)
    {
        //setupfeesCollectionTable();
            String ID = addit.getStudentId();
            String FEETYPE = addit.getFeeType();
            String AMOUNTPAID =  addit.getAmountPaid();
            String PAIDBY = addit.getPayedBy();
            String PAYMENTMODE = addit.getPaymentMode();
            String RECIEVEDBY = addit.getRecievedBy();
            String CAPTUREDDATE = addit.getCapturedDate();
            String TERMID = addit.getTermId();
            String BALANCE = addit.getBalance();
            
            //java.util.Date today = new java.util.Date();

           String sq = "INSERT INTO FEES_COLLECTED VALUES ("+
                  
                   "'"+ID+"',"+
                   "'"+FEETYPE+"',"+
                   "'"+AMOUNTPAID+"',"+
                   "'"+PAIDBY+"',"+
                   "'"+PAYMENTMODE+"',"+
                   "'"+RECIEVEDBY+"',"+
                   "'"+CAPTUREDDATE+"',"+
                   "'"+TERMID+"',"+
                   "'"+BALANCE+"'"+
           ")";
           System.out.println(sq);
           try{
               STATEMENT.executeUpdate(sq);
            }
            catch(SQLException e){
             System.out.print("*....Inserting Not Successful....*" + e);
       }

    }
   
   
   public static boolean updateFee(AddFee add) {
        try {
            
            String update = "UPDATE FEES_COLLECTED SET amountpaid=?, paidby=?, paymentmode=?, recievedby=?, captureddate=?, termid=?, balance=? WHERE studentid=? AND feetype=?";
            PreparedStatement stm = CONNECTION.prepareStatement(update);
            
            
            stm.setString(1, add.getAmountPaid());
            stm.setString(2, add.getPayedBy());
            stm.setString(3, add.getPaymentMode());
            stm.setString(4, add.getRecievedBy());
            stm.setString(5, add.getCapturedDate());
            stm.setString(6, add.getTermId());
            stm.setString(7, add.getBalance());
            stm.setString(8, add.getStudentId());
            stm.setString(9, add.getFeeType());
            int res = stm.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            Logger.getLogger(FeesQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
    public static Fee getFeeById(String feeID){
        Fee fee = null;
        String query ="SELECT `id`, `name`, `description`, `paymentType`, `defaultAmount`, `academicYear` FROM `fee` WHERE `id`="+feeID;
        try{
            STATEMENT.addBatch(query);
            ResultSet result = STATEMENT.executeQuery(query);
            if(result.next()){
                 fee = new Fee(result.getString("id"), result.getString("name"),result.getString("paymentType"),result.getString("description")
                        ,result.getString("defaultAmount"),result.getString("academicYear"));
            }
            return fee;
        }
        
        catch(Exception e){
            System.err.println(e);
            return fee;
        }
    }
   
    public static boolean deleteFee(String id) {
        try {
            String query ="DELETE FROM `fee` WHERE `id`="+id;
            System.out.println(query);
            return STATEMENT.executeUpdate(query)>0;
        }
        catch(Exception e){
            System.err.println(e);
            return false;
        }
    }
   
   public static ObservableList<String> getStudentsIDList(){
       ObservableList<String> students = FXCollections.observableArrayList();
       try{
           String query = " SELECT `studentID`"
                        + " FROM `student`";
           
           ResultSet result = STATEMENT.executeQuery(query);
           
           while(result.next()){
               students.add(result.getString("studentID"));
           }
           return students;
           
       }catch(Exception ex){
           System.out.println(ex.getMessage());
           
           return students;
       }
   }
   
   
    public static boolean updateFee(Fee feemodel, boolean flag)
    {
        try{
            String sql_query = "INSERT INTO `fee` (`id`, `name`, `description`, `paymentType`, `defaultAmount`, `academicYear`)"
                       + " VALUES (0,'"+feemodel.getName()+"','"+feemodel.getDescription()+"','"+feemodel.getPaymentMode()+"'"
                    + ",'"+feemodel.getDefaultAmount()+"','"+feemodel.getAcademicYear()+"')";
            
            if(flag){
                sql_query = "UPDATE `fee` SET `name`='"+feemodel.getName()+"',`description`='"+feemodel.getDescription()+"'"
                        + ",`paymentType`='"+feemodel.getPaymentMode()+"',`defaultAmount`='"+feemodel.getDefaultAmount()+"',`academicYear`='"+feemodel.getAcademicYear()+"' "
                        + "WHERE id="+feemodel.getId();
            }
            
            
            return STATEMENT.executeUpdate(sql_query) >0;
                
        }
         catch(SQLException e){
            System.err.print("*....Inserting Not Successful....*" + e);
            return false;
         }
       
    }
    
     public String getSchoolNameByID(String schoolID){
        try{
            String query = "SELECT `name`  FROM `school` WHERE `id` = '"+schoolID+"'";
            
            ResultSet result = STATEMENT.executeQuery(query);
            
            if(result.next()){
                return (result.getString("name"));
            }
            return "";
        } 
        catch(Exception ex){
             System.out.println(ex.getMessage());
             
             return "";
        }
    }
     
     
     public static String getPaidby(String ID, String ft){
       String payer=null;
       try{
           String query = "SELECT studentid,feetype,paidby  FROM fees_collected WHERE studentid = '"+ID+"' AND feetype = '"+ft+"'";
           
           ResultSet result = STATEMENT.executeQuery(query);
           
           while(result.next()){
               payer = result.getString("paidby");
           }
           return payer;
           
       }catch(Exception ex){
           System.out.println(ex.getMessage());
           
          
       }
       return payer;
   }
   
  
      public static String getPaymentType(String ID, String ft){
       String payermode =null;
       try{
           String query = "SELECT studentid,feetype,paymentmode  FROM fees_collected WHERE studentid = '"+ID+"' AND feetype = '"+ft+"'";
           
           ResultSet result = STATEMENT.executeQuery(query);
           
           while(result.next()){
               payermode = result.getString("paymentmode");
           }
           return payermode;
           
       }catch(Exception ex){
           System.out.println(ex.getMessage());
           
          
       }
       return payermode;
   }
      
    
    public static ObservableList<Transaction> getAllTransactions() {
        ObservableList<Transaction> list = FXCollections.observableArrayList();

       
         String query = "SELECT `id`, `studentID`, `feeID`, `receivedBy`, `amountPaid`, `captureDate`"
                    + " FROM `student_fee_payment` WHERE `1";
        try {
            ResultSet result = STATEMENT.executeQuery(query);
            
            while (result.next()) {
                list.add(new Transaction(result.getString("id"),result.getString("studentID"),result.getString("feeID"),result.getString("amountPaid"),result.getString("captureDate"),result.getString("receivedBy")));
            }
            
            return list;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage()+ "Trrrr");
            return list;
        }
    }
    
    public static ObservableList<Transaction> getTransactionByFeeID(String feeID) {
        ObservableList<Transaction> list = FXCollections.observableArrayList();

       
        String query = "SELECT `id`, `studentID`, `feeID`, `receivedBy`, `amountPaid`, `captureDate`"
                    + " FROM `student_fee_payment` WHERE `feeID`='"+feeID+"'";
        
        
        try {
            ResultSet result = STATEMENT.executeQuery(query);
            
            while (result.next()) {
                list.add(new Transaction(result.getString("id"),result.getString("studentID"),result.getString("feeID"),result.getString("amountPaid"),result.getString("captureDate"),result.getString("receivedBy")));
            }
            
            return list;
        } 
        catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return list;
        }
    }
    
     public static boolean deleteTransaction() {
        //Fetch the selected row
        Transaction selectedToDelete;
       selectedToDelete = (TransactionsUI.table).getTableView().getSelectionModel().getSelectedItem();
        if (selectedToDelete == null) {
            
            System.out.print(" No coumn to delete");
            return false;
        }
         try {
            
            String update = "DELETE FROM FEES_COLLECTED WHERE studentid=? AND feetype=?";
            PreparedStatement stm = CONNECTION.prepareStatement(update);
            
            stm.setString(1, selectedToDelete.getStudentId());
            //stm.setString(2, selectedToDelete.getFeeType());
            int res = stm.executeUpdate();
            
            if(update.equals(true)){ return true;}
           
        } catch (SQLException ex) {
            Logger.getLogger(FeesQuery.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
         return true;
    }
     
     
     
    public static ObservableList<Fee> getAllFees(String status) {
        ObservableList<Fee> list = FXCollections.observableArrayList();
        
        String query = "";
        if(status.equals("active")){
            query = "SELECT * FROM fee WHERE `status`='active'";
        }
        else if(status.equals("inactive")){
            query = "SELECT * FROM fee WHERE `status`!='active'";
        }
        else{
            query = "SELECT * FROM fee WHERE 1";
        }
        
        try {
            ResultSet result = STATEMENT.executeQuery(query);
      
            while (result.next()) {
              
                list.add(new Fee(result.getString("id"),result.getString("name"),result.getString("paymentType"),result.getString("description"),result.getString("defaultAmount"), result.getString("academicYear")));

            }
            return list;
        } catch (SQLException ex) {
            System.err.print("Error :"+ex);
            return list;
        }
    }
        
    public static ObservableList<Fee> getFeesByAcademicYear(String year) {
        ObservableList<Fee> list = FXCollections.observableArrayList();
        String query = "SELECT `id`, `name`, `description`, `paymentType`, `defaultAmount`, `academicYear` FROM  fee WHERE `academicYear`='"+year+"'";
        
        try {
            ResultSet result = STATEMENT.executeQuery(query);
      
            while (result.next()) {
              
                list.add(new Fee(result.getString("id"),result.getString("name"),result.getString("paymentType"),
                        result.getString("description"),result.getString("defaultAmount"), result.getString("academicYear")));

            }
            return list;
        } catch (SQLException ex) {
            System.err.print("Error :"+ex);
            return list;
        }
    }
    
     public static Fee getFeeByName(String name) {
        Fee list = null;
        String query = "SELECT `id`, `name`, `description`, `paymentType`, `defaultAmount`, `academicYear` FROM  fee WHERE `name`='"+name+"'";
        
        try {
            ResultSet result = STATEMENT.executeQuery(query);
      
            while (result.next()) {
              
                return new Fee(result.getString("id"),result.getString("name"),result.getString("paymentType"),
                        result.getString("description"),result.getString("defaultAmount"), result.getString("academicYear"));

            }
            return list;
        } catch (SQLException ex) {
            System.err.print("Error :"+ex);
            return list;
        }
    }
      
}

