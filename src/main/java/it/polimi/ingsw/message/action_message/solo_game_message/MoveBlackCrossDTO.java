package it.polimi.ingsw.message.action_message.solo_game_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.faith.FaithTrack;

public class MoveBlackCrossDTO extends SoloTokenDTO {
    private final FaithTrack faithTrack;

    public MoveBlackCrossDTO(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }
}
