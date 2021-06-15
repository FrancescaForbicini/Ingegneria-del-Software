package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PickStartingResources extends ClientAction {
    public PickStartingResources(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        PickStartingResourcesDTO pickStartingResourcesDTO = (PickStartingResourcesDTO) clientGameObserverProducer.getPendingTurnDTOs().pop();
        int resourceToPick = pickStartingResourcesDTO.getNumber();
        ArrayList<ResourceType> pickedResources = new ArrayList<>();
        while(resourceToPick>0){
            if(resourceToPick==1){
                view.showMessage("Pick your starting resource: ");
            } else {
                view.showMessage("Pick your first starting resource: ");
            }
            pickedResources.add(view.pickStartingResources());
            resourceToPick--;
        }
        clientConnector.sendMessage(new PickStartingResourcesDTO(pickStartingResourcesDTO.getNumber(), pickedResources));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.remove(this);
    }
}
