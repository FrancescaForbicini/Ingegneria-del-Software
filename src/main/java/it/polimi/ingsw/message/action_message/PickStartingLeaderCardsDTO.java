package it.polimi.ingsw.message.action_message;

import it.polimi.ingsw.client.action.starting.PickStartingLeaderCards;
import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;

import java.util.List;

public class PickStartingLeaderCardsDTO extends ActionMessageDTO{

    private final List<LeaderCard> cards;

    public PickStartingLeaderCardsDTO(List<LeaderCard> cards) {
        this.cards = cards;
    }

    public List<LeaderCard> getCards() {
        return cards;
    }

    @Override
    public String getRelatedAction() {
        return PickStartingLeaderCards.class.getName();
    }
}
