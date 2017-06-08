package http.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable  {

    private Socket socketClient;
    private boolean isRunning;
    private InetAddress ip;
    private int port;
    private String requete;
    
    public Client()
    {
    }
    
    @Override
    public void run() {
        System.out.println("Thread running on Server");
        this.isRunning = true;
        
        while(this.isRunning){
            
        }
    }
    
    public void setRequete(String url){
        String commande ="GET /";
        commande+=url;
        commande+=" HTTP/1.1";
    }
    
    public void stop(){
        this.isRunning = false;
    }
    
    public static void main(String[] args) throws IOException{
        
    }
    
}
