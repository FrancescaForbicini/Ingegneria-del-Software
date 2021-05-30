package it.polimi.ingsw.message.setup;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class PickStartingResourcesDTO extends MessageDTO {
    public final int number;
    public ArrayList<ResourceType> pickedResources;

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
}
