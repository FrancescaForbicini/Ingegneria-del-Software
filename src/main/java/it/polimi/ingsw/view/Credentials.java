package it.polimi.ingsw.view;

public class Credentials {
    private final String username;
    private final String gameID;
    private final int maxPlayers;

    public Credentials(String username, String gameID, int maxPlayers) {
        this.username = username;
        this.gameID = gameID;
        this.maxPlayers = maxPlayers;
    }

    public String getUsername() {
        return username;
    }

    public String getGameID() {
        return gameID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
