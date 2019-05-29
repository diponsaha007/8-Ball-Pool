package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileScene implements Initializable {
    @FXML
    Circle circle;
    @FXML
    Pane parent;
    @FXML
    Label won;
    @FXML
    Label percentage;
    @FXML
    Label rating;
    @FXML
    Label played;
    @FXML
    Label username;
    @FXML
    Label balance;
    @FXML
    Button back;

    static String name = "Username";
    static String id = "100006483736141";
    static String gamesplayed = "12";
    static String gameswon = "8";
    static String money = "0";
    double x = 0, y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDragable ();
        username.setText (name);
        circle.setStroke (Color.SEAGREEN);
        Image image;
        image = new Image ("https://graph.facebook.com/" + id + "/picture?type=large&width=300&height=300");
        if (image.isError ()) {
            image = new Image ("sample/Default Profile Pictures/images.jpg");
        }
        circle.setFill (new ImagePattern (image));

        back.setOnAction (e -> {
            Main.window.setScene (Main.menu);
        });
        played.setText (gamesplayed);
        won.setText (gameswon);
        double value;
        if (gamesplayed.compareTo ("0") != 0)
            value = Double.parseDouble (gameswon) * 100 / Double.parseDouble (gamesplayed);
        else value = 0;
        percentage.setText (String.valueOf (value) + "%");
        balance.setText (money + " $");
        setRating (value);
    }

    private void makeDragable() {
        parent.setOnMousePressed (((event) -> {
            x = event.getSceneX ();
            y = event.getSceneY ();
        }));

        parent.setOnMouseDragged (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setX (event.getScreenX () - x);
            stage.setY (event.getScreenY () - y);
            stage.setOpacity (0.8f);
        }));

        parent.setOnDragDone (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setOpacity (1.0f);
        }));

        parent.setOnMouseReleased (((event) -> {
            Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
            stage.setOpacity (1.0f);
        }));
    }

    private void setRating(double p) {
        if (p == 0) {
            rating.setText ("Newbie");
        }
        else if (p <= 30) {
            rating.setText ("Pupil");
        }
        else if (p <= 50) {
            rating.setText ("Specialist");
        }
        else if (p <= 70) {
            rating.setText ("Master");
        }
        else {
            rating.setText ("Grand Master");
        }
    }
}
