package sample;

public class Player {
    private boolean isMyturn;
    private String name, ID;
    private boolean win;
    private int ballType;//1 will mean solids 2 will mean stripes 0 will mean not set
    private boolean allBallsPotted;
    private int balance;

    public Player() {
        isMyturn = false;
        win = false;
        ballType = 0;
        name = "";
        allBallsPotted = false;
        ID = "";
        balance = 0;
    }

    public Player(String name) {
        isMyturn = false;
        win = false;
        ballType = 0;
        this.name = name;
        allBallsPotted = false;
        ID = "";
        balance = 0;
    }

    public boolean isMyturn() {
        return isMyturn;
    }

    public void setMyturn(boolean myturn) {
        isMyturn = myturn;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setBallType(int ballType) {
        this.ballType = ballType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllBallsPotted() {
        return allBallsPotted;
    }

    public void setAllBallsPotted(boolean allBallsPotted) {
        this.allBallsPotted = allBallsPotted;
    }

    public int getBallType() {
        return ballType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
