package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class DiscardLeaderCardsDTO extends LeaderActionMessageDTO{
    private ArrayList<LeaderCard> leaderCardsToDiscard;

    public void setLeaderCardsToDiscard(ArrayList<LeaderCard> leaderCardsToDiscard) {
        this.leaderCardsToDiscard = leaderCardsToDiscard;
    }

    public ArrayList<LeaderCard> getLeaderCardsToDiscard() {
        return leaderCardsToDiscard;
    }
}
