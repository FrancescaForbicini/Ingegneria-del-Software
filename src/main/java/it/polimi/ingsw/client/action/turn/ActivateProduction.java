package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivateProduction extends TurnAction {
    private final Player player;
    private ActivateProductionDTO activateProductionDTO;
    private ArrayList<DevelopmentCard> developmentCardChosen;

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
//        activateProductionDTO  = new ActivateProductionDTO();
    }

    @Override
    public boolean isDoable() {
        return Arrays.stream(player.getPersonalBoard().getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.showCardOnTop().get().getTradingRule().isUsable(player));
    }

    @Override
    public void doAction() {
//        ArrayList<DevelopmentCard> developmentCardsAvailable = new ArrayList<>();
//        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
//            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player)) {
//                developmentCardsAvailable.add(slot.showCardOnTop().get());
//            }
//        }
//        developmentCardChosen = chooseTradingRule(developmentCardsAvailable);
//        if (developmentCardChosen.stream().anyMatch(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)))
//            activateProductionDTO.setInputAnyChosen(view.chooseResourcesAny( (int) developmentCardChosen.stream().filter(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)).count()));
//        if (developmentCardChosen.stream().anyMatch(developmentCard -> developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)))
//            activateProductionDTO.setOutputAnyChosen(view.chooseResourcesAny( (int) developmentCardChosen.stream().filter(developmentCard -> developmentCard.getTradingRule().getOutput().containsKey(ResourceType.Any)).count()));
//        inputFromWhere();
//        clientConnector.sendMessage(activateProductionDTO);
    }


    private ArrayList<DevelopmentCard> chooseTradingRule(ArrayList<DevelopmentCard> developmentCards){
        this.developmentCardChosen = view.chooseDevelopmentCards(developmentCards);
//        activateProductionDTO.setDevelopmentCardChosen(developmentCardChosen);
        return developmentCardChosen;
    }

    private void inputFromWhere(){
//        Map <ResourceType,Integer> resourcesChosenFromStrongBox;
//        Map <ResourceType,Integer> resourcesToChoose = new HashMap<>();
//        for (DevelopmentCard developmentCard: developmentCardChosen){
//            developmentCard.getTradingRule().getInput().forEach((resourceType,i) -> resourcesToChoose.replace(resourceType,resourcesToChoose.get(resourceType),i+resourcesToChoose.get(resourceType)));
//        }
//        if (!player.getStrongbox().isEmpty()) {
//            resourcesChosenFromStrongBox = view.inputFromStrongbox(resourcesToChoose);
//            for (ResourceType resourceType : resourcesToChoose.keySet()) {
//                if (resourcesChosenFromStrongBox.containsKey(resourceType))
//                    resourcesToChoose.remove(resourceType,resourcesChosenFromStrongBox.get(resourceType));
//            }
//            activateProductionDTO.setInputChosenFromStrongbox(resourcesChosenFromStrongBox);
//        }
//        if (!player.getWarehouse().getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty)){
//            activateProductionDTO.setInputChosenFromWarehouse(view.inputFromWarehouse(resourcesToChoose));
//        }
    }
}
