package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class TakeFromMarket extends TurnActionMessageDTO {
    private static final long serialVersionUID = -1970763339913938262L;
    ArrayList<ResourceType> resourceAnyToChoose;
    ArrayList<ResourceType> activeWhiteMarbleConversion;
    ArrayList<ResourceType> resourcesTaken;

    public ArrayList<ResourceType> getResourceAnyToChoose(){
        return this.resourceAnyToChoose;
    }

    public ArrayList<ResourceType> getActiveWhiteMarbleConversion(){
        return this.activeWhiteMarbleConversion;
    }

    public ArrayList<ResourceType> getResourcesTaken(){
        return this.resourcesTaken;
    }
}
