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
    private int faithPoints;
    private final ArrayList<DevelopmentCard> developmentCardsChosen;
    private final ArrayList<AdditionalTradingRule> additionalTradingRulesChosen;
    private final Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    private final ArrayList<ResourceType> outputAnyChosen;
    private final ArrayList<ResourceType> inputAnyChosen;


    public ActivateProduction(ArrayList<DevelopmentCard> developmentCardsChosen, ArrayList<AdditionalTradingRule> additionalTradingRulesChosen, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox, ArrayList<ResourceType> inputAnyChosen, ArrayList<ResourceType> outputAnyChosen) {
        this.developmentCardsChosen = developmentCardsChosen;
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
        if(isUserInputCorrect()) {
            for (DevelopmentCard developmentCard : developmentCardsChosen) {
                takeResourcesFrom(player, developmentCard.getTradingRule());
                addResourcesTo(player, developmentCard.getTradingRule());
            }
            for (AdditionalTradingRule additionalTradingRule : additionalTradingRulesChosen) {
                takeResourcesFrom(player, additionalTradingRule.getAdditionalTradingRule());
                addResourcesTo(player, additionalTradingRule.getAdditionalTradingRule());
            }
            addFaithPoints(player);
        }
        else {
            //TODO esplodi
        }
    }

    private void addFaithPoints(Player player){
        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
    }

    private boolean takeResourcesFrom(Player player, TradingRule tradingRule) {
        int amountToTake;
        for (ResourceType resourceToTake : tradingRule.getInput().keySet()) {
            amountToTake = tradingRule.getInput().get(resourceToTake);
            if (!resourceToTake.equals(ResourceType.Any)) {
                if (!RemoveResources.removeResources(amountToTake, resourceToTake, player, inputFromWarehouse, inputFromStrongbox))
                    return false;
            } else {
                while (amountToTake != 0) {
                    if (!RemoveResources.removeResources(1, inputAnyChosen.get(0), player, inputFromWarehouse, inputFromStrongbox))
                        return false;
                    inputAnyChosen.remove(0);
                    amountToTake--;
                }
            }
        }
        return true;
    }

    private boolean addResourcesTo(Player player, TradingRule tradingRule){
        if (!tradingRule.getOutput().isEmpty()) {
            if (!insertOutput(player, tradingRule))
                return false;
        }
        if (tradingRule.getFaithPoints() > 0)
            faithPoints += tradingRule.getFaithPoints();
        return true;
    }

    private boolean insertOutput(Player player, TradingRule tradingRule){
        int amountToAdd;
        for (ResourceType resourceType: tradingRule.getOutput().keySet()){
            amountToAdd = tradingRule.getOutput().get(resourceType);
            if (resourceType.equals(ResourceType.Any)){
                if (amountToAdd < outputAnyChosen.size())
                    return false;
                while (amountToAdd != 0){
                    player.getStrongbox().merge(outputAnyChosen.get(0),1,Integer::sum);
                    outputAnyChosen.remove(0);
                    amountToAdd --;
                }
            }
            else{
                player.getStrongbox().merge(resourceType,amountToAdd,Integer::sum);
            }
        }
        return true;
    }

    private boolean isUserInputCorrect(){
        return false;
    }
}
