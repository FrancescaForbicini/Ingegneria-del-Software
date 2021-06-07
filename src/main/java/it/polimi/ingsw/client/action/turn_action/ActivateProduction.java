package it.polimi.ingsw.client.action.turn_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.update.UpdateMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Map;


public class ActivateProduction extends ClientAction {
    private final Player player;
    private final ActivateProductionDTO activateProductionDTO;
    private TradingRule tradingRuleChosen;

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        activateProductionDTO  = new ActivateProductionDTO();
    }

    @Override
    public boolean isDoable() {
        return tradingRuleChosen != null && tradingRuleChosen.isUsable(player);
    }

    @Override
    public void doAction() {
        ArrayList<TradingRule> tradingRulesAvailable = (ArrayList<TradingRule>) player.getPersonalBoard().getActiveTradingRules();
        do{
            tradingRuleChosen = null;
            chooseTradingRule();
            if (tradingRuleChosen != null && isDoable()) {
                tradingRulesAvailable.remove(tradingRuleChosen);
                if (tradingRuleChosen.getOutput().containsKey(ResourceType.Any))
                    chooseAnyOutput(tradingRuleChosen.getOutput().get(ResourceType.Any));
                if (tradingRuleChosen.getInput().containsKey(ResourceType.Any))
                    chooseAnyInput(tradingRuleChosen.getInput().get(ResourceType.Any));
                inputFromWhere();
            }
            clientConnector.sendMessage(activateProductionDTO);
            clientConnector.receiveMessage(UpdateMessageDTO.class);
        }while (tradingRulesAvailable.size() != 0 && tradingRuleChosen != null );
    }

    private void chooseTradingRule(){
        this.tradingRuleChosen = view.chooseTradingRuleToActivate((ArrayList<TradingRule>) player.getPersonalBoard().getActiveTradingRules());
        activateProductionDTO.setTradingRuleChosen(tradingRuleChosen);
    }

    private void chooseAnyInput(int inputToChoose){
        ArrayList<ResourceType> inputAnyChosen = view.chooseAnyInput(inputToChoose);
        activateProductionDTO.setInputAnyChosen(inputAnyChosen);
    }

    private void chooseAnyOutput(int outputToChoose){
        ArrayList<ResourceType> outputAnyChosen = view.chooseAnyOutput(outputToChoose);
        activateProductionDTO.setOutputAnyChosen(outputAnyChosen);
    }

    private void inputFromWhere(){
        Map <ResourceType,Integer> resourcesChosenFromStrongBox;
        Map <ResourceType,Integer> resourcesChosenFromWarehouse;
        Map <ResourceType,Integer> resourcesToChoose;
        resourcesToChoose = tradingRuleChosen.getInput();
        if (!player.getStrongbox().isEmpty()) {
            resourcesChosenFromStrongBox = view.inputFromStrongbox(resourcesToChoose);
            for (ResourceType resourceType : resourcesToChoose.keySet()) {
                if (resourcesChosenFromStrongBox.containsKey(resourceType))
                    resourcesToChoose.remove(resourceType,resourcesChosenFromStrongBox.get(resourceType));
            }
            activateProductionDTO.setInputChosenFromStrongbox(resourcesChosenFromStrongBox);
        }
        if (!player.getWarehouse().getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty)){
            resourcesChosenFromWarehouse = view.inputFromWarehouse(resourcesToChoose);
            activateProductionDTO.setInputChosenFromWarehouse(resourcesChosenFromWarehouse);
        }
    }
}
