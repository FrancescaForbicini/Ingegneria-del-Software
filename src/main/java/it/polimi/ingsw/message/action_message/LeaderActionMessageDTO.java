package it.polimi.ingsw.message.action_message;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LeaderActionMessageDTO extends MessageDTO {
    ArrayList<LeaderCard> leaderCardsActivated;

    public void setLeaderCardsActivated(ArrayList<LeaderCard> leaderCardsActivated) {
        this.leaderCardsActivated = leaderCardsActivated;
    }
}
