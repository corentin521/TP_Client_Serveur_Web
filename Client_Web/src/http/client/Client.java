package http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable  {

    Socket socketClient;
    private boolean isRunning;
    InetAddress ip;
    int port;
    String requete;
    
    public Client(String requete) throws IOException
    {
        ip = InetAddress.getByName("127.0.0.1");
        port = 1026;
        socketClient = new Socket(ip,port);
        this.requete = requete;
    }
    
    @Override
    public void run() {
        System.out.println("Thread running on Server");
        this.isRunning = true;
        
        while(this.isRunning){
            
        }
    }
    
    public void stop(){
        this.isRunning = false;
    }
    
    
    
}
