package http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainHttp extends Application {
    
    private Thread serverThread;
    private Server httpServer;
    private TextArea log;
    private ObservableList<Node> logLinesList;
    private ScrollPane sp;
    
    public static void main(String[] args) throws IOException{
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws UnknownHostException {
        
        primaryStage.setTitle("Assistant de configuration de serveur Web HTTP 1.1.");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label serverPortLabel = new Label("Server port:");
        serverPortLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        grid.add(serverPortLabel, 0, 0);

        TextField serverPortField = new TextField();
        serverPortField.setPrefWidth(50);
        grid.add(serverPortField, 1, 0);
        
        // Port validation 
        serverPortField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Remove non numeric character
                if (!newValue.matches("\\d*")) {
                    serverPortField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                // Set the string length to 5 because the max port is 65536
                if (!newValue.equals("") && !oldValue.equals("") 
                        && Integer.valueOf(newValue) >  Integer.valueOf(oldValue) 
                        && serverPortField.getText().length() >= 5) {
                    serverPortField.setText(serverPortField.getText().substring(0, 5));
                }
            }
        });

        Button startServerButton = new Button("Démarrer");
        startServerButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        grid.add(startServerButton, 2, 0);
        
        Label IPAddresstLabel = new Label("Adresse IP:");
        IPAddresstLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        grid.add(IPAddresstLabel, 3, 0);
        
        Label IPAddress = new Label(InetAddress.getLocalHost().getHostAddress());
        IPAddress.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        IPAddress.setTextFill(Color.GREEN);
        IPAddress.setMinWidth(90);
        grid.add(IPAddress, 4, 0);
        
        Button clearLogButton = new Button("Vider le log");
        clearLogButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        grid.add(clearLogButton, 5, 0);
        
        FlowPane fp = new FlowPane();
        this.logLinesList = fp.getChildren();
        sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setContent(fp);
        grid.add(sp, 0, 2, 7, 40);
        
        writeInformationInLog("Bienvenue sur l'assistant de configuration de serveur"
                + "Web de niveau HTTP 1.1. réalisé par Hugo Chomarat et Corentin"
                + " Bect. Veuillez choisir un port valide et lancer le serveur.");
        

        startServerButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                
                try {                 
                    if(serverPortField.getText().equals(""))
                        writeErrorInLog("Le port doit être renseigné.");
                    else{
                        int port = Integer.valueOf(serverPortField.getText());
                        if(port < 1024 || port > 65536)
                        writeErrorInLog("Le port doit être compris entre 1024 et 65536.");
                        else{
                            if(startServerButton.getText().equals("Start")){
                            httpServer = new Server(InetAddress.getLocalHost(), Integer.valueOf(serverPortField.getText()));
                            serverThread = new Thread(httpServer);
                            serverThread.start();
                            startServerButton.setText("Arrêter");
                            writeSuccessInLog("Le serveur a demarré avec succès sur"
                                    + "le port " + port + " de " +  InetAddress.getLocalHost());

                            }else{
                                httpServer.stop();
                                startServerButton.setText("Start");
                                writeSuccessInLog("Le serveur a été arrêté avec succès.");
                            }
                        }
                    } 
                } catch (Exception ex) {
                    writeErrorInLog("Le port n'est pas disponible. Veuillez en choisir un autre.");
                    Logger.getLogger(MainHttp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        clearLogButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
               logLinesList.clear();
               sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
        });
        
        serverPortField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER && !serverPortField.getText().equals(""))  {
                    startServerButton.fire();
                }
            }
        });
        
        serverPortField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    startServerButton.fire();
                }
            }
        });
        
        Scene scene = new Scene(grid, 500, 450);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void writeInformationInLog(String message){
        writeInLog(message, Color.BLACK);
    }
    
    public void writeSuccessInLog(String message){
        writeInLog(message, Color.GREEN);
    }
    
    public void writeWarningInLog(String message){
        writeInLog(message, Color.ORANGE);
    }
    
    public void writeErrorInLog(String message){
        writeInLog("ERREUR: " + message, Color.FIREBRICK);
    }
    
    public void writeInLog(String message, Color color){
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime currentDate = LocalDateTime.now();
        
        String string = dtf.format(currentDate).toString() + " : " + message;
        Text finalMessage = new Text(string);
        
        finalMessage.setWrappingWidth(460);
        finalMessage.setFill(color);
        this.logLinesList.add(finalMessage);
        if(sp.getVbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER)
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    } 
}
