package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
    
    private ServerSocket serverSocket;
    private boolean isRunning;
    private InetAddress serverIP;
    private int serverPort;
    final int bufferSize = 2000;

    public Server(InetAddress serverIP, int serverPort) throws IOException {
        this.isRunning = true;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.serverSocket = new ServerSocket(this.serverPort);
    }

    @Override
    public void run() {
        System.out.println("Thread running on Server");
        this.isRunning = true;
        
        while(this.isRunning){
            try 
                try (Socket clientSocket = serverSocket.accept()) {
                
                InputStreamReader isr =  new InputStreamReader(clientSocket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                String [] parts = line.split(" ");
                
                // Affichage de la requête
                while (!line.isEmpty()) {
                    line = br.readLine();
                    System.out.println(line);
                }
                
                // Réponse du serveur
                if(parts[0].equals("GET")){
                    System.out.println("Requête GET");

                    String message = "Réponse du serveur";
                    String httpHead = "HTTP/1.1 200 OK\r\n\r\n" + message;
                    clientSocket.getOutputStream().write(httpHead.getBytes("UTF-8"));
                }
                
            }

            } catch (Exception e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    public void stop(){
        this.isRunning = false;
    }
    
}
