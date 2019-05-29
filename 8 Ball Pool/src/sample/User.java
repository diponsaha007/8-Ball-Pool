package sample;

public class User {
    private String name;
    private int gamesplayed, gameswon, balance;
    double winpercentage;

    public User(String name, int gamesplayed, int gameswon, int balance) {

        this.name = name;
        this.gamesplayed = gamesplayed;
        this.gameswon = gameswon;
        this.balance = balance;
        if (gamesplayed != 0)
            this.winpercentage = (double) gameswon * 100 / gamesplayed;
        else this.winpercentage = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesplayed() {
        return gamesplayed;
    }

    public void setGamesplayed(int gamesplayed) {
        this.gamesplayed = gamesplayed;
    }

    public int getGameswon() {
        return gameswon;
    }

    public void setGameswon(int gameswon) {
        this.gameswon = gameswon;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public double getWinpercentage() {
        return winpercentage;
    }

    public void setWinpercentage(double winpercentage) {
        this.winpercentage = winpercentage;
    }
}
