package it.polimi.ingsw.message.action_message;

import it.polimi.ingsw.client.action.starting.PickStartingResources;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class PickStartingResourcesDTO extends ActionMessageDTO {
    private final int number;
    private ArrayList<ResourceType> pickedResources;

    public int getNumber() {
        return number;
    }

    public ArrayList<ResourceType> getPickedResources() {
        return pickedResources;
    }

    public PickStartingResourcesDTO(int number, ArrayList<ResourceType> pickedResources) {
        this.number = number;
        this.pickedResources = pickedResources;
    }

    @Override
    public String getRelatedAction() {
        return PickStartingResources.class.getName();
    }
}
