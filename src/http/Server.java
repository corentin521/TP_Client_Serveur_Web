package http;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;

public class Server implements Runnable {
    
    private ServerSocket serverSocket;
    private boolean isRunning;
    private InetAddress serverIP;
    private int serverPort;
    final int bufferSize = 2000;

    public Server(InetAddress serverIP, int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(1026);
        this.isRunning = true;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        System.out.println("Thread running on Server");
        this.isRunning = true;
        
        while(this.isRunning){
            try {
                DatagramSocket ds = new DatagramSocket(this.serverPort, this.serverIP);
                byte [] data = new byte[this.bufferSize];
                
                DatagramPacket dp = new DatagramPacket(data, data.length);
                ds.receive(dp);
                ds.close();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    public void stop(){
        this.isRunning = false;
    }
    
}
