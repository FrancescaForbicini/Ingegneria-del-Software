package it.polimi.ingsw.message.action_message.leader_message;

import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;

public class DiscardLeaderCardsDTO extends LeaderActionDTO {
    private final LeaderCard leaderCardsToDiscard;

    public LeaderCard getLeaderCardToDiscard() {
        return leaderCardsToDiscard;
    }

    public DiscardLeaderCardsDTO(LeaderCard leaderCardsToDiscard) {
        this.leaderCardsToDiscard = leaderCardsToDiscard;
    }
}
