package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class TakeFromMarketDTO extends TurnActionMessageDTO {
    private ArrayList<ResourceType> resourceAnyToChoose;
    private ArrayList<ResourceType> activeWhiteMarbleConversion;
    private ArrayList<ResourceType> resourcesTaken;

    public ArrayList<ResourceType> getResourceAnyToChoose(){
        return resourceAnyToChoose;
    }

    public ArrayList<ResourceType> getActiveWhiteMarbleConversion(){
        return activeWhiteMarbleConversion;
    }

    public ArrayList<ResourceType> getResourcesTaken(){
        return resourcesTaken;
    }
}
