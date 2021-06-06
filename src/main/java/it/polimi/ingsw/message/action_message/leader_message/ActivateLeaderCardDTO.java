package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.LeaderCard;

public class ActivateLeaderCardDTO extends LeaderActionDTO {
    private final LeaderCard leaderCardsToActive;

    public LeaderCard getLeaderCardsToActive() {
        return leaderCardsToActive;
    }

    public ActivateLeaderCardDTO(LeaderCard leaderCardsToActive) {
        this.leaderCardsToActive = leaderCardsToActive;
    }
}
