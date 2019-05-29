package com.jetbrains;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket (5123);
        Connection c;
        ResultSet rs;
        Statement st;
        Class.forName ("com.mysql.jdbc.Driver");
        c = DriverManager.getConnection ("jdbc:mysql://localhost:3306/8 ball pool", "root", "");
        //System.out.println ("database opened");
        st=c.createStatement ();
        int id=1;
        while (true) {
            Socket s1 = serverSocket.accept ();
            InputStream is1;
            OutputStream os1;
            PrintWriter pr1;
            BufferedReader br1;
            is1 = s1.getInputStream ();
            os1 = s1.getOutputStream ();
            pr1 = new PrintWriter (os1, true);
            br1 = new BufferedReader (new InputStreamReader (is1));
            PlayerThread player1Thread = new PlayerThread ( br1, s1 ,pr1,st);
            player1Thread.addThread (player1Thread);
            Thread t1 = new Thread (player1Thread);
            t1.start ();
            System.out.println (" Client "+id+" is now connected");
            id++;
        }

    }
}

