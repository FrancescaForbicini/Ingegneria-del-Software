package it.polimi.ingsw.view;

/**
 * Sets the username, the ID of the game and the amount of players of the game
 */
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
