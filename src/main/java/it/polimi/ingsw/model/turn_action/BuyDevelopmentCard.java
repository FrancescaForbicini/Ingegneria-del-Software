package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.NoEligiblePlayerException;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.turn_taker.Player;

public class BuyDevelopmentCard implements TurnAction{
    private DevelopmentCard card;
    private int slotID;

    public BuyDevelopmentCard(){
        this.card = null;
        this.slotID = 0;
    }

    public void setCard(DevelopmentCard card){ this.card = card; }
    public void setSlotID(int slotID) { this.slotID = slotID; }

    @Override
    public void play(Player player){
        try{
            this.card.buy(player,this.slotID);
        }catch (NoEligiblePlayerException e){
            System.out.println("Choose another card ");
        }
    }

    @Override
    public boolean isFinished(){
        return !isCardNull() && !isSlotIDZero() ;
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
