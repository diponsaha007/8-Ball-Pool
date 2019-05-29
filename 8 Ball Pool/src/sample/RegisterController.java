package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    TextField username;
    @FXML
    TextField fbID;
    @FXML
    PasswordField pass;
    @FXML
    PasswordField repass;
    @FXML
    Button signup;
    @FXML
    Button back;
    @FXML
    Pane parent;

    private double x=0,y=0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDragable ();
        back.setOnAction (e->{
            Main.window.setScene (Main.login);
        });
        signup.setOnAction (e->{
            if(pass.getText ().compareTo (repass.getText ())!=0)
            {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setTitle ("Error!");
                alert.setHeaderText ("Passwords do not match!");
                alert.show ();
            }
            else {
                if(!username.getText ().isEmpty () && !pass.getText ().isEmpty () && !fbID.getText ().isEmpty ())
                {
                    Main.outToServer.println ("signup#"+username.getText ()+"#"+pass.getText ()+"#"+fbID.getText ());
                    username.setText ("");
                    pass.setText ("");
                    repass.setText ("");
                    fbID.setText ("");
                }
                else {
                    Alert alert = new Alert (Alert.AlertType.INFORMATION);
                    alert.setTitle ("Error!");
                    alert.setHeaderText ("No field can be empty");
                    alert.show ();
                }
            }
        });


    }
    private void makeDragable() {
        parent.setOnMousePressed(((event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));

        parent.setOnMouseDragged(((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(0.8f);
        }));

        parent.setOnDragDone(((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        }));

        parent.setOnMouseReleased(((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        }));
    }
}
