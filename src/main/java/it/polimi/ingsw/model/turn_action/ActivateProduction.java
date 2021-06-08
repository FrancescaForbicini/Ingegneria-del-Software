package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.NotEnoughResourcesException;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ActivateProduction implements TurnAction{
    private  boolean rulesDefined;
    private ArrayList<DevelopmentCard> developmentCardChosen ;
    private Map<ResourceType,Integer> totalInput;
    private Map<ResourceType,Integer> totalOutput;
    private Map<ResourceType,Integer> inputFromWarehouse;
    private Map<ResourceType,Integer> inputFromStrongbox;
    private ArrayList<ResourceType> outputAnyChosen;
    private ArrayList<ResourceType> inputAnyChosen;
    private int faithPoints;

    public ActivateProduction(ArrayList<DevelopmentCard> developmentCardChosen, ArrayList<ResourceType> inputAnyChosen, ArrayList<ResourceType> outputAnyChosen, Map<ResourceType,Integer> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox) {
        this.developmentCardChosen = developmentCardChosen;
        this.inputAnyChosen = inputAnyChosen;
        this.outputAnyChosen = outputAnyChosen;
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
        this.rulesDefined = true;
        faithPoints = 0;
    }

    /**
     * Sets the trading rules that have been chosen
     * @param chosenTradingRules the trading rules chosen
     */
    public void setChosenTradingRules(ArrayList <TradingRule> chosenTradingRules){
        faithPoints = (int) chosenTradingRules.stream().mapToInt(TradingRule::getVictoryPoints).count();
        getTotalInput(chosenTradingRules);
        getTotalOutput(chosenTradingRules);
    }

    /**
     * Sets the input that has not a specific resource
     * @param chosenInputAny the resources chosen for the input if it is any
     */
    public void setInputAny(ArrayList <ResourceType> chosenInputAny ){
        this.inputAnyChosen = chosenInputAny;
    }

    /**
     * Sets the output that has not a specific resource
     * @param chosenOutputAny the resources chosen for the output if it is any
     */
    public void setOutputAny(ArrayList<ResourceType> chosenOutputAny ){
        this.outputAnyChosen = chosenOutputAny;
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

    /**
     * Activates a production from the trading rules chosen by the player
     * @param player the player that chooses the trading rules to activate
     */

    @Override
    public void play (Player player) {
        convertInputToOutput(player);
        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
    }

    /**
     * Converts the resources needed to activate the trading rules and convert them to resources to put in the strongbox
     * @param player the player that wants to use its resource to activate a production of trading rules
     */
    public void convertInputToOutput(Player player){
        ArrayList<WarehouseDepot> depot;
        for (ResourceType resourceType : inputFromWarehouse.keySet()){
            //selects the right depot
            depot = player.getPersonalBoard().getWarehouse().findDepotsByType(player.getPersonalBoard().getWarehouse().getWarehouseDepots(),resourceType);
            player.getPersonalBoard().removeResourceFromWarehouse(resourceType,inputFromWarehouse.get(resourceType),depot.get(0).getDepotID());
        }

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
    }

    /**
     * Collects all the input of the trading rules
     * @param tradingRules the trading rules chosen to count the resource to take from the strongbox or the warehouse
     */
    protected void getTotalInput(Collection <TradingRule> tradingRules){
        tradingRules.forEach(tradingRule -> tradingRule.getInput().forEach((resourceType, integer) -> totalInput.merge(resourceType,tradingRule.getInput().get(resourceType),Integer::sum)));
        if (tradingRules.stream().anyMatch(tradingRule -> tradingRule.getInput().containsKey(ResourceType.Any))){
            for (ResourceType resourceType: inputAnyChosen){
                totalInput.merge(resourceType,1,Integer::sum);
                totalInput.remove(ResourceType.Any,1);
            }
        }
    }

    /**
     * Collects all the output of the trading rules
     * @param tradingRules the trading rules chosen to count the resource to put in the strongbox
     */
    protected void getTotalOutput(Collection <TradingRule> tradingRules){
        tradingRules.forEach(tradingRule -> tradingRule.getOutput().forEach((resourceType, integer) -> totalOutput.merge(resourceType,tradingRule.getOutput().get(resourceType),Integer::sum)));
        if(tradingRules.stream().anyMatch(tradingRule -> tradingRule.getOutput().containsKey(ResourceType.Any))) {
            for (ResourceType resourceType: outputAnyChosen){
                totalOutput.merge(resourceType,1,Integer::sum);
                totalOutput.remove(ResourceType.Any,1);
            }
        }
    }
}
