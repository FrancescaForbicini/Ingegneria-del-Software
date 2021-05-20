package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.ArrayList;

public class TakeFromMarketDTO extends TurnActionMessageDTO {
    private final ArrayList<ResourceType> resourcesAnyToChoose;
    private final ArrayList<ResourceType> activeWhiteMarbleConversion;
    private final ArrayList<ResourceType> resourcesTaken;
    private final Warehouse warehouse;

    public ArrayList<ResourceType> getActiveWhiteMarbleConversion(){
        return activeWhiteMarbleConversion;
    }

    public ArrayList<ResourceType> getResourcesAnyToChoose(){
        return resourcesTaken;
    }

    public ArrayList<ResourceType> getResourcesTaken() {
        return resourcesTaken;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public TakeFromMarketDTO(ArrayList<ResourceType> resourcesAnyToChoose, ArrayList<ResourceType> activeWhiteMarbleConversion, ArrayList<ResourceType> resourcesTaken, Warehouse warehouse) {
        this.resourcesAnyToChoose = resourcesAnyToChoose;
        this.activeWhiteMarbleConversion = activeWhiteMarbleConversion;
        this.resourcesTaken = resourcesTaken;
        this.warehouse = warehouse;
    }
}
