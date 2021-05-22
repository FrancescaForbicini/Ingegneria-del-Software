package it.polimi.ingsw.message.action_message.solo_game_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.util.ArrayList;

public class DiscardDevelopmentCardsDTO extends SoloTokenDTO {
    private final ArrayList<DevelopmentCard> developmentCardsAvailable;
    private final ArrayList<DevelopmentCard> developmentCardsToDiscard;

    public DiscardDevelopmentCardsDTO(ArrayList<DevelopmentCard> developmentCardsAvailable,ArrayList<DevelopmentCard> developmentCardsToDiscard){
        this.developmentCardsAvailable = developmentCardsAvailable;
        this.developmentCardsToDiscard = developmentCardsToDiscard;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCardsAvailable() {
        return developmentCardsAvailable;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCardsToDiscard() {
        return developmentCardsToDiscard;
    }
}
