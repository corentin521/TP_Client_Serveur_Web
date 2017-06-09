package http.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainClient extends Application {
    
    public static void main(String[] args) throws IOException{
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        // Header of the page
        HBox header = new HBox(); 
        header.setPadding(new Insets(10, 10, 10, 10));
        
        TextField adressField = new TextField();
        adressField.setText("http://127.0.0.1:4444/test.txt");
        adressField.setPromptText("Saisir une adresse");
        adressField.setPrefWidth(400);
        
        ImageView loadAdressButtonIcon = new ImageView(new Image("file:refreshIcon.png"));
        loadAdressButtonIcon.setFitHeight(12);
        loadAdressButtonIcon.setFitWidth(12);
        Button loadAdressButton = new Button();
        loadAdressButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        loadAdressButton.setGraphic(loadAdressButtonIcon);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher");
        searchField.setPrefWidth(200);
        
        ImageView performSearchButtonIcon = new ImageView(new Image("file:performSearch.png"));
        performSearchButtonIcon.setFitHeight(15);
        performSearchButtonIcon.setFitWidth(12);
        Button performSearchButton = new Button();
        performSearchButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        performSearchButton.setGraphic(performSearchButtonIcon);
        
        HBox.setMargin(loadAdressButton, new Insets(0,10,0,0));
        
        header.getChildren().add(adressField); 
        header.getChildren().add(loadAdressButton); 
        header.getChildren().add(searchField); 
        header.getChildren().add(performSearchButton); 
        header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
        header.setMinHeight(50); 
        
        // Browser
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("http://www.google.fr");
        browser.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        
        BorderPane root = new BorderPane(); 
        root.setTop(header); 
        root.setCenter(browser); 
        
        Scene scene = new Scene(root, 900, 800);
        root.requestFocus();
        primaryStage.setTitle("Navigateur Web");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Client c = new Client();
        Thread threadClient = new Thread(c);
        threadClient.start();
        
        loadAdressButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    String s = adressField.getText();
                    if(s.contains("http://"))
                        s = s.replace("http://", "");
                    if(s.contains("https://"))
                        s = s.replace("https://", "");
                    System.out.println(InetAddress.getLocalHost().getHostAddress());
                    if (s.contains(InetAddress.getLocalHost().getHostAddress())) {
                        c.setRequete(adressField.getText());
                    }
                    webEngine.load(adressField.getText());
                } catch (IOException ex) {
                    Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        performSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(searchField.getText() != null){
                    String url = "https://www.google.fr/search?q=" + searchField.getText();
                    webEngine.load(url);
                    adressField.setText(url);
                }
            }
        });
        
        adressField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER && !adressField.getText().equals(""))  {
                    loadAdressButton.fire();
                }
            }
        });
        
        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER && !searchField.getText().equals(""))  {
                    performSearchButton.fire();
                }
            }
        });
    }
    
}