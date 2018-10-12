package com.bitri.resource.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.application.Platform;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
 
/**
 * Uploading a files from local computer to a remote FTP server using Apache Commons Net API.
 * @author jabari
 */
public class FTPDownloader extends Thread{
        String server = "";
        int port = 21;
        String user = "";
        String pass = "";
 
        private static FTPClient ftpClient;
        private static boolean success;
        public static File downloadedFile  = null;
        
        String remoteDirectory, remoteFile;
        
        public FTPDownloader(String dir, String filename){
            
            this.remoteDirectory = dir;
            this.remoteFile = filename;
            DataSource ftpc = DataSource.deserialiseObject();
        
            server = ftpc.getFTPHost();
            user   = ftpc.getFTPUserName();
            pass   = ftpc.getFTPPassword();
            
            System.out.println(server+"\n"+user+"\n"+pass);
            
            ftpClient = new FTPClient();
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            
            success = false;
            try {

                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            
            start();
        }
        
    
    /**
     * 
     * @param folder
     * @return 
     */
    public FTPFile[] getUploadedFiles(String folder) {
        try{
           return ftpClient.listFiles("attachments/"+folder);
            
        }catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
 
    /**
     * 
     * @param folder
     * @return 
     */
    public ArrayList<String> getFileNames(String folder) {
        try{
            ArrayList<String> filenames = new ArrayList<>();
            
            String[] files = ftpClient.listNames("/"+folder);
            if (files != null && files.length > 0) {
                for (String aFile: files) {
                    filenames.add(aFile);
                }
            }
            return filenames;
        }catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
 
    /**
     * 
     * @param filePath
     * @return 
     */
    public boolean deleteFile(String filePath) {
        try{
            return ftpClient.deleteFile(filePath);
        }catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Removes a non-empty directory by delete all its sub files and
     * sub directories recursively. And finally remove the directory.
     * @param parentDir
     * @param currentDir
     */
    public static void removeDirectory(String parentDir,String currentDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }
 
        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
 
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/" + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }
 
                if (aFile.isDirectory()) {
                    // remove the sub directory
                    removeDirectory(dirToList, currentFileName);
                } else {
                    // delete the file
                    boolean deleted = ftpClient.deleteFile(filePath);
                    if (deleted) {
                        System.out.println("DELETED the file: " + filePath);
                    } else {
                        System.out.println("CANNOT delete the file: "
                                + filePath);
                    }
                }
            }
 
            // finally, remove the directory itself
            boolean removed = ftpClient.removeDirectory(dirToList);
            if (removed) {
                System.out.println("REMOVED the directory: " + dirToList);
            } else {
                System.out.println("CANNOT remove the directory: " + dirToList);
            }
        }
    }
    
    
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
       
    
    
    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void run(){
        File temp = new File("temp/");
            try {
                FileUtils.forceMkdir(temp);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        downloadedFile = new File( "temp/"+ remoteFile);
            
        OutputStream outputStream1;
        try {
                outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadedFile));

                //retrive the file from a remote directory
                success = ftpClient.retrieveFile(remoteDirectory+"/"+remoteFile, outputStream1);
                if(success){
                    System.out.println("download successfully.");
                    if (ftpClient.isConnected()) {
                        try {
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException f) {
                        }
                    }
                    outputStream1.close();
                }else{
                    outputStream1.close();
                    downloadedFile = null;
                }
            }catch (Exception f) {
                Platform.runLater(() -> {
//                    new DialogUI(null, "File Uploaded", "File upload has failed. Please contact your administrator for help"
//                            + " if you think this is technical issue",
//                            false, DialogUI.ERROR);
                });
           }
        }

    }
