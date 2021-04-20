package it.polimi.ingsw.model.turn_action_strategy;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.NoEligiblePlayerException;
import it.polimi.ingsw.model.turn_taker.Player;

public class BuyDevelopmentCard implements TurnActionStrategy{

    @Override
    public void play(Player player){
        DevelopmentCard card;
        //choose the card
        try{
            card.buy(player);
        }catch (NoEligiblePlayerException e){
            System.out.println("Choose another card");
        }
    }
}
