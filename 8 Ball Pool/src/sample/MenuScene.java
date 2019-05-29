package sample;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Optional;

public class MenuScene {
    private Group group;
    private Stage window;
    private Scene credits, current, rules;
    private ImageView imageView;
    private Button gameButton, exitbutton, creditsbutton, rulesbutton, profileButton, leaderboardButton, logout;
    private double x=0, y=0;

    public MenuScene(Group group, Stage window, Scene current, Scene credits, Scene rules)  {
        this.group = group;
        makeDragable ();
        this.window = window;
        this.credits = credits;
        this.current = current;
        this.rules = rules;
        imageView = new ImageView ();
        imageView.setImage (new Image ("sample/Cover Pics/Main-Game.jpg"));
        gameButton = new Button ("New Game");
        creditsbutton = new Button ("Credits");
        exitbutton = new Button ("Exit");
        rulesbutton = new Button ("Learn Rules");
        profileButton = new Button ("User Profile");
        leaderboardButton = new Button ("Leader Board");
        logout = new Button ("Logout");
        current.getStylesheets ().add ("Viper.css");
        gameButton.getStyleClass ().add ("button-orange2");
        exitbutton.getStyleClass ().add ("button-orange2");
        creditsbutton.getStyleClass ().add ("button-orange2");
        rulesbutton.getStyleClass ().add ("button-orange2");
        profileButton.getStyleClass ().add ("button-orange2");
        leaderboardButton.getStyleClass ().add ("button-orange2");
        logout.getStyleClass ().add ("button-orange2");
        gameButton.setLayoutX (100);
        gameButton.setLayoutY (250 - 75);
        leaderboardButton.setLayoutX (100);
        leaderboardButton.setLayoutY (250);
        profileButton.setLayoutX (100);
        profileButton.setLayoutY (250 + 75);
        rulesbutton.setLayoutX (100);
        rulesbutton.setLayoutY (250 + 75 + 75);
        creditsbutton.setLayoutX (100);
        creditsbutton.setLayoutY (250 + 75 + 75 + 75);
        exitbutton.setLayoutX (100);
        exitbutton.setLayoutY (250 + 75 + 75 + 75 + 75 + 75);
        logout.setLayoutX (100);
        logout.setLayoutY (250 + 75 + 75 + 75 + 75);
        group.getChildren ().addAll (imageView, gameButton, creditsbutton, exitbutton, rulesbutton, profileButton, leaderboardButton, logout);
        gameButton.setOnAction (e -> {
            Main.outToServer.println ("active#" + GameScene.getPlayer1 ().getName ());
        });
        rulesbutton.setOnAction (e -> {
            window.setScene (rules);
        });
        creditsbutton.setOnAction (e -> {
            window.setScene (credits);
        });
        exitbutton.setOnAction (e -> {
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle ("Exit?");
            alert.setHeaderText ("Confirmation Dialog");
            alert.setContentText ("Sure you want to exit the game?");
            Optional<ButtonType> result = alert.showAndWait ();
            if (result.get () == ButtonType.OK) {
                Main.outToServer.println ("logout");
                window.close ();
                try {
                    Main.outToServer.close ();
                    Main.inFromServer.close ();
                    Main.clientSocket.close ();
                } catch (Exception e1) {

                }
                System.exit (0);
            }
        });
        profileButton.setOnAction (e -> {
            Main.outToServer.println ("profile#" + GameScene.getPlayer1 ().getName ());
        });
        leaderboardButton.setOnAction (e -> {
            Main.outToServer.println ("leaderboard");
        });
        logout.setOnAction (event -> {
            Main.outToServer.println ("logout");
            window.setScene (Main.login);
        });
    }
    private void makeDragable() {
        group.setOnMousePressed (((event) -> {
            x = event.getSceneX ();
            y = event.getSceneY ();
        }));

        group.setOnMouseDragged (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setX (event.getScreenX () - x);
            stage.setY (event.getScreenY () - y);
            stage.setOpacity (0.8f);
        }));

        group.setOnDragDone (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setOpacity (1.0f);
        }));

        group.setOnMouseReleased (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setOpacity (1.0f);
        }));
    }
}
