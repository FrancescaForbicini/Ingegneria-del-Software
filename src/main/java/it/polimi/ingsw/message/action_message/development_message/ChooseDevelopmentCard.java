package it.polimi.ingsw.message.action_message.development_message;

import Message.ActionMessage.ActionMessage;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class ChooseDevelopmentCard extends ActionMessage {
    DevelopmentCard card;

    public void setCard(DevelopmentCard card) {
        this.card = card;
    }

    public DevelopmentCard getCard() {
        return card;
    }
}
