package it.polimi.ingsw.client.action.starting;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.starting.StartingAction;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Lets pick the starting resources, if any
 */
public class PickStartingResources extends StartingAction {
    public PickStartingResources(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Lets the user choose the starting resources
     * The quantity of the possible resources to pick and the eventual faith points depend
     * on the player's position in the round turning
     *
     */
    @Override
    public void doAction() {
        int resourceToPick = clientGameObserverProducer.getStartingResourceNumber();
        int missToPick = resourceToPick;
        ArrayList<ResourceType> pickedResources = new ArrayList<>();
        while(missToPick>0){
            if(missToPick==1){
                view.showMessage("Pick your starting resource: ");
            } else {
                view.showMessage("Pick your first starting resource: ");
            }
            pickedResources.add(view.pickStartingResources());
            missToPick--;
        }
        clientConnector.sendMessage(new PickStartingResourcesDTO(resourceToPick, pickedResources));
    }
}
