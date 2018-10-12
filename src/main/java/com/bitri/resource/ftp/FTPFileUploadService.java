package com.bitri.resource.ftp;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;



public class FTPFileUploadService extends Service<Integer> {
    
    FTPClient ftp = null;
    public String host = "";
    public String user = "";
    public String passwd = "";
    File localFilePath;
    String remoteFileName;
    String remoteDirectory;
    
    public FTPFileUploadService(){
        DataSource ftpc = DataSource.deserialiseObject();
        host = ftpc.getFTPHost();
        user = ftpc.getFTPUserName();
        passwd = ftpc.getFTPPassword();
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        
        start();
    }

    /**
     * 
     */
    public void ftpClientConnect() {
        try {
            ftp.connect(host);
            int reply = ftp.getReplyCode();
            
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!FTP Connection Failed!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ftp.login(user, passwd);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    //-- Set remote diretory and file to download
    public void setRemoteDirectory(String rmdir){ remoteDirectory = rmdir; }
    public void setRemoteFile(String rmfile){ remoteFileName = rmfile; }
    public void setFileToUpload(File fileToUpload){localFilePath = fileToUpload;}
    
    @Override
    protected Task createTask() {
        return new FTPTask();
    }
            
            
    public class FTPTask extends Task<Integer> {
        
        @Override 
        protected Integer call() throws Exception {
            try{
                if(localFilePath != null){
                    InputStream input = new FileInputStream(localFilePath);

                    FTPCreateDir cre_dir = new FTPCreateDir();

                    cre_dir.createDir(remoteDirectory);

                    ftp.storeFile(remoteDirectory+"/"+remoteFileName, input);
                    
                    if (ftp.isConnected()) {
                        try {
                            ftp.logout();
                            ftp.disconnect();
                        }catch (IOException f) {
                            System.out.println(f.getMessage());
                        }
                    }
                    
                    Platform.runLater(() -> {
//                        new UploaderNotifier("File Uploaded", "File has been uploaded successfully",
//                                UploaderNotifier.NOTIFICATION);
                    });

                }
            }catch (IOException f) {
                Platform.runLater(() -> {
//                        new UploaderNotifier("File Uploaded", "File upload has failed...",
//                                UploaderNotifier.ERROR);
                });
            }
            return 1;
        }
    }
    
    /**
     * 
     * @param file
     * @return 
     */
    public static String getFileExtension(File file){
        String ext = "";
        if(file != null){
            int i = file.getName().lastIndexOf('.');
            if(i > 0){ ext = file.getName().substring(i+1);}
        }
        return ext;
    }
}