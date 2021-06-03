package it.polimi.ingsw.message.action_message.development_message;


import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.ArrayList;

public class BuyDevelopmentCardDTO extends ActionMessageDTO {
    private final ArrayList<DevelopmentCard> developmentCardsAvailable;

    public BuyDevelopmentCardDTO(ArrayList<DevelopmentCard> developmentCardsAvailable){
        this.developmentCardsAvailable = developmentCardsAvailable;
    }
    public ArrayList<DevelopmentCard> getDevelopmentCards(){
        return this.developmentCardsAvailable;
    }

    @Override
    public String getRelatedAction() {
        return null;
    }
}
