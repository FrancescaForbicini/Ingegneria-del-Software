package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivateProduction implements TurnAction{
    private boolean rulesDefined;
    private Map<ResourceType,Integer> totalInput;
    private Map<ResourceType,Integer> totalOutput;
    private Map<ResourceType,Integer> inputFromWarehouse;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private int faithPoints;

    public ActivateProduction() {
        this.rulesDefined = true;
        this.totalInput = null;
        this.totalOutput = null;
        this.inputFromWarehouse = null;
        this.inputFromStrongbox = null;
        this.faithPoints = -1;
    }

    /**
     * Sets the trading rules that have been chosen
     * @param chosenTradingRules the trading rules chosen
     */
    public void setChosenTradingRules(ArrayList <TradingRule> chosenTradingRules){
        faithPoints = (int) chosenTradingRules.stream().mapToInt(TradingRule::getFaithPoints).count();
        getTotalInput(chosenTradingRules);
        getTotalOutput(chosenTradingRules);
    }

    /**
     * Sets the input that has not a specific resource
     * @param chosenInputAny the resources chosen for the input if it is any
     */
    public void setInputAny(ArrayList <ResourceType> chosenInputAny ){
        for (ResourceType resourceType: chosenInputAny){
            totalInput.replace(resourceType,totalInput.get(resourceType)+1);
        }
    }

    /**
     * Sets the output that has not a specific resource
     * @param chosenOutputAny the resources chosen for the output if it is any
     */
    public void setOutputAny(ArrayList<ResourceType> chosenOutputAny ){
        for (ResourceType resourceType: chosenOutputAny){
            totalOutput.replace(resourceType,totalOutput.get(resourceType)+1);
        }
    }

    public boolean areTradingRulesChosen(){
        return totalInput != null && totalOutput != null && faithPoints != -1;
    }

    public boolean areRulesDefined (){ return rulesDefined;}

    @Override
    public boolean isFinished(){ return areTradingRulesChosen() && !areRulesDefined(); }

    public boolean isWhereFromChosen(){
        return inputFromWarehouse != null && inputFromStrongbox != null;
    }

    /**
     *
     * @param inputFromWarehouse mapping between the resource and the depot ID od the warehouse
     * @param inputFromStrongbox mapping between the resource and the quantity to take from the strongbox
     */
    public void setInputFrom(Map<ResourceType,Integer> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox){
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
    }
    @Override
    public void play (Player player) {
        for (ResourceType resourceType : inputFromWarehouse.keySet()){
            player.getPersonalBoard().removeResourceFromWarehouse(resourceType,1,inputFromWarehouse.get(resourceType));
        }
        //TODO choose boolean vs exception
        for (ResourceType resourceType : inputFromStrongbox.keySet()){
            try {
                player.getPersonalBoard().removeResourceFromStrongbox(resourceType,inputFromStrongbox.get(resourceType));
            } catch (NotEnoughResourcesException e) {
                e.printStackTrace();
            }
        }
        for (ResourceType resourceType : totalOutput.keySet()){
            player.getPersonalBoard().addResourceToStrongbox(resourceType,totalOutput.get(resourceType));
        }

        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
    }
    //TODO move usableTradingRules to controller
    /*
    /**
     * Gets all the trading rules that a player can use
     * @param player
     * @return
     */
    /*private Collection <TradingRule> usableTradingRules(Player player){
        return player.getPersonalBoard().getActiveTradingRules().stream().filter(tradingRule -> tradingRule.isUsable(player)).collect(Collectors.toList());
    }*/

    /**
     * Collects all the input of the trading rules
     * @param tradingRules the trading rules chosen to count the resource to take from the strongbox or the warehouse
     */
    private void getTotalInput(Collection <TradingRule> tradingRules){
        tradingRules.forEach(tradingRule -> totalInput.putAll(tradingRule.getInput()));
    }

    /**
     * Collects all the output of the trading rules
     * @param tradingRules the trading rules chosen to count the resource to put in the strongbox
     */
    private void getTotalOutput(Collection <TradingRule> tradingRules){
        tradingRules.forEach(tradingRule -> totalOutput.putAll(tradingRule.getOutput()));
    }
}
