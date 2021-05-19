package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class ActiveLeaderCardsDTO extends LeaderActionMessageDTO{
    private ArrayList<LeaderCard> leaderCardsToActive;

    public ArrayList<LeaderCard> getLeaderCardsToActive() {
        return leaderCardsToActive;
    }

    public void setLeaderCardsToActive(ArrayList<LeaderCard> leaderCardsToActive) {
        this.leaderCardsToActive = leaderCardsToActive;
    }

}
