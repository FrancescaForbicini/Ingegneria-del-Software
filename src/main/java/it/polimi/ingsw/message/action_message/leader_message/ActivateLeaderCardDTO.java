package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;

public class ActivateLeaderCardDTO extends LeaderActionDTO {
    private final ArrayList<LeaderCard> leaderCardsToActive;

    public ArrayList<LeaderCard> getLeaderCardsToActive() {
        return leaderCardsToActive;
    }

    public ActivateLeaderCardDTO(ArrayList<LeaderCard> leaderCardsToActive) {
        this.leaderCardsToActive = leaderCardsToActive;
    }
}
