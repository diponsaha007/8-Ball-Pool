package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class Controller {
    @FXML
    Line line;
    @FXML
    Circle circle;
    @FXML
    Slider velocitySlider;
    @FXML
    Label velocityLabel;
    @FXML
    ImageView stick;
    @FXML
    Line dirLine1;
    @FXML
    CheckBox soundBox;

    double ang;

    private double xp = -1, yp = -1;

    @FXML
    public void initialize() {
        soundBox.selectedProperty ().addListener (new ChangeListener<Boolean> () {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if (new_val) {
                    GameScene.setTurnOffSounds (false);
                }
                else GameScene.setTurnOffSounds (true);
            }
        });
    }

    public void Moveline(MouseEvent event) {
        if (GameScene.isIsTurn () && !GameScene.isGameOver () && !GameScene.isGamePause () && GameScene.getPlayer1 ().isMyturn ()) {
            line.setVisible (true);
            circle.setVisible (true);
            line.setStroke (Color.WHITE);
            circle.setStroke (Color.WHITE);
            double x2 = event.getSceneX (), y2 = event.getSceneY ();
            double x1 = GameScene.getCueBall ().getPosition ().getX (), y1 = GameScene.getCueBall ().getPosition ().getY ();
            line.setStartX (x1);
            line.setStartY (y1);
            line.setEndX (x2);
            line.setEndY (y2);
            circle.setCenterX (x2);
            circle.setCenterY (y2);
            circle.setRadius (12.5);
            int flag = 0;
            for (int i = 0; i < 16; i++) {
                if (collides (circle, GameScene.ball[i])) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                dirLine1.setVisible (true);
            }
            else dirLine1.setVisible (false);
            for (int i = 1; i < 16; i++) {
                if (collides (circle, GameScene.ball[i])) {
                    double cueBallVelocity = 40;
                    double angle = Math.atan ((y2 - y1) / (x2 - x1));
                    if (x2 < x1) cueBallVelocity = -cueBallVelocity;
                    Vector position = new Vector (circle.getCenterX (), circle.getCenterY ());
                    Vector velocity = new Vector (cueBallVelocity * Math.cos (angle), cueBallVelocity * Math.sin (angle));
                    Vector nv2 = position.sub (GameScene.ball[i].getPosition ());
                    nv2.normalize ();
                    nv2.multiply (velocity.dot (nv2));
                    Vector nv2b = GameScene.ball[i].getPosition ().sub (position);
                    nv2b.normalize ();
                    nv2b.multiply (GameScene.ball[i].getVelocity ().dot (nv2b));
                    Vector nv1b = GameScene.ball[i].getVelocity ().sub (nv2b);
                    double p = GameScene.ball[i].getSphere ().getLayoutX (), q = GameScene.ball[i].getSphere ().getLayoutY ();
                    Vector v = nv2.add (nv1b);
                    dirLine1.setStartX (p);
                    dirLine1.setStartY (q);
                    dirLine1.setEndX (p + v.getX ());
                    dirLine1.setEndY (q + v.getY ());
                }
            }

            stick.setVisible (true);
            stick.setLayoutX (x1 - (346 + 36));
            stick.setLayoutY (y1 - 14);
            ang = Math.toDegrees (Math.atan ((y2 - y1) / (x2 - x1)));
            if (x2 <= x1) {
                ang = 180 - ang;
                ang = -ang;
            }
            stick.setRotate (ang);
            double mid_x = stick.getLayoutX () + stick.getFitWidth () / 2;
            double mid_y = stick.getLayoutY () + stick.getFitHeight () / 2;
            double dist = (x1 - mid_x);
            double now_y = Math.sin (Math.toRadians (-ang)) * dist + mid_y;
            double now_x = mid_x + (dist - dist * Math.cos (Math.toRadians (ang)));
            double pos_x = now_x - stick.getFitWidth () / 2;
            double pos_y = now_y - stick.getFitHeight () / 2 + 4;
            stick.setLayoutX (pos_x);
            stick.setLayoutY (pos_y);


        }
    }

    private boolean collides(Circle circle, SingleBall b) {
        double x = circle.getCenterX () - b.getPosition ().getX ();
        double y = circle.getCenterY () - b.getPosition ().getY ();
        double dist = Math.sqrt (x * x + y * y);
        if (dist - (12.5 + 12.5) <= 0 && dist - (12.5 + 12.5) >= -3) {

            return true;
        }
        else return false;
    }

    public void released(MouseEvent event) {
        if (GameScene.isIsTurn () && !GameScene.isGameOver () && !GameScene.isGamePause () && GameScene.getPlayer1 ().isMyturn ()) {
            double x = event.getSceneX ();
            double y = event.getSceneY ();
            xp = x;
            yp = y;
            Main.outToServer.println ("st#" + (int) stick.getRotate () + "#" + (int) stick.getLayoutX () + "#" + (int) stick.getLayoutY ());
        }

    }

    public void showVelocity() {
        if (!GameScene.isGameOver () && !GameScene.isGamePause () && GameScene.getPlayer1 ().isMyturn ()) {
            velocityLabel.setText (String.valueOf (Math.floor (velocitySlider.getValue () / 30 * 100)));
            stick.setLayoutX (GameScene.getCueBall ().getPosition ().getX () - (346 + 36) - (Math.floor (velocitySlider.getValue () / 30 * 100)));
            stick.setLayoutY (GameScene.getCueBall ().getPosition ().getY () - (375 - 367));
            ang = Math.toDegrees (Math.atan ((yp - GameScene.getCueBall ().getPosition ().getY ()) / (xp - GameScene.getCueBall ().getPosition ().getX ())));
            if (GameScene.getCueBall ().getPosition ().getX () >= xp) {
                ang = 180 - ang;
                ang = -ang;
            }
            double mid_x = stick.getLayoutX () + stick.getFitWidth () / 2;
            double mid_y = stick.getLayoutY () + stick.getFitHeight () / 2;
            double dist = (GameScene.getCueBall ().getPosition ().getX () - mid_x);
            double now_y = Math.sin (Math.toRadians (-ang)) * dist + mid_y;
            double now_x = mid_x + (dist - dist * Math.cos (Math.toRadians (ang)));
            stick.setLayoutX (now_x - stick.getFitWidth () / 2);
            stick.setLayoutY (now_y - stick.getFitHeight () / 2);
        }

    }

    public void mereDaw() {
        double cueBallVelocity = 0;
        if (GameScene.isIsTurn () && !GameScene.isGameOver () && xp != -1 && yp != -1 && !GameScene.isGamePause () && GameScene.getPlayer1 ().isMyturn ()) {

            if (GameScene.getTurnNum () == 1 && !GameScene.isTurnOffSounds ()) {
                SoundEffects.START.play ();
            }
            cueBallVelocity = velocitySlider.getValue ();
            if (cueBallVelocity != 0) {
                line.setVisible (false);
                circle.setVisible (false);
                velocitySlider.setValue (0);
                velocityLabel.setText ("0");
                double angle = Math.atan ((yp - GameScene.getCueBall ().getPosition ().getY ()) / (xp - GameScene.getCueBall ().getPosition ().getX ()));
                if (xp < GameScene.getCueBall ().getPosition ().getX ()) cueBallVelocity = -cueBallVelocity;
                GameScene.setVelocity (cueBallVelocity * Math.cos (angle), cueBallVelocity * Math.sin (angle));

                xp = -1;
                yp = -1;
                stick.setVisible (false);
                Main.outToServer.println ("stf");
                dirLine1.setVisible (false);
            }

        }
        else {
            velocitySlider.setValue (0);
            velocityLabel.setText ("0");
        }
    }


}
