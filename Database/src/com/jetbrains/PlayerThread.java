package com.jetbrains;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayerThread implements Runnable {
    private PrintWriter outToClient= null, outToMyClient;
    private BufferedReader inFromClient;
    private Socket socket;
    private static boolean turn = false;
    private ResultSet rs;
    private Statement st;
    private static ArrayList<PlayerThread>playerThreads= new ArrayList<> ();
    private PlayerData thisPlayer;

    public PlayerThread( BufferedReader inFromClient, Socket socket, PrintWriter outToMyClient, Statement st) {
        this.inFromClient = inFromClient;
        this.socket = socket;
        this.outToMyClient = outToMyClient;
        this.st = st;
        thisPlayer= new PlayerData ();
    }

    public void run() {
        while (true) {
            try {
                String sentence = inFromClient.readLine ();
                if (sentence != null) {
                    //venge felo
                    ArrayList<String> str = new ArrayList<> ();
                    for (String val : sentence.split ("[#]")) {
                        if (!val.isEmpty ()) {
                            str.add (val);
                        }
                    }
                    //done
                    //now check cases
                    //first case if the game is over
                    if (sentence.charAt (0) == 'W') {
                        System.out.println (str.get (1) + " wins.. " + str.get (2) + " loses..");
                        updateDatabase (str.get (1), str.get (2),Integer.parseInt (str.get (3)));
                    }
                    //second case assign turns
                    else if (str.get (0).compareTo ("login2") == 0) {
                        if (turn) {
                            sentence += "#true";
                            turn = false;
                        }
                        else {
                            sentence += "#false";
                            turn = true;
                        }
                        outToClient.println (sentence);
                    }
                    //third case login koraw
                    else if (str.get (0).compareTo ("login") == 0) {
                        setlogin (str.get (1), str.get (2));
                    }
                    //fourth case sign up koraw
                    else if (str.get (0).compareTo ("signup") == 0) {
                        setSignup (str.get (1), str.get (2), str.get (3));
                    }
                    //next case profile info pathaw
                    else if (str.get (0).compareTo ("profile") == 0) {
                        showProfile (str.get (1));
                    }
                    //next case leaderboard pathaw
                    else if (str.get (0).compareTo ("leaderboard") == 0) {
                        sendLeaderBoard ();
                    }
                    //next case active player list pathaw
                    else if(str.get (0).compareTo ("active")==0)
                    {
                        sendActivePlayers(str.get (1));
                    }
                    //Next case porer player er kache info pathaw khelte parbe ki na
                    else if(str.get (0).compareTo ("canPlay")==0)
                    {
                        sendOtherPlayer(str.get (1),Integer.parseInt (str.get (2)));
                    }
                    //Next case reject msg pass kora
                    else if(str.get (0).compareTo ("reject")==0)
                    {
                        for(int i=0;i<playerThreads.size ();i++)
                        {
                            if( playerThreads.get (i).getThisPlayer ().getName ().compareTo (str.get (1))==0)
                            {
                                playerThreads.get (i).getOutToMyClient ().println (sentence);
                            }
                        }
                    }
                    else if(str.get (0).compareTo ("play")==0)
                    {
                        khelasuru(str.get (1),Integer.parseInt (str.get (2)));
                    }
                    else if(str.get (0).compareTo ("logout")==0)
                    {
                        thisPlayer.setLoggedin (false);
                    }
                    //othoba khela colche onno joner kache pathaw
                    else {
                        outToClient.println (sentence);
                    }


                }

            } catch (Exception e) {

            }
        }
    }
    private void khelasuru(String opponent, int bet)
    {
        for(int i=0;i<playerThreads.size ();i++)
        {
            if( playerThreads.get (i).getThisPlayer ().getName ().compareTo (opponent)==0 && playerThreads.get (i).getThisPlayer ().isLoggedin ())
            {
                outToClient = playerThreads.get (i).getOutToMyClient ();
                playerThreads.get (i).setOutToClient (outToMyClient);
                thisPlayer.setPlaying (true);
                playerThreads.get (i).getThisPlayer ().setPlaying (true);
                outToMyClient.println ("login2#"+opponent+"#"+playerThreads.get (i).getThisPlayer ().getFbID ()+"#"+bet+"#true");
                outToClient.println ("login2#"+thisPlayer.getName ()+"#"+thisPlayer.getFbID ()+"#"+bet+"#false");
                break;
            }
        }
    }
    private void sendOtherPlayer(String name, int money)throws Exception
    {
        String cmd = "SELECT * FROM players where Name='" + name + "'";
        rs= st.executeQuery (cmd);
        if(rs.next ())
        {
            int m = rs.getInt ("Money");
            if(m<money)
            {
                outToMyClient.println ("canPlay#false#"+m);
            }
            else {
                for(int i=0;i<playerThreads.size ();i++)
                {
                    if( playerThreads.get (i).getThisPlayer ().getName ().compareTo (name)==0 && playerThreads.get (i).getThisPlayer ().isLoggedin ())
                    {
                        playerThreads.get (i).getOutToMyClient ().println ("canPlay#"+thisPlayer.getName ()+"#"+money);
                        break;
                    }
                }
            }
        }

    }
    private void sendActivePlayers(String username)
    {
        outToMyClient.println ("startActive#");
        for(int i=0;i<playerThreads.size ();i++)
        {
            if(playerThreads.get (i).getThisPlayer ().isLoggedin () && playerThreads.get (i).getThisPlayer ().getName ().compareTo (username)!=0 && !playerThreads.get (i).getThisPlayer ().isPlaying ())
            {
                outToMyClient.println ("active#"+playerThreads.get (i).getThisPlayer ().getName ());
            }
        }
        outToMyClient.println ("#endActive");
    }
    private void updateDatabase(String player1, String player2, int bet) throws Exception {
        String cmd = "SELECT * FROM players where Name='" + player1 + "'";
        rs = st.executeQuery (cmd);

        if (rs.next ()) {
            int gamePlayed = rs.getInt ("GamesPlayed");
            gamePlayed += 1;
            int gameWon = rs.getInt ("GamesWon");
            gameWon += 1;
            int m = rs.getInt ("Money");
            m+= bet;
            String cmd2 = "UPDATE players SET GamesPlayed='" + gamePlayed + "' WHERE Name = '" + player1 + "'";
            st.executeUpdate (cmd2);
            String cmd3 = "UPDATE players SET GamesWon='" + gameWon + "' WHERE Name = '" + player1 + "'";
            st.executeUpdate (cmd3);
            String cmd7 = "UPDATE players SET Money='" + m + "' WHERE Name = '" + player1 + "'";
            st.executeUpdate (cmd7);
        }

        String cmd4 = "SELECT * FROM players where Name='" + player2 + "'";
        rs = st.executeQuery (cmd4);
        if(rs.next ()) {
            int gamePlayed = rs.getInt ("GamesPlayed");
            gamePlayed += 1;
            int m = rs.getInt ("Money");
            m-= bet;
            String cmd5 = "UPDATE players SET GamesPlayed='" + gamePlayed + "' WHERE Name = '" + player2 + "'";
            st.executeUpdate (cmd5);
            String cmd8 = "UPDATE players SET Money='" + m + "' WHERE Name = '" + player2 + "'";
            st.executeUpdate (cmd8);
        }
        thisPlayer.setPlaying (false);
        for(int i=0;i<playerThreads.size ();i++)
        {
            if( playerThreads.get (i).getThisPlayer ().getName ().compareTo (player2)==0 )
            {
                playerThreads.get (i).getThisPlayer ().setPlaying (false);
            }
        }


    }

    private void sendLeaderBoard() throws Exception {
        outToMyClient.println ("start#");
        String cmd = "SELECT * FROM players";
        rs = st.executeQuery (cmd);
        while (rs.next ()) {
            String name = rs.getString ("Name");
            int gamePlayed = rs.getInt ("GamesPlayed");
            int gameWon = rs.getInt ("GamesWon");
            int money = rs.getInt ("Money");
            outToMyClient.println ("leaderboard#" + name + "#" + gamePlayed + "#" + gameWon + "#" + money);

        }
        outToMyClient.println ("#end");
    }

    private void showProfile(String username) throws Exception {
        String cmd = "SELECT * FROM players";
        rs = st.executeQuery (cmd);
        while (rs.next ()) {
            String name = rs.getString ("Name");
            String id = rs.getString ("FacebookID");
            int gamePlayed = rs.getInt ("GamesPlayed");
            int gameWon = rs.getInt ("GamesWon");
            int money = rs.getInt ("Money");
            if (name.compareTo (username) == 0) {
                outToMyClient.println ("profile#" + id + "#" + gamePlayed + "#" + gameWon + "#" + money);
                break;
            }
        }
    }

    private void setlogin(String username, String password) throws Exception {
        for(int i=0;i<playerThreads.size ();i++)
        {
            if(playerThreads.get (i).getThisPlayer ().getName ().compareTo (username)==0 && playerThreads.get (i).getThisPlayer ().isLoggedin ())
            {
                outToMyClient.println ("login#false#onno");
                return;
            }
        }
        String cmd = "SELECT * FROM players";
        boolean x = false;
        rs = st.executeQuery (cmd);
        while (rs.next ()) {
            String name = rs.getString ("Name");
            String pass = rs.getString ("Password");
            String id = rs.getString ("FacebookID");
            int money = rs.getInt ("Money");
            if (name.compareTo (username) == 0 && pass.compareTo (password) == 0) {
                outToMyClient.println ("login#" + username + "#" + id+"#"+money);
                thisPlayer= new PlayerData (name,pass,id,money,true);
                x = true;
                break;
            }
        }
        if (!x) {
            outToMyClient.println ("login#false");
        }
    }

    private void setSignup(String username, String pass, String fbID) throws Exception {
        boolean x = true;
        String cmd = "SELECT * FROM players";
        rs = st.executeQuery (cmd);
        while (rs.next ()) {
            String name = rs.getString ("Name");
            if (name.compareTo (username) == 0) {
                outToMyClient.println ("signup#false");
                x = false;
                break;
            }
        }
        if (x) {
            outToMyClient.println ("signup#true");
            String cmd1 = "INSERT INTO players (Name,Password,FacebookID,GamesPlayed,GamesWon,Money) VALUES ('" + username + "','" + pass + "','" + fbID + "', '" + 0 + "','" + 0 + "','" + 100 + "')";
            st.executeUpdate (cmd1);
        }
    }

    public void addThread(PlayerThread pt)
    {
        playerThreads.add (pt);
    }

    public PlayerData getThisPlayer() {
        return thisPlayer;
    }

    public void setThisPlayer(PlayerData thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public PrintWriter getOutToMyClient() {
        return outToMyClient;
    }

    public PrintWriter getOutToClient() {
        return outToClient;
    }

    public void setOutToClient(PrintWriter outToClient) {
        this.outToClient = outToClient;
    }

    public void setOutToMyClient(PrintWriter outToMyClient) {
        this.outToMyClient = outToMyClient;
    }
}
