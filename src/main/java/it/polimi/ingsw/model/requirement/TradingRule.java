package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.requirement.ResourceType;

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
}
