package com.bitri.resource.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPURLListing {

    public static String remoteDirectory;
    private final String server;
    private final String user;
    private final String pass;
    public static FTPClient ftpClient;
    int port = 21;
    public static boolean success = false;
    
    public FTPURLListing(String directory){
            this.remoteDirectory = directory;
            DataSource ftpc = DataSource.deserialiseObject();
        
            server = ftpc.getFTPHost();
            user   = ftpc.getFTPUserName();
            pass   = ftpc.getFTPPassword();
            
            ftpClient = new FTPClient();
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                success = true;
                
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                success = false;
            }
    }
    
    public  FTPFile[] getFTPFiles() {
        try{
           return ftpClient.listFiles(remoteDirectory);
            
        }catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}