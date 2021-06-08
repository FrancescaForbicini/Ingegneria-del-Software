package it.polimi.ingsw.client.action.turn_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.update.UpdateMessageDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class ActivateProduction extends ClientAction {
    private final Player player;
    private final ActivateProductionDTO activateProductionDTO;
    private DevelopmentCard developmentCardChosen;

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        activateProductionDTO  = new ActivateProductionDTO();
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
        do{
            developmentCardChosen = chooseTradingRule(developmentCardsAvailable);
            if (developmentCardChosen != null) {
                activateProductionDTO.setDevelopmentCardChosen(developmentCardChosen);
                developmentCardsAvailable.remove(developmentCardChosen);
                if (developmentCardChosen.getTradingRule().getOutput().containsKey(ResourceType.Any))
                    chooseAnyOutput(developmentCardChosen.getTradingRule().getOutput().get(ResourceType.Any));
                if (developmentCardChosen.getTradingRule().getInput().containsKey(ResourceType.Any))
                    chooseAnyInput(developmentCardChosen.getTradingRule().getInput().get(ResourceType.Any));
                inputFromWhere();
                clientConnector.sendMessage(activateProductionDTO);
                clientConnector.receiveMessage(UpdateMessageDTO.class);
            }
        }while (developmentCardsAvailable.size() != 0 && developmentCardChosen != null );
    }

    private DevelopmentCard chooseTradingRule(ArrayList<DevelopmentCard> developmentCards){
        this.developmentCardChosen = view.chooseTradingRuleToActivate(developmentCards);
        activateProductionDTO.setTradingRuleChosen(developmentCardChosen.getTradingRule());
        return developmentCardChosen;
    }

    private void chooseAnyInput(int inputToChoose){
        view.showMessage("You have to decide the input of this trading rule");
        activateProductionDTO.setInputAnyChosen(view.chooseResourcesAny(inputToChoose));
    }

    private void chooseAnyOutput(int outputToChoose){
        view.showMessage("You have to decide the output of this trading rule ");
        activateProductionDTO.setOutputAnyChosen(view.chooseResourcesAny(outputToChoose));
    }

    private void inputFromWhere(){
        Map <ResourceType,Integer> resourcesChosenFromStrongBox;
        Map <ResourceType,Integer> resourcesToChoose;
        resourcesToChoose = developmentCardChosen.getTradingRule().getInput();
        if (!player.getStrongbox().isEmpty()) {
            resourcesChosenFromStrongBox = view.inputFromStrongbox(resourcesToChoose);
            for (ResourceType resourceType : resourcesToChoose.keySet()) {
                if (resourcesChosenFromStrongBox.containsKey(resourceType))
                    resourcesToChoose.remove(resourceType,resourcesChosenFromStrongBox.get(resourceType));
            }
            activateProductionDTO.setInputChosenFromStrongbox(resourcesChosenFromStrongBox);
        }
        if (!player.getWarehouse().getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty)){
            activateProductionDTO.setInputChosenFromWarehouse(view.inputFromWarehouse(resourcesToChoose));
        }
    }
}
