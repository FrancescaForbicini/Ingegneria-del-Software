package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Settings;

import java.util.Objects;

public class LoginMessageDTO extends MessageDTO {
    public static final LoginMessageDTO LoginFailed = new LoginMessageDTO(null, null, false);
    private final String username;
    private final String gameId;
    private final Settings customSettings;
    private final int maxPlayers;
    private boolean custom;


    public LoginMessageDTO(String username, String gameId, Settings customSettings, int maxPlayers, boolean custom) {
        this.username = username;
        this.gameId = gameId;
        this.customSettings = customSettings;
        this.maxPlayers = maxPlayers;
        this.custom = custom;
    }

    public LoginMessageDTO(String username, String gameId, boolean custom) {
        this(username, gameId, null, 0, custom);
    }

    public int getMaxPlayers() {
        return maxPlayers;
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

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom){
        this.custom = custom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginMessageDTO that = (LoginMessageDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(gameId, that.gameId);
    }

}
