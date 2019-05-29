package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML
    AnchorPane parent;
    @FXML
    Button login;
    @FXML
    Button register;
    @FXML
    TextField txtfield;
    @FXML
    PasswordField passfield;
    double x = 0, y = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDragable ();
        login.setOnAction (e -> {
            Main.outToServer.println ("login#" + txtfield.getText () + "#" + passfield.getText ());
        });
        register.setOnAction (event -> {
            Main.window.setScene (Main.register);
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

