package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class PickStartingResources extends ClientAction {
    public PickStartingResources(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        PickStartingResourcesDTO pickStartingResourcesDTO = (PickStartingResourcesDTO) clientGameObserverProducer.getPendingTurnDTOs().pop();
        int resourceToPick = pickStartingResourcesDTO.getNumber();
        ArrayList<ResourceType> pickedResources;
        if (resourceToPick > 0) {
            do {
                if (view.isSceneAlreadySeen()) {
                    view.showMessage("Choose the right amount of resources, please");
                }
                pickedResources = view.pickStartingResources(resourceToPick);
                view.setSceneAlreadySeen(true);
            } while (pickedResources.size() > resourceToPick);
        } else {
            pickedResources = new ArrayList<>();
        }
        pickStartingResourcesDTO = new PickStartingResourcesDTO(resourceToPick, pickedResources);
        clientConnector.sendMessage(pickStartingResourcesDTO);
    }
}
