package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class Main extends Application {
    static Scene menu, game, credits, rules, login, register, profile, leaderBoard, online;
    static Stage window;
    static Socket clientSocket;
    static PrintWriter outToServer;
    static BufferedReader inFromServer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        primaryStage.setTitle ("8 Ball Pool");
        primaryStage.show ();

        //Implementing client Socket
        try {
            clientSocket = new Socket ("localhost", 5123);
            outToServer = new PrintWriter (clientSocket.getOutputStream (), true);
            inFromServer = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
        } catch (Exception e) {

        }

        //This is GamePlay scene
        Group gameGroup = new Group ();
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("sample.fxml"));
        Parent root5 = loader.load ();
        Controller controller = loader.getController ();
        game = new Scene (gameGroup, 1100, 700, Color.rgb (51, 102, 153));
        //This is Menu Scene
        Group menuGroup = new Group ();
        menu = new Scene (menuGroup, 1100, 700);
        //This is rules Scene
        Parent root3 = FXMLLoader.load (getClass ().getResource ("RuleScene.fxml"));
        rules = new Scene (root3);
        //This is credits scene
        Group creditsGroup = new Group ();
        credits = new Scene (creditsGroup, 1100, 700);
        //This is login scene
        Parent root = FXMLLoader.load (getClass ().getResource ("LoginScene.fxml"));
        login = new Scene (root);
        //This is Register Scene
        Parent root1 = FXMLLoader.load (getClass ().getResource ("RegisterScene.fxml"));
        register = new Scene (root1);
        //this is active player list scene


        GameScene gameScene = new GameScene (gameGroup, game, menu, root5, window, outToServer, inFromServer, controller);
        gameScene.startGame ();
        MenuScene menuScene = new MenuScene (menuGroup, primaryStage, menu, credits, rules);
        CreditsScene creditsScene = new CreditsScene (creditsGroup, credits, menu, primaryStage);


        ClientThread clientThread = new ClientThread (outToServer, inFromServer, controller);
        Thread t = new Thread (clientThread);
        t.start ();
        customizeWindow ();

    }

    private void customizeWindow() {
        window.setScene (login);
        window.setResizable (false);
        window.setOnCloseRequest (e -> {
            e.consume ();
            if (window.getScene () == game) {
                Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
                alert.setTitle ("Exit?");
                alert.setHeaderText ("Confirmation Dialog");
                alert.setContentText ("You will lose the game if you leave!!");
                Optional<ButtonType> result = alert.showAndWait ();
                if (result.get () == ButtonType.OK) {
                    GameScene.getPlayer2 ().setWin (true);
                    GameScene.getPlayer1 ().setWin (false);
                    GameScene.setGameOver (true);
                    outToServer.println ("Lt");
                    outToServer.println ("logout");
                    window.close ();
                    try {
                        outToServer.close ();
                        inFromServer.close ();
                        clientSocket.close ();
                    } catch (Exception e1) {

                    }
                    System.exit (0);
                }
            }
            else {
                Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
                alert.setTitle ("Exit?");
                alert.setHeaderText ("Confirmation Dialog");
                alert.setContentText ("Sure you want to exit the game?");
                Optional<ButtonType> result = alert.showAndWait ();
                if (result.get () == ButtonType.OK) {
                    if (window.getScene () != login)
                        outToServer.println ("logout");
                    window.close ();
                    try {
                        outToServer.close ();
                        inFromServer.close ();
                        clientSocket.close ();
                    } catch (Exception e1) {

                    }

                    System.exit (0);
                }
            }
        });
        window.getIcons ().add (new Image ("sample/Icons/ls.jpg"));
    }


    public static void main(String[] args) {
        launch (args);
    }
}
