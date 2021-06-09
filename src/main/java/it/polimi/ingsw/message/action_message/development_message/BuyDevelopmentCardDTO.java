package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

public class BuyDevelopmentCardDTO extends ActionMessageDTO {
    private final DevelopmentCard card;
    private final int slotID;

    public BuyDevelopmentCardDTO(DevelopmentCard card, int slotID) {
        this.card = card;
        this.slotID = slotID;
    }
    public int getSlotID() {
        return slotID;
    }

    public DevelopmentCard getCard() {
        return card;
    }

    @Override
    public String getRelatedAction() {
        return BuyDevelopmentCard.class.getName();
    }
}
