package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProduction implements TurnAction{
    private final ArrayList<DevelopmentCard> developmentCardChosen ;
    private final Map<ResourceType,Integer> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    private final ArrayList<ResourceType> outputAnyChosen;
    private final ArrayList<ResourceType> inputAnyChosen;

    public ActivateProduction(ArrayList<DevelopmentCard> developmentCardChosen,Map<ResourceType,Integer> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox,ArrayList<ResourceType> inputAnyChosen,ArrayList<ResourceType> outputAnyChosen) {
        this.developmentCardChosen = developmentCardChosen;
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputAnyChosen = inputAnyChosen;
        this.inputFromStrongbox = inputFromStrongbox;
        this.outputAnyChosen = outputAnyChosen;
    }


    /**
     * Activates a production from the trading rules chosen by the player
     * @param player the player that chooses the trading rules to activate
     */
    @Override
    public void play (Player player) {
        addVictoryPoints(player);
        takeResourcesFrom(player);
    }

    private void addVictoryPoints(Player player){
        int victoryPoints = 0;
        for (DevelopmentCard developmentCard: developmentCardChosen){
            if (developmentCard.getRequirements() != null && developmentCard.getTradingRule().getOutput().containsKey(ResourceType.Any))
                victoryPoints += developmentCard.getTradingRule().getOutput().get(ResourceType.Any);
        }
        player.addPersonalVictoryPoints(victoryPoints);
    }

    private boolean takeResourcesFrom(Player player) {
        int amount;
        for (DevelopmentCard developmentCard : developmentCardChosen){
            for (ResourceType resourceType : developmentCard.getTradingRule().getInput().keySet()) {
                amount = developmentCard.getTradingRule().getInput().get(resourceType);
                if (!resourceType.equals(ResourceType.Any)) {
                    if (!RemoveResources.removeResources(amount,resourceType,player,inputFromWarehouse,inputFromStrongbox))
                        return false;
                }
                else{
                    while (amount != 0){
                        if (!RemoveResources.removeResources(1,inputAnyChosen.get(0),player,inputFromWarehouse,inputFromStrongbox))
                            return false;
                        inputAnyChosen.remove(0);
                        amount --;
                    }
                }
            }
            if (!developmentCard.getTradingRule().getOutput().isEmpty()) {
                if (!insertOutput(developmentCard, player))
                    return false;
            }
            if (developmentCard.getTradingRule().getVictoryPoints() > 0)
                Game.getInstance().getFaithTrack().move(player,developmentCard.getTradingRule().getVictoryPoints());
        }

        return true;
    }

    private boolean insertOutput(DevelopmentCard developmentCard,Player player){
        int amount;
        for (ResourceType resourceType: developmentCard.getTradingRule().getOutput().keySet()){
            amount = developmentCard.getTradingRule().getOutput().get(resourceType);
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
