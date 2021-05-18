package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class ChooseDevelopmentCardDTO extends TurnActionMessageDTO {
    DevelopmentCard card;

    public void setCard(DevelopmentCard card) {
        this.card = card;
    }

    public DevelopmentCard getCard() {
        return card;
    }
}
