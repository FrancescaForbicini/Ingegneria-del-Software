package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProduction implements TurnAction{
    private final ArrayList<DevelopmentCard> developmentCardChosen ;
    private int faithPoints;
    private final ArrayList<AdditionalTradingRule> additionalTradingRulesChosen;
    private final Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    private final ArrayList<ResourceType> outputAnyChosen;
    private final ArrayList<ResourceType> inputAnyChosen;


    public ActivateProduction(ArrayList<DevelopmentCard> developmentCardChosen,ArrayList<AdditionalTradingRule> additionalTradingRulesChosen, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox,ArrayList<ResourceType> inputAnyChosen,ArrayList<ResourceType> outputAnyChosen) {
        this.developmentCardChosen = developmentCardChosen;
        this.additionalTradingRulesChosen = additionalTradingRulesChosen;
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputAnyChosen = inputAnyChosen;
        this.inputFromStrongbox = inputFromStrongbox;
        this.outputAnyChosen = outputAnyChosen;
        faithPoints = 0;
    }


    /**
     * Activates a production from the trading rules chosen by the player
     * @param player the player that chooses the trading rules to activate
     */
    @Override
    public void play (Player player) {
        for (DevelopmentCard developmentCard: developmentCardChosen)
            if (!takeResourcesFrom(player,developmentCard.getTradingRule())){
                //TODO esplodi
            }
        for (AdditionalTradingRule additionalTradingRule: additionalTradingRulesChosen){
            if (!takeResourcesFrom(player,additionalTradingRule.getAdditionalTradingRule())){
                //TODO esplodi
            }
        }
        addFaithPoints(player);
    }

    private void addFaithPoints(Player player){
        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
    }

    private boolean takeResourcesFrom(Player player, TradingRule tradingRule) {
        int amount;
        for (ResourceType resourceType : tradingRule.getInput().keySet()) {
            amount = tradingRule.getInput().get(resourceType);
            if (!resourceType.equals(ResourceType.Any)) {
                if (!RemoveResources.removeResources(amount, resourceType, player, inputFromWarehouse, inputFromStrongbox))
                    return false;
            } else {
                while (amount != 0) {
                    if (!RemoveResources.removeResources(1, inputAnyChosen.get(0), player, inputFromWarehouse, inputFromStrongbox))
                        return false;
                    inputAnyChosen.remove(0);
                    amount--;
                }
            }
        }
        if (!tradingRule.getOutput().isEmpty()) {
            if (!insertOutput(tradingRule, player))
                return false;
        }
        if (tradingRule.getFaithPoints() > 0)
            faithPoints += tradingRule.getFaithPoints();

        return true;
    }

    private boolean insertOutput(TradingRule tradingRule,Player player){
        int amount;
        for (ResourceType resourceType: tradingRule.getOutput().keySet()){
            amount = tradingRule.getOutput().get(resourceType);
            if (resourceType.equals(ResourceType.Any)){
                if (amount < outputAnyChosen.size())
                    return false;
                while (amount != 0){
                    player.getStrongbox().merge(outputAnyChosen.get(0),1,Integer::sum);
                    outputAnyChosen.remove(0);
                    amount --;
                }
            }
            else{
                player.getStrongbox().merge(resourceType,amount,Integer::sum);
            }
        }
        return true;
    }
}
