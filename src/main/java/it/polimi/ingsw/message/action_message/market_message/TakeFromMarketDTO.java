package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Map;

public class TakeFromMarketDTO extends ActionMessageDTO {
    private String rc;
    private int line;
    private Map<ResourceType,Integer> resourcesTaken;
    private ArrayList<ResourceType> resourceAnyChosen;
    private Warehouse warehouse;
    private int faithPoints;

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public ArrayList<ResourceType> getResourceAnyChosen() {
        return resourceAnyChosen;
    }

    public void setResourceAnyChosen(ArrayList<ResourceType> resourceAnyChosen) {
        this.resourceAnyChosen = resourceAnyChosen;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Map<ResourceType, Integer> getResourcesTaken() {
        return resourcesTaken;
    }

    public void setResourcesTaken(Map<ResourceType, Integer> resourcesTaken) {
        this.resourcesTaken = resourcesTaken;
    }

    @Override
    public String getRelatedAction() {
        return "TakeFromMarket";
    }
}
