package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class ChooseDevelopmentCardDTO extends ActionMessageDTO {
    private final DevelopmentCard card;

    public ChooseDevelopmentCardDTO (DevelopmentCard card) {
        this.card = card;
    }

    public DevelopmentCard getCard() {
        return card;
    }

    @Override
    public String getRelatedAction() {
        return null;
    }
}
