package it.polimi.ingsw.message;

public class LoginMessage extends Message{
    private String username;
    private String gameId;

    public LoginMessage(String username, String gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }
}
