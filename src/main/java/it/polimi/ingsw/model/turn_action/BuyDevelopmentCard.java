package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Player;

public class BuyDevelopmentCard implements TurnAction{
    private final DevelopmentCard card;
    private final int slotID;
    private boolean isBought;

    public BuyDevelopmentCard(DevelopmentCard card, int slotID){
        this.card = card;
        this.slotID = slotID;
        this.isBought = false;
    }
    /**
     * Buys a card if the player has the right requirements
     * @param player the player that wants buy the development card
     */
    @Override
    public void play(Player player){
        if (card.buy(player,this.slotID))
            isBought = true;
        else
            isBought = false;
    }

    /**
     * Checks if the turn is finish
     * @return true if the turn is finished : the card is chosen and it is put in the right slot, false if not
     */
    @Override
    public boolean isFinished(){
        return isBought;
    }

    /**
     * Checks if the card is null
     * @return true if the card is null, false if not
     */
    public boolean isCardNull(){
        return this.card == null;
    }

    /**
     * Checks if the slot is zero
     * @return true if the slot is zero, false if not
     */
    public boolean isSlotIDZero(){
        return this.slotID == 0;
    }
}
