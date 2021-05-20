package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class DiscardLeaderCardsDTO extends LeaderActionDTO {
    private final ArrayList<LeaderCard> leaderCardsToDiscard;

    public ArrayList<LeaderCard> getLeaderCardsToDiscard() {
        return leaderCardsToDiscard;
    }

    public DiscardLeaderCardsDTO(ArrayList<LeaderCard> leaderCardsToDiscard) {
        this.leaderCardsToDiscard = leaderCardsToDiscard;
    }
}
