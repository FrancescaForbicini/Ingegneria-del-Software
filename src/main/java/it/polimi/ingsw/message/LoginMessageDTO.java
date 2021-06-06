package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Settings;

import java.util.Objects;

public class LoginMessageDTO extends MessageDTO {
    public static final LoginMessageDTO LoginFailed = new LoginMessageDTO(null, null);
    private final String username;
    private final String gameId;
    private final Settings customSettings;


    public LoginMessageDTO(String username, String gameId, Settings customSettings) {
        this.username = username;
        this.gameId = gameId;
        this.customSettings = customSettings;
    }

    public LoginMessageDTO(String username, String gameId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginMessageDTO that = (LoginMessageDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(gameId, that.gameId);
    }

}
