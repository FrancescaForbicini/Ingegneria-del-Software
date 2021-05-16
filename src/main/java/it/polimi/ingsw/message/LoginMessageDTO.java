package it.polimi.ingsw.message;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.List;

public class LoginMessageDTO extends MessageDTO {
    private static final long serialVersionUID = -5596736600796949166L;
    public static final LoginMessageDTO LoginFailed = new LoginMessageDTO(null, null);
    private final String username;
    private final String gameId;
    private final Settings customSettings;  // This is an semantically and Optional but java cannot serialize it properly
    private final List<LeaderCard> cards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginMessageDTO that = (LoginMessageDTO) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (gameId != null ? !gameId.equals(that.gameId) : that.gameId != null) return false;
        if (customSettings != null ? !customSettings.equals(that.customSettings) : that.customSettings != null)
            return false;
        return cards != null ? cards.equals(that.cards) : that.cards == null;
    }


    public LoginMessageDTO(String username, String gameId, Settings customSettings, List<LeaderCard> cards) {
        this.username = username;
        this.gameId = gameId;
        this.customSettings = customSettings;
        this.cards = cards;

    }

    public LoginMessageDTO(String username, String gameId) {
        this(username, gameId, null, null);
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

    public List<LeaderCard> getCards() {
        return cards;
    }
}
