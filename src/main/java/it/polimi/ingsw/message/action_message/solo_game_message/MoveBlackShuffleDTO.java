package it.polimi.ingsw.message.action_message.solo_game_message;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.solo_game.SoloToken;

import java.util.ArrayList;

public class MoveBlackShuffleDTO extends SoloTokenDTO {
    private final Deck<SoloToken> soloTokensToPick;
    private final Deck<SoloToken> soloTokensDiscarded;
    private final FaithTrack faithTrack;

    public MoveBlackShuffleDTO(Deck<SoloToken> soloTokensToPick, Deck<SoloToken> soloTokensDiscarded,FaithTrack faithTrack) {
        this.soloTokensToPick = soloTokensToPick;
        this.soloTokensDiscarded = soloTokensDiscarded;
        this.faithTrack = faithTrack;
    }

    public Deck<SoloToken> getSoloTokensToPick() {
        return soloTokensToPick;
    }

    public Deck<SoloToken> getSoloTokensDiscarded() {
        return soloTokensDiscarded;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
}
