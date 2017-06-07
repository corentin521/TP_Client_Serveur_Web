package http.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    Socket socketClient;
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
    
    private void run()
    {
        
    }
    
    
}
