package http.client;

import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    public void start(Stage primaryStage) {
        
        // Header of the page
        HBox header = new HBox(); 
        header.setPadding(new Insets(10, 10, 10, 10));
        
        TextField adressField = new TextField();
        adressField.setPromptText("Saisir une adresse");
        adressField.setPrefWidth(400);
        
        //Image loadAdressButtonIcon = new Image(getClass().getResourceAsStream("../refreshIcon.png"));
        Button loadAdressButton = new Button();
        loadAdressButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        //loadAdressButton.setGraphic(new ImageView(loadAdressButtonIcon));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher");
        searchField.setPrefWidth(200);
        
        Button performSearchButton = new Button("Démarrer");
        performSearchButton.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
        
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
        
        // Bottom of the page
        Label bottomLabel = new Label("Bas"); 
        bottomLabel.setStyle("-fx-alignment: center; -fx-background-color: limegreen;"); 
        bottomLabel.setMinHeight(50); 
        bottomLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
        
        BorderPane root = new BorderPane(); 
        root.setTop(header); 
        root.setBottom(bottomLabel); 
        root.setCenter(browser); 
        
        Scene scene = new Scene(root, 900, 800);
        root.requestFocus();
        primaryStage.setTitle("Navigateur Web");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}