package it.polimi.ingsw.message.action_message;

import it.polimi.ingsw.client.action.PickLeaderCards;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.List;

public class PickLeaderCardsDTO extends ActionMessageDTO{

    private final List<LeaderCard> cards;

    public PickLeaderCardsDTO(List<LeaderCard> cards) {
        this.cards = cards;
    }

    public List<LeaderCard> getCards() {
        return cards;
    }

    @Override
    public String getRelatedAction() {
        return PickLeaderCards.class.getName();
    }
}
