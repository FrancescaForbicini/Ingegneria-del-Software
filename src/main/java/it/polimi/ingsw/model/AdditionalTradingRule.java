package it.polimi.ingsw.model;

import java.util.Collection;

public class AdditionalTradingRule extends LeaderCardStrategy{
    private TradingRule additionalTradingRule;
    /**
     *
     * @param victoryPoints : used to specify the victory points
     * @param resourceType : used to specify the resources that are needed to activate the card
     * @param requirement: used to verify if a player has the right resources to use the card
     * @param additionalTradingRule: used to add a TradingRule to a player
     */
    public AdditionalTradingRule(int victoryPoints, ResourceType resourceType, Collection<Requirement> requirement, TradingRule additionalTradingRule) {
        super(victoryPoints, resourceType,requirement);
        this.additionalTradingRule = additionalTradingRule;
    }

    public TradingRule getAdditionalTradingRule() {
        return additionalTradingRule;
    }
    /**Check if a player has the requirements to activate an additional TradingRule
     *
     * @param player: used to specify the player that wants activate a card
     */
    @Override
    public void activate(Player player){
        if (isEligible(player)){
            player.addTradingRule(additionalTradingRule);
        }
    }
}
