package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    
    private ServerSocket socketServeur;
    private Socket socketClient;

    public Serveur() throws IOException
    {
        socketServeur = new ServerSocket(1026);
    }
    
}
