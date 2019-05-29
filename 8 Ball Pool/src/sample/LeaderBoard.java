package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderBoard implements Initializable {
    @FXML
    Pane parent;
    @FXML
    Button back;
    @FXML
    TableView<User> table;
    @FXML
    TableColumn<User, String> name;
    @FXML
    TableColumn<User, Integer> gamesplayed;
    @FXML
    TableColumn<User, Integer> gameswon;
    @FXML
    TableColumn<User, Double> winpercentage;
    @FXML
    TableColumn<User, Integer> balance;
    double x = 0, y = 0;

    static ObservableList<User> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeDragable ();
        back.setOnAction (e -> {
            Main.window.setScene (Main.menu);
        });
        name.setCellValueFactory (new PropertyValueFactory<> ("name"));
        gamesplayed.setCellValueFactory (new PropertyValueFactory<> ("gamesplayed"));
        gameswon.setCellValueFactory (new PropertyValueFactory<> ("gameswon"));
        winpercentage.setCellValueFactory (new PropertyValueFactory<> ("winpercentage"));
        balance.setCellValueFactory (new PropertyValueFactory<> ("balance"));
        table.setItems (users);
        table.getSortOrder ().add (gameswon);
        table.sort ();
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
