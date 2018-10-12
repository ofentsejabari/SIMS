package com.bitri.resource.ftp;
/**
 *
 * @author O'JAY
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter; 
import javafx.application.Platform;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
 
public class FTPUploader extends Thread{
     
    FTPClient ftp = null;
    public String host = "";
    public String user = "";
    public String passwd = "";
    File localFilePath;
    String remoteFileName;
    String remoteDirectory;
    
    public FTPUploader(File localFilePath, String remoteFileName, String remoteDirectory){
        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Start Uploading!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            
            this.localFilePath = localFilePath;
            this.remoteDirectory = remoteDirectory;
            this.remoteFileName = remoteFileName;
            
            DataSource ftpc = DataSource.deserialiseObject();
            
            host = ftpc.getFTPHost();
            user = ftpc.getFTPUserName();
            passwd = ftpc.getFTPPassword();
            
            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            
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
    
    
    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void run(){
        try{
            if(localFilePath != null){
                InputStream input = new FileInputStream(localFilePath);

                FTPCreateDir cre_dir = new FTPCreateDir();

                cre_dir.createDir(remoteDirectory);

                ftp.storeFile(remoteDirectory+"/"+remoteFileName, input);
                //com.bitri.resource.ftp.storeFile(remoteDirectory+"/"+remoteFileName+".png", input);
                disconnect();
                Platform.runLater(() -> {
//                    new DialogUI(null, "File Uploaded", "File has been successfully uploaded to file server",
//                            true, DialogUI.NOTIFICATION);
                });
                
            }
        }
        catch (IOException f) {
            Platform.runLater(() -> {
//                new DialogUI(null, "File Uploaded", "File upload has failed. Please contact your administrator for help"
//                        + " if you think this is technical issue",
//                        false, DialogUI.ERROR);
            });
       }
    }
    
    
    
    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }

    public static void main(String[] args) throws Exception {
    
    }

}