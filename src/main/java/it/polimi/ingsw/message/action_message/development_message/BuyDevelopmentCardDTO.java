package it.polimi.ingsw.message.action_message.development_message;


import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.ArrayList;

public class BuyDevelopmentCardDTO extends TurnActionMessageDTO {
    private final ArrayList<DevelopmentCard> developmentCardsAvailable;

    public BuyDevelopmentCardDTO(ArrayList<DevelopmentCard> developmentCardsAvailable){
        this.developmentCardsAvailable = developmentCardsAvailable;
    }
    public ArrayList<DevelopmentCard> getDevelopmentCards(){
        return this.developmentCardsAvailable;
    }
}
