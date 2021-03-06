package http.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {

    private Socket socketClient;
    private boolean isRunning;
    private InetAddress ip;
    private String requete;
    private boolean performedRequest = false;
    private String nomFichier;
    
    public Client()
    {
    }
    
    @Override
    public void run() {
        System.out.println("Thread running on Server");
        this.isRunning = true;
        while(this.isRunning){ 
            System.out.print("");
            try {
                if(performedRequest){
                    DataOutputStream outToServer = new DataOutputStream(socketClient.getOutputStream());
                    DataInputStream inFromServer = new DataInputStream(socketClient.getInputStream());
                    
                    outToServer.writeBytes(requete + "\n");
                    outToServer.flush();
                    
                    String temp;
                    String getResp = "";
                    String length = "";
                    String entete = "";
                    String typeFichier = "";
                    String contenu = "";

                    String getAuth = inFromServer.readLine();
                    String stat_line[] = getAuth.split(" ");
                    
                    //SI LA REPONSE EST OK, on lit la suite de l'header
                    if(stat_line.length > 1) {
                        if (stat_line[1].equals("200")) {
                            getResp = getAuth+"\n";

                            while (!(temp = inFromServer.readLine()).equals("")) {
                                if (temp.contains("Length"))
                                    if(temp.split(": ").length > 1)
                                        length = (temp.split(": "))[1];
                                getResp += temp + "\n";
                            }
                            entete = getResp ;

                            System.out.println("Received: \n" + getResp);

                            //CREATION DU BUFFER DE RECEP
                            byte data[] =new byte[Integer.parseInt(length)];

                            //RECEPTION DU FICHIER
                            for(int i =0; i<data.length;i++)
                                    data[i] = inFromServer.readByte();
                            inFromServer.close();

                            //CREATION DU FICHIER A LA RACINE
//                            String nomFichierSplit = "inconnu.html";
//                            if(requete.split("/").length > 2)
//                            nomFichierSplit = requete.split("/")[2].split(" ")[0];
                            File file = new File(nomFichier);
                            file.createNewFile();

                            //typeFichier = nomFichierSplit;

                            //ECRITURE DU CONTENU RECU DANS LE FICHIER
                            FileOutputStream fop = new FileOutputStream(file);
                            fop.write(data);
                            contenu = new String(data);
                            fop.close();

                            System.out.println("TRANSFERT REUSSI");
                            socketClient.close();
                            performedRequest = false;
                            
                        }else if (stat_line[1].equals("404")) {
                            
                            System.out.println("404 NOT FOUND : "+getAuth);
                            byte data[] = new byte[133];

                            for(int i=0;i<data.length;i++)
                                    data[i] = inFromServer.readByte();
                            inFromServer.close();

                            File file = new File("404.html");
                            file.createNewFile();

                            FileOutputStream fop = new FileOutputStream(file);
                            fop.write(data);
                            contenu = new String(data);
                            fop.close();
                            socketClient.close();

                        }
                    }
                } 
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
   
    
    public void setRequete(String url) throws IOException{

        if(url.contains("http://"))
            url = url.replace("http://", "");
        
        InetAddress IPAddress = InetAddress.getByName(url.split(":")[0]);
        String port = url.split(":")[1].split("/")[0];
        url = url.split("/")[1];
        requete ="GET /";
        requete+=url;
        requete+=" HTTP/1.1";
        nomFichier = url;
        
        try {
            System.out.println("OOOOOOOOO");
            System.out.println("ip: " +  url.split(":")[0] + " port: " + port);
            
            socketClient = new Socket(IPAddress, Integer.parseInt(port));
            performedRequest = true;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}