package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class ChooseDevelopmentCard extends TurnActionMessageDTO {
    private static final long serialVersionUID = 2716859524410341240L;
    DevelopmentCard card;

    public void setCard(DevelopmentCard card) {
        this.card = card;
    }

    public DevelopmentCard getCard() {
        return card;
    }
}
