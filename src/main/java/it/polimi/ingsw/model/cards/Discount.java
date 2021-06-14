package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Collection;


public class Discount extends LeaderCard {
    private final int amount;
    private final ResourceType resourceType;

    /**
     * Allows a player to buy a leader card with a discount
     * @param victoryPoints the victory points that the card gives to the player
     * @param resourceType the resource of the discount
     * @param amount the number of resource of the discount
     * @param requirements the resource needed to activate the discount
     */
    public Discount(int victoryPoints, ResourceType resourceType, Integer amount, Collection<Requirement> requirements) {
        super(requirements, victoryPoints);
        this.amount=amount;
        this.resourceType = resourceType;
    }

    public int getAmount(){
        return this.amount;
    }
    public ResourceType getResourceType(){
        return resourceType;
    }
    /**
     * Checks if a player has the requirements to activate a discount
     * @param player the player on which the card is activated
     * @throws NoEligiblePlayerException catch if the player has not the right requirements to active the card
     */
    @Override
    public boolean activate(Player player) throws NoEligiblePlayerException {
        if (super.activate(player)) {
            player.addDiscount(this.getResourceType(), this.amount);
            player.addPersonalVictoryPoints(victoryPoints);
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return super.toString() + "\nDiscount: -" + amount + " " + resourceType.convertColor();
    }
}
