package com.bitri.resource.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import com.bitri.resource.dao.DataSource;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
 
public class FTPCreateDir {
    
    String server = "";
    int port = 21;
    String user = "";
    String pass = "";
    
    public boolean SUCCESS = false;
    private static FTPClient ftpClient;
    
    public FTPCreateDir(){
        
        DataSource ftpc = DataSource.deserialiseObject();
        
        server = ftpc.getFTPHost();
        user = ftpc.getFTPUserName();
        pass = ftpc.getFTPPassword();
        
        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {
            ftpClient.connect(server, port);
            //showServerReply(ftpClient);
            
            int replyCode = ftpClient.getReplyCode();
            
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                SUCCESS = false;
            }
            
            SUCCESS = ftpClient.login(user, pass);
            //showServerReply(ftpClient);
            if (!SUCCESS) {
                System.out.println("Could not login to the server");
            }
        }
        
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    
    }
    

    /**
     * 
     * @param dirToCreate
     * @return 
     */
    public boolean createDir(String dirToCreate ) {
        try{
            SUCCESS = ftpClient.makeDirectory(dirToCreate);
            if(SUCCESS){
                
                ftpClient.logout();
                ftpClient.disconnect();
                return true;   
            } else{
                
                ftpClient.logout();
                ftpClient.disconnect();
                return false;
            }

        }catch (IOException ex) {
            return false;
        }
    }
}
