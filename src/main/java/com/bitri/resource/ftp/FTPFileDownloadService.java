package com.bitri.resource.ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class FTPFileDownloadService extends Service<Integer> {
    
    public static String server = "";
    public static int port = 21;
    public static String user = "";
    public static String pass = "";
 
    public static FTPClient ftpClient;
    public static boolean success;
    public static File downloadedFile  = null;
        
    public static String remoteDirectory, remoteFile;
    Stage stage;
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public FTPFileDownloadService(Stage parent, String remoteD, String remoteF){
        
        stage = parent;
        DataSource ftpc = DataSource.deserialiseObject();
        
        server = ftpc.getFTPHost();
        user   = ftpc.getFTPUserName();
        pass   = ftpc.getFTPPassword();
        
        remoteDirectory = remoteD;
        remoteFile = remoteF;

        System.out.println(server+"\n"+user+"\n"+pass);

        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.err)));

        success = false;
        //downloading.visibleProperty().bind(runningProperty());
    }

    public File getDownloadedFile(){return downloadedFile;}
    
    @Override
    protected Task createTask() {
        return new FTPTask();
    }
            
            
    public class FTPTask extends Task<Integer> {
        
        @Override 
        @SuppressWarnings("ResultOfObjectAllocationIgnored")
        protected Integer call() throws Exception {
            
            //new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        //Footer.downloadStatusText.setText("Establishing FTP Connection with "+server);
                    });
                    System.out.println("Establishing FTP Connection with "+server);
                    
                    //Thread.sleep(5000);
                    
                    ftpClient.connect(server, port);
                    ftpClient.login(user, pass);
                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    
                    Platform.runLater(() -> {
                        //Footer.downloadStatusText.setText("Downloading file - "+remoteFile);
                    });
                    System.out.println("Downloading file - "+remoteFile);
                    
                    //Thread.sleep(5000);
                    
                    File temp = new File("temp/");
                    FileUtils.forceMkdir(temp);

                    downloadedFile = new File( "temp/"+ remoteFile);

                    OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadedFile));

                    //retrive the file from a remote directory
                    success = ftpClient.retrieveFile(remoteDirectory+"/"+remoteFile, outputStream1);
                    if(success){
                        Platform.runLater(() -> {
                            //new DialogUI(stage, "File Download", "File downloaded successfully...("+downloadedFile+")", true, DialogUI.SUCCESS);
                        });
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
//                        Platform.runLater(() -> {
//                            new DialogUI(null, "File Download", "The system failed to download file ("
//                                    +remoteFile +") from FTP Server at "+server, false, DialogUI.ERROR);
//                        });
                        
                    }
                }catch (IOException f) {
                    Platform.runLater(() -> {
                        //new DialogUI(stage, "InterruptedException | IOException", f.getMessage(), false, DialogUI.ERROR);
                    });
                }
            
            //}).start();
            return 1;
        }
    }
}