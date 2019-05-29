package com.jetbrains;

public class PlayerData {
    private String name, pass,fbID;
    private int money;
    private boolean loggedin;
    private boolean isPlaying;

    public PlayerData() {
        this.name = "";
        this.pass = "";
        this.fbID = "";
        this.money = 0;
        this.isPlaying=false;
        this.loggedin=false;
    }

    public PlayerData(String name, String pass, String fbID, int money, boolean loggedin) {
        this.name = name;
        this.pass = pass;
        this.fbID = fbID;
        this.money = money;
        this.loggedin = loggedin;
        this.isPlaying=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
