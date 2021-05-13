package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Settings;

import java.util.Optional;

public class LoginMessage extends Message{
    private static final long serialVersionUID = -5596736600796949166L;
    public static final LoginMessage LoginFailed = new LoginMessage(null, null);
    private final String username;
    private final String gameId;
    private final Settings customSettings;  // This is an semantically and Optional but java cannot serialize it properly

    public LoginMessage(String username, String gameId, Settings customSettings) {
        this.username = username;
        this.gameId = gameId;
        this.customSettings = customSettings;

    }

    public LoginMessage(String username, String gameId) {
        this(username, gameId, null);
    }


    public String getUsername() {
        return username;
    }

    public String getGameId() {
        return gameId;
    }

    public Settings getCustomSettings() {
        return customSettings;
    }
}
