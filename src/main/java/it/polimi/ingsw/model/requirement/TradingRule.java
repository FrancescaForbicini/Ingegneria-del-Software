package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.Map;

public class TradingRule {
    private Map<ResourceType, Integer> input;
    private Map<ResourceType, Integer> output;
    private int addFaithPoint;

    /**
     *
     * @param input: resource that a player has and wants to change
     * @param output: resource that a player has by changing an other resource
     * @param addFaithPoint: add faith point that depend on a specific Card
     */
    public TradingRule(Map<ResourceType, Integer> input, Map<ResourceType, Integer> output,int addFaithPoint) {
        this.input = input;
        this.output = output;
        this.addFaithPoint = addFaithPoint;
    }
    public Map<ResourceType, Integer> getInput() { return input; }
    public Map<ResourceType, Integer> getOutput() {
        return output;
    }

    public int getAddFaithPoint(){
        return addFaithPoint;
    }

    /**
     * Checks if a player can use a trading rule
     * @param player player that wants use the trading rule
     * @return true if a player can use the trading rule, false if not
     */
    public boolean isUsable (Player player){
         for (ResourceType resourceType: input.keySet()){
             if (input.get(resourceType)<player.getResourceAmount(resourceType))
                 return false;
         }
         return true;
    }

    /**
     * Activates a trading rule for a player
     * @param player that wants activate a trading rule
     */
    public void activate (Player player){
        for (ResourceType resourceType: input.keySet()) {
            //TODO where get resource in input: warehouse or strongbox?
            player.getPersonalBoard().addResourceToStrongbox(resourceType,input.get(resourceType));
        }
    }
}
