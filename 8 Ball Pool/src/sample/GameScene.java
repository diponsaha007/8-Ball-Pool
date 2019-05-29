package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;


public class GameScene {
    private Group group;
    private Scene scene, menu;
    private Parent root;
    static SingleBall ball[];
    private static Player player1, player2;
    private static int turnNum = 1;
    private static boolean isTurn;
    private boolean isFoul;
    private static boolean gameOver;
    private double stack_y = 605;
    private int flag2 = 0;//for turn change
    private int flag3 = 0; //for foul check
    private int flg = 0; //for other type ball collision foul check
    private int flagg = 1; // for no ball hit foul check
    private ArrayList<Integer> thisTurnPottedBalls;
    private static Label label = new Label (); //Label for the turn change
    private Label label1 = new Label (); //label for ball type stripes or solids
    private Label label2 = new Label (); //label for ball type stripes or solids
    private Label label3 = new Label (); //label for Best Of Lcuk
    private Label label4 = new Label (); //label for Game over
    private Label label5 = new Label (); //laber  for player wins
    private Label label6 = new Label ();  //label for press any key to continue
    private Label label7 = new Label (); //label for foul
    private ImageView[] BallSolid = new ImageView[7];
    private ImageView[] BallStripes = new ImageView[7];
    private ImageView[] BallKala = new ImageView[2];
    private boolean potted[] = new boolean[16];
    private static boolean TurnOffSounds;
    private Timeline timeline = new Timeline ();
    private static boolean gamePause = false;
    private Stage window;
    private static ImageView imageView1;
    private static ImageView imageView2;
    private static Label player1label, player2Label;
    private static PrintWriter outToServer;
    private static BufferedReader inFromServer;
    private Button leave;
    private static int bet;
    private Controller controller;

    public GameScene(Group group, Scene scene, Scene menu, Parent root, Stage window, PrintWriter outToServer, BufferedReader inFromServer, Controller controller) throws Exception {
        ball = new SingleBall[16];
        this.outToServer = outToServer;
        this.inFromServer = inFromServer;
        this.window = window;
        this.group = group;
        this.menu = menu;
        this.root = root;
        this.controller = controller;
        player1label = new Label ();
        player2Label = new Label ();
        player1label.setLayoutX (240);
        player1label.setLayoutY (10);
        player2Label.setLayoutX (760);
        player2Label.setLayoutY (10);
        player1label.getStyleClass ().add ("label-player2");
        player2Label.getStyleClass ().add ("label-player2");
        group.getChildren ().addAll (player1label, player2Label);
        player1 = new Player ("");
        player2 = new Player ("");
        imageView1 = new ImageView ();
        imageView2 = new ImageView ();
        imageView1.setLayoutX (100);
        imageView1.setLayoutY (0);
        imageView2.setLayoutX (880);
        imageView2.setLayoutY (0);
        imageView1.setFitWidth (122);
        imageView2.setFitWidth (122);
        imageView1.setFitHeight (122);
        imageView2.setFitHeight (122);
        imageView1.setImage (new Image ("sample/Default Profile Pictures/4860042536_a85b1c2745.jpg"));
        imageView2.setImage (new Image ("sample/Default Profile Pictures/images.jpg"));
        //Parent root = FXMLLoader.load (getClass ().getResource ("sample.fxml"));

        group.getChildren ().addAll (root, imageView1, imageView2);
        this.scene = scene;
        scene.getStylesheets ().add ("Viper.css");
        //Creating Balls
        ball[4] = new SingleBall (865, 325, "\\Ball Images\\4.png", 1, 4);
        ball[12] = new SingleBall (865, 350, "\\Ball Images\\12.png", 2, 12);
        ball[3] = new SingleBall (865, 375, "\\Ball Images\\3.png", 1, 3);
        ball[9] = new SingleBall (865, 400, "\\Ball Images\\9.png", 2, 9);
        ball[7] = new SingleBall (865, 425, "\\Ball Images\\7.png", 1, 7);

        ball[1] = new SingleBall (841, 338, "\\Ball Images\\1.png", 1, 1);
        ball[15] = new SingleBall (841, 363, "\\Ball Images\\15.png", 2, 15);
        ball[2] = new SingleBall (841, 388, "\\Ball Images\\2.png", 1, 2);
        ball[5] = new SingleBall (841, 413, "\\Ball Images\\5.png", 1, 5);

        ball[14] = new SingleBall (817, 350, "\\Ball Images\\14.png", 2, 14);
        ball[8] = new SingleBall (817, 375, "\\Ball Images\\8.png", 3, 8);
        ball[10] = new SingleBall (817, 400, "\\Ball Images\\10.png", 2, 10);

        ball[11] = new SingleBall (793, 363, "\\Ball Images\\11.png", 2, 11);
        ball[6] = new SingleBall (793, 388, "\\Ball Images\\6.png", 1, 6);

        ball[13] = new SingleBall (769, 375, "\\Ball Images\\13.png", 2, 13);

        ball[0] = new SingleBall (346, 375, "\\Ball Images\\0.png", 0, 0);
        //Adding them to group
        for (int i = 0; i < 16; i++) {
            group.getChildren ().add (ball[i].DrawBall ());
        }
        thisTurnPottedBalls = new ArrayList<> ();
        player1.setMyturn (true);
        isFoul = false;
        gameOver = false;
        isTurn = true;
        label.setLayoutX (462);
        label.setLayoutY (81);
        label.setText (player1.getName () + " Is Breaking");
        label.getStyleClass ().add ("label-player");
        label7.setLayoutX (531);
        label7.setLayoutY (655);
        label7.setText ("FOUL!!");
        label7.getStyleClass ().add ("label-player");
        group.getChildren ().add (label);
        for (int i = 0; i < 16; i++) {
            potted[i] = false;
        }
        SoundEffects.init ();
        SoundEffects.volume = SoundEffects.Volume.LOW;
        TurnOffSounds = false;
        group.getChildren ().addAll (label1, label2, label3);
        group.getChildren ().addAll (label4, label5, label6, label7);
        label1.setVisible (false);
        label2.setVisible (false);
        label3.setVisible (false);
        label4.setVisible (false);
        label5.setVisible (false);
        label6.setVisible (false);
        label7.setVisible (false);
        for (int i = 0; i < 7; i++) {
            BallSolid[i] = new ImageView ("\\sample\\Small Balls\\Ball" + String.valueOf (i + 1) + ".png");
            BallStripes[i] = new ImageView ("\\sample\\Small Balls\\Ball" + String.valueOf (i + 9) + ".png");
            BallSolid[i].setFitWidth (30);
            BallSolid[i].setFitHeight (30);
            BallStripes[i].setFitHeight (30);
            BallStripes[i].setFitWidth (30);
            group.getChildren ().addAll (BallSolid[i], BallStripes[i]);
            BallStripes[i].setVisible (false);
            BallSolid[i].setVisible (false);
        }
        for (int i = 0; i < 2; i++) {
            BallKala[i] = new ImageView ("\\sample\\Small Balls\\Ball" + String.valueOf (8) + ".png");
            BallKala[i].setFitWidth (30);
            BallKala[i].setFitHeight (30);
            BallKala[i].setVisible (false);
            group.getChildren ().add (BallKala[i]);
        }
        BallKala[0].setLayoutX (171);
        BallKala[0].setLayoutY (660);
        BallKala[1].setLayoutX (678);
        BallKala[1].setLayoutY (660);
        leave = new Button ("Leave");
        leave.setLayoutX (2);
        leave.setLayoutY (70);
        leave.getStyleClass ().add ("button-game");
        leave.setOnAction (event -> {
            if (!gameOver) {
                showLeaveAlert ();
            }
        });
        group.getChildren ().add (leave);
    }

    public static void setVelocity(double x, double y) {
        ball[0].setVelocity (x, y);
        outToServer.println ("V#" + x + "#" + y);
    }

    private void reInitialize() {
        for (int i = 0; i < 16; i++) {
            group.getChildren ().remove (ball[i].getSphere ());
        }
        ball = new SingleBall[16];
        ball[4] = new SingleBall (865, 325, "\\Ball Images\\4.png", 1, 4);
        ball[12] = new SingleBall (865, 350, "\\Ball Images\\12.png", 2, 12);
        ball[3] = new SingleBall (865, 375, "\\Ball Images\\3.png", 1, 3);
        ball[9] = new SingleBall (865, 400, "\\Ball Images\\9.png", 2, 9);
        ball[7] = new SingleBall (865, 425, "\\Ball Images\\7.png", 1, 7);

        ball[1] = new SingleBall (841, 338, "\\Ball Images\\1.png", 1, 1);
        ball[15] = new SingleBall (841, 363, "\\Ball Images\\15.png", 2, 15);
        ball[2] = new SingleBall (841, 388, "\\Ball Images\\2.png", 1, 2);
        ball[5] = new SingleBall (841, 413, "\\Ball Images\\5.png", 1, 5);

        ball[14] = new SingleBall (817, 350, "\\Ball Images\\14.png", 2, 14);
        ball[8] = new SingleBall (817, 375, "\\Ball Images\\8.png", 3, 8);
        ball[10] = new SingleBall (817, 400, "\\Ball Images\\10.png", 2, 10);

        ball[11] = new SingleBall (793, 363, "\\Ball Images\\11.png", 2, 11);
        ball[6] = new SingleBall (793, 388, "\\Ball Images\\6.png", 1, 6);

        ball[13] = new SingleBall (769, 375, "\\Ball Images\\13.png", 2, 13);

        ball[0] = new SingleBall (346, 375, "\\Ball Images\\0.png", 0, 0);
        //Adding them to group
        for (int i = 0; i < 16; i++) {
            group.getChildren ().add (ball[i].DrawBall ());
        }
        turnNum = 1;
        stack_y = 605;
        flag2 = 0;
        flag3 = 0;
        flg = 0;
        flagg = 1;
        thisTurnPottedBalls.clear ();
        for (int i = 0; i < 16; i++) {
            potted[i] = false;
        }
        player1.setMyturn (true);
        player2.setMyturn (false);
        isFoul = false;
        gameOver = false;
        isTurn = true;
        label1.setVisible (false);
        label2.setVisible (false);
        label3.setVisible (false);
        label4.setVisible (false);
        label5.setVisible (false);
        label6.setVisible (false);
        label7.setVisible (false);
        for (int i = 0; i < 7; i++) {
            BallStripes[i].setVisible (false);
            BallSolid[i].setVisible (false);
        }
        for (int i = 0; i < 2; i++) {
            BallKala[i].setVisible (false);
        }
        gamePause = false;
        player1.setWin (false);
        player2.setWin (false);
        player1.setBallType (0);
        player2.setBallType (0);
        player2.setAllBallsPotted (false);
        player1.setAllBallsPotted (false);
    }

    private void update() {
        if (turnNum == 1) {
            labelDekhaw ();
        }
        int flag = 0;
        moveCueBall ();
        for (int i = 0; i < 16; i++) {
            if (!ball[i].getVelocity ().isNull ()) {
                flag = 1;
                flag2 = 1;
            }
            updateSingleBalls (i);
            checkForPocket (i);
        }
        if (flag == 1) {
            isTurn = false;
        }
        else if (flag == 0 && flag2 == 0) {
            isTurn = true;
            turnLabel ();
        }
        else if (flag == 0 && flag2 == 1) {
            isFoul = false;
            checkForCases ();
            checkAllPottedBalls ();
            if (isFoul && !gameOver) {
                stopGame ();
                showAlert ();
                startFromPause ();
            }
            flag2 = 0;
            flag3 = 0;
            flg = 0;
            flagg = 1;
            turnNum++;
            isTurn = true;

            if (thisTurnPottedBalls.contains (Integer.valueOf (0))) {
                ball[0].setPosition (new Vector (346, 375));
                ball[0].getSphere ().setVisible (true);
            }
            for (int i = 1; i <= 7; i++) {
                if (potted[i]) {
                    BallSolid[i - 1].setVisible (false);
                }
            }
            for (int i = 9; i <= 15; i++) {
                if (potted[i]) {
                    BallStripes[i - 9].setVisible (false);
                }
            }
            if (player1.isAllBallsPotted ()) {
                BallKala[0].setVisible (true);
            }
            if (player2.isAllBallsPotted ()) {
                BallKala[1].setVisible (true);
            }
            thisTurnPottedBalls.clear ();
        }
    }

    private void labelDekhaw() {
        player1label.setText (player1.getName ());
        player2Label.setText (player2.getName ());
        if (player1.isMyturn ())
            label.setText (player1.getName () + " Is Breaking");
        else {
            label.setText (player2.getName () + " Is Breaking");
        }
    }

    private void turnLabel() {
        if (player1.isMyturn ()) {
            label.setText ("Turn for " + player1.getName ());
        }
        else {
            label.setText ("Turn for " + player2.getName ());
        }
    }

    private void showLeaveAlert() {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setTitle ("Leave!!");
        alert.setHeaderText ("You will lose the game if you leave!!");
        alert.setContentText ("Sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait ();
        if (result.get () == ButtonType.OK) {
            player2.setWin (true);
            player1.setWin (false);
            gameOver = true;
            window.setScene (menu);
            reInitialize ();
            outToServer.println ("Lt");
        }
    }

    private void showAlert() {
        Alert alert = new Alert (Alert.AlertType.WARNING);
        alert.setTitle ("FOUL!!");

        if (thisTurnPottedBalls.contains (Integer.valueOf (0)))
            alert.setHeaderText ("You potted the Cue ball");
        else if (flg == 1)
            alert.setHeaderText ("You must hit your assigned ball type");
        else
            alert.setHeaderText ("You must hit a ball");

        if (player1.isMyturn ())
            alert.setContentText ("Ball in hand " + player1.getName ());
        else alert.setContentText ("Ball in hand " + player2.getName ());
        alert.show ();
    }

    private void checkAllPottedBalls() {
        if (player1.getBallType () == 0)
            return;
        if (player1.isMyturn ()) {
            int f = 0;
            if (player1.getBallType () == 1) {
                for (int i = 1; i <= 7; i++) {
                    if (potted[i] == false) {
                        f = 1;
                        break;
                    }
                }
            }
            else {
                for (int i = 9; i <= 15; i++) {
                    if (potted[i] == false) {
                        f = 1;
                        break;
                    }
                }
            }
            if (f == 0)
                player1.setAllBallsPotted (true);
        }
        else {
            int f = 0;
            if (player2.getBallType () == 1) {
                for (int i = 1; i <= 7; i++) {
                    if (potted[i] == false) {
                        f = 1;
                        break;
                    }
                }
            }
            else {
                for (int i = 9; i <= 15; i++) {
                    if (potted[i] == false) {
                        f = 1;
                        break;
                    }
                }
            }
            if (f == 0)
                player2.setAllBallsPotted (true);
        }
    }

    private void updateSingleBalls(int ball_num) {
        if (ball[ball_num].getVelocity ().getSize () <= 8e-2) {
            ball[ball_num].setVelocity (0, 0);
        }
        else {
            ball[ball_num].getPosition ().setX (ball[ball_num].getPosition ().getX () + ball[ball_num].getVelocity ().getX ());
            ball[ball_num].getPosition ().setY (ball[ball_num].getPosition ().getY () + ball[ball_num].getVelocity ().getY ());
            for (SingleBall b : ball) {
                if (ball_num != b.getBallNumber () && ball[ball_num].collides (b)) {
                    if (turnNum != 1 && !isTurnOffSounds ()) {
                        SoundEffects.COLLIDE.play ();
                    }
                    if (ball_num == 0 && flg == 0 && player1.getBallType () == 0) {
                        flg = 1;
                        if (b.getBallType () == 3) {
                            flag3 = 1;
                        }
                    }
                    if (ball_num == 0 && flg == 0 && player1.getBallType () != 0) {
                        flg = 1;
                        if (player1.isMyturn ()) {
                            if (player1.getBallType () != b.getBallType ()) {
                                if (b.getBallNumber () == 8 && player1.isAllBallsPotted ())
                                    flag3 = 0;
                                else flag3 = 1;
                            }
                        }
                        else {
                            if (player2.getBallType () != b.getBallType ()) {
                                if (b.getBallNumber () == 8 && player2.isAllBallsPotted ())
                                    flag3 = 0;
                                else flag3 = 1;
                            }
                        }
                    }
                    if (ball_num == 0) {
                        flagg = 0;
                    }
                    ball[ball_num].getPosition ().setX (ball[ball_num].getPosition ().getX () - ball[ball_num].getVelocity ().getX ());
                    ball[ball_num].getPosition ().setY (ball[ball_num].getPosition ().getY () - ball[ball_num].getVelocity ().getY ());
                    ball[ball_num].transferEnergy (b);
                    break;
                }
            }
            ball[ball_num].updateWallCollision ();
            ball[ball_num].applyTableFriction ();
            ball[ball_num].spin ();
        }
        ball[ball_num].getSphere ().setLayoutX (ball[ball_num].getPosition ().getX ());
        ball[ball_num].getSphere ().setLayoutY (ball[ball_num].getPosition ().getY ());
    }

    private void moveCueBall() {
        ball[0].getSphere ().addEventHandler (MouseEvent.MOUSE_DRAGGED, event -> {
            if (isTurn && isFoul && player1.isMyturn ()) {
                controller.stick.setVisible (false);
                controller.circle.setVisible (false);
                controller.line.setVisible (false);
                controller.dirLine1.setVisible (false);
                ball[0].getSphere ().setCursor (Cursor.CLOSED_HAND);
                if ((event.getSceneX () <= 937 && event.getSceneX () >= 157) && (event.getSceneY () >= 180 && event.getSceneY () <= 568)) {
                    ball[0].setPosition (new Vector (event.getSceneX (), event.getSceneY ()));
                    outToServer.println ("M#" + event.getSceneX () + "#" + event.getSceneY ());
                }

            }
            else if (isTurn && turnNum == 1 && player1.isMyturn ()) {
                controller.stick.setVisible (false);
                controller.circle.setVisible (false);
                controller.line.setVisible (false);
                controller.dirLine1.setVisible (false);
                ball[0].getSphere ().setCursor (Cursor.CLOSED_HAND);
                if ((event.getSceneX () <= 344 && event.getSceneX () >= 155) && (event.getSceneY () >= 170 && event.getSceneY () <= 570)) {
                    ball[0].setPosition (new Vector (event.getSceneX (), event.getSceneY ()));
                    outToServer.println ("M#" + event.getSceneX () + "#" + event.getSceneY ());
                }
            }
        });

    }

    private void checkForCases() {
        int flag = 0;
        if (turnNum == 1) {
            if (thisTurnPottedBalls.size () == 0) {
                flag = 1;
            }

            else {
                for (int i = 0; i < thisTurnPottedBalls.size (); i++) {
                    if (thisTurnPottedBalls.get (i).intValue () == 8) {
                        khelaSes ();
                    }
                    else if (thisTurnPottedBalls.get (i).intValue () == 0) {
                        isFoul = true;
                        flag = 1;
                    }
                    else potted[thisTurnPottedBalls.get (i).intValue ()] = true;
                }
            }

        }
        else if (turnNum >= 2 && player1.getBallType () == 0) {
            if (thisTurnPottedBalls.size () == 0) {
                flag = 1;
            }
            else {
                int firstPuttedBallNum = thisTurnPottedBalls.get (0).intValue ();
                if (firstPuttedBallNum >= 1 && firstPuttedBallNum < 8) {
                    if (player1.isMyturn ()) {
                        player1.setBallType (1);
                        player2.setBallType (2);
                    }
                    else {
                        player1.setBallType (2);
                        player2.setBallType (1);
                    }
                    showLabel ();
                }
                else if (firstPuttedBallNum >= 9 && firstPuttedBallNum <= 15) {
                    if (player1.isMyturn ()) {
                        player1.setBallType (2);
                        player2.setBallType (1);
                    }
                    else {
                        player1.setBallType (1);
                        player2.setBallType (2);
                    }
                    showLabel ();
                }
                for (int i = 0; i < thisTurnPottedBalls.size (); i++) {
                    if (thisTurnPottedBalls.get (i).intValue () == 8) {
                        khelaSes ();
                    }
                    else if (thisTurnPottedBalls.get (i).intValue () == 0) {
                        isFoul = true;
                        flag = 1;
                    }
                    else potted[thisTurnPottedBalls.get (i).intValue ()] = true;
                }
            }

        }
        else {
            if (thisTurnPottedBalls.size () == 0) {
                flag = 1;
            }
            else if (thisTurnPottedBalls.size () == 1 && thisTurnPottedBalls.get (0).intValue () == 8) {
                if (player1.isMyturn ()) {
                    if (player1.getBallType () == 1) {
                        int f = 0;
                        for (int i = 1; i <= 7; i++) {
                            if (!potted[i]) {
                                f = 1;
                                khelaSes ();
                            }
                        }
                        if (f == 0) {
                            win ();
                        }
                    }
                    else {
                        int f = 0;
                        for (int i = 9; i <= 15; i++) {
                            if (!potted[i]) {
                                f = 1;
                                khelaSes ();
                            }
                        }
                        if (f == 0) {
                            win ();
                        }
                    }
                }
                else {
                    if (player2.getBallType () == 1) {
                        int f = 0;
                        for (int i = 1; i <= 7; i++) {
                            if (!potted[i]) {
                                f = 1;
                                khelaSes ();
                            }
                        }
                        if (f == 0) {
                            win ();
                        }
                    }
                    else {
                        int f = 0;
                        for (int i = 9; i <= 15; i++) {
                            if (!potted[i]) {
                                f = 1;
                                khelaSes ();
                            }
                        }
                        if (f == 0) {
                            win ();
                        }
                    }
                }
            }
            else {
                int firstPuttedBallNum = thisTurnPottedBalls.get (0).intValue ();
                if (player1.isMyturn ()) {
                    if (player1.getBallType () != ball[firstPuttedBallNum].getBallType ()) {
                        flag = 1;
                    }
                    for (int i = 0; i < thisTurnPottedBalls.size (); i++) {
                        if (thisTurnPottedBalls.get (i).intValue () == 8) {
                            //Write code
                            khelaSes ();
                        }
                        else if (thisTurnPottedBalls.get (i).intValue () == 0) {
                            isFoul = true;
                            flag = 1;
                        }
                        else {
                            if (thisTurnPottedBalls.get (i).intValue () != 0)
                                potted[thisTurnPottedBalls.get (i).intValue ()] = true;
                        }
                    }
                }
                else {
                    if (player2.getBallType () != ball[firstPuttedBallNum].getBallType ()) {
                        flag = 1;
                    }
                    for (int i = 0; i < thisTurnPottedBalls.size (); i++) {
                        if (thisTurnPottedBalls.get (i).intValue () == 8) {
                            //Write Code
                            khelaSes ();
                        }
                        else if (thisTurnPottedBalls.get (i).intValue () == 0) {
                            isFoul = true;
                            flag = 1;
                        }
                        else {
                            if (thisTurnPottedBalls.get (i).intValue () != 0)
                                potted[thisTurnPottedBalls.get (i).intValue ()] = true;
                        }
                    }
                }
            }


        }
        if (flag3 == 1 || flagg == 1) {
            isFoul = true;
        }
        if (flag == 1 || flag3 == 1 || flagg == 1)
            alterTurn ();
        if (isFoul) {
            label7.setVisible (true);
        }
        else label7.setVisible (false);

    }

    private void showLabel() {
        label1.setVisible (true);
        label2.setVisible (true);
        label3.setVisible (true);
        label1.setLayoutX (186);
        label2.setLayoutX (719);
        label3.setLayoutX (488);
        label1.setLayoutY (624);
        label2.setLayoutY (624);
        label3.setLayoutY (624);
        if (player1.getBallType () == 1) {
            label1.setText (player1.getName () + " is Solids");
            label2.setText (player2.getName () + " is Stripes");
            int place1 = 360, place2 = 147;
            for (int i = 0; i < 7; i++) {
                BallSolid[i].setLayoutX (531 - place1);
                BallSolid[i].setLayoutY (660);
                BallStripes[i].setLayoutX (531 + place2);
                BallStripes[i].setLayoutY (660);
                place1 -= 45;
                place2 += 45;
                BallStripes[i].setVisible (true);
                BallSolid[i].setVisible (true);
            }
        }
        else {
            label1.setText (player1.getName () + " is Stripes");
            label2.setText (player2.getName () + " is Solids");
            int place1 = 360, place2 = 147;
            for (int i = 0; i < 7; i++) {
                BallStripes[i].setLayoutX (531 - place1);
                BallSolid[i].setLayoutY (660);
                BallSolid[i].setLayoutX (531 + place2);
                BallStripes[i].setLayoutY (660);
                place1 -= 45;
                place2 += 45;
                BallStripes[i].setVisible (true);
                BallSolid[i].setVisible (true);
            }
        }
        label3.setText ("Best Of Luck");
        label1.getStyleClass ().add ("label-player");
        label2.getStyleClass ().add ("label-player");
        label3.getStyleClass ().add ("label-player");

    }

    private void khelaSes() {
        if (player1.isMyturn ()) {
            player2.setWin (true);
            player1.setWin (false);
            gameOver = true;
        }
        else {
            player1.setWin (true);
            player2.setWin (false);
            gameOver = true;
        }
    }

    private void win() {
        if (player1.isMyturn ()) {
            player2.setWin (false);
            player1.setWin (true);
            gameOver = true;
        }
        else {
            player1.setWin (false);
            player2.setWin (true);
            gameOver = true;
        }
    }

    private void gameOverDilg() {
        label4.setVisible (true);
        label5.setVisible (true);
        label6.setVisible (true);
        label4.setLayoutX (425);
        label4.setLayoutY (169);
        label5.setLayoutX (415);
        label5.setLayoutY (242);
        label6.setLayoutX (240);
        label6.setLayoutY (500);
        label4.setText ("Game Over");
        if (player1.isWin ()) label5.setText (player1.getName () + " Wins!");
        else label5.setText (player2.getName () + " Wins!");
        label6.setText ("Press Any Key To Continue");
        label4.getStyleClass ().add ("label-over");
        label5.getStyleClass ().add ("label-over");
        label6.getStyleClass ().add ("label-over");
        if (GameScene.getPlayer1 ().isWin ()) {
            player1.setBalance (player1.getBalance () + bet);
            outToServer.println ("W#" + GameScene.getPlayer1 ().getName () + "#" + GameScene.getPlayer2 ().getName () + "#" + bet);
        }
        else {
            player1.setBalance (player1.getBalance () - bet);
        }
        scene.addEventHandler (KeyEvent.KEY_PRESSED, keyEvent -> {
            window.setScene (menu);
            startNewGame ();
        });
    }

    private void alterTurn() {
        if (player1.isMyturn ()) {
            player1.setMyturn (false);
            player2.setMyturn (true);
        }
        else {
            player2.setMyturn (false);
            player1.setMyturn (true);
        }
        if (!TurnOffSounds)
            SoundEffects.TURNCHANGE.play ();
    }


    private void checkForPocket(int ballNum) {
        double x = ball[ballNum].getPosition ().getX (), y = ball[ballNum].getPosition ().getY ();
        double check = 625;

        if (sqdistance (x, y, 130, 154) <= check) {
            dropit (ballNum);
        }
        else if (sqdistance (x, y, 550, 151) <= check) {
            dropit (ballNum);
        }
        else if (sqdistance (x, y, 970, 154) <= check) {
            dropit (ballNum);
        }
        else if (sqdistance (x, y, 970, 595) <= check) {
            dropit (ballNum);
        }
        else if (sqdistance (x, y, 550, 600) <= check) {
            dropit (ballNum);
        }
        else if (sqdistance (x, y, 130, 595) <= check) {
            dropit (ballNum);
        }
        if ((y <= 148 || y >= 602) && !ball[ballNum].getisDropped ()) {
            dropit (ballNum);
        }
        if ((x <= 113 || x >= 979) && !ball[ballNum].getisDropped ()) {
            dropit (ballNum);
        }

    }

    private double sqdistance(double x1, double y1, double x2, double y2) {
        return (((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
    }

    private void dropit(int ballNum) {
        thisTurnPottedBalls.add (Integer.valueOf (ballNum));
        ball[ballNum].setDropped (true);
        ball[ballNum].setVelocity (0, 0);
        ball[ballNum].setPosition (new Vector (1045, stack_y));

        stack_y -= 25;
        if (ballNum == 0) {
            stack_y += 25;
            ball[0].getSphere ().setVisible (false);
            ball[0].setPosition (new Vector (0, 0));
            ball[0].setDropped (false);
        }
    }


    public void startGame() {
        gamePause = false;
        timeline.setCycleCount (Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame (
                Duration.seconds (0.015),
                ae -> {
                    if (!gameOver) {
                        update ();
                    }
                    else {
                        gameOver = false;
                        gameOverDilg ();
                    }
                });
        timeline.getKeyFrames ().add (keyFrame);
        timeline.play ();
    }

    public void stopGame() {
        timeline.stop ();
        gamePause = true;
    }

    public void startFromPause() {
        gamePause = false;
        timeline.play ();
    }

    public void startNewGame() {
        stopGame ();
        gamePause = false;
        reInitialize ();
        timeline.play ();
    }

    public static void setTurnNum(int turnNum) {
        GameScene.turnNum = turnNum;
    }

    public static boolean isIsTurn() {
        return isTurn;
    }

    public static void setIsTurn(boolean isTurn) {
        GameScene.isTurn = isTurn;
    }

    public boolean isFoul() {
        return isFoul;
    }

    public void setFoul(boolean foul) {
        isFoul = foul;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        GameScene.gameOver = gameOver;
    }

    public static boolean isGamePause() {
        return gamePause;
    }

    public static void setGamePause(boolean gamePause) {
        GameScene.gamePause = gamePause;
    }

    public static SingleBall getCueBall() {
        return ball[0];
    }

    public static int getTurnNum() {
        return turnNum;
    }

    public static boolean isTurnOffSounds() {
        return TurnOffSounds;
    }

    public static void setTurnOffSounds(boolean turnOffSounds) {
        TurnOffSounds = turnOffSounds;
    }

    public static Player getPlayer1() {
        return player1;
    }

    public static void setPlayer1(Player player1) {
        GameScene.player1 = player1;
    }

    public static Player getPlayer2() {
        return player2;
    }

    public static void setPlayer2(Player player2) {
        GameScene.player2 = player2;
    }

    public static void setName1(String s) {
        player1.setName (s);
        player1label.setText (s);
    }

    public static void setImage1(String s) {
        Image image = new Image ("https://graph.facebook.com/" + s + "/picture?type=large&width=122&height=122");
        if (image.isError ()) {
            imageView1.setImage (new Image ("sample/Default Profile Pictures/4860042536_a85b1c2745.jpg"));
        }
        else imageView1.setImage (image);
    }

    public static void setName2(String s) {
        player2.setName (s);
        player2Label.setText (s);
    }

    public static void setImage2(String s) {
        Image image = new Image ("https://graph.facebook.com/" + s + "/picture?type=large&width=122&height=122");
        if (image.isError ()) {
            imageView2.setImage (new Image ("sample/Default Profile Pictures/4860042536_a85b1c2745.jpg"));
        }
        else imageView2.setImage (image);
    }

    public static void setTurn(boolean t) {
        if (t) {
            player1.setMyturn (true);
            player2.setMyturn (false);
            label.setText (player1.getName () + " is Breaking");
        }
        else {
            player1.setMyturn (false);
            player2.setMyturn (true);
            label.setText (player2.getName () + " is Breaking");
        }

    }


    public static Label getPlayer1label() {
        return player1label;
    }

    public static void setPlayer1label(Label player1label) {
        GameScene.player1label = player1label;
    }

    public static Label getPlayer2Label() {
        return player2Label;
    }

    public static void setPlayer2Label(Label player2Label) {
        GameScene.player2Label = player2Label;
    }

    public static int getBet() {
        return bet;
    }

    public static void setBet(int bet) {
        GameScene.bet = bet;
    }
}
