package it.polimi.ingsw.view.gui;

public class LoginScene {
    private String username;
    private String GameID;
    private String IP;

    public String getIP() {
        return IP;
    }
    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameID(String gameID) {
        GameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return GameID;
    }
}
