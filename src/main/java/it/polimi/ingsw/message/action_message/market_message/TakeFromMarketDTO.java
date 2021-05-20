package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class TakeFromMarketDTO extends TurnActionMessageDTO {
    private final ArrayList<ResourceType> resourceAnyToChoose;
    private final ArrayList<ResourceType> activeWhiteMarbleConversion;
    private final ArrayList<ResourceType> resourcesTaken;

    public ArrayList<ResourceType> getActiveWhiteMarbleConversion(){
        return activeWhiteMarbleConversion;
    }

    public ArrayList<ResourceType> getResourcesTaken(){
        return resourcesTaken;
    }

    public TakeFromMarketDTO(ArrayList<ResourceType> resourceAnyToChoose, ArrayList<ResourceType> activeWhiteMarbleConversion, ArrayList<ResourceType> resourcesTaken) {
        this.resourceAnyToChoose = resourceAnyToChoose;
        this.activeWhiteMarbleConversion = activeWhiteMarbleConversion;
        this.resourcesTaken = resourcesTaken;
    }
}
