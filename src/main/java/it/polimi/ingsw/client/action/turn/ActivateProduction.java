package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.*;

public class ActivateProduction extends TurnAction {
    private final Player player;
    private ArrayList<DevelopmentCard> developmentCardChosen;
    private final Map<ResourceType,Integer> totalInput;
    private final Map<ResourceType,Integer> totalOutput;
    private ArrayList<ResourceType> inputAnyChosen;
    private ArrayList<ResourceType> outputAnyChosen;

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        totalInput = new HashMap<>();
        totalOutput = new HashMap<>();
    }

    @Override
    public boolean isDoable() {
        return Arrays.stream(player.getPersonalBoard().getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.showCardOnTop().get().getTradingRule().isUsable(player));
    }

    @Override
    public void doAction() {
     ArrayList<DevelopmentCard> developmentCardsAvailable = new ArrayList<>();
      for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player)) {
                developmentCardsAvailable.add(slot.showCardOnTop().get());
            }
        }
        chooseTradingRule(developmentCardsAvailable);
        ArrayList<TradingRule> tradingRulesChosen = new ArrayList<>();
        developmentCardChosen.forEach(developmentCard -> tradingRulesChosen.add(developmentCard.getTradingRule()));
        getTotalInput(tradingRulesChosen);
        getTotalOutput(tradingRulesChosen);
        chooseInputAny();
        chooseOutputAny();
        checkChoice(totalInput);
        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardChosen,inputFromWarehouse(),totalInput,inputFromStrongbox(),totalOutput));
    }


    private void chooseTradingRule(ArrayList<DevelopmentCard> developmentCards){
        developmentCardChosen = view.chooseDevelopmentCards(developmentCards);
    }

    private void chooseInputAny(){
        if (developmentCardChosen.stream().anyMatch(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)))
            inputAnyChosen = view.chooseResourcesAny( (int) developmentCardChosen.stream().filter(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)).count());
    }

    private void chooseOutputAny(){
        if (developmentCardChosen.stream().anyMatch(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)))
            outputAnyChosen = view.chooseResourcesAny( (int) developmentCardChosen.stream().filter(developmentCard -> developmentCard.getTradingRule().getOutput().containsKey(ResourceType.Any)).count());
    }

    private void checkChoice(Map<ResourceType,Integer> input){
        for (DevelopmentCard developmentCard: developmentCardChosen){
            for (ResourceType resourceType: developmentCard.getTradingRule().getInput().keySet()) {
                if (player.getResourceAmount(resourceType) < input.get(resourceType)) {
                    developmentCardChosen.remove(developmentCard);
                    view.showMessage("The trading rule has been discarded because you do not have enough resources \n" +developmentCard.getTradingRule());
                }
                else{
                    input.replace(resourceType,input.get(resourceType),input.get(resourceType)-developmentCard.getTradingRule().getInput().get(resourceType));
                }
            }
        }
    }
    private Map <ResourceType,Integer> inputFromStrongbox(){
        Map <ResourceType,Integer> resourcesChosenFromStrongBox = new HashMap<>();
        Map <ResourceType,Integer> resourcesToChooseFromStrongBox  = new HashMap<>();
        if (!player.getStrongbox().isEmpty()) {
            for (ResourceType resourceType: totalInput.keySet())
                if (player.getStrongbox().containsKey(resourceType)){
                    resourcesToChooseFromStrongBox.put(resourceType,player.getStrongbox().get(resourceType));
                }
            resourcesChosenFromStrongBox = view.inputFromStrongbox(resourcesToChooseFromStrongBox);
            for (ResourceType resourceType : totalInput.keySet()) {
                if (resourcesChosenFromStrongBox.containsKey(resourceType))
                    totalInput.remove(resourceType,resourcesChosenFromStrongBox.get(resourceType));
            }
        }
        return resourcesChosenFromStrongBox;
    }
    private Map <ResourceType,Integer> inputFromWarehouse(){
        Map <ResourceType,Integer> resourcesChosenFromWarehouse = new HashMap<>();
        Map <ResourceType,Integer> resourcesToChooseFromWarehouse = new HashMap<>();
        if (!player.getWarehouse().getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty)){
            for (ResourceType resourceType: totalInput.keySet()){
                if (player.getWarehouse().getQuantity(resourceType) > 0){
                    resourcesToChooseFromWarehouse.put(resourceType,player.getWarehouse().getQuantity(resourceType));
                }
            }
            resourcesChosenFromWarehouse = view.inputFromWarehouse(resourcesToChooseFromWarehouse);
        }
        return resourcesChosenFromWarehouse;
    }
    /**
     * Collects all the input of the trading rules
     */
    protected void getTotalInput(ArrayList<TradingRule> tradingRulesChosen){
        tradingRulesChosen.forEach(tradingRule -> tradingRule.getInput().forEach((resourceType, integer) -> totalInput.merge(resourceType,tradingRule.getInput().get(resourceType),Integer::sum)));
        if (tradingRulesChosen.stream().anyMatch(tradingRule -> tradingRule.getInput().containsKey(ResourceType.Any))){
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
