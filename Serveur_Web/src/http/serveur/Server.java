package http.serveur;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Path;

public class Server extends Observable implements Runnable  {
    
    private ServerSocket serverSocket;
    private boolean isRunning;
    private InetAddress serverIP;
    private int serverPort;
    final int bufferSize = 2000;
    private String log;
    private String reponse;

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
                    this.log = line;
                    setChanged();
                    notifyObservers(this.log);
                    
                    // Réponse du serveur
                    if(parts[0].equals("GET")){
                        File f = new File(parts[1].split("/")[1]);
                        
                        if (f!=null){
                            
                            String message;
                            String getResp = "reponse";
                            String httpHead; 
                            String contentType;
                            String contentLength;
                            
                            int size = (int) f.length();
                            System.out.println("lu: " + size);
                            byte[] bufferEnvoi = new byte[size];
                            try (FileInputStream inputStream = new FileInputStream(parts[1].split("/")[1])) {
                                
                                contentType = "Content-Type: " + Files.probeContentType(Paths.get(parts[1].split("/")[1]));
                                
                                contentLength = "Content-Length: " + Integer.toString(size);
                                
                                httpHead = "HTTP/1.1 200 OK\n" + contentType + "\n" +contentLength;
                                
                                /////// LECTURE DU FICHIER ///////
                                inputStream.read(bufferEnvoi);
                            }catch(FileNotFoundException e){
                                message = "404\n";
                                httpHead = "HTTP/1.1 404 \n" + message;
                            }
                            
                            //clientSocket.getOutputStream().write(httpHead.getBytes("UTF-8"));
                            System.out.println(httpHead);
                            this.reponse = httpHead;
                            setChanged();
                            notifyObservers(this.reponse);
                    
                            outToClient.writeBytes(httpHead + "\n\n");
                            outToClient.flush();
                            
                            
                            outToClient.write(bufferEnvoi);
                            outToClient.close();
                        }
                        else{
                            String message = "Réponse du serveur : Le fichier "+parts[1]+" n'existe pas";
                            String httpHead = "HTTP/1.1 404 OK\r\n\r\n" + message;
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }  
}
