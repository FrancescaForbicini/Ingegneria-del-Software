package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProduction implements TurnAction{
    private  boolean rulesDefined;
    private final ArrayList<DevelopmentCard> developmentCardChosen ;
    private final Map<ResourceType,Integer> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    private final Map<ResourceType,Integer> totalOutput;
    private final Map<ResourceType,Integer> quantityInputFromWarehouse;

    public ActivateProduction(ArrayList<DevelopmentCard> developmentCardChosen,Map<ResourceType,Integer> inputFromWarehouse,Map<ResourceType,Integer> quantityInputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox,Map<ResourceType,Integer> totalOutput) {
        this.developmentCardChosen = developmentCardChosen;
        this.inputFromWarehouse = inputFromWarehouse;
        this.quantityInputFromWarehouse = quantityInputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
        this.totalOutput = totalOutput;
        this.rulesDefined = true;
    }


    @Override
    public boolean isFinished(){ return rulesDefined;}


    /**
     * Activates a production from the trading rules chosen by the player
     * @param player the player that chooses the trading rules to activate
     */
    @Override
    public void play (Player player) {
        addVictoryPoints(player);
        takeResourcesFromWarehouse(player);
        takeResourcesFromStrongbox(player);
        addResourcesToStrongbox(player);
        rulesDefined = true;
    }

    private void addVictoryPoints(Player player){
        int victoryPoints = 0;
        for (DevelopmentCard developmentCard: developmentCardChosen){
            victoryPoints += developmentCard.getVictoryPoints();
        }
        player.addPersonalVictoryPoints(victoryPoints);
    }

    private void takeResourcesFromWarehouse(Player player){
        if (inputFromWarehouse != null){
            for (ResourceType resourceType: inputFromWarehouse.keySet()){
                player.getWarehouse().removeResource(quantityInputFromWarehouse.get(resourceType),inputFromWarehouse.get(resourceType));
            }
        }
    }
    private void takeResourcesFromStrongbox(Player player){
        if (!inputFromStrongbox.isEmpty()){
            for (ResourceType resourceType: inputFromStrongbox.keySet()){
                player.getStrongbox().remove(resourceType,inputFromStrongbox.get(resourceType));
            }
        }
    }

    private void addResourcesToStrongbox(Player player){
        for (ResourceType resourceType: totalOutput.keySet()){
            player.getStrongbox().merge(resourceType,totalOutput.get(resourceType), Integer::sum);
        }
    }
}
