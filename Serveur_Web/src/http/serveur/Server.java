package http.serveur;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
            try{
                try (Socket clientSocket = serverSocket.accept()) {
                
                    DataInputStream inFromClient = null;
                    inFromClient = new DataInputStream(clientSocket.getInputStream());

                    DataOutputStream outToClient = null;
                    outToClient = new DataOutputStream(clientSocket.getOutputStream());
                    
                    InputStreamReader isr =  new InputStreamReader(clientSocket.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String line = br.readLine();
                    String [] parts = line.split(" ");

                    System.out.println(line);
                    // Affichage de la requête
                    while (!line.isEmpty()) {
                        line = br.readLine();
                        //System.out.println(line);
                    }

                    // Réponse du serveur
                    if(parts[0].equals("GET")){
                        System.out.println("Requête GET");
                        
                        File f = new File(parts[1]);

                        //File f = new File(parts[1].replace('/', '\\'));
                        
                        System.out.println(parts[1].replace("/", ""));
                        if (f!=null){
                            //FileOutputStream file = new FileOutputStream(f);
                            String message;
                            String getResp = "reponse";
                            String httpHead; 
                            
                            
                            int size = (int) f.length();
                            byte[] bufferEnvoi = new byte[size];
                            try (FileInputStream inputStream = new FileInputStream(parts[1].replace("/", ""))) {
                                message = "good";
                                httpHead = "HTTP/1.1 200 OK\r\n\r\n" + message;
                                /////// LECTURE DU FICHIER ///////
                                inputStream.read(bufferEnvoi);
                                System.out.println("1");
                            }catch(FileNotFoundException e){
                                System.out.println("404");
                                message = "404\n";
                                httpHead = "HTTP/1.1 404 \r\n\r\n" + message;
                            }
                            
                            clientSocket.getOutputStream().write(httpHead.getBytes("UTF-8"));
                            
                            outToClient.writeBytes(getResp + "\n\n");
                            outToClient.flush();
                            
                            
                            outToClient.write(bufferEnvoi);
                            outToClient.close();
                        }
                        else{
                            String message = "Réponse du serveur : Le fichier "+parts[1]+" n'existe pas";
                            String httpHead = "HTTP/1.1 200 OK\r\n\r\n" + message;
                            clientSocket.getOutputStream().write(httpHead.getBytes("UTF-8"));
                        }
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
