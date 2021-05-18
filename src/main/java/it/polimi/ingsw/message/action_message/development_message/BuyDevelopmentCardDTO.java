package it.polimi.ingsw.message.action_message.development_message;


import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.ArrayList;

public class BuyDevelopmentCardDTO extends TurnActionMessageDTO {
    ArrayList<ArrayList<Deck<DevelopmentCard>>> developmentCardsDeck;
    public ArrayList<ArrayList<Deck<DevelopmentCard>>> getDevelopmentCardsDeck(){
        return this.developmentCardsDeck;
    }
}
