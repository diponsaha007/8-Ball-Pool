package sample;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CreditsScene {
    private Group group;
    private Button button;
    private ImageView imageView;
    private Scene thisScene, backScene;
    private Stage window;
    double x = 0, y = 0;

    public CreditsScene(Group group, Scene thisScene, Scene backScene, Stage window) {
        this.group = group;
        makeDragable ();
        this.thisScene = thisScene;
        this.backScene = backScene;
        this.window = window;
        imageView = new ImageView ("sample/Cover Pics/Credits.jpg");
        thisScene.getStylesheets ().add ("Viper.css");
        button = new Button ("Back");
        button.getStyleClass ().add ("button-back");
        button.setLayoutX (525);
        button.setLayoutY (640);
        group.getChildren ().addAll (imageView, button);
        button.setOnAction (e -> {
            window.setScene (backScene);
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
