package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RulesScene implements Initializable {
    @FXML
    Button back;
    @FXML
    Pane parent;
    double x = 0, y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDragable ();
        back.setOnAction (e -> {
            Main.window.setScene (Main.menu);
        });
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
}
