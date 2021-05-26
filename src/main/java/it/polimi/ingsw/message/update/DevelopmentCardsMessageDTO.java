package it.polimi.ingsw.message.update;

import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.ArrayList;

public class DevelopmentCardsMessageDTO extends UpdateMessageDTO {
    private ArrayList<DevelopmentCard> availableCards;

    public ArrayList<DevelopmentCard> getAvailableCards() {
        return availableCards;
    }

    public DevelopmentCardsMessageDTO(ArrayList<DevelopmentCard> availableCards) {
        this.availableCards = availableCards;
    }
}
